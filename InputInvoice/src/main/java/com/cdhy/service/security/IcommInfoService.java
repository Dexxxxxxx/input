package com.cdhy.service.security;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;

public interface IcommInfoService {
    Page list(Map<String, Object> parm);
    void add(Map<String, Object> parm);
    int delete(Map<String, Object> parm);
    void update(Map<String, Object> parm);
    void reset(Map<String, Object> parm);
    Map<String, Object> getById(Map<String, Object> parm);
    List<Map<String, Object>> getTree(Map<String, Object> parm);
}
