package com.cdhy.entity.message.req;
/**
 * 图片消息
 * 
 * @author liufeng
 * @date 2013-05-19
 */
public class ImageMessage extends BaseMessage {
	// 图片链接
	private String PicUrl;
	// 消息类型（text/image/location/link）
	@SuppressWarnings("unused")
	private String MsgType="imgae";
	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}