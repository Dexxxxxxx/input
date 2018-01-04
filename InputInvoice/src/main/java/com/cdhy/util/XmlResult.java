package com.cdhy.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import com.cdhy.exception.BizException;

public class XmlResult {
	public static Map<String, Object> check(Map<String, Object> param) {
		String xml = MapUtil.getString(param, "xml");
		Map<String, Object> resp_result = new HashMap<>();
		try {
			// System.out.println(xml);
			resp_result = MessageUtil.parseXmlFromString(xml);
			resp_result = MapUtil.mapKey2Lower(resp_result);
		} catch (Exception e) {
			throw new BizException(xml);
		}
		return resp_result;
	}
	/**
	     * 解析发票查验XML
	     * 
	     * @param request
	     * @return
	     * @throws Exception
	     */
	    @SuppressWarnings("unchecked")
	    public static Map<String, Object> parseInvoiceXmlFrom(String xmlStr) throws Exception {
		int kpxxCount = 1;
		int fyxmCount = 1;
		// 将解析结果存储在HashMap中
		Map<String, Object> map = new HashMap<String, Object>();
		StringReader sr = new StringReader(xmlStr);
		InputSource is = new InputSource(sr);
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(is);

		// 得到xml根元素
		Element root = document.getRootElement();
		Element body = root.element("body");
		String returncode = body.elementTextTrim("returncode");
		String returnmsg = body.elementTextTrim("returnmsg");
		if (!"0".equals(returncode)) {
		    throw new BizException(returnmsg);
		}
		Element returndata = body.element("returndata");
		Element kpxx = returndata.element("kpxx");
		String kpxxCount1 = kpxx.attributeValue("count");
		if ("0".equals(kpxxCount1)) {
		    throw new BizException("没有发票数据");
		} else {
		    kpxxCount = Integer.valueOf(kpxxCount1);
		}
		Element fpgroup = kpxx.element("group");
		Map<String, Object> invoice = new HashMap<>();
		List<Map<String, Object>> invoice_detail = new ArrayList<>();
		Iterator<Element> it = fpgroup.elementIterator();
		while (it.hasNext()) {
		    Element invEle = it.next();
		    String eleName = invEle.getName();
		    if ("fyxm".equals(eleName)) {
			Iterator<Element> fyxmGroup = invEle.elementIterator("group");
			while (fyxmGroup.hasNext()) {
			    Element invFyxm = fyxmGroup.next();
			    invoice_detail.add(element2Map(invFyxm.elements(), new HashMap<String, Object>()));
			}
		    }
		    invoice.put(eleName, invEle.getText());
		}
		invoice.put("invoice_detail", invoice_detail);
		return invoice;
	    }
	    @SuppressWarnings("unchecked")
	    private static Map<String, Object> element2Map(List<Element> list, Map<String, Object> map) {
		// 遍历所有子节点
		for (Element e : list) {
		    if (e.elements() != null && e.elements().size() > 0) {
			List<Element> elements = e.elements();
			map.put(e.getName(), element2Map(elements, map));
		    } else {
			map.put(e.getName(), e.getText());
		    }
		}
		return map;
	    }

	
}
