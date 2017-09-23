package com.fintechviet.content.service;

import com.fintechviet.content.dto.News;
import com.fintechviet.content.dto.NewsCategory;
import com.fintechviet.content.respository.ContentRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;



public class ContentService {
	private final ContentRepository contentRepository;
	static final int limitResult = 500;

	@Inject
	public ContentService(ContentRepository contentRepository){
		this.contentRepository = contentRepository;
	}

	public CompletionStage<String> saveImpression() {
		return contentRepository.saveImpression();
	}

	public CompletionStage<String> saveClick() {
		return contentRepository.saveClick();
	}
	
	private List<News> convertToDto(List<com.fintechviet.content.model.News> newsModelList){
		List<News> newsDtoList = new ArrayList<>();
		for(com.fintechviet.content.model.News news : newsModelList) {
			News neDTO =  new News();
			neDTO.setId(news.getId());
			neDTO.setTitle(news.getTitle());
			neDTO.setShortDescription(news.getShortDescription());
			neDTO.setImageLink(news.getImageLink());
			neDTO.setLink(news.getLink());
			neDTO.setNewsCategoryCode(news.getNewsCategory().getCode());
			neDTO.setCreatedDate(news.getCreatedDate());
			newsDtoList.add(neDTO);
		}
		return newsDtoList;
	}
	
	public CompletionStage<List<NewsCategory>> getCategoriesList() throws InterruptedException, ExecutionException{
		CompletableFuture<List<com.fintechviet.content.model.NewsCategory>> cateListFuture =  
				supplyAsync(() -> contentRepository.getAllCategories());
		List<com.fintechviet.content.model.NewsCategory> cateList = cateListFuture.get();
		return supplyAsync(() -> convertCategoriesToDto(cateList));
		
	}
	
	private List<NewsCategory> convertCategoriesToDto(List<com.fintechviet.content.model.NewsCategory> categoriesList){
		List<NewsCategory> categoryDtoList = new ArrayList<>();
		for(com.fintechviet.content.model.NewsCategory cate : categoriesList) {
			NewsCategory cateDto =  new NewsCategory();
			cateDto.setCode(cate.getCode());
			cateDto.setName(cate.getName());
			cateDto.setImageFile(cate.getImage());
			categoryDtoList.add(cateDto);
		}
		return categoryDtoList;
	}

	/**
	 * 
	 * @param deviceToken
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public CompletionStage<List<News>> getNewsByUserInterest(String deviceToken, Date fromDate, Date toDate) throws InterruptedException, ExecutionException {
		long t0 = System.currentTimeMillis();
		List<Long> categoryList = contentRepository.getNumberOfUserInterest(deviceToken);
		List<News> newList = new ArrayList<>();
		List<CompletableFuture<List<com.fintechviet.content.model.News>>> pendingTask = new ArrayList<>();
		for(int i =0; i< categoryList.size(); i++){
			Long cateId = categoryList.get(i);
			CompletableFuture<List<com.fintechviet.content.model.News>> newsTrunkFuture =
					supplyAsync(() -> contentRepository.getNewsByUserInterest(deviceToken, cateId, fromDate, toDate));
			pendingTask.add(newsTrunkFuture);
		}
		for(CompletableFuture<List<com.fintechviet.content.model.News>> futureTask: pendingTask){
			newList.addAll(convertToDto(futureTask.get()));
		}
		long t1 = System.currentTimeMillis();
		System.out.println("Total time: " + (t1 - t0) );
		return supplyAsync(() -> newList);
	}

}
