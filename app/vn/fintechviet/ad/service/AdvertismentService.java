package vn.fintechviet.ad.service;

import javax.inject.Inject;

import vn.fintechviet.ad.model.Ad;
import vn.fintechviet.ad.model.AdImpressions;
import vn.fintechviet.ad.model.AppAd;
import vn.fintechviet.ad.repository.AdvertismentRepository;
import vn.fintechviet.user.repository.UserRepository;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;


public class AdvertismentService {
	private final AdvertismentRepository advertismentRepository;
	private final UserRepository userRepository;

	@Inject
	public AdvertismentService(AdvertismentRepository advertismentRepository, UserRepository userRepository){
		this.advertismentRepository = advertismentRepository;
		this.userRepository = userRepository;
	}

	public CompletionStage<Ad> findAdByTemplate(String template, int adTypeId, String deviceToken) {
		return advertismentRepository.findAdByTemplate(template, adTypeId, deviceToken);
	}

	public CompletionStage<List<Ad>> getTopAdv(String deviceToken) {
		return advertismentRepository.getTopAdv(deviceToken);
	}

	public CompletionStage<AdImpressions> saveImpression(long adId) {
		return advertismentRepository.saveImpression(adId);
	}

	public CompletionStage<String> saveClick(long adId, String deviceToken) {
		boolean isClicked = advertismentRepository.isAdClicked(deviceToken, adId);
		if (!isClicked) {
			userRepository.updateReward(deviceToken, "ADV", 10);
			return advertismentRepository.saveClick(adId, deviceToken);
		} else {
			return supplyAsync(() -> "ok");
		}
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
