package com.fintech.adl.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fintech.adl.dto.Content;
import com.fintech.adl.dto.Decision;
import com.fintech.adl.dto.DecisionResponse;
import com.fintech.adl.dto.Request;
import com.fintech.adl.model.Ad;
import play.api.Play;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import com.fintech.adl.service.AdvertismentService;
import com.fintech.adl.service.UserInfoService;

import io.swagger.annotations.*;


@Api
public class AdvertismentController extends Controller {
	private static String DOMAIN = "http://10.0.2.2:9000";
	AdvertismentService adsService;

	@Inject()
	public AdvertismentController  (AdvertismentService adsService){
		this.adsService = adsService;
	}

	@Transactional
    public Result getAdPlacement() {
		JsonNode json = request().body().asJson();
		Request request = Json.fromJson(json, Request.class);
		Ad ad = adsService.findAdByTemplate(request.getPlacement().getTemplate());
		DecisionResponse response = buildAdResponse(ad, request);
        return ok("get ads by template");
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
	
	@Transactional
    public Result saveImpression(long adId) {
		adsService.saveImpression(adId);
		return null;
    }	
	
	@Transactional
    public Result saveClick(long adId, String deviceToken) {
		adsService.saveClick(adId, deviceToken);
		return null;
    }
	
	@Transactional
    public Result saveView(long adId, String deviceToken) {
		adsService.saveView(adId, deviceToken);
		return null;
    }	
}
