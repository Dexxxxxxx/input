/**
 * 
 */
define([ 'jquery', 'app','jqueryForm', 'ztree','text!biz/security/resource.html' ], function($, app,jqueryForm,ztree,tpl) {
	var z = {
			a:false,b:false
	};
	var w ={
			a:false,b:false,c:false,d:false
	};
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/security/resource/listType.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr dd-id="'+item['ID']+'">'
						+'  <td><div class="checker"><span><input type="checkbox"  class="checkboxes" value="'+item['ID']+'"></span></div></td>'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['CODE']+'</td>'
						+'	<td>'
						+'		<div>'
						+'				<button class="btn sbold green btn-sm" btn-type="edit" data-id="'+item['ID']+'">'
						+'				编辑 <i class="fa fa-edit"></i>'
						+'				</button>'
						+'		</div>'
						+'	</td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	};
	var loadData_items=function(){
		new app.Page({
			url : app.rootPath + '/security/resource/listItems.do?pid='+app.pid,
			target : 'page-div_1',
			from: 'search-form_1',
			dataTarget: 'data-tbody_1',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr dd-id="'+item['ID']+'">'
						+'  <td><div class="checker"><span><input type="checkbox"  class="checkboxes_items" value="'+item['ID']+'"></span></div></td>'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['CODE']+'</td>'
						+'	<td>'+item['VAL']+'</td>'
						+'	<td>'+item['BYORDER']+'</td>'
						+'	<td>'+item['MASK']+'</td>'
						+'	<td>'
						+'		<div>'
						+'				<button class="btn sbold green btn-sm" btn-type="edit" data-id="'+item['ID']+'">'
						+'				编辑 <i class="fa fa-edit"></i>'
						+'				</button>'
						+'		</div>'
						+'	</td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	}
	
	var buildEvent = function(){
		$('#data-tbody').on('click','tr', function(){
			$('#search_key1').val('');
			app.pid=$(this).attr('dd-id');
			loadData_items();
			
		});
		$('#data-tbody').on('click','input', function(e){
			e.stopPropagation();
		});
		
		$('#data-tbody').on('click','button', function(e){
			e.stopPropagation();
			var btn = $(this);
			if(btn.attr('btn-type') == 'edit'){
				var id=btn.attr('data-id');
				app.ajax({
					url:app.rootPath + '/security/resource/getTypeById.do',
					type:'POST',
					data:'id='+id,
					success:function(data){
						$('#id').val(data['ID']);
						$('#name').val(data['NAME']);
						$('#code').val(data['CODE']);
						$('#fn_tittle').html('资源类型 详情/编辑');
						$('#resource_form').attr('action',app.rootPath+'/security/resource/updateType.do');
						z = {
								a:true,b:true
						};
						$('#name-error').html("");
						$('#code-error').html("");
						$('#submit-error').html("");
						$('#ajax-modal').modal('show');
					}
				});
				
			}
		});
		$('#check_group').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.checkboxes').prop('checked',isChecked);
		});
		$('#btn-del').on('click',function(){
			var ids = [];
			$('.checkboxes').each(function(){
				if($(this).is(':checked') == true){
					ids.push($(this).val());
				}
			});
			if(confirm("确定删除？")){
				app.ajax({
					url: app.rootPath + '/security/resource/deleteType.do',
					data: 'ids=' + ids,
					success: function(data){
						alert('删除成功');
						loadData();
					}
				});
			}
		});	
		$('#btn-add').on('click', function(e) {
			$('#id').val('');
			$('#name').val('');
			$('#code').val('');
			$('#fn_tittle').html('新增资源类型');
			$('#resource_form').attr('action',app.rootPath+'/security/resource/addType.do');
			z = {
					a:false,b:false
			};
			$('#name-error').html("");
			$('#code-error').html("");
			$('#submit-error').html("");
			$('#ajax-modal').modal('show');
			
		});
		
		$('#resource_form').on('submit', function(e) {
			e.preventDefault();
			if(z.a&&z.b){
				$(this).ajaxSubmit({
	                success: function(data){
	                	data = JSON.parse(data);
	                	if(data['result'] == 'SUCCESS'){
	                		$('#ajax-modal').modal('hide');
	                		loadData();
	                	} else {
	                		
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
	            })
			}else{
				$('#submit-error').html("所填写信息不正确");
			}
            
		});
		
	}
	
	
	
	var buildEvent_1=function(){
		$('#data-tbody_1').on('click','button', function(){
			var btn = $(this);
			if(btn.attr('btn-type') == 'edit'){
				var id=btn.attr('data-id');
				$('#fn_tittle_items').html('资源项目 详情/编辑');
				app.ajax({
					url:app.rootPath + '/security/resource/getItemsById.do',
					type:'POST',
					data:'id='+id,
					success:function(data){
						$('#id_items').val(data['ID'])
						$('#name_items').val(data['NAME']);
						$('#code_items').val(data['CODE']);
						$('#val_items').val(data['VAL']);
						$('#byorder_items').val(data['BYORDER']);
						$('#mask_items').val(data['MASK']);
						$('#resource_form_1').attr('action',app.rootPath+'/security/resource/updateItems.do');
						w ={
								a:true,b:true,c:true,d:true
						};
						$('#name_items-error').html('');
						$('#code_items-error').html('');
						$('#val_items-error').html('');
						$('#byorder_items-error').html('');
						$('#submit_items-error').html('')
						$('#ajax-modal_items').modal('show');
					}
				});
				
			}
		});
		$('#check_group_items').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.checkboxes_items').prop('checked',isChecked);
		});
		$('#btn-del_items').on('click',function(){
			var ids = [];
			$('.checkboxes_items').each(function(){
				if($(this).is(':checked') == true){
					ids.push($(this).val());
				}
			});
			if(confirm("确定删除？")){
				app.ajax({
					url: app.rootPath + '/security/resource/deleteItems.do',
					data: 'ids=' + ids,
					success: function(data){
						alert('删除成功');
						loadData_items();
					}
				});
			}
		});
		
		$('#btn-add_items').on('click', function(e) {
			if(app.pid!=null&app.pid!=''){
				$('#name_items').val('');
				$('#code_items').val('');
				$('#val_items').val('');
				$('#byorder_items').val('');
				$('#mask_items').val('');
				$('#fn_tittle_items').html('新增资源项目');
				$('#resource_form_1').attr('action',app.rootPath+'/security/resource/addItems.do?pid='+app.pid);
				w ={
						a:false,b:false,c:false,d:false
				};
				$('#name_items-error').html('');
				$('#code_items-error').html('');
				$('#val_items-error').html('');
				$('#byorder_items-error').html('');
				$('#submit_items-error').html('')
				$('#ajax-modal_items').modal('show');
			}else{
				alert("选择资源类型！")
			}	
			
		});
		
		$('#resource_form_1').on('submit', function(e) {
            e.preventDefault();
            if(w.a&&w.b&&w.c&&w.d){
            	$(this).ajaxSubmit({
                	success: function(data){
                		data = JSON.parse(data);
                	if(data['result'] == 'SUCCESS'){
                		$('#ajax-modal_items').modal('hide');
                		loadData_items();
                	} else {
                		
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
            }else{
            	$('#submit_items-error').html("所填写信息不正确");
            }
            
		});
		
	}
	var bindcheck = function () {
		//资源类型输入验证
		$('#name').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#name-error').html("部门名称不能为空");
				z.a = false;
			} else {
				$('#name-error').html("");
				z.a = true;
			}
		});
		$('#code').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#code-error').html("代码不能为空");
				z.b = false;
			} else {
				$('#code-error').html("");
				z.b = true;
			}
		});
		//资源项目输入验证
		$('#name_items').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#name_items-error').html("名称不能为空");
				w.a = false;
			} else {
				$('#name_items-error').html("");
				w.a = true;
			}
		});
		$('#code_items').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#code_items-error').html("编码不能为空");
				w.b = false;
			} else {
				$('#code_items-error').html("");
				w.b = true;
			}
		});
		$('#val_items').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#val_items-error').html("值不能为空");
				w.c = false;
			} else {
				$('#val_items-error').html("");
				w.c = true;
			}
		});
		$('#byorder_items').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#byorder_items-error').html("排序不能为空");
				w.d = false;
			} else if(!$.trim($(this).val()).match(/^(-)?[0-9]+$/)){
				$('#byorder_items-error').html("排序必须为数字");
				w.d = false;
			}else {
				$('#byorder_items-error').html("");
				w.d = true;
			}
		});
		
    }
			
	var handleSample2 = function () {
		
    }
	var resource = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			buildEvent_1();
			loadData();
			bindcheck();
		}
	};

	return resource;
});