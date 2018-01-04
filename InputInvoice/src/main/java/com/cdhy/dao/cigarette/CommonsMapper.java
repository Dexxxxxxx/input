package com.cdhy.dao.cigarette;

import java.util.List;
import java.util.Map;

public interface CommonsMapper {
	/**
	 * 查询客户信息
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryCustomerInfo(Map<String, Object> param);

	/**
	 * 修改客户信息
	 * 
	 * @param param
	 * @return
	 */
	public int modifyCustomerInfo(Map<String, Object> param);
}
