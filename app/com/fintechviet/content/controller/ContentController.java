package com.fintechviet.content.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fintechviet.content.dto.NewsResponse;
import com.fintechviet.content.service.ContentService;
import io.swagger.annotations.Api;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


@Api
public class ContentController extends Controller {
	private static String DOMAIN = "http://10.0.2.2:9000";
	private HttpExecutionContext ec;
	private ContentService contentService;

	@Inject()
	public ContentController(ContentService contentService, HttpExecutionContext ec){
		this.contentService = contentService;
        this.ec = ec;
	}

	@Transactional
    public Result getNewsByCategories(String deviceToken) {
		JsonNode json = request().body().asJson();
		NewsResponse newsResponse = new NewsResponse();
		newsResponse.setNewsList(contentService.getNewsByUserInterest(deviceToken));
        return ok("");
    }

	public CompletionStage<Result> saveImpression() {
        return contentService.saveImpression().thenApplyAsync(response -> {
            return created(Json.toJson(response));
        }, ec.current());
	}

    public CompletionStage<Result> saveClick() {
        return contentService.saveClick().thenApplyAsync(response -> {
            return created(Json.toJson(response));
        }, ec.current());
    }
}
