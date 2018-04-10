package com.fintechviet.ad.repository;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.fintechviet.ad.model.Ad;
import com.fintechviet.ad.model.AdImpressions;
import com.fintechviet.ad.model.AppAd;
import com.google.inject.ImplementedBy;

@ImplementedBy(JPAAdvertismentRepository.class)
public interface AdvertismentRepository{
    CompletionStage<Ad> findAdByTemplate(String template, int adTypeId, String deviceToken);
    CompletionStage<List<Ad>> getTopAdv(String deviceToken);
    CompletionStage<AdImpressions> saveImpression(long adId);
    CompletionStage<String> saveClick(long adId, String deviceToken);
    CompletionStage<String> saveView(long adId, String deviceToken);
    CompletionStage<List<AppAd>> getListAppAd();
    CompletionStage<String> saveInstall(long appId, String deviceToken, String platform);
    Ad getAdByTemplate(String template, int adTypeId);
    boolean isAdClicked(String deviceToken, long adId);
}
