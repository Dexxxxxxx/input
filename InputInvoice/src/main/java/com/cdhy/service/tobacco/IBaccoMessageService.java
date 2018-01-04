package com.cdhy.service.tobacco;

import java.util.Map;

import com.cdhy.entity.Page;

public interface IBaccoMessageService {
    /**
     * 查询列表
     * @param parm
     * @return
     */
    public Page list(Map<String, Object> param);
    /**
     * 获取单条记录的详情
     * @param parm
     * @return
     */
    public Map<String, Object> getById(Map<String, Object> param);
    /**
     * 增加
     * @param param
     */
    public void add(Map<String, Object> param);
    /**
     * 删除
     * @param param
     */
    int delete(Map<String, Object> param);
    /**
     * 修改
     * @param param
     */
    public void update(Map<String, Object> param);
}
