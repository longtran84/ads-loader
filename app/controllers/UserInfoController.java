package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import java.util.ArrayList;
import io.swagger.annotations.*;


@Api
public class UserInfoController extends Controller {
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
    public Result updateFavouriteCategories(String favouriteList,
    		String userId) {
		System.out.println(userId + favouriteList);
        return ok(userId + favouriteList);
    }
}
