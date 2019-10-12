package com.xianggole.manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xianggole.pojo.TbBrand;
import com.xianggole.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;

@RestController
@RequestMapping("/brand")
public class BrandController {
	
	@Reference
	private BrandService brandService;
	
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){
		return brandService.findAll();
	} 
	@RequestMapping("/findPage")
	public PageResult findPage(int pageNum,int pageSize){
		return brandService.findPage(pageNum, pageSize);
	} 
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand brand){
		try {
			 brandService.add(brand);
			 return new Result("添加成功",true);
		}catch (Exception e){
			e.printStackTrace();
			return new Result("添加失败",false);
		}
		
	} 

}
