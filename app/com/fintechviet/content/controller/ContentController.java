package com.fintechviet.content.controller;

import com.fintechviet.content.dto.News;
import com.fintechviet.content.dto.NewsResponse;
import com.fintechviet.content.model.Game;
import com.fintechviet.content.service.ContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import play.data.validation.Constraints.Required;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import com.fintechviet.utils.DateUtils;
import com.typesafe.config.Optional;

@Api(value = "Content")
public class ContentController extends Controller {
	private static String DOMAIN = "http://10.0.2.2:9000";
	private HttpExecutionContext ec;
	private ContentService contentService;
	private static final String  dateFormat = "yyyy-MM-dd HH:mm:ss";

	@Inject()
	public ContentController(ContentService contentService, HttpExecutionContext ec) {
		this.contentService = contentService;
		this.ec = ec;
	}

	@ApiOperation(value = "Get News by Interests")
	public CompletionStage<Result> getNewsByCategories(String deviceToken, String fromDate, String toDate)
			throws InterruptedException, ExecutionException {
		Date from = StringUtils.isNotEmpty(fromDate) ? DateUtils.convertStringToDate2(fromDate) : null;
		Date to = StringUtils.isNotEmpty(toDate) ? DateUtils.convertStringToDate2(toDate) : null;
		return contentService.getNewsByUserInterest(deviceToken, from, to).thenApplyAsync(newsList -> {
			return ok(Json.toJson(buildNewsResponse(newsList)));
		}, ec.current());
	}

	@ApiOperation(value = "Get News by Interests from crawler")
	public CompletionStage<Result> getNewsByCategoriesFromCrawler(String deviceToken, String page)
			throws InterruptedException, ExecutionException {
//		Date from = StringUtils.isNotEmpty(fromDate) ? DateUtils.convertStringToDate2(fromDate) : null;
//		Date to = StringUtils.isNotEmpty(toDate) ? DateUtils.convertStringToDate2(toDate) : null;
		int pageIndex = StringUtils.isNotEmpty(page) ? Integer.valueOf(page) : 1;
		return contentService.getNewsByUserInterestFromCrawler(deviceToken, pageIndex).thenApplyAsync(newsList -> {
			return ok(Json.toJson(buildNewsResponse(newsList)));
		}, ec.current());
	}

	@ApiOperation(value = "Get News by Interests from crawler")
	public CompletionStage<Result> getNewsByCategoriesFromCrawler1(String interests, String page)
			throws InterruptedException, ExecutionException {
//		Date from = StringUtils.isNotEmpty(fromDate) ? DateUtils.convertStringToDate2(fromDate) : null;
//		Date to = StringUtils.isNotEmpty(toDate) ? DateUtils.convertStringToDate2(toDate) : null;
		int pageIndex = StringUtils.isNotEmpty(page) ? Integer.valueOf(page) : 1;
		return contentService.getNewsByUserInterestFromCrawler1(interests, pageIndex).thenApplyAsync(newsList -> {
			return ok(Json.toJson(buildNewsResponse(newsList)));
		}, ec.current());
	}

	private NewsResponse buildNewsResponse(List<News> newsList) {
		NewsResponse response = new NewsResponse();
		response.setNewsList(newsList);
		return response;
	}

	@ApiOperation(value = "Save content impression")
	public CompletionStage<Result> saveImpression() {
		return contentService.saveImpression().thenApplyAsync(response -> {
			return ok(Json.toJson(response));
		}, ec.current());
	}

	@ApiOperation(value = "Save content click")
	public CompletionStage<Result> saveClick() {
		return contentService.saveClick().thenApplyAsync(response -> {
			return ok(Json.toJson(response));
		}, ec.current());
	}

	@Transactional
	@ApiOperation(value = "Get categories list")
	public CompletionStage<Result> getCategoriesList()
			throws InterruptedException, ExecutionException {
		return contentService.getCategoriesList().thenApplyAsync(list -> {
			return ok(Json.toJson(list));
		}, ec.current());
	}

	@ApiOperation(value = "Get top 20 news by category")
	public CompletionStage<Result> getTopNewsByCategory(String categoryCode)
			throws InterruptedException, ExecutionException {
		return contentService.getTopNewsByCategory(categoryCode).thenApplyAsync(news -> {
			return ok(Json.toJson(news));
		}, ec.current());
	}

	@ApiOperation(value = "Get News on Lock screen")
	public CompletionStage<Result> getNewsOnLockScreen(String deviceToken)
			throws InterruptedException, ExecutionException {
		return contentService.getNewsOnLockScreen(deviceToken).thenApplyAsync(news -> {
			return ok(Json.toJson(news));
		}, ec.current());
	}

	@ApiOperation(value = "Get Ad News")
	public CompletionStage<Result> getAdNewsList(String page)
			throws InterruptedException, ExecutionException {
		int pageIndex = StringUtils.isNotEmpty(page) ? Integer.valueOf(page) : 1;
		return contentService.getListAdNews(pageIndex).thenApplyAsync(news -> {
			return ok(Json.toJson(news));
		}, ec.current());
	}

	@ApiOperation(value = "Get News from Lock screen")
	public CompletionStage<Result> getNewsFromLockScreen(String deviceToken, String newsId, String page)
			throws InterruptedException, ExecutionException {
		int pageIndex = StringUtils.isNotEmpty(page) ? Integer.valueOf(page) : 1;
		return contentService.getNewsFromLockScreen(deviceToken, pageIndex, newsId).thenApplyAsync(news -> {
			return ok(Json.toJson(news));
		}, ec.current());
	}

	private List<com.fintechviet.content.dto.Game> buildGamesResponse(List<Game> gameList) {
		List<com.fintechviet.content.dto.Game> games = new ArrayList<com.fintechviet.content.dto.Game>();
		for (Game game : gameList) {
			com.fintechviet.content.dto.Game gameDTO = new com.fintechviet.content.dto.Game();
			gameDTO.setName(game.getName());
			gameDTO.setLink(game.getLink());
			gameDTO.setImage(game.getImage());
			games.add(gameDTO);
		}
		return games;
	}

	@ApiOperation(value = "Get list of games")
	public CompletionStage<Result> getGames()
			throws InterruptedException, ExecutionException {
		return contentService.getGames().thenApplyAsync(games -> {
			return ok(Json.toJson(buildGamesResponse(games)));
		}, ec.current());
	}
}
