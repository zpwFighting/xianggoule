package com.xianggole.sellergoods.service;

import java.util.List;

import com.xianggole.pojo.TbBrand;

import entity.PageResult;

public interface BrandService {
	
	public List<TbBrand> findAll();
	
	public PageResult findPage(int pageNum,int pageSize);
	/**
	 * 分页查询
	 * @param brand
	 */
	public PageResult findPage(TbBrand brand ,int pageNum,int pageSize);
	
	
	public void add(TbBrand brand);
	
	/**
	 * 查找一个brand
	 */
	
	public TbBrand findOne(Long id);
	
	/**
	 * 修改brand
	 */
	
	public void update(TbBrand brand);
	
	/**
	 * 删除
	 */
	public void delete(Long[] ids);

}
