package com.cdhy.comms;

public interface IConstants {

    String USERLOGIN_URL = null;
    int TIMEOUT = 5000;
    /* 消息体参数 */

    /* 开发者微信号 */
    String MESSAGE_ToUserName = "ToUserName";
    /* 发送方帐号（一个OpenID） */
    String MESSAGE_FromUserName = "FromUserName";
    /* 消息创建时间 （整型） */
    String MESSAGE_CreateTime = "CreateTime";
    /* 消息类型，event */
    String MESSAGE_MsgType = "MsgType";
    /* 事件类型，CLICK */
    String MESSAGE_Event = "Event";
    /* 事件KEY值，与自定义菜单接口中KEY值对应 */
    String MESSAGE_EventKey = "EventKey";

    /* 指菜单ID，如果是个性化菜单，则可以通过这个字段，知道是哪个规则的菜单被点击了 */
    String MESSAGE_MenuID = "MenuID";

    /* 扫描信息 */
    String MESSAGE_ScanCodeInfo = "ScanCodeInfo";
    /* 扫描类型，一般是qrcode */
    String MESSAGE_ScanType = "ScanType";
    /* 扫描结果，即二维码对应的字符串信息 */
    String MESSAGE_ScanResult = "ScanResult";

    /* 发送的图片信息 */
    String MESSAGE_SendPicsInfo = "SendPicsInfo";

    /* 发送的图片数量 */
    String MESSAGE_Count = "Count";
    /* 图片列表 */
    String MESSAGE_PicList = "PicList";
    /* 图片的MD5值，开发者若需要，可用于验证接收到图片 */
    String MESSAGE_PicMd5Sum = "PicMd5Sum";

    /* 发送的位置信息 */
    String MESSAGE_SendLocationInfo = "SendLocationInfo";
    /* X坐标信息 */
    String MESSAGE_Location_X = "Location_X";
    /* Y坐标信息 */
    String MESSAGE_Location_Y = "Location_Y";
    /* 精度，可理解为精度或者比例尺、越精细的话 scale越高 */
    String MESSAGE_Scale = "Scale";
    /* 地理位置的字符串信息 */
    String MESSAGE_Label = "Label";
    /* 朋友圈POI的名字，可能为空 */
    String MESSAGE_Poiname = "Poiname";
    String ACCSEE_TOKEN = null;

    String TYPE_WX = "wx";
    String TYPE_IOS = "IOS";
    String TYPE_ANDROID = "Android";

    /**
     * 企业用户
    */
    String YHLX_ENT = "0";

    /**
     * 个人用户
     */
    String YHLX_PER = "1";
    /**
     * 个人用户
     */
    String YHLX_ENTC = "2";
    
    /**
     * 开票方的类型 销货方
     */
    String KPTYPE_XHDW = "0";
    
    /**
     * 开票方的类型 购货方
     */
    String KPTYPE_GHDW = "1";
    
    /**
     * 申请状态(0/1/2/3)(0:未申请认证；1:已结申请，但还未批准；2；申请成功；3：认证申请失败)
     */
    String SQZT_0 = "0";
    /**
     * 申请状态(0/1/2/3)(0:未申请认证；1:已结申请，但还未批准；2；申请成功；3：认证申请失败)
     */
    String SQZT_1 = "1";
    /**
     * 申请状态(0/1/2/3)(0:未申请认证；1:已结申请，但还未批准；2；申请成功；3：认证申请失败)
     */
    String SQZT_2 = "2";
    /**
     * 申请状态(0/1/2/3)(0:未申请认证；1:已结申请，但还未批准；2；申请成功；3：认证申请失败)
     */
    String SQZT_3 = "3";
    

}
