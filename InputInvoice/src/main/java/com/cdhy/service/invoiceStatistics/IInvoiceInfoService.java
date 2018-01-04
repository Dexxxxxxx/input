package com.cdhy.service.invoiceStatistics;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cdhy.entity.Page;
import com.cdhy.entity.Result;

public interface IInvoiceInfoService {
	

    /**
     * 查询列表
     * 
     */
    public  Map<String,Object>  list(Map<String, Object> parm);
    public  Map<String,Object>  list2(Map<String, Object> parm);
    
    public Map<String,Object> listAll(Map<String, Object> parm);
    
    public List<Map<String, Object>> listCollect(Map<String, Object> parm);
    
    public Page detailList(Map<String, Object> parm);
    
    public Page listDep(Map<String, Object> parm);
    
    public Page listSupplier(Map<String, Object> parm);
    
  //导出发票查验数据
  	Result exportList(Map<String, Object> param, HttpServletRequest request, HttpServletResponse response);
  	
    //导出发票明细数据
  	Result exportDetailList(Map<String, Object> param, HttpServletRequest request, HttpServletResponse response);
	Result exportList2(Map<String, Object> parm, HttpServletRequest request, HttpServletResponse response);
  	

}
