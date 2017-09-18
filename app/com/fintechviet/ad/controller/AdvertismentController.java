package com.fintechviet.ad.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fintechviet.ad.dto.Content;
import com.fintechviet.ad.dto.Decision;
import com.fintechviet.ad.dto.DecisionResponse;
import com.fintechviet.ad.dto.Request;
import com.fintechviet.ad.model.Ad;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fintechviet.ad.service.AdvertismentService;

import io.swagger.annotations.*;


@Api
public class AdvertismentController extends Controller {
	private static String DOMAIN = "http://10.0.2.2:9000";
	private final AdvertismentService adsService;
	private final HttpExecutionContext ec;

	@Inject()
	public AdvertismentController  (AdvertismentService adsService, HttpExecutionContext ec){
		this.adsService = adsService;
		this.ec = ec;
	}

    public CompletionStage<Result> getAdPlacement() {
		JsonNode json = request().body().asJson();
		Request request = Json.fromJson(json, Request.class);
		return adsService.findAdByTemplate(request.getPlacement().getTemplate()).thenApplyAsync(ad -> {
			return created(Json.toJson(buildAdResponse(ad, request)));
		}, ec.current());
    }

	private DecisionResponse buildAdResponse(Ad ad, Request request) {
		if (ad != null) {
			DecisionResponse response = new DecisionResponse();
			Decision decision = new Decision();
			Content content = new Content();
			decision.setAdId(ad.getId());
			decision.setClickUrl("http://www.vnexpress.net");
			if (request.getPlacement().getTemplate() == "image") {
				decision.setTrackingUrl(DOMAIN + "/ad/click?adId=" + ad.getId() + "&userId=" + request.getUserId());
				content.setImageUrl(ad.getCreative().getImageLink());;
			} else {
				decision.setViewUrl(DOMAIN + "/ad/view?adId=" + ad.getId() + "&userId=" + request.getUserId());
				content.setVideoUrl(ad.getCreative().getVideoLink());
			}
			decision.setImpressionUrl(DOMAIN + "/ad/impression/" + ad.getId());
			content.setBody(ad.getCreative().getBody());
			content.setTemplate(request.getPlacement().getTemplate());
			decision.setContent(content);
			response.setDecision(decision);
			return response;
		}

		return null;
	}

    public CompletionStage<Result> saveImpression(long adId) {
		return adsService.saveImpression(adId).thenApplyAsync(response -> {
			return created(Json.toJson(response));
		}, ec.current());
    }	

    public CompletionStage<Result> saveClick(long adId, String deviceToken) {
		return adsService.saveClick(adId, deviceToken).thenApplyAsync(response -> {
			return created(Json.toJson(response));
		}, ec.current());
    }

    public CompletionStage<Result> saveView(long adId, String deviceToken) {
		return adsService.saveView(adId, deviceToken).thenApplyAsync(response -> {
			return created(Json.toJson(response));
		}, ec.current());
    }	
}
