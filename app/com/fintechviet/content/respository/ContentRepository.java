package com.fintechviet.content.respository;

import com.fintechviet.content.model.News;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JPAContentRepository.class)
public interface ContentRepository {
	CompletionStage<List<News>> getNewsByUserInterest(String deviceToken);
	CompletionStage<List<News>> getNewsByUserInterestByTrunk(String deviceToken, Long cateId, Long lastNewsId, int offset);
    List<News> getNewsByAllCategories();
    CompletionStage<String> saveImpression();
    CompletionStage<String> saveClick();
	List<Long> getNumberOfUserInterest(String deviceToken);
	List<News> getNewsByUserInterestByTrunk2(String deviceToken, Long cateId, Long lastNewsId,
			int offset);
}
