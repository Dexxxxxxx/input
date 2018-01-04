/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap','ztree','My97DatePicker', 'text!biz/invoiceStatistics/invoiceInfoAll.html' ], function($, app, bootstrap, ztree, My97DatePicker,tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/invoiceStatistics/invoiceInfo/listAll.do',
			target : 'page-div',
			from: 'search-form',
			limit: 20,
			dataTarget: 'data-tbody',
			buildData : function(data){
				var _count=data.count;
				$('#fpcount').html(_count['COUNT']);
				$('#fpzje').html(_count['HJJE']);
				$('#fpzse').html(_count['HJSE']);
				$('#fpjshj').html(_count['JSHJ']);
				var _data=data.page.data
				var html = ''; 
				$.each(_data,function(i,item){
//					var fplx=item['FPLX']=='004'?'增值税专票':(item['FPLX']=='007'?'增值税普票':(item['FPLX']=='009'?'货运专票':'机动车专票'))
					html += ''
						+'<tr data-tr-id = '+item['ID']+'>'
//						+'  <td><div name="checker" class="checker"><input type="checkbox" class="checkboxes" value="'+item['ID']+'"></div></td>'
						+'	<td>'+item['CODE']+'</td>'
						+'	<td>'+item['NUM']+'</td>'
						+'	<td>'+item['INDATE']+'</td>'
						+'	<td>'+item['DRAWERUNIT']+'</td>'
						+'	<td>'+item['HJJE']+'</td>'
						
						+'	<td>'+item['HJSE']+'</td>'
						+'	<td>'+item['JSHJ']+'</td>'
//						+'	<td>'+item['AUTHWAY']+'</td>'
//						+'	<td>'+item['AUTHTIME']+'</td>'
						+'	<td>'+item['INVOICE_TYPE']+'</td>'
						+'	<td>'+item['ISCHECK']+'</td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	};
	
	var buildEvent = function(){
		$('#authpoint').click(function(){
			//获取认证点
			var html = ''; 
			app.ajax({
				url: app.rootPath + '/incomeInvoice/listAuthPoint.do',
				type : 'POST',
				success: function(data){
					$.each(data,function(i,item){
						html += ''
							+'<tr data-tr-id = '+item['NSRSBH']+'>'
							+'  <td><div name="checker" class="checker"><input type="checkbox" class="authpointchecks" value="'+item['NSRSBH']+'"></div></td>'
							+'	<td>'+item['NAME']+'</td>'
							+'	<td>'+item['NSRSBH']+'</td>'
							+'</tr>';
					});
					$('#authpointdata-tbody').html(html);
					$("#authpoint-modal").modal('show');
				}
			});
		})
//		$('#xhdwmc').click(function(){
//			$('#xhdwmccheck_group').attr('checked',false);
//			new app.Page({
//				url : app.rootPath + '/incomeInvoice/listXhdwmc.do',
//				target : '',
//				from: 'search-form-disk',
//				limit: 1000,
//				dataTarget: 'xhdwmcdata-tbody',
//				buildData : function(data){
//					var html = ''; 
//					$.each(data,function(i,item){
//						html += ''
//							+'<tr data-tr-id = '+item['XHDWMC']+'>'
//							+'  <td><div name="checker" class="checker"><input type="checkbox" class="xhdwmcchecks" value="'+item['XHDWMC']+'"></div></td>'
//							+'	<td>'+item['XHDWMC']+'</td>'
//							+'</tr>';
//					});
//					return html;
//				}
//			}).load();
//			$("#xhdwmc-modal").modal('show');
//		})
		$('#authpointcheck_group').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.authpointchecks').prop('checked',isChecked);
		});
		$('#xhdwmccheck_group').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.xhdwmcchecks').prop('checked',isChecked);
		});
		//模态
		$('#surexhdwmc').click(function(){
			var ids = [];
			$('.xhdwmcchecks').each(function(){
				if($(this).is(':checked') == true){
					ids.push($(this).val());
				}
			});
			//将查出的数据放入框中
			if(ids.length==0){
				$('#xhdwmc').val("");
			}else{
				$('#xhdwmc').val(ids);
			}
			$("#xhdwmc-modal").modal('hide');
		});
		$('#sureauthpoint').click(function(){
			var ids = [];
			$('.authpointchecks').each(function(){
				if($(this).is(':checked') == true){
					ids.push($(this).val());
				}
			});
			//将查出的数据放入框中
			if(ids.length==0){
				$('#authpoint').val("");
			}else{
				$('#authpoint').val(ids);
			}
			$("#authpoint-modal").modal('hide');
		});
		$("#btn-import").click(function(){
			$("#fileImport").hide();
			$(".selectFile").show();
			$("#file").val("");
		});
		
		$(document).click(function(){
			$("#fileImport").show();
			$(".selectFile").hide();
		});
		$("#fileImport").click(function(e){
			e.stopPropagation();
		});
		$(".selectFile").click(function(e){
			e.stopPropagation();
		});
		//用form表单提交
		$('#uploadForm').attr('action','incomeInvoice/import.do');
		$('#uploadForm').on('submit', function(e) {
            	e.preventDefault();
				$(this).ajaxSubmit({
                    success: function(data){
                    	data = JSON.parse(data);
                    	if(data.result=="SUCCESS"){
                    		$("#ajax-import").modal("hide");
                    		alert(data.msg);
                    		loadData();
                    	}else{
                    		alert(data.msg);
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
	}
	
	var incomeInvoice = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return incomeInvoice;
});