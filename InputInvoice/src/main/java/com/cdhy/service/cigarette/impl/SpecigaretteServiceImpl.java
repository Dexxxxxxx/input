package com.cdhy.service.cigarette.impl;

import java.util.ArrayList;
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
import com.cdhy.entity.Page;
import com.cdhy.entity.Result;
import com.cdhy.exception.BizException;
import com.cdhy.service.cigarette.ISpeCigaretteService;
import com.cdhy.util.BuildInvoiceData;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.HttpClientUtils;
import com.cdhy.util.MapUtil;
import com.cdhy.util.MessageUtil;
import com.cdhy.util.PropertiesUtil;
import com.cdhy.util.XMLUtil_TaxServer2;

@Service
public class SpecigaretteServiceImpl implements ISpeCigaretteService {
    	@Autowired
    	private CigarInvoiceMapper invoice_mapper;
    	@Autowired
    	private CigarInvoiceDetailMapper invoiceDetail_mapper;
    	@Autowired
    	private CigarOrigMapper orig_mapper;
    	private static final String REQUEST_URL = PropertiesUtil.getInstance().getProperites("request_url");
	@Override
	public Page list(Map<String, Object> parm) {
	    	String status = MapUtil.getString(parm, "invoice_status");
		parm.put("type", 1);
		parm.put("status", status);
		Object start = parm.get("start");
		Object limit = parm.get("limit");
		Page page = new Page(start, limit);
		parm.put("start", page.getStart());
		parm.put("end", page.getEnd());
		int size = orig_mapper.listCount(parm);
		page.setRowsCount(size);
		if (size > 0) {
			List<Map<String, Object>> list = orig_mapper.list(parm);
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
		    saveInfo(bulid_list,order_no);
		    Map<String, Object> _map = new HashMap<String,Object>();
		    _map.put("invoice_no", ids[i]);
		    _map.put("status", "0");
		    List<Map<String, Object>> invoice_list= invoice_mapper.getByInvoice_no(_map);
		    for(Map<String, Object> mp: invoice_list){
			String invoice_id = MapUtil.getString(mp, "ID");
			parm.put("invoice_id", invoice_id);
			try {
			    // 2、执行开票
			    operate(parm);
			    // 1、修改源数据状态
			    parm.put("status", "1");// 开票成功
			    parm.put("fplxdm", "004");
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
		    parm.put("fplxdm", "004");
		    orig_mapper.update(parm);
		}else{
		    //2、删除该订单号的发票的发票数据和发票详情
		    List<Map<String, Object>> invoice_list = invoice_mapper.getByInvoice_no(parm);
		    //删除发票明细
		    for(Map<String, Object> map:invoice_list){
			map.put("invoice_id", map.get("ID"));
			invoiceDetail_mapper.delete(parm);
		    }
		    //删除发票
		    invoice_mapper.delete(parm);
		}
	    }

	@SuppressWarnings("unchecked")
	public void saveInfo(List<Map<String, Object>> list, String order_no) {
		Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
			.getAttribute(LoginController.LOGIN_USER);
		Map<String, Object> user_info = (Map<String, Object>) loginMap.get("user");
		Map<String, Object> dept = (Map<String, Object>) loginMap.get("dept");

		// 正式环境
		String xhdwmc= MapUtil.getString(dept, "NSRNAME");
		String xhdwsbh = MapUtil.getString(dept, "NSRSBH");
		String xhdwdzdh = MapUtil.getString(dept, "DZDH");
		String xhdwyhzh = MapUtil.getString(dept, "YHZH");
		for(Map<String, Object> map:list){
		    Map<String, Object> mm = new HashMap<String, Object>();
		    List<Map<String, Object>> detail = (List<Map<String, Object>>) map.get("data");
		    mm.put("order_no", order_no);
		    mm.put("invoice_no", MapUtil.getString(map, "INVOICE_NO"));
		    mm.put("tspz", "00");
		    mm.put("xhdwsbh", xhdwsbh);
		    mm.put("xhdwmc", xhdwmc);
		    mm.put("xhdwdzdh", xhdwdzdh);
		    mm.put("xhdwyhzh", xhdwyhzh);
		    mm.put("ghdwdm", MapUtil.getString(map, "GHDWDM"));
		    mm.put("ghdwsbh", MapUtil.getString(map, "GHDWNSRSBH"));
		    mm.put("ghdwmc", MapUtil.getString(map, "GHDWMC"));
		    mm.put("ghdwdzdh", MapUtil.getString(map, "GHDWDZDH"));
		    mm.put("ghdwyhzh", MapUtil.getString(map, "GHDWYHZH"));
		    mm.put("zsfs", "0");// 普通征收
		    mm.put("kplx", "0");//正数发票
		    mm.put("hjje", MapUtil.getString(map, "JE_COUNT"));
		    mm.put("hjse", MapUtil.getString(map, "SE_COUNT"));
		    mm.put("jshj", MapUtil.getString(map, "JSHJ_COUNT"));
		    mm.put("kce", "");// 差额征收
		    mm.put("bz", "");
		    mm.put("skr", "");
		    mm.put("fhr", "");
		    mm.put("kpr", user_info.get("NAME"));
		    mm.put("deptid", dept.get("ID"));
		    if(detail.size()>8){
			mm.put("qdbz", "1");
		    }else{
			mm.put("qdbz", "0");
		    }
		    mm.put("ssyf", "");
		    mm.put("yfpdm", "");
		    mm.put("yfphm", "");
		    mm.put("zfrq", "");
		    mm.put("zfr", "");
		    mm.put("type", "004");
		    mm.put("status", "0");
		    invoice_mapper.save(mm);
		    String invoice_id = mm.get("id").toString();
		    int xh = 0;
		    for (Map<String, Object> _map : detail) {
			xh++;
			Map<String, Object> _mm = new HashMap<String, Object>();
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
			invoiceDetail_mapper.insert(_mm);
		    }
		}
	    }
	@SuppressWarnings("unchecked")
	public void operate(Map<String, Object> parm) throws Exception {
		Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
			.getAttribute(LoginController.LOGIN_USER);
//		Map<String, Object> user_info = (Map<String, Object>) loginMap.get("user");
		final Map<String, Object> dept = (Map<String, Object>)loginMap.get("dept");
		if(MapUtil.getString(dept, "KPZDBS").equals("")){
		    throw new BizException("开票终端标识为空，请配置！");
		}
        	parm.put("status", "0");
        	parm.put("id", parm.get("invoice_id"));
        	Map<String, Object> mm = invoice_mapper.getById(parm);
        	Map<String, Object> invoice = new HashMap<String, Object>();
        	invoice.put("kpzdbs", MapUtil.getString(dept, "KPZDBS"));
        	invoice.putAll(mm);
        	final String invoice_id = MapUtil.getString(mm, "ID");
        	List<Map<String, Object>> invoice_detail = invoiceDetail_mapper.list(new HashMap<String, Object>() {
        	    {
        		put("invoice_id", invoice_id);
        	    }
        	});
        	Map<String, Object> xml_map = XMLUtil_TaxServer2.createSpecialXmlWithMap(invoice, invoice_detail);
        	String return_xml = HttpClientUtils.postMethodByXml(MapUtil.getString(xml_map, "xml"), REQUEST_URL);
        	parm.put("xml", return_xml);
        	parm.put("invoice_id", invoice_id);
        	saveInvoice(parm);
	    }
	@Override
	public List<Map<String, Object>> getInvoice(Map<String, Object> parm) {
	    Object obj = parm.get("ids");
	    if (obj == null || obj == "") {
        	 throw new BizException("选择为空");
	    }
	    String ids[] = obj.toString().split(",");
	    parm.put("array", ids);
	    //返回的list
	    List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	    for(int i=0;i<ids.length;i++){
		parm.put("invoice_no", ids[i]);
		 //获取发票公共信息
		 Map<String, Object> comm_map=orig_mapper.getInvoiceCommonInfo(parm);
		 //获取拆分后的商品信息
		 List<Map<String, Object>> list_item=BuildInvoiceData.splitInvoice(orig_mapper.getById(parm));
		 //生成发票的票面信息
		 List<Map<String, Object>> bulid_list=BuildInvoiceData.buildInvoice(list_item,comm_map);
		 list.addAll(bulid_list);
	    }
	    return list;
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
		Map<String, Object> mm = invoice_mapper.getById(parm);
		String invoice_no = MapUtil.getString(mm, "INVOICE_NO");
		try {
		    operate(parm);
		} catch (BizException ex) {
		    throw new BizException(ex.getMessage());
		}catch (Exception e) {
		    throw new BizException(e.getMessage());
		}
		//判断是否所有失败票都已开具成功
		List<Map<String, Object>> invoice_list = invoice_mapper.getByInvoice_no(parm);
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
		    _map.put("fplxdm", "004");
		    orig_mapper.update(_map);
		}
		return_map.put("status", status);
		return_map.put("order_no", MapUtil.getString(mm, "ORDER_NO"));
		return_map.put("invoice_no", invoice_no);
		return return_map;
	    }

	@Override
	public Map<String, Object> redoInvoice(Map<String, Object> parm) {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public Map<String, Object> saveInvoice(Map<String, Object> parm) throws Exception {
	    String invoice_id = MapUtil.getString(parm, "invoice_id");
		String xml = MapUtil.getString(parm, "xml");
		Map<String, Object> resp_result = MessageUtil.parseXmlFromString(xml);
		resp_result = MapUtil.mapKey2Lower(resp_result);
		String returncode = MapUtil.getString(resp_result, "returncode");
		String returnmsg = MapUtil.getString(resp_result, "returnmsg");
		if (!returncode.equals("0")) {
		    if(returnmsg.equals("")){
			 throw new BizException("开票失败");
		    }else{
			throw new BizException(returnmsg);
		    }
		}
		String fphm = MapUtil.getString(resp_result, "fphm");
		String fpdm = MapUtil.getString(resp_result, "fpdm");
		String skm = MapUtil.getString(resp_result, "skm");
		String jym = MapUtil.getString(resp_result, "jym");
		String kprq = MapUtil.getString(resp_result, "kprq");
		skm = skm.replaceAll("&gt;", ">");
		skm = skm.replaceAll("&lt;", "<");
		Map<String, Object> update_map = new HashMap<String, Object>();
		update_map.put("id", invoice_id);
		update_map.put("fphm", fphm);
		update_map.put("fpdm", fpdm);
		update_map.put("jym", jym);
		update_map.put("skm", skm);
		update_map.put("kprq", kprq);
		update_map.put("status", "1");
		invoice_mapper.update(update_map);
		Map<String, Object> result_map=new HashMap<>();
		result_map.put("fphm", fphm);
		result_map.put("fpdm", fpdm);
		return result_map;
	}

	@Override
	public Result saveBillsToDB(String filePath) {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public List<Map<String, Object>> getInvoiceList(Map<String, Object> parm) {
	    return invoice_mapper.getByInvoice_no(parm);
	}

	

}
