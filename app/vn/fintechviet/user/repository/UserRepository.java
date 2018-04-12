package vn.fintechviet.user.repository;

import vn.fintechviet.content.model.MobileUserInterestItems;
import vn.fintechviet.user.model.Message;
import vn.fintechviet.user.model.User;
import vn.fintechviet.user.model.UserLuckyNumber;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JPAUserRepository.class)
public interface UserRepository{
    CompletionStage<String> updateUserInfo(String deviceToken, String username, String gender, int dob, String location, String inviteCode);
    CompletionStage<String> registerUser(String deviceToken);
    CompletionStage<String> updateReward(String deviceToken, String rewardCode, long point);
    CompletionStage<User> getUserInfo(String deviceToken);
    Long getUserIdByDeviceToken(String deviceToken);
    List<MobileUserInterestItems> updateUserInterest(String deviceToken, List<MobileUserInterestItems> interests);
    CompletionStage<List<Object[]>> getRewardInfo(String deviceToken);
    CompletionStage<Long> getRedeemPoint(String deviceToken);
    CompletionStage<String> updateInviteCode(String deviceToken, String inviteCode);
    CompletionStage<List<UserLuckyNumber>> getUserLuckyNumberByToken(String deviceToken);
    CompletionStage<List<Message>> getMessages(String deviceToken);
    CompletionStage<List<Message>> getNewMessages(String deviceToken);
    CompletionStage<String> updateMessage(long messageId, String status);
    String getRegistrationByDeviceToken(String deviceToken, String registrationToken);
}
