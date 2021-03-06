package vn.fintechviet.user.service;

import vn.fintechviet.content.model.MobileUserInterestItems;
import vn.fintechviet.user.dto.Message;
import vn.fintechviet.user.dto.Reward;
import vn.fintechviet.user.model.UserLuckyNumber;
import vn.fintechviet.user.repository.UserRepository;
import vn.fintechviet.utils.CommonUtils;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;


public class UserService {
	private final UserRepository userRepository;
	private final HttpExecutionContext ec;

	@Inject
	public UserService(UserRepository userRepository, HttpExecutionContext ec){
		this.userRepository = userRepository;
		this.ec = ec;
	}

	public CompletionStage<String> updateUserInfo(String deviceToken, String email, String gender, int dob, String location, String inviteCode) {
		return userRepository.updateUserInfo(deviceToken, email, gender, dob, location, inviteCode);
	}

	public CompletionStage<String> registerUser(String deviceToken) {
		return userRepository.registerUser(deviceToken);
	}


	public CompletionStage<String> updateReward(String deviceToken, String rewardCode, long point){
		return userRepository.updateReward(deviceToken, rewardCode, point);
	}

	public CompletionStage<vn.fintechviet.user.dto.User> getUserInfo(String deviceToken){
		return userRepository.getUserInfo(deviceToken).thenApplyAsync(user -> {
			return (user != null)? buildUserInfo(user) : null;
		}, ec.current());
	}

	private vn.fintechviet.user.dto.User buildUserInfo(vn.fintechviet.user.model.User usr) {
		vn.fintechviet.user.dto.User user = new vn.fintechviet.user.dto.User();
		user.setEmail(usr.getUsername());
		user.setGender(usr.getGender());
		user.setDob(usr.getDob());
		user.setLocation(usr.getLocation());
		user.setEarning(CommonUtils.convertLongToString(usr.getEarning()));
		user.setInviteCode(usr.getInviteCode());
		user.setInviteCodeUsed(usr.getInviteCodeUsed());
		return user;
	}

	private List<Reward> buildRewardInfo(List<Object[]> rewardObjs) {
		List<Reward> rewards = new ArrayList<Reward>();
		for (Object row[] : rewardObjs) {
			String rewardCode = (String)row[0];
			String rewardName = (String)row[1];
			long amount = (long)row[2];
			Reward reward = new Reward();
			reward.setRewardCode(rewardCode);
			reward.setRewardName(rewardName);
			reward.setAmount(CommonUtils.convertLongToString(amount));
			rewards.add(reward);
		}
		return rewards;
	}

	private List<Message> buildMessages(List<vn.fintechviet.user.model.Message> messages) {
		List<Message> messageDTOs = new ArrayList<Message>();
		for (vn.fintechviet.user.model.Message message : messages) {
			Message mess = new Message();
			mess.setId(message.getId());
			mess.setSubject(message.getSubject());
			mess.setBody(message.getBody());
			mess.setRead(Byte.valueOf(message.getRead()));
			mess.setCreatedDate(message.getCreatedDate());
			messageDTOs.add(mess);
		}
		return messageDTOs;
	}

	public CompletionStage<List<Reward>> getRewardInfo(String deviceToken){
		return userRepository.getRewardInfo(deviceToken).thenApplyAsync(rewards -> {
			return buildRewardInfo(rewards);
		}, ec.current());
	}

	public CompletionStage<String> getRedeemPoint(String deviceToken){
		return userRepository.getRedeemPoint(deviceToken).thenApplyAsync(redeem -> {
			return CommonUtils.convertLongToString(redeem);
		}, ec.current());
	}

	public CompletionStage<List<MobileUserInterestItems>> updateUserInterest(String deviceToken, String interests){
		Long uid = userRepository.getUserIdByDeviceToken(deviceToken);
		//build Interest object
		List<MobileUserInterestItems> interestsList = new ArrayList<>();
		for(String cateId: Arrays.asList(interests.split(","))){
			MobileUserInterestItems interest = new MobileUserInterestItems(uid, Long.valueOf(cateId));
			interestsList.add(interest);
		}
		return supplyAsync(() -> userRepository.updateUserInterest(deviceToken, interestsList)) ;
	}

	public CompletionStage<String> updateInviteCode(String deviceToken, String inviteCode){
		return userRepository.updateInviteCode(deviceToken, inviteCode);
	}

	public CompletionStage<List<UserLuckyNumber>> getUserLuckyNumberByToken(String deviceToken){
		return userRepository.getUserLuckyNumberByToken(deviceToken);
	}

	public CompletionStage<List<Message>> getMessages(String deviceToken){
		return userRepository.getMessages(deviceToken).thenApplyAsync(messages -> {
			return buildMessages(messages);
		}, ec.current());
	}

	public CompletionStage<List<Message>> getNewMessages(String deviceToken){
		return userRepository.getNewMessages(deviceToken).thenApplyAsync(messages -> {
			return buildMessages(messages);
		}, ec.current());
	}

	public CompletionStage<String> updateMessage(long messageId, String status){
		return userRepository.updateMessage(messageId, status);
	}
}
