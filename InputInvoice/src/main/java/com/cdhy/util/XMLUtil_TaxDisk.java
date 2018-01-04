package com.cdhy.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cdhy.entity.SpInfo;

public class XMLUtil_TaxDisk {
    /**
     * 发票开具xml
     * 
     * @param list
     * @param map
     * @return
     */
    public static Map<String, Object> getInvoiceXml(List<SpInfo> list, Map<String, Object> map) {
	// 获取公共信息
	String skpbh = MapUtil.getString(map, "skpbh");
	String skpkl = MapUtil.getString(map, "skpkl");
	String keypwd = MapUtil.getString(map, "keypwd");
	String fplxdm = MapUtil.getString(map, "fplxdm");
	String kplx = MapUtil.getString(map, "kplx");
	String tspz = MapUtil.getString(map, "tspz");
	String xhdwsbh = MapUtil.getString(map, "xhdwsbh");
	String xhdwmc = MapUtil.getString(map, "xhdwmc");
	String xhdwdzdh = MapUtil.getString(map, "xhdwdzdh");
	String xhdwyhzh = MapUtil.getString(map, "xhdwyhzh");
	String ghdwsbh = MapUtil.getString(map, "ghdwsbh");
	String ghdwmc = MapUtil.getString(map, "ghdwmc");
	String ghdwdzdh = MapUtil.getString(map, "ghdwdzdh");
	String ghdwyhzh = MapUtil.getString(map, "ghdwyhzh");
	String bmbbbh = MapUtil.getString(map, "bmbbbh");

	StringBuffer sb = new StringBuffer();
	sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>")
		.append("<business comment=\"发票开具\" id=\"FPKJ\">")
		.append("<body yylxdm=\"1\">")
		.append("<input>")
		.append("<skpbh>" + skpbh + "</skpbh>")
		.append("<skpkl>" + skpkl + "</skpkl>")
		.append("<keypwd>" + keypwd + "</keypwd>")
		.append("<fplxdm>" + fplxdm + "</fplxdm>")
		.append("<kplx>" + kplx + "</kplx>")
		.append("<tspz>" + tspz + "</tspz>")
		.append("<xhdwsbh>" + xhdwsbh + "</xhdwsbh>")
		.append("<xhdwmc>" + xhdwmc + "</xhdwmc>")
		.append("<xhdwdzdh>" + xhdwdzdh + "</xhdwdzdh>")
		.append("<xhdwyhzh>" + xhdwyhzh + "</xhdwyhzh>")
		.append("<ghdwsbh>" + ghdwsbh + "</ghdwsbh>")
		.append("<ghdwmc>" + ghdwmc + "</ghdwmc>")
		.append("<ghdwdzdh>" + ghdwdzdh + "</ghdwdzdh>")
		.append("<ghdwyhzh>" + ghdwyhzh + "</ghdwyhzh>")
		.append("<bmbbbh>" + bmbbbh + "</bmbbbh>")
		.append("<hsslbs>0</hsslbs>");
		// 获取商品明细
		int size=list.size();
		if(size>8){
		    sb.append("<qdxm count=\"" + list.size() + "\">");
		}
		else{
		    sb.append("<fyxm count=\"" + list.size() + "\">");
		}
	double hjje = 0;
	double hjse = 0;
	double jshj = 0;
	for (int i = 0; i < list.size(); i++) {
	    SpInfo Spmx = list.get(i);
	    int m = i + 1;
	    sb.append("<group xh=\"" + m + "\">")
		    .append("<fphxz>" + Spmx.getFphxz() + "</fphxz>")
		    .append("<spmc>" + Spmx.getSpmc() + "</spmc>")
		    .append("<spsm>" + Spmx.getSpsm() + "</spsm>")
		    .append("<ggxh>" + Spmx.getGgxh() + "</ggxh>")
		    .append("<dw>" + Spmx.getDw() + "</dw>")
		    .append("<spsl>" + Spmx.getSpsl() + "</spsl>")
		    .append("<dj>" + Spmx.getDj() + "</dj>")
		    .append("<je>" + Spmx.getJe() + "</je>")
		    .append("<kcje></kcje>")
		    .append("<sl>" + Spmx.getSl() + "</sl>")
		    .append("<se>" + Spmx.getSe() + "</se>")
		    .append("<hsbz>0</hsbz>")
		    .append("<spbm>" + Spmx.getSpbm() + "</spbm>")
		    .append("<zxbm>" + Spmx.getZxbm() + "</zxbm>")
		    .append("<yhzcbs>" + Spmx.getYhzcbs() + "</yhzcbs>")
		    .append("<slbs>" + Spmx.getLslbs() + "</slbs>")
		    .append("<zzstsgl>" + Spmx.getZzstsgl() + "</zzstsgl>")
		    .append("</group>");
	    hjje += Double.parseDouble(Spmx.getJe());
	    hjse += Double.parseDouble(Spmx.getSe());
	    jshj += Double.parseDouble(Spmx.getHsje());
	}
	String qdbz = "0";

	if (size > 8) {
	    qdbz = "1";
	}
	
	if(size>8){
	    sb.append("</qdxm>");
	}
	else{
	    sb.append("</fyxm>");
	}
		sb.append("<zhsl></zhsl>")
		.append("<hjje>" + String.format("%.2f", hjje) + "</hjje>")
		.append("<hjse>" + String.format("%.2f", hjse) + "</hjse>")
		.append("<jshj>" + String.format("%.2f", jshj) + "</jshj>")
		.append("<bz>" + MapUtil.getString(map, "bz") + "</bz>")
		.append("<skr>" + MapUtil.getString(map, "skr") + "</skr>")
		.append("<fhr>" + MapUtil.getString(map, "fhr") + "</fhr>")
		.append("<kpr>" + MapUtil.getString(map, "kpr") + "</kpr>")
		.append("<jmbbh>" + MapUtil.getString(map, "jmbbh") + "</jmbbh>")
		.append("<zyspmc>" + MapUtil.getString(map, "zyspmc") + "</zyspmc>")
		.append("<spsm>" + MapUtil.getString(map, "spsm") + "</spsm>")
		.append("<qdbz>" + qdbz + "</qdbz>")
		.append("<ssyf>" + MapUtil.getString(map, "ssyf") + "</ssyf>")
		.append("<kpjh>1</kpjh>")
		.append("<tzdbh>" + MapUtil.getString(map, "tzdbh") + "</tzdbh>")
		.append("<yfpdm>" + MapUtil.getString(map, "yfpdm") + "</yfpdm>")
		.append("<yfphm>" + MapUtil.getString(map, "yfphm") + "</yfphm>")
		.append("<qmcs>0000004282000000</qmcs>")
		.append("</input>")
		.append("</body>")
		.append("</business>");
	// 返回值
	Map<String, Object> result_map = new HashMap<String, Object>();
	result_map.put("hjje", String.format("%.2f", hjje));
	result_map.put("hjse", String.format("%.2f", hjse));
	result_map.put("jshj", String.format("%.2f", jshj));
	result_map.put("qdbz", qdbz);
	result_map.put("xml", sb);

	return result_map;
    }

