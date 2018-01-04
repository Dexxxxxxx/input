package com.cdhy.service.cigarette.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.cigarette.CommonsMapper;
import com.cdhy.service.cigarette.ICommonsService;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.MapUtil;

@Service("CommonsService")
public class CommonsService implements ICommonsService {
	@Resource
	CommonsMapper commonsMapper;

	@SuppressWarnings("unchecked")
	@Override
	public int modifyCustomerInfo(Map<String, Object> param) {
		Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
		Map<String, Object> dept = (Map<String, Object>) loginMap.get("dept");
		param.put("ssdwdm", MapUtil.getString(dept, "CODE"));
		return commonsMapper.modifyCustomerInfo(param);
	}

}
