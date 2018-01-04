package com.cdhy.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cdhy.entity.SpInfo;
import com.cdhy.exception.BizException;

/**
 * @description 解析xml字符串
 */
public class XMLUtil_TaxServer {

	private XMLUtil_TaxServer() {
		super();
	}

	/**
	 * @description 将申报表xml字符串转换成map
	 * @param xml
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> readStringXmlOut(String xml,
			String targetNodeName) {
		Map<String, Object> map = new HashMap<String, Object>();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML

			Element rootElt = doc.getRootElement(); // 获取根节点

			// System.out.println("根节点：" + rootElt.getName()); //
			// 拿到根节点的名称dzsbcjxx

			List<Element> element = rootElt.element(targetNodeName).elements();
			for (Element itemEle : element) {
				String name = itemEle.getName();
				String text = itemEle.getTextTrim();
				map.put(name, text);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return map;
	}

	/**
	 * 生成设置Xml
	 * 
	 * @param map
	 * @return
	 */
	public static String createSetingXmlWithMap(Map<String, Object> map) {

		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("business");
		root.addAttribute("id", "20001");
		root.addAttribute("comment", "参数设置");
		Element body = root.addElement("body");
		body.addAttribute("yylxdm", "1");
		body.addElement("servletip").setText(MapUtil.getValue(map, "servletip"));
		body.addElement("servletport").setText(MapUtil.getValue(map, "servletport"));
		body.addElement("keypwd").setText(MapUtil.getValue(map, "keypwd"));
		OutputFormat format = OutputFormat.createCompactFormat(); // createPrettyPrint()
																	// // 层次格式化
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);
		try {
			output.write(doc);
			writer.close();
			output.close();
		} catch (IOException e) {
			throw new BizException("生成xml失败！", e);
		}
		return writer.toString();
	}

	/**
	 * 生成查询xml
	 * 
	 * @param map
	 * @return
	 */
	public static String createSelectXmlWithMap(Map<String, Object> map) {

		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("business");
		root.addAttribute("id", "10004");
		root.addAttribute("comment", "查询当前未开票号");
		Element body = root.addElement("body");
		body.addAttribute("yylxdm", "1");
		body.addElement("kpzdbs").setText(MapUtil.getValue(map, "kpzdbs"));
		body.addElement("fplxdm").setText(MapUtil.getValue(map, "fplxdm"));
		OutputFormat format = OutputFormat.createCompactFormat(); // createPrettyPrint()
																	// // 层次格式化
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);
		try {
			output.write(doc);
			writer.close();
			output.close();
		} catch (IOException e) {
			throw new BizException("生成xml失败！", e);
		}
		return writer.toString();
	}

	/**
	 * 生成专票开具xml
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String,Object> createSpecialXmlWithMap(Map<String, Object> map,
			List<SpInfo> list) {
	    	//发票请求流水号
	    	String fpqqlsh=MapUtil.getString(map, "fpqqlsh");
	    	//新创建的xml生成新的发票请求流水号，发票重开的保留原发票号码
	    	if("".equals(fpqqlsh)){
	    	try {
		    Thread.sleep(1);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    	    fpqqlsh = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + ""
	    		    + (new Random().nextInt(9999));// 发票请求流水号
	    	}
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("business");
		root.addAttribute("id", "10008");
		root.addAttribute("comment", "发票开具");
		Element body = root.addElement("body");
		body.addAttribute("yylxdm", "1");
		body.addElement("kpzdbs").setText(MapUtil.getValue(map, "kpzdbs"));
		body.addElement("fplxdm").setText(MapUtil.getValue(map, "fplxdm"));
		body.addElement("fpqqlsh").setText(fpqqlsh);
		body.addElement("kplx").setText(MapUtil.getValue(map, "kplx"));
		body.addElement("tspz").setText(MapUtil.getValue(map, "tspz"));
		body.addElement("xhdwsbh").setText(MapUtil.getValue(map, "xhdwsbh"));
		body.addElement("xhdwmc").setText(MapUtil.getValue(map, "xhdwmc"));
		body.addElement("xhdwdzdh").setText(MapUtil.getValue(map, "xhdwdzdh"));
		body.addElement("xhdwyhzh").setText(MapUtil.getValue(map, "xhdwyhzh"));
		
		body.addElement("ghdwsbh").setText(MapUtil.getValue(map, "ghdwsbh"));
		body.addElement("ghdwmc").setText(MapUtil.getValue(map, "ghdwmc"));
		body.addElement("ghdwdzdh").setText(MapUtil.getValue(map, "ghdwdzdh"));
		body.addElement("ghdwyhzh").setText(MapUtil.getValue(map, "ghdwyhzh"));
		String qdbz="0";
		if(list.size()>8){
		    qdbz="1";
		}
		body.addElement("qdbz").setText(qdbz);
		body.addElement("zsfs").setText("0");

		Element fyxm = body.addElement("fyxm");
		fyxm.addAttribute("count", String.valueOf(list.size()));
		double hjse = 0.0;
		double hjje = 0.0;
		// Attribute count = fyxm.attribute("count");
		for (int i = 0; i < list.size(); i++) {
			Element group = fyxm.addElement("group");
			group.addAttribute("xh", String.valueOf(i+1));
			group.addElement("fphxz").addText(list.get(i).getFphxz());
			group.addElement("spmc").addText(list.get(i).getSpmc());
			group.addElement("spsm").addText(list.get(i).getSpsm());
			group.addElement("ggxh").addText(list.get(i).getGgxh());
			group.addElement("dw").addText(list.get(i).getDw());
			group.addElement("spsl").addText(list.get(i).getSpsl());
			group.addElement("dj").addText(list.get(i).getDj());
			group.addElement("je").addText(list.get(i).getJe());
			group.addElement("sl").addText(list.get(i).getSl());
			group.addElement("se").addText(list.get(i).getSe());
			group.addElement("hsbz").addText(list.get(i).getHsbz());
			group.addElement("spbm").addText(list.get(i).getSpbm());
			group.addElement("zxbm").addText(list.get(i).getZxbm());
			group.addElement("yhzcbs").addText(list.get(i).getYhzcbs());
			group.addElement("lslbs").addText(list.get(i).getLslbs());
			group.addElement("zzstsgl").addText(list.get(i).getZzstsgl());
			hjse += Double.parseDouble(list.get(i).getSe());
			hjje += Double.parseDouble(list.get(i).getJe());
		}
		body.addElement("hjje").setText(String.valueOf(hjje));
		body.addElement("hjse").setText(String.valueOf(hjse));
		body.addElement("jshj").setText(String.valueOf(hjse+hjje));
		body.addElement("kce").setText(MapUtil.getString(map, "kce"));
		body.addElement("bz").setText(MapUtil.getString(map, "bz"));
		body.addElement("skr").setText(MapUtil.getString(map, "skr"));
		body.addElement("fhr").setText(MapUtil.getString(map, "fhr"));
		body.addElement("kpr").setText(MapUtil.getValue(map, "kpr"));
		body.addElement("tzdbh").setText(MapUtil.getString(map, "tzdbh"));
		body.addElement("yfpdm").setText(MapUtil.getString(map, "yfpdm"));
		body.addElement("yfphm").setText(MapUtil.getString(map, "yfphm"));

		OutputFormat format = OutputFormat.createCompactFormat(); // createPrettyPrint()
																	// // 层次格式化
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);
		try {
			output.write(doc);
			writer.close();
			output.close();
		} catch (IOException e) {
			throw new BizException("生成xml失败！", e);
		}
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		resultMap.put("xml", writer.toString());
		resultMap.put("fpqqlsh", fpqqlsh);
		resultMap.put("hjje", hjje);
		resultMap.put("hjse", hjse);
		resultMap.put("jshj", hjje+hjse);
		resultMap.put("qdbz", qdbz);
		
		return resultMap;
		
	}

	/**
	 * 生成普票开具xml
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String,Object> createPlainXmlWithMap(Map<String, Object> map,
			List<SpInfo> list) {
	    	String fpqqlsh=MapUtil.getString(map, "fpqqlsh");
	    	if("".equals(fpqqlsh)){
	    	try {
		    Thread.sleep(1);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    	    fpqqlsh = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + ""
	    		    + (new Random().nextInt(9999));// 发票请求流水号
	    	}
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("business");
		root.addAttribute("id", "10008");
		root.addAttribute("comment", "发票开具");
		Element body = root.addElement("body");
		body.addAttribute("yylxdm", "1");
		body.addElement("kpzdbs").setText(MapUtil.getValue(map, "kpzdbs"));
		body.addElement("fplxdm").setText(MapUtil.getValue(map, "fplxdm"));
		body.addElement("fpqqlsh").setText(fpqqlsh);
		body.addElement("kplx").setText(MapUtil.getValue(map, "kplx"));
		body.addElement("tspz").setText(MapUtil.getValue(map, "tspz"));
		body.addElement("xhdwsbh").setText(MapUtil.getValue(map, "xhdwsbh"));
		body.addElement("xhdwmc").setText(MapUtil.getValue(map, "xhdwmc"));
		body.addElement("xhdwdzdh").setText(MapUtil.getValue(map, "xhdwdzdh"));
		body.addElement("xhdwyhzh").setText(MapUtil.getValue(map, "xhdwyhzh"));
		body.addElement("ghdwsbh").setText(MapUtil.getString(map, "ghdwsbh"));
		body.addElement("ghdwmc").setText(MapUtil.getValue(map, "ghdwmc"));
		body.addElement("ghdwdzdh").setText(MapUtil.getString(map, "ghdwdzdh"));
		body.addElement("ghdwyhzh").setText(MapUtil.getString(map, "ghdwyhzh"));
		body.addElement("qdbz").setText("0");
		body.addElement("zsfs").setText("0");

		Element fyxm = body.addElement("fyxm");
		fyxm.addAttribute("count", String.valueOf(list.size()));
		double hjse = 0.0;
		double hjje = 0.0;
		double jshj = 0.0;
		// Attribute count = fyxm.attribute("count");
		for (int i = 0; i < list.size(); i++) {
			Element group = fyxm.addElement("group");
			group.addAttribute("xh", list.get(i).getXh()+"");
			group.addElement("fphxz").addText(list.get(i).getFphxz());
			group.addElement("spmc").addText(list.get(i).getSpmc());
			group.addElement("spsm").addText(list.get(i).getSpsm());
			group.addElement("ggxh").addText(list.get(i).getGgxh());
			group.addElement("dw").addText(list.get(i).getDw());
			group.addElement("spsl").addText(list.get(i).getSpsl());
			if(list.get(i).getDj().equals("0")){
			    throw new BizException("商品单价不能为0");
			}
			group.addElement("dj").addText(list.get(i).getDj());
			group.addElement("je").addText(list.get(i).getJe());
			group.addElement("sl").addText(list.get(i).getSl());
			group.addElement("se").addText(list.get(i).getSe());
			group.addElement("hsbz").addText(list.get(i).getHsbz());
			group.addElement("spbm").addText(list.get(i).getSpbm());
			group.addElement("zxbm").addText(list.get(i).getZxbm());
			group.addElement("yhzcbs").addText(list.get(i).getYhzcbs());
			group.addElement("lslbs").addText(list.get(i).getLslbs());
			group.addElement("zzstsgl").addText(list.get(i).getZzstsgl());
			hjse+= Double.parseDouble(list.get(i).getSe());
			hjje+= Double.parseDouble(list.get(i).getJe());
		}
		jshj=hjse+hjje;
		body.addElement("hjje").setText(new DecimalFormat("0.00").format(hjje));
		body.addElement("hjse").setText(new DecimalFormat("0.00").format(hjse));
		body.addElement("jshj").setText(new DecimalFormat("0.00").format(jshj));
		body.addElement("kce").setText(MapUtil.getString(map, "kce"));
		body.addElement("bz").setText(MapUtil.getString(map, "bz"));
		body.addElement("skr").setText(MapUtil.getString(map, "skr"));
		body.addElement("fhr").setText(MapUtil.getString(map, "fhr"));
		body.addElement("kpr").setText(MapUtil.getValue(map, "kpr"));
		body.addElement("tzdbh").setText(MapUtil.getString(map, "tzdbh"));
		body.addElement("yfpdm").setText(MapUtil.getString(map, "yfpdm"));
		body.addElement("yfphm").setText(MapUtil.getString(map, "yfphm"));

		OutputFormat format = OutputFormat.createCompactFormat(); // createPrettyPrint()
																	// // 层次格式化
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);
		try {
			output.write(doc);
			writer.close();
			output.close();
		} catch (IOException e) {
			throw new BizException("生成xml失败！", e);
		}
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("xml", writer.toString());
		resultMap.put("fpqqlsh", fpqqlsh);
		resultMap.put("hjje", new DecimalFormat("0.00").format(hjje));
		resultMap.put("hjse", hjse);
		resultMap.put("jshj", new DecimalFormat("0.00").format(jshj));
		resultMap.put("qdbz", 0);
		return resultMap;
		
	}
	public static String createCancelXml(Map<String, Object> map){
	    Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("business");
		root.addAttribute("id", "10009");
		root.addAttribute("comment", "发票作废");
		Element body = root.addElement("body");
		body.addAttribute("yylxdm", "1");
		body.addElement("kpzdbs").setText(MapUtil.getString(map, "kpzdbs"));//开票终端标识
		body.addElement("fplxdm").setText(MapUtil.getString(map, "fplxdm"));//开票终端标识
		body.addElement("zflx").setText("1");//作废类型 0=空白作废 1=已开发票作废
		body.addElement("fpdm").setText(MapUtil.getString(map, "fpdm"));//发票代码
		body.addElement("fphm").setText(MapUtil.getString(map, "fphm"));//发票号码
		body.addElement("hjje").setText(MapUtil.getString(map, "hjje"));//合计金额
		body.addElement("zfr").setText(MapUtil.getString(map, "zfr"));//作废人
		OutputFormat format = OutputFormat.createCompactFormat();
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);
		try {
			output.write(doc);
			writer.close();
			output.close();
		} catch (IOException e) {
			throw new BizException("生成xml失败！", e);
		}
		return writer.toString().replace("UTF-8", "gbk");
	}
	/**
	     * 查询某张发票详情xml
	     * 
	     * @param map
	     * @return
	     */
	    public static String createQueryInvoiceXml(Map<String, Object> map) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("business");
		root.addAttribute("id", "10010");
		root.addAttribute("comment", "发票查询");
		Element body = root.addElement("body");
		body.addAttribute("yylxdm", "1");
		body.addElement("kpzdbs").setText(MapUtil.getString(map, "kpzdbs"));
		body.addElement("fplxdm").setText(MapUtil.getString(map, "fplxdm"));
		body.addElement("cxfs").setText("0");
		body.addElement("cxtj").setText(MapUtil.getString(map, "fpdm") + MapUtil.getString(map, "fphm") + MapUtil.getString(map, "fphm"));

		OutputFormat format = OutputFormat.createCompactFormat();
		StringWriter writer = new StringWriter();
		XMLWriter output = new XMLWriter(writer, format);
		try {
		    output.write(doc);
		    writer.close();
		    output.close();
		} catch (IOException e) {
		    throw new BizException("生成xml失败！", e);
		}
		return writer.toString().replace("UTF-8", "gbk");
	    }
	/**
	 * 通过输入流解析xml
	 * 
	 * @param inputStream
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String, Object> parseXmlWithInputStream(
			InputStream inputStream) throws DocumentException {
		Map<String, Object> result = new HashMap<String, Object>();
		SAXReader sr = new SAXReader();
		Document doc = sr.read(inputStream);
		Element root = doc.getRootElement();
		getElementMap(result, root);
		return result;
	}

	/**
	 * 通过字符串解析xml
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String, Object> parseXmlWithString(String xml)
			throws DocumentException {
		return parseXmlWithInputStream(getStringStream(xml));
	}

	/**
	 * 通过文件解析xml
	 * 
	 * @param xmlFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	public static Map<String, Object> parseXmlWithFile(File xmlFile)
			throws FileNotFoundException, DocumentException {
		return parseXmlWithInputStream(new FileInputStream(xmlFile));
	}

	
	
	
	
	/**
	 * 遍历元素
	 * 
	 * @param result
	 * @param root
	 */
	@SuppressWarnings("unchecked")
	public static void getElementMap(Map<String, Object> result, Element root) {
		List<Element> list = root.elements();
		for (Element e1 : list) {
			if (e1.elements().size() == 0) {
				result.put(e1.getName(), e1.getTextTrim());
			} else {
				getElementMap(result, e1);

			}
		}
	}

	/**
	 * 将字符串转换为输入流
	 * 
	 * @param sInputString
	 * @return
	 */
	public static InputStream getStringStream(String sInputString) {
		if (sInputString != null && !sInputString.trim().equals("")) {
			try {
				ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(
						sInputString.getBytes());
				return tInputStringStream;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将输入流转换为字符串
	 * 
	 * @param tInputStream
	 * @return
	 */
	public static String getStreamString(InputStream tInputStream) {
		if (tInputStream != null) {
			try {
				BufferedReader tBufferedReader = new BufferedReader(
						new InputStreamReader(tInputStream));
				StringBuffer tStringBuffer = new StringBuffer();
				String sTempOneLine = new String("");
				while ((sTempOneLine = tBufferedReader.readLine()) != null) {
					tStringBuffer.append(sTempOneLine);
				}
				return tStringBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
//	public static void main(String[] args) {
//		String xml = "" + "<?xml version=\"1.0\" encoding=\"GB18030\"?>"
//				+ "<dzsbcjxx>" + "<report>" + "<cjhm>LS5A33FEXFB338080</cjhm>"
//				+ "	<fdjhm>F4NB35590</fdjhm>" + "	<cpxh>长安牌SC7162XG</cpxh>"
//				+ "	<cpsm></cpsm>" + "	<dw>12000</dw>" + "	<xcrs></xcrs>"
//				+ "	<pql></pql>" + "	<jkbz>N</jkbz>"
//				+ "	<ccrq>2015/10/12</ccrq>" + "	<mc></mc>" + "	<dh></dh>"
//				+ "	<dz></dz>" + "	<jbr></jbr>" + "	<kprq>2015-11-18</kprq>"
//				+ "	<zgswjg_dm>122019500</zgswjg_dm>"
//				+ "	<nsr_sbh>91220101MA0Y342M8B</nsr_sbh>"
//				+ "	<fpdm>122011521706</fpdm>" + "	<fphm>00166849</fphm>"
//				+ "	<jshj>100000.00</jshj>" + "	<jwfy>0.00</jwfy>"
//				+ "	<gsws_jg></gsws_jg>" + "	<gs>0.00</gs>"
//				+ "	<xfs>0.00</xfs>" + "	<zzssl>.17</zzssl>"
//				+ "	<ghdw>孙迎春</ghdw>" + "	<nsr_zjzl_dm>10</nsr_zjzl_dm>"
//				+ "	<sfzhm>220111197204032811</sfzhm>"
//				+ "	<nsr_yb>130000</nsr_yb>"
//				+ "	<nsr_dz>长春市南关区新湖镇大南街道委7组</nsr_dz>"
//				+ "	<nsr_dh>13596109233</nsr_dh>"
//				+ "	<nsr_djzclx_dm>160</nsr_djzclx_dm>"
//				+ "	<nsr_hy_dm>51</nsr_hy_dm>" + "	<fphsbz>1</fphsbz>"
//				+ "	<ytyfpbz>Y</ytyfpbz>" + "</report>" + "</dzsbcjxx>";
//
//		Map<String, Object> map = readStringXmlOut(xml, "report");
//		System.out.println(map);
//		System.out.println(map.size());
//	}
	public static void main(String[] args) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("servletip", "111");
		map.put("servletport", "112");
		map.put("keypwd", "113");
		String xml=createSetingXmlWithMap(map);
		System.out.println(xml);
	}
}