    /**
     * 注册码导入xml
     * 
     * @param map
     * @return
     */
    public static String getZCMDRXml(Map<String, Object> map) {
	String zcmxx = MapUtil.getString(map, "zcmxx");
	StringBuffer sb = new StringBuffer();
	sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>")
		.append("<business comment=\"注册码信息导入\" id=\"ZCMDR\">")
		.append("<body yylxdm=\"1\">")
		.append("<input>")
		.append("<zcmxx>" + zcmxx + "</zcmxx>")
		.append("</input>")
		.append("</body>")
		.append("</business>");
	return sb.toString();
    }

    /**
     * 税控盘信息查询xml
     * 
     * @return
     */
    public static String getSkpxxcxXml(Map<String, Object> map) {
	String skpkl = MapUtil.getString(map, "skpkl");
	StringBuffer sb = new StringBuffer();
	sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>")
		.append("<business comment=\"税控盘信息查询\" id=\"SKPXXCX\">")
		.append("<body yylxdm=" + 1 + ">")
		.append("<input>")
		.append("<skpkl>" + skpkl + "</skpkl>")
		.append("</input>")
		.append("</body>")
		.append("</business>");
	return sb.toString();
    }

    /**
     * 监控数据查询xml
     * 
     * @param map
     * @return
     */
    public static String getJksjcxXml(Map<String, Object> param) {
	String nsrsbh = MapUtil.getString(param, "nsrsbh");
	String skpbh = MapUtil.getString(param, "skpbh");
	String skpkl = MapUtil.getString(param, "skpkl");
	String keypwd = MapUtil.getString(param, "keypwd");
	String fplxdm = MapUtil.getString(param, "fplxdm");

	StringBuffer sb = new StringBuffer();
	sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>")
		.append("<business comment=\"监控数据查询\" id=\"JKSJCX\">")
		.append("<body yylxdm=" + 1 + ">")
		.append("<input>")
		.append("<nsrsbh>" + nsrsbh + "</nsrsbh>")
		.append("<skpbh>" + skpbh + "</skpbh>")
		.append("<skpkl>" + skpkl + "</skpkl>")
		.append("<keypwd>" + keypwd + "</keypwd>")
		.append("<fplxdm>" + fplxdm + "</fplxdm>")
		.append("</input>")
		.append("</body>")
		.append("</business>");
	return sb.toString();
    }

