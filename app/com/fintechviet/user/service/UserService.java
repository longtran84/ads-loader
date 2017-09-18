package com.fintechviet.user.service;

import javax.inject.Inject;

import com.fintechviet.user.model.User;
import com.fintechviet.user.respository.UserRepository;
import play.libs.concurrent.HttpExecutionContext;

import java.util.List;
import java.util.concurrent.CompletionStage;


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

	public CompletionStage<String> updateUserInterest(String deviceToken, List<String> interests){
		return userRepository.updateUserInterest(deviceToken, interests);
	}

	public CompletionStage<String> updateReward(String deviceToken, String event, long point){
		return userRepository.updateReward(deviceToken, event, point);
	}

	public CompletionStage<com.fintechviet.user.dto.User> getUserInfo(String deviceToken){
		return userRepository.getUserInfo(deviceToken).thenApplyAsync(user -> {
			return new com.fintechviet.user.dto.User(user.getEmail(), user.getGender(), user.getDob(), user.getLocation(), user.getEarning());
		}, ec.current());
	}

}
