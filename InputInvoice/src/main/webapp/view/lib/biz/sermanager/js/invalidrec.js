/**
 * 
 */
define([ 'jquery', 'app','bootstrap', 'My97DatePicker', 'text!biz/sermanager/invalidrec.html'], function($, app, bootstrap,My97DatePicker, tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/sermanager/invalidrec/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr>'
						+'	<td>'+item['DEPTNAME']+'</td>'
						+'	<td>'+item['TAMID']+'</td>'
						+'	<td>'+item['TAMNAME']+'</td>'
						+'	<td>'+item['PZZLDM']+'</td>'
						+'	<td>'+item['PZHM']+'</td>'
						+'	<td>'+item['PZZG']+'</td>'
						+'	<td>'+item['CREATETIME']+'</td>'
						+'	<td>'+item['MODIFYTIME']+'</td>'
						+'	<td>'+(item['ISDELETE'] == 0? "正常使用" : "作废")+'</td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	};
	
	var buildEvent = function(){
		$('.datePicker').click(function(){
			WdatePicker();
		});
		 $('#reSetTable').on('click',function(){
			$('#search_key').val("");
			$('#isdelete').val("999");
			$('#start_date').val("");
			$('#end_date').val("");
			loadData();
		});
	};
	
	var invalidrec = {
			init : function() {
				app.mainContainer.html(tpl);
				buildEvent();
				loadData();
			}
		};
	return invalidrec;
});