package com.xianggole.sellergoods.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xianggole.mapper.TbSpecificationMapper;
import com.xianggole.mapper.TbSpecificationOptionMapper;
import com.xianggole.pojo.TbSpecification;
import com.xianggole.pojo.TbSpecificationExample;
import com.xianggole.pojo.TbSpecificationOption;
import com.xianggole.pojo.TbSpecificationOptionExample;
import com.xianggole.pojo.TbSpecificationExample.Criteria;

import com.xianggole.pojogroup.Specification;
import com.xianggole.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 * @company 恩施迅博科技
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	@Autowired
	private TbSpecificationOptionMapper tbSpecificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		TbSpecification tbSpecification = specification.getSpecification();
		specificationMapper.insert(tbSpecification);
		List<TbSpecificationOption> tbSpecificationOption = specification.getSpecificationOptionList();
		
		for(TbSpecificationOption option : tbSpecificationOption ) {
			option.setSpecId(tbSpecification.getId());
			tbSpecificationOptionMapper.insert(option);
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
		TbSpecification tbSpecification = specification.getSpecification();
		for(TbSpecificationOption option :specificationOptionList) {
			tbSpecificationOptionMapper.deleteByPrimaryKey(option.getId());
		}
		specificationMapper.deleteByPrimaryKey(tbSpecification.getId());
		add(specification);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		Specification specification = new Specification();
		specification.setSpecification(tbSpecification);
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.xianggole.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		List<TbSpecificationOption> tbSpecificationOption = tbSpecificationOptionMapper.selectByExample(example);
		specification.setSpecificationOptionList(tbSpecificationOption);
		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.xianggole.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		for(Long id:ids){
			criteria.andSpecIdEqualTo(id);
			tbSpecificationOptionMapper.deleteByExample(example);
			specificationMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
