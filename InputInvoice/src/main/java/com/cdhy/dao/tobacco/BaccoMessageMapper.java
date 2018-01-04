package com.cdhy.dao.tobacco;

import java.util.List;
import java.util.Map;

public interface BaccoMessageMapper {
    /**
     * 增加
     * @param param
     */
    void add(Map<String, Object> param);
    /**
     * 删除
     * @param param
     */
    int delete(Map<String, Object> param);
    /**
     * 修改
     * @param param
     */
    void update(Map<String, Object> param);
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
     * 查询某条信息
     * @param param
     * @return
     */
    Map<String, Object> getById(Map<String, Object> param);
    
}
