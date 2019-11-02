package com.xianggole.sellergoods.service;
import java.util.List;
import com.xianggole.pojo.TbGoods;
import com.xianggole.pojo.TbItem;
import com.xianggole.pojogroup.Goods;

import entity.PageResult;
/**
 * 服务层接口
 * @company 恩施迅博科技
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Goods goods);
	
	
	/**
	 * 修改
	 */
	public void update(Goods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum,int pageSize);
	
	/**
	 * 更改审核状态
	 */
	public void updateAuditStatus(Long[] ids,String status);
	
	public List<TbItem> searchItemListByGoodsListAndStatus(Long []goodsIds,String status);
}
