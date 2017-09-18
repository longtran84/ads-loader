package com.fintechviet.content.service;

import com.fintechviet.content.dto.News;
import com.fintechviet.content.respository.ContentRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;


public class ContentService {
	private final ContentRepository contentRepository;

	@Inject
	public ContentService(ContentRepository contentRepository){
		this.contentRepository = contentRepository;
	}

	public List<News> getNewsByUserInterest(String deviceToken) {
		List<News> newsDTO = new ArrayList<News>();
		List<com.fintechviet.content.model.News> newsList = contentRepository.getNewsByUserInterest(deviceToken);
		for(com.fintechviet.content.model.News news : newsList) {
			News neDTO =  new News();
			neDTO.setTitle(news.getTitle());
			neDTO.setShortDescription(news.getShortDescription());
			neDTO.setImageLink(news.getImageLink());
			neDTO.setLink(news.getLink());
			neDTO.setNewsCategoryCode(news.getNewsCategory().getCode());
			newsDTO.add(neDTO);
		}
		return newsDTO;
	}

	public CompletionStage<String> saveImpression() {
		return contentRepository.saveImpression();
	}

	public CompletionStage<String> saveClick() {
		return contentRepository.saveClick();
	}

}
