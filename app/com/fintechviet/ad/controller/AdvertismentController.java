package com.fintechviet.ad.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fintechviet.ad.dto.*;
import com.fintechviet.ad.dto.AppAd;
import com.fintechviet.ad.model.*;
import com.fintechviet.common.ErrorResponse;
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
	private static String DOMAIN = "http://222.252.16.132:9000";
	private final AdvertismentService adsService;
	private final HttpExecutionContext ec;

	@Inject()
	public AdvertismentController  (AdvertismentService adsService, HttpExecutionContext ec){
		this.adsService = adsService;
		this.ec = ec;
	}

	/**
	 * @param template
	 * @param deviceToken
	 * @param adTypeId
	 * @return
	 */
	@ApiOperation(value="Get ad")
	public CompletionStage<Result> getAdPlacement(String template, String deviceToken, Integer adTypeId) {
		if (adTypeId == null) {
			adTypeId = 0;
		}
		return adsService.findAdByTemplate(template, adTypeId, deviceToken).thenApplyAsync(ad -> {
			return ad != null ? ok(Json.toJson(buildAdResponse(ad, template, deviceToken))) : badRequest(Json.toJson(new ErrorResponse("AdNotFound")));
		}, ec.current());
	}

	/**
	 * @param deviceToken
	 * @return
	 */
	@ApiOperation(value="Get top ad")
	public CompletionStage<Result> getTopAdv(String deviceToken) {
		return adsService.getTopAdv(deviceToken).thenApplyAsync(ads -> {
			return ads.size() > 0 ? ok(Json.toJson(buildAdsResponse(ads, deviceToken))) : badRequest(Json.toJson(new ErrorResponse("AdNotFound")));
		}, ec.current());
	}

	private List<DecisionResponse> buildAdsResponse(List<Ad> ads, String deviceToken) {
		List<DecisionResponse> dtos = new ArrayList<DecisionResponse>();
		for (Ad ad : ads) {
			dtos.add(buildAdResponse(ad, "image", deviceToken));
		}
		return  dtos;
	}

	private DecisionResponse buildAdResponse(Ad ad, String template, String deviceToken) {
		if (ad != null) {
			DecisionResponse response = new DecisionResponse();
			Decision decision = new Decision();
			Content content = new Content();
			decision.setAdId(ad.getId());
			decision.setClickUrl(ad.getCreative().getClickUrl());
			if (template.equals("image")) {
				decision.setTrackingUrl(DOMAIN + "/ad/click?adId=" + ad.getId() + "&deviceToken=" + deviceToken);
				content.setImageUrl(ad.getCreative().getImageLink());;
			} else {
				decision.setViewUrl(DOMAIN + "/ad/view?adId=" + ad.getId() + "&deviceToken=" + deviceToken);
				content.setVideoUrl(ad.getCreative().getVideoLink());
			}
			decision.setImpressionUrl(DOMAIN + "/ad/impression/" + ad.getId());
			content.setBody(ad.getDescription());
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
			return ok(Json.toJson(response));
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
			return ok(Json.toJson(response));
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
			return ok(Json.toJson(response));
		}, ec.current());
	}

	/**
	 * @param appId
	 * @param deviceToken
	 * @param platform
	 * @return
	 */
	@ApiOperation(value="Save install(platform=ios, android)")
	public CompletionStage<Result> saveInstall(long appId, String deviceToken, String platform) {
		return adsService.saveInstall(appId, deviceToken, platform).thenApplyAsync(response -> {
			return ok(Json.toJson(response));
		}, ec.current());
	}

	/**
	 * @return
	 */
	@ApiOperation(value="Get list app ad")
	public CompletionStage<Result> getListAppAd() {
		return adsService.getListAppAd().thenApplyAsync(list -> {
			return ok(Json.toJson(buildDTO(list)));
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
