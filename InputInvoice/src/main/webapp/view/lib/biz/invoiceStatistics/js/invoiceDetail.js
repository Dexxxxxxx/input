define([ 'jquery', 'app', 'My97DatePicker','ztree','text!biz/invoiceStatistics/invoiceDetail.html' ], function($, app,My97DatePicker,ztree,tpl) {
	
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/invoiceStatistics/invoiceInfo/detailList.do',
			target : 'page-div',
			from: 'search-form',
			limit :10,
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr>'
						+'  <td><div class="checker"><span><input type="checkbox"  class="checkboxes" value="'+item['ID']+'"></span></div></td>'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['CUSER']+'</td>'
						+'	<td>'+item['CODE']+'</td>'
						+'	<td>'+item['NUM']+'</td>'
						+'	<td>'+item['XMMC']+'</td>'
						
						+'	<td>'+item['XMJE']+'</td>'
						+'	<td>'+item['SHULIANG']+'</td>'
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
			form.attr("action",app.rootPath + '/invoiceStatistics/invoiceInfo/exportDetailList.do');
			var input1=$("<input>");
			input1.attr("type","hidden");input1.attr("name","sl");input1.attr("value",$("#sl").val());
			var input2=$("<input>");
			input2.attr("type","hidden");input2.attr("name","xmmc");input2.attr("value",$("#xmmc").val());
//			invoice_type
//			var input3=$("<input>");
//			input3.attr("type","hidden");input3.attr("name","invoice_type");input3.attr("value",$("#invoice_type").val());
//			var input4=$("<input>");
//			input4.attr("type","hidden");input4.attr("name","check_state");input4.attr("value",$("#check_state").val());
//			var input5=$("<input>");
//			input5.attr("type","hidden");input5.attr("name","pid");input5.attr("value",$("#pid").val());
//			var input6=$("<input>");
//			input6.attr("type","hidden");input6.attr("name","emp_name");input6.attr("value",$("#emp_name").val());
			$("#row").append(form);
			form.append(input1);
			form.append(input2);
//			form.append(input3);
//			form.append(input4);
//			form.append(input5);
//			form.append(input6);
			form.ajaxSubmit({
           	 success : function(data){
           		
	      	    },
	      	    error : function(xhr, errorType, error) {
	      	    	alert(1);
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
	};
	var invoiceDetail = {
			init : function() {
				app.mainContainer.html(tpl);
				buildEvent();
				loadData();
			}
		};
		return invoiceDetail;
});
