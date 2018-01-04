package com.cdhy.dao.invoice;

import java.util.List;
import java.util.Map;

public interface IInvoiceMapper {
//烟草
    	/**
    	 * 获取烟草发票开具信息
    	 * @param param
    	 * @return
    	 */
    	List<Map<String,Object>> listTobaccoInvoice(Map<String,Object> param);
    	/**
    	 * 烟草发票开具信息总条数
    	 * @param param
    	 * @return
    	 */
    	int tobaccoInvoiceCount(Map<String, Object> param);
    	/**
    	 * 获取一张发票的公共信息
    	 * @param param
    	 * @return
    	 */
    	Map<String, Object> getTobaccoInvoice(Map<String, Object> param);
    	/**
    	 * 获取某张烟草发票信息
    	 * @param param
    	 * @return
    	 */
    	List<Map<String, Object>> getTobaccoInvoiceDetail(Map<String, Object> param);
    	
    	void updateStatus(Map<String, Object> param);
    	
//卷烟	
    	/**
    	 * 获取卷烟发票开具信息
    	 * @param param
    	 * @return
    	 */
    	List<Map<String,Object>> listCigaretteInvoice(Map<String, Object> param);
    	/**
    	 * 卷烟发票开具信息总条数
    	 * @param param
    	 * @return
    	 */
    	int cigaretteInvoiceCount(Map<String, Object> param);
    	/**
    	 * 获取某张卷烟发票信息
    	 * @param param
    	 * @return
    	 */
    	List<Map<String, Object>> getCigaretteInvoiceDetail(Map<String, Object> param);
}
