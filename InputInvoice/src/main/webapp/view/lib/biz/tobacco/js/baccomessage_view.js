/**
 * 
 */
define([ 'jquery', 'app', 'ztree', 'jqueryForm','text!biz/tobacco/baccomessage_view.html' ], function($, app, ztree, jqueryForm, tpl) {
	var z={
			a:false,b:false,c:false,d:false,e:false
			};//用户 确定 页面 submit 是否能进行提交
	var buildEvent_add = function(){
		z={
			a:false,b:false,c:false,d:false
		};
		$('#baccomessage_form').attr('action','tobacco/message/add.do');
		$('#baccomessage_form').on('submit',function(e){
			e.preventDefault();
			if(z.a && z.b&&z.c&&z.d&&z.e){
				$(this).ajaxSubmit({
					success: function(data){
//						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							app.loadPage('baccomessage');
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
			app.loadPage('baccomessage');
		});
	}
	var buildEvent_edit = function(){
		z = {
			a: true, b: true,c:true,d:true,e:true
		};
		$('#baccomessage_form').attr('action','tobacco/message/update.do');
		$('#baccomessage_form').on('submit',function(e){
			e.preventDefault();
			if(z.a && z.b&&z.c&&z.d&&z.e){
				$(this).ajaxSubmit({
					success: function(data){
//						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							$(this).resetForm();
							app.loadPage('baccomessage');
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
			app.loadPage('baccomessage');
		});
	}
	var loadData_add = function(){
		$('#fn_tittle').html('烟草信息新增');
	}
	var loadData_edit = function(){
		$('#fn_tittle').html('详情/编辑');
		var id = app.baccomessage_id;
		app.ajax({
			url: app.rootPath + '/tobacco/message/getById.do',
			type: 'POST',
			data: 'id=' + id,
			dataType: 'json',
			success: function(data){
					$('#baccomessage_form input').each(function(i,item){
					$(this).val(data[$(this).attr('id').toUpperCase()]);
				});
				$('#type').val(data['TYPE']);
			}
		});
	};
	var bindCheck = function(){
		$('#name').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#name-error').html("输入商品名称");
				z.a = false;
			}
			if($(this).val()){
				$('#name-error').html("");
				z.a = true;
			}
		});
		$('#code').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#code-error').html("输入商品编号");
				z.b = false;
			} else {
					$('#code-error').html("");
					z.b = true;
			}
		});
		$('#dw').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#dw-error').html("输入商品单位");
				z.c = false;
			} else {
					$('#dw-error').html("");
					z.c = true;
			}
		});
		$('#sl').on('blur',function(){
			if(!$.trim($(this).val())||!$.trim($(this).val()).match(/^\d+(\.\d{2})?$/)){
				$('#sl-error').html("输入数字不合法,输入为数字或两位小数");
				z.d = false;
			} else {
					$('#sl-error').html("");
					z.d = true;
			}
		});
		$('#zxbm').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#zxbm-error').html("输入自行编码");
				z.e = false;
			} else {
					$('#zxbm-error').html("");
					z.e = true;
			}
		});
	}
	var judgment = function(){
		var _fn = app._function;
		if(_fn == 'add'){
			loadData_add();
			buildEvent_add();
		} else if(_fn == 'edit'){
			loadData_edit();
			buildEvent_edit();
		} 
	}
	
	
	var baccomessage_view = {
		init : function() {
			app.mainContainer.html(tpl);
			bindCheck();
			judgment();
		}
	};

	return baccomessage_view;
});