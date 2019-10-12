package com.xianggole.sellergoods.service;

import java.util.List;

import com.xianggole.pojo.TbBrand;

import entity.PageResult;

public interface BrandService {
	
	public List<TbBrand> findAll();
	
	public PageResult findPage(int pageNum,int pageSize);
	
	public void add(TbBrand brand);

}
