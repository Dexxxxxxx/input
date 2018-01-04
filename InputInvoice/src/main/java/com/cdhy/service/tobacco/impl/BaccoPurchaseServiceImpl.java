package com.cdhy.service.tobacco.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.tobacco.BaccoInvoiceMapper;
import com.cdhy.dao.tobacco.BaccoMessageMapper;
import com.cdhy.dao.tobacco.BaccoOrigMapper;
import com.cdhy.dao.tobacco.BaccoPurchaseMapper;
import com.cdhy.entity.SpInfo;
import com.cdhy.exception.BizException;
import com.cdhy.service.tobacco.IBaccoPurchaseService;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.MapUtil;
import com.cdhy.util.MessageUtil;
import com.cdhy.util.XMLUtil_TaxServer;

@Service
public class BaccoPurchaseServiceImpl implements IBaccoPurchaseService {
    @Autowired
    private BaccoPurchaseMapper mapper;
    @Autowired
    private BaccoMessageMapper msgmapper;
    @Autowired
    private BaccoOrigMapper orig_mapper;
    @Autowired
    private BaccoInvoiceMapper invoice_mapper;
    
    
    @Override
    @Transactional(propagation=Propagation.NESTED )
    public List<Map<String, Object>> doInvoice(Map<String, Object> parm) {
	parm.put("suffix", GetSuffix.suffixCheck());
	Object obj = parm.get("ids");
	String id_card = MapUtil.getString(parm, "id_card");
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
	if(id_card.equals("")){
	    throw new BizException("传入参数错误");
	}
	//获取农户信息
	Map<String, Object> farmer_info=orig_mapper.getCommonInfo(parm);
	//获取待开票信息
	List<Map<String, Object>> list=orig_mapper.getItems(parm);
	//存入中间表
	orig_mapper.insertOrigToInvoice(parm);
	String jylsh=parm.get("id").toString();
	parm.put("jylsh", jylsh);
	parm.put("status", "1");
	mapper.updateStatus(parm);
	//获取list_xml
	List<Map<String, Object>> list_xml=new ArrayList<>();
	try {
	    list_xml=getInvoiceList(farmer_info,list,jylsh);
	}catch(BizException ex){
//	    orig_mapper.deleteOrigToInvoice(parm);
//	    mapper.updateStatus(parm);
	    revokeTran(parm);
	    throw new BizException(ex.getMessage());
	} catch (Exception e) {
//	    orig_mapper.deleteOrigToInvoice(parm);
//	    mapper.updateStatus(parm);
	    revokeTran(parm);
	    throw new BizException("请求开票有误");
	}
	return list_xml;
    }
    @Transactional(propagation=Propagation.NESTED )
    public void revokeTran(Map<String, Object> parm){
	parm.put("status", "0");
	orig_mapper.deleteOrigToInvoice(parm);
	mapper.updateStatus(parm);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>>  getInvoiceList(Map<String, Object> farmer_info,List<Map<String, Object>> list,String jylsh){
	Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
		.getAttribute(LoginController.LOGIN_USER);
	Map<String, Object> dept = (Map<String, Object>) loginMap.get("dept");
	Map<String, Object> user = (Map<String, Object>) loginMap.get("user");
	String ghf_id=MapUtil.getString(dept, "ID");
	String kpr_id = MapUtil.getString(user, "ID"); // 获取开票人id
	String kpr_name = MapUtil.getString(user, "NAME"); // 获取开票人名称
	String kpzdbs = "";
	if(dept == null){
	    throw new BizException("开票信息不合法");
	}
	kpzdbs = MapUtil.getString(dept, "KPZDBS"); // 获取开票终端标识
	if (kpzdbs.equals("")) {
	    throw new BizException("你所在区域没有配置开票终端标识");
	}
	//测试环境
	kpzdbs = "10001";
	String ghdwsbh = "510302744676556";// 购货单位识别号 烟草公司
	String ghdwmc = "自贡市商业银行股份有限公司";// 购货单位名称 烟草公司
	String ghdwdzdh = "四川省自贡市";// 购货单位地址电话 烟草公司
	String ghdwyhzh = "6222220202"; // 购货单位银行账号 烟草公司
	//正式环境
//	kpzdbs = MapUtil.getString(dept, "KPZDBS");
//	String ghdwsbh = MapUtil.getString(dept, "NSRSBH");// 购货单位识别号 烟草公司
//	String ghdwmc = MapUtil.getString(dept, "NSRNAME");// 购货单位名称 烟草公司
//	String ghdwdzdh = MapUtil.getString(dept, "DZDH");// 购货单位地址电话 烟草公司
//	String ghdwyhzh = MapUtil.getString(dept, "YHZH"); // 购货单位银行账号 烟草公司
	
	String xsf_id=MapUtil.getString(farmer_info, "FARMERID");
	String xhdwsbh=MapUtil.getString(farmer_info, "ID_CARD");
	String xhdwmc = MapUtil.getString(farmer_info, "FARMER_NAME");// 销货单位名称 烟农名称
	String xhdwdzdh = MapUtil.getString(farmer_info, "ADD_NAME");// 销货单位地址电话
	String xhdwyhzh = MapUtil.getString(farmer_info, "BANK_NAME")+ MapUtil.getString(farmer_info, "BANK_ACNT"); // 销货单位银行账号
	if(xsf_id.equals("")){
	    throw new BizException("未找到该农户信息");
	}
	//无收购发票测试环境  正式环境需删除
	xhdwsbh="510302744676556";
	xhdwmc="自贡市商业银行股份有限公司";
	
	
	//所有发票的公共参数
	Map<String, Object> common_param=new HashMap<String,Object>();
	common_param.put("kpzdbs", kpzdbs);
	common_param.put("ghdwsbh", ghdwsbh);
	common_param.put("ghdwmc", ghdwmc);
	common_param.put("ghdwdzdh", ghdwdzdh);
	common_param.put("ghdwyhzh", ghdwyhzh);
	common_param.put("xhdwsbh", xhdwsbh);
	common_param.put("xhdwmc", xhdwmc);
	common_param.put("xhdwdzdh", xhdwdzdh);
	common_param.put("xhdwyhzh", xhdwyhzh);
	common_param.put("fplxdm", "007");
	common_param.put("kplx", "0");
	common_param.put("tspz", "02");
	common_param.put("kpr", kpr_name);//开票人
	common_param.put("kpr_id", kpr_id);//开票人id
	common_param.put("ghf_id", ghf_id);//购货方id
	common_param.put("xsf_id", xsf_id);//销售方id
	common_param.put("xsf_add", xhdwdzdh);//销售方id
	
	common_param.put("zsfs", "0");// 征税方式
	common_param.put("kce", "");// 差额征税扣除额
	common_param.put("skr", "");// 收款人
	common_param.put("fhr", "");// 复核人
	common_param.put("tzdbh", "");// 信息表编号
	common_param.put("yfpdm", "");// 原发票代码
	common_param.put("yfphm", "");// 原发票号码
	common_param.put("deptid", dept.get("ID"));// 所属组织机构id
	
	Map<String, Object> submap = new HashMap<String, Object>();
	String deptPidCode = MapUtil.getString(dept, "CODE").substring(0, 4);
	submap.put("code", deptPidCode);
	submap.put("type", "1");
	//查询补贴
	List<Map<String, Object>> sublist = mapper.getSubInfo(submap);
	if(!checkCombine(list, sublist)){
	    throw new BizException("补贴单价不一致不能进行合并");
	}
	//获取所有的原始数据代开票list
	List<List<SpInfo>> list_listSpInfo=new ArrayList<List<SpInfo>>();
	//获取总重量
	double sum_weight = 0;
	double sum_deduct_amount =0;
	double sum_all_amount=0;
	for(int i=0;i<list.size();){
	    List<SpInfo> list_spinfo=new ArrayList<SpInfo>();
	    for(int s=1;s<=8;){
		if(i>=list.size()){
		    break;
		}
		//1、判断补贴总数    注：该地市含有该补贴但是该农户的此补贴为0
		int sub_count=0;
		for(Map<String, Object> mp:sublist){
		    String sub_name=mp.get("ITEM").toString();
		    if(!list.get(i).get(sub_name).toString().equals("0")){
			sub_count++;
		    }
		}
		//2、判断该商品加入到发票是否超过8条
		if(s+sub_count<=8){
		    //烟草项
		    String spmc=MapUtil.getString(list.get(i), "LEVEL_NAME");
		    Map<String, Object> spmap = getSpInfo(spmc);
		    SpInfo spinfo=new SpInfo();
		    spinfo.setXh(s);
		    spinfo.setSpmc(spmc);
		    spinfo.setSpbm(MapUtil.getString(spmap, "CODE"));
		    spinfo.setZxbm(MapUtil.getString(spmap, "ZXBM"));
		    spinfo.setFphxz("0");
		    spinfo.setDw("千克");
		    spinfo.setSpsl(list.get(i).get("WEIGHT").toString());
		    spinfo.setDj(list.get(i).get("PRICE").toString());
		    double spje=MapUtil.getDouble(list.get(i), "AMOUNT");
		    double tureje=MapUtil.getDouble(list.get(i), "WEIGHT")*MapUtil.getDouble(list.get(i), "PRICE");
		    if(Math.abs(spje-tureje)>0.01){
			throw new BizException(spmc+"的金额误差超过0.01,请减少合并项，或执行单条开票");
		    }
		    if(spje<=0){
			throw new BizException("正数票开具商品金额必须大于零，"+spmc+"的金额为"+spje);
		    }
		    spinfo.setJe(list.get(i).get("AMOUNT").toString());
		    spinfo.setSl("0");
		    spinfo.setSe("0.00");
		    list_spinfo.add(spinfo);
		    sum_weight+=MapUtil.getDouble(list.get(i), "WEIGHT");
		    sum_deduct_amount+=MapUtil.getDouble(list.get(i), "DEDUCT_AMOUNT");
		    sum_all_amount+=MapUtil.getDouble(list.get(i), "AMOUNT"); //未扣款金额
		    s++;
		    //补贴项
		    for(Map<String, Object> mm:sublist){
			//判断补贴单价是否为0
			String sub_name=mm.get("ITEM").toString();
			if(!list.get(i).get(sub_name).toString().equals("0")){
			    String price = mm.get("ITEM").toString();// 补贴单价
			    String subspmc = mm.get("VALUE").toString();//补贴名称
			    Map<String, Object> subspmap = getSpInfo(subspmc);
			    SpInfo sp_sub=new SpInfo();
			    sp_sub.setXh(s);
			    sp_sub.setSpmc(subspmc);
			    sp_sub.setSpbm(MapUtil.getString(subspmap, "CODE"));
			    sp_sub.setZxbm(MapUtil.getString(subspmap, "ZXBM"));
			    sp_sub.setFphxz("0");
			    sp_sub.setDw("千克");
			    sp_sub.setSpsl(list.get(i).get("WEIGHT").toString());
			    sp_sub.setDj(list.get(i).get(price).toString());
			    double je = Double.parseDouble(sp_sub.getSpsl()) * Double.parseDouble(sp_sub.getDj());
			    String sub_amount="SUB_AMOUNT"+sub_name.charAt(sub_name.length()-1);
			    double sub_amountje=MapUtil.getDouble(list.get(i), sub_amount);
			    if(Math.abs(sub_amountje-je)>0.01){
				throw new BizException(spmc+"的"+subspmc+"金额误差超过0.01,请减少合并项，或执行单条开票");
			    }
			    if(je<=0){
				throw new BizException("正数票开具商品金额必须大于零，"+spmc+"的"+subspmc+"金额为"+je);
			    }
			    String countje = new DecimalFormat("0.00").format(sub_amountje);
			    sp_sub.setJe(countje);
			    sp_sub.setSl("0");
			    sp_sub.setSe("0.00");
			    list_spinfo.add(sp_sub);
			    sum_all_amount+=je; //未扣款金额
			    s++;
			}
		    }
		    i++;
		}else{
		    break;
		}
	    }
	    list_listSpInfo.add(list_spinfo);	    
	}
	int fpcount=list_listSpInfo.size();
	StringBuilder bzinfo = new StringBuilder();
	bzinfo.append("本套票详情，总张数:" + fpcount + "张；");
	bzinfo.append("总重量:" + new DecimalFormat("0.00").format(sum_weight) + " 千克；");
	if(GetSuffix.suffixCheck().equals("_SL")){
	    bzinfo.append("代扣物资款:-" + new DecimalFormat("0.00").format(sum_deduct_amount)+"；");
	}else{
	    bzinfo.append("扣款金额:-" + new DecimalFormat("0.00").format(sum_deduct_amount)+"；");
	}
	if(GetSuffix.suffixCheck().equals("_YA")){
	    bzinfo.append("含10%补贴；");
	}
	//查询扣款   备注补充扣款项
	submap.put("type", "-1");
	double sum_deduct_item=0;
	List<Map<String, Object>> deduct_list = mapper.getSubInfo(submap);
	for(Map<String, Object> mmp:deduct_list){
		String price = mmp.get("ITEM").toString();
		String value=mmp.get("VALUE").toString();
		double count=0;
		for(Map<String, Object> mp:list){
		    count+=MapUtil.getDouble(mp, price);
		}
		sum_deduct_item+=count;
		bzinfo.append(value +": "+new DecimalFormat("0.00").format(count)+"；");
	    }
	bzinfo.append("实付:" + new DecimalFormat("0.00").format(sum_all_amount-sum_deduct_amount+sum_deduct_item)+"；");
	common_param.put("bz", bzinfo.toString());
	List<Map<String, Object>> back_list=new ArrayList<Map<String,Object>>();
	for(int n=0;n<fpcount;n++){
	    Map<String, Object> back_map=new HashMap<String,Object>();
	    Map<String, Object> return_map=XMLUtil_TaxServer.createPlainXmlWithMap(common_param, list_listSpInfo.get(n));
	    common_param.put("fpqqlsh", MapUtil.getString(return_map, "fpqqlsh"));
	    common_param.put("hjje", MapUtil.getString(return_map, "hjje"));
	    common_param.put("hjse", MapUtil.getString(return_map, "hjse"));
	    common_param.put("jshj", MapUtil.getString(return_map, "jshj"));
	    common_param.put("qdbz", MapUtil.getString(return_map, "qdbz"));
	    common_param.put("jylsh", jylsh);
	    saveInfo(common_param, list_listSpInfo.get(n));
	    back_map.put("fpqqlsh", MapUtil.getString(return_map, "fpqqlsh"));
	    back_map.put("xml", MapUtil.getString(return_map, "xml"));
	    back_map.put("jylsh", jylsh);
	    back_list.add(back_map);
	    common_param.remove("fpqqlsh");
	}
	return back_list;
    }
    
    public Map<String, Object> getSpInfo(String spmc) {
	Map<String, Object> spbmmap = new HashMap<>();
	spbmmap.put("name", spmc);
	Map<String, Object> spInfo = msgmapper.getById(spbmmap);
	if (spInfo == null) {
	    throw new BizException("商品库中未含有该商品信息");
	}
	return spInfo;
    }
    public void saveInfo(Map<String, Object> mainInfo, List<SpInfo> list) {
	String suffix=GetSuffix.suffixCheck();
	mainInfo.put("suffix", suffix);
	mapper.save(mainInfo);
	Object id = mainInfo.get("id");
	for (SpInfo spinfo : list) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("invoice_id", id);
	    map.put("scale_count", spinfo.getXh());
	    map.put("fphxz", spinfo.getFphxz());
	    map.put("invoice_id", id);
	    map.put("spmc", spinfo.getSpmc());
	    map.put("spsm", spinfo.getSpsm());
	    map.put("ggxh", spinfo.getGgxh());
	    map.put("dw", spinfo.getDw());
	    map.put("spsl", spinfo.getSpsl());
	    map.put("dj", spinfo.getDj());
	    map.put("je", spinfo.getJe());
	    map.put("sl", spinfo.getSl());
	    map.put("se", spinfo.getSe());
	    map.put("hsbz", spinfo.getHsbz());
	    map.put("spbm", spinfo.getSpbm());
	    map.put("zxbm", spinfo.getZxbm());
	    map.put("yhzcbs", spinfo.getYhzcbs());
	    map.put("lslbs", spinfo.getLslbs());
	    map.put("zzstsgl", spinfo.getZzstsgl());
	    map.put("suffix", suffix);
	    mapper.saveDetail(map);
	}
    }
    
    
    
    
    // 返回子项xml
    public StringBuilder getSpInfoXMl(SpInfo spinfo) {
	StringBuilder sb = new StringBuilder();
	sb.append("<group xh=\"" + spinfo.getXh() + "\">");
	sb.append("<fphxz>" + spinfo.getFphxz() + "</fphxz>");
	sb.append("<spmc>" + spinfo.getSpmc() + "</spmc>");
	sb.append("<spsm>" + spinfo.getSpsm() + "</spsm>");
	sb.append("<ggxh>" + spinfo.getGgxh() + "</ggxh>");
	sb.append("<dw>" + spinfo.getDw() + "</dw>");
	sb.append("<spsl>" + spinfo.getSpsl() + "</spsl>");
	sb.append("<dj>" + spinfo.getDj() + "</dj>");
	sb.append("<je>" + spinfo.getJe() + "</je>");
	sb.append("<sl>" + spinfo.getSl() + "</sl>");
	sb.append("<se>" + spinfo.getSe() + "</se>");
	sb.append("<hsbz>" + spinfo.getHsbz() + "</hsbz>");
	sb.append("<spbm>" + spinfo.getSpbm() + "</spbm>");
	sb.append("<zxbm>" + spinfo.getZxbm() + "</zxbm>");
	sb.append("<yhzcbs>" + spinfo.getYhzcbs() + "</yhzcbs>");
	sb.append("<lslbs>" + spinfo.getLslbs() + "</lslbs>");
	sb.append("<zzstsgl>" + spinfo.getZzstsgl() + "</zzstsgl>");
	sb.append("</group>");
	return sb;
    }

