package com.cdhy.entity.message.req;
/**
 * 文本消息
 * 
 * @author liufeng
 * @date 2013-05-19
 */
public class TextMessage extends BaseMessage {
	// 消息内容
	private String Content;
	private String MsgType="text";
	public String getMsgType() {
	    return MsgType;
	}

	public void setMsgType(String msgType) {
	    MsgType = msgType;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}