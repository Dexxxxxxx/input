package com.cdhy.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @description 解析xml字符串
 */
public class XMLUtil {

    /**
     * @description 将申报表xml字符串转换成map
     * @param xml
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> readStringXmlOut(String xml, String targetNodeName) {
	Map<String, Object> map = new HashMap<String, Object>();
	Document doc = null;
	try {
	    doc = DocumentHelper.parseText(xml); // 将字符串转为XML

	    Element rootElt = doc.getRootElement(); // 获取根节点

	    //System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称dzsbcjxx

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

    public static void main(String[] args) {
	String xml = "" + "<?xml version=\"1.0\" encoding=\"GB18030\"?>" + "<dzsbcjxx>" + "<report>"
		+ "<cjhm>LS5A33FEXFB338080</cjhm>" + "	<fdjhm>F4NB35590</fdjhm>" + "	<cpxh>长安牌SC7162XG</cpxh>"
		+ "	<cpsm></cpsm>" + "	<dw>12000</dw>" + "	<xcrs></xcrs>" + "	<pql></pql>"
		+ "	<jkbz>N</jkbz>" + "	<ccrq>2015/10/12</ccrq>" + "	<mc></mc>" + "	<dh></dh>"
		+ "	<dz></dz>" + "	<jbr></jbr>" + "	<kprq>2015-11-18</kprq>"
		+ "	<zgswjg_dm>122019500</zgswjg_dm>" + "	<nsr_sbh>91220101MA0Y342M8B</nsr_sbh>"
		+ "	<fpdm>122011521706</fpdm>" + "	<fphm>00166849</fphm>" + "	<jshj>100000.00</jshj>"
		+ "	<jwfy>0.00</jwfy>" + "	<gsws_jg></gsws_jg>" + "	<gs>0.00</gs>" + "	<xfs>0.00</xfs>"
		+ "	<zzssl>.17</zzssl>" + "	<ghdw>孙迎春</ghdw>" + "	<nsr_zjzl_dm>10</nsr_zjzl_dm>"
		+ "	<sfzhm>220111197204032811</sfzhm>" + "	<nsr_yb>130000</nsr_yb>"
		+ "	<nsr_dz>长春市南关区新湖镇大南街道委7组</nsr_dz>" + "	<nsr_dh>13596109233</nsr_dh>"
		+ "	<nsr_djzclx_dm>160</nsr_djzclx_dm>" + "	<nsr_hy_dm>51</nsr_hy_dm>" + "	<fphsbz>1</fphsbz>"
		+ "	<ytyfpbz>Y</ytyfpbz>" + "</report>" + "</dzsbcjxx>";

	Map<String, Object> map = readStringXmlOut(xml,"report");
	System.out.println(map);
	System.out.println(map.size());
    }

}