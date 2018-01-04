package com.cdhy.dao.cigarette;

import java.util.List;
import java.util.Map;

/**
 * 卷烟的开票信息
 * @author Administrator
 *
 */
public interface CigarOrigMapper {
    /**
     * 分页查询
     * 
     * @author dxg
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 分页查询 总条数
     * 
     * @author dxg
     * @param map
     * @return
     */
    public int listCount(Map<String, Object> map);
    /**
     * 获取订单的公共信息
     * @param parm
     * @return
     */
    Map<String, Object> getInvoiceCommonInfo(Map<String, Object> parm);
    /**
     * 根据单号获取出售明细
     * @param parm
     * @return
     */
    List<Map<String, Object>> getById(Map<String, Object> parm);
    /**
     * 查询导入的发票单据在数据库是否已存在
     * 
     * @param orig_id
     * @return
     */
    int findBillsCountByOrig(String orig_id);
    /**
     * 将导入数据插入数据库
     * 
     * @param param
     * @return
     */
    int saveBills(Map<String, Object> param);
    
    /**
     * 修改源数据状态（是否已开票）
     * @param param
     */
    void update(Map<String, Object> param);
    

}
