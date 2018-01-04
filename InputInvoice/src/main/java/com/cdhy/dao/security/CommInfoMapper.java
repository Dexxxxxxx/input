package com.cdhy.dao.security;

import java.util.List;
import java.util.Map;

public interface CommInfoMapper {
    /**
     * 获取所有接口信息 包括分页
     * @return
     */
    List<Map<String, Object>> list(Map<String, Object> parm);
    
    int listCount(Map<String, Object> parm);
    
    Map<String, Object> getById(Map<String, Object> param);
    
    void add(Map<String, Object> parm);
    
    int deleteByIds(Map<String, Object> parm);
    
    void update(Map<String, Object> parm);
    /**
     * 
     * @param parm
     * @return
     */
    int hasChild(Map<String, Object> parm);
    
    int hasDept(Map<String, Object> parm);
    
    List<Map<String, Object>> getTree(Map<String, Object> parm);
    
    
    
    Map<String, Object> getInfo(Map<String, Object> parm);
    
    void updateInfo(Map<String, Object> parm); 
    
}
