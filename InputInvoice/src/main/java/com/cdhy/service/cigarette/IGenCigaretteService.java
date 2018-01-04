package com.cdhy.service.cigarette;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;
import com.cdhy.entity.Result;

/**
 * 普票
 * @author Administrator
 *
 */
public interface IGenCigaretteService {
    
    /**
     * 查询列表
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public Page list(Map<String, Object> parm);
    /**
     * 获取单条
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param departid
     * @return
     */
    public List<Map<String, Object>> getById(Map<String, Object> parm);
    /**
     * 删除
     * @param parm
     * @return
     */
    public int delete(Map<String, Object> parm);
    /**
     * 获取待开具的发票信息
     * @param parm
     * @return
     */
    public List<Map<String, Object>> getInvoice(Map<String, Object> parm);
    /**
     * 开票
     * @param parm
     * @return
     */
    void doInvoice(Map<String, Object> parm);
    /**
     * 只针对单张开票
     * @param parm
     * @return
     */
    public Map<String, Object> doInvoiceByOne(Map<String, Object> parm);
    
    
    
    Result saveBillsToDB(String filePath);
    
	/**
	 * 重新开票
	 * @param param
	 * @return
	 */
    public Map<String, Object> reDoInvoice(Map<String, Object> param);
    
    public List<Map<String,Object>> getInvoiceList(Map<String,Object> param);
    
    Map<String, Object> saveInvoice(Map<String, Object> parm) throws Exception;
}