    @Override
    public Map<String, Object> saveInvoice(Map<String, Object> param) throws Exception {
	String fpqqlsh = MapUtil.getString(param, "fpqqlsh");
	String xml = MapUtil.getString(param, "xml");
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
	Map<String, Object> updateInfo = new HashMap<String, Object>();
	updateInfo.put("fpqqlsh", fpqqlsh);
	updateInfo.put("fphm", fphm);
	updateInfo.put("fpdm", fpdm);
	updateInfo.put("jym", jym);
	updateInfo.put("skm", skm);
	updateInfo.put("kprq", kprq);
	updateInfo.put("suffix",GetSuffix.suffixCheck());
	param.put("suffix", GetSuffix.suffixCheck());
	mapper.update(updateInfo);
	Map<String, Object> result_map=new HashMap<>();
	result_map.put("fphm", fphm);
	result_map.put("fpdm", fpdm);
	return result_map;
    }

    public static void main(String[] args) {
	 String ss=new SimpleDateFormat("yyMMddHHmmssSSS") .format(new
	 Date())+""+(new Random().nextInt(9999));
	System.out.println(ss);
    }

    @SuppressWarnings({ "unchecked", "serial" })
    @Override
    public Map<String, Object> doInvoiceByOne(Map<String, Object> param) {
	param.put("suffix", GetSuffix.suffixCheck());
	//获取公共信息map
	final Map<String, Object> cominfo=invoice_mapper.getById(param);
	// 检测数据是否回滚
	Map<String, Object> sssmap = new HashMap<>();
	sssmap.put("suffix", GetSuffix.suffixCheck());
	sssmap.put("jylsh", MapUtil.getString(cominfo, "JYLSH"));
	if (orig_mapper.getOrigToInvoice(sssmap).size() == 0) {
	    throw new BizException("数据已回滚不能重新开票");
	}
	// 获取发票信息list
	param.put("invoice_id", MapUtil.getString(param, "id"));
	//获取农户信息
	Map<String, Object> farmer_info=mapper.getFarmerInfo(new HashMap<String,Object>(){
	    {
		put("id_card",MapUtil.getString(cominfo, "XSF_ID"));
	    }
	});
	List<Map<String, Object>> invoice_detail = invoice_mapper.listDetail(param);
	Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
		.getAttribute(LoginController.LOGIN_USER);
	Map<String, Object> user = (Map<String, Object>) loginMap.get("user");
	Map<String, Object> dept = (Map<String, Object>) loginMap.get("dept");
	String ghf_id=MapUtil.getString(dept, "ID");
	String kpr_id = MapUtil.getString(user, "ID"); // 获取开票人id
	String kpr_name = MapUtil.getString(user, "NAME"); // 获取开票人名称
	String kpzdbs = "";
	if(dept == null){
	    throw new BizException("开票信息不合法");
	}
	kpzdbs = MapUtil.getString(dept, "KPZDBS"); // 获取开票终端标识
	
	// 测试环境
	kpzdbs = "51010221";
	String ghdwsbh = "510302744676556";// 购货单位识别号 烟草公司
	String ghdwmc = "自贡市商业银行股份有限公司";// 购货单位名称 烟草公司
	String ghdwdzdh = "四川省自贡市";// 购货单位地址电话 烟草公司
	String ghdwyhzh = "6222220202"; // 购货单位银行账号 烟草公司
	// 正式环境
//	 kpzdbs = MapUtil.getString(dept, "KPZDBS");
//	 String ghdwsbh = MapUtil.getString(dept, "NSRSBH");// 购货单位识别号 烟草公司
//	 String ghdwmc = MapUtil.getString(dept, "NSRNAME");// 购货单位名称 烟草公司
//	 String ghdwdzdh = MapUtil.getString(dept, "DZDH");// 购货单位地址电话 烟草公司
//	 String ghdwyhzh = MapUtil.getString(dept, "YHZH"); // 购货单位银行账号 烟草公司
	String xsf_id=MapUtil.getString(farmer_info, "ID");
	String xhdwsbh=MapUtil.getString(farmer_info, "ID_CARD");
	String xhdwmc = MapUtil.getString(farmer_info, "NAME");// 销货单位名称 烟农名称
	String xhdwdzdh = MapUtil.getString(cominfo, "XSF_ADD");// 销货单位地址电话
	String xhdwyhzh = MapUtil.getString(farmer_info, "BANK_NAME")+ MapUtil.getString(farmer_info, "BANK_ACNT"); // 销货单位银行账号
	if(xsf_id.equals("")){
	    throw new BizException("未找到该农户信息");
	}
	//无收购发票测试环境  正式环境需删除
	xhdwsbh="510302744676556";
	xhdwmc="自贡市商业银行股份有限公司";
	// 所有发票的公共参数
	Map<String, Object> common_param=new HashMap<String,Object>();
	common_param.put("fpqqlsh", MapUtil.getString(cominfo, "FPQQLSH"));
	common_param.put("kpzdbs", kpzdbs);
	common_param.put("ghdwsbh", ghdwsbh);
	common_param.put("ghdwmc", ghdwmc);
	common_param.put("ghdwdzdh", ghdwdzdh);
	common_param.put("ghdwyhzh", ghdwyhzh);
	common_param.put("xhdwsbh", xhdwsbh);
	common_param.put("xhdwmc", xhdwmc);
	common_param.put("xhdwdzdh", xhdwdzdh);
	common_param.put("xhdwyhzh", xhdwyhzh);
	common_param.put("fplxdm", "007");
	common_param.put("kplx", "0");
	common_param.put("tspz", "02");
	common_param.put("kpr", kpr_name);//开票人
	common_param.put("kpr_id", kpr_id);//开票人id
	common_param.put("ghf_id", ghf_id);//购货方id
	common_param.put("xsf_id", xsf_id);//销售方id
	common_param.put("xsf_add", xhdwdzdh);//销售方id
	common_param.put("zsfs", "0");// 征税方式
	common_param.put("kce", "");// 差额征税扣除额
	common_param.put("skr", "");// 收款人
	common_param.put("fhr", "");// 复核人
	common_param.put("tzdbh", "");// 信息表编号
	common_param.put("yfpdm", "");// 原发票代码
	common_param.put("yfphm", "");// 原发票号码
	common_param.put("deptid", dept.get("ID"));// 所属组织机构id
	common_param.put("bz", MapUtil.getString(cominfo, "BZ"));// 备注
	List<SpInfo> list_spinfo=new ArrayList<SpInfo>();
	for(Map<String, Object> sp_mp : invoice_detail){
	    SpInfo spinfo=new SpInfo();
//	    SELECT ID, INVOICE_ID, SCALE_COUNT, FPHXZ, SPMC, SPSM, GGXH, 
//		DW, SPSL, DJ, JE, SL, SE, HSBZ, SPBM, ZXBM, YHZCBS, LSLBS, ZZSTSGL 
	    spinfo.setXh(MapUtil.getInt(sp_mp, "SCALE_COUNT"));
	    spinfo.setSpmc(MapUtil.getString(sp_mp, "SPMC"));
	    spinfo.setSpbm(MapUtil.getString(sp_mp, "SPBM"));
	    spinfo.setZxbm(MapUtil.getString(sp_mp, "ZXBM"));
	    spinfo.setFphxz("0");
	    spinfo.setDw(MapUtil.getString(sp_mp, "DW"));
	    spinfo.setSpsl(MapUtil.getString(sp_mp, "SPSL"));
	    spinfo.setDj(MapUtil.getString(sp_mp, "DJ"));
	    spinfo.setJe(MapUtil.getString(sp_mp, "JE"));
	    spinfo.setSl(MapUtil.getString(sp_mp, "SL"));
	    spinfo.setSe(MapUtil.getString(sp_mp, "SE"));
	    list_spinfo.add(spinfo);
	}
	 Map<String, Object> return_map=XMLUtil_TaxServer.createPlainXmlWithMap(common_param, list_spinfo);
	 Map<String, Object> back_map=new HashMap<String,Object>();
	 back_map.put("fpqqlsh", MapUtil.getString(return_map, "fpqqlsh"));
	 back_map.put("xml", MapUtil.getString(return_map, "xml"));
	 back_map.put("jylsh", MapUtil.getString(cominfo, "JYLSH"));
	return back_map;
    }

