package com.xianggole.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import com.xianggole.mapper.TbItemMapper;
import com.xianggole.pojo.TbItem;
import com.xianggole.pojo.TbItemExample;
import com.xianggole.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private SolrTemplate solrTemplate;
	
	public void importItemData() {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");
		
		List<TbItem> itemList = itemMapper.selectByExample(example );
		for (TbItem item : itemList) {
			System.out.println(item.getId()+" "+ item.getTitle()+" "+item.getPrice());
			Map map = JSON.parseObject(item.getSpec(),Map.class);
			item.setSpecMap(map);
		}
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
		System.out.println("....结束......");
		
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
		
		solrUtil.importItemData();
	}

}
