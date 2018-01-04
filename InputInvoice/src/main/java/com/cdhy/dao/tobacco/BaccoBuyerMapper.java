package com.cdhy.dao.tobacco;

import java.util.List;
import java.util.Map;

public interface BaccoBuyerMapper {
    /**
     * 增加
     * @param param
     */
    void addBuyer(Map<String, Object> param);
    /**
     * 删除
     * @param param
     */
    int delBuyer(Map<String, Object> param);
    /**
     * 修改
     * @param param
     */
    void updateBuyer(Map<String, Object> param);
    /**
     * 查询列表
     * @param param
     * @return
     */
    List<Map<String, Object>> listBuyer(Map<String, Object> param);
    /**
     * 分页总数
     * @param param
     * @return
     */
    int listCount(Map<String, Object> param);
    /**
     * 查询某条信息
     * @param param
     * @return
     */
    Map<String, Object> getBuyer(Map<String, Object> param);
    
}
