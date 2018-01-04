/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap','ztree', 'My97DatePicker', 'text!biz/report/taxpayment.html' ], function($, app, bootstrap,ztree, My97DatePicker, tpl) {
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
		$('#now_date').val(new Date().Format("yyyy-MM-dd"));
		new app.Page({
			url : app.rootPath + '/report/taxpayment/list.do',
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
						+'	<td>'+item['TAMID']+'</td>'
						+'	<td>'+item['PZZLDM']+'</td>'
						+'	<td>'+item['QSHM']+'</td>'
						+'	<td>'+item['JSHM']+'</td>'
						+'	<td><a data-type="SYSL" data-id="'+item['DEPARTID']+','+item['TAMID']+'">'+item['SY_NUM']+'</td>'
						+'	<td><a data-type="ZFSL" data-id="'+item['DEPARTID']+','+item['TAMID']+'">'+item['ZF_NUM']+'</a></td>'
						+'	<td><a data-type="ZSL" data-id="'+item['DEPARTID']+','+item['TAMID']+'">'+item['ZS_NUM']+'</a></td>'
						+'	<td>'+item['WSZ_STOCK_NUM']+'</td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	};
	
	var buildEvent = function(){
		/*$('#dept').on('click',function(){
			new app.Page({
				url : app.rootPath + '/report/taxpayment/listdept.do',
				target : 'page-div-dept',
				from: 'sub_dept',
				dataTarget: 'data-tbody-dept',
				buildData : function(data){
					var html = ''; 
					$.each(data,function(i,item){
						html += ''
							+'<tr data-id='+item['ID']+' data-name='+item['NAME']+'>'
							+'	<td>'+item['NAME']+'</td>'
							+'	<td>'+item['CODE']+'</td>'
							+'</tr>';
					});
					return html;
				}
			}).load();
			$('#modal-dept').modal('show');
		});*/
		$('#dept').on('click',function(){
			app.ajax({
				url : app.rootPath + '/report/taxpayment/listdept.do',
				dataType : 'json',
				success : function(data){
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
							    var treeObj = $.fn.zTree.getZTreeObj("treeDept");
							    var node = treeObj.getNodeByTId(treeNode.tId);
							    treeObj.checkNode(node, true, true);
							}
						}
					};
					$('#dept-tree').modal('show');
					$.fn.zTree.init($("#treeDept"), setting, zNodes);
				}
			});
		});
		$('#saveDept').on('click',function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDept");
			var nodes = treeObj.getCheckedNodes(true);
			var ids = [];
			var names = [];
			for(var i = 0; i < nodes.length; i++){
				ids.push(nodes[i].id);
			}
			for(var i = 0;i < nodes.length; i++){
				names.push(nodes[i].name);
			}
			if(ids.length == 0){
				$('#deptid').val("999");
				$('#dept').val("所有机构");
			} else {
				$('#deptid').val(ids);
				$('#dept').val(names);
			}
			$('#dept-tree').modal('hide');
		});
		
		$('#data-tbody').on('click','a',function(e){
			e.stopPropagation();
			$('.now_time').val($('#now_date').val());
			var a = $(this);
			if('ZFSL' == a.attr('data-type')){
				var daz = a.attr('data-id');
				$('#subm input').eq(0).val(daz.split(",")[0]);
				$('#subm input').eq(1).val(daz.split(",")[1]);
				$('.reset_1').val("");
				$('.show_tittle').html('完税证作废详情(TAM机编号:'+daz.split(",")[1]+')');
				new app.Page({
					url : app.rootPath + '/report/taxpayment/listNullify.do',
					from: 'subm',
					target : 'page-div-tail',
					dataTarget: 'data-tbody-tail',
					buildData : function(data){
						var html = ''; 
						$.each(data,function(i,item){
							if(null == item['PZZG']){
								item['PZZG'] = "-";
							}
							html += ''
								+'<tr>'
								+'	<td>'+item['DEPTNAME']+'</td>'
								+'	<td>'+item['TAMNAME']+'</td>'
								+'	<td>'+item['TAMID']+'</td>'
								+'	<td>'+item['PZZLDM']+'</td>'
								+'	<td>'+item['PZZG']+'</td>'
								+'	<td>'+item['PZHM']+'</td>'
								+'	<td>'+item['MODIFYTIME']+'</td>'
								+'</tr>';
						});
						return html;
					}
				}).load();
				$('#ajax-modal').modal('show');
			}
			if('SYSL' == a.attr('data-type')){
				var daz = a.attr('data-id');
				$('#subm_2 input').eq(0).val(daz.split(",")[0]);
				$('#subm_2 input').eq(1).val(daz.split(",")[1]);
				$('.reset_1').val("");
				$('.show_tittle').html('完税证使用详情(TAM机编号:'+daz.split(",")[1]+')');
				new app.Page({
					url : app.rootPath + '/report/taxpayment/listLevy.do',
					from: 'subm_2',
					target : 'page-div-levy',
					dataTarget: 'data-tbody-levy',
					buildData : function(data){
						var html = ''; 
						$.each(data,function(i,item){
							html += ''
								+'<tr>'
								+'	<td>'+item['DEPTNAME']+'</td>'
								+'	<td>'+item['TAMNAME']+'</td>'
								+'	<td>'+item['TAMID']+'</td>'
								+'	<td>'+item['NSRSBH']+'</td>'
								+'	<td>'+item['NSRMC']+'</td>'
								+'	<td>'+item['PZZLDM']+'</td>'
								+'	<td>'+item['PZHM']+'</td>'
								+'	<td>'+item['CREATETIME']+'</td>'
								+'</tr>';
						});
						return html;
					}
				}).load();
				$('#modal-levy').modal('show');
			}
			if('ZSL' == a.attr('data-type')){
				var daz = a.attr('data-id');
				$('.reset_1').val("");
				var daz = a.attr('data-id');
				$('#subm_3 input').eq(0).val(daz.split(",")[0]);
				$('#subm_3 input').eq(1).val(daz.split(",")[1]);
				$('.show_tittle').html('完税证明征收总数详情(TAM机编号:'+daz.split(",")[1]+')');
				new app.Page({
					url : app.rootPath + '/report/taxpayment/listTotal.do',
					from: 'subm_3',
					target : 'page-div-total',
					dataTarget: 'data-tbody-total',
					buildData : function(data){
						var html = ''; 
						$.each(data,function(i,item){
							html += ''
								+'<tr>'
								+'	<td>'+item['DEPTNAME']+'</td>'
								+'	<td>'+item['TAMNAME']+'</td>'
								+'	<td>'+item['TAMID']+'</td>'
								+'	<td>'+item['PZZLDM']+'</td>'
								+'	<td>'+item['NSRMC']+'</td>'
								+'	<td>'+item['NSRSBH']+'</td>'
								+'	<td>'+item['PZHM']+'</td>'
								+'	<td>'+item['CREATETIME']+'</td>'
								+'</tr>';
						});
						return html;
					}
				}).load();
				$('#modal-total').modal('show');
			}
		});
		
		$('#show-all-dept').on('click',function(){
			$('#deptid').val(999);
			$('#dept').val('所有机构');
			$('#modal-dept').modal('hide');
		});
		$('#excel').on('click',function(){
			var form = $("<form>");   //定义一个form表单
            form.attr('style', 'display:none');   //在form表单中添加查询参数
            form.attr('target', '');
            form.attr('method', 'post');
            form.attr('action', app.rootPath + '/report/taxpayment/exec.do');

            var input1 = $('<input>');
            input1.attr('type', 'hidden');
            input1.attr('name', 'time');
            input1.attr('value', $('#now_date').val());
            
            var input2 = $('<input>');
            input2.attr('type', 'hidden');
            input2.attr('name', 'dept');
            input2.attr('value', $('#deptid').val());
            
            var input3 = $('<input>');
            input3.attr('type', 'hidden');
            input3.attr('name', 'department');
            input3.attr('value', $('#dept').val());
            
            $('body').append(form);  //将表单放置在web中 
            form.append(input1);   //将查询参数控件提交到表单上
            form.append(input2);
            form.append(input3);
            form.submit();
            form.remove();
		});
	}
	
	var taxpayment = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return taxpayment;
});