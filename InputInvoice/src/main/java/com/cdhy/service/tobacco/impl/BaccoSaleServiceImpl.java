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

import com.cdhy.controller.security.LoginController;
import com.cdhy.dao.security.DepartMapper;
import com.cdhy.dao.tobacco.BaccoSaleMapper;
import com.cdhy.entity.SpInfo;
import com.cdhy.exception.BizException;
import com.cdhy.service.tobacco.IBaccoSaleService;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.MapUtil;
import com.cdhy.util.MessageUtil;
@Service
public class BaccoSaleServiceImpl implements IBaccoSaleService {
    @Autowired
    private BaccoSaleMapper mapper;
    @Autowired
    private DepartMapper departMapper;
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> doInvoice(Map<String, Object> param) {
	Map<String, Object> buyer=(Map<String, Object>) param.get("buyer");
	List<Map<String, Object>> items=(List<Map<String, Object>>) param.get("items");
	if(MapUtil.getString(buyer, "id").equals("")){
	    throw new BizException("购方信息不能为空！");
	}
	if(items.size()==0){
	    throw new BizException("商品信息不能为空！");
	}
	double count_jshj=0;
	for(Map<String, Object> mm:items){
	    if(Double.parseDouble(mm.get("je").toString())==0){
		throw new BizException("商品金额不能为0！");
	    }else{
		count_jshj+=Double.parseDouble(mm.get("je").toString());
		count_jshj+=Double.parseDouble(mm.get("se").toString());
	    }
	}
	if(count_jshj>100){
	    throw new BizException("超出了票面总金额！");
	}
	Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
		.getAttribute(LoginController.LOGIN_USER);
	Map<String, Object> userinfo = (Map<String, Object>) loginMap.get("user");
	Map<String, Object> dept = (Map<String, Object>) loginMap.get("dept");
	userinfo.put("id", userinfo.get("DEPARTID"));
	//获取公共信息
	Map<String, Object> deptinfo = departMapper.getById(userinfo);
	if(deptinfo==null){
	    throw new BizException("你所在区域不合法");
	}
	String xsf_id=MapUtil.getString(userinfo, "DEPARTID");
	String ghf_id=MapUtil.getString(buyer, "id");
	String kpr=MapUtil.getString(userinfo, "NAME"); //获取开票人信息
	String kpzdbs=MapUtil.getString(deptinfo, "KPZDBS"); //获取开票终端标识
	if(kpzdbs.equals("")){
	    throw new BizException("你所在区域没有配置开票终端标识");
	}
	kpzdbs="51010221";//测试
	String xhdwsbh="510302744676556";//销货单位识别号  烟草公司
	String xhdwmc="自贡市商业银行股份有限公司";//销货单位名称   烟草公司
	String xhdwdzdh="四川省自贡市";// 销货单位地址电话 烟草公司
	String xhdwyhzh="6222220202"; //销货单位银行账号 烟草公司
	
	String ghdwsbh="510302744676554"; //购货单位识别号  buyer  测试
	String ghdwmc="成都沪友有限公司";//购货单位名称  buyer
	String ghdwdzdh="四川省成都市";//购货单位地址电话  buyer
	String ghdwyhzh="625585555";//购货单位银行账号  buyer
	
	
//	kpzdbs = MapUtil.getString(dept, "KPZDBS");// 正式环境
//	String xhdwsbh = MapUtil.getString(dept, "NSRSBH");// 销货单位识别号 烟草公司
//	String xhdwmc = MapUtil.getString(dept, "NSRNAME");// 销货单位名称 烟草公司
//	String xhdwdzdh = MapUtil.getString(dept, "DZDH");// 销货单位地址电话 烟草公司
//	String xhdwyhzh = MapUtil.getString(dept, "YHZH"); // 销货单位银行账号 烟草公司
//	
//	String ghdwsbh=MapUtil.getString(buyer, "nsrsbh"); //购货单位识别号  buyer 正式环境
//	String ghdwmc=MapUtil.getString(buyer, "name");//购货单位名称  buyer
//	String ghdwdzdh=MapUtil.getString(buyer, "address")+MapUtil.getString(buyer, "phone");//购货单位地址电话  buyer
//	String ghdwyhzh=MapUtil.getString(buyer, "khh")+MapUtil.getString(buyer, "yhzh");//购货单位银行账号  buyer
	
	String fpqqlsh=new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date())+""+(new Random().nextInt(99));//发票请求流水号
	String bz="";//备注
	int m=0;
	double hjje=0.0;
	double hjse=0.0;
	StringBuilder sb = new StringBuilder();
	sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
	sb.append("<business id=\"10008\" comment=\"发票开具\">");
	sb.append("<body yylxdm=\"1\">");
	sb.append("<kpzdbs>" + kpzdbs + "</kpzdbs>");
	sb.append("<fplxdm>004</fplxdm>");
	sb.append("<fpqqlsh>" + fpqqlsh + "</fpqqlsh>");
	sb.append("<kplx>0</kplx>");
	sb.append("<tspz>00</tspz>");
	sb.append("<xhdwsbh>" + xhdwsbh + "</xhdwsbh>");
	sb.append("<xhdwmc>" + xhdwmc + "</xhdwmc>");
	sb.append("<xhdwdzdh>" + xhdwdzdh + "</xhdwdzdh>");
	sb.append("<xhdwyhzh>" + xhdwyhzh + "</xhdwyhzh>");

