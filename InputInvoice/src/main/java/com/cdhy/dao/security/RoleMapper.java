package com.cdhy.dao.security;

import java.util.List;
import java.util.Map;

public interface RoleMapper {
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
     * 新增
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @return
     */
    public int addRoleUser(Map<String, Object> map);

    /**
     * 删除
     * 
     * @author pjf
     * @Date 2015年10月26日
     * @param ids
     * @return
     */
    public int deleteByIds(Map<String, Object> parm);

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

    /** 设置角色权限
     * @author pjf
     * @Date 2015年10月29日
     * @param parm
     */
    public void setPrivilege(Map<String, Object> parm);
    
    /** 删除角色权限
     * @author pjf
     * @Date 2015年10月29日
     * @param parm
     */
    public void deleteRolePrivilege(Map<String, Object> parm);
    
    /** 删除角色权限 -批量
     * @author pjf
     * @Date 2015年10月29日
     * @param parm
     */
    public void deleteRolePrivileges(Map<String, Object> parm);
    
    /** 获取tree
     * @author pjf
     * @Date 2015年10月29日
     * @param parm
     * @return
     */
    public List<Map<String, Object>> getTree(Map<String, Object> parm);

    /** 根据角色删除 角色-用户 中间表
     * @author pjf
     * @Date 2015年11月2日
     * @param parm
     */
    public void deleteRoleUser(Map<String, Object> parm);
}
