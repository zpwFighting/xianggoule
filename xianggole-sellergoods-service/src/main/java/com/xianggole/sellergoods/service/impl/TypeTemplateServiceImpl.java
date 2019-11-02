package com.xianggole.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xianggole.mapper.TbSpecificationOptionMapper;
import com.xianggole.mapper.TbTypeTemplateMapper;
import com.xianggole.pojo.TbSpecificationOption;
import com.xianggole.pojo.TbSpecificationOptionExample;
import com.xianggole.pojo.TbTypeTemplate;
import com.xianggole.pojo.TbTypeTemplateExample;
import com.xianggole.pojo.TbTypeTemplateExample.Criteria;
import com.xianggole.sellergoods.service.TypeTemplateService;

import entity.PageResult;

/**
 * 服务实现层
 * @company 恩施迅博科技
 * @author Administrator
 *
 */
@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbTypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbTypeTemplate> page=   (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.insert(typeTemplate);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbTypeTemplate typeTemplate){
		typeTemplateMapper.updateByPrimaryKey(typeTemplate);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbTypeTemplate findOne(Long id){
		return typeTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			typeTemplateMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbTypeTemplateExample example=new TbTypeTemplateExample();
		Criteria criteria = example.createCriteria();
		
		if(typeTemplate!=null){			
						if(typeTemplate.getName()!=null && typeTemplate.getName().length()>0){
				criteria.andNameLike("%"+typeTemplate.getName()+"%");
			}
			if(typeTemplate.getSpecIds()!=null && typeTemplate.getSpecIds().length()>0){
				criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
			}
			if(typeTemplate.getBrandIds()!=null && typeTemplate.getBrandIds().length()>0){
				criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
			}
			if(typeTemplate.getCustomAttributeItems()!=null && typeTemplate.getCustomAttributeItems().length()>0){
				criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
			}
	
		}
		
		Page<TbTypeTemplate> page= (Page<TbTypeTemplate>)typeTemplateMapper.selectByExample(example);		
		//缓存处理
		saveToRedis();
		
		return new PageResult(page.getTotal(), page.getResult());
	}
		@Autowired
		private RedisTemplate redisTemplate;
		/**
		 * 将品牌列表和规格列表放入缓存
		 */
		private void saveToRedis() {
			List<TbTypeTemplate> templateList = findAll();
			for(TbTypeTemplate template : templateList) {
				
				List<Map> brandList = JSON.parseArray(template.getBrandIds(),Map.class);
				redisTemplate.boundHashOps("BrandList").put(template.getId(),brandList );
				
				//得到规格列表
				List<Map> specList = findSpecList(template.getId());
				redisTemplate.boundHashOps("specList").put(template.getId(),specList );
			}
			System.out.println("缓存品牌列表");
		}

		@Override
		public List<Map> findType() {
			
			return typeTemplateMapper.selectAllType();
		}
		@Autowired
		private TbSpecificationOptionMapper specificationOptionMapper;
		@Override
		public List<Map> findSpecList(Long id) {
			TbTypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
			List<Map> list = JSON.parseArray(typeTemplate.getSpecIds(),Map.class);
			for(Map map : list) {
				TbSpecificationOptionExample example = new TbSpecificationOptionExample();
				com.xianggole.pojo.TbSpecificationOptionExample.Criteria createCriteria = example.createCriteria();
				createCriteria.andSpecIdEqualTo(new Long((Integer)map.get("id")));
				List<TbSpecificationOption> options = specificationOptionMapper.selectByExample(example);
				map.put("options", options);
			}
			
			return list;
		}
	
}
