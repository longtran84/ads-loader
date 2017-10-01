package com.fintechviet.ad.service;

import javax.inject.Inject;

import com.fintechviet.ad.model.Ad;
import com.fintechviet.ad.model.AdImpressions;
import com.fintechviet.ad.model.AppAd;
import com.fintechviet.ad.repo.AdvertismentRepository;

import java.util.List;
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

	public CompletionStage<AdImpressions> saveImpression(long adId) {
		return advertismentRepository.saveImpression(adId);
	}

	public CompletionStage<String> saveClick(long adId, String deviceToken) {
		return advertismentRepository.saveClick(adId, deviceToken);
	}

	public CompletionStage<String> saveView(long adId, String deviceToken) {
		return advertismentRepository.saveView(adId, deviceToken);
	}

	public CompletionStage<List<AppAd>> getListAppAd() {
		return advertismentRepository.getListAppAd();
	}

	public CompletionStage<String> saveInstall(long appId, String deviceToken, String platform) {
		return advertismentRepository.saveInstall(appId, deviceToken, platform);
	}
}
