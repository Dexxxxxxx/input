/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap','ztree', 'text!biz/security/role.html' ], function($, app, bootstrap, ztree, tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/security/role/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					if(item['NAME'] == null){
						item['NAME'] = '';
					}
					if(item['SN'] == null){
						item['SN'] = '';
					}
					if(item['DESCRIPTION'] == null){
						item['DESCRIPTION'] = '';
					}
					if(item['CREATETIME'] == null){
						item['CREATETIME'] = '';
					}
					html += ''
						+'<tr data-tr-id = '+item['ID']+'>'
						+'  <td><div name="checker" class="checker"><input type="checkbox" class="checkboxes" value="'+item['ID']+'"></div></td>'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['SN']+'</td>'
						+'	<td>'+item['DESCRIPTION']+'</td>'
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
		var roleId = '';
		
		$('#data-tbody').on('click','button',function(){
			var btn = $(this);
			if(btn.attr('data-id') != null && btn.attr('data-id') != ''){
				app._function = 'UPDATEROLE';
				app.role_id = btn.attr('data-id');
				app.loadPage('role_view');
			}else{
				alert('未选择查看的信息');
			}
		});
		$('#check_group').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.checkboxes').prop('checked',isChecked);
		});
		$('#btn-new').click(function(){
			app._function = 'ADDROLE';
			app.loadPage('role_view');
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
					url: app.rootPath + '/security/role/delete.do',
					data: 'ids=' + ids,
					success: function(data){
						alert('删除成功');
						loadData();
					}
				});
			}
		});
		$('#ajax-demo').on('click',function(){
			var len = 0;
			var checkbox = $('.checkboxes');
			for(var i = 0;i < checkbox.length; i ++){
				if(checkbox[i].checked == true){
					len ++;
					roleId = checkbox[i].value;
				}
			}
			if(len != 1){
				alert('选择一条信息');
			} else {
				app.ajax({
					url: app.rootPath + '/security/privilege/getTree.do',
					type: 'POST',
					data: 'roleId=' + roleId,
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
				
			}
		});
		$('#savePri').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getCheckedNodes(true);
			var ids = [];
			for(var i = 0; i < nodes.length; i++){
				ids[i] = nodes[i].id;
			}
			app.ajax({
				url: app.rootPath + '/security/role/savePrivilege.do',
				data: 'ids=' + ids + '&roleId=' + roleId,
				success: function(data){
					$('#ajax-modal').modal('hide');
				}
			});
		});
	}
	
	var role = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return role;
});