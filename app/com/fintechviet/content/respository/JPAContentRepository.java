package com.fintechviet.content.respository;

import com.fintechviet.content.ContentExecutionContext;
import com.fintechviet.content.model.ContentClicks;
import com.fintechviet.content.model.ContentImpressions;
import com.fintechviet.content.model.News;
import com.fintechviet.content.model.NewsCategory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.List;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.ArrayList;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    public List<News> getNewsByUserInterest(String deviceToken, Long cateId, Long lastNewsId, int offset) {
        try {
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
	
	private List<News> getNewsByUserInterestByTrunk(EntityManager em, String deviceToken, Long cateId, Long lastNewsId, int offset){
        String queryStr = "SELECT n from News n WHERE n.newsCategory.id = " + cateId;
        		if (null != lastNewsId){
        			queryStr+= " AND n.id < " + lastNewsId; 
        		}
        		queryStr += " ORDER BY n.createdDate desc";
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
            String queryStr = "SELECT i.newsCategoryId FROM MobileUserInterestItems i WHERE i.mobileUserId = "
            		+ "(SELECT t.userMobile.id FROM UserDeviceToken t WHERE t.deviceToken = :deviceToken)";
            Query query = em.createQuery(queryStr).setParameter("deviceToken", deviceToken);
            return  (List<Long>)query.getResultList();
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
    public List<News> getNewsByUserInterest2(String deviceToken, Long cateId, Date fromDate, Date toDate) {
        try {
            List<News> newsList = new ArrayList<>();
            newsList = wrap(em -> getNewsByUserInterestByTrunk2(em, deviceToken, cateId, fromDate, toDate));
            System.out.println("return trunk of news list size: " + newsList.size());
            return newsList;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private List<News> getNewsByUserInterestByTrunk2(EntityManager em, String deviceToken, Long cateId, Date fromDate, Date toDate){
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

}
