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

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.cigarette.CigarInvoiceDetailMapper;
import com.cdhy.dao.cigarette.CigarInvoiceMapper;
import com.cdhy.dao.cigarette.CigarOrigMapper;
import com.cdhy.dao.security.CommInfoMapper;
import com.cdhy.entity.Page;
import com.cdhy.exception.BizException;
import com.cdhy.service.cigarette.ICigarInvoiceService;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.EInvoiceDataHandle;
import com.cdhy.util.EInvoiceUtil;
import com.cdhy.util.HttpClientUtils;
import com.cdhy.util.MapUtil;
import com.cdhy.util.PropertiesUtil;
import com.cdhy.util.XMLUtil_TaxServer;
import com.cdhy.util.XmlResult;

@Service
public class CigarInvoiceServiceImpl implements ICigarInvoiceService {

	@Autowired
	private CigarInvoiceMapper cigarInvoiceMapper;
	@Autowired
	private CigarOrigMapper cigarOrigMapper;
	@Autowired
	private CigarInvoiceDetailMapper cigarInvoiceDetailMapper;
	@Autowired
	private CommInfoMapper commMapper;
	private static final String REQUEST_URL = PropertiesUtil.getInstance().getProperites("request_url");
	@Override
	public Page list(Map<String, Object> parm) {
	    	Object start = parm.get("start");
		Object limit = parm.get("limit");
		Page page = new Page(start, limit);
		parm.put("start", page.getStart());
		parm.put("end", page.getEnd());
		parm.put("start_date", MapUtil.getString(parm, "start_date").replace("-", ""));
		parm.put("end_date", MapUtil.getString(parm, "end_date").replace("-", ""));
		int size = cigarInvoiceMapper.listCount(parm);
		page.setRowsCount(size);
		if (size > 0) {
			List<Map<String, Object>> list = cigarInvoiceMapper.list(parm);
			List<Map<String, Object>> _list = new ArrayList<Map<String,Object>>();
			for(Map<String, Object> map:list){
		        	map.put("KPRQ", changeDate(MapUtil.getString(map, "KPRQ")));
		        	_list.add(map);
		        }
			page.setData(_list);
		}
		return page;
	}
	
	@Override
	public List<Map<String, Object>> listInvoice(Map<String, Object> parm) {
	   //根据订单号或批次号获取
		return null;
	}
	
