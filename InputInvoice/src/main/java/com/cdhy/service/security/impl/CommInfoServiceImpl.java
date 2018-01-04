package com.cdhy.service.security.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdhy.dao.security.CommInfoMapper;
import com.cdhy.dao.security.NsrInfoMapper;
import com.cdhy.entity.Page;
import com.cdhy.exception.BizException;
import com.cdhy.service.security.IcommInfoService;
import com.cdhy.util.EInvoiceUtil;
import com.cdhy.util.MapUtil;
@Service
public class CommInfoServiceImpl implements IcommInfoService {
    private static final Logger log = Logger.getLogger(CommInfoServiceImpl.class);
    @Autowired
    private CommInfoMapper commInfo_mapper;
    @Autowired
    private NsrInfoMapper nsr_mapper;
    @Override
    public Page list(Map<String, Object> parm) {
	Object start = parm.get("start");
	Object limit = parm.get("limit");
	Page page=new Page(start,limit);
	parm.put("start", page.getStart());
	parm.put("end", page.getEnd());
	int size = nsr_mapper.listCount(parm);
	page.setRowsCount(size);
	if(size>0){
		List<Map<String, Object>> list=nsr_mapper.list(parm);
		page.setData(list);
	}
	return page;
    }

    @Override
    public void add(Map<String, Object> parm) {
	if(nsr_mapper.getById(parm)!=null){
	    throw new BizException("该税号信息已存在");
	}else{
	    parm.put("pid", "");
	    nsr_mapper.add(parm);
	}
	if(commInfo_mapper.getInfo(parm)!=null){
	    parm.put("status", "0");
	    commInfo_mapper.update(parm);
	}else{
	    parm.put("status", "0");
	    parm.put("access_token", "");
	    commInfo_mapper.add(parm);
	}
    }

    @Override
    public int delete(Map<String, Object> parm) {
	Object obj = parm.get("ids");
	Object obj_nsr = parm.get("nsrids");
	if (obj == null||obj=="") {
	    throw new BizException("选择为空");
	}
	try {
	    String ids[] = obj.toString().split(",");
	    String nsrid[] = obj_nsr.toString().split(",");
	    for(int i=0;i<ids.length;i++){
		parm.put("nsrid", ids[i]);
		int dps = nsr_mapper.hasDept(parm);
		if (dps > 0) {
		    throw new BizException("纳税人识别号:"+nsrid[i]+"含子部门不能删除");
		}
	    }
	    parm.clear();
	    parm.put("array", nsrid);
	}catch(BizException ex){
	    throw new BizException(ex.getMessage());
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new BizException("删除失败");
	}
	// 1、判断该税号改税号是否关联了部门
	commInfo_mapper.deleteByIds(parm);
	
	return nsr_mapper.deleteByIds(parm);
    }

    @Override
    public void update(Map<String, Object> parm) {
	//修改税号信息
	nsr_mapper.update(parm);
	//修改接口信息
	//查看comm_info中是否含该税号信息
	if(commInfo_mapper.getInfo(parm)==null){
	    parm.put("status", "0");
	    parm.put("access_token", "");
	    commInfo_mapper.add(parm);
	}else{
	    parm.put("status", "0");
	    commInfo_mapper.update(parm);
	}
    }

    @Override
    public void reset(Map<String, Object> parm) {
	String id=MapUtil.getString(parm, "id");
	if("".equals(id)){
	    throw new BizException("id is null");
	}
	Map<String, Object> comm_info = commInfo_mapper.getInfo(parm);
	log.debug("############【获取ACCESS_TOKEN开始】##################");
	    String access_token = EInvoiceUtil.getAccess_token(comm_info);
	    log.debug("############【获取ACCESS_TOKEN结束】##################");
	    String nsrsbh = MapUtil.getString(comm_info, "NSRSBH");
	    String openid = MapUtil.getString(comm_info, "OPENID");
	    Map<String, Object> map=new HashMap<String,Object>();
	    map.put("nsrsbh", nsrsbh);
	    map.put("openid", openid);
	    map.put("access_token", access_token);
	    map.put("status", "1");
	    commInfo_mapper.update(map);
	    log.debug("############【ACCESS_TOKEN更新数据库结束】##################");
    }

    @Override
    public Map<String, Object> getById(Map<String, Object> parm) {
	return commInfo_mapper.getById(parm);
    }
    @Override
    public List<Map<String, Object>> getTree(Map<String, Object> parm) {
	return commInfo_mapper.getTree(parm);
    }

}
