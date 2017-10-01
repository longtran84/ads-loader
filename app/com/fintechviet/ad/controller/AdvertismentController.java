package com.fintechviet.ad.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fintechviet.ad.dto.*;
import com.fintechviet.ad.dto.AppAd;
import com.fintechviet.ad.model.*;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fintechviet.ad.service.AdvertismentService;

import io.swagger.annotations.*;


@Api(value="Advertisment")
public class AdvertismentController extends Controller {
	private static String DOMAIN = "http://10.0.2.2:9000";
	private final AdvertismentService adsService;
	private final HttpExecutionContext ec;

	@Inject()
	public AdvertismentController  (AdvertismentService adsService, HttpExecutionContext ec){
		this.adsService = adsService;
		this.ec = ec;
	}

	/**
	 * @return
	 */
	@ApiOperation(value="Get ad")
    public CompletionStage<Result> getAdPlacement(String template, String deviceToken) {
		return adsService.findAdByTemplate(template).thenApplyAsync(ad -> {
			return created(Json.toJson(buildAdResponse(ad, template, deviceToken)));
		}, ec.current());
    }

	private DecisionResponse buildAdResponse(Ad ad, String template, String userId) {
		if (ad != null) {
			DecisionResponse response = new DecisionResponse();
			Decision decision = new Decision();
			Content content = new Content();
			decision.setAdId(ad.getId());
			decision.setClickUrl("http://www.vnexpress.net");
			if (template == "image") {
				decision.setTrackingUrl(DOMAIN + "/ad/click?adId=" + ad.getId() + "&deviceToken=" + userId);
				content.setImageUrl(ad.getCreative().getImageLink());;
			} else {
				decision.setViewUrl(DOMAIN + "/ad/view?adId=" + ad.getId() + "&deviceToken=" + userId);
				content.setVideoUrl(ad.getCreative().getVideoLink());
			}
			decision.setImpressionUrl(DOMAIN + "/ad/impression/" + ad.getId());
			content.setBody(ad.getCreative().getBody());
			content.setTemplate(template);
			decision.setContent(content);
			response.setDecision(decision);
			return response;
		}

		return null;
	}

	/**
	 * @param adId
	 * @return
	 */
	@ApiOperation(value="Save impression")
    public CompletionStage<Result> saveImpression(long adId) {
		return adsService.saveImpression(adId).thenApplyAsync(response -> {
			return created(Json.toJson(response));
		}, ec.current());
    }

	/**
	 * @param adId
	 * @param deviceToken
	 * @return
	 */
	@ApiOperation(value="Save click")
    public CompletionStage<Result> saveClick(long adId, String deviceToken) {
		return adsService.saveClick(adId, deviceToken).thenApplyAsync(response -> {
			return created(Json.toJson(response));
		}, ec.current());
    }

	/**
	 * @param adId
	 * @param deviceToken
	 * @return
	 */
	@ApiOperation(value="Save view")
    public CompletionStage<Result> saveView(long adId, String deviceToken) {
		return adsService.saveView(adId, deviceToken).thenApplyAsync(response -> {
			return created(Json.toJson(response));
		}, ec.current());
    }

	/**
	 * @return
	 */
	@ApiOperation(value="Get list app ad")
	public CompletionStage<Result> getListAppAd() {
		return adsService.getListAppAd().thenApplyAsync(list -> {
			return created(Json.toJson(buildDTO(list)));
		}, ec.current());
	}

	private List<AppAd> buildDTO(List<com.fintechviet.ad.model.AppAd> appAds) {
		List<AppAd> appAdDTOs = new ArrayList<AppAd>();
		for (com.fintechviet.ad.model.AppAd appAd : appAds) {
			AppAd apa = new AppAd();
			apa.setId(appAd.getId());
			apa.setName(appAd.getName());
			apa.setIcon(appAd.getIcon());
			apa.setInstallLink(appAd.getInstallLink());
			appAdDTOs.add(apa);
		}
		return appAdDTOs;
	}
}
