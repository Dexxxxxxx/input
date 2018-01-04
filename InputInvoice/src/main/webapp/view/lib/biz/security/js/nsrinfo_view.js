/**
 * 
 */
define([ 'jquery', 'app', 'jqueryForm','ztree', 'text!biz/security/nsrinfo_view.html' ], function($, app, jqueryForm, ztree, tpl) {
	var z={
			a:false,b:false,c:false,d:false
			};//用户 确定 页面 submit 是否能进行提交
	var loadData_edit = function(){
		$('#fn_tittle').html('详情/编辑');
		var id = app.nsrinfoid;
		var nsrsbh = app.nsrsbh;
		app.ajax({
			url: app.rootPath + '/security/commInfo/getById.do',
			type: 'POST',
			data: 'id=' + id+"&nsrsbh="+nsrsbh,
			dataType: 'json',
			success: function(data){
				$('#nsrinfo_form input').each(function(i, item){
					$(this).val(data[$(this).attr('id').toUpperCase()]);
				});
				$('#nsrinfo_form textarea').each(function(i, item){
					$(this).val(data[$(this).attr('id').toUpperCase()]);
				});
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
		
	};
	var loadData_add = function(){
		$('#fn_tittle').html('纳税人信息新增');
		$('#nsrsbh').removeAttr('readonly');
		$('#name').removeAttr('readonly');
	}
	var buildEvent_edit = function(){
		z={
			a:true,b:true,c:true,d:true
		};
		var id = app.nsrinfoid;
		$('#nsrinfo_form').attr('action','security/commInfo/update.do');
		$('#nsrinfo_form').on('submit',function(e){
			e.preventDefault();
			if(z.a && z.b && z.c && z.d){
				$(this).ajaxSubmit({
					success: function(data){
						if(data['result'] == 'SUCCESS'){
							$(this).resetForm();
							app.loadPage('nsrinfo');
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
			}else{
				$('#submit-error').html('所修改信息不正确');
			}
		});
		$('#cancel').on('click',function(){
			app.loadPage('nsrinfo');
		});
	}
	
	var buildEvent_add=function(){
		z={
			a:false,b:false,c:false,d:false
		};
		$('#nsrinfo_form').attr('action','security/commInfo/add.do');
		$('#nsrinfo_form').on('submit',function(e){
			e.preventDefault();
			if(z.a && z.b && z.c && z.d){
				$(this).ajaxSubmit({
					success: function(data){
						if(data['result'] == 'SUCCESS'){
							$(this).resetForm();
							app.loadPage('nsrinfo');
						}else{
							alert(data['msg']);
						}
					},
					error : function(xhr, errorType, error) {
		      	    	cfg.tmp_start = true;
		      	    	$('#my-modal-loading').modal('close');
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
			}else{
				$('#submit-error').html('所填写信息不正确');
			}
		});
		$('#cancel').on('click',function(){
			app.loadPage('nsrinfo');
		});
	};
	
	var bindCheck = function(){
		$('#nsrsbh').on('blur',function(){
			if(!$(this).val()){
				$('#nsrsbh-error').html("输入纳税人识别号");
				z.a = false;
			}
			if($(this).val()){
				$('#nsrsbh-error').html("");
				z.a = true;
			}
		});
		$('#name').on('blur',function(){
			if(!$(this).val()){
				$('#name-error').html("输入纳税人名称");
				z.b = false;
			}
			if($(this).val()){
				$('#name-error').html("");
				z.b = true;
			}
		});
		$('#dzdh').on('blur',function(){
			if(!$(this).val()){
				$('#dzdh-error').html("输入地址电话");
				z.c = false;
			}
			if($(this).val()){
				$('#dzdh-error').html("");
				z.c = true;
			}
		});
		$('#yhzh').on('blur',function(){
			if(!$(this).val()){
				$('#yhzh-error').html("输入银行及银行账号");
				z.d = false;
			}
			else{
				$('#yhzh-error').html("");
				z.d = true;
			}
		});
	};
	
	var judgment=function(){
		if(app._function=="edit"){
			loadData_edit();
			buildEvent_edit();
			
		}else if(app._function=="add"){
			loadData_add();
			buildEvent_add();
		}
	};
	var nsrinfo_view = {
		init : function() {
			app.mainContainer.html(tpl);
			bindCheck();
			judgment();
		}
	};

	return nsrinfo_view;
});