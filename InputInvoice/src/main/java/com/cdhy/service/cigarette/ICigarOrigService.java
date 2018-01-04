package com.cdhy.service.cigarette;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;
import com.cdhy.entity.Result;

public interface ICigarOrigService {
    /**
     * 查询列表
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public Page list(Map<String, Object> parm);
    /**
     * 获取明细  返回值为list
     * @param parm
     * @return
     */
    public List<Map<String, Object>> getById(Map<String, Object> parm);
    /**
     * 导入源数据
     * @param filePath
     * @return
     */
    Result saveBillsToDB(String filePath);
    /**
     * 修改数据状态（是否开票）
     * @param parm
     */
    void update(Map<String, Object> parm);
    

}
