package com.fintech.adl.controller;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import com.fintech.adl.service.UserInfoService;

import io.swagger.annotations.*;


@Api
public class UserInfoController extends Controller {
	UserInfoService userService;
/*	@ApiImplicitParams({
		@ApiImplicitParam(
					name = "favouriteList",
			      value = "Favourite list",
			      required = true,
			      dataType = "string", // complete path
			      paramType = "query"
				),
		@ApiImplicitParam(
					name = "userId",
			      value = "userId",
			      required = true,
			      dataType = "string", // complete path
			      paramType = "query"
				)		
		}
		
	)*/
	@Inject()
	public UserInfoController  (UserInfoService userInfoService){
		this.userService = userInfoService;
	}
	
	/**
	 * 
	 * @param favouriteList
	 * @param userId
	 * @return
	 */
	@Transactional
    public Result updateFavouriteCategories(String favouriteList,
    		String userId) {
    	userService.updateFavouriteCategories();
        return ok(userId + favouriteList);
    }
	
	/**
	 * 
	 * @param favouriteList
	 * @param deviceToken
	 * @return
	 */
	@Transactional
    public Result updateFavouriteCategoriesByDevice(String favouriteList,
    		String deviceToken) {
        return ok(deviceToken + favouriteList);
    }
	
	/**
	 * 
	 * @param userId
	 * @param email
	 * @param gender
	 * @param dob
	 * @param location
	 * @return
	 */
	@Transactional
    public Result updateUserInfo(String userId,
    		String email,
    		String gender,
    		long dob,
    		String location) {
        return ok("updated userInfo");
    }
	
	/**
	 * 
	 * @param userId
	 * @param addedPoint
	 * @return
	 */
	@Transactional
    public Result updateReward(String userId,
    		Long addedPoint) {
        return ok("updated reward info");
    }	
	
	
	//Pull from DB
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
    public Result getFavouriteCategories(String userId) {
        return ok("get Fav by userId");
    }
	

	/**
	 * 
	 * @param deviceToken
	 * @return
	 */
	@Transactional
    public Result getFavouriteCategoriesByDevice(String deviceToken) {
        return ok("get Fav by device");
    }
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
    public Result getUserInfo(String userId) {
        return ok("get userInfo");
    }
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
    public Result getRewardInfo(String userId) {
        return ok("get reward info");
    }	
}
