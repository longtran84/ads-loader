package com.fintechviet.ad.repo;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.fintechviet.ad.AdExecutionContext;

import com.fintechviet.ad.model.*;
import com.fintechviet.user.model.User;
import play.db.jpa.JPAApi;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAAdvertismentRepository implements AdvertismentRepository {
	
    private final JPAApi jpaApi;
    private final AdExecutionContext ec;
	
    @Inject
    public JPAAdvertismentRepository(JPAApi jpaApi, AdExecutionContext ec) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

	@Override
	public CompletionStage<Ad> findAdByTemplate(String template) {
        return supplyAsync(() -> wrap(em -> findAdByTemplate(em, template)), ec);
	}

    private Ad findAdByTemplate(EntityManager em, String template) {
        List<Ad> ads = em.createQuery("SELECT ad FROM Ad ad WHERE ad.flight.startDate <= CURRENT_DATE AND (ad.flight.endDate >= CURRENT_DATE OR ad.flight.endDate IS NULL) " +
                                             "AND ad.impressions > (SELECT COUNT(adi.id) FROM AdImpressions adi WHERE adi.ad.id = ad.id) AND ad.creative.template = :template AND ad.status = 'ACTIVE'")
                         .setParameter("template", template)
                         .getResultList();
        Ad ad = null;
        if (!ads.isEmpty()) {
            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(ads.size());
            ad = ads.get(index);
        }
        return ad;
    }

    @Override
    public CompletionStage<AdImpressions> saveImpression(long adId) {
        return supplyAsync(() -> wrap(em -> saveImpression(em, adId)), ec);
    }

    private AdImpressions saveImpression(EntityManager em, long adId) {
        AdImpressions impression = new AdImpressions();
        Ad ad = em.find(Ad.class, adId);
        impression.setAd(ad);
        impression.setImpression(1);
        em.persist(impression);
        return impression;
    }

    @Override
    public CompletionStage<String> saveClick(long adId, String deviceToken) {
        return supplyAsync(() -> wrap(em -> saveClick(em, adId, deviceToken)), ec);
    }

    private String saveClick(EntityManager em, long adId, String deviceToken) {
        AdClicks click = new AdClicks();
        User user = em.createQuery("SELECT u FROM User u WHERE u.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)", User.class)
                .setParameter("deviceToken", deviceToken).getSingleResult();
        Ad ad = jpaApi.em().find(Ad.class, adId);
        click.setUser(user);
        click.setAd(ad);
        em.persist(click);
        return "ok";
    }

    @Override
    public CompletionStage<String> saveView(long adId, String deviceToken) {
        return supplyAsync(() -> wrap(em -> saveView(em, adId, deviceToken)), ec);
    }

    private String saveView(EntityManager em, long adId, String deviceToken) {
        AdViews view = new AdViews();
        User user = em.createQuery("SELECT u FROM User u WHERE u.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)", User.class)
                .setParameter("deviceToken", deviceToken).getSingleResult();
        Ad ad = em.find(Ad.class, adId);
        view.setUser(user);
        view.setAd(ad);
        em.persist(view);
        return "ok";
    }

    @Override
    public CompletionStage<List<AppAd>> getListAppAd() {
        return supplyAsync(() -> wrap(em -> getListAppAd(em)), ec);
    }

    private List<AppAd> getListAppAd(EntityManager em) {
        List<AppAd> appAds = em.createQuery("SELECT apa FROM AppAd apa WHERE apa.campaign.startDate <= CURRENT_DATE AND (apa.campaign.endDate >= CURRENT_DATE OR apa.campaign.endDate IS NULL) " +
                                      "AND apa.status = 'ACTIVE'")
                .getResultList();
        return appAds;
    }

    @Override
    public CompletionStage<String> saveInstall(long appId, String deviceToken, String platform) {
        return supplyAsync(() -> wrap(em -> saveInstall(em, appId, deviceToken, platform)), ec);
    }

    private String saveInstall(EntityManager em, long appId, String deviceToken, String platform) {
        AppAdInstalls install = new AppAdInstalls();
        install.setAppAdId(appId);
        install.setDeviceToken(deviceToken);
        install.setPlatform(platform);
        em.persist(install);
        return "ok";
    }
}
