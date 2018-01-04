/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap', 'text!biz/security/validate.html' ], function($, app, bootstrap, tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/security/validate/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					if(null == item['PREID']){
						item['PREID'] = '无'
					}
					html += ''
						+'<tr>'
						+'	<td>'+item['MSGNAME']+'</td>'
						+'	<td>'+item['CNAME']+'</td>'
						+'	<td>'+item['PARAMS']+'</td>'
						+'	<td>'+item['RULE']+'</td>'
						+'	<td>'+item['PREID']+'</td>'
						+'	<td>'+(item['ISPRE'] == 0 ? '否' : '是') +'</td>'
						+'	<td>'+(item['ENABLED'] == 0 ? '启用' : '禁用')+'</td>'
						+'	<td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	};
	
	var buildEvent = function(){
		
	}
	
	var validate = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return validate;
});