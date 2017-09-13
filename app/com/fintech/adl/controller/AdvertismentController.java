package com.fintech.adl.controller;

import play.db.jpa.Transactional;
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
	AdvertismentService adsService;

	@Inject()
	public AdvertismentController  (AdvertismentService adsService){
		this.adsService = adsService;
	}
	
	/**
	 * 
	 * @param favouriteList
	 * @param userId
	 * @return
	 */
	@Transactional
    public Result getAdsByVideo(String userId) {
        return ok("get ads by videos");
    }
	
	@Transactional
    public Result getAdsByImage(String userId) {
        return ok("get ads by Images");
    }	
	
	@Transactional
    public Result saveViewCounter(String userId) {
        return ok("Saving view counter");
    }	
	
	@Transactional
    public Result saveClickCounterForImage(String userId) {
        return ok("Saving click counter for Image");
    }
	
	@Transactional
    public Result saveClickCounterForVideo(String userId) {
        return ok("Saving click counter for Video");
    }	
}
