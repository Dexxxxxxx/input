package com.cdhy.service.tobacco;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;

public interface IBaccoBuyerService {
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
    public void addBuyer(Map<String, Object> param);
    /**
     * 删除
     * @param param
     */
    int delBuyer(Map<String, Object> param);
    /**
     * 修改
     * @param param
     */
    public void updateBuyer(Map<String, Object> param);
    /**
     * 检查并保存
     * @param param
     * @return
     */
    public Map<String, Object> checkAndSave(Map<String, Object> param);
    
}
