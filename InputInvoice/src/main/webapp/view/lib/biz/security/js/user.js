/**
 * 
 */
define([ 'jquery', 'app','ztree', 'text!biz/security/user.html' ], function($, app, ztree, tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/security/user/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					if(item['NAME'] == null){
						item['NAME'] = '';
					}
					if(item['USERNAME'] == null){
						item['USERNAME'] = '';
					}
					if(item['EMAIL'] == null){
						item['EMAIL'] = '';
					}
					if(item['PHONE'] == null){
						item['PHONE'] = '';
					}
					if(item['DEPARTNAME'] == null){
						item['DEPARTNAME'] = '';
					}
					if(item['CREATETIME'] == null){
						item['CREATETIME'] = '';
					}
					if(item['ENABLED'] == '-1'){
						item['ENABLED'] = '是';
					}else if(item['ENABLED'] == '0'){
						item['ENABLED'] = '否';
					}
					html += ''
						+'<tr data-tr-id = '+item['ID']+' data-u="'+item['USERNAME']+'">'
						+'  <td><div class="checker"><input type="checkbox" class="checkboxes" value="'+item['ID']+'"></div></td>'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['GENDER']+'</td>'
						+'	<td>'+item['USERNAME']+'</td>'
						+'	<td>'+item['EMAIL']+'</td>'
						+'	<td>'+item['PHONE']+'</td>'
						+'	<td>'+item['DEPARTNAME']+'</td>'
						+'	<td>'+item['ENABLED']+'</td>'
						+'	<td>'+item['CREATETIME']+'</td>'
						+'	<td>'
						+'		<div class="am-btn-toolbar">'
					    +'			<button class="btn green btn-sm" data-id="'+item['ID']+'">'
					    +'			编辑 <i class="fa fa-edit"></i>'
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
		var id='';
		$('#data-tbody').on('click','button',function(e){
			e.stopPropagation();
			var btn = $(this);
			if(btn.attr('data-id') != null && btn.attr('data-id') != ''){
				app.user_id = btn.attr('data-id');
				app._function='edit';
				app.loadPage('user_view');
			}else{
				alert('未选择查看的信息');
			}
		});
		$('#check_group').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.checkboxes').prop('checked',isChecked);
		});
		$('#btn-new').on('click',function(){
			app._function='add';
			app.loadPage('user_view');
		});
		$('#reset-pwd').on('click',function(){
			var len=0;
			var checkbox=$('.checkboxes');
			for(var i=0;i<checkbox.length;i++){
				if(checkbox[i].checked==true){
					len++;
					id=checkbox[i].value;
				}
			}
			if(len != 1){
				alert("选择一条信息");
			} else {
				if (confirm("确认重置密码？")) {
					app.ajax({
						url : app.rootPath + '/security/login/resetPassword.do',
						data : 'id='+ id,
						success : function(data) {
							alert('重置成功，恢复初始密码');
						}
					});
				}
			}
		});
		$('#btn-start').on('click',function(){
			var ids = [];
			$('.checkboxes').each(function(){
				if($(this).is(':checked') == true){
					ids.push($(this).val());
				}
			});
			if(confirm("确定启用？")){
				app.ajax({
					url: app.rootPath + '/security/user/invokeByStart.do',
					data: 'ids=' + ids,
					success: function(data){
						loadData();
					}
				});
			}
		});
		$('#btn-del').on('click',function(){
			var ids = [];
			$('.checkboxes').each(function(){
				if($(this).is(':checked') == true){
					ids.push($(this).val());
				}
			});
			if(confirm("确定禁用？")){
				app.ajax({
					url: app.rootPath + '/security/user/delete.do',
					data: 'ids=' + ids,
					success: function(data){
						loadData();
					}
				});
			}
		});
		$('#btn-deluser').on('click',function(){
			var ids = [];
			$('.checkboxes').each(function(){
				if($(this).is(':checked') == true){
					ids.push($(this).val());
				}
			});
			if(confirm("确定删除？")){
				app.ajax({
					url: app.rootPath + '/security/user/deleteUsers.do',
					data: 'ids=' + ids,
					success: function(data){
						loadData();
					}
				});
			}
		})
		//显示角色树形结构
		$('#showrole').click(function(){
			var len=0;
			var checkbox=$('.checkboxes');
			for(var i=0;i<checkbox.length;i++){
				if(checkbox[i].checked==true){
					len++;
					id=checkbox[i].value;
				}
			}
			if(len==0){
				alert("勾选一个用户");
			}else if(len>1){
				alert("只能勾选一个用户");
			}else{
					app.ajax({
						url: app.rootPath + '/security/role/getTree.do',
						type: 'POST',
						data: 'userId=' + id,
						dataType: 'json',
						success: function(data){
							var zNodes = [];
							var temp ;
							$.each(data,function(i,item){
								if(item['CHECKED'] == 'true'){
									temp = {id:item['ID'], pId: item['PID'], name: item['NAME'], checked: true };
								}else{
									temp = {id:item['ID'], pId: item['PID'], name: item['NAME'] };
								}
								zNodes.push(temp);
							});
							var setting = {
								check: {
									enable: true,
									chkStyle: "checkbox",
									chkboxType: { "Y": "ps", "N": "ps" }
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
									    var treeObj = $.fn.zTree.getZTreeObj("tree_2");
									    var node = treeObj.getNodeByTId(treeNode.tId);
									    treeObj.checkNode(node, true, true);
									}
								}
							};
							$('#ajax-modal').modal('show');
							$.fn.zTree.init($("#tree_2"), setting, zNodes).expandAll(true);
						}
					});
			}
		});
		//模态框 保存设置角色
		$('#save-role').click(function(){
			var treeObj = $.fn.zTree.getZTreeObj("tree_2");
			var nodes = treeObj.getCheckedNodes(true);
			var roleIds=[];
			for(var i=0; i<nodes.length; i++){
				roleIds.push(nodes[i].id);
				}
			app.ajax({
				url: app.rootPath + '/security/user/saveRole.do',
				type: 'POST',
				data: 'roleIds=' + roleIds+'&userId='+id,
				dataType: 'json',
				success: function(data){
					$('#ajax-modal').modal('hide');
				}
			});
			
		});
		//显示部门树形结构
		$('#showdepart').click(function(){
			var len=0;
			var checkbox=$('.checkboxes');
			for(var i=0;i<checkbox.length;i++){
				if(checkbox[i].checked==true){
					len++;
					id=checkbox[i].value;
				}
			}
			if(len==0){
				alert("请勾选一个用户！");
			}else if(len>1){
				alert("只能勾选一个用户！");
			}else{
				app.ajax({
					url: app.rootPath + '/security/depart/getTree.do',
					type: 'POST',
					data: 'userId=' + id+ '&type=tree',
					dataType: 'json',
					success: function(data){
						var zNodes = [];
						var temp ;
						$.each(data,function(i,item){
							if(item['CHECKED'] == 'true'){
								temp = {id:item['ID'], pId: item['PID'], name: item['NAME'], checked: true };
							}else{
								temp = {id:item['ID'], pId: item['PID'], name: item['NAME'] };
							}
							zNodes.push(temp);
						});
						var setting = {
							check: {
								enable: true,
								chkStyle: "checkbox",
								chkboxType: { "Y": "s", "N": "s" }
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
								    var treeObj = $.fn.zTree.getZTreeObj("tree_3");
								    var node = treeObj.getNodeByTId(treeNode.tId);
								    treeObj.checkNode(node, true, true);
								}
							}
						};
						$.fn.zTree.init($("#tree_3"), setting, zNodes).expandAll(true);
					}
				});
				$('#ajax-modal1').modal('show');
			}
		});
		//保存
		$('#save-depart').click(function(){
			var treeObj = $.fn.zTree.getZTreeObj("tree_3");
			var nodes = treeObj.getCheckedNodes(true);
			var departIds=[];
			for(var i=0; i<nodes.length; i++){
				departIds.push(nodes[i].id);
				}
			app.ajax({
				url: app.rootPath + '/security/user/saveDepart.do',
				type: 'POST',
				data: 'departIds=' + departIds+'&userId='+id,
				dataType: 'json',
				success: function(data){
					$('#ajax-modal1').modal('hide');
				}
			});
			
		});
	}

	var user = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return user;
});