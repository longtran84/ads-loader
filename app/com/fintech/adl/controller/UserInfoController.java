package com.fintech.adl.controller;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.ArrayList;

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
	@Transactional
    public Result updateFavouriteCategories(String favouriteList,
    		String userId) {
    	userService.updateFavouriteCategories();
        return ok(userId + favouriteList);
    }
}
