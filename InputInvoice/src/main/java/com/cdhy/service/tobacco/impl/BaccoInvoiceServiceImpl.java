package com.cdhy.service.tobacco.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.tobacco.BaccoInvoiceMapper;
import com.cdhy.entity.Page;
import com.cdhy.service.tobacco.IBaccoInvoiceService;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.MapUtil;
@Service
public class BaccoInvoiceServiceImpl implements IBaccoInvoiceService {
    @Autowired
    private BaccoInvoiceMapper mapper;
    @Override
    public Page list(Map<String, Object> param) {
	param.put("suffix", GetSuffix.suffixCheck());
	String type=MapUtil.getString(param, "invoice_type").equals("purchase")?"1":"2";
	
	String invoice_status=MapUtil.getString(param, "invoice_status");
	String status=invoice_status.equals("normal")?"1":invoice_status.equals("failed")?"0":"-1";
	param.put("status", status);
	param.put("type", type);
	
	Page page=new Page();
	if(type.equals("1")){
	    page=gen_list(param);
	}else if(type.equals("2")){
	    page=spe_list(param);
	}
	return page;
    }
    @SuppressWarnings("unchecked")
    public Page gen_list(Map<String, Object> param){
	Object start = param.get("start");
	Object limit = param.get("limit");
	Page page = new Page(start, limit);
	param.put("start", page.getStart());
	param.put("end", page.getEnd());
	Map<String, Object> result=(Map<String, Object>) ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	Map<String, Object> user=(Map<String, Object>) result.get("user");
	String username=user.get("USERNAME").toString();
	if(!username.equals("admin")){
	    param.put("departid", user.get("DEPARTID"));
	}
	int size = mapper.listCount(param);
	page.setRowsCount(size);
	if (size > 0) {
	    List<Map<String, Object>> list = mapper.list(param);
	    page.setData(list);
	}
	return page;
    }
    @SuppressWarnings("unchecked")
    public Page spe_list(Map<String, Object> param){
	Object start = param.get("start");
	Object limit = param.get("limit");
	Page page = new Page(start, limit);
	param.put("start", page.getStart());
	param.put("end", page.getEnd());
	Map<String, Object> result=(Map<String, Object>) ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	Map<String, Object> user=(Map<String, Object>) result.get("user");
	String username=user.get("USERNAME").toString();
	if(!username.equals("admin")){
	    param.put("departid", user.get("DEPARTID"));
	}
	int size = mapper.spe_listCount(param);
	page.setRowsCount(size);
	if (size > 0) {
	    List<Map<String, Object>> list = mapper.spe_list(param);
	    page.setData(list);
	}
	return page;
    }
    @Override
    public List<Map<String, Object>> listDetail(Map<String, Object> param) {
	param.put("suffix", GetSuffix.suffixCheck());
	return mapper.listDetail(param);
    }
    @Override
    public List<Map<String, Object>> listInvoice(Map<String, Object> param) {
	param.put("suffix", GetSuffix.suffixCheck());
	return mapper.listInvoice(param);
    }

    
}
