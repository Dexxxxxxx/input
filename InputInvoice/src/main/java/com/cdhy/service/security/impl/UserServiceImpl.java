package com.cdhy.service.security.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.security.UserMapper;
import com.cdhy.entity.Page;
import com.cdhy.exception.BizException;
import com.cdhy.service.security.IUserService;
import com.cdhy.util.ContextHolderUtils;
@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    private UserMapper mapper;
    @Override
    public Page list(Map<String, Object> parm) {
    	Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
		Map<String,Object> mm=(Map<String,Object>)m.get("user");
		// 公司管理员查询
		if (!"admin".equals(mm.get("USERNAME").toString())&&mm.get("USERNAME").toString().startsWith("admin")) {
			
				parm.put("dpid", mm.get("DEPARTID"));
			
		}
		

		Object start = parm.get("start");
		Object limit = parm.get("limit");
		Page page = new Page(start, limit);
		parm.put("start", page.getStart());
		parm.put("end", page.getEnd());
		int size = mapper.listCount(parm);
		page.setRowsCount(size);
		if (size > 0) {
			List<Map<String, Object>> list = mapper.list(parm);
			page.setData(list);
		}
		return page;
    }

    @Override
    public void add(Map<String, Object> parm) {
    	if (parm == null) {
    	    throw new BizException("所添加的用户为空");
    	}
    	parm.put("password",
    		LoginServiceImpl.buildPassword(parm.get("username").toString(), LoginServiceImpl.DEFAULT_PASSWORD));
    	parm.put("enabled", 0);
    	mapper.add(parm);
    }

    @Override
    public int delete(Map<String, Object> parm) {
    	Object obj = parm.get("ids");
    	if (obj == null || obj == "") {
    	    throw new BizException("选择为空");
    	}
    	try {
    	    String ids[] = obj.toString().split(",");
    	    parm.put("array", ids);
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    throw new BizException("禁用失败");
    	}
    	parm.put("enabled", "-1");
    	int n = mapper.deleteByIds(parm);
    	return n;
    }

    @Override
    public int deleteUsers(Map<String, Object> parm) {
	Object obj = parm.get("ids");
    	if (obj == null || obj == "") {
    	    throw new BizException("选择为空");
    	}
    	try {
    	    String ids[] = obj.toString().split(",");
    	    parm.put("array", ids);
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    throw new BizException("删除失败");
    	}
    	//批量删除用户的角色
    	mapper.deleteUsersRole(parm);
    	//批量删除用户
    	int n= mapper.deleteUsers(parm);
	return n;
    }

    @Override
    public void update(Map<String, Object> parm) {
    	if (parm == null || null == parm.get("id") || "".equals(parm.get("id").toString())) {
    	    throw new BizException("所要修改的用户信息不合法或为空");
    	}
    	mapper.update(parm);
    }

    @Override
    public Map<String, Object> getById(Map<String, Object> parm) {

	return mapper.getById(parm);
    }

    @Override
    public void saveRole(Map<String, Object> parm) {
	if (parm == null || null == parm.get("userId") || "".equals(parm.get("userId").toString())) {
	    throw new BizException("所要设置的用户信息不合法或为空");
	}
	Object obj = parm.get("roleIds");
	if (obj == null) {
	    throw new BizException("选择为空");
	}
	String ids[] = obj.toString().split(",");
	parm.put("roleIds", ids);
	mapper.deleteUserRole(parm);
	mapper.setRole(parm);
    }

    @Override
    public List<Map<String, Object>> getAllRoleIdById(Map<String, Object> parm) {
	return mapper.getAllRoleIdById(parm);
    }
    
    @Override
	public int invokeByStart(Map<String, Object> parm) {
		Object obj = parm.get("ids");
		if (obj == null || obj == "") {
		    throw new BizException("选择为空");
		}
		try {
		    String ids[] = obj.toString().split(",");
		    parm.put("array", ids);
		} catch (Exception e) {
		    e.printStackTrace();
		    throw new BizException("启用失败");
		}
		parm.put("enabled", "0");
		int n = mapper.startByIds(parm);
		return n;
	}

	@Override
	public List<Map<String, Object>> saveUserDepartTree(Map<String, Object> parm) {
		List<Map<String, Object>> list = mapper.setUserDepartTree(parm);
		return list;
	}

	@Override
	public void saveDepart(Map<String, Object> parm) {
		if (parm == null || null == parm.get("userId") || "".equals(parm.get("userId").toString())) {
		    throw new BizException("所要设置的用户信息不合法或为空");
		}
		Object obj =parm.get("departIds");
		if(obj==null){
			throw new BizException("选择为空");
		}
		String ids[]=obj.toString().split(",");
		parm.put("array", ids);
		mapper.deleteUserDepart(parm);
		mapper.setDepart(parm);
	}
	
	  public Map<String,Object> wxQueryDep(Map<String, Object> parm){
		  if (parm == null || null == parm.get("username") || "".equals(parm.get("username").toString())) {
			    throw new BizException("手机号不能为空");
			}
		  Map<String,Object> user=mapper.getUserByUsername(parm);
		  if(user==null){
			  throw new BizException("未查到该用户");
		  }
		Map<String,Object> org=mapper.getOrgByDep(user);
		  
		  user.put("ORG_NAME", org.get("ORG_NAME"));
		  return user;
	  }

	

}
