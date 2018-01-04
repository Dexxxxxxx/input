/**
 * 
 */
define([ 'jquery', 'app','ztree', 'bootstrap', 'My97DatePicker', 'text!biz/report/dereport.html' ], function($, app, ztree, bootstrap, My97DatePicker, tpl) {
	Date.prototype.Format = function (fmt) { 
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	};
	var loadData = function(){
		//默认选中当天时间  yyyy-MM-dd
		$('#time').val(new Date().Format("yyyy-MM-dd"));
		$('#end_time').val(new Date().Format("yyyy-MM-dd"));
		new app.Page({
			url : app.rootPath + '/report/dereport/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr>'
						+'	<td>'+item['DEPTNAME']+'</td>'
						+'	<td>'+item['TAMNAME']+'</td>'
						+'	<td>'+item['TAM_ID']+'</td>'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['NSRSBH']+'</td>'
						+'	<td>'+item['VIN']+'</td>'
						+'	<td>'+item['FDJH']+'</td>'
						+'	<td>'+item['CLCPXH']+'</td>'
						+'	<td>'+item['SPHM']+'</td>'
						+'	<td>'+item['WSZHM']+'</td>'
						+'	<td  class="text-right">'+item['YBTSE']+'</td>'
						+'	<td>'+item['SKSSQQ']+'</td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	};
	
	var buildEvent = function(){
		$('#search-form').attr('action','report/tamreport/count.do');
		$('#search-form').on('submit', function(e) {
            e.preventDefault();
            	$(this).ajaxSubmit({
                    success: function(data){
                    	data = JSON.parse(data);
                    	if(data['result'] == 'SUCCESS'){
                    		$('#daycount').html(data['rows']['byday']['ZZSL']);
                    		$('#daypospayment').html(data['rows']['byday']['ZSHJ']);
                    		$('#monthcount').html(data['rows']['bymonth']['ZZSL']);
                    		$('#monthpospayment').html(data['rows']['bymonth']['ZSHJ']);
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
		});
		$('.datePicker').click(function(){
			WdatePicker();
		}); 
		$('#dept').on('click', function(e){
//			$('#search_key').val('');
//			new app.Page({
//				url : app.rootPath + '/report/dereport/listdept.do',
//				target : 'page-div-tail',
//				from: 'tam-search',
//				dataTarget: 'data-tbody-tail',
//				buildData : function(data){
//					var html = ''; 
//					$.each(data,function(i,item){
//						html += ''
//							+'<tr data-id='+item['ID']+' data-name='+item['NAME']+'>'
//							+'	<td>'+item['NAME']+'</td>'
//							+'	<td>'+item['CODE']+'</td>'
//							+'</tr>';
//					});
//					return html;
//				}
//			}).load();
//			$('#ajax-modal').modal('show');
			app.ajax({
				url: app.rootPath + '/report/dereport/listdept.do',
				type: 'POST',
				dataType: 'json',
				success: function(data){
//					console.log(data);
					var zNodes = [];
					var temp ;
					$.each(data,function(i,item){
						temp = {id:item['ID'], pId: item['PID'], name: item['NAME'] };
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
					$.fn.zTree.init($("#tree_3"), setting, zNodes).expandAll(false);
				}
			});
			$('#ajax-modal1').modal('show');
		});
		$('#save-depart').click(function(){
			var treeObj = $.fn.zTree.getZTreeObj("tree_3");
			var nodes = treeObj.getCheckedNodes(true);
			var departIds=[];
			var departNames=[];
			for(var i=0; i<nodes.length; i++){
				departIds.push(nodes[i].id);
				departNames.push(nodes[i].name);
				}
			$('#deptid').val(departIds);
			$('#dept').val(departNames);
			$('#ajax-modal1').modal('hide');
//			app.ajax({
//				url: app.rootPath + '/security/user/saveDepart.do',
//				type: 'POST',
//				data: 'departIds=' + departIds+'&userId='+id,
//				dataType: 'json',
//				success: function(data){
//					$('#ajax-modal1').modal('hide');
//				}
//			});
			
		});
		
		$('#data-tbody-tail').on('click','tr', function(){
			$('#search_key').val('');
			//将tam机名称赋值到input显示标签
			$('#dept').val($(this).attr('data-name'));
			//将tamid编号赋值到input隐藏标签
			$('#deptid').val($(this).attr('data-id'));
			//关闭模态
			$('#ajax-modal').modal('hide');
			
		});
		$('#showall').on('click', function(e){
			$('#dept').val('所有机构');
			$('#deptid').val('999');
			$('#ajax-modal').modal('hide');
		});
		$('#excel').on('click',function(){
			var form = $("<form>");   //定义一个form表单
            form.attr('style', 'display:none');   //在form表单中添加查询参数
            form.attr('target', '');
            form.attr('method', 'post');
            form.attr('action', app.rootPath + '/report/dereport/exec.do');

            var input1 = $('<input>');
            input1.attr('type', 'hidden');
            input1.attr('name', 'time');
            input1.attr('value', $('#time').val());
            
            var input2 = $('<input>');
            input2.attr('type', 'hidden');
            input2.attr('name', 'dept');
            input2.attr('value', $('#deptid').val());
            
            var input3 = $('<input>');
            input3.attr('type', 'hidden');
            input3.attr('name', 'deptname');
            input3.attr('value', $('#dept').val());
            
            var input4 = $('<input>');
            input4.attr('type', 'hidden');
            input4.attr('name', 'end_time');
            input4.attr('value', $('#end_time').val());
            
            var input5 = $('<input>');
            input5.attr('type', 'hidden');
            input5.attr('name', 'stampnumber');
            input5.attr('value', $('#stampnumber').val());
            
            $('body').append(form);  //将表单放置在web中 
            form.append(input1);   //将查询参数控件提交到表单上
            form.append(input2);
            form.append(input3);
            form.append(input4);
            form.append(input5);
            form.submit();
            form.remove();
		});
	}
	
	var dereport = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return dereport;
});