package com.cdhy.service.tobacco.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.dao.tobacco.BaccoMessageMapper;
import com.cdhy.entity.Page;
import com.cdhy.exception.BizException;
import com.cdhy.service.tobacco.IBaccoMessageService;
@Service
public class BaccoMessageServiceImpl implements IBaccoMessageService {
    @Autowired
    private BaccoMessageMapper mapper;
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
	    List<Map<String, Object>> list = mapper.list(param);
	    page.setData(list);
	}
	return page;
    }

    @Override
    public Map<String, Object> getById(Map<String, Object> param) {
	return mapper.getById(param);
    }

    @Override
    public void add(Map<String, Object> param) {
	Map<String, Object> msg=mapper.getById(param);
	if(msg!=null){
	    throw new BizException("该商品编码已存在");
	}
	mapper.add(param);

    }

    @Override
    public int delete(Map<String, Object> param) {
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
	return mapper.delete(param);
    }

    @Override
    public void update(Map<String, Object> param) {
	mapper.update(param);
	
    }

}
