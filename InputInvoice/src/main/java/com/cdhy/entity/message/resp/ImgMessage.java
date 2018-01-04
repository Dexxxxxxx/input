package com.cdhy.entity.message.resp;

/**
 * 图文本消息
 * 
 * @author ddc
 * @date
 */
public class ImgMessage extends BaseMessage {
    private Image Image;
    
    // 消息类型（image）
    private String MsgType = "image";
    

    public Image getImage() {
    return Image;
    }

    public void setImage(Image image) {
    Image = image;
    }

    public String getMsgType() {
    return MsgType;
    }

    public void setMsgType(String msgType) {
    MsgType = msgType;
    }

}
