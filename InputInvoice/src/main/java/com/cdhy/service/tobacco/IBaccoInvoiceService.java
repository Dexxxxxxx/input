package com.cdhy.service.tobacco;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;

public interface IBaccoInvoiceService {
    /**
     * 查询列表
     * @param parm
     * @return
     */
    public Page list(Map<String, Object> param);
    /**
     * 获取单条记录的详情
     * @param parm
     * @return
     */
    List<Map<String, Object>>  listDetail(Map<String, Object> param);
    /**
     * 查看执行开票后的发票的详情
     * @param pram
     * @return
     */
    List<Map<String, Object>>  listInvoice(Map<String, Object> pram);
    
}
