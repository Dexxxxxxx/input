/**
 * 
 */
define([ 'jquery', 'app', 'jqueryForm','text!biz/cigarette/cigarcustomer_view.html' ], function($, app, jqueryForm, tpl) {
	var z={
			a:false,b:false
			};//用户 确定 页面 submit 是否能进行提交
	var buildEvent_add = function(){
		z={
			a:false,b:false
		};
		$('#cigarcustomer_form').attr('action','cigarette/customer/add.do');
		$('#cigarcustomer_form').on('submit',function(e){
			e.preventDefault();
			if(z.a && z.b){
				$(this).ajaxSubmit({
					success: function(data){
//						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							app.loadPage('cigarcustomer');
						}else{
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
							alert('出错了! error:'  + xhr['status']);
						}
					}
				});
			} else {
				$('#submit-error').html("所填写信息不正确");
			}
		});
		
		$('#cancel').on('click',function(){
			app.loadPage('cigarcustomer');
		});
	}
	var buildEvent_edit = function(){
		z = {
			a: true, b: true
		};
		$('#role_form').attr('action','security/role/update.do');
		$('#role_form').on('submit',function(e){
			e.preventDefault();
			if(z.a && z.b){
				$(this).ajaxSubmit({
					success: function(data){
//						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							$(this).resetForm();
							app.loadPage('cigarcustomer');
						}else{
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
							alert('出错了! error:'  + xhr['status']);
						}
					}
				});
			} else {
				$('#submit-error').html("所修改信息不正确");
			}
		});
		
		$('#cancel').on('click',function(){
			app.loadPage('cigarcustomer');
		});
	}
	var loadData_add = function(){
		$('#fn_tittle').html('角色新增');
	}
	var loadData_edit = function(){
		$('#fn_tittle').html('详情/编辑');
		var id = app.role_id;
		app.ajax({
			url: app.rootPath + '/security/role/getById.do',
			type: 'POST',
			data: 'id=' + id,
			dataType: 'json',
			success: function(data){
				$('#role_form input').each(function(i,item){
					$(this).val(data[$(this).attr('id').toUpperCase()]);
				});
			}
		});
	};
	var bindCheck = function(){
		$('#ghdwdm').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#ghdwdm-error').html("输入购货单位代码");
				z.a = false;
			}
			if($(this).val()){
				$('#ghdwdm-error').html("");
				z.a = true;
			}
		});
		$('#ghdwmc').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#ghdwmc-error').html("输入编码");
				z.b = false;
			} else {
					$('#ghdwmc-error').html("");
					z.b = true;
			}
		});
	}
	
	var cigarcustomer_view = {
		init : function() {
			app.mainContainer.html(tpl);
			bindCheck();
			buildEvent_add();
		}
	};
	return cigarcustomer_view;
});