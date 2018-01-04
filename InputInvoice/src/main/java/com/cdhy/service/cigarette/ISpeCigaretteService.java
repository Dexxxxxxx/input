package com.cdhy.service.cigarette;

import java.util.List;
import java.util.Map;


import com.cdhy.entity.Page;
import com.cdhy.entity.Result;

/**
 * 专票
 * @author Administrator
 *
 */
public interface ISpeCigaretteService {
    
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
    public void doInvoice(Map<String, Object> parm);
    /**
     * 单张开票
     * @param parm
     * @return
     */
    Map<String, Object> doInvoiceByOne(Map<String, Object>parm);
    /**
     * 重新开票
     * @param parm
     * @return
     */
    Map<String, Object> redoInvoice(Map<String, Object> parm);
    
    /**
     * 保存发票信息
     * @param param
     * @throws Exception 
     */
    Map<String, Object> saveInvoice(Map<String, Object> param) throws Exception;
    
    Result saveBillsToDB(String filePath);
    
    List<Map<String, Object>> getInvoiceList(Map<String, Object> parm);

}
