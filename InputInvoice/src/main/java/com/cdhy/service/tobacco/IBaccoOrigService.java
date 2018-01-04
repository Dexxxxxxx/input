package com.cdhy.service.tobacco;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;

public interface IBaccoOrigService {
    /**
     * 查询列表
     * @param parm
     * @return
     */
    public Page list(Map<String, Object> parm);
    /**
     * 获取单条记录的详情
     * @param parm
     * @return
     */
    public List<Map<String, Object>> listDetail(Map<String, Object> parm);
}
