package com.cdhy.dao.tobacco;

import java.util.List;
import java.util.Map;

public interface BaccoOrigMapper {
    /**
     * 获取list
     * @param parm
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> parm);
    /**
     * 获取list总数
     * @param parm
     * @return
     */
    public int listCount(Map<String, Object> parm);
    
    /**
     * 查看收购详情
     * @param parm
     * @return
     */
    public List<Map<String, Object>> listDetail(Map<String, Object> parm);
    /**
     * 获取公共信息（农户的基本信息）
     * @param parm
     * @return
     */
    Map<String, Object> getCommonInfo(Map<String, Object> parm);
    /**
     * 获取待开票信息
     * @param parm
     * @return
     */
    List<Map<String, Object>> getItems(Map<String, Object> parm);    
    /**
     * 插入批次中间表
     * @param parm
     */
    void insertOrigToInvoice(Map<String, Object> parm);
    /**
     * 删除批次中间表
     * @param parm
     */
    void deleteOrigToInvoice(Map<String, Object> parm);
    void deleteOrigToInvoice1(Map<String, Object> parm);
    /**查询批次中间表
     * 
     * @param parm
     * @return
     */
    List<Map<String, Object>> getOrigToInvoice(Map<String, Object> parm);
    /**
     * 修改源数据是否开票状态
     * @param parm
     */
    void update(Map<String, Object> parm); 
}
