package com.fintech.adl.repo;

import java.util.List;
import java.util.concurrent.CompletionStage;

import com.fintech.adl.model.Ad;
import com.google.inject.ImplementedBy;

import javax.persistence.NamedQuery;

@ImplementedBy(JPAAdvertismentRepository.class)
public interface AdvertismentRepository{
    CompletionStage<Ad> findAdByTemplate(String template);
    CompletionStage<String> saveImpression(long adId);
    CompletionStage<String> saveClick(long adId, String deviceToken);
    CompletionStage<String> saveView(long adId, String deviceToken);
}
