package com.fintech.adl.repo;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import com.fintech.adl.model.*;

import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

public class JPAAdvertismentRepository implements AdvertismentRepository {
	
    private final JPAApi jpaApi;
	
    @Inject
    public JPAAdvertismentRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

	@Override
	public Ad findAdByTemplate(String template) {
        List<Ad> ads = jpaApi.em().createQuery("Advertisment.findAdByTemplate").setParameter("template", template).getResultList();
        Ad ad = null;
        if (!ads.isEmpty()) {
            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(ads.size());
            ad = ads.get(index);
        }
		return ad;
	}

    public void saveImpression(long adId) {
        AdImpressions impression = new AdImpressions();
        Ad ad = jpaApi.em().find(Ad.class, adId);
        impression.setAd(ad);
        impression.setImpression(1);
        jpaApi.em().persist(impression);
    }

    public void saveClick(long adId, String deviceToken) {
        AdClicks click = new AdClicks();
        User user = jpaApi.em().createQuery("SELECT u FROM User u WHERE u.deviceToken = :deviceToken", User.class)
                .setParameter("deviceToken", deviceToken).getSingleResult();
        Ad ad = jpaApi.em().find(Ad.class, adId);
        click.setUser(user);
        click.setAd(ad);
        jpaApi.em().persist(click);
    }

    public void saveView(long adId, String deviceToken) {
        AdViews view = new AdViews();
        User user = jpaApi.em().createQuery("SELECT u FROM User u WHERE u.deviceToken = :deviceToken", User.class)
                .setParameter("deviceToken", deviceToken).getSingleResult();
        Ad ad = jpaApi.em().find(Ad.class, adId);
        view.setUser(user);
        view.setAd(ad);
        jpaApi.em().persist(view);
    }
}
