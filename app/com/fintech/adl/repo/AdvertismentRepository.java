package com.fintech.adl.repo;

import java.util.List;

import com.fintech.adl.model.Ad;
import com.google.inject.ImplementedBy;

import javax.persistence.NamedQuery;

@ImplementedBy(JPAAdvertismentRepository.class)
public interface AdvertismentRepository{
    Ad findAdByTemplate(String template);
    void saveImpression(long adId);
    void saveClick(long adId, String deviceToken);
    void saveView(long adId, String deviceToken);
}
