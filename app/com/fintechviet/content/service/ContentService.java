package com.fintechviet.content.service;

import com.fintechviet.content.dto.News;
import com.fintechviet.content.respository.ContentRepository;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;


public class ContentService {
	private final ContentRepository contentRepository;

	@Inject
	public ContentService(ContentRepository contentRepository){
		this.contentRepository = contentRepository;
	}

	public CompletionStage<List<News>> getNewsByUserInterest(String deviceToken) {
		List<News> newsDTO = new ArrayList<News>();
		CompletionStage<List<com.fintechviet.content.model.News>> newsList = contentRepository.getNewsByUserInterest(deviceToken);
		List<com.fintechviet.content.model.News> newListModel = new ArrayList<>();
		try {
			newListModel = newsList.toCompletableFuture().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		for(com.fintechviet.content.model.News news : newListModel) {
			News neDTO =  new News();
			neDTO.setTitle(news.getTitle());
			neDTO.setShortDescription(news.getShortDescription());
			neDTO.setImageLink(news.getImageLink());
			neDTO.setLink(news.getLink());
			neDTO.setNewsCategoryCode(news.getNewsCategory().getCode());
			newsDTO.add(neDTO);
		}
		return supplyAsync(() -> newsDTO);
	}

	public CompletionStage<String> saveImpression() {
		return contentRepository.saveImpression();
	}

	public CompletionStage<String> saveClick() {
		return contentRepository.saveClick();
	}

}
