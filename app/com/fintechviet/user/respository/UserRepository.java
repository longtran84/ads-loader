package com.fintechviet.user.respository;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.fintechviet.user.model.User;
import com.google.inject.ImplementedBy;

@ImplementedBy(JPAUserRepository.class)
public interface UserRepository{
    CompletionStage<String> updateUserInterest(String deviceToken, List<String> interests);
    CompletionStage<String> updateUserInfo(String deviceToken, String email, String gender, int dob, String location);
    CompletionStage<String> updateReward(String deviceToken, String event, long point);
    CompletionStage<User> getUserInfo(String deviceToken);
}
