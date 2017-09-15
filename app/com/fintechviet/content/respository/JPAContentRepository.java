package com.fintechviet.content.respository;

import com.fintechviet.content.ContentExecutionContext;
import com.fintechviet.content.model.ContentClicks;
import com.fintechviet.content.model.ContentImpressions;
import com.fintechviet.content.model.News;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAContentRepository implements ContentRepository {

    private final JPAApi jpaApi;
    private final ContentExecutionContext ec;

    @Inject
    public JPAContentRepository(JPAApi jpaApi, ContentExecutionContext ec) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

	@Override
    public List<News> getNewsByUserInterest(String deviceToken) {
        List<News> newsList = jpaApi.em()
                              .createQuery("SELECT n FROM News n WHERE n.newCategory.id IN (SELECT nc.id FROM NewsCategory nc)", News.class).getResultList();
		return newsList;
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

    public void saveClick() {
        ContentClicks click = new ContentClicks();
        jpaApi.em().persist(click);
    }
}
