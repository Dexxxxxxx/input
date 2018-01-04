/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap', 'text!biz/security/datahandle.html' ], function($, app, bootstrap, tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/j3mgr/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr>'
						+'	<td>'+item['PARAMCODE']+'</td>'
						+'	<td>'+item['PARAMNAME']+'</td>'
						+'	<td>'+item['PARAMVALUE']+'</td>'
						+'	<td>'+item['PARAMDESC']+'</td>'
						+'	<td>'
						+'		<div class="am-btn-toolbar">'
					    +'			<button class="btn green btn-sm" data-id="'+item['PARAMCODE']+'">'
					    +'			编辑 <i class="fa fa-edit"></i>'
					    +'			</button>'
						+'		</div>'
						+'	</td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	};
	
	var buildEvent = function(){
		$('#data-tbody').on('click','button',function(){
			var btn = $(this);
			if(btn.attr('data-id') != null && btn.attr('data-id') != ''){
				app._function = 'UPDATE';
				app.paramcode = btn.attr('data-id');
				app.loadPage('datahandle_view');
			}else{
				alert('未选择查看的信息');
			}
		});
		/*$('#btn-new').click(function(){
			app._function = 'ADD';
			app.loadPage('datahandle_view');
		});
		$('#btn-del').on('click',function(){
			var paramcode = [];
			$('.checkboxes').each(function(){
				if($(this).is(':checked') == true){
					paramcode.push($(this).val());
				}
			});
			if(confirm("确定删除？")){
				app.ajax({
					url: app.rootPath + '/j3mgr/delete.do',
					data: 'paramcodes=' + paramcode,
					success: function(data){
						alert('删除成功');
						loadData();
					}
				});
			}
		});*/
		$('#btn-data').on('click',function(){
			if(confirm("确定操作资源同步？")){
				app.ajax({
					url: app.rootPath + '/j3mgr/invokeByhandle.do',
					success: function(data){
						alert('同步成功');
					}
				});
			}
		});
		$('#check_group').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.checkboxes').prop('checked',isChecked);
		});
	}
	
	var datahandle = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return datahandle;
});