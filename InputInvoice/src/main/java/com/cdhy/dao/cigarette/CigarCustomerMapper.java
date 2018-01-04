package com.cdhy.dao.cigarette;

import java.util.List;
import java.util.Map;

public interface CigarCustomerMapper {
    List<Map<String, Object>> list(Map<String, Object> parm);
    
    int listCount(Map<String, Object> parm);
    
    void update(Map<String, Object> parm);
    /**
     * 根据id,单位代码,单位名称 查询客户信息
     * @param parm
     * @return
     */
    Map<String, Object> getById(Map<String, Object> parm);
    
    void add(Map<String, Object> parm);
    
    int deleteByIds(Map<String, Object> parm);
    
}