    /**
     * 税种税目信息查询
     * 
     * @param map
     * @return
     */
    public static String getSzsmcxXml(Map<String, Object> param) {
	String nsrsbh = MapUtil.getString(param, "nsrsbh");
	String skpbh = MapUtil.getString(param, "skpbh");
	String skpkl = MapUtil.getString(param, "skpkl");
	String keypwd = MapUtil.getString(param, "keypwd");
	String fplxdm = MapUtil.getString(param, "fplxdm");

	StringBuffer sb = new StringBuffer();
	sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>")
		.append("<business comment=\"税种税目信息查询\" id=\"SZSMCX\">")
		.append("<body yylxdm=" + 1 + ">")
		.append("<input>")
		.append("<nsrsbh>" + nsrsbh + "</nsrsbh>")
		.append("<skpbh>" + skpbh + "</skpbh>")
		.append("<skpkl>" + skpkl + "</skpkl>")
		.append("<keypwd>" + keypwd + "</keypwd>")
		.append("<fplxdm>" + fplxdm + "</fplxdm>")
		.append("</input>")
		.append("</body>")
		.append("</business>");
	return sb.toString();
    }

    /**
     * 购票信息查询
     * 
     * @param map
     * @return
     */
    public static String getGpxxcxXml(Map<String, Object> param) {
	String nsrsbh = MapUtil.getString(param, "nsrsbh");
	String skpbh = MapUtil.getString(param, "skpbh");
	String skpkl = MapUtil.getString(param, "skpkl");
	String keypwd = MapUtil.getString(param, "keypwd");
	String fplxdm = MapUtil.getString(param, "fplxdm");

	StringBuffer sb = new StringBuffer();
	sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>")
		.append("<business comment=\"购票信息查询\" id=\"GPXXCX\">")
		.append("<body yylxdm=" + 1 + ">")
		.append("<input>")
		.append("<nsrsbh>" + nsrsbh + "</nsrsbh>")
		.append("<skpbh>" + skpbh + "</skpbh>")
		.append("<skpkl>" + skpkl + "</skpkl>")
		.append("<keypwd>" + keypwd + "</keypwd>")
		.append("<fplxdm>" + fplxdm + "</fplxdm>")
		.append("</input>")
		.append("</body>")
		.append("</business>");
	return sb.toString();
    }
    
    /**
     * 发票作废
     * @param param
     * @return
     */
    public static String getFpzfXml(Map<String, Object> param){
    	String nsrsbh = MapUtil.getString(param, "nsrsbh");
    	String skpbh = MapUtil.getString(param,"skpbh");
    	String skpkl = MapUtil.getString(param, "skpkl");
    	String keypwd = MapUtil.getString(param, "keypwd");
    	String fplxdm = MapUtil.getString(param, "fplxdm");
    	String zflx = MapUtil.getString(param, "zflx");
    	String fpdm = MapUtil.getString(param, "fpdm");
    	String fphm = MapUtil.getString(param, "fphm");
    	String hjje = MapUtil.getString(param, "hjje");
    	String zfr = MapUtil.getString(param, "zfr");
    	String qmcs = MapUtil.getString(param, "qmcs");
    	if(zflx.equals("")){
    	    zflx="1";
    	}
    	StringBuffer sb = new StringBuffer();
    	sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>")
		.append("<business comment=\"发票作废\" id=\"FPZF\">")
		.append("<body yylxdm=\"1\">")
		.append("<input>")
    	.append("<nsrsbh>"+nsrsbh+"</nsrsbh>")
    	.append("<skpbh>"+skpbh+"</skpbh>")
    	.append("<skpkl>"+skpkl+"</skpkl>")
    	.append("<keypwd>"+keypwd+"</keypwd>")
    	.append("<fplxdm>"+fplxdm+"</fplxdm>")
    	.append("<zflx>"+zflx+"</zflx>")
    	.append("<fpdm>"+fpdm+"</fpdm>")
    	.append("<fphm>"+fphm+"</fphm>")
    	.append("<hjje>"+hjje+"</hjje>")
    	.append("<zfr>"+zfr+"</zfr>")
    	.append("<qmcs>"+qmcs+"</qmcs>")
    	.append("</input>")
    	.append("</body>")
    	.append("</business>");
		return sb.toString();
    }
    
