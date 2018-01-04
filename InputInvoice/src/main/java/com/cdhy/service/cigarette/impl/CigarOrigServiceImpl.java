package com.cdhy.service.cigarette.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.dao.cigarette.CigarOrigMapper;
import com.cdhy.entity.Page;
import com.cdhy.entity.Result;
import com.cdhy.service.cigarette.ICigarOrigService;
import com.cdhy.util.MapUtil;
@Service
public class CigarOrigServiceImpl implements ICigarOrigService {
    @Autowired
    private CigarOrigMapper mapper;
    @Override
    public Page list(Map<String, Object> parm) {
	String status = MapUtil.getString(parm, "invoice_status").equals("yes") ? "1" : "0";
	//传入的type判断专普票类型
	parm.put("status", status);
	Object start = parm.get("start");
	Object limit = parm.get("limit");
	Page page=new Page(start,limit);
	parm.put("start", page.getStart());
	parm.put("end", page.getEnd());
	int size = mapper.listCount(parm);
	page.setRowsCount(size);
	if(size>0){
		List<Map<String, Object>> list=mapper.list(parm);
		page.setData(list);
	}
	return page;
    }

    @Override
    public List<Map<String, Object>> getById(Map<String, Object> parm) {
	return mapper.getById(parm);
    }

    @Override
    public Result saveBillsToDB(String filePath) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void update(Map<String, Object> parm) {
	// TODO Auto-generated method stub

    }

}
