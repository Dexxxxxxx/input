package com.cdhy.service.security;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;


public interface IDepartService {


    /**
     * 查询列表
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public Page list(Map<String, Object> parm);

    /**
     * 获取单条
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param departid
     * @return
     */
    public Map<String, Object> getById(Map<String, Object> parm);

    /**
     * 增加
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     * @throws BizException
     */
    public void add(Map<String, Object> parm);

    /**
     * 删除
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param ids
     * @return
     */
    public int delete(Map<String, Object> parm);

    /**
     * 修改
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     * @throws BizException
     */
    public void update(Map<String, Object> parm);

    /**
     * 获取tree
     * 
     * @author pjf
     * @Date 2015年10月29日
     * @param parm
     * @return
     */
    public List<Map<String, Object>> getTree(Map<String, Object> parm);


}