    /**
     * 打印发票
     * 
     * @param map
     * @return
     */
    public static String getFpprintXml(Map<String, Object> param) {
	String nsrsbh = MapUtil.getString(param, "nsrsbh");
	String skpbh = MapUtil.getString(param, "skpbh");
	String skpkl = MapUtil.getString(param, "skpkl");
	String keypwd = MapUtil.getString(param, "keypwd");
	String fplxdm = MapUtil.getString(param, "fplxdm");
	String fpdm = MapUtil.getString(param, "fpdm");
	String fphm = MapUtil.getString(param, "fphm");
	String dylx = MapUtil.getString(param, "dylx");
	String dyfs = MapUtil.getString(param, "dyfs");
	if(dylx.equals("")){
	    dylx="0";
	}
	if(dyfs.equals("")){
	    dyfs="1";
	}
	StringBuffer sb = new StringBuffer();
	sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>")
		.append("<business comment=\"发票打印\" id=\"FPDY\">")
		.append("<body  yylxdm=\"1\">")
		.append("<input>")
		.append("<nsrsbh>" + nsrsbh + "</nsrsbh>")
		.append("<skpbh>" + skpbh + "</skpbh>")
		.append("<skpkl>" + skpkl + "</skpkl>")
		.append("<keypwd>" + keypwd + "</keypwd>")
		.append("<fplxdm>" + fplxdm + "</fplxdm>")
		.append("<fpdm>" + fpdm + "</fpdm>")
		.append("<fphm>" + fphm + "</fphm>")
		.append("<dylx>"+dylx+"</dylx>")
		.append("<dyfs>"+dyfs+"</dyfs>")
		.append("</input>")
		.append("</body>")
		.append("</business>");
	return sb.toString();
    }
    
    /**
     * 打印页边距设置
     * 
     * @param map
     * @return
     */
    public static String getFpprintSetXml(Map<String, Object> param) {
	String fplxdm = MapUtil.getString(param, "fplxdm");
	String top = MapUtil.getString(param, "top");
	String left = MapUtil.getString(param, "left");

	StringBuffer sb = new StringBuffer();
	sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>")
		.append("<business comment=\"发票打印\" id=\"YBJSZ\">")
		.append("<body  yylxdm=\"1\">")
		.append("<input>")
		.append("<fplxdm>" + fplxdm + "</fplxdm>")
		.append("<top>" + top + "</top>")
		.append("<left>" + left + "</left>")
		.append("</input>")
		.append("</body>")
		.append("</business>");
	return sb.toString();
    }
}



// <?xml version="1.0" encoding="gbk"?>
// <business comment="发票开具" id="FPKJ">
// <body yylxdm="1">
// <input>
// <skpbh>税控盘编号</skpbh>
// <skpkl>税控盘口令</skpkl>
// <keypwd>数字证书密码</keypwd>
// <fplxdm>发票类型代码</fplxdm>
// <kplx>开票类型</kplx>
// <tspz>特殊票种标识<tspz>
// <xhdwsbh>销货单位识别号</xhdwsbh>
// <xhdwmc>销货单位名称</xhdwmc>
// <xhdwdzdh>销货单位地址电话</xhdwdzdh>
// <xhdwyhzh>销货单位银行帐号</xhdwyhzh>
// <ghdwsbh>购货单位识别号</ghdwsbh>
// <ghdwmc>购货单位名称</ghdwmc>
// <ghdwdzdh>购货单位地址电话</ghdwdzdh>
// <ghdwyhzh>购货单位银行帐号</ghdwyhzh>
// <bmbbbh>编码表版本号</bmbbbh>
// <hsslbs>含税税率标识</hsslbs>
// <fyxm count="1">
// <group xh="1">
// <fphxz>发票行性质</fphxz>
// <spmc>商品名称</spmc>
// <spsm>商品税目</spsm>
// <ggxh>规格型号</ggxh>
// <dw>单位</dw>
// <spsl>商品数量</spsl>
// <dj>单价</dj>
// <je>金额</je>
// <kcje>扣除金额</kcje>
// <sl>税率</sl>
// <se>税额</se>
// <hsbz>含税标志</hsbz>
// <spbm>商品编码</spbm>
// <zxbm>纳税人自行编码</zxbm>
// <yhzcbs>优惠政策标识</yhzcbs>
// <slbs>0税率标识</slbs>
// <zzstsgl>增值税特殊管理</zzstsgl>
// </group>
// </fyxm>
// <zhsl></zhsl>
// <hjje>合计金额</hjje>
// <hjse>合计税额</hjse>
// <jshj>价税合计</jshj>
// <bz>备注</bz>
// <skr>收款人</skr>
// <fhr>复核人</fhr>
// <kpr>开票人</kpr>
// <jmbbh>加密版本号</jmbbh>
// <zyspmc>主要商品名称</zyspmc>
// <spsm>商品税目</spsm>
// <qdbz>清单标志</qdbz>
// <ssyf>所属月份</ssyf>
// <kpjh>开票机号</kpjh>
// <tzdbh>通知单编号</tzdbh>
// <yfpdm>原发票代码</yfpdm>
// <yfphm>原发票号码</yfphm>
// <qmcs>签名参数</qmcs>
// </input>
// </body>
// </business>
