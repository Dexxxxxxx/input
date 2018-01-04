package com.cdhy.service.security;

import java.util.List;
import java.util.Map;

import com.cdhy.entity.Page;


public interface IUserService {
    /**
     * 查询列表
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public Page list(Map<String, Object> parm);

    /**
     * 获取
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
     * 禁用
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param ids
     * @return
     */
    public int delete(Map<String, Object> map);
    /**
     * 删除用户
     * @param map
     * @return
     */
    public int deleteUsers(Map<String, Object> map);

    /**
     * 修改
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     * @throws BizException
     */
    public void update(Map<String, Object> parm);

    /** 设置用户角色
     * @author pjf
     * @Date 2015年10月29日
     * @param parm
     */
    public void saveRole(Map<String, Object> parm);

    /** 获取所有角色ID
     * @author pjf
     * @Date 2015年10月30日
     * @param parm
     * @return
     */
    public List<Map<String, Object>> getAllRoleIdById(Map<String, Object> parm);
    
    /**
     * 启用
     * @author java
     * @Date 2016年03月03日
     * @param ids
     * @return
     */
    public int invokeByStart(Map<String, Object> map);
    
    /**
     * 设置用户所属机构数状图
     * @author java
     * @Date 2016年03月03日
     * @return
     */
    public List<Map<String, Object>> saveUserDepartTree(Map<String, Object> parm);
    
    /**
     * 设置用户管理部门
     * @param parm
     */
    public void saveDepart(Map<String, Object> parm);
    /**
     * 根据手机号查询公司部门
     */
    public Map<String,Object> wxQueryDep(Map<String, Object> parm);
    

}
