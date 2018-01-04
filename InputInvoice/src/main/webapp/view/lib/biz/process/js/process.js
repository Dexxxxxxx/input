/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap', 'jqueryForm','text!biz/process/process.html' ], function($, app, bootstrap, jqueryForm,tpl) {
	var loadData = function(){
		app.ajax({
			url : app.rootPath + '/process/list.do',
			type : 'POST',
			data : '',
			success : function(data){
				var html = buildHtml(data);			
				$('#data-main').html(html);
			},
			error : function(data){
				
			}
		});
	};
	
	var buildEvent = function(){
		$('#data-main').on('click','tr',function(){
			var type = $(this).attr("data-type");
			if(type == 'task' || type == 'item'){
				var dataid = $(this).attr("data-id");
				app.ajax({
					url : app.rootPath + '/process/getById.do',
					type : 'POST',
					data : 'type='+type+'&id=' + dataid,
					success : function(data){
						for(var key in data){
							$('#process_form input[name='+key+']').val(data[key]);
							$('#process_form select[name='+key+']').val(data[key]);
							$('#process_form textarea[name='+key+']').val(data[key]);
						}
						$('#ajax-modal-nopass').modal('show');
						if(type == 'task'){
							$('#process_form select[name=DOCONTINUE]').attr("disabled","disabled");
							$('#process_form select[name=DISPLAYD]').removeAttr("disabled","disabled");
							$('#process_form input[name=DISNAME]').removeAttr("disabled","disabled");
						} else {
							$('#process_form select[name=DOCONTINUE]').removeAttr("disabled","disabled");
							$('#process_form select[name=DISPLAYD]').attr("disabled","disabled");
							$('#process_form input[name=DISNAME]').attr("disabled","disabled");
						}
					},
					error : function(data){
						alert('获取失败');
					}
				});
				
				
				
			}
		});
		$('#fomr_btn_submit').click(function(){
			$('#process_form').attr("action",app.rootPath + "/process/update.do").ajaxSubmit({
		        success : function(data){
		        	data = JSON.parse(data);
		        	if(data['result'] == 'SUCCESS'){
						loadData();
						$('#ajax-modal-nopass').modal('hide');
					} else if(data['result'] == 'ERROR'){
						alert(data['msg']);
					}
		      	},
		      	error : function(xhr, errorType, error) {
		      	   if(xhr['status'] == '600'){
						alert('未登录!');
						window.location.href = "login.html";
					} else if(xhr['status'] == '401'){
						alert('您没有该操作的访问权限!');
					} else {
						_ajax_config.error('出错了! error:'  + xhr['status']);
					}
				}
	       });
		});
	};
	
	
	var buildHtml = function(data){
		var html = '';
			$.each(data, function(i, item) {
				if(item['TP'] == 'TASK'){
					html += '<div class="col-md-12 process_main">' 
						 + '<div class="col-md-12">'
						 +'		<table class="table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer">'
						 +'			<tr>'
						 +'				<th>流程类型</th>'
						 +'				<th>执行顺序</th>'
						 +'				<th>任务名称</th>'
						 +'				<th>客户端名</th>'
						 +'				<th>任务代码</th>'
						 +'				<th>是否启用</th>'
						 +'				<th>是否显示</th>'
						 +'				<th>是否可跳过</th>'
						 +'				<th>对应bean</th>'
						 +'				<th>说明</th>'
						 +'			</tr>'
						 +'			<tr data-type="task" data-id="'+item['ID']+'">'
						 +'				<td>'+item['TP']+'</td>'
						 +'				<td>'+item['BYORDER']+'</td>'
						 +'				<td>'+item['NAME']+'</td>'
						 +'				<td>'+item['DISNAME']+'</td>'
						 +'				<td>'+item['CODE']+'</td>'
						 +'				<td>'+(item['DISABLED'] == 0? "启用" : "禁用")+'</td>'
						 +'				<td>'+(item['DISPLAYD'] == 0? "显示" : "影藏")+'</td>'
						 +'				<td>不可以</td>'
						 +'				<td>'+item['BEAN']+'</td>'
						 +'				<td>'+item['REMARKS']+'</td>'
						 +'			</tr>'
						 +'		</table>'
						 +'	 </div>';
					html += getChild(item, data);
					html += '</div>';
				}
			});
			
		return html;
	};
	var getChild = function (pitem, data){
		var html = ''
			+'<div class="col-md-12">'
			+'	<table class="table table-striped table-bordered table-hover table-checkable order-column dataTable no-footer">'
			+'			<tr>'
			+'				<th>流程类型</th>'
			+'				<th>执行顺序</th>'
			+'				<th>任务名称</th>'
			+'				<th>客户端名</th>'
			+'				<th>任务代码</th>'
			+'				<th>是否启用</th>'
			+'				<th>是否显示</th>'
			+'				<th>是否可跳过</th>'
			+'				<th>对应bean</th>'
			+'				<th>说明</th>'
			+'			</tr>';
			$.each(data, function(i, item) {
				if(item['TP'] == 'ITEM' && item['TASKID'] == pitem['ID']){
					html +='		<tr data-type="item" data-id="'+item['ID']+'">'
						 +'			<td>'+item['TP']+'</td>'
						 +'			<td>'+item['BYORDER']+'</td>'
						 +'			<td>'+item['NAME']+'</td>'
						 +'			<td>'+item['DISNAME']+'</td>'
						 +'			<td>'+item['CODE']+'</td>'
						 +'			<td>'+(item['DISABLED'] == 0? "启用" : "禁用")+'</td>'
						 +'			<td>影藏</td>'
						 +'			<td>'+(item['DOCONTINUE'] == 0? "可以" : "不可以")+'</td>'
						 +'			<td>'+item['BEAN']+'</td>'
						 +'			<td>'+item['REMARKS']+'</td>'
						 +'		</tr>';
				}
			});
			
			html +='	</table></div>';
		return html;
	};
	
	var process = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return process;
});