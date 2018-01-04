package com.cdhy.service.security.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.runner.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.dao.security.LoginMapper;
import com.cdhy.exception.BizException;
import com.cdhy.service.security.ILoginService;
import com.cdhy.util.CookieUtil;
import com.cdhy.util.MD5;
import com.cdhy.util.MapUtil;
import com.framework.util.ResourceUtil;
@Service
public class LoginServiceImpl implements ILoginService {
	public static final String DEFAULT_PASSWORD = ResourceUtil.getDefaultPassword();
	@Autowired
	private LoginMapper mapper;
	@Override
	public Map<String, Object> login(Map<String, Object> parm,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map=new HashMap<String,Object>();
		String username=MapUtil.getString(parm, "username");
		String password=MapUtil.getString(parm, "password");
		if(username.equals("")||password.equals("")){
			throw new BizException("用户名或密码必填");
		}
		String md5Psw=buildPassword(username,password);
		map.put("username", username);
		map.put("password", md5Psw);
		final Map<String,Object> user=mapper.login(map);
		if(user==null){
			throw new BizException("用户名或密码不正确");
		}
		//用户是否禁用 TODO 系统设置是否含禁用
		CookieUtil.saveCookie(username, password, response);
//		String userMsg=CookieUtil.readCookie(request);
//		System.out.println(userMsg);
		//获取用户权限
		List<Map<String, Object>> rolePrivilege=null;
		if("admin".equals(username)){
			rolePrivilege=mapper.queryAllRolePrivilege();
		}else{
			rolePrivilege=mapper.queryRolePrivilege(user);
		}
		Set<String> rolePrivilegeSet = new HashSet<String>();
		for(Map<String, Object> pmap:rolePrivilege){
			String userprivileges=MapUtil.getString(pmap, "URL").trim();
			if(!"".equals(userprivileges)){
				rolePrivilegeSet.add(userprivileges);
			}
		}
		map.clear();
		map.put("user", user);
		map.put("rolePrivilege", rolePrivilege);
		map.put("rolePrivilegeSet", rolePrivilegeSet);
		//判断密码是否为初始密码
		final String defaultPsw = buildPassword(user.get("USERNAME").toString(),
			LoginServiceImpl.DEFAULT_PASSWORD);
		//user
		@SuppressWarnings("serial")
		int flag = mapper.queryDefaultPasswordFlag(new HashMap<String, Object>(){{
		    put("id", MapUtil.getString(user, "ID"));
		    put("newPsw", defaultPsw);
		}});
		map.put("defaultPasswordFlag", flag);
		@SuppressWarnings("serial")
		Map<String, Object> dept=mapper.getDeptInfo(new HashMap<String,Object>(){
		    {
			put("id",MapUtil.getString(user, "DEPARTID"));
		    }
		});
		map.put("dept", dept);
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updatePassword(Map<String, Object> parm) {
		String oldPassword=MapUtil.getString(parm, "oldPassword");
		String newPassword=MapUtil.getString(parm, "newPassword");
		String newPassword2=MapUtil.getString(parm, "newPassword2");
		if(oldPassword.equals("")||newPassword.equals("")||newPassword2.equals("")){
			throw new BizException("密码信息不能为空");
		}
		Map<String, Object> loginUser = (Map<String, Object>) parm.get("loginUser");
		String username=((Map<String, Object>)loginUser.get("user")).get("USERNAME").toString();
		String id=((Map<String, Object>)loginUser.get("user")).get("ID").toString();
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("id", id);
		String oldPwd=mapper.getOldPassword(map);
		String oldpsw=buildPassword(username, oldPassword);
		if(oldPwd==null||!oldPwd.equals(oldpsw)){
			throw new BizException("原始密码错误");
		}
		if(!newPassword.equals(newPassword2)){
			throw new BizException("两次密码输入不一致");
		}
		if(newPassword.toString().trim().length()<6 || newPassword.toString().trim().length()>40){
			throw new BizException("密码长度只能在6个至13个字符之间");
		}
//		if(newPassword.toString().trim().matches("[0-9a-zA-Z]+")){
//			throw new BizException("您的新密码过于简单,为了账户安全考虑,请增加密码的复杂度!");
//		}
		String newpwd = buildPassword(username,newPassword);
		map.put("password", newpwd);
		mapper.updatePassword(map);

	}

	@Override
	public void resetPassword(Map<String, Object> parm) {
		Map<String, Object> user= mapper.getUser(parm);
		if(user==null){
			throw new BizException("用户不存在");
		}
		String newpassword = buildPassword(user.get("USERNAME").toString(),LoginServiceImpl.DEFAULT_PASSWORD);
		parm.put("password", newpassword);
		mapper.updatePassword(parm);
	}
	public static final String buildPassword(String username,String password){
		String md5Psw = MD5.MD5Encode(password+username).toLowerCase();
		return md5Psw;
		
	}

}
