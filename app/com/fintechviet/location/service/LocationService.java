package com.fintechviet.location.service;

import com.fintechviet.location.dto.Place;
import com.fintechviet.location.model.AdLocation;
import com.fintechviet.location.repository.LocationRepository;
import com.fintechviet.notification.PushAdsHelper;
import com.fintechviet.user.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import play.Configuration;
import play.Logger;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;


public class LocationService {

	@Inject
	private Configuration configuration;

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String TYPE_DETAILS = "/details";
	private static final String TYPE_SEARCH = "/search";

	private static final String OUT_JSON = "/json";

	// KEY!
	private static final String API_KEY = "AIzaSyCWeYRYc3M5-7XvL35M7asdOB8E9qKtqVI";

	private final LocationRepository locationRepository;

	private final UserRepository userRepository;

	private final HttpExecutionContext ec;

	@Inject
	public LocationService(LocationRepository locationRepository, UserRepository userRepository, HttpExecutionContext ec){
		this.locationRepository = locationRepository;
		this.userRepository = userRepository;
		this.ec = ec;
	}

	public static ArrayList<Place> autocomplete(String input) {
		ArrayList<Place> resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append(TYPE_AUTOCOMPLETE);
			sb.append(OUT_JSON);
			sb.append("?sensor=false");
			sb.append("&key=" + API_KEY);
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Logger.error("Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Logger.error("Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<Place>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				Place place = new Place();
				place.setReference(predsJsonArray.getJSONObject(i).getString("reference"));
				place.setName(predsJsonArray.getJSONObject(i).getString("description"));
				resultList.add(place);
			}
		} catch (JSONException e) {
			Logger.error("Error processing JSON results", e);
		}

		return resultList;
	}

	public List<Place> search(String keyword, String lng, String lat, int radius) {
		List<Place> resultList = new ArrayList<>();

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append(TYPE_SEARCH);
			sb.append(OUT_JSON);
			sb.append("?sensor=false");
			sb.append("&key=" + API_KEY);
			sb.append("&keyword=" + URLEncoder.encode(keyword, "utf8"));
			sb.append("&location=" + String.valueOf(lat) + "," + String.valueOf(lng));
			sb.append("&radius=" + String.valueOf(radius));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Logger.error("Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Logger.error("Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("results");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<Place>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				JSONObject jsObj = predsJsonArray.getJSONObject(i);
				Place place = new Place();
				place.setIcon(jsObj.getString("icon"));
				place.setReference(jsObj.getString("reference"));
				place.setName(jsObj.getString("name"));
				place.setVicinity(jsObj.getString("vicinity"));
				if (jsObj.has("geometry")) {
					place.setGeometry(jsObj.getJSONObject("geometry").toString());
				}
				if (jsObj.has("formatted_address")) {
					place.setFormattedAddress(jsObj.getString("formatted_address"));
				}
				if (jsObj.has("formatted_phone_number")) {
					place.setFormattedPhoneNumber(jsObj.getString("formatted_phone_number"));
				}
				resultList.add(place);
			}
		} catch (JSONException e) {
			Logger.error("Error processing JSON results", e);
		}

		return resultList;
	}

	public static Place details(String reference) {
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append(TYPE_DETAILS);
			sb.append(OUT_JSON);
			sb.append("?sensor=false");
			sb.append("&key=" + API_KEY);
			sb.append("&reference=" + URLEncoder.encode(reference, "utf8"));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Logger.error("Error processing Places API URL", e);
			return null;
		} catch (IOException e) {
			Logger.error("Error connecting to Places API", e);
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		Place place = null;
		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString()).getJSONObject("result");

			place = new Place();
			place.setIcon(jsonObj.getString("icon"));
			place.setName(jsonObj.getString("name"));
			place.setFormattedAddress(jsonObj.getString("formatted_address"));
			if (jsonObj.has("formatted_phone_number")) {
				place.setFormattedPhoneNumber(jsonObj.getString("formatted_phone_number"));
			}
		} catch (JSONException e) {
			Logger.error("Error processing JSON results", e);
		}

		return place;
	}

	public CompletionStage<List<Place>> searchNearBy(String longitude, String latitude) {
		return supplyAsync(() -> search("Vietinbank", longitude, latitude, 5000));
		//return search("Maritime bank", 21.0318272, 105.787884, 100000);
	}

	public CompletionStage<List<AdLocation>> searchAdLocationsNearby(String longitude, String latitude) throws IOException {
		return supplyAsync(() -> locationRepository.findAdLocationsNearBy(longitude, latitude));
		//return supplyAsync(() -> locationRepository.findAdLocationsNearBy(105.787884, 21.0318272));
	}

	public CompletionStage<String> checkAdLocationsNearby(String deviceToken, String registrationToken, String longitude, String latitude) throws IOException, InterruptedException  {
		List<AdLocation> adLocations = locationRepository.findAdLocationsNearBy( longitude, latitude);
		String regToken = userRepository.getRegistrationByDeviceToken(deviceToken, registrationToken);
		for (AdLocation adLocation : adLocations) {
			PushAdsHelper.pushAdNotificationTorUsers(regToken, adLocation);
		}
		return supplyAsync(() -> "ok");
	}
}
