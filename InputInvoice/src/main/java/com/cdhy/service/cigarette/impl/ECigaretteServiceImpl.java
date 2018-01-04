package com.cdhy.service.cigarette.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.cigarette.CigarInvoiceDetailMapper;
import com.cdhy.dao.cigarette.CigarInvoiceMapper;
import com.cdhy.dao.cigarette.CigarOrigMapper;
import com.cdhy.dao.security.CommInfoMapper;
import com.cdhy.entity.Page;
import com.cdhy.exception.BizException;
import com.cdhy.service.cigarette.IECigaretteService;
import com.cdhy.util.BuildInvoiceData;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.EInvoiceDataHandle;
import com.cdhy.util.EInvoiceUtil;
import com.cdhy.util.MapUtil;
@Service
public class ECigaretteServiceImpl implements IECigaretteService {
    @Autowired
    private CigarInvoiceMapper InvoiceMapper;
    @Autowired
    private CigarOrigMapper orig_mapper;
    @Autowired
    private CigarInvoiceDetailMapper InvoiceDetailMapper;
    @Autowired
    private CommInfoMapper commonInfoMapper;
    @Override
    public Page elist(Map<String, Object> parm) {
	parm.put("type", "026");
	parm.put("start_date", parm.get("start_date").toString().replaceAll("-", ""));
	parm.put("end_date", parm.get("end_date").toString().replaceAll("-", ""));
	Object start = parm.get("start");
	Object limit = parm.get("limit");
	Page page=new Page(start,limit);
	parm.put("start", page.getStart());
	parm.put("end", page.getEnd());
	int size = InvoiceMapper.listCount(parm);
	page.setRowsCount(size);
	if(size>0){
		List<Map<String, Object>> list=InvoiceMapper.list(parm);
		List<Map<String, Object>> _list = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> map:list){
	        	map.put("KPRQ", changeDate(MapUtil.getString(map, "KPRQ")));
	        	_list.add(map);
	        }
		page.setData(_list);
	}
	if("".equals(MapUtil.getString(parm, "yh"))){
	    page.setRowsCount(0); 
	    page.setData(null);
	}
	return page;
    }
    @Override
    public Page list(Map<String, Object> parm) {
	String status = MapUtil.getString(parm, "invoice_status");
	if("1".equals(status)||"2".equals(status)){
	    parm.put("fplxdm", "026");
	}
	//传入的type判断专普票类型
	parm.put("status", status);
	Object start = parm.get("start");
	Object limit = parm.get("limit");
	Page page=new Page(start,limit);
	parm.put("start", page.getStart());
	parm.put("end", page.getEnd());
	int size = orig_mapper.listCount(parm);
	page.setRowsCount(size);
	if(size>0){
		List<Map<String, Object>> list=orig_mapper.list(parm);
		page.setData(list);
	}
	return page;
    }
    @Override
    public List<Map<String, Object>> getById(Map<String, Object> parm) {
	return orig_mapper.getById(parm);
    }
    @Override
    public void doInvoice(Map<String, Object> parm) {
	Object obj = parm.get("ids");
	if (obj == null || obj == "") {
	    throw new BizException("选择为空");
	}
	String ids[] = obj.toString().split(",");
	parm.put("array", ids);
	// 返回的list
	String order_no = MapUtil.getString(parm, "order_no");
	for (int i = 0; i < ids.length; i++) {
	    parm.put("invoice_no", ids[i]);
	    // 获取发票公共信息
	    Map<String, Object> comm_map = orig_mapper.getInvoiceCommonInfo(parm);
	    // 获取拆分后的商品信息
	    List<Map<String, Object>> list_item = BuildInvoiceData.splitInvoice(orig_mapper.getById(parm));
	    // 生成发票的票面信息
	    List<Map<String, Object>> bulid_list = BuildInvoiceData.buildInvoice(list_item, comm_map);
	    // 存入数据库
	    saveInfo(bulid_list, order_no);
	    Map<String, Object> _map = new HashMap<String,Object>();
	    _map.put("invoice_no", ids[i]);
	    _map.put("status", "0");
	    List<Map<String, Object>> invoice_list= InvoiceMapper.getByInvoice_no(_map);
	    for (Map<String, Object> mp : invoice_list) {
		String invoice_id = MapUtil.getString(mp, "ID");
		parm.put("invoice_id", invoice_id);
		try {
		    // 2、执行开票
		    operate(parm);
		    // 1、修改源数据状态
		    parm.put("status", "1");// 开票成功
		    parm.put("fplxdm", "026");
		    orig_mapper.update(parm);
		} catch (BizException ex) {
		    revokeTran(parm);
		    throw new BizException(ex.getMessage());
		} catch (Exception e) {
		    revokeTran(parm);
		    throw new BizException(e.getMessage());
		}
	    }
	}
    }
    @Transactional(propagation=Propagation.NESTED )
    public void revokeTran(Map<String, Object> parm){
	//1、查看该发票源数据的状态
	Map<String, Object> comm_info = orig_mapper.getInvoiceCommonInfo(parm);
	if("1".equals(MapUtil.getString(comm_info, "STATUS"))){
	    //如果有开成功的票 将源数据改为失败
	    parm.put("status", "2");
	    parm.put("fplxdm", "026");
	    orig_mapper.update(parm);
	}else{
	    //2、删除该发票的发票数据和发票详情
	    InvoiceMapper.delete(parm);
	    InvoiceDetailMapper.delete(parm);
	}
    }
    //将发票信息和商品信息存入数据库
    @SuppressWarnings("unchecked")
    public void saveInfo(List<Map<String, Object>> list,String order_no){
	Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
			.getAttribute(LoginController.LOGIN_USER);
	Map<String, Object> user_info = (Map<String, Object>) loginMap.get("user");
	Map<String, Object> dept = (Map<String, Object>)loginMap.get("dept");
	//正式环境
	String xhdwmc= MapUtil.getString(dept, "NSRNAME");
	String xhdwsbh = MapUtil.getString(dept, "NSRSBH");
	String xhdwdzdh = MapUtil.getString(dept, "DZDH");
	String xhdwyhzh = MapUtil.getString(dept, "YHZH");
	for(Map<String, Object> map:list){
	    Map<String, Object> mm= new HashMap<String,Object>();
	    List<Map<String, Object>> detail = (List<Map<String, Object>>) map.get("data");
	    mm.put("order_no", order_no);
	    mm.put("invoice_no", MapUtil.getString(map, "INVOICE_NO"));
	    mm.put("tspz", "");
	    mm.put("xhdwsbh", xhdwsbh);
	    mm.put("xhdwmc", xhdwmc);
	    mm.put("xhdwdzdh", xhdwdzdh);
	    mm.put("xhdwyhzh", xhdwyhzh);
	    mm.put("ghdwdm", MapUtil.getString(map, "GHDWDM"));
	    mm.put("ghdwsbh", MapUtil.getString(map, "GHDWNSRSBH"));
	    mm.put("ghdwmc", MapUtil.getString(map, "GHDWMC"));
	    mm.put("ghdwdzdh", MapUtil.getString(map, "GHDWDZDH"));
	    mm.put("ghdwyhzh", MapUtil.getString(map, "GHDWYHZH"));
	    mm.put("zsfs", "0");//普通征收
	    mm.put("kplx", "0");//正数发票
	    mm.put("hjje", MapUtil.getString(map, "JE_COUNT"));
	    mm.put("hjse", MapUtil.getString(map, "SE_COUNT"));
	    mm.put("jshj", MapUtil.getString(map, "JSHJ_COUNT"));
	    mm.put("kce", "");//差额征收
	    mm.put("bz", "");
	    mm.put("skr", "");
	    mm.put("fhr", "");
	    mm.put("kpr", user_info.get("NAME"));
	    mm.put("deptid", dept.get("ID"));
	    mm.put("qdbz", "0");
	    if(detail.size()>8){
		mm.put("qdbz", "1");
	    }
	    mm.put("ssyf", "");
	    mm.put("yfpdm", "");
	    mm.put("yfphm", "");
	    mm.put("zfrq", "");
	    mm.put("zfr", "");
	    mm.put("type", "026");
	    mm.put("status", "0");
	    InvoiceMapper.save(mm);
	    String invoice_id=mm.get("id").toString();
	    int xh=0;
	    for(Map<String, Object> _map : detail){
		xh++;
		Map<String, Object> _mm = new HashMap<String,Object>();
		_mm.put("invoice_id", invoice_id);
		_mm.put("xh", xh);
		_mm.put("fphxz", "0");
		_mm.put("spmc", MapUtil.getString(_map, "SPMC"));
		_mm.put("spsm", "");
		_mm.put("ggxh", MapUtil.getString(_map, "GGXH"));
		_mm.put("dw", MapUtil.getString(_map, "DW"));
		_mm.put("spsl", MapUtil.getString(_map, "SPSL"));
		_mm.put("dj", MapUtil.getString(_map, "HSDJ"));
		_mm.put("je", MapUtil.getString(_map, "JE"));
		_mm.put("sl", MapUtil.getString(_map, "SL"));
		_mm.put("se", MapUtil.getString(_map, "SE"));
		_mm.put("jshj", MapUtil.getString(_map, "JSHJ"));
		_mm.put("hsbz", "1");
		_mm.put("spbm", "1030402010000000000");
		_mm.put("zxbm", "");
		_mm.put("yhzcbs", "");
		_mm.put("lslbs", "");
		_mm.put("zzstsgl", "");
		InvoiceDetailMapper.insert(_mm);
	    }
	    
	}
    }
    //执行开电票操作
    @SuppressWarnings({ "unchecked", "serial" })
    public void operate(Map<String, Object> parm) throws Exception{
	Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
		.getAttribute(LoginController.LOGIN_USER);
	final Map<String, Object> dept = (Map<String, Object>)loginMap.get("dept");
	//正式环境
	final String nsrsbh = MapUtil.getString(dept, "NSRSBH");
	parm.put("status", "0");
	parm.put("id", parm.get("invoice_id"));
	Map<String, Object> mm = InvoiceMapper.getById(parm);
	     Map<String, Object> invoice=new HashMap<String,Object>();
		invoice.putAll(mm);
		final String invoice_id=MapUtil.getString(mm, "ID");
		List<Map<String, Object>> invoice_items=InvoiceDetailMapper.list(new HashMap<String,Object>(){
		    {
			put("invoice_id", invoice_id);
		    }
		});
		
		invoice.put("data", invoice_items);
		invoice.put("access_token", commonInfoMapper.getInfo(new HashMap<String,Object>(){
		    {
			put("nsrsbh", nsrsbh);
		    }
		}).get("ACCESS_TOKEN"));
		//调用开票接口
		Map<String, Object> invoice_return = EInvoiceDataHandle.newBlueInvoiceBuild(invoice);
		Map<String, Object> update_map=new HashMap<String,Object>();
		List<Map<String, Object>> data=(List<Map<String, Object>>) invoice_return.get("data");
		for (Map<String, Object> mp : data) {
		    update_map.put("id", mp.get("order_num"));
		    update_map.put("fpdm", mp.get("fp_dm"));
		    update_map.put("fphm", mp.get("fp_hm"));
		    update_map.put("skm", mp.get("fp_mw"));
		    update_map.put("jym", mp.get("jym"));
		    update_map.put("kprq", mp.get("kprq"));
		    update_map.put("status", 1);
		    Map<String, Object> url_parm = new HashMap<String,Object>();
		    url_parm.put("nsrsbh", nsrsbh);
		    url_parm.put("order_num", mp.get("order_num"));
		    update_map.put("url", EInvoiceUtil.getEIURL(url_parm));
		}
		InvoiceMapper.update(update_map);
    }
    
    @Override
    public Map<String, Object> doInvoiceByOne(Map<String, Object> parm) {
	Map<String, Object> return_map = new HashMap<String,Object>(); 
	parm.put("status", "0");
	String invoice_id=MapUtil.getString(parm, "id");
	if("".equals(invoice_id)){
	    throw new BizException("id is null");
	}
	parm.put("id", invoice_id);
	parm.put("invoice_id", invoice_id);
	Map<String, Object> mm = InvoiceMapper.getById(parm);
	String invoice_no = MapUtil.getString(mm, "INVOICE_NO");
	try {
	    operate(parm);
	} catch (BizException ex) {
	    throw new BizException(ex.getMessage());
	}catch (Exception e) {
	    throw new BizException(e.getMessage());
	}
	//判断是否所有失败票都已开具成功
	List<Map<String, Object>> invoice_list = InvoiceMapper.getByInvoice_no(parm);
	String status = "1";
	for(Map<String, Object> mp:invoice_list){
	    if(MapUtil.getString(mp, "STATUS").equals("0")){
		status="0";
		break;
	    }
	}
	if("1".equals(status)){
	    Map<String, Object> _map = new HashMap<String,Object>();
	    _map.put("invoice_no", invoice_no);
	    _map.put("status", "1");
	    _map.put("fplxdm", "026");
	    orig_mapper.update(_map);
	}
	return_map.put("status", status);
	return_map.put("order_no", MapUtil.getString(mm, "ORDER_NO"));
	return_map.put("invoice_no", invoice_no);
	return return_map;
    }
    @Override
    public void doRedInvoice(Map<String, Object> parm) {
	
	
    }
    @Override
    public List<Map<String, Object>> getInvoiceList(Map<String, Object> parm) {
	return InvoiceMapper.getByInvoice_no(parm);
    }
    public String changeDate(String str) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	Date date = new Date();
	try {
	    date = sdf.parse(str);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

}
