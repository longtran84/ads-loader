package com.fintechviet.content.respository;

import com.fintech.adl.repo.JPAAdvertismentRepository;
import com.fintechviet.content.model.News;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JPAContentRepository.class)
public interface ContentRepository {
    List<News> getNewsByUserInterest(String deviceToken);
    List<News> getNewsByAllCategories();
    CompletionStage<String> saveImpression();
    CompletionStage<String> saveClick();
}
