package com.cdhy.service.cigarette;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;

public interface IECigaretteService {
    /**
     * 提供页面查询电子发票数据
     * @param parm
     * @return
     */
    Page elist (Map<String, Object> parm);
    
    Page list(Map<String, Object> parm);
    
    List<Map<String, Object>> getById(Map<String, Object> parm);
    
    void doInvoice(Map<String, Object> parm);
    
    Map<String, Object> doInvoiceByOne(Map<String, Object> parm);
    
    List<Map<String, Object>> getInvoiceList(Map<String, Object> parm);
    /**
     * 开具红票
     * @param parm
     */
    void doRedInvoice(Map<String, Object> parm);
}
