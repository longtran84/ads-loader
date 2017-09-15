package com.fintech.adl.service;

import javax.inject.Inject;

import com.fintech.adl.model.Ad;
import com.fintech.adl.repo.AdvertismentRepository;
import com.fintech.adl.repo.UserRepository;


public class AdvertismentService {
	AdvertismentRepository advertismentRepository;

	@Inject
	public AdvertismentService(AdvertismentRepository advertismentRepository){
		this.advertismentRepository = advertismentRepository;
	}

	public Ad findAdByTemplate(String template) {
		return advertismentRepository.findAdByTemplate(template);
	}

	public void saveImpression(long adId) {
		advertismentRepository.saveImpression(adId);
	}

	public void saveClick(long adId, String deviceToken) {
		advertismentRepository.saveClick(adId, deviceToken);
	}

	public void saveView(long adId, String deviceToken) {
		advertismentRepository.saveView(adId, deviceToken);
	}
}
