package com.cdhy.service.cigarette.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.dao.cigarette.CigarCustomerMapper;
import com.cdhy.entity.Page;
import com.cdhy.entity.Result;
import com.cdhy.exception.BizException;
import com.cdhy.service.cigarette.ICigarCustomerService;
import com.cdhy.util.MapUtil;
import com.cdhy.util.ReadTxtFileTest;
@Service
public class CigarCustomerServiceImpl implements ICigarCustomerService {
    @Autowired
    private CigarCustomerMapper mapper;
    @Override
    public Page list(Map<String, Object> parm) {
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
    public void update(Map<String, Object> parm) {
	//查询该客户信息在数据库中是否存在
	Map<String, Object> map =new HashMap<String,Object>();
	map.put("ghdwdm", MapUtil.getString(parm, "ghdwdm"));
	Map<String, Object> custom_info = mapper.getById(map);
	if(custom_info==null){
	    //数据库中不存在
	    parm.put("orig_cd", "");
	    mapper.add(parm);
	}else{
	    //数据库中已存在
	    mapper.update(parm);
	}
    }

    @Override
    public Map<String, Object> getById(Map<String, Object> parm) {
	return mapper.getById(parm);
    }

    @Override
    public void add(Map<String, Object> parm) {
	Map<String, Object> map = new HashMap<String,Object>();
	Map<String, Object> customer = new HashMap<String,Object>();
	String ghdwdm = MapUtil.getString(parm, "ghdwdm");
	String ghdwmc = MapUtil.getString(parm, "ghdwmc");
//	String ghdwnsrsbh = MapUtil.getString(parm, "ghdwnsrsbh");
//	String ghdwdzdh = MapUtil.getString(parm, "ghdwdzdh");
//	String ghdwyhzh = MapUtil.getString(parm, "ghdwyhzh");
	if("".equals(ghdwdm)){
	    throw new BizException("购货单位代码为空");
	}
	if("".equals(ghdwmc)){
	    throw new BizException("购货单位名称为空");
	}
	//判断单位代码是否已存在
	map.put("ghdwdm", ghdwdm);
	customer = mapper.getById(parm);
	if(customer!=null){
	    throw new BizException("该客户单位代码已存在");
	}
	map.clear();
	//判断单位名称是否已存在
	map.put("ghdwmc", ghdwmc);
	customer = mapper.getById(map);
	if(customer!=null){
	    throw new BizException("该客户名称已存在");
	}
	mapper.add(parm);
    }

    @Override
    public String saveBillsToDB(String filePath) {
	File file = new File(filePath);
	//得到文件后缀名
	String fileName = file.getName();
    String prefix = fileName.substring(fileName.lastIndexOf(".")+1);
   
	if(!"txt".equals(prefix)){
		throw new BizException("文件类型不合法，请选择txt文件");
	}
    
	ReadTxtFileTest rf = new ReadTxtFileTest();
	//读取文件信息，并放入List集合中
	List<Map<String, Object>> tempList =new ArrayList<>();
	try {
	    tempList = rf.readTxtFile(filePath);
	} catch (BizException ex) {
	    throw new BizException(ex.getMessage());
	}
	//删除文件
	if (file.isFile()&&file.exists())  
		file.delete();
	
	int c = 0;//插入成功条数
	for(Map<String, Object> mm: tempList){
	    Map<String, Object> map =new HashMap<String,Object>();
		map.put("ghdwdm", MapUtil.getString(mm, "ghdwdm"));
		Map<String, Object> custom_info = mapper.getById(map);
		if(custom_info==null){
		    c++;
		    //数据库中不存在
		    mm.put("orig_cd", "");
		    mapper.add(mm);
		}
	}
		return "导入成功，成功导入"+c+"条数据";
}

    @Override
    public int delete(Map<String, Object> parm) {
	Object obj = parm.get("ids");
	if (obj == null||obj=="") {
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
	return n;
}
    

}
