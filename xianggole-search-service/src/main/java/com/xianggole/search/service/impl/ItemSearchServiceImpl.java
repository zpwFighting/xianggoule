package com.xianggole.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.xianggole.pojo.TbItem;
import com.xianggole.search.service.ItemSearchService;

@Service(timeout=5000)
public class ItemSearchServiceImpl implements ItemSearchService{

	@Autowired
	private SolrTemplate solrTemplate;
	
	@Override
	public Map search(Map searchMap) {
		Map map = new HashMap();
		//空格处理
		String keywords = (String)searchMap.get("keywords");
		searchMap.put("keywords",keywords.replace(" ", "") );
		//1.查询列表
		map.putAll(searchList(searchMap));
		//2.分组查询商品分类列表
		List<String> categoryList = searchCategoryList(searchMap);
		map.put("categoryList", categoryList);
		
		//3.查询品牌和分类列表
		String category = (String) searchMap.get("category");
		if(!"".equals(category)) {
			   map.putAll(searchBrandAndSpecList(category));
		}else {
			if(categoryList.size()>0) {
				map.putAll(searchBrandAndSpecList(categoryList.get(0)));
			}
		}
		return map;
	}
	
	private Map searchList(Map searchMap) {
		Map map = new HashMap();
		/*
		Query query = new SimpleQuery("*:*");
		
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria );
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query , TbItem.class);
		
		map.put("rows", page.getContent());
		*/
		HighlightQuery query = new SimpleHighlightQuery();
		//构建高亮显示对象
	    HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
	    highlightOptions.setSimplePrefix("<em style='color:red'>");
	    highlightOptions.setSimplePostfix("</em>");
	    
	    //为查询对象设置高亮显示
		query.setHighlightOptions(highlightOptions);
	
		// 1.1关键字查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria );
		
		//1.2按照商品分类过滤
		if(!"".equals(searchMap.get("category"))) {
			
			FilterQuery filterQuery = new SimpleFilterQuery();
			Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			filterQuery.addCriteria(filterCriteria );
			query.addFilterQuery(filterQuery);
		}
		//1.3按照商品分类过滤
		if(!"".equals(searchMap.get("brand"))) {
			
			FilterQuery filterQuery = new SimpleFilterQuery();
			Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
			filterQuery.addCriteria(filterCriteria );
			query.addFilterQuery(filterQuery);
		}
		//1.4规格过滤
		if(searchMap.get("spec")!=null) {
          Map<String,String> specMap=(Map<String, String>) searchMap.get("spec");
          for(String key:specMap.keySet()) {
        	FilterQuery filterQuery = new SimpleFilterQuery();
  			Criteria filterCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
  			filterQuery.addCriteria(filterCriteria );
  			query.addFilterQuery(filterQuery);
          }
		}
		//1.5价格过滤
        if(!"".equals(searchMap.get("price"))) {
        	String[] price = ((String) searchMap.get("price")).split("-");;
        	if(!price[0].equals("0")) {//最低价格不等于0
        		FilterQuery filterQuery = new SimpleFilterQuery();
    			Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
    			filterQuery.addCriteria(filterCriteria );
    			query.addFilterQuery(filterQuery);
        	}
        	if(!price[1].equals("*")) {//最高价格不等于*
        		FilterQuery filterQuery = new SimpleFilterQuery();
    			Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
    			filterQuery.addCriteria(filterCriteria );
    			query.addFilterQuery(filterQuery);
        	}
			
		}
        //1.6分页
        Integer pageNo=(Integer)searchMap.get("pageNo");//获取页码
        if(pageNo==null) {
        	pageNo=1;
        }
        Integer pageSize = (Integer) searchMap.get("pageSize");//获取大小
        if(pageSize==null) {
        	pageSize=20;
        }
        query.setOffset((pageNo-1)*pageSize);//起始索引
        query.setRows(pageSize);

        
		//1.7排序
       
        String sortValue = (String)searchMap.get("sort");//升序还是降序
        String sortField = (String)searchMap.get("sortField");//排序字段
        
        if(!"".equals(sortValue) && sortValue!=null) {
        	if(sortValue.equals("ASC")) {
        		
        		Sort sort = new Sort(Sort.Direction.ASC,"item_"+sortField);
        		query.addSort(sort);
        	}else {
        		Sort sort = new Sort(Sort.Direction.DESC,"item_"+sortField);
        		query.addSort(sort);
        	}
        }
        
        
       
		
		//*******获取高亮结果集**********
		//高亮页对象
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
		//高亮入口集合
		List<HighlightEntry<TbItem>> entryList = page.getHighlighted();
		
		for (HighlightEntry<TbItem> entry:entryList) {
			List<Highlight> highlights = entry.getHighlights();
			if(highlights.size()>0 && highlights.get(0).getSnipplets().size()>0) {
				TbItem item = entry.getEntity();
				item.setTitle(highlights.get(0).getSnipplets().get(0));
			}
			
		}
		map.put("rows", page.getContent());
		map.put("totalPages", page.getTotalPages());//总页数
		map.put("total", page.getTotalElements());//总记录数
		return map;
	}
	
	
	private List searchCategoryList(Map searchMap) {
		List list = new ArrayList();
		
		Query query = new SimpleQuery("*:*");
		//关键字查询 相当于where
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria );
		
		//相当于group
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions );
		//取值
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query , TbItem.class);
		//获取分组结果对象
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		//分组入口页
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		//获取分组入口
		List<GroupEntry<TbItem>> content = groupEntries.getContent();
		
		for(GroupEntry<TbItem> entry :content) {
			list.add(entry.getGroupValue());
		}
		
		return list;
	}
	
	@Autowired
	private RedisTemplate redisTemplate;
	/**
	 * 查询品牌规格列表
	 * @param searchMap
	 * @return
	 */
	private Map searchBrandAndSpecList(String category) {
		Map map = new HashMap();
		
		//1.根据商品分类名称得到模板id
		Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
		if(templateId!=null) {
			
			//2.根据模板id获取品牌列表
			List brand = (List) redisTemplate.boundHashOps("BrandList").get(templateId);
			
			map.put("brandList", brand);
			
			//3.根据模板id获取规格列表
			List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
			
			map.put("specList", specList);
		}
		
		return map;
	}

	@Override
	public void importList(List list) {
		
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
		
	}

	@Override
	public void deleteByGoodsIds(List goodsIds) {

		Query query = new SimpleQuery("");
		Criteria criteria = new Criteria("item_goodsid").in(goodsIds);
		query.addCriteria(criteria );
		solrTemplate.delete(query );
		solrTemplate.commit();
	}

	
}
