package com.fintechviet.ad.repo;

import java.util.concurrent.CompletionStage;

import com.fintechviet.ad.model.Ad;
import com.google.inject.ImplementedBy;

@ImplementedBy(JPAAdvertismentRepository.class)
public interface AdvertismentRepository{
    CompletionStage<Ad> findAdByTemplate(String template);
    CompletionStage<String> saveImpression(long adId);
    CompletionStage<String> saveClick(long adId, String deviceToken);
    CompletionStage<String> saveView(long adId, String deviceToken);
}
