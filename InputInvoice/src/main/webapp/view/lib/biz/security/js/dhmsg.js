/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap', 'text!biz/security/dhmsg.html' ], function($, app, bootstrap, tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/security/dhmsg/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr data-nam="'+item['NAME']+'">'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['CNAME']+'</td>'
						+'	<td><p style="height: 75px;overflow: hidden;">'+item['VALUE']+'</p></td>'
						+'	<td>'+item['TYPE']+'</td>'
						+'	<td>'+item['CLASSNAME']+'</td>'
						+'	<td>'+(item['ENABLED'] == 0 ? '启用' : '禁用')+'</td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	};
	
	var buildEvent = function(){
		$('#data-tbody').on('click','tr',function(){
			var b = $(this).attr('data-nam');
			$('#show-tit').html('键：'+ b);
			var c = $(this).find('td').eq(2).find('p').html();
			$('#pre').html(c);
			$('#details').modal('show');
		})
	}
	
	var dhmsg = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return dhmsg;
});