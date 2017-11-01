package com.fintechviet.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fintechviet.common.ErrorResponse;
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
			return ok(Json.toJson(response));
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
												  String location,
												  String inviteCode) {
		return userService.updateUserInfo(deviceToken, email, gender, dob, location, inviteCode).thenApplyAsync(response -> {
			return response.equals("ok") ? ok(Json.toJson(response)) : badRequest(Json.toJson(new ErrorResponse(response)));
		}, ec.current());
	}

	/**
	 *
	 * @param deviceToken
	 * @param rewardCode
	 * @param addedPoint
	 * @return
	 */
	@ApiOperation(value="Update User Reward. rewardCode(INSTALL, INVITE, READ, EVENT)")
	public CompletionStage<Result> updateReward(String deviceToken,
												String rewardCode,
												Long addedPoint) {
		return userService.updateReward(deviceToken, rewardCode, addedPoint).thenApplyAsync(response -> {
			return response.equals("ok") ? ok(Json.toJson(response)) : badRequest(Json.toJson(new ErrorResponse(response)));
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
			return ok(Json.toJson(response));
		}, ec.current());
	}

	/**
	 *
	 * @param deviceToken
	 * @return
	 */
	@ApiOperation(value="Get User Info By Device Token")
	public CompletionStage<Result> getUserInfo(String deviceToken) {
		return userService.getUserInfo(deviceToken).thenApplyAsync(response -> {
			return response != null ? ok(Json.toJson(response)) : badRequest(Json.toJson(new ErrorResponse("UserNotFound")));
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
			return ok(Json.toJson(response));
		}, ec.current());
	}

	/**
	 * @param deviceToken
	 * @return
	 */
	@ApiOperation(value="Get User Lucky Number")
	public CompletionStage<Result> getUserLuckyNumberByToken(String deviceToken) {
		return userService.getUserLuckyNumberByToken(deviceToken).thenApplyAsync(response -> {
			return response != null ? ok(Json.toJson(response)) :badRequest(Json.toJson("Lucky Number Not Found"));
		}, ec.current());
	}

	/**
	 * @param deviceToken
	 * @return
	 */
	@ApiOperation(value="Get User messages")
	public CompletionStage<Result> getMessages(String deviceToken) {
		return userService.getMessages(deviceToken).thenApplyAsync(response -> {
			return ok(Json.toJson(response));
		}, ec.current());
	}

	/**
	 * @param deviceToken
	 * @param type
	 * @return
	 */
	@ApiOperation(value="Get User messages")
	public CompletionStage<Result> getMessagesByType(String deviceToken, String type) {
		return userService.getMessagesByType(deviceToken, type).thenApplyAsync(response -> {
			return ok(Json.toJson(response));
		}, ec.current());
	}


	/**
	 * @param messageId
	 * @param status
	 * @return
	 */
	@ApiOperation(value="Update User message")
	public CompletionStage<Result> updateMessage(Long messageId, String status) {
		return userService.updateMessage(messageId, status).thenApplyAsync(response -> {
			return response.equals("ok") ? ok(Json.toJson(response)) : badRequest(Json.toJson(response));
		}, ec.current());
	}
}
