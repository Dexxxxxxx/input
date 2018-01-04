/**
 * 
 */
define([ 'jquery', 'app', 'ztree', 'jqueryForm','text!biz/tobacco/baccobuyer_view.html' ], function($, app, ztree, jqueryForm, tpl) {
	var z={
			a:false,b:false,c:false,d:false,e:false,f:false
			};//用户 确定 页面 submit 是否能进行提交
	var buildEvent_add = function(){
		z={
			a:false,b:false,c:false,d:false,e:false,f:false
		};
		$('#baccobuyer_form').attr('action','tobacco/buyer/add.do');
		$('#baccobuyer_form').on('submit',function(e){
			e.preventDefault();
			if(z.a && z.b&&z.c && z.d && z.e && z.f){
				$(this).ajaxSubmit({
					success: function(data){
//						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							app.loadPage('baccobuyer');
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
			app.loadPage('baccobuyer');
		});
	}
	var buildEvent_edit = function(){
		z = {
			a: true, b: true,c:true,d:true,e:true,f:true
		};
		$('#nsrsbh').attr('readOnly','readonly');
		$('#baccobuyer_form').attr('action','tobacco/buyer/update.do');
		$('#baccobuyer_form').on('submit',function(e){
			e.preventDefault();
			if(z.a && z.b&&z.c && z.d && z.e && z.f){
				$(this).ajaxSubmit({
					success: function(data){
//						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							$(this).resetForm();
							app.loadPage('baccobuyer');
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
			app.loadPage('baccobuyer');
		});
	}
	var loadData_add = function(){
		$('#fn_tittle').html('购方信息新增');
	}
	var loadData_edit = function(){
		$('#fn_tittle').html('详情/编辑');
		var id = app.baccobuyer_id;
		app.ajax({
			url: app.rootPath + '/tobacco/buyer/getById.do',
			type: 'POST',
			data: 'id=' + id,
			dataType: 'json',
			success: function(data){
				$('#baccobuyer_form input').each(function(i,item){
					$(this).val(data[$(this).attr('id').toUpperCase()]);
				});
			}
		});
	};
	var bindCheck = function(){
		$('#name').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#name-error').html("输入用户名");
				z.a = false;
			}
			if($(this).val()){
				$('#name-error').html("");
				z.a = true;
			}
		});
		$('#nsrsbh').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#nsrsbh-error').html("输入纳税人识别号");
				z.b = false;
			} else {
					$('#nsrsbh-error').html("");
					z.b = true;
			}
		});
		$('#address').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#address-error').html("输入有效地址");
				z.c = false;
			} else {
					$('#address-error').html("");
					z.c = true;
			}
		});
		$('#phone').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#phone-error').html("输入正确电话号码");
				z.d = false;
			} else {
					$('#phone-error').html("");
					z.d = true;
			}
		});
		$('#khh').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#khh-error').html("输入开户行名称");
				z.e = false;
			} else {
					$('#khh-error').html("");
					z.e = true;
			}
		});
		$('#yhzh').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#yhzh-error').html("输入银行账号");
				z.f = false;
			} else {
					$('#yhzh-error').html("");
					z.f = true;
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
	
	
	var baccobuyer_view = {
		init : function() {
			app.mainContainer.html(tpl);
			bindCheck();
			judgment();
		}
	};

	return baccobuyer_view;
});