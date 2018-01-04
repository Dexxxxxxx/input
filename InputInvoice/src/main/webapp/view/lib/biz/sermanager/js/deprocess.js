/**
 * 
 */
define([ 'jquery', 'app','bootstrap','My97DatePicker', 'text!biz/sermanager/deprocess.html'], function($, app, bootstrap,My97DatePicker, tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/sermanager/deprocess/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					var status=item['STATUS'];
					item['STATUS']=(item['STATUS']=='0'?'正常':(item['STATUS']=='999'?'完成':'业务删除'));
					item['PROCESS_STATUS']=(item['PROCESS_STATUS']=='SUCCESS'?'成功':'失败');
					html += ''
						+'<tr data-tr-id = '+item['ID']+'>'
						+'	<td>'+item['TAM_ID']+'</td>'
						+'	<td>'+item['TAMNAME']+'</td>'
						+'	<td>'+item['USERID']+'</td>'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['NSRSBH']+'</td>'
						+'	<td>'+item['SFZJLXDM']+'</td>'
						+'	<td>'+item['VIN']+'</td>'
						+'	<td>'+item['MMODEL']+'</td>'
						+'	<td>'+item['PROCESS_NAME']+'</td>'
						+'	<td>'+item['STATUS']+'</td>'
						+'	<td>'+item['PROCESS_STATUS']+'</td>'
						+'	<td>'+item['CREATETIME']+'</td>'
						+'	<td>'
						+'		<div class="am-btn-toolbar">'
					    +'			<button class="btn green btn-sm" btn-type="tails" data-id="'+item['ID']+'" data-state="'+status+'">'
					    +'			查看详情'
					    +'			</button>'
					    +'			<button class="btn green btn-sm" btn-type="del" data-id="'+item['ID']+'"  data-state="'+status+'">'
					    +'			删除'
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
		/*$('#btn-del').click(function(){
			app.loadPage("deprocess_view");
		});*/
		$('.datePicker').click(function(){
			WdatePicker();
		});
		 $('#data-tbody').on('click','button',function(){
			 var btn=$(this);
			 if(btn.attr('btn-type')=='tails'){
				 app.deprocess_orderid = btn.attr('data-id');
				 app._status=btn.attr('data-state');
				 app.loadPage('deprocess_view');
			 }else if(btn.attr('btn-type')=='del'){
				 if(btn.attr('data-state')=='999'){
					 alert("流程状态已完成不能删除！");
				 }else if(btn.attr('data-state')=='-1'){
					 alert("业务已经删除！");
				 }else{
					 if(confirm("你确定要删除？")){
						 var del_id=btn.attr('data-id');
						 app.ajax({
								url:app.rootPath + '/sermanager/deprocess/update.do',
								type:'POST',
								data:'ids='+del_id,
								success:function(data){
									loadData();
							 }
						});
					 }
				 }	
			 }
			 
		 });
		 $('#reSetTable').on('click',function(){
			$('#search_key').val("");
			$('#status').val('1');
			$('#process_status').val('ALL')
			$('#start_date').val("");
			$('#end_date').val("");
			loadData();
		});
	};
	
	var deprocess = {
			init : function() {
				app.mainContainer.html(tpl);
				buildEvent();
				loadData();
			}
		};
		return deprocess;
});