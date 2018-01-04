package com.cdhy.entity.message.req;
/**
 * 音频消息
 * 
 * @author liufeng
 * @date 2013-05-19
 */
public class VoiceMessage extends BaseMessage {
	// 媒体ID
	private String MediaId;
	// 语音格式
	private String Format;

	// 消息类型（text/image/location/link）
	@SuppressWarnings("unused")
	private String MsgType="voice";
	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}
}