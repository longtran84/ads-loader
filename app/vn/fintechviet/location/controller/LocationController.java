package vn.fintechviet.location.controller;

import vn.fintechviet.location.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@Api(value = "Location")
public class LocationController extends Controller {
	private HttpExecutionContext ec;
	private LocationService locationService;
	private final MessagesApi messagesApi;

	@Inject()
	public LocationController(LocationService locationService, HttpExecutionContext ec, MessagesApi messagesApi) {
		this.locationService = locationService;
		this.ec = ec;
		this.messagesApi = messagesApi;
	}

	/**
	 * @param type
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	@ApiOperation(value = "Search by type (BANK_AGENCY, ATM, AD_LOCATION)")
	public CompletionStage<Result> searchNearBy(String type, String longitude, String latitude)
			throws InterruptedException, ExecutionException {
		return locationService.searchNearBy(type, longitude, latitude).thenApplyAsync(places -> {
			return ok(Json.toJson(places));
		}, ec.current());
	}

	/**
	 * @param deviceToken
	 * @param registrationToken
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	@ApiOperation(value = "Check ad location nearby")
	public CompletionStage<Result> checkAdLocationsNearBy(String deviceToken, String registrationToken, String longitude, String latitude)
			throws InterruptedException, ExecutionException, IOException {
		return locationService.checkAdLocationsNearby(deviceToken, registrationToken, longitude, latitude).thenApplyAsync(result -> {
			return ok(Json.toJson(result));
		}, ec.current());

	}

	/**
	 * @param placeId
	 * @return
	 */
	@ApiOperation(value = "Get detail of location")
	public CompletionStage<Result> details(String placeId)
			throws InterruptedException, ExecutionException {
		return locationService.details(placeId).thenApplyAsync(place -> {
			return ok(Json.toJson(place));
		}, ec.current());
	}

}
