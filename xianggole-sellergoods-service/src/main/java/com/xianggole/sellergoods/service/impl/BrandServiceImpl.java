package com.xianggole.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xianggole.mapper.TbBrandMapper;
import com.xianggole.pojo.TbBrand;
import com.xianggole.pojo.TbBrandExample;
import com.xianggole.pojo.TbBrandExample.Criteria;
import com.xianggole.sellergoods.service.BrandService;

import entity.PageResult;
@Service
@Transactional
public class BrandServiceImpl implements BrandService {
	@Autowired
	 private TbBrandMapper brandMapper;
	 
	@Override
	public List<TbBrand> findAll() {
		return brandMapper.selectByExample(null);
	}

	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbBrand> page = (Page<TbBrand>)brandMapper.selectByExample(null);
		return new PageResult(page.getTotal(),page.getResult());
	}

	@Override
	public void add(TbBrand brand) {
		brandMapper.insertSelective(brand);
	}

	@Override
	public TbBrand findOne(Long id) {
		return brandMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(TbBrand brand) {
		brandMapper.updateByPrimaryKeySelective(brand);
	}

	@Override
	public void delete(Long[] ids) {
		for(Long id : ids) {
			brandMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		TbBrandExample example = new TbBrandExample();
		Criteria criteria = example.createCriteria();
		if(brand!=null) {
			if(brand.getName()!=null && brand.getName().length()>0) {
				criteria.andNameLike("%"+brand.getName()+"%");
			}
			if(brand.getFirstChar()!=null && brand.getFirstChar().length()>0) {
				criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
			}
		}
		Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> selectOptionList() {
		
		return brandMapper.selectOptionList();
	}

}
