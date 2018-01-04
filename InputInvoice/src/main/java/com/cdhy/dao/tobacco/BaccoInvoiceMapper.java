package com.cdhy.dao.tobacco;

import java.util.List;
import java.util.Map;

public interface BaccoInvoiceMapper {
    /**
     * 查询列表
     * @param param
     * @return
     */
    List<Map<String, Object>> list(Map<String, Object> param);
    /**
     * 分页总数
     * @param param
     * @return
     */
    int listCount(Map<String, Object> param);
    /**
     * 查询专票列表
     * @param param
     * @return
     */
    List<Map<String, Object>> spe_list(Map<String, Object> param);
    /**
     * 专票列表分页总数
     * @param param
     * @return
     */
    int spe_listCount(Map<String, Object> param);
    
    /**
     * 获取发票的公共信息
     * @param param
     * @return
     */
    Map<String, Object> getById(Map<String, Object> param);
    /**
     * 查询某条信息
     * @param param
     * @return
     */
    List<Map<String, Object>> listDetail(Map<String, Object> param);
    /**
     * 查看执行开票后的发票的详情
     * @param param
     * @return
     */
    List<Map<String, Object>> listInvoice(Map<String, Object> param);
    
}
