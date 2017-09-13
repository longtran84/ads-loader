package com.fintech.adl.service;

import javax.inject.Inject;

import com.fintech.adl.repo.UserRepository;


public class AdvertismentService {
	UserRepository userRepository;
	@Inject
	public AdvertismentService(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	public void updateFavouriteCategories(){
		System.out.println("test service layer");
		userRepository.findByUsername("test");
	}

}
