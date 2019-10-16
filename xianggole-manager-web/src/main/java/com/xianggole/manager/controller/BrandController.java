package com.xianggole.manager.controller;

import java.util.List;
import java.util.Map;

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
	@RequestMapping("/findOne")
	public TbBrand findOne(Long id) {
		TbBrand brand = brandService.findOne(id);
		return brand;
	}
	@RequestMapping("/update")
	public Result update (@RequestBody TbBrand brand) {
		try {
			brandService.update(brand);
			return new Result("更改成功",true);
		}catch (Exception e) {
			return new Result("更改失败",false);
		}
		
	}
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			brandService.delete(ids);
			return new Result("删除成功",true);
		}catch (Exception e) {
			return new Result("删除失败",false);
		}
	}
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbBrand brand,int page,int size) {
		return brandService.findPage(brand, page,size);
	}
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return brandService.selectOptionList();
	}

}
