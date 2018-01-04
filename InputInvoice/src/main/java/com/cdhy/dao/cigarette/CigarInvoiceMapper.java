package com.cdhy.dao.cigarette;

import java.util.List;
import java.util.Map;

public interface CigarInvoiceMapper {
	/**
	 * 获取卷烟发票开具信息
	 * 
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> list(Map<String, Object> param);
	/**
	 * 卷烟发票开具信息总条数
	 * 
	 * @param param
	 * @return
	 */
	int listCount(Map<String, Object> param);
	/**
	 * 获取某张发票的公共信息
	 * @param param
	 * @return
	 */
	Map<String, Object> getInvoice(Map<String, Object> param);

	/**
	 * 删除一条订单号的所有发票
	 * 
	 * @param map
	 */
	void delete(Map<String, Object> map);
	/**
	 * 删除某张发票
	 * @param map
	 */
	void deleteById(Map<String, Object> map);
	/**
	 * 保存发票
	 * 
	 * @param map
	 * @return
	 */
	public int save(Map<String, Object> map);
	/**
	 * 保存红票
	 * @param map
	 */
	void saveRed(Map<String, Object> map);

	/**
	 * 修改发票信息
	 * 
	 * @param map
	 */
	void update(Map<String, Object> map);
	/**
	 * 修改作废状态
	 * @param map
	 */
	void updateCancel(Map<String, Object> map);

	/**
	 * 修改发票源数据状态 (已开)
	 * @param parm
	 */
	void updateDyStatus(Map<String, Object> parm);
	/**
	 * 根据invoice_no 查询发票
	 * @param parm
	 * @return
	 */
	List<Map<String, Object>> getByInvoice_no(Map<String, Object> parm);
	/**
	 * 根据id获取发票信息
	 * @param parm
	 * @return
	 */
	Map<String, Object> getById(Map<String, Object> parm);
	/**
	 * 根据发票代码号码获取该发票信息
	 * @param parm
	 * @return
	 */
	Map<String, Object> getByDmHm(Map<String, Object> parm);
 }
