package vn.fintechviet.notification;

import vn.fintechviet.ad.model.Ad;
import vn.fintechviet.content.dto.News;
import vn.fintechviet.location.model.AdLocation;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by tungn on 8/5/2017.
 */
public class PushNotificationsHelper {
    private final static String AUTH_KEY_FCM = "AIzaSyDJfLSg6HEBQyVnt7_oRcTa3B5pt50ubv8";
    private final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private final static String DOMAIN = "http://222.252.16.132:9000";

    public static String sendPushAds(Ad ad)
            throws IOException {
        String result = "";
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        try {
            json.put("to", "/topics/ads");
            //JSONObject info = new JSONObject();
            //info.put("title", "ads title"); // Notification title
            //info.put("body", "Advertising from Fintechviet"); // Notification

            JSONObject data = new JSONObject();

            data.put("message", "Advertising from SMA");
            data.put("image", ad.getCreative().getImageLink());
            data.put("impressionUrl", DOMAIN + "/ad/impression/" + ad.getId());
            data.put("clickUrl", ad.getCreative().getClickUrl());
            data.put("trackingUrl", DOMAIN + "/ad/click?adId=" + ad.getId());

            // body
            //json.put("notification", info);
            json.put("data", data);

            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            result = "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            result = "FAILURE";
        }
        System.out.println("FCM Ads Notification is sent successfully");

        return result;

    }

    public static String pushAdNotificationToUsers(String registrationId, AdLocation adLocation)
            throws IOException {
        String result = "";
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        try {
            json.put("to", registrationId);

            JSONObject data = new JSONObject();

            UUID uuid = UUID.randomUUID();

            data.put("id", uuid);
            data.put("type", "AD_LOCATION_NOTIFICATION");
            data.put("message", adLocation.getAdContent());
            data.put("image", adLocation.getCreative().getImageLink());
            data.put("impressionUrl", "");
            data.put("clickUrl", "");
            data.put("trackingUrl", "");
            data.put("name", adLocation.getName());
            data.put("address", adLocation.getAddress() != null ? adLocation.getAddress() : "");
            data.put("longitude", adLocation.getLng());
            data.put("latitude", adLocation.getLat());

            // body
            //json.put("notification", info);
            json.put("data", data);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(json.toString());
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            result = "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            result = "FAILURE";
        }
        System.out.println("FCM Ad Location Notification is sent successfully");

        return result;

    }

    public static String pushLatestNewsToUsers(String registrationId, News news)
            throws IOException {
        String result = "";
        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        try {
            json.put("to", registrationId);

            JSONObject data = new JSONObject();

            UUID uuid = UUID.randomUUID();

            data.put("id", uuid);
            data.put("type", "LATEST_NEWS_NOTIFICATION");
            data.put("message", news.getTitle());
            data.put("image",news.getImageLink());
            data.put("newsId", news.getId());

            // body
            //json.put("notification", info);
            json.put("data", data);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(json.toString());
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            result = "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            result = "FAILURE";
        }
        System.out.println("FCM Latest News Notification is sent successfully");

        return result;

    }
}
