package com.fintechviet.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fintechviet.user.dto.UserInterest;
import com.fintechviet.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;


@Api(value="User")
public class UserController extends Controller {
	private final UserService userService;
	private final HttpExecutionContext ec;

	@Inject()
	public UserController(UserService userInfoService, HttpExecutionContext ec){
		this.userService = userInfoService;
		this.ec = ec;
	}
	
	/**
	 * @param favouriteList
	 * @param deviceToken
	 * @return
	 */
	@ApiOperation(value="Update User Interests by device token")
	public CompletionStage<Result> updateFavouriteCategoriesByDevice(String favouriteList, String deviceToken) {
		return userService.updateUserInterest(deviceToken, favouriteList).thenApplyAsync(response -> {
			return created(Json.toJson(response));
		}, ec.current());
	}
	
	/**
	 * 
	 * @param deviceToken
	 * @param email
	 * @param gender
	 * @param dob
	 * @param location
	 * @return
	 */
	@ApiOperation(value="Update User Details")
    public CompletionStage<Result> updateUserInfo(String deviceToken,
												  String email,
												  String gender,
												  int dob,
												  String location) {
		return userService.updateUserInfo(deviceToken, email, gender, dob, location).thenApplyAsync(response -> {
			return created(Json.toJson(response));
		}, ec.current());
    }
	
	/**
	 * 
	 * @param deviceToken
	 * @param event
	 * @param addedPoint
	 * @return
	 */
	@ApiOperation(value="Update User Reward")
    public CompletionStage<Result> updateReward(String deviceToken,
							   String event,
    		                   Long addedPoint) {
		return userService.updateReward(deviceToken, event, addedPoint).thenApplyAsync(response -> {
			return created(Json.toJson(response));
		}, ec.current());
    }

	/**
	 *
	 * @param deviceToken
	 * @param inviteCode
	 * @return
	 */
	@ApiOperation(value="Update Invite code")
	public CompletionStage<Result> updateInviteCode(String deviceToken,
												String inviteCode) {
		return userService.updateInviteCode(deviceToken, inviteCode).thenApplyAsync(response -> {
			return created(Json.toJson(response));
		}, ec.current());
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
	 * @param deviceToken
	 * @return
	 */
	@ApiOperation(value="Get User Info By Device Token")
    public CompletionStage<Result> getUserInfo(String deviceToken) {
		return userService.getUserInfo(deviceToken).thenApplyAsync(response -> {
			return response != null ? created(Json.toJson(response)) : ok("User Not Found");
		}, ec.current());
    }
	
	/**
	 * 
	 * @param deviceToken
	 * @return
	 */
	@ApiOperation(value="Get User Reward Info")
    public CompletionStage<Result> getRewardInfo(String deviceToken) {
		return userService.getRewardInfo(deviceToken).thenApplyAsync(response -> {
			return created(Json.toJson(response));
		}, ec.current());
    }	
}
