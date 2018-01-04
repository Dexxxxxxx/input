/**
 * 
 */
define([ 'jquery', 'app','jqueryForm', 'text!biz/security/depart_view.html' ], function($, app,jqueryForm,tpl) {
	var z = {
			a:false,b:false,c:false,d:false,e:false,f:false,g:false,h:false,i:false
	};
	//编辑 数据加载
	var loadData_edit = function(){
		var depart_id=app.depart_id;
		app.ajax({
			url:app.rootPath + '/security/depart/getById.do',
			type:'POST',
			data:'id='+depart_id,
			success:function(data){
				$('#id').val(data['ID']);
				$('#name').val(data['NAME']);
				$('#code').val(data['CODE']);
				$('#pid').val(data['PID']);
				$('#pname').val(data['PNAME']);
				$('#description').val(data['DESCRIPTION']);
				$('#address').val(data['ADDRESS']);
				$('#phone').val(data['PHONE']);
				$('#orderby').val(data['ORDERBY']);
				$('#createtime').val(data['CREATETIME']);
				$('#contacts').val(data['CONTACTS']);
				$('#areas').val(data['AREAS']);
				$('#nsrid').val(data['NSRID']);
				$('#nsrsbh').val(data['NSRNAME']);
				$('#kpzdbs').val(data['KPZDBS']);
			}
		});
	};
	//编辑  事件绑定
	var buildEvent_edit = function(){
		z = {
				a:true,b:true,c:true,d:true,e:true,f:true,g:true,h:true,i:true
			};
		$('#fn_tittle').html('详情/编辑');
		$('#depart_form').attr('action','security/depart/update.do');
		$('#depart_form').on('submit', function(e) {
            e.preventDefault();
            if(z.a&&z.b&&z.c&&z.d&&z.e&&z.f&&z.g&&z.h&&z.i){
            	$(this).ajaxSubmit({
                    success: function(data){
//                    	data = JSON.parse(data);
                    	if(data['result'] == 'SUCCESS'){
                    		app.loadPage('depart');
                    	} else {
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
            	$('#submit-error').html("所填写信息不正确");
            };
            
		});
		$('#pname').on('click', function(e) {
			//显示部门树形图
			var pid=$('#pid').val();
			var id=$('#id').val();
			app.ajax({
				url: app.rootPath + '/security/depart/getTree.do',
				type: 'POST',
				data: 'userId=' + id+ '&type=ntree',
				dataType: 'json',
				success: function(data){
					var zNodes = [];
					var temp ;
					$.each(data,function(i,item){
						if(pid==item['ID']){
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
					$.fn.zTree.init($("#treeDemo"), setting, zNodes).expandAll(false);
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
		$('#savedep').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes==null||nodes==''){
				$('#ajax-modal').modal('hide');
				$('#pid').val('');
				$('#pname').val('');
			}else{
				$('#pid').val(nodes[0].id);
				$('#ajax-modal').modal('hide');
				$('#pname').val(nodes[0].name);
			}
		});
		
		$('#nsrsbh').on('click', function(e) {
			var nid=$('#nsrid').val();
			//显示部门树形图
			app.ajax({
				url: app.rootPath + '/tobacco/purchase/getNsrTree.do',
				type: 'POST',
				dataType: 'json',
				success: function(data){
					var zNodes = [];
					var temp ;
					$.each(data,function(i,item){
						if(nid==item['ID']){
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
							    var treeObj = $.fn.zTree.getZTreeObj("treeDemo_nsr");
							    var node = treeObj.getNodeByTId(treeNode.tId);
							    treeObj.checkNode(node, true, true);
							}
						}
					};
					$.fn.zTree.init($("#treeDemo_nsr"), setting, zNodes).expandAll(false);
				}
			});
			$('#ajax-modal-nsr').modal('show');
		});
		$('#savensr').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo_nsr");
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes==''||nodes==null){
				$('#ajax-modal-nsr').modal('hide');
				$('#nsrid').val('');
				$('#nsrsbh').val('');
			}else{
				$('#nsrid').val(nodes[0].id);
				$('#ajax-modal-nsr').modal('hide');
				$('#nsrsbh').val(nodes[0].name);
				$('#nsrid-error').html("");
				z.i=true;
			}
		});
		
		$('#cancel').on('click', function() {
			app.loadPage('depart');
		});
	}; 
	//增加  事件绑定
	var buildEvent_add = function(){
		z = {
				a:false,b:true,c:true,d:true,e:true,f:true,g:false,h:true,i:true
			};
		$('#fn_tittle').html('组织机构新增');
		$('#depart_form').attr('action','security/depart/add.do');
		$('#depart_form').on('submit', function(e) {
            e.preventDefault();
            if(z.a&&z.b&&z.c&&z.d&&z.e&&z.f&&z.g&&z.h&&z.i){
            	$(this).ajaxSubmit({
                    success: function(data){
//                    	data = JSON.parse(data);
                    	if(data['result'] == 'SUCCESS'){
                    		app.loadPage('depart');
                    	} else {
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
            	$('#submit-error').html("所填写信息不正确");
            }
            
		});
		$('#pname').on('click', function(e) {
			//显示部门树形图
			var pid=$('#pid').val();
			var id=$('#id').val();
			app.ajax({
				url: app.rootPath + '/security/depart/getTree.do',
				type: 'POST',
				data: 'userId=' + id+ '&type=ntree',
				dataType: 'json',
				success: function(data){
					var zNodes = [];
					var temp ;
					$.each(data,function(i,item){
						if(pid==item['ID']){
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
					$.fn.zTree.init($("#treeDemo"), setting, zNodes).expandAll(false);
				}
			});
			$('#ajax-modal').modal('show');
		});
		$('#savedep').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes==''||nodes==null){
				$('#ajax-modal').modal('hide');
				$('#pid').val('');
				$('#pname').val('');
			}else{
				$('#pid').val(nodes[0].id);
				$('#ajax-modal').modal('hide');
				$('#pname').val(nodes[0].name);
			}
		});
		$('#nsrsbh').on('click', function(e) {
			var nid=$('#nsrid').val();
			//显示部门树形图
			app.ajax({
				url: app.rootPath + '/tobacco/purchase/getNsrTree.do',
				type: 'POST',
				dataType: 'json',
				success: function(data){
					var zNodes = [];
					var temp ;
					$.each(data,function(i,item){
						if(nid==item['ID']){
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
							    var treeObj = $.fn.zTree.getZTreeObj("treeDemo_nsr");
							    var node = treeObj.getNodeByTId(treeNode.tId);
							    treeObj.checkNode(node, true, true);
							}
						}
					};
					$.fn.zTree.init($("#treeDemo_nsr"), setting, zNodes).expandAll(false);
				}
			});
			$('#ajax-modal-nsr').modal('show');
		});
		$('#savensr').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo_nsr");
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes==''||nodes==null){
				$('#ajax-modal-nsr').modal('hide');
				$('#nsrid').val('');
				$('#nsrsbh').val('');
			}else{
				$('#nsrid').val(nodes[0].id);
				$('#ajax-modal-nsr').modal('hide');
				$('#nsrsbh').val(nodes[0].name);
				$('#nsrid-error').html("");
				z.i = true;
			}
		});
		
		
		
		
		$('#cancel').on('click', function(e) {
			app.loadPage('depart');
		});
	};
	var bindcheck=function(){
		$('#name').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#name-error').html("部门名称不能为空");
				z.a = false;
			} else {
				$('#name-error').html("");
				z.a = true;
			}
		});
//		$('#code').on('blur',function(){
//			if(!$.trim($(this).val())){
//				$('#code-error').html("代码不能为空");
//				z.b = false;
//			} else {
//				$('#code-error').html("");
//				z.b = true;
//			}
//		});
//		$('#address').on('blur',function(){
//			if(!$.trim($(this).val())){
//				$('#address-error').html("代码不能为空");
//				z.c = false;
//			} else {
//				$('#address-error').html("");
//				z.c = true;
//			}
//		});
//		$('#contacts').on('blur',function(){
//			if(!$.trim($(this).val())){
//				$('#contacts-error').html("联系人不能为空");
//				z.d = false;
//			} else {
//				$('#contacts-error').html("");
//				z.d = true;
//			}
//		});
//		$('#phone').on('blur',function(){
//			if(!$.trim($(this).val())){
//				$('#phone-error').html("联系电话不能为空");
//				z.e = false;
//			} else {
//				$('#phone-error').html("");
//				z.e = true;
//			}
//		});
//		$('#areas').on('blur',function(){
//			if(!$.trim($(this).val())){
//				$('#areas-error').html("区域不能为空");
//				z.f = false;
//			} else {
//				$('#areas-error').html("");
//				z.f = true;
//			}
//		});
		$('#orderby').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#orderby-error').html("排序不能为空");
				z.g = false;
			}else if(!$.trim($(this).val()).match(/^[0-9]+$/)){
				$('#orderby-error').html("排序必须为数字");
				z.g = false;
			} else {
				$('#orderby-error').html("");
				z.g = true;
			}
		});
//		$('#kpzdbs').on('blur',function(){
//			if(!$.trim($(this).val())){
//				$('#kpzdbs-error').html("开票终端号不能为空");
//				z.h = false;
//			} else {
//				$('#kpzdbs-error').html("");
//				z.h = true;
//			}
//		});
//		$('#nsrsbh').on('blur',function(){
//			if(!$.trim($(this).val())){
//				$('#nsrid-error').html("纳税人信息不能为空");
//				z.i = false;
//			} else {
//				$('#nsrid-error').html("");
//				z.i = true;
//			}
//		});
		
	}
	//判断操作方式
	var type=function(){
		if(app._function=="edit"){
			loadData_edit();
			buildEvent_edit();
		}else if(app._function=="add"){
			buildEvent_add();
		}
	};
	var depart_view = {
		init : function() {
			app.mainContainer.html(tpl);
			bindcheck();    	
			type();
		}
	};
	return depart_view;
});