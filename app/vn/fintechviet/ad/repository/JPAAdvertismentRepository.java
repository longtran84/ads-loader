package vn.fintechviet.ad.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import vn.fintechviet.ad.AdExecutionContext;

import vn.fintechviet.ad.model.*;
import vn.fintechviet.ad.model.*;
import vn.fintechviet.content.model.NewsCategory;
import vn.fintechviet.user.model.User;
import org.apache.commons.lang3.StringUtils;
import play.Configuration;
import play.db.jpa.JPAApi;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAAdvertismentRepository implements AdvertismentRepository {
	
    private final JPAApi jpaApi;
    private final AdExecutionContext ec;
    private static int ADVS = 5;
    @Inject
    private Configuration configuration;
	
    @Inject
    public JPAAdvertismentRepository(JPAApi jpaApi, AdExecutionContext ec) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

	@Override
	public CompletionStage<Ad> findAdByTemplate(String template, int adTypeId, String deviceToken) {
        return supplyAsync(() -> wrap(em -> findAdByTemplate(em, template, adTypeId, deviceToken)), ec);
	}

    private Ad findAdByTemplate(EntityManager em, String template, int adTypeId, String deviceToken) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)", User.class)
                .setParameter("deviceToken", deviceToken).getSingleResult();
        StringBuilder queryStr = new StringBuilder("SELECT ad FROM Ad ad WHERE ad.flight.id IN ");
        queryStr.append("(SELECT fl.id FROM Flight fl WHERE fl.startDate <= CURRENT_DATE AND (fl.endDate >= CURRENT_DATE OR fl.endDate IS NULL))");
        queryStr.append("AND ad.impressions > (SELECT COUNT(adi.id) FROM AdImpressions adi WHERE adi.ad.id = ad.id) AND ad.creative.template = :template AND ad.status = 'ACTIVE'");
        if (adTypeId != 0 && !template.equalsIgnoreCase("video")) {
            queryStr.append(" AND ad.creative.adType.id = :adTypeId");
        }

        Query query = em.createQuery(queryStr.toString());
        query.setParameter("template", template);

        if (adTypeId != 0 && !template.equalsIgnoreCase("video")) {
            query.setParameter("adTypeId", adTypeId);
        }

        List<Ad> ads = query.getResultList();
        List<Ad> adsResult = filterByUser(ads, user);

        Ad ad = null;
        if (!adsResult.isEmpty()) {
            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(adsResult.size());
            ad = adsResult.get(index);
        } else {
            if (!ads.isEmpty()) {
                Random randomGenerator = new Random();
                int index = randomGenerator.nextInt(ads.size());
                ad = ads.get(index);
            }
        }
        return ad;
    }

    @Override
    public Ad getAdByTemplate(String template, int adTypeId) {
        return wrap(em -> {
            StringBuilder queryStr = new StringBuilder("SELECT ad FROM Ad ad WHERE ad.flight.startDate <= CURRENT_DATE AND (ad.flight.endDate >= CURRENT_DATE OR ad.flight.endDate IS NULL) ");
            queryStr.append("AND ad.impressions > (SELECT COUNT(adi.id) FROM AdImpressions adi WHERE adi.ad.id = ad.id) AND ad.creative.template = :template AND ad.status = 'ACTIVE'");
            if (adTypeId != 0) {
                queryStr.append(" AND ad.creative.adType.id = :adTypeId");
            }

            Query query = em.createQuery(queryStr.toString());
            query.setParameter("template", template);
            if (adTypeId != 0) {
                query.setParameter("adTypeId", adTypeId);
            }

            List<Ad> ads = query.getResultList();
            Ad ad = null;
            if (!ads.isEmpty()) {
                Random randomGenerator = new Random();
                int index = randomGenerator.nextInt(ads.size());
                ad = ads.get(index);
            }
            return ad;
        });
    }

    private List<Ad> filterByUser(List<Ad> ads, User user) {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        List<String> userInterests = new ArrayList<String>();
        for (NewsCategory newsCategory : user.getNewsCategories()) {
            userInterests.add(newsCategory.getCode());
        }

        List<Ad> adsResult = new ArrayList<Ad>();
        boolean isContainInterest = false;
        for (Ad ad : ads) {
            Flight flight = ad.getFlight();
            if (StringUtils.isNotEmpty(flight.getUserInterest())) {
                for (String userInterest : userInterests) {
                    if (flight.getUserInterest().contains(userInterest)) {
                        isContainInterest = true;
                        break;
                    }
                }
            }
            if ((StringUtils.isNotEmpty(flight.getUserGender()) && flight.getUserGender().equals(user.getGender()))
                    || (flight.getUserAgeFrom() != 0 && flight.getUserAgeTo() != 0 && flight.getUserAgeFrom() <= (currentYear - user.getDob()) && flight.getUserAgeTo() >= (currentYear - user.getDob()))
                    || (StringUtils.isNotEmpty(flight.getUserLocation()) && flight.getUserLocation().contains(user.getLocation()))
                    || (StringUtils.isNotEmpty(flight.getUserInterest()) && isContainInterest)) {
                adsResult.add(ad);
            }
        }

        return adsResult;
    }

    @Override
    public CompletionStage<List<Ad>> getTopAdv(String deviceToken) {
        return supplyAsync(() -> wrap(em -> getTopAdv(em, deviceToken)), ec);
    }

    private List<Ad> getTopAdv(EntityManager em, String deviceToken) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)", User.class)
                .setParameter("deviceToken", deviceToken).getSingleResult();
        StringBuilder queryStr = new StringBuilder("SELECT ad FROM Ad ad WHERE ad.flight.id IN ");
        queryStr.append("(SELECT fl.id FROM Flight fl WHERE fl.startDate <= CURRENT_DATE AND (fl.endDate >= CURRENT_DATE OR fl.endDate IS NULL))");
        queryStr.append("AND ad.impressions > (SELECT COUNT(adi.id) FROM AdImpressions adi WHERE adi.ad.id = ad.id) AND ad.creative.template = 'image' AND ad.creative.adType.id = 1 AND ad.status = 'ACTIVE'");

        Query query = em.createQuery(queryStr.toString());
        query.setMaxResults(ADVS);

        List<Ad> ads = query.getResultList();
        List<Ad> adsResult = filterByUser(ads, user);
        if (adsResult.size() > 0 ) {
            return adsResult;
        }
        return ads;
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
    public boolean isAdClicked(String deviceToken, long adId) {
        return wrap(em -> {
            List<Ad> adClicks = em.createQuery("SELECT a FROM AdClicks a WHERE a.user.id = (SELECT t.userMobile.id FROM UserDeviceToken t WHERE t.deviceToken = :deviceToken) AND a.ad.id = :adId")
                    .setParameter("deviceToken", deviceToken).setParameter("adId", adId).getResultList();
            return adClicks.size() > 0 ? true : false;
        });
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
        int advNumber = Integer.parseInt(configuration.getString("adv.display.number"));
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
