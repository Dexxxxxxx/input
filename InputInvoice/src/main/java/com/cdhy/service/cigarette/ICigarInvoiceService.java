package com.cdhy.service.cigarette;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;

public interface ICigarInvoiceService {
    
	/**
	 * 分页查询发票
	 * @param parm
	 * @return
	 */
	public Page list(Map<String, Object> parm);
	
	/**
	 * 根据不同条件获取发票list
	 * @param parm
	 * @return
	 */
	List<Map<String, Object>> listInvoice(Map<String, Object> parm);

	/**
	 * 获取发票详情
	 * @param parm
	 * @return
	 */
	public List<Map<String, Object>> listInvoiceDetail(Map<String, Object> parm);
	/**
	 * 发票作废
	 * @param parm
	 */
	void cancel(Map<String, Object> parm);
	/**
	 * 专普票冲红
	 * @param parm
	 */
	void redCancel(Map<String, Object> parm);
	
	/**
	 * 电票冲红
	 * @param parm
	 */
	void e_redCancel(Map<String, Object> parm);
	/**
	 * 冲红发票回滚
	 * @param parm
	 */
	void rollback(Map<String, Object> parm);
}
