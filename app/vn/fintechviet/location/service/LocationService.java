package vn.fintechviet.location.service;

import org.apache.commons.lang3.StringUtils;
import vn.fintechviet.location.dto.*;
import vn.fintechviet.location.model.AdLocation;
import vn.fintechviet.location.repository.LocationRepository;
import vn.fintechviet.notification.PushNotificationsHelper;
import vn.fintechviet.user.repository.UserRepository;
import vn.fintechviet.utils.CommonUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import play.Configuration;
import play.Logger;
import play.libs.concurrent.HttpExecutionContext;
import vn.fintechviet.utils.DateUtils;

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

	private String distance(double lat1, double lng1, double lat2, double lng2) {
		int r = 6731; // average radius of the earth in km
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
						* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = r * c;
		return CommonUtils.convertDoubleToString(d) + " km";
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
			InputStreamReader in = new InputStreamReader(conn.getInputStream(), "utf-8");

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
				place.setPlaceId(jsObj.getString("place_id"));
				place.setIcon(jsObj.getString("icon"));
				place.setReference(jsObj.getString("reference"));
				place.setName(jsObj.getString("name"));
				place.setAddress(jsObj.getString("vicinity"));
				if (jsObj.has("geometry")) {
					JSONObject locationObj = jsObj.getJSONObject("geometry").getJSONObject("location");
					place.setLongitude(locationObj.get("lng").toString());
					place.setLatitude(locationObj.get("lat").toString());
					place.setDistance(distance(Double.valueOf(locationObj.get("lat").toString()), Double.valueOf(locationObj.get("lng").toString()), Double.valueOf(lat), Double.valueOf(lng)));
				}
				if (jsObj.has("formatted_address")) {
					place.setFormattedAddress(jsObj.getString("formatted_address"));
				}
				if (jsObj.has("formatted_phone_number")) {
					place.setFormattedPhoneNumber(jsObj.getString("formatted_phone_number"));
				}
//				Place placeDetail = details(jsObj.getString("place_id"));
//				place.setReviews(placeDetail.getReviews());
//				place.setWeekdayText(placeDetail.getWeekdayText());
//				place.setOpeningHour(placeDetail.getOpeningHour());
//				place.setPhotos(placeDetail.getPhotos());
				resultList.add(place);
			}
		} catch (JSONException e) {
			Logger.error("Error processing JSON results", e);
		}

		return resultList;
	}

	private Place getDetails(String placeid) {
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append(TYPE_DETAILS);
			sb.append(OUT_JSON);
			sb.append("?sensor=false");
			sb.append("&key=" + API_KEY);
			sb.append("&placeid=" + placeid);
			sb.append("&language=vi");

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

		Place place = new Place();
		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString()).getJSONObject("result");
			place.setPlaceId(jsonObj.getString("place_id"));
			place.setIcon(jsonObj.getString("icon"));
			place.setReference(jsonObj.getString("reference"));
			place.setName(jsonObj.getString("name"));
			place.setAddress(jsonObj.getString("vicinity"));
			if (jsonObj.has("geometry")) {
				JSONObject locationObj = jsonObj.getJSONObject("geometry").getJSONObject("location");
				place.setLongitude(locationObj.get("lng").toString());
				place.setLatitude(locationObj.get("lat").toString());
			}
			if (jsonObj.has("formatted_address")) {
				place.setFormattedAddress(jsonObj.getString("formatted_address"));
			}
			if (jsonObj.has("formatted_phone_number")) {
				place.setFormattedPhoneNumber(jsonObj.getString("formatted_phone_number"));
			}
			if (jsonObj.has("opening_hours")) {
				JSONObject openingHourObj = jsonObj.getJSONObject("opening_hours");
				OpeningHour openingHour = new OpeningHour();
				openingHour.setOpenNow(openingHourObj.getBoolean("open_now"));
				JSONArray periods = openingHourObj.getJSONArray("periods");
				List<Period> periodList = new ArrayList<>();
				for (int i = 0; i < periods.length(); i++) {
					JSONObject openCloseObj = periods.getJSONObject(i);
					JSONObject openObj = openCloseObj.getJSONObject("open");
					JSONObject closeObj = openCloseObj.getJSONObject("close");

					Period period = new Period();
					Open open = new Open();
					open.setDay(DateUtils.convertIntDayToString(openObj.getInt("day")));
					open.setTime(openObj.getString("time"));
					Close close = new Close();
					close.setDay(DateUtils.convertIntDayToString(closeObj.getInt("day")));
					close.setTime(closeObj.getString("time"));
					period.setOpen(open);
					period.setClose(close);
					periodList.add(period);
				}
				//openingHour.setPeriods(periodList);
				place.setOpeningHour(openingHour);
				List<String> weekdays = new ArrayList<>();
				if (openingHourObj.has("weekday_text")) {
					JSONArray weekDays = openingHourObj.getJSONArray("weekday_text");
					for (int i = 0; i < weekDays.length(); i++) {
						weekdays.add(weekDays.getString(i));
					}
				}
				place.setWeekdayText(weekdays);
			}

			if (jsonObj.has("reviews")) {
				JSONArray reviews = jsonObj.getJSONArray("reviews");
				List<Review> reviewList = new ArrayList<>();
				for (int i = 0; i < reviews.length(); i++) {
					JSONObject reviewObj = reviews.getJSONObject(i);
					Review review = new Review();
					review.setAuthorName(reviewObj.getString("author_name"));
					review.setAuthorUrl(reviewObj.getString("author_url"));
					review.setProfilePhotoUrl(reviewObj.getString("profile_photo_url"));
					review.setRating(reviewObj.getInt("rating"));
					review.setRelativeTimeDescription(reviewObj.getString("relative_time_description"));
					review.setText(reviewObj.getString("text"));
					review.setTime(DateUtils.convertLongToDateString(reviewObj.getLong("time")));
					reviewList.add(review);
				}
				place.setReviews(reviewList);
			}

			if (jsonObj.has("photos")) {
				JSONArray photos = jsonObj.getJSONArray("photos");
				List<Photo> photoList = new ArrayList<>();
				for (int i = 0; i < photos.length(); i++) {
					JSONObject photoObj = photos.getJSONObject(i);
					String imageLink = getImageLink(photoObj.getString("photo_reference"));
					if (StringUtils.isNotEmpty(imageLink)) {
						Photo photo = new Photo();
						photo.setWidth(photoObj.getInt("width"));
						photo.setHeight(photoObj.getInt("height"));
						photo.setLink(imageLink);
						photoList.add(photo);
					}
				}
				place.setPhotos(photoList);
			}
		} catch (JSONException e) {
			Logger.error("Error processing JSON results", e);
		}

		return place;
	}

	private static String getImageLink(String photoRef) {
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		String link = "";
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append("/photo");
			sb.append("?maxwidth=400");
			sb.append("&photoreference=" + photoRef);
			sb.append("&key=" + API_KEY);
			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			link = conn.getURL().toString();
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
		return link;
	}

	public CompletionStage<List<Place>> searchNearBy(String type, String longitude, String latitude) {
		if ("BANK_AGENCY".equals(type)) {
			return supplyAsync(() -> search("Vietinbank", longitude, latitude, 5000));
		} else if ("ATM".equals(type)) {
			return supplyAsync(() -> search("Mobile World supermarket", longitude, latitude, 5000));
			//return supplyAsync(() -> search("ATM Vietinbank", longitude, latitude, 50000));
		} else {
			return supplyAsync(() -> searchAdLocationsNearby(longitude, latitude));
		}
		//return search("Maritime bank", 21.0318272, 105.787884, 100000);
	}

	public List<Place> searchAdLocationsNearby(String longitude, String latitude) {
		List<AdLocation> adLocations = locationRepository.findAdLocationsNearBy(longitude, latitude);
		List<Place> places = new ArrayList<Place>();
		for (AdLocation adLocation : adLocations) {
			Place place = new Place();
			place.setPlaceId(adLocation.getPlaceId());
			place.setName(adLocation.getName());
			place.setAddress(adLocation.getAddress());
			place.setLongitude(adLocation.getLng());
			place.setLatitude(adLocation.getLat());
			place.setDistance(distance(Double.valueOf(adLocation.getLat()), Double.valueOf(adLocation.getLng()), Double.valueOf(latitude), Double.valueOf(longitude)));
//			Place placeDetail = details(place.getPlaceId());
//			place.setReviews(placeDetail.getReviews());
//			place.setWeekdayText(placeDetail.getWeekdayText());
//			place.setOpeningHour(placeDetail.getOpeningHour());
//			place.setPhotos(placeDetail.getPhotos());
			places.add(place);
		}
		return places;
		//return supplyAsync(() -> locationRepository.findAdLocationsNearBy(105.787884, 21.0318272));
	}

	public CompletionStage<String> checkAdLocationsNearby(String deviceToken, String registrationToken, String longitude, String latitude) throws IOException, InterruptedException  {
		List<AdLocation> adLocations = locationRepository.findAdLocationsNearBy( longitude, latitude);
		String regToken = userRepository.getRegistrationByDeviceToken(deviceToken, registrationToken);
		for (AdLocation adLocation : adLocations) {
			PushNotificationsHelper.pushAdNotificationToUsers(regToken, adLocation);
		}
		return supplyAsync(() -> "ok");
	}

	public CompletionStage<Place> details(String placeId) {
		return supplyAsync(() -> getDetails(placeId));
	}
}
