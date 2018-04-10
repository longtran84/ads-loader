package com.fintechviet.content.repository;

import com.fintechviet.content.model.Game;
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
    CompletionStage<String> saveClick(String deviceToken, String newsId);
	List<Long> getNumberOfUserInterest(String deviceToken);
	List<NewsCategory> getAllCategories();
	List<News> getNewsByUserInterest(String deviceToken, Long cateId, Date fromDate, Date toDate);
    List<NewsCategory> getUserInterests(String deviceToken);
    CompletionStage<List<Game>> getGames();
    boolean isNewsClicked(String deviceToken, String newsId);
}
