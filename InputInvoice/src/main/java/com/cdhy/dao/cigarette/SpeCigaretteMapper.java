package com.cdhy.dao.cigarette;

import java.util.List;
import java.util.Map;

public interface SpeCigaretteMapper {
    /**
     * 分页查询
     * 
     * @author dxg
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 分页查询 总条数
     * 
     * @author dxg
     * @param map
     * @return
     */
    public int listCount(Map<String, Object> map);

    
	/**
	 * 删除发票（通过ID删除）
	 * @param parm
	 * @return
	 */
	public int deleteByIds(Map<String, Object> parm);

	/**
	 * 获取商品信息
	 * @param parm
	 * @return
	 */
	public List<Map<String, Object>>  getSpInfo(Map<String, Object> parm);

	/**
	 * 获取公共信息（抬头信息）
	 * @param parm
	 * @return
	 */
	public Map<String, Object> getComInfo(Map<String, Object> parm);
	
	/**
	 * 获取单张单据的商品信息
	 * @param parm
	 * @return
	 */
	public List<Map<String, Object>> getSpInfoByOne(Map<String, Object> parm);
    
}
