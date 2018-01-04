/**
 * 
 */
define([ 'jquery', 'app', 'jqueryForm','text!biz/security/datahandle_view.html' ], function($, app, jqueryForm, tpl) {
	var z={a:false, b:false, c:false, d:false};
	var buildEvent_add = function(){
		z={a:false, b:false, c:false, d:false};
		$('#datahandle_form').attr('action','j3mgr/add.do');
		$('#datahandle_form').on('submit',function(e){
			e.preventDefault();
			if(z.a && z.b && z.c && z.d){
				$(this).ajaxSubmit({
					success: function(data){
						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							app.loadPage('datahandle');
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
			app.loadPage('datahandle');
		});
	}
	var buildEvent_edit = function(){
		z={a:true, b:true, c:true, d:true};
		$('#datahandle_form').attr('action','j3mgr/update.do');
		$('#datahandle_form').on('submit',function(e){
			e.preventDefault();
			if(z.a && z.b && z.c && z.d){
				$(this).ajaxSubmit({
					success: function(data){
						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							$(this).resetForm();
							app.loadPage('datahandle');
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
			app.loadPage('datahandle');
		});
	}
	var loadData_add = function(){
		$('#fn_tittle').html('系统常量新增');
	}
	var loadData_edit = function(){
		$('#fn_tittle').html('详情/编辑');
		$('#PARAMCODE').attr('readOnly','readOnly');
		var paramcode = app.paramcode;
		app.ajax({
			url: app.rootPath + '/j3mgr/getById.do',
			type: 'POST',
			data: 'paramcode=' + paramcode,
			dataType: 'json',
			success: function(data){
				$('#datahandle_form input').each(function(i,item){
					$(this).val(data[$(this).attr('id')]);
				});
			}
		});
	};
	var bindCheck = function(){
		$('#PARAMNAME').on('blur',function(){
			if(!$(this).val()){
				$('#paramname-error').html("输入正确的参数名称");
				z.a = false;
			} else {
				$('#paramname-error').html("");
				z.a = true;
			}
		});
		$('#PARAMVALUE').on('blur',function(){
			if(!$(this).val()){
				$('#paramvalue-error').html("输入正确的参数值");
				z.b = false;
			} else {
				$('#paramvalue-error').html("");
				z.b = true;
			}
		});
		$('#PARAMDESC').on('blur',function(){
			if(!$(this).val()){
				$('#paramdesc-error').html("输入正确的参数代码");
				z.c = false;
			} else {
				$('#paramdesc-error').html("");
				z.c = true;
			}
		});
		$('#PARAMCODE').on('blur',function(){
			if(!$(this).val()){
				$('#paramcode-error').html("输入正确的参数代码");
				z.d = false;
			} else {
				$('#paramcode-error').html("");
				z.d = true;
			}
		});
	}
	var judgment = function(){
		var _fn = app._function;
		if(_fn == 'ADD'){
			loadData_add();
			buildEvent_add();
		} else if(_fn == 'UPDATE'){
			loadData_edit();
			buildEvent_edit();
		} else {
			alert("解析出错");
			app.loadPage("index");
		}
	}
	
	var datahandle_view = {
		init : function() {
			app.mainContainer.html(tpl);
			judgment();
			bindCheck();
		}
	};
	return datahandle_view;
});