    @SuppressWarnings({ "unchecked", "serial" })
    @Override
    public Map<String, Object> redoInvoice(Map<String, Object> param) {
	
	param.put("suffix", GetSuffix.suffixCheck());
	//获取公共信息map
	final Map<String, Object> cominfo=invoice_mapper.getById(param);
	//检测数据是否回滚
	Map<String, Object> sssmap=new HashMap<>();
	sssmap.put("suffix", GetSuffix.suffixCheck());
	sssmap.put("jylsh", MapUtil.getString(cominfo, "JYLSH"));
	if(orig_mapper.getOrigToInvoice(sssmap).size()==0){
	    throw new BizException("数据已回滚不能重新开票");
	}
	//获取发票信息list
	param.put("invoice_id", MapUtil.getString(param, "id"));
	//获取农户信息
	Map<String, Object> farmer_info=mapper.getFarmerInfo(new HashMap<String,Object>(){
	    {
		put("id_card",MapUtil.getString(cominfo, "XSF_ID"));
	    }
	});
	List<Map<String, Object>> invoice_detail = invoice_mapper.listDetail(param);
	Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
		.getAttribute(LoginController.LOGIN_USER);
	Map<String, Object> user = (Map<String, Object>) loginMap.get("user");
	Map<String, Object> dept = (Map<String, Object>) loginMap.get("dept");
	String ghf_id=MapUtil.getString(dept, "ID");
	String kpr_id = MapUtil.getString(user, "ID"); // 获取开票人id
	String kpr_name = MapUtil.getString(user, "NAME"); // 获取开票人名称
	String kpzdbs = "";
	if(dept == null){
	    throw new BizException("开票信息不合法");
	}
	kpzdbs = MapUtil.getString(dept, "KPZDBS"); // 获取开票终端标识
	
	// 测试环境
	kpzdbs = "51010221";
	String ghdwsbh = "510302744676556";// 购货单位识别号 烟草公司
	String ghdwmc = "自贡市商业银行股份有限公司";// 购货单位名称 烟草公司
	String ghdwdzdh = "四川省自贡市";// 购货单位地址电话 烟草公司
	String ghdwyhzh = "6222220202"; // 购货单位银行账号 烟草公司
	// 正式环境
//	 kpzdbs = MapUtil.getString(dept, "KPZDBS");
//	 String ghdwsbh = MapUtil.getString(dept, "NSRSBH");// 购货单位识别号 烟草公司
//	 String ghdwmc = MapUtil.getString(dept, "NSRNAME");// 购货单位名称 烟草公司
//	 String ghdwdzdh = MapUtil.getString(dept, "DZDH");// 购货单位地址电话 烟草公司
//	 String ghdwyhzh = MapUtil.getString(dept, "YHZH"); // 购货单位银行账号 烟草公司
	String xsf_id=MapUtil.getString(farmer_info, "ID");
	String xhdwsbh=MapUtil.getString(farmer_info, "ID_CARD");
	String xhdwmc = MapUtil.getString(farmer_info, "NAME");// 销货单位名称 烟农名称
	String xhdwdzdh = MapUtil.getString(cominfo, "XSF_ADD");// 销货单位地址电话
	String xhdwyhzh = MapUtil.getString(farmer_info, "BANK_NAME")+ MapUtil.getString(farmer_info, "BANK_ACNT"); // 销货单位银行账号
	if(xsf_id.equals("")){
	    throw new BizException("未找到该农户信息");
	}
	//无收购发票测试环境  正式环境需删除
	xhdwsbh="510302744676556";
	xhdwmc="自贡市商业银行股份有限公司";
	// 所有发票的公共参数
	Map<String, Object> common_param=new HashMap<String,Object>();
	common_param.put("kpzdbs", kpzdbs);
	common_param.put("ghdwsbh", ghdwsbh);
	common_param.put("ghdwmc", ghdwmc);
	common_param.put("ghdwdzdh", ghdwdzdh);
	common_param.put("ghdwyhzh", ghdwyhzh);
	common_param.put("xhdwsbh", xhdwsbh);
	common_param.put("xhdwmc", xhdwmc);
	common_param.put("xhdwdzdh", xhdwdzdh);
	common_param.put("xhdwyhzh", xhdwyhzh);
	common_param.put("fplxdm", "007");
	common_param.put("kplx", "0");
	common_param.put("tspz", "02");
	common_param.put("kpr", kpr_name);//开票人
	common_param.put("kpr_id", kpr_id);//开票人id
	common_param.put("ghf_id", ghf_id);//购货方id
	common_param.put("xsf_id", xsf_id);//销售方id
	common_param.put("xsf_add", xhdwdzdh);//销售方id
	common_param.put("zsfs", "0");// 征税方式
	common_param.put("kce", "");// 差额征税扣除额
	common_param.put("skr", "");// 收款人
	common_param.put("fhr", "");// 复核人
	common_param.put("tzdbh", "");// 信息表编号
	common_param.put("yfpdm", "");// 原发票代码
	common_param.put("yfphm", "");// 原发票号码
	common_param.put("deptid", dept.get("ID"));// 所属组织机构id
	common_param.put("bz", MapUtil.getString(cominfo, "BZ"));
	List<SpInfo> list_spinfo=new ArrayList<SpInfo>();
	for(Map<String, Object> sp_mp : invoice_detail){
	    SpInfo spinfo=new SpInfo();
//	    SELECT ID, INVOICE_ID, SCALE_COUNT, FPHXZ, SPMC, SPSM, GGXH, 
//		DW, SPSL, DJ, JE, SL, SE, HSBZ, SPBM, ZXBM, YHZCBS, LSLBS, ZZSTSGL 
	    spinfo.setXh(MapUtil.getInt(sp_mp, "SCALE_COUNT"));
	    spinfo.setSpmc(MapUtil.getString(sp_mp, "SPMC"));
	    spinfo.setSpbm(MapUtil.getString(sp_mp, "SPBM"));
	    spinfo.setZxbm(MapUtil.getString(sp_mp, "ZXBM"));
	    spinfo.setFphxz("0");
	    spinfo.setDw(MapUtil.getString(sp_mp, "DW"));
	    spinfo.setSpsl(MapUtil.getString(sp_mp, "SPSL"));
	    spinfo.setDj(MapUtil.getString(sp_mp, "DJ"));
	    spinfo.setJe(MapUtil.getString(sp_mp, "JE"));
	    spinfo.setSl(MapUtil.getString(sp_mp, "SL"));
	    spinfo.setSe(MapUtil.getString(sp_mp, "SE"));
	    list_spinfo.add(spinfo);
	}
	Map<String, Object> return_map = XMLUtil_TaxServer.createPlainXmlWithMap(common_param, list_spinfo);
	common_param.put("fpqqlsh", MapUtil.getString(return_map, "fpqqlsh"));
	common_param.put("hjje", MapUtil.getString(return_map, "hjje"));
	common_param.put("hjse", MapUtil.getString(return_map, "hjse"));
	common_param.put("jshj", MapUtil.getString(return_map, "jshj"));
	common_param.put("qdbz", MapUtil.getString(return_map, "qdbz"));
	common_param.put("jylsh", MapUtil.getString(cominfo, "JYLSH"));
	common_param.put("bz", MapUtil.getString(cominfo, "BZ"));
	saveInfo(common_param, list_spinfo);
	Map<String, Object> back_map = new HashMap<String, Object>();
	back_map.put("fpqqlsh", MapUtil.getString(return_map, "fpqqlsh"));
	back_map.put("xml", MapUtil.getString(return_map, "xml"));
	back_map.put("jylsh", MapUtil.getString(cominfo, "JYLSH"));
	return back_map;

    }
    //多条（补贴）判断是否能合并
    public boolean checkCombine(List<Map<String, Object>> list,List<Map<String, Object>> sublist){
	for(Map<String, Object> mm:list){
	    double weight=MapUtil.getDouble(mm, "WEIGHT");
	    for(Map<String, Object> map:sublist){
		String sub_name=map.get("ITEM").toString();
		String sub_value=map.get("VALUE").toString();
		String sub_amount="SUB_AMOUNT"+sub_name.charAt(sub_name.length()-1);
		double sub_price=MapUtil.getDouble(mm, sub_name);
		double sub_count=weight*sub_price;
		if(!new DecimalFormat("0.00").format(sub_count).equals(new DecimalFormat("0.00").format(MapUtil.getDouble(mm, sub_amount)))){
		    throw new BizException(sub_value+"单价不一致不能进行合并");
		}
	    }
	}
	return true;
    }
    @Override
    public List<Map<String, Object>> getTree(Map<String, Object> parm) {
	return mapper.getTree1(parm);
    }
    /**
     * 回滚信息
     */
    @Override
    public void rollback(Map<String, Object> parm) {
	//1.查看是否所有票都已作废
	parm.put("suffix", GetSuffix.suffixCheck());
	List<Map<String, Object>> list=invoice_mapper.listInvoice(parm);
	for(Map<String, Object> mm:list){
	    if(MapUtil.getString(mm, "STATUS").equals("1")){
		throw new BizException("回滚信息前必须作废该套票的所有已开发票");
	    }
	}
	//2.更新源数据
	parm.put("status", "0");
	mapper.updateStatus(parm);
	//3.删除中间表
	orig_mapper.deleteOrigToInvoice1(parm);
    }
    /**
     * 单条数据开票
     */
    @Override
    @Transactional(propagation=Propagation.NESTED )
    public List<Map<String, Object>> doInvoice1(Map<String, Object> parm) {
	//1、获取单条的源数据
	parm.put("suffix", GetSuffix.suffixCheck());
	//1、获取单条的源数据 传入参数invoice_no
	List<Map<String, Object>> list=orig_mapper.listDetail(parm);
	//获取农户信息 传入参数id_card
	Map<String, Object> farmer_info=orig_mapper.getCommonInfo(parm);
	String ids[] = parm.get("invoice_no").toString().split(",");
	parm.put("array", ids);
	orig_mapper.insertOrigToInvoice(parm);
	String jylsh=parm.get("id").toString();
	parm.put("jylsh", jylsh);
	parm.put("status", "1");
	mapper.updateStatus(parm);
	//获取list_xml
	List<Map<String, Object>> list_xml=new ArrayList<>();
	try {
	    list_xml=getInvoiceList(farmer_info,list,jylsh);
	}catch(BizException ex){
	    revokeTran(parm);
	    throw new BizException(ex.getMessage());
	} catch (Exception e) {
	    revokeTran(parm);
	    throw new BizException("请求开票有误");
	}
	return list_xml;
    }
    

   

}
