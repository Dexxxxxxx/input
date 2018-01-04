/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap', 'My97DatePicker','text!biz/security/dhlog.html' ], function($, app, bootstrap, My97DatePicker, tpl) {
	Date.prototype.Format = function (fmt) { 
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	};
	var loadData = function(){
		$('#start_date').val(new Date(new Date().getTime() - 3600 * 1000).Format("yyyy-MM-dd hh:mm"));
		new app.Page({
			url : app.rootPath + '/security/dhlog/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					if(null == item['EXCEPTION_MESSAGE']){
						item['EXCEPTION_MESSAGE'] = '无';
					}
					html += ''
						+'<tr data-nam="'+item['NAME']+'">'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['TRAN_ID']+'</td>'
						+'	<td>'+item['REQUEST_TIME']+'</td>'
						+'	<td><p style="height: 75px;overflow: hidden;">'+item['REQUEST_MESSAGE']+'</p></td>'
						+'	<td>'+item['SEND_TIME']+'</td>'
						+'	<td><p style="height: 75px;overflow: hidden;">'+item['SEND_MESSAGE']+'</p></td>'
						+'	<td>'+item['RECEIVE_TIME']+'</td>'
						+'	<td><p style="height: 75px;overflow: hidden;">'+item['RECEIVE_MESSAGE']+'</p></td>'
						+'	<td>'+item['RESPONSE_TIME']+'</td>'
						+'	<td><p style="height: 75px;overflow: hidden;">'+item['RESPONSE_MESSAGE']+'</p></td>'
						+'	<td><p style="height: 75px;overflow: hidden;">'+item['EXCEPTION_MESSAGE']+'</p></td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	};
	
	var buildEvent = function(){
		$('#data-tbody').on('click','tr',function(){
			var html = '';
			var b = $(this).attr('data-nam');
			$('#show-tit').html('键：'+ b);
			var z_1 = $(this).find('td').eq(3).find('p').html();
			var z_2 = $(this).find('td').eq(5).find('p').html();
			var z_3 = $(this).find('td').eq(7).find('p').html();
			var z_4 = $(this).find('td').eq(9).find('p').html();
			var z_5 = $(this).find('td').eq(10).find('p').html();
			html += ''
				+ '<span style="height:15px;">请求原始数据:</span>'
				+ '<pre style="height:260px;overflow-y:auto;">'+z_1+'</pre><hr/>'
				+ '<span style="height:15px;">调用接口原始报文:</span>'
				+ '<pre style="height:260px;overflow-y:auto;">'+z_2+'</pre><hr/>'
				+ '<span style="height:15px;">调用接口响应报文:</span>'
				+ '<pre style="height:260px;overflow-y:auto;">'+z_3+'</pre><hr/>'
				+ '<span style="height:15px;">请求响应报文:</span>'
				+ '<pre style="height:260px;overflow-y:auto;">'+z_4+'</pre><hr/>'
				+ '<span style="height:15px;">异常:</span>'
				+ '<pre style="height:260px;overflow-y:auto;">'+z_5+'</pre><hr/>';
			$('#pre').html(html);
			$('#details').modal('show');
		})
	}
	
	var dhlog = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return dhlog;
});