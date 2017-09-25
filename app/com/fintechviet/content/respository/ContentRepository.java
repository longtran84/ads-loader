package com.fintechviet.content.respository;

import com.fintechviet.content.model.News;
import com.fintechviet.content.model.NewsCategory;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Date;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JPAContentRepository.class)
public interface ContentRepository {
    List<News> getNewsByAllCategories();
    CompletionStage<String> saveImpression();
    CompletionStage<String> saveClick();
	List<Long> getNumberOfUserInterest(String deviceToken);
	List<NewsCategory> getAllCategories();
	public List<News> getNewsByUserInterest(String deviceToken, Long cateId, Date fromDate, Date toDate);
    List<String> getUserInterests(String deviceToken);
}
