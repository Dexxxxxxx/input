/**
 * 
 */
define([ 'jquery', 'app','jqueryForm','ztree', 'text!biz/security/privilege_view.html' ], function($, app,jqueryForm,ztree,tpl) {
	var z = {
			a:false,b:false,c:false
	};
	var loadData_edit = function(){
		var privilege_id=app.privilege_id;
		app.ajax({
			url:app.rootPath + '/security/privilege/getById.do',
			type:'POST',
			data:'id='+privilege_id,
			success:function(data){
				$('#id').val(data['ID']);
				$('#name').val(data['NAME']);
				$('#pid').val(data['PID']);
				$('#pname').val(data['PNAME']);
				$('#icon').val(data['ICON']);
				$('#orderby').val(data['ORDERBY']);
				$('#js').val(data['JS']);
				$('#url').val(data['URL']);
				$('#ptype').val(data['PTYPE']);
				
			}
		});
	};
	//修改编辑权限
	var buildEvent_edit = function(){
		z = {
				a:true,b:true,c:true
		};
		$('#fn_tittle').html('详情/编辑');
		$('#privilege_form').attr('action', 'security/privilege/update.do');
		$('#privilege_form').on('submit', function(e) {
            e.preventDefault();
            if(z.a&&z.b&&z.c){
            	$(this).ajaxSubmit({
                    success: function(data){
//                    	data = JSON.parse(data);
                    	if(data['result'] == 'SUCCESS'){
                    		app.loadPage('privilege');
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
		$('#pname').on('click',function(){
			var pid=$('#pid').val();
			app.ajax({
				url: app.rootPath + '/security/privilege/getTree.do',
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
					$.fn.zTree.init($("#treeDemo"), setting, zNodes).expandAll(true);
				}
			});
		});
		$('#saveprivilege').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getCheckedNodes(true);
			$('#pid').val(nodes[0].id);
			$('#ajax-modal').modal('hide');
			$('#pname').val(nodes[0].name);
		})
		$('#cancel').on('click', function(e) {
			app.loadPage('privilege');
		});
		
	};
	//新增权限
	var buildEvent_add=function(){
		z = {
				a:false,b:false,c:false
		};
		$('#fn_tittle').html('权限新增');
		$('#privilege_form').attr('action', 'security/privilege/add.do');
		$('#privilege_form').on('submit', function(e) {
            e.preventDefault();
            if(z.a&&z.b&&z.c){
            	$(this).ajaxSubmit({
                    success: function(data){
//                    	data = JSON.parse(data);
                    	if(data['result'] == 'SUCCESS'){
                    		app.loadPage('privilege');
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
		$('#cancel').on('click', function(e) {
			app.loadPage('privilege');
		});
		$('#pname').on('click',function(){
			var pid=$('#pid').val();
			app.ajax({
				url: app.rootPath + '/security/privilege/getTree.do',
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
					$.fn.zTree.init($("#treeDemo"), setting, zNodes).expandAll(true);
					$('#ajax-modal').modal('show');
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
		
		$('#saveprivilege').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getCheckedNodes(true);
			$('#pid').val(nodes[0].id);
			$('#ajax-modal').modal('hide');
			$('#pname').val(nodes[0].name);
		});
		
	}
	var bindCheck = function(){
		$('#name').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#name-error').html("部门名称不能为空");
				z.a = false;
			} else {
				$('#name-error').html("");
				z.a = true;
			}
		});
		$('#orderby').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#orderby-error').html("排序不能为空");
				z.b = false;
			} else if(!$.trim($(this).val()).match(/^[0-9]+$/)){
				$('#orderby-error').html("排序必须为数字");
				z.b = false;
			} else{
				$('#orderby-error').html("");
				z.b = true;
			}
		});
		$('#ptype').on('blur',function(){
			if(!$.trim($(this).val())){
				$('#ptype-error').html("类型不能为空");
				z.c = false;
			} else if(!$.trim($(this).val()).match(/^[0-9]+$/)){
				$('#ptype-error').html("类型必须为数字");
				z.b = false;
			}else {
				$('#ptype-error').html("");
				z.c = true;
			}
		});
	}
		
	var type=function(){
		if(app._function=="edit"){
			loadData_edit();
			buildEvent_edit();
		}else if(app._function=="add"){
			buildEvent_add();
		}
	};
	var privilege_view = {
		init : function() {
			app.mainContainer.html(tpl);
			type();
			bindCheck();
		}
	};

	return privilege_view;
});