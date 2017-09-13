package com.fintech.adl.service;

import javax.inject.Inject;

import com.fintech.adl.repo.UserRepository;

import play.db.jpa.Transactional;

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

}
