package com.fintechviet.content.service;

import com.fintechviet.content.dto.News;
import com.fintechviet.content.respository.ContentRepository;

import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static java.util.concurrent.CompletableFuture.supplyAsync;



public class ContentService {
	private final ContentRepository contentRepository;
	static final int limitResult = 3;

	@Inject
	public ContentService(ContentRepository contentRepository){
		this.contentRepository = contentRepository;
	}

	/**
	 * 
	 * @param deviceToken
	 * @param mappedCateIds cates Id list mapped with startNewsIds
	 * @param startNewsIds
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public CompletionStage<List<News>> getNewsByUserInterest(String deviceToken, String mappedCateIds, String startNewsIds) throws InterruptedException, ExecutionException {
		CompletableFuture<List<News>> future = new CompletableFuture<>();
		CompletionStage<List<News>> newsListFuture = calculateTrunk(deviceToken, mappedCateIds, startNewsIds);
//		Consumer<List<News>> complete=future::complete;
//		newsListFuture.whenComplete((value, exception) -> {future.complete(value);});
//		newsListFuture.thenAccept(complete);
		try {
			List<News> res = newsListFuture.toCompletableFuture().get();
			return supplyAsync(() -> res);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newsListFuture;
	}

	public CompletionStage<String> saveImpression() {
		return contentRepository.saveImpression();
	}

	public CompletionStage<String> saveClick() {
		return contentRepository.saveClick();
	}
	
	private CompletionStage<List<News>> calculateTrunk(String deviceToken, String mappedCateIds,String lastNewsIds) throws InterruptedException, ExecutionException{
		List<Long> categoryList = contentRepository.getNumberOfUserInterest(deviceToken);
		int trunkSize = Math.round(limitResult/categoryList.size()); 
		String[] newsIdsArray = lastNewsIds.split(",");
		String[] cateIdsArray = mappedCateIds.split(",");
		List<News> newList = new ArrayList<>();
		List<CompletableFuture<List<com.fintechviet.content.model.News>>> pendingTask = new ArrayList<>();
		for(int i =0; i< categoryList.size(); i++){
			Long cateId = categoryList.get(i);
			int idx = Arrays.asList(cateIdsArray).indexOf(cateId.toString());
			
			if(idx >= 0){//input category existing on DB
				CompletableFuture<List<com.fintechviet.content.model.News>> newsTrunkFuture = supplyAsync(() -> contentRepository.getNewsByUserInterestByTrunk2(deviceToken, cateId, Long.valueOf(newsIdsArray[idx]), trunkSize));
				pendingTask.add(newsTrunkFuture);
			}else{
				CompletableFuture<List<com.fintechviet.content.model.News>> newsTrunkFuture = supplyAsync(() -> contentRepository.getNewsByUserInterestByTrunk2(deviceToken, cateId, null, trunkSize)) ;
				pendingTask.add(newsTrunkFuture);
			}
		}
		for(CompletableFuture<List<com.fintechviet.content.model.News>> futureTask: pendingTask){
			newList.addAll(convertToDto(futureTask.get()));
		}
		return supplyAsync(() -> newList);
 

	}
	
	private List<News> convertToDto(List<com.fintechviet.content.model.News> newsModelList){
		List<News> newsDtoList = new ArrayList<>();
		for(com.fintechviet.content.model.News news : newsModelList) {
			News neDTO =  new News();
			neDTO.setTitle(news.getTitle());
			neDTO.setShortDescription(news.getShortDescription());
			neDTO.setImageLink(news.getImageLink());
			neDTO.setLink(news.getLink());
			neDTO.setNewsCategoryCode(news.getNewsCategory().getCode());
			newsDtoList.add(neDTO);
		}
		return newsDtoList;
	}
	


}
