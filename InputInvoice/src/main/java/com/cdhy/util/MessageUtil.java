package com.cdhy.util;

import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import com.cdhy.entity.message.Message;
import com.cdhy.entity.message.resp.Article;
import com.cdhy.entity.message.resp.Image;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 消息工具类
 * 
 * @author liufeng
 * @date 2013-05-19
 */
public class MessageUtil {

    /**
     * 返回消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * 返回消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(订阅)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";

    /**
     * 解析XML
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseXmlFromString(String xmlStr) throws Exception {
	// 将解析结果存储在HashMap中
	Map<String, Object> map = new HashMap<String, Object>();
	StringReader sr = new StringReader(xmlStr);
	InputSource is = new InputSource(sr);
	// 读取输入流
	SAXReader reader = new SAXReader();
	Document document = reader.read(is);
	// 得到xml根元素
	Element root = document.getRootElement();
	// 得到根元素的所有子节点
	List<Element> elementList = root.elements();

	Map<String, Object> result = new HashMap<>();
	// 遍历所有子节点
	result = element2Map(elementList, map);
	sr = null;
	is = null;
	return result;
    }

    /**
     * 解析微信发来的请求（XML）
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseXml(HttpServletRequest request) throws Exception {
	// 将解析结果存储在HashMap中
	Map<String, Object> map = new HashMap<String, Object>();

	// 从request中取得输入流
	InputStream inputStream = request.getInputStream();
	// 读取输入流
	SAXReader reader = new SAXReader();
	Document document = reader.read(inputStream);
	// 得到xml根元素
	Element root = document.getRootElement();
	// 得到根元素的所有子节点
	List<Element> elementList = root.elements();

	Map<String, Object> result = new HashMap<>();
	// 遍历所有子节点
	result = element2Map(elementList, map);

	// 释放资源
	inputStream.close();
	inputStream = null;

	return result;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> element2Map(List<Element> list, Map<String, Object> map) {
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

    /**
     * 文本消息对象转换成xml
     * 
     * @param textMessage
     *            文本消息对象
     * @return xml
     */
    public static String textMessageToXml(Message textMessage) {
	xstream.alias("xml", textMessage.getClass());
	return xstream.toXML(textMessage);
    }

    /**
     * 音乐消息对象转换成xml
     * 
     * @param musicMessage
     *            音乐消息对象
     * @return xml
     */
    public static String musicMessageToXml(Message musicMessage) {
	xstream.alias("xml", musicMessage.getClass());
	return xstream.toXML(musicMessage);
    }

    /**
     * 图文消息对象转换成xml
     * 
     * @param newsMessage
     *            图文消息对象
     * @return xml
     */
    public static String newsMessageToXml(Message newsMessage) {
	xstream.alias("xml", newsMessage.getClass());
	xstream.alias("item", new Article().getClass());
	return xstream.toXML(newsMessage);
    }
    
    /**
     * 图文消息对象转换成xml
     * 		获取服务器上的图片id
     * @param imgMessage
     *            图文消息对象
     * @return xml
     */
    public static String imgMessageToXml(Message imgMessage) {
	xstream.alias("xml", imgMessage.getClass());
//	xstream.alias("item", new Image().getClass());
	return xstream.toXML(imgMessage);
    }

    /**
     * 扩展xstream，使其支持CDATA块
     * 
     * @date 2013-05-19
     */
    private static XStream xstream = new XStream(new XppDriver() {
	public HierarchicalStreamWriter createWriter(Writer out) {
	    return new PrettyPrintWriter(out) {
		// 对所有xml节点的转换都增加CDATA标记
		boolean cdata = true;

		@SuppressWarnings("rawtypes")
		public void startNode(String name, Class clazz) {
		    super.startNode(name, clazz);
		}

		protected void writeText(QuickWriter writer, String text) {
		    if (cdata) {
			writer.write("<![CDATA[");
			writer.write(text);
			writer.write("]]>");
		    } else {
			writer.write(text);
		    }
		}
	    };
	}
    });
}