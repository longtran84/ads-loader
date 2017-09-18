package com.fintech.adl.service;

import javax.inject.Inject;

import com.fintech.adl.model.Ad;
import com.fintech.adl.repo.AdvertismentRepository;

import java.util.concurrent.CompletionStage;


public class AdvertismentService {
	private final AdvertismentRepository advertismentRepository;

	@Inject
	public AdvertismentService(AdvertismentRepository advertismentRepository){
		this.advertismentRepository = advertismentRepository;
	}

	public CompletionStage<Ad> findAdByTemplate(String template) {
		return advertismentRepository.findAdByTemplate(template);
	}

	public CompletionStage<String> saveImpression(long adId) {
		return advertismentRepository.saveImpression(adId);
	}

	public CompletionStage<String> saveClick(long adId, String deviceToken) {
		return advertismentRepository.saveClick(adId, deviceToken);
	}

	public CompletionStage<String> saveView(long adId, String deviceToken) {
		return advertismentRepository.saveView(adId, deviceToken);
	}
}
