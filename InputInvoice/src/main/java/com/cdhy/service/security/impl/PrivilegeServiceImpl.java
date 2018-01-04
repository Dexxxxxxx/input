package com.cdhy.service.security.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.security.PrivilegeMapper;
import com.cdhy.entity.Page;
import com.cdhy.exception.BizException;
import com.cdhy.service.security.IPrivilegeService;
import com.cdhy.util.ContextHolderUtils;
@Service
public class PrivilegeServiceImpl implements IPrivilegeService{
    @Autowired
    private PrivilegeMapper mapper;
    @Override
    public Page list(Map<String, Object> parm) {
	Object start = parm.get("start");
	Object limit = parm.get("limit");
	Page page = new Page(start, limit);
	parm.put("start", page.getStart());
	parm.put("end", page.getEnd());
	int size =mapper.listCount(parm);
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
	    throw new BizException("所添加的权限为空");
	}
	try {
	    Object ptype = parm.get("ptype");
	    Integer.parseInt(ptype.toString());
	    Object ob = parm.get("orderby");
	    Integer.parseInt(ob.toString());
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	    throw new BizException("排序或类型必须是数字",e);
	}
	mapper.add(parm);
    }

    @Override
    public int delete(Map<String, Object> parm) {
	Object obj = parm.get("ids");
	if(obj==null||obj==""){
	    throw new BizException("选择为空");
	}
	try {
	    String ids[] = obj.toString().split(",");
	    parm.put("array", ids);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new BizException("删除失败");
	}
	//查出需要删除的 （包括子节点）
	List<Map<String,Object>> lmpids = mapper.getListByPids(parm);
	String ids[] = new String[lmpids.size()];
	for (int i=0;i<ids.length;i++) {
	    ids[i]=lmpids.get(i).get("ID").toString();
	}
	parm.put("array", ids);
	
	//删除 权限
	int n = mapper.deleteByIds(parm);
	//删除 角色-权限
	mapper.deleteRolePrivilege(parm);
	return n;
    }

    @Override
    public void update(Map<String, Object> parm) {
	if (parm == null || null == parm.get("id")
		|| "".equals(parm.get("id").toString())) {
	    throw new BizException("所要修改的权限信息不合法或为空");
	}
	try {
	    Object ptype = parm.get("ptype");
	    Integer.parseInt(ptype.toString());
	    Object ob = parm.get("orderby");
	    Integer.parseInt(ob.toString());
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	    throw new BizException("排序或类型必须是数字",e);
	}
	mapper.update(parm);
    }

    @Override
    public Map<String, Object> getById(Map<String, Object> parm) {
	return mapper.getById(parm);
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