	sb.append("<ghdwsbh>" + ghdwsbh + "</ghdwsbh>");
	sb.append("<ghdwmc>" + ghdwmc + "</ghdwmc>");
	sb.append("<ghdwdzdh>" + ghdwdzdh + "</ghdwdzdh>");
	sb.append("<ghdwyhzh>" + ghdwyhzh + "</ghdwyhzh>");
	sb.append("<qdbz>0</qdbz>");
	sb.append("<zsfs>0</zsfs>");
	sb.append("<fyxm count=\"" + items.size()+ "\">");
	List<SpInfo> listDetail=new ArrayList<SpInfo>();
	for(Map<String, Object> mm:items){
	    m++;
	    SpInfo spinfo=new SpInfo();
	    spinfo.setXh(m);
	    spinfo.setFphxz("0"); 
	    spinfo.setSpmc(MapUtil.getString(mm, "comname")); //商品名称
	    spinfo.setDw(MapUtil.getString(mm, "dw"));//商品单位
	    spinfo.setSpsl(MapUtil.getString(mm, "count")); //商品数量
	    spinfo.setDj(MapUtil.getString(mm, "dj"));//商品单价
	    spinfo.setJe(MapUtil.getString(mm, "je"));//商品金额
	    spinfo.setSl(MapUtil.getString(mm, "sl"));//商品税率
	    spinfo.setSe(MapUtil.getString(mm, "se"));//商品税额
	    spinfo.setSpbm(MapUtil.getString(mm, "code"));//商品编码
	    spinfo.setZxbm(MapUtil.getString(mm, "zxbm"));//自行编码
	    hjje+=MapUtil.getDouble(mm, "je");
	    hjse+=MapUtil.getDouble(mm, "se");
	    sb.append(getSpInfoXMl(spinfo));
	    listDetail.add(spinfo);
	}
	String sshjje=new DecimalFormat("0.00").format(hjje);
	String jshj=new DecimalFormat("0.00").format(hjje+hjse);
	sb.append("</fyxm>");
    	sb.append("<hjje>"+sshjje+"</hjje>");
	sb.append("<hjse>"+hjse+"</hjse>");
	sb.append("<jshj>"+jshj+"</jshj>");
	sb.append("<kce></kce>");
	sb.append("<bz></bz>");
	sb.append("<skr></skr>");
	sb.append("<fhr></fhr>");
	sb.append("<kpr>"+kpr+"</kpr>");
	sb.append("<tzdbh></tzdbh>");
	sb.append("<yfpdm></yfpdm>");
	sb.append("<yfphm></yfphm>");
	sb.append("</body>");
	sb.append("</business>");
	//返回页面数据
	Map<String , Object> map=new HashMap<>();
	map.put("fpqqlsh", fpqqlsh);
	map.put("xml", sb);
	Map<String, Object> mainInfo =new HashMap<String, Object>();
	mainInfo.put("fpqqlsh", fpqqlsh);//发票请求流水号
	mainInfo.put("jylsh", "");//交易流水号
	mainInfo.put("kplx", "");//开票类型
	mainInfo.put("xsf_id", xsf_id);//销售方id
	mainInfo.put("ghf_id", ghf_id);//购货方id
	mainInfo.put("qdbz", "");//清单标识
	mainInfo.put("zsfs", "");//征税方式
	mainInfo.put("hjje", sshjje);//合计金额
	mainInfo.put("hjse", hjse);//合计税额
	mainInfo.put("jshj", jshj);//价税合计
	mainInfo.put("kce", "0.00");//差额征税扣除额
	mainInfo.put("bz", bz);//备注
	mainInfo.put("skr", "");//收款人
	mainInfo.put("fhr", "");//复核人
	mainInfo.put("kpr", kpr);//开票人
	mainInfo.put("tzdbh", "");//信息表编号
	mainInfo.put("yfpdm", "");//原发票代码
	mainInfo.put("yfphm", "");//原发票号码
	mainInfo.put("fpdm", "");//发票代码
	mainInfo.put("fphm", "");//发票号码
	mainInfo.put("skm", "");//税控码
	mainInfo.put("jym", "");//校验码
	mainInfo.put("deptid", dept.get("ID"));//所属组织机构id
	mainInfo.put("type", "1");//发票种类（1=烟草收购，2=烟草转售）
	saveInfo(mainInfo,listDetail);
	List<Map<String, Object>> listMap=new ArrayList<Map<String, Object>>();
	listMap.add(map);
	return listMap;
    }
    public StringBuilder getSpInfoXMl(SpInfo spinfo){
	    StringBuilder sb = new StringBuilder();
	    double sl =Double.parseDouble(spinfo.getSl())/100;
	    double je =Double.parseDouble(spinfo.getJe());
	    sb.append("<group xh=\""+spinfo.getXh()+"\">");
	    sb.append("<fphxz>"+spinfo.getFphxz()+"</fphxz>");
	    sb.append("<spmc>"+spinfo.getSpmc()+"</spmc>");
	    sb.append("<spsm></spsm>");
	    sb.append("<ggxh></ggxh>");
	    sb.append("<dw>"+spinfo.getDw()+"</dw>");
	    sb.append("<spsl>"+spinfo.getSpsl()+"</spsl>");
	    sb.append("<dj>"+spinfo.getDj()+"</dj>");
	    sb.append("<je>"+je+"</je>");
	    sb.append("<sl>"+sl+"</sl>");
	    sb.append("<se>"+je*sl+"</se>");
	    sb.append("<hsbz>0</hsbz>");
	    sb.append("<spbm></spbm>");
	    sb.append("<zxbm></zxbm>");
	    sb.append("<yhzcbs>0</yhzcbs>");
	    sb.append("<lslbs></lslbs>");
	    sb.append("<zzstsgl></zzstsgl>");
	    sb.append("</group>");
	    return sb;
	}
    public void saveInfo(Map<String, Object> mainInfo,List<SpInfo> list){
	    mainInfo.put("suffix", GetSuffix.suffixCheck());
	    mapper.save(mainInfo);
	    Object id=mainInfo.get("id");
	    for(SpInfo spinfo:list){
		Map<String, Object> map=new HashMap<String, Object>();
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
		map.put("suffix", GetSuffix.suffixCheck());
		mapper.saveDetail(map);
	    }
	}
    @Override
    public Map<String, Object> saveInvoice(Map<String, Object> param) throws Exception {
	param.put("suffix", GetSuffix.suffixCheck());
	String fpqqlsh=MapUtil.getString(param, "fpqqlsh");
	String xml=MapUtil.getString(param, "xml");
	    Map<String, Object> resp_result = MessageUtil.parseXmlFromString(xml);
	    resp_result = MapUtil.mapKey2Lower(resp_result);
	    String returncode=MapUtil.getString(resp_result, "returncode");
	    String returnmsg=MapUtil.getString(resp_result, "returnmsg");
	    if(!returncode.equals("0")){
		if(returnmsg.equals("")){
		    throw new BizException("开票失败");
		}
		throw new BizException(returnmsg);
	    }
	    String fphm=MapUtil.getString(resp_result, "fphm");
	    String fpdm=MapUtil.getString(resp_result, "fpdm");
	    String skm=MapUtil.getString(resp_result, "skm");
	    String jym=MapUtil.getString(resp_result, "jym");
	    String kprq=MapUtil.getString(resp_result, "kprq");
	    skm = skm.replaceAll("&gt;", ">");
	    skm = skm.replaceAll("&lt;", "<");
	    Map<String, Object> updateInfo=new HashMap<String,Object>();
	    updateInfo.put("fpqqlsh", fpqqlsh);
	    updateInfo.put("fphm", fphm);
	    updateInfo.put("fpdm", fpdm);
	    updateInfo.put("jym", jym);
	    updateInfo.put("skm", skm);
            updateInfo.put("kprq", kprq);
            updateInfo.put("suffix", GetSuffix.suffixCheck());
            mapper.update(updateInfo);
            Map<String, Object> result_map = new HashMap<>();
            result_map.put("fphm", fphm);
            result_map.put("fpdm", fpdm);
            return result_map;
    }

}
