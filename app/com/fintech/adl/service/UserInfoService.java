package com.fintech.adl.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fintech.adl.repo.UserRepository;
import com.fintechviet.content.model.MobileUserInterest;
import com.fintechviet.content.model.MobileUserInterestItems;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import play.mvc.Result;


public class UserInfoService {
	UserRepository userRepository;
	@Inject
	public UserInfoService(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	public void updateFavouriteCategories(){
		System.out.println("test service layer");
		userRepository.findByUsername("test");
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

}