	@Override
	public List<Map<String, Object>> listInvoiceDetail(Map<String, Object> parm) {
	    return cigarInvoiceDetailMapper.list(parm);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void cancel(Map<String, Object> parm) {
		Map<String, Object> result = (Map<String, Object>) ContextHolderUtils.getSession()
			.getAttribute(LoginController.LOGIN_USER);
		Map<String, Object> dept = (Map<String, Object>) result.get("dept");
		Map<String, Object> user = (Map<String, Object>) result.get("user");
		Object obj = parm.get("ids");//发票号码集合
		if (obj == null||obj=="") {
		    throw new BizException("选择为空");
		}
		String ids[] = obj.toString().split(",");
		for(int i=0;i<ids.length;i++){
		    parm.put("id", ids[i]);
		    Map<String, Object> invoice = cigarInvoiceMapper.getById(parm);
		    Map<String, Object> map = new HashMap<String, Object>();
		    map.put("kpzdbs", MapUtil.getString(dept, "KPZDBS"));
		    map.put("fplxdm", MapUtil.getString(invoice, "TYPE"));
		    map.put("fpdm", MapUtil.getString(invoice, "FPDM"));
		    map.put("fphm", MapUtil.getString(invoice, "FPHM"));
		    map.put("hjje", MapUtil.getString(invoice, "HJJE"));
		    map.put("zfr", MapUtil.getString(user, "USERNAME"));
		    String xml = XMLUtil_TaxServer.createCancelXml(map);
		    String return_xml=HttpClientUtils.postMethodByXml(xml, REQUEST_URL);
		    Map<String, Object> cancel_map=new HashMap<String,Object>();
		    cancel_map.put("xml", return_xml);
		    Map<String, Object> cancelCheck_map=XmlResult.check(cancel_map);
			String returncode=MapUtil.getString(cancelCheck_map, "returncode");
			String returnmsg=MapUtil.getString(cancelCheck_map, "returnmsg");
			String fphm=MapUtil.getString(cancelCheck_map, "fphm");
			String fpdm=MapUtil.getString(cancelCheck_map, "fpdm");
			if (!returncode.equals("0")) {
			    if(returnmsg.equals("")){
				throw new BizException("作废操作失败");
			    }else{
				throw new BizException(returnmsg);
			    }
			}
			//作废成功 修改发票状态（-1）
			Map<String, Object> mp =new HashMap<String,Object>();
			mp.put("status", "-1");
			mp.put("fpdm", fpdm);
			mp.put("fphm", fphm);
			cigarInvoiceMapper.updateCancel(mp);
			//获取该发票
			String invoice_no=MapUtil.getString(cigarInvoiceMapper.getByDmHm(mp), "INVOICE_NO");
			//根据该订单号查看所有发票
			mp.clear();
			mp.put("invoice_no", invoice_no);
			List<Map<String, Object>> invoice_list = cigarInvoiceMapper.getByInvoice_no(mp);
			int flag=0;
			for(Map<String, Object> mm:invoice_list){
			    if(!MapUtil.getString(mm, "STATUS").equals("-1")){
				flag=1;
			    }
			}
			//回滚
			if(flag==0){
			    mp.put("status", "0");
			    mp.put("fplxdm", "");
			    cigarOrigMapper.update(mp);
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void redCancel(Map<String, Object> parm) {
	    //专普票冲红（同步税控服务器）
	    String fpdm = MapUtil.getString(parm, "fpdm");//红票发票代码
	    String fphm = MapUtil.getString(parm, "fphm");//红票发票号码
	    if("".equals(MapUtil.getString(parm, "id"))){
		throw new BizException("id is null");
	    }
	    Map<String, Object> invoice = cigarInvoiceMapper.getById(parm);
	    Map<String, Object> result = (Map<String, Object>) ContextHolderUtils.getSession()
			.getAttribute(LoginController.LOGIN_USER);
		Map<String, Object> dept = (Map<String, Object>) result.get("dept");
	    Map<String, Object> map= new HashMap<String,Object>();
	    map.put("kpzdbs", MapUtil.getString(dept, "KPZDBS"));
	    map.put("fplxdm", MapUtil.getString(invoice, "TYPE"));
	    map.put("fpdm", MapUtil.getString(parm, "fpdm"));
	    map.put("fphm", MapUtil.getString(parm, "fphm"));
	    if(cigarInvoiceMapper.getByDmHm(map)!=null){
		throw new BizException("该发票已同步红字发票");
	    }
	    String xml = XMLUtil_TaxServer.createQueryInvoiceXml(map);
	    String return_xml=HttpClientUtils.postMethodByXml(xml, REQUEST_URL);
	    Map<String, Object> invoice_return = new HashMap<String,Object>();
	    try {
		invoice_return = XmlResult.parseInvoiceXmlFrom(return_xml);
		String yfpdm=MapUtil.getString(invoice_return, "yfpdm");
		String yfphm=MapUtil.getString(invoice_return, "yfphm");
		if(!yfpdm.equals(MapUtil.getString(invoice, "FPDM"))||!yfphm.equals(MapUtil.getString(invoice, "FPHM"))){
		    throw new BizException("输入的红字发票信息与原发票不同步");
		}
		invoice_return.put("invoice_no", MapUtil.getString(invoice, "INVOICE_NO"));
		invoice_return.put("ghdwdm", MapUtil.getString(invoice, "GHDWDM"));
		invoice_return.put("type", MapUtil.getString(invoice, "TYPE"));
		saveRedInfo(invoice_return);
	    } catch (Exception e) {
		e.printStackTrace();
		throw new BizException(e.getMessage());
	    }
	    
	}

    @SuppressWarnings({ "unchecked", "serial" })
    @Override
    public void e_redCancel(Map<String, Object> parm) {
	// 电票冲红
	Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
			.getAttribute(LoginController.LOGIN_USER);
	final Map<String, Object> dept = (Map<String, Object>)loginMap.get("dept");
		//正式环境
	final String nsrsbh = MapUtil.getString(dept, "NSRSBH");
	Map<String, Object> invoice = cigarInvoiceMapper.getById(parm);
	Map<String, Object> redInvoice = new HashMap<String, Object>();
	redInvoice.put("access_token", commMapper.getInfo(new HashMap<String, Object>() {
	    {
		put("nsrsbh", nsrsbh);
	    }
	}).get("ACCESS_TOKEN"));
	redInvoice.put("nsrsbh", MapUtil.getString(invoice, "XHDWSBH"));
	redInvoice.put("order_num", MapUtil.getString(invoice, "ID") + "RED");// 变成红票的ID
	redInvoice.put("fpdm", MapUtil.getString(invoice, "FPDM"));
	redInvoice.put("fphm", MapUtil.getString(invoice, "FPHM"));
	redInvoice.put("bz", "");
	Map<String, Object> redInvoice_return = new HashMap<String, Object>();
	try {
	    redInvoice_return = EInvoiceDataHandle.newRedInvoiceBuild(redInvoice);
	    // 新增红票
	    redInvoice_return.put("invoice_no", MapUtil.getString(invoice, "INVOICE_NO"));
	    redInvoice_return.put("ghdwdm", MapUtil.getString(invoice, "GHDWDM"));
	    saveERedInfo(redInvoice_return);
	} catch (BizException e) {
	    throw new BizException(e.getMessage());
	} catch (Exception e) {
	    throw new BizException(e.getMessage());
	}

    }
    @SuppressWarnings("unchecked")
    public void saveERedInfo(Map<String, Object> redInvoice_return){
	String invoice_no=MapUtil.getString(redInvoice_return, "invoice_no");
	String ghdwdm=MapUtil.getString(redInvoice_return, "ghdwdm");
	List<Map<String, Object>> data = (List<Map<String, Object>>) redInvoice_return.get("data");
	// 获取登录信息
	Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
		.getAttribute(LoginController.LOGIN_USER);
	// 登录用户信息
	Map<String, Object> user = (Map<String, Object>) loginMap.get("user");
	Map<String, Object> dept = (Map<String, Object>) loginMap.get("dept");
	for (Map<String, Object> mm : data) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    String invoice_id = MapUtil.getString(mm, "order_num");
	    map.put("id", invoice_id);
	    map.put("invoice_no", invoice_no);
	    map.put("order_no", "");
	    map.put("fpzt", "");
	    map.put("scbz", "");
	    map.put("jqbh", "");
	    map.put("tspz", "");
	    map.put("xhdwsbh", MapUtil.getString(mm,"xsf_nsrsbh"));
	    map.put("xhdwmc", MapUtil.getString(mm,"xsf_mc"));
	    map.put("xhdwdzdh", MapUtil.getString(mm,"xsf_dzdh"));
	    map.put("xhdwyhzh", MapUtil.getString(mm,"xsf_yhzh"));
	    map.put("ghdwdm", ghdwdm);
	    map.put("ghdwsbh", MapUtil.getString(mm,"gmf_nsrsbh"));
	    map.put("ghdwmc", MapUtil.getString(mm,"gmf_mc"));
	    map.put("ghdwdzdh", MapUtil.getString(mm,"gmf_dzdh"));
	    map.put("ghdwyhzh", MapUtil.getString(mm,"gmf_yhzh"));

	    map.put("fpdm", MapUtil.getString(mm,"fp_dm"));
	    map.put("fphm", MapUtil.getString(mm,"fp_hm"));
	    map.put("yfpdm", MapUtil.getString(mm,"yfp_dm"));
	    map.put("yfphm", MapUtil.getString(mm,"yfp_hm"));
	    map.put("kprq", MapUtil.getString(mm, "kprq"));
	    map.put("skm", MapUtil.getString(mm,"fp_mw"));
	    map.put("jym", MapUtil.getString(mm, "jym"));
	    
	    map.put("kplx", "1");//负数票
	    map.put("zsfs", MapUtil.getString(mm, "zsfs"));
	    map.put("hjje", MapUtil.getString(mm,"hjje"));
	    map.put("hjse", MapUtil.getString(mm,"hjse"));
	    map.put("jshj", MapUtil.getString(mm,"jshj"));
	    map.put("kce", "");
	    map.put("bz", MapUtil.getString(mm, "bz"));
	    map.put("skr", MapUtil.getString(mm, "skr"));
	    map.put("fhr", MapUtil.getString(mm, "fhr"));
	    map.put("kpr", MapUtil.getString(mm,"kpr"));
	    map.put("deptid", MapUtil.getString(dept, "ID"));
	    map.put("qdbz", "0");
	    map.put("ssyf", "");
	    map.put("zfrq", "");
	    map.put("zfr", "");
	    map.put("type", "026");
	    map.put("status", "1");
	    map.put("dystatus", "");
	    Map<String, Object> mp= new HashMap<String,Object>();
	    mp.put("nsrsbh", map.get("xhdwsbh"));
	    mp.put("order_num", map.get("id"));
	    String url = EInvoiceUtil.getEIURL(mp);
	    map.put("url", url);
	    map.put("hpurl", "");
	    //保存红票
	    List<Map<String, Object>> item_list=(List<Map<String, Object>>) mm.get("common_fpkj_xmxx");
	    if(item_list.size()>8){
		map.put("qdbz", "1");
	    }
	    cigarInvoiceMapper.saveRed(map);
	    int xh=0;
	    for(Map<String, Object> item:item_list){
		xh++;
		Map<String, Object> item_map=new HashMap<String,Object>();
		item_map.put("invoice_id", map.get("id"));
		item_map.put("xh", xh);
		item_map.put("fphxz", MapUtil.getString(item, "fphxz"));
		item_map.put("spmc", MapUtil.getString(item, "xmmc"));
		item_map.put("spsm", MapUtil.getString(item, "spsm"));
		item_map.put("ggxh", MapUtil.getString(item, "ggxh"));
		item_map.put("dw", MapUtil.getString(item, "dw"));
		item_map.put("spsl", MapUtil.getString(item, "xmsl"));
		item_map.put("dj", MapUtil.getString(item, "xmdj"));
		item_map.put("je", String.format("%.2f",MapUtil.getDouble(item, "xmje")-MapUtil.getDouble(item, "se")));
		item_map.put("jshj", MapUtil.getString(item, "xmje"));
		item_map.put("sl", MapUtil.getString(item, "sl"));
		item_map.put("se", MapUtil.getString(item, "se"));
		item_map.put("hsbz", "1");
		
		item_map.put("spbm", "");
		item_map.put("zxbm", "");
		item_map.put("yhzcbs","");
		item_map.put("lslbs", MapUtil.getString(item, "lslbs"));
		item_map.put("zzstsgl","");
		cigarInvoiceDetailMapper.insert(item_map);
	    }
	}
    }
    @SuppressWarnings("unchecked")
    public void saveRedInfo(Map<String, Object> redInvoice_return){
	String invoice_no=MapUtil.getString(redInvoice_return, "invoice_no");
	String ghdwdm=MapUtil.getString(redInvoice_return, "ghdwdm");
	List<Map<String, Object>> data = (List<Map<String, Object>>) redInvoice_return.get("invoice_detail");
	// 获取登录信息
	Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
		.getAttribute(LoginController.LOGIN_USER);
	// 登录用户信息
	Map<String, Object> user = (Map<String, Object>) loginMap.get("user");
	Map<String, Object> dept = (Map<String, Object>) loginMap.get("dept");
	for (Map<String, Object> mm : data) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    String invoice_id = MapUtil.getString(mm, "order_num");
	    map.put("id", invoice_id);
	    map.put("invoice_no", invoice_no);
	    map.put("order_no", "");
	    map.put("fpzt", MapUtil.getString(redInvoice_return, "fpzt"));
	    map.put("scbz", MapUtil.getString(redInvoice_return, "scbz"));
	    map.put("jqbh", MapUtil.getString(redInvoice_return, "jqbh"));
	    map.put("tspz", MapUtil.getString(redInvoice_return, "tsbz"));
	    map.put("xhdwsbh", MapUtil.getString(redInvoice_return,"xhdwsbh"));
	    map.put("xhdwmc", MapUtil.getString(redInvoice_return,"xhdwmc"));
	    map.put("xhdwdzdh", MapUtil.getString(redInvoice_return,"xhdwdzdh"));
	    map.put("xhdwyhzh", MapUtil.getString(redInvoice_return,"xhdwyhzh"));
	    map.put("ghdwdm", ghdwdm);
	    map.put("ghdwsbh", MapUtil.getString(redInvoice_return,"ghdwsbh"));
	    map.put("ghdwmc", MapUtil.getString(redInvoice_return,"ghdwmc"));
	    map.put("ghdwdzdh", MapUtil.getString(redInvoice_return,"ghdwdzdh"));
	    map.put("ghdwyhzh", MapUtil.getString(redInvoice_return,"ghdwyhzh"));

	    map.put("fpdm", MapUtil.getString(redInvoice_return,"fpdm"));
	    map.put("fphm", MapUtil.getString(redInvoice_return,"fphm"));
	    map.put("yfpdm", MapUtil.getString(redInvoice_return,"yfpdm"));
	    map.put("yfphm", MapUtil.getString(redInvoice_return,"yfphm"));
	    map.put("kprq", MapUtil.getString(redInvoice_return,"kprq"));
	    map.put("skm", MapUtil.getString(redInvoice_return,"skm"));
	    map.put("jym", MapUtil.getString(redInvoice_return,"jym"));
	    
	    map.put("kplx", "1");//负数票
	    map.put("zsfs", MapUtil.getString(redInvoice_return,"zsfs"));
	    map.put("hjje", MapUtil.getString(redInvoice_return,"hjje"));
	    map.put("hjse", MapUtil.getString(redInvoice_return,"hjse"));
	    map.put("jshj", MapUtil.getString(redInvoice_return,"jshj"));
	    map.put("kce", "");
	    map.put("bz", MapUtil.getString(redInvoice_return,"bz"));
	    map.put("skr", MapUtil.getString(redInvoice_return, "skr"));
	    map.put("fhr", MapUtil.getString(redInvoice_return, "fhr"));
	    map.put("kpr", MapUtil.getString(redInvoice_return,"kpr"));
	    map.put("deptid", MapUtil.getString(dept, "ID"));
	    map.put("qdbz", MapUtil.getString(redInvoice_return,"qdbz"));
	    map.put("ssyf", MapUtil.getString(redInvoice_return,"ssyf"));
	    map.put("zfrq", MapUtil.getString(redInvoice_return,"zfrq"));
	    map.put("zfr", MapUtil.getString(redInvoice_return,"zfr"));
	    map.put("type", MapUtil.getString(redInvoice_return,"type"));
	    map.put("status", "1");
	    map.put("dystatus", "");
	    map.put("url", "");
	    map.put("hpurl", "");
	    //保存红票
	    cigarInvoiceMapper.saveRed(map);
	    for(Map<String, Object> item:data){
		Map<String, Object> item_map=new HashMap<String,Object>();
		item_map.put("invoice_id", map.get("id"));
		item_map.put("xh", MapUtil.getString(item, "xh"));
		item_map.put("fphxz", MapUtil.getString(item, "fphxz"));
		item_map.put("spmc", MapUtil.getString(item, "spmc"));
		item_map.put("spsm", MapUtil.getString(item, "spsm"));
		item_map.put("ggxh", MapUtil.getString(item, "ggxh"));
		item_map.put("dw", MapUtil.getString(item, "dw"));
		item_map.put("spsl", MapUtil.getString(item, "spsl"));
		item_map.put("dj", MapUtil.getString(item, "dj"));
		item_map.put("je", String.format("%.2f",MapUtil.getDouble(item, "je")-MapUtil.getDouble(item, "se")));
		item_map.put("jshj", MapUtil.getString(item, "je"));//价税合计
		item_map.put("sl", MapUtil.getString(item, "sl"));
		item_map.put("se", MapUtil.getString(item, "se"));
		item_map.put("hsbz", MapUtil.getString(item, "hsbz"));
		item_map.put("spbm", MapUtil.getString(item, "spbm"));
		item_map.put("zxbm", MapUtil.getString(item, "zxbm"));
		item_map.put("yhzcbs",MapUtil.getString(item, "yhzcbs"));
		item_map.put("lslbs", MapUtil.getString(item, "lslbs"));
		item_map.put("zzstsgl",MapUtil.getString(item, "zzstsgl"));
		cigarInvoiceDetailMapper.insert(item_map);
	    }
	}
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

    @Override
    public void rollback(Map<String, Object> parm) {
	//根据id查看订单号
	String id=MapUtil.getString(parm, "id");
	if("".equals(id)){
	    throw new BizException("id is null");
	}
	Map<String, Object> invoice = cigarInvoiceMapper.getById(parm);
	String invoice_no = MapUtil.getString(invoice, "INVOICE_NO");
	Map<String, Object> map = new HashMap<String,Object>();
	map.put("invoice_no", invoice_no);
	List<Map<String, Object>> invoice_list = cigarInvoiceMapper.getByInvoice_no(map);
	int blue=0;
	int red=0;
	for(Map<String, Object> mm :invoice_list){
	    if("0".equals(MapUtil.getString(mm, "KPLX"))){
		blue++;
	    }else if("1".equals(MapUtil.getString(mm, "KPLX"))){
		red++;
	    }
	}
	if(blue!=red){
	    throw new BizException("该单号未全部冲红，不能回滚");
	}
	
    }



}
