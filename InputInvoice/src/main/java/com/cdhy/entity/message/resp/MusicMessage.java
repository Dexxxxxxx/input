package com.cdhy.entity.message.resp;

/**
 * 音乐消息
 * 
 * @author liufeng
 * @date 2013-05-19
 */
public class MusicMessage extends BaseMessage {
    // 音乐
    private Music Music;

    // 消息类型（text/music/news）
    private String MsgType = "music";

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public Music getMusic() {
	return Music;
    }

    public void setMusic(Music music) {
	Music = music;
    }
}