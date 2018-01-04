package com.cdhy.dao.security;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /**
     * 分页查询
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param end
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 分页查询 总条数
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param map
     * @return
     */
    public int listCount(Map<String, Object> map);

    /**
     * 新增
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public int add(Map<String, Object> map);

    /**
     * 禁用
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param ids
     * @return
     */
    public int deleteByIds(Map<String, Object> parm);
    
    /**
     * 删除用户
     * @param parm
     * @return
     */
    public int deleteUsers(Map<String, Object> parm); 

    /**
     * 修改
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public int update(Map<String, Object> map);

    /**
     * 返回单个对象
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param departid
     * @return
     */
    public Map<String, Object> getById(Map<String, Object> map);

    /** 删除用户角色
     * @author pjf
     * @Date 2015年10月29日
     * @param parm
     */
    public void deleteUserRole(Map<String, Object> parm);
    
    /** 删除用户角色-批量
     * @author pjf
     * @Date 2015年10月29日
     * @param parm
     */
    public void deleteUsersRole(Map<String, Object> parm);

    /** 设置角色
     * @author pjf
     * @Date 2015年10月29日
     * @param parm
     */
    public void setRole(Map<String, Object> parm);

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
    public int startByIds(Map<String, Object> parm);
    
    /**
     * 设置用户所属机构数状图
     * @author java
     * @Date 2016年03月03日
     * @param ids
     * @return
     */
    public List<Map<String, Object>> setUserDepartTree(Map<String, Object> parm);
    
     /**
      * 删除用户管理部门
      * @author java
      * @Date 2016年03月01日
      * @param parm
      * @return
      */
    public void deleteUserDepart(Map<String, Object> parm);
    
    /**
     * 设置用户管理部门
     * @author java
     * @Date 2016年03月01日
     * @param parm
     * @return
     */
    public void setDepart(Map<String, Object> parm);
    /**
     * 根据手机号查询用户
     * @param parm
     * @return
     */
    public Map<String, Object>  getUserByUsername(Map<String, Object> parm);
    /**
     * 根据部门查询公司
     * @param parm
     * @return
     */
    public Map<String, Object>  getOrgByDep(Map<String, Object> parm);

}