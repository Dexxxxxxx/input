define([ 'jquery', 'app', 'My97DatePicker','ztree','text!biz/invoiceStatistics/invoiceInfo.html' ], function($, app,My97DatePicker,ztree,tpl) {
	
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/invoiceStatistics/invoiceInfo/list.do',
			target : 'page-div',
			from: 'search-form',
			limit :5,
			dataTarget: 'data-tbody',
			buildData : function(data){
			if(data.count!=undefined){
				var _count=data.count;
				$('#fpcount').html(_count['COUNT']);
				$('#fpzje').html(_count['HJJE']);
				$('#fpzse').html(_count['HJSE']);
				$('#fpjshj').html(_count['JSHJ']);
				}else{
					$('#fpcount').html("0");
					$('#fpzje').html("0");
					$('#fpzse').html("0");
					$('#fpjshj').html("0");
				}
				var _data=data.page.data;
				var html = ''; 
				$.each(_data,function(i,item){
					html += ''
						+'<tr>'
						+'  <td><div class="checker"><span><input type="checkbox"  class="checkboxes" value="'+item['INVOICE_ID']+'"></span></div></td>'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['DPNAME']+'</td>'
						+'	<td>'+item['CUSER']+'</td>'
						+'	<td>'+item['CODE']+'</td>'
						+'	<td>'+item['NUM']+'</td>'
						+'	<td>'+item['PURCHASEUNIT']+'</td>'
						+'	<td>'+item['DRAWERUNIT']+'</td>'
						+'	<td>'+item['INDATE']+'</td>'
						+'	<td>'+item['CHECKDATE']+'</td>'
						+'	<td>'+item['ISCHECK']+'</td>'
						+'	<td>'+item['XMMC']+'</td>'
						+'	<td>'+item['XMJE']+'</td>'
						+'	<td>'+item['SHUILV']+'</td>'
						+'	<td>'+item['SE']+'</td>'
						+'	<td>'+item['JSHJ']+'</td>'
					
						+'</tr>';
				});
				return html;
			}
		}).load();
		
	};
	
	var buildEvent = function(){
		$('#export').on('click',function(){

			var form=$("<form>");//定义一个form表单
			form.attr("style","display:none");
			form.attr("target","");
			form.attr("method","post");
			form.attr("action",app.rootPath + '/invoiceStatistics/invoiceInfo/exportList.do');
			var input1=$("<input>");
			input1.attr("type","hidden");input1.attr("name","start_date");input1.attr("value",$("#start_date").val());
			var input2=$("<input>");
			input2.attr("type","hidden");input2.attr("name","end_date");input2.attr("value",$("#end_date").val());
			invoice_type
			var input3=$("<input>");
			input3.attr("type","hidden");input3.attr("name","invoice_type");input3.attr("value",$("#invoice_type").val());
			var input4=$("<input>");
			input4.attr("type","hidden");input4.attr("name","check_state");input4.attr("value",$("#check_state").val());
			var input5=$("<input>");
			input5.attr("type","hidden");input5.attr("name","pid");input5.attr("value",$("#pid").val());
			var input6=$("<input>");
			input6.attr("type","hidden");input6.attr("name","emp_name");input6.attr("value",$("#emp_name").val());
			$("#row").append(form);
			form.append(input1);
			form.append(input2);
			form.append(input3);
			form.append(input4);
			form.append(input5);
			form.append(input6);
			form.ajaxSubmit({
           	 success : function(data){
           		
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
			form.submit();
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
	};
	var invoiceInfo = {
			init : function() {
				app.mainContainer.html(tpl);
				buildEvent();
				loadData();
			}
		};
		return invoiceInfo;
});
