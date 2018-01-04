package com.cdhy.service.security.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.security.RoleMapper;
import com.cdhy.entity.Page;
import com.cdhy.exception.BizException;
import com.cdhy.service.security.IRoleService;
import com.cdhy.util.ContextHolderUtils;
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleMapper mapper;
    @Override
    public Page list(Map<String, Object> parm) {
		Map<String, Object> m = (Map<String, Object>) ContextHolderUtils.getSession()
				.getAttribute(LoginController.LOGIN_USER);
		Map<String, Object> mm = (Map<String, Object>) m.get("user");
		// 管理员查询
		if (!"admin".equals(mm.get("USERNAME").toString())) {
			parm.put("id", mm.get("ID"));
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
    	Map<String, Object> m = (Map<String, Object>) ContextHolderUtils.getSession()
				.getAttribute(LoginController.LOGIN_USER);
		Map<String, Object> mm = (Map<String, Object>) m.get("user");
		
		parm.put("userid", mm.get("ID"));
	if (parm == null) {
	    throw new BizException("所添加的角色为空");
	}
	mapper.add(parm);
	mapper.addRoleUser(parm);
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
    	    throw new BizException("删除失败");
    	}
    	int n = mapper.deleteByIds(parm);
    	// 删除 用户-角色中间表
    	mapper.deleteRoleUser(parm);
    	// 删除权限-角色中间表
    	mapper.deleteRolePrivileges(parm);
    	return n;
    }

    @Override
    public void update(Map<String, Object> parm) {
	if (parm == null || null == parm.get("id") || "".equals(parm.get("id").toString())) {
	    throw new BizException("所要修改的角色信息不合法或为空");
	}
	mapper.update(parm);
    }

    @Override
    public Map<String, Object> getById(Map<String, Object> parm) {

	return mapper.getById(parm);
    }

    @Override
    public void savePrivilege(Map<String, Object> parm) {
	if (parm == null || null == parm.get("roleId") || "".equals(parm.get("roleId").toString())) {
	    throw new BizException("所要设置的角色信息不合法或为空");
	}
	Object obj = parm.get("ids");
	if (obj == null) {
	    throw new BizException("选择为空");
	}
	String ids[] = obj.toString().split(",");
	parm.put("pids", ids);
	mapper.deleteRolePrivilege(parm);
	mapper.setPrivilege(parm);
    }

    @Override
    public List<Map<String, Object>> getTree(Map<String, Object> parm) {
    	Map<String,Object> m=(Map<String,Object>)ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
		Map<String,Object> mm=(Map<String,Object>)m.get("user");
		if(!"admin".equals(mm.get("USERNAME").toString())){
		parm.put("id", mm.get("ID"));
		}
	List<Map<String, Object>> list = mapper.getTree(parm);
	return list;
    }
}
