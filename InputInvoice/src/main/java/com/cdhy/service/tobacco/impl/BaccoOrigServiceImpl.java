package com.cdhy.service.tobacco.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.tobacco.BaccoOrigMapper;
import com.cdhy.entity.Page;
import com.cdhy.service.tobacco.IBaccoOrigService;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.MapUtil;
@Service
public class BaccoOrigServiceImpl implements IBaccoOrigService {
    @Autowired
    private BaccoOrigMapper mapper;
    
    @SuppressWarnings("unchecked")
    @Override
    public Page list(Map<String, Object> parm) {
	parm.put("suffix", GetSuffix.suffixCheck());
	String status = MapUtil.getString(parm, "invoice_status").equals("yes") ? "1" : "";
	parm.put("status", status);
	Object start = parm.get("start");
	Object limit = parm.get("limit");
	Page page = new Page(start, limit);

	parm.put("start", page.getStart());
	parm.put("end", page.getEnd());
	Map<String, Object> result = (Map<String, Object>) ContextHolderUtils.getSession()
		.getAttribute(LoginController.LOGIN_USER);
	Map<String, Object> user = (Map<String, Object>) result.get("user");
	Map<String, Object> dept = (Map<String, Object>) result.get("dept");
	String username = user.get("USERNAME").toString();
	if (!username.equals("admin")) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("id", user.get("DEPARTID"));
	    String code = dept.get("CODE").toString();
	    parm.put("code", code);
	}
	int size = mapper.listCount(parm);
	page.setRowsCount(size);
	if (size > 0) {
	    List<Map<String, Object>> list = mapper.list(parm);
	    page.setData(list);
	}
	return page;
    }

    @Override
    public List<Map<String, Object>> listDetail(Map<String, Object> parm) {
	GetSuffix suffix=new GetSuffix();
	parm.put("suffix", suffix.suffixCheck());
	return mapper.listDetail(parm);
    }
    

}
