package com.cdhy.dao.invoiceStatistics;

import java.util.List;
import java.util.Map;

public interface IInvoiceInfoMapper {
	  /**
     * 分页查询
     * 
    
     */
    public List<Map<String, Object>> list(Map<String, Object> map);
    public List<Map<String, Object>> list2(Map<String, Object> map);
    
    public List<Map<String, Object>> listAll(Map<String, Object> map);
    
    public List<Map<String, Object>> listCollect(Map<String, Object> map);
    
    public Map<String, Object> listAllCollect(Map<String, Object> map);
    
    public List<Map<String, Object>> listDep(Map<String, Object> map);
    
    public List<Map<String, Object>> listSupplier(Map<String, Object> map);
    
    /**
     * 分页查询 总条数
     * 
     */
    public int listCount(Map<String, Object> map);
    public int listCount2(Map<String, Object> map);
    
    public int listAllCount(Map<String, Object> map);
    
    public int listDepCount(Map<String, Object> map);
    
    public int listSupplierCount(Map<String, Object> map);
    
    /**
     * 分页查询
     * 
    
     */
    public List<Map<String, Object>> detailList(Map<String, Object> map);
    
    /**
     * 分页查询 总条数
     * 
     */
    public int detailListCount(Map<String, Object> map);
    
    /**
     * 导出
     * 
    
     */
    public List<Map<String, Object>> exportList(Map<String, Object> map);
    
    public List<Map<String, Object>> exportDetailList(Map<String, Object> map);
    
    public List<Map<String, Object>> exportList2(Map<String, Object> map);

}
