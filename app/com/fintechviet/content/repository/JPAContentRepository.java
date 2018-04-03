package com.fintechviet.content.repository;

import com.fintechviet.content.ContentExecutionContext;
import com.fintechviet.content.model.*;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.Date;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.ArrayList;

public class JPAContentRepository implements ContentRepository {

    private final JPAApi jpaApi;
    private final ContentExecutionContext ec;
    static final int limitResult = 3;

    @Inject
    public JPAContentRepository(JPAApi jpaApi, ContentExecutionContext ec) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    @Override
    public List<News> getNewsByAllCategories() {
        List<News> newsList = jpaApi.em().createQuery("SELECT n FROM News n", News.class).getResultList();
        return newsList;
    }

    @Override
    public CompletionStage<String> saveImpression() {
        return supplyAsync(() -> wrap(em -> saveImpression(em)), ec);
    }

    public String saveImpression(EntityManager em) {
        ContentImpressions impression = new ContentImpressions();
        em.persist(impression);
        return "ok";
    }

    @Override
    public CompletionStage<String> saveClick() {
        return supplyAsync(() -> wrap(em -> saveClick(em)), ec);
    }

    public String saveClick(EntityManager em) {
        ContentClicks click = new ContentClicks();
        em.persist(click);
        return "ok";
    }
    
    @Override
	public List<Long> getNumberOfUserInterest(String deviceToken){
        return wrap(em -> {
            String queryStr = "SELECT i.newsCategoryId FROM MobileUserInterestItems i WHERE i.mobileUserId = "
            		+ "(SELECT t.userMobile.id FROM UserDeviceToken t WHERE t.deviceToken = :deviceToken)";
            Query query = em.createQuery(queryStr).setParameter("deviceToken", deviceToken);
            return  (List<Long>)query.getResultList();
        });
        
	}

    @Override
    public List<NewsCategory> getUserInterests(String deviceToken) {
        return wrap(em -> {
            String queryStr = "SELECT nc FROM NewsCategory nc WHERE nc.id IN (SELECT i.newsCategoryId FROM MobileUserInterestItems i WHERE i.mobileUserId = "
                    + "(SELECT t.userMobile.id FROM UserDeviceToken t WHERE t.deviceToken = :deviceToken))";
            Query query = em.createQuery(queryStr).setParameter("deviceToken", deviceToken);
            return  (List<NewsCategory>)query.getResultList();
        });

    }
    
    @Override
    public List<NewsCategory> getAllCategories(){
        return wrap(em -> {
            String queryStr = "SELECT c FROM NewsCategory c ";
            Query query = em.createQuery(queryStr);
            return  (List<NewsCategory>)query.getResultList();
        });
	}

    @Override
    public List<News> getNewsByUserInterest(String deviceToken, Long cateId, Date fromDate, Date toDate) {
        try {
        	System.out.println("Start getting news for " + cateId);
        	long t0 = System.currentTimeMillis();
            List<News> newsList = new ArrayList<>();
            newsList = wrap(em -> getNewsByUserInterestByTrunk(em, deviceToken, cateId, fromDate, toDate));
            System.out.println("news number of category " + cateId + " :" + newsList.size());
            long t1 = System.currentTimeMillis();
            System.out.println("query for  " + cateId + ":" + (t1 - t0) );
            return newsList;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private List<News> getNewsByUserInterestByTrunk(EntityManager em, String deviceToken, Long cateId, Date fromDate, Date toDate){
        String queryStr = "SELECT n from News n WHERE n.newsCategory.id = " + cateId;
        queryStr += " AND n.createdDate > :fromDate ";
        if(toDate != null){
            queryStr += " AND n.createdDate < :toDate ";
        }

        Query query = em.createQuery(queryStr, News.class);
                query.setParameter("fromDate", fromDate);
        if(toDate != null){
            query.setParameter("toDate", toDate);
        }
        return query.getResultList();
    }

    @Override
    public CompletionStage<List<Game>> getGames(){
        return supplyAsync(() -> wrap(em -> {
            return  (List<Game>)em.createQuery("SELECT g FROM Game g WHERE g.status = 'ACTIVE'").getResultList();
        }), ec);
    }

}
