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
public class XMLUtil_TaxServer2 {

	private XMLUtil_TaxServer2() {
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
	public static Map<String,Object> createSpecialXmlWithMap(Map<String, Object> invoice,
		List<Map<String, Object>> invoice_detail) {
	    	try {
		    Thread.sleep(1);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    	String fpqqlsh = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + ""
	    		    + (new Random().nextInt(9999));// 发票请求流水号
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("business");
		root.addAttribute("id", "10008");
		root.addAttribute("comment", "发票开具");
		Element body = root.addElement("body");
		body.addAttribute("yylxdm", "1");
		body.addElement("kpzdbs").setText(MapUtil.getValue(invoice, "kpzdbs"));
		body.addElement("fplxdm").setText(MapUtil.getValue(invoice, "TYPE"));
		body.addElement("fpqqlsh").setText(fpqqlsh);
		body.addElement("kplx").setText("0");
		body.addElement("tspz").setText(MapUtil.getValue(invoice, "TSPZ"));
		body.addElement("xhdwsbh").setText(MapUtil.getValue(invoice, "XHDWSBH"));
		body.addElement("xhdwmc").setText(MapUtil.getValue(invoice, "XHDWMC"));
		body.addElement("xhdwdzdh").setText(MapUtil.getValue(invoice, "XHDWDZDH"));
		body.addElement("xhdwyhzh").setText(MapUtil.getValue(invoice, "XHDWYHZH"));
		body.addElement("ghdwmc").setText(MapUtil.getValue(invoice, "GHDWMC"));
		body.addElement("ghdwsbh").setText(MapUtil.getValue(invoice, "GHDWSBH"));
		body.addElement("ghdwdzdh").setText(MapUtil.getValue(invoice, "GHDWDZDH"));
		body.addElement("ghdwyhzh").setText(MapUtil.getValue(invoice, "GHDWYHZH"));
		body.addElement("qdbz").setText(MapUtil.getString(invoice, "QDBZ"));
		body.addElement("zsfs").setText(MapUtil.getString(invoice, "ZSFS"));

		Element fyxm = body.addElement("fyxm");
		fyxm.addAttribute("count", String.valueOf(invoice_detail.size()));
		double hjse = 0.0;
		double hjje = 0.0;
		double jshj = 0.0;
		// Attribute count = fyxm.attribute("count");
		int xh=0;
		for (Map<String, Object> detail : invoice_detail) {
			Element group = fyxm.addElement("group");
			xh++;
			group.addAttribute("xh", xh+"");
			group.addElement("fphxz").addText(MapUtil.getString(detail, "FPHXZ"));
			group.addElement("spmc").addText(MapUtil.getString(detail, "SPMC"));
			group.addElement("spsm").addText(MapUtil.getString(detail, "SPSM"));
			group.addElement("ggxh").addText(MapUtil.getString(detail, "GGXH"));
			group.addElement("dw").addText(MapUtil.getString(detail, "DW"));
			group.addElement("spsl").addText(MapUtil.getString(detail, "SPSL"));
			if(MapUtil.getString(detail, "DJ").equals("0")){
			    throw new BizException("商品单价不能为0");
			}
			group.addElement("dj").addText(MapUtil.getString(detail, "DJ"));
			group.addElement("je").addText(MapUtil.getString(detail, "JSHJ"));
			group.addElement("sl").addText(MapUtil.getString(detail, "SL"));
			group.addElement("se").addText(MapUtil.getString(detail, "SE"));
			group.addElement("hsbz").addText(MapUtil.getString(detail, "HSBZ"));
			group.addElement("spbm").addText(MapUtil.getString(detail, "SPBM"));
			group.addElement("zxbm").addText(MapUtil.getString(detail, "ZXBM"));
			group.addElement("yhzcbs").addText(MapUtil.getString(detail, "YHZCBS"));
			group.addElement("lslbs").addText(MapUtil.getString(detail, "LSLBS"));
			group.addElement("zzstsgl").addText("");
			hjse+= Double.parseDouble(MapUtil.getString(detail, "SE"));
			hjje+= Double.parseDouble(MapUtil.getString(detail, "JE"));
		}
		jshj=hjse+hjje;
		body.addElement("hjje").setText(new DecimalFormat("0.00").format(hjje));
		body.addElement("hjse").setText(new DecimalFormat("0.00").format(hjse));
		body.addElement("jshj").setText(new DecimalFormat("0.00").format(jshj));
		body.addElement("kce").setText(MapUtil.getString(invoice, "KCE"));
		body.addElement("bz").setText(MapUtil.getString(invoice, "BZ"));
		body.addElement("skr").setText(MapUtil.getString(invoice, "SKR"));
		body.addElement("fhr").setText(MapUtil.getString(invoice, "FHR"));
		body.addElement("kpr").setText(MapUtil.getString(invoice, "KPR"));
		body.addElement("tzdbh").setText("");
		body.addElement("yfpdm").setText(MapUtil.getString(invoice, "YFPDM"));
		body.addElement("yfphm").setText(MapUtil.getString(invoice, "YFPHM"));

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
		resultMap.put("order_no", invoice.get("ORDER_NO"));
		resultMap.put("id", invoice.get("ID"));
		resultMap.put("xml", writer.toString());
		resultMap.put("fpqqlsh", fpqqlsh);
		resultMap.put("hjje", new DecimalFormat("0.00").format(hjje));
		resultMap.put("hjse", hjse);
		resultMap.put("jshj", new DecimalFormat("0.00").format(jshj));
		return resultMap;
		
	}

	/**
	 * 生成普票开具xml
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String,Object> createPlainXmlWithMap(Map<String, Object> invoice,
			List<Map<String, Object>> invoice_detail) {
	    	try {
		    Thread.sleep(1);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    	String fpqqlsh = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + ""
	    		    + (new Random().nextInt(9999));// 发票请求流水号
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("business");
		root.addAttribute("id", "10008");
		root.addAttribute("comment", "发票开具");
		Element body = root.addElement("body");
		body.addAttribute("yylxdm", "1");
		body.addElement("kpzdbs").setText(MapUtil.getValue(invoice, "kpzdbs"));
		body.addElement("fplxdm").setText(MapUtil.getValue(invoice, "TYPE"));
		body.addElement("fpqqlsh").setText(fpqqlsh);
		body.addElement("kplx").setText("0");
		body.addElement("tspz").setText(MapUtil.getValue(invoice, "TSPZ"));
		body.addElement("xhdwsbh").setText(MapUtil.getValue(invoice, "XHDWSBH"));
		body.addElement("xhdwmc").setText(MapUtil.getValue(invoice, "XHDWMC"));
		body.addElement("xhdwdzdh").setText(MapUtil.getValue(invoice, "XHDWDZDH"));
		body.addElement("xhdwyhzh").setText(MapUtil.getValue(invoice, "XHDWYHZH"));
		body.addElement("ghdwsbh").setText(MapUtil.getString(invoice, "GHDWSBH"));
		body.addElement("ghdwmc").setText(MapUtil.getValue(invoice, "GHDWMC"));
		body.addElement("ghdwdzdh").setText(MapUtil.getString(invoice, "GHDWDZDH"));
		body.addElement("ghdwyhzh").setText(MapUtil.getString(invoice, "GHDWYHZH"));
		body.addElement("qdbz").setText(MapUtil.getString(invoice, "QDBZ"));
		body.addElement("zsfs").setText(MapUtil.getString(invoice, "ZSFS"));

		Element fyxm = body.addElement("fyxm");
		fyxm.addAttribute("count", String.valueOf(invoice_detail.size()));
		double hjse = 0.0;
		double hjje = 0.0;
		double jshj = 0.0;
		// Attribute count = fyxm.attribute("count");
		int xh=0;
		for (Map<String, Object> detail : invoice_detail) {
			Element group = fyxm.addElement("group");
			xh++;
			group.addAttribute("xh", xh+"");
			group.addElement("fphxz").addText(MapUtil.getString(detail, "FPHXZ"));
			group.addElement("spmc").addText(MapUtil.getString(detail, "SPMC"));
			group.addElement("spsm").addText(MapUtil.getString(detail, "SPSM"));
			group.addElement("ggxh").addText(MapUtil.getString(detail, "GGXH"));
			group.addElement("dw").addText(MapUtil.getString(detail, "DW"));
			group.addElement("spsl").addText(MapUtil.getString(detail, "SPSL"));
			if(MapUtil.getString(detail, "DJ").equals("0")){
			    throw new BizException("商品单价不能为0");
			}
			group.addElement("dj").addText(MapUtil.getString(detail, "DJ"));
			group.addElement("je").addText(MapUtil.getString(detail, "JSHJ"));
			group.addElement("sl").addText(MapUtil.getString(detail, "SL"));
			group.addElement("se").addText(MapUtil.getString(detail, "SE"));
			group.addElement("hsbz").addText(MapUtil.getString(detail, "HSBZ"));
			group.addElement("spbm").addText(MapUtil.getString(detail, "SPBM"));
			group.addElement("zxbm").addText(MapUtil.getString(detail, "ZXBM"));
			group.addElement("yhzcbs").addText(MapUtil.getString(detail, "YHZCBS"));
			group.addElement("lslbs").addText(MapUtil.getString(detail, "LSLBS"));
			group.addElement("zzstsgl").addText("");
			hjse+= Double.parseDouble(MapUtil.getString(detail, "SE"));
			hjje+= Double.parseDouble(MapUtil.getString(detail, "JE"));
		}
		jshj=hjse+hjje;
		body.addElement("hjje").setText(new DecimalFormat("0.00").format(hjje));
		body.addElement("hjse").setText(new DecimalFormat("0.00").format(hjse));
		body.addElement("jshj").setText(new DecimalFormat("0.00").format(jshj));
		body.addElement("kce").setText(MapUtil.getString(invoice, "KCE"));
		body.addElement("bz").setText(MapUtil.getString(invoice, "BZ"));
		body.addElement("skr").setText(MapUtil.getString(invoice, "SKR"));
		body.addElement("fhr").setText(MapUtil.getString(invoice, "FHR"));
		body.addElement("kpr").setText(MapUtil.getString(invoice, "KPR"));
		body.addElement("tzdbh").setText("");
		body.addElement("yfpdm").setText(MapUtil.getString(invoice, "YFPDM"));
		body.addElement("yfphm").setText(MapUtil.getString(invoice, "YFPHM"));

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
		resultMap.put("order_no", invoice.get("ORDER_NO"));
		resultMap.put("id", invoice.get("ID"));
		resultMap.put("xml", writer.toString());
		resultMap.put("fpqqlsh", fpqqlsh);
		resultMap.put("hjje", new DecimalFormat("0.00").format(hjje));
		resultMap.put("hjse", hjse);
		resultMap.put("jshj", new DecimalFormat("0.00").format(jshj));
		return resultMap;
		
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
//		String xml=createSetingXmlWithMap(map);
//		System.out.println(xml);
	}
}