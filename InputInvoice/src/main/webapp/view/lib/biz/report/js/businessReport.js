/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap', 'My97DatePicker','ztree','text!biz/report/businessReport.html' ], function($, app, bootstrap,My97DatePicker,ztree ,tpl) {
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
		$('#start_date').val(new Date().Format("yyyy-MM-dd"));
		$('#search-form').submit();
	};
	
	var buildEvent = function(){
		$('#search-form').attr('action','report/businessReport/list.do');
		$('#search-form').on('submit', function(e) {
			e.preventDefault();
			$(this).ajaxSubmit({
				success : function(data){
					data = JSON.parse(data);
					if(data['result'] == 'SUCCESS'){
						var html = '';
						$.each(data['rows'],function(i,item){
							if(null == item['TOPNAME'] || '' == item['TOPNAME']){
								item['TOPNAME'] = '-';
							}
							if(null == item['SUPERNAME'] || '' == item['SUPERNAME']){
								item['SUPERNAME'] = '-';
							}
							html += ''
								+'<tr>'
								+'	<td>'+item['TOPNAME']+'</td>'
								+'	<td>'+item['SUPERNAME']+'</td>'
								+'	<td>'+item['DEPTNAME']+'</td>'
								+'	<td>'+item['TAMNAME']+'</td>'
								+'	<td>'+item['TAM_ID']+'</td>'
								+'	<td class="text-right">'+item['ZSSL']+'</td>'
								+'	<td class="text-right">'+item['YBTSE']+'</td>'
								+'	<td class="text-right">0.00</td>'
								+'	<td class="text-right">0.00</td>'
								+'	<td class="text-right">0.00</td>'
								+'	<td class="text-right">'+item['YBTSE']+'</td>'
								+'</tr>';
						});
						$('#data-tbody').html(html);
						$('#data-tbody tr:last td:lt(4)').html("");
						$('#data-tbody tr:last td:nth-child(5)').html('<strong>合   计</strong>');
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
		
		$('#tamname').on('click',function(){
			app.ajax({
				url : app.rootPath + '/report/businessReport/listtam.do',
				dataType : 'json',
				success : function(data){
					var zNodes = [];
					var temp ;
					$.each(data,function(i,item){
						temp = {id:item['ID'],tid:item['TAM_ID'], pId: item['PID'], name: item['NAME'] };
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
					$('#tam-modal').modal('show');
					$.fn.zTree.init($("#tree_3"), setting, zNodes);
				}
			});
		});
		$('#save-tam').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("tree_3");
			var nodes = treeObj.getCheckedNodes(true);
			var ids = [];
			var names = [];
			for(var i = 0; i < nodes.length; i++){
				ids.push(nodes[i].tid);
			}
			for(var i = 0;i < nodes.length; i++){
				names.push(nodes[i].name);
			}
			if(ids.length == 0){
				$('#tamid').val("999");
				$('#tamname').val("所有TAM机");
			} else {
				$('#tamid').val(ids);
				$('#tamname').val(names);
			}
			$('#tam-modal').modal('hide');
		});
		$('#excel').on('click',function(){
			var form = $("<form>");   //定义一个form表单
            form.attr('style', 'display:none');   //在form表单中添加查询参数
            form.attr('target', '');
            form.attr('method', 'post');
            form.attr('action', app.rootPath + '/report/businessReport/exec.do');

            var input1 = $('<input>');
            input1.attr('type', 'hidden');
            input1.attr('name', 'start_date');
            input1.attr('value', $('#start_date').val());
            
            var input2 = $('<input>');
            input2.attr('type', 'hidden');
            input2.attr('name', 'tamid');
            input2.attr('value', $('#tamid').val());
//            
//            var input3 = $('<input>');
//            input3.attr('type', 'hidden');
//            input3.attr('name', 'deptname');
//            input3.attr('value', $('#dept').val());
            
            var input4 = $('<input>');
            input4.attr('type', 'hidden');
            input4.attr('name', 'end_date');
            input4.attr('value', $('#end_date').val());
            
            $('body').append(form);  //将表单放置在web中 
            form.append(input1);   //将查询参数控件提交到表单上
            form.append(input2);
//            form.append(input3);
            form.append(input4);
            form.submit();
            form.remove();
		});
		
	};
	
	var businessReport = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return businessReport;
});