package com.cdhy.dao.security;

import java.util.List;
import java.util.Map;

public interface NsrInfoMapper {
    List<Map<String, Object>> list(Map<String, Object> parm);
    int listCount(Map<String, Object> parm);
    Map<String, Object> getById(Map<String, Object> param);
    void update(Map<String, Object> param);
    void add(Map<String, Object> parm);
    int deleteByIds(Map<String, Object> parm);
    int hasDept(Map<String, Object> parm);
}
