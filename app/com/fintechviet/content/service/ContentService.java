package com.fintechviet.content.service;

import com.fintechviet.content.dto.News;
import com.fintechviet.content.dto.NewsCategory;
import com.fintechviet.content.respository.ContentRepository;
import com.fintechviet.utils.CommonUtils;
import com.fintechviet.utils.DateUtils;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import static java.util.concurrent.CompletableFuture.supplyAsync;



public class ContentService {
	private final ContentRepository contentRepository;
	static final int limitResult = 500;
	private static String CRAWLER_ENPOINT = "http://192.168.100.107:3689/solr/Crawler";
	private static String ID = "id";
	private static String CATEGORY_CODE = "MaChuyenMuc";
	private static String SOURCE_NAME = "TenMien";
	private static String TITLE = "TieuDe";
	private static String CONTENT = "NoiDung";
	private static String LINK = "DuongDan";
	private static String IMAGE_LINK = "AnhDaiDien";
	private static String PUBLISH_DATE = "NgayDangTin";
	private static String CRAWLER_DATE = "NgayCrawler";
	private static int ROWS = 100;

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
		String parentThreadName = Thread.currentThread().getName();
		System.out.println("["+ parentThreadName +"] Started ");
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
		System.out.println("["+ parentThreadName +"]Total time: " + (t1 - t0) );
		return supplyAsync(() -> newList);
	}
	



	public List<News> getNewsFromCrawler(List<String> interest, Date fromDate, Date toDate, Integer pageIndex) {
		String startTime = DateUtils.convertDateToStringUTC(fromDate);
		String endTime = DateUtils.convertDateToStringUTC(toDate);
		List<News> newsList = new ArrayList<>();
		try {
			SolrClient client = new HttpSolrClient.Builder(CRAWLER_ENPOINT).build();
			String queryStr = CATEGORY_CODE + ":" + CommonUtils.convertListToString(interest);
			SolrQuery query = new SolrQuery();
			query.setQuery(queryStr);
			String filterQueryStr = CRAWLER_DATE + ":[" + startTime + " TO " + endTime + "]";
			filterQueryStr = filterQueryStr.replaceAll("\\+", "");
			query.addFilterQuery(filterQueryStr);
			query.setFields(ID, CATEGORY_CODE, SOURCE_NAME, TITLE, CONTENT, LINK, IMAGE_LINK, PUBLISH_DATE, CRAWLER_DATE);
			query.setStart((pageIndex - 1) * ROWS);
			query.setRows(ROWS);
			query.setSort(CRAWLER_DATE, SolrQuery.ORDER.desc);
			query.set("defType", "edismax");

			QueryResponse response = client.query(query);
			SolrDocumentList results = response.getResults();

			for (SolrDocument document : results) {
				News news = new News();
				for (Iterator<Map.Entry<String, Object>> i = document.iterator(); i.hasNext();) {
					Map.Entry<String, Object> element = i.next();
					String key = element.getKey().toString();
					String value = element.getValue().toString();
					if (ID.equals(key)) {
						news.setId(Long.valueOf(value));
					} else if(CATEGORY_CODE.equals(key)) {
						news.setNewsCategoryCode(value);
					} else if(TITLE.equals(key)) {
						news.setTitle(value);
					} else if(CONTENT.equals(key)) {
						news.setShortDescription(value);
					} else if(LINK.equals(key)) {
						news.setLink(value);
					} else if(IMAGE_LINK.equals(key)) {
						value = value.replaceAll("localhost", "222.252.16.132");
						news.setImageLink(value);
					} else if(CRAWLER_DATE.equals(key)) {
						news.setCreatedDate((Date)element.getValue());
					}
				}
				newsList.add(news);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return newsList;
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
	public CompletionStage<List<News>> getNewsByUserInterestFromCrawler(String deviceToken, Date fromDate, Date toDate, Integer page) throws InterruptedException, ExecutionException {
		List<String> categoryList = contentRepository.getUserInterests(deviceToken);
		return supplyAsync(() -> getNewsFromCrawler(categoryList, fromDate, toDate, page));
	}

}
