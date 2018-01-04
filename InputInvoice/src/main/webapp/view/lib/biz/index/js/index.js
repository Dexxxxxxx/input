/**
 * 
 */
define([ 'jquery', 'app', 'highcharts', 'text!biz/index/index.html' ], function($, app, highcharts, tpl) {
	app.skStatus='0';
	app.kpzdbs='';
	app.skMsg='税控钥匙控件未加载成功';
	var loadData = function(){
	};
	
	var highchart = function(_data){
	};
	//执行xml
	var xmlCheck=function(sInputInfo){
//		try
//		{
//			ret = sk.Operate(sInputInfo);
//			return ret;
//		}
//		catch(e)
//		{
//			alert(e.message + ",error:" + e.number);
//			alert(app.skMsg);
//		}
	}
	var setting = function(){
		var backInfo='';
		var sInputInfo = ''
			$.ajax({
				url:app.rootPath + '/cigarette/common/setting.do',
				type:'POST',
				async:false,
				contentType: "application/json; charset=utf-8",
				success:function(data){
					if(data['result']=='SUCCESS'){
						sInputInfo=data['rows']['xml'];
						backInfo=xmlCheck(sInputInfo);
						var _backInfo=parseXML(backInfo);
						var ss=_backInfo.getElementsByTagName("returncode")[0].childNodes[0].nodeValue;
						if(ss==0){
							//参数设置成功
							app.skStatus='1';
							app.kpzdbs=data['rows']['kpzdbs'];
						}else{
							//参数设置失败
							alert(_backInfo.getElementsByTagName("returnmsg")[0].childNodes[0].nodeValue);
						}
					}else{
						alert("参数错误！")
					}
				},
				error:function(data){
					alert(data);
					
				}
			});
	}
	function parseXML(ssxml){
		  try{
	        xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
	        xmlDoc.async="false";
	        xmlDoc.loadXML(ssxml);
	        if(xmlDoc!=null){
	        	return xmlDoc;
	        }
		  	}catch(e){
		  		try {
		  			parser=new DOMParser();
		  			xmlDoc=parser.parseFromString(ssxml,"text/xml");
		  			if(xmlDoc!=null){
		  				return xmlDoc;
		  			}
		  		}catch(e) {
		  			alert('解析xml错误 ： '+ e.message);
		  			return;
		  		}
		  	}
	}
	var index = {
		init : function() {
			app.mainContainer.html(tpl);
			loadData();
//			setting();
		}
	};

	return index;
});