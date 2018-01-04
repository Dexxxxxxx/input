package com.cdhy.service.tobacco.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.dao.tobacco.BaccoBuyerMapper;
import com.cdhy.entity.Page;
import com.cdhy.exception.BizException;
import com.cdhy.service.tobacco.IBaccoBuyerService;
@Service
public class BaccobuyerServiceImpl implements IBaccoBuyerService {
    @Autowired
    private BaccoBuyerMapper mapper;
    @Override
    public Page list(Map<String, Object> param) {
	Object start = param.get("start");
	Object limit = param.get("limit");
	Page page = new Page(start, limit);
	param.put("start", page.getStart());
	param.put("end", page.getEnd());
	int size = mapper.listCount(param);
	page.setRowsCount(size);
	if (size > 0) {
	    List<Map<String, Object>> list = mapper.listBuyer(param);
	    page.setData(list);
	}
	return page;
    }

    @Override
    public Map<String, Object> getById(Map<String, Object> param) {
	return mapper.getBuyer(param);
    }

    @Override
    public void addBuyer(Map<String, Object> param) {
	//查找是否有重复的纳税人识别号
	Map<String, Object> buyer=mapper.getBuyer(param);
	if(buyer!=null){
	    throw new BizException("该纳税人识别号已添加");
	}
	mapper.addBuyer(param);

    }

    @Override
    public int delBuyer(Map<String, Object> param) {
	Object obj = param.get("ids");
	if (obj == null||obj=="") {
	    throw new BizException("选择为空");
	}
	try {
	    String ids[] = obj.toString().split(",");
	    param.put("array", ids);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new BizException("删除失败");
	}
	int n = mapper.delBuyer(param);
	return n;
    }

    @Override
    public void updateBuyer(Map<String, Object> param) {
	mapper.updateBuyer(param);

    }

    @Override
    public Map<String, Object> checkAndSave(Map<String, Object> param) {
	//查找是否有重复的纳税人识别号
	Map<String, Object> buyer = mapper.getBuyer(param);
	if (buyer != null) {
	    throw new BizException("该纳税人识别号已添加");
	}
	mapper.addBuyer(param);
	return mapper.getBuyer(param);
    }

}
