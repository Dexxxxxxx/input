package com.cdhy.dao.tobacco;

import java.util.List;
import java.util.Map;

public interface BaccoPurchaseMapper {
    /**
     * 分页查询
     * @param map
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> map);
    /**
     * 分页查询总条数
     * @param map
     * @return
     */
    public int listCount(Map<String, Object> map);
    /**
     * 刷新数据
     * @param map
     */
    void refresh(Map<String, Object> map);
    /**
     * 分页查询单条记录的详情
     * @param map
     * @return
     */
    public List<Map<String, Object>> getById(Map<String, Object> map);
    /**
     * 获取本次收购的详情
     * @param map
     * @return
     */
    Map<String, Object> getCommonInfo(Map<String, Object> map);
    /**
     * 分页查询单条记录的总条数
     * @param map
     * @return
     */
    public int listDetailCount(Map<String, Object> map);
    /**
     * 获取需要开票的所有信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getAllInfo(Map<String, Object> map);
    
//发票操作
    /**
     * 删除原有发票不成功的数据
     * @param map
     */
    void delete(Map<String, Object> map);
    /**
     * 删除原有发票不成功的详情数据
     * @param map
     */
    int delInvoiceDetail(Map<String, Object> map);
    /**
     * 保存发票
     * @param map
     * @return
     */
    public int save(Map<String, Object> map);
    /**
     * 保存发票详情
     * @param map
     * @return
     */
    public int saveDetail(Map<String, Object> map);
    
    void update(Map<String, Object> map);
    
    void updateStatus(Map<String, Object> map);
    /**
     * 获取补贴信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getSubInfo(Map<String, Object> map);
    
//    public List<Map<String, Object>> getSubInfo2(Map<String, Object> map);
    
    public List<Map<String, Object>> getFpInfo(Map<String, Object> map);
    
    //获取农户信息
    Map<String, Object> getFarmerInfo(Map<String, Object> param);
    
    
    /**
     * 获取所属纳税人识别号tree
     * @param parm
     * @return
     */
    public List<Map<String, Object>> getTree1(Map<String, Object> parm);
    
    
    
    
}
