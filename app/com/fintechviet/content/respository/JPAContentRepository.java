package com.fintechviet.content.respository;

import com.fintechviet.content.ContentExecutionContext;
import com.fintechviet.content.model.ContentClicks;
import com.fintechviet.content.model.ContentImpressions;
import com.fintechviet.content.model.News;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    public CompletionStage<List<News>> getNewsByUserInterest(String deviceToken) {
		return supplyAsync(() -> wrap(em -> getNewsByUserInterest(em, deviceToken)), ec);
	}
	
	@Override
    public CompletionStage<List<News>> getNewsByUserInterestByTrunk(String deviceToken, Long cateId,Long lastNewsId, int offset) {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CompletionStage<List<News>> newsTrunk = supplyAsync(() -> wrap(em -> getNewsByUserInterestByTrunk(em, deviceToken, cateId, lastNewsId, offset)), ec);
		System.out.println("Finish getting trunk for news" + lastNewsId);
		return newsTrunk;
	}
	
	@Override
    public List<News> getNewsByUserInterestByTrunk2(String deviceToken, Long cateId,Long lastNewsId, int offset) {
        try {
			String queryStr = "SELECT n from News n WHERE n.newsCategory.id = " + cateId + " AND n.id < " + lastNewsId + " ORDER BY n.createdDate desc";
			List<News> newsList = new ArrayList<>();
			newsList = wrap(em -> getNewsByUserInterestByTrunk(em, deviceToken, cateId, lastNewsId, offset));
			System.out.println("return trunk of news list size: " + newsList.size());
			return newsList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}	
	
	public List<News> getNewsByUserInterest(EntityManager em, String deviceToken){
        String queryStr = "SELECT n from News n WHERE n.newsCategory.id IN "
        		+ "(SELECT i.newsCategoryId FROM MobileUserInterest i "
        		+ " WHERE i.mobileUserId = (SELECT u.id FROM User u WHERE u.deviceToken = '" + deviceToken + "')) "
        		+ "ORDER BY n.createdDate";
        TypedQuery<News> query = em.createQuery(queryStr, News.class);
        query.setMaxResults(limitResult);
        return query.getResultList();
	}
	
	public List<News> getNewsByUserInterestByTrunk(EntityManager em, String deviceToken, Long cateId, Long lastNewsId, int offset){
        String queryStr = "SELECT n from News n WHERE n.newsCategory.id = " + cateId + " AND n.id < " + lastNewsId + " ORDER BY n.createdDate desc";
        TypedQuery<News> query = em.createQuery(queryStr, News.class);
        query.setMaxResults(offset);
        return query.getResultList();
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
            String queryStr = "SELECT i.newsCategoryId FROM MobileUserInterest i WHERE i.mobileUserId = "
            		+ "(SELECT u.id FROM User u WHERE u.deviceToken = '" + deviceToken + "')";
            Query query = em.createQuery(queryStr);
            return  (List<Long>)query.getResultList();
        });
        
	}

}
