package com.fintechviet.user.service;

import javax.inject.Inject;

import com.fintechviet.content.model.MobileUserInterestItems;
import com.fintechviet.user.dto.Reward;
import com.fintechviet.user.respository.UserRepository;
import play.libs.concurrent.HttpExecutionContext;

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

	public CompletionStage<String> updateUserInfo(String deviceToken, String email, String gender, int dob, String location) {
		return userRepository.updateUserInfo(deviceToken, email, gender, dob, location);
	}

	public CompletionStage<String> updateReward(String deviceToken, String event, long point){
		return userRepository.updateReward(deviceToken, event, point);
	}

	public CompletionStage<com.fintechviet.user.dto.User> getUserInfo(String deviceToken){
		return userRepository.getUserInfo(deviceToken).thenApplyAsync(user -> {
			return new com.fintechviet.user.dto.User(user.getUsername(), user.getGender(), user.getDob(), user.getLocation(), user.getEarning(), user.getInviteCode());
		}, ec.current());
	}

	private List<Reward> buildRewardInfo(List<Object[]> rewardObjs) {
		List<Reward> rewards = new ArrayList<Reward>();
		for (Object row[] : rewardObjs) {
			String event = (String)row[0];
			long amount = (long)row[1];
			Reward reward = new Reward();
			reward.setEvent(event);
			reward.setAmount(amount);
			rewards.add(reward);
		}
		return rewards;
	}

	public CompletionStage<List<Reward>> getRewardInfo(String deviceToken){
		return userRepository.getRewardInfo(deviceToken).thenApplyAsync(rewards -> {
			return buildRewardInfo(rewards);
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

}
