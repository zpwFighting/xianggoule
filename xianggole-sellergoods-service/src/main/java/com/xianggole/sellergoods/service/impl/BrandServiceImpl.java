package com.xianggole.sellergoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.xianggole.mapper.TbBrandMapper;
import com.xianggole.pojo.TbBrand;
import com.xianggole.sellergoods.service.BrandService;
@Service
public class BrandServiceImpl implements BrandService {
	@Autowired
	 private TbBrandMapper brandMapper;
	 
	@Override
	public List<TbBrand> findAll() {
		return brandMapper.selectByExample(null);
	}

}
