/**
 * 
 */
define([ 'jquery', 'app', 'jqueryForm','ztree', 'text!biz/security/user_view.html' ], function($, app, jqueryForm, ztree, tpl) {
	var z={
			z:false,a:false,b:false,c:false,d:false
			};//用户 确定 页面 submit 是否能进行提交
	var loadData_edit = function(){
		$('#fn_tittle').html('详情/编辑');
		var id = app.user_id;
		app.ajax({
			url: app.rootPath + '/security/user/getById.do',
			type: 'POST',
			data: 'id=' + id,
			dataType: 'json',
			success: function(data){
				$('#user_form input').each(function(i, item){
					$(this).val(data[$(this).attr('id').toUpperCase()]);
				});
				$('#enabled_1').val(data.ENABLED == '-1' ? '是' : '否');
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
		$('#fn_tittle').html('用户新增');
		$('#username').removeAttr('readOnly');
		$('#enabled-div').attr('style','display:none');
	}
	var buildEvent_edit = function(){
		z={
			z:true,a:true,b:true,c:true,d:true
		};
		var id = app.user_id;
		$('#user_form').attr('action','security/user/update.do');
		$('#user_form').on('submit',function(e){
			e.preventDefault();
			if(z.z && z.a && z.b && z.c && z.d){
				$(this).ajaxSubmit({
					success: function(data){
//						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							$(this).resetForm();
							app.loadPage('user');
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
			app.loadPage('user');
		});
		// 编辑调出所属结构TreeView 
		$('#departname').on('click',function(){
			var userid = $('#id').val();
			var departid = $('#departid').val();
			app.ajax({
				url: app.rootPath + '/security/depart/getTree.do',
				data: 'userId=' + userid+ '&type=ntree',
				success: function(data){
					var zNodes = [];
					var temp;
					$.each(data,function(i,item){
						if(item['ID'] == departid){
							temp = {id:item['ID'], pId: item['PID'], name: item['NAME'], checked: true };
						}else{
							temp = {id:item['ID'], pId: item['PID'], name: item['NAME'] };
						}
						zNodes.push(temp);
					});
					var setting = {
						check: {
							enable: true,
							chkStyle: "radio",
							radioType: "all"
						},
						data: {  
			                simpleData: {  
			                    enable: true,
			                    idKey: "id",
			        			pIdKey: "pId"
			                }
			            },
			            callback: {
			            	beforeClick: function zTreeOnClick(treeId, treeNode) {
							    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
							    var node = treeObj.getNodeByTId(treeNode.tId);
							    treeObj.checkNode(node, true, true);
							}
						}
					};
				$('#ajax-modal').modal('show');
				$.fn.zTree.init($("#treeDemo"), setting, zNodes).expandAll(true);
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
		});
		$('#saveDepart').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getCheckedNodes(true);
			if(null == nodes || "" == nodes){
				$('#ajax-modal').modal('hide');
				$('#departid').val('');
				$('#departname').val('');
			} else {
				$('#ajax-modal').modal('hide');
				$('#departid').val(nodes[0].id);
				$('#departname').val(nodes[0].name);
			};
			if(null == $('#departname').val() || "" == $('#departname').val()){
				$('#departname-error').html("选择用户所属机构");
				z.z = false;
			} else {
				$('#departname-error').html("");
				z.z = true;
			};
		});
	}
	
	var buildEvent_add=function(){
		z={
			z:false,a:false,b:false,c:false,d:true
		};
		$('#user_form').attr('action','security/user/add.do');
		$('#user_form').on('submit',function(e){
			e.preventDefault();
			if(z.z && z.a && z.b && z.c && z.d){
				$(this).ajaxSubmit({
					success: function(data){
//						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							$(this).resetForm();
							app.loadPage('user');
						}else{
							alert(data['msg']);
						}
					},
				});
			}else{
				$('#submit-error').html('所填写信息不正确');
			}
		});
		$('#cancel').on('click',function(){
			app.loadPage('user');
		});
		// 新增调出所属结构TreeView
		$('#departname').on('click',function(){
			var departid = $('#departid').val();
			app.ajax({
				url: app.rootPath + '/security/depart/getTree.do',
				data: 'userId=' + ''+ '&type=ntree',
				success: function(data){
					var zNodes = [];
					var temp;
					$.each(data,function(i,item){
						if(item['ID'] == departid){
							temp = {id:item['ID'], pId: item['PID'], name: item['NAME'], checked: true };
						}else{
							temp = {id:item['ID'], pId: item['PID'], name: item['NAME'] };
						}
						zNodes.push(temp);
					});
					var setting = {
						check: {
							enable: true,
							chkStyle: "radio",
							radioType: "all"
						},
						data: {  
			                simpleData: {  
			                    enable: true,
			                    idKey: "id",
			        			pIdKey: "pId"
			                }
			            },
			            callback: {
			            	beforeClick: function zTreeOnClick(treeId, treeNode) {
							    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
							    var node = treeObj.getNodeByTId(treeNode.tId);
							    treeObj.checkNode(node, true, true);
							}
						},
					};
				$.fn.zTree.init($("#treeDemo"), setting, zNodes).expandAll(true);
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
			$('#ajax-modal').modal('show');
		});
		$('#saveDepart').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getCheckedNodes(true);
			if(null == nodes || "" == nodes){
				$('#ajax-modal').modal('hide');
				$('#departid').val('');
				$('#departname').val('');
			} else {
				$('#ajax-modal').modal('hide');
				$('#departid').val(nodes[0].id);
				$('#departname').val(nodes[0].name);
			};
			if(null == $('#departname').val() || "" == $('#departname').val()){
				$('#departname-error').html("选择用户所属机构");
				z.z = false;
			} else {
				$('#departname-error').html("");
				z.z = true;
			}
		});
	};
	
	var bindCheck = function(){
		$('#name').on('blur',function(){
			if(!$(this).val()){
				$('#name-error').html("输入用户名");
				z.a = false;
			}
			if($(this).val()){
				$('#name-error').html("");
				z.a = true;
			}
		});
		$('#username').on('blur',function(){
			if(!$(this).val()){
				$('#username-error').html("输入真实姓名");
				z.b = false;
			}
			else{
				$('#username-error').html("");
				z.b = true;
			}
		});
		$('#phone').on('blur',function(){
//			 	      /^(0[0-9]{2,3}\-)?([1-9][0-9]{6,7})+(\-[0-9]{1,4})?$|(^(1[1-9][0-9]|15[0|3|6|7|8|9]|18[8|9])\d{8}$)/
			var reg = /^(0[0-9]{2,3})?([1-9][0-9]{6,7})$|(^(1[1-9][0-9])\d{8}$)|(^(0[0-9]{2,3})?\-([1-9][0-9]{6,7})$)/;
			if(!$(this).val()){
				$('#phone-error').html("输入正确的电话格式");
				z.c = false;
			}else{
				if(!$(this).val().match(reg)){
					$('#phone-error').html("输入正确的电话格式");
					z.c = false;
				}else{
					$('#phone-error').html("");
					z.c = true;
				}
			}
		});
		$('#email').on('blur',function(){
			if($(this).val()){
				if(!$(this).val().match(/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/)){
					$('#email-error').html("输入正确的邮箱格式");
					z.d = false;
				} else {
					$('#email-error').html("");
					z.d = true;
				}
			}else{
				$('#email-error').html("");
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
	var user_view = {
		init : function() {
			app.mainContainer.html(tpl);
			bindCheck();
			judgment();
		}
	};

	return user_view;
});