package com.fintechviet.content.controller;

import com.fintechviet.content.dto.News;
import com.fintechviet.content.dto.NewsResponse;
import com.fintechviet.content.service.ContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@Api(value = "Content")
public class ContentController extends Controller {
	private static String DOMAIN = "http://10.0.2.2:9000";
	private HttpExecutionContext ec;
	private ContentService contentService;

	@Inject()
	public ContentController(ContentService contentService, HttpExecutionContext ec) {
		this.contentService = contentService;
		this.ec = ec;
	}

	@Transactional
	@ApiOperation(value = "Get news base on interests")
	public CompletionStage<Result> getNewsByCategories(String deviceToken, String cateIds, String lastNewsIds)
			throws InterruptedException, ExecutionException {
		return contentService.getNewsByUserInterest(deviceToken, cateIds, lastNewsIds).thenApplyAsync(newsList -> {
			return created(Json.toJson(buildNewsResponse(newsList)));
		}, ec.current());
	}

	private NewsResponse buildNewsResponse(List<News> newsList) {
		NewsResponse response = new NewsResponse();
		response.setNewsList(newsList);
		return response;
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
	
	@Transactional
	@ApiOperation(value = "Get categories list")
	public CompletionStage<Result> getCategoriesList()
			throws InterruptedException, ExecutionException {
		return contentService.getCategoriesList().thenApplyAsync(list -> {
			return created(Json.toJson(list));
		}, ec.current());
	}
}
