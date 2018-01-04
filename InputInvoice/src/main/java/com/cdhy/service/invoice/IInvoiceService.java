package com.cdhy.service.invoice;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;

public interface IInvoiceService {
    /**
     * 获取烟草发票开具信息列表
     * @param param
     * @return
     */
    Page listTobaccoInvoice(Map<String,Object> param);
    /**
     * 获取某条烟草发票开具信息
     * @param param
     * @return
     */
    List<Map<String, Object>> getTobaccoInvoiceDetail(Map<String,Object> param);
    
    
    
    
    /**
     * 获取卷烟发票开具信息列表
     * @param param
     * @return
     */
    Page listCigaretteInvoice(Map<String,Object> param);
    /**
     * 获取某条卷烟发票开具信息
     * @param param
     * @return
     */
    List<Map<String, Object>> getCigaretteInvoiceDetail(Map<String,Object> param);
}
