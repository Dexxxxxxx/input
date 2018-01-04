package com.cdhy.service.invoice.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.invoice.IInvoiceMapper;
import com.cdhy.dao.security.DepartMapper;
import com.cdhy.entity.Page;
import com.cdhy.exception.BizException;
import com.cdhy.service.invoice.IInvoiceService;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.MapUtil;
@Service
public class InvoiceServiceImpl implements IInvoiceService {
    @Autowired
    private IInvoiceMapper mapper;
    @Autowired
    private DepartMapper departMapper;
//烟草发票管理
    @SuppressWarnings("unchecked")
    @Override
    public Page listTobaccoInvoice(Map<String, Object> param) {
	String suffix=suffixCheck();
	param.put("suffix", suffix);
	String type=MapUtil.getString(param, "invoice_type").equals("purchase")?"1":"";
	String status=MapUtil.getString(param, "invoice_status").equals("normal")?"1":"-1";
	param.put("status", status);
	param.put("type", type);
	Object start = param.get("start");
	Object limit = param.get("limit");
	Page page = new Page(start, limit);
	param.put("start", page.getStart());
	param.put("end", page.getEnd());
	Map<String, Object> result=(Map<String, Object>) ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	Map<String, Object> user=(Map<String, Object>) result.get("user");
	String username=user.get("USERNAME").toString();
	if(!username.equals("admin")){
//	    Map<String, Object> map=new HashMap<String,Object>();
//	    map.put("id", user.get("DEPARTID"));
//	    Map<String, Object> deptInfo=departMapper.getById(map);
//	    String code=deptInfo.get("CODE").toString();
//	    param.put("code", code);
	    param.put("departid", user.get("DEPARTID"));
	}
	int size = mapper.tobaccoInvoiceCount(param);
	page.setRowsCount(size);
	if (size > 0) {
	    List<Map<String, Object>> list = mapper.listTobaccoInvoice(param);
	    page.setData(list);
	}
	return page;
    }
    
    @Override
    public List<Map<String, Object>> getTobaccoInvoiceDetail(Map<String, Object> param) {
	String suffix=suffixCheck();
	param.put("suffix", suffix);
	return mapper.getTobaccoInvoiceDetail(param);
    }

    
    
    
    @Override
    public Page listCigaretteInvoice(Map<String, Object> param) {
	return null;
    }

    @Override
    public List<Map<String, Object>> getCigaretteInvoiceDetail(Map<String, Object> param) {
	return null;
    }
    
//卷烟发票管理

    @SuppressWarnings("unchecked")
    private String suffixCheck(){
	Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
		.getAttribute(LoginController.LOGIN_USER);
	Map<String, Object> dept = (Map<String, Object>) loginMap.get("dept");
	String pidcode=MapUtil.getString(dept, "CODE");
	if(pidcode.length()>=4){
	    pidcode=pidcode.substring(0,4);
	}
	if(pidcode.equals("")){
	    throw new BizException("");
	}
	String suffix="";
	if(pidcode.equals("6103")){
	    //宝鸡
	    suffix="_BJ";
	}else if(pidcode.equals("6104")){
	    //咸阳
	    suffix="_XY";
	}else if(pidcode.equals("6106")){
	    //延安
	    suffix="_YA";
	}else if(pidcode.equals("6107")){
	    //汉中
	    suffix="_HZ";
	}else if(pidcode.equals("6124")){
	    //安康
	    suffix="_AK";
	}else if(pidcode.equals("6125")){
	    //商洛
	    suffix="_SL";
	}
	return suffix;
    }



}
