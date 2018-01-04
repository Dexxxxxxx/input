/**
 * 
 */
define([ 'jquery', 'app','bootstrap', 'jqueryForm','text!biz/sermanager/deprocess_view.html' ], function($, app, bootstrap, jqueryForm, tpl) {
	var loadData = function(){
		var id = app.deprocess_orderid;
		app.ajax({
			url: app.rootPath + '/sermanager/deprocess/getById.do',
			type: 'POST',
			data:'orderid=' + id,
			dataType: 'json',
			success: function(data){
				data['certificate']['IMPORTED'] = (data['certificate']['IMPORTED']=='1'?'国产':'进口');
				data['declaration']['JKBZ'] = (data['declaration']['JKBZ']=='N'?'国产':'其他');
				data['declaration']['NSRLB'] = (data['declaration']['NSRLB']=='0'?'个人':'企业');
				data['pos']['STATUS'] = (data['pos']['STATUS']=='0'?'未支付':(data['pos']['STATUS']=='1'?'成功支付':'支付异常'));
				
				data['paid']['BILLBALANCE'] = (data['paid']['BILLBALANCE']=='Y'?'已经获取':'未获取');
				data['paid']['PRINTPAYPAID'] = (data['paid']['PRINTPAYPAID']=='Y'?'已经打印':'未打印');
				data['paid']['PRINTSTATUS'] = (data['paid']['PRINTSTATUS']=='0'?'未打印':'打印');
				data['paid']['RECODE'] =(data['paid']['RECODE'] =='1'?'正常状态允许发放':(data['paid']['RECODE']=='2'?'税款未上解不允许发放':data['paid']['RECODE']=='3'?'已发放，不允许重复发放':''))

				data['receipt']['BILLBALANCE'] = (data['receipt']['BILLBALANCE']=='Y'?'已经获取':'未获取');
				data['receipt']['PRINTPAYPAID'] = (data['receipt']['PRINTPAYPAID']=='Y'?'已经打印':'未打印');
				data['receipt']['PRINTSTATUS'] = (data['receipt']['PRINTSTATUS']=='0'?'未打印':'打印');
				$('#cst_taxpayer td').each(function(i, item){
					$(this).html(data['taxpayer'][$(this).attr('id')]);
				});
				$('#cst_certificate td').each(function(i, item){
					$(this).html(data['certificate'][$(this).attr('id')]);
				});
				$('#cst_certificate #XML').html(data['certificate']['XML']);
				$('#cst_declaration td').each(function(i, item){
					$(this).html(data['declaration'][$(this).attr('id')]);
				});
				$('#cst_declaration #XML').html(data['declaration']['XML']);
				$('#cst_pay_car td').each(function(i, item){
					$(this).html(data['car'][$(this).attr('id')]);
				});
				$('#cst_pay_car #MAINCFG').html(data['car']['MAINCFG']);
				
				$('#cst_pay_pos td').each(function(i, item){
					$(this).html(data['pos'][$(this).attr('id')]);
				});
				$('#cst_pay_receipt td').each(function(i, item){
					$(this).html(data['receipt'][$(this).attr('id')]);
				});
				$('#cst_declaration #REMARKS').html(data['receipt']['BZ']);
				$('#cst_pay_invoice td').each(function(i, item){
					$(this).html(data['invoice'][$(this).attr('id')]);
				});
				$('#cst_pay_paid td').each(function(i, item){
					$(this).html(data['paid'][$(this).attr('id')]);
				});
				$('#cst_taxes_info td').each(function(i,item){
					$(this).html(data['taxesinfo'][$(this).attr('id')]);
				});
				$('#cst_taxes_info #BZ').html(data['taxesinfo']['BZ']);
				$('#cst_pay_receipt #BZ').html(data['receipt']['BZ']);
				var html_motor='';
				$.each(data['motorcycle'],function(i,item){
					html_motor+=''
						+'<tr role="row" >'
						+'	<td>'+item['CLLBDM1']+'</td>'
						+'	<td>'+item['CLLXDM']+'</td>'
						+'	<td>'+item['JGLX']+'</td>'
						+'	<td>'+item['ZDJSJG']+'</td>'
						+'	<td>'+item['CLXLH']+'</td>'
						+'	<td>'+item['CLXH']+'</td>'
						+'	<td>'+item['CLCP']+'</td>'
						+'	<td>'+item['CLCPXH']+'</td>'
						+'	<td>'+item['CLDW']+'</td>'
						+'	<td>'+item['ZWS']+'</td>'
						+'	<td>'+item['PQL']+'</td>'
						+'	<td>'+item['CLSCQYMC']+'</td>'
						+'	<td>'+item['CDSX']+'</td>'
						+'	<td><div style="width:100%;height: 80px; overflow-y: auto; overflow-x: hidden;">'+item['ZYPZ']+'</div></td>'
						+'</tr>';
				});
				$('#data-tbody-motor').html(html_motor);
				var html_script='';
				$.each(data['script'],function(i,item){
					html_script+=''
						+'<tr role="row" >'
						+'	<td>'+item['TASKNAME']+'</td>'
						+'	<td>'+item['ITEMNAME']+'</td>'
						+'	<td>'+item['PROCESS_CODE']+'</td>'
						+'	<td>'+item['EXEC_TAKE']+'</td>'
						+'	<td>'+item['EXEC_TIME']+'</td>'
						+'	<td>'+item['EXEC_RESULT']+'</td>'
						+'	<td>'+item['MSG']+'</td>'
						+'	<td><div style="width:100%;height: 80px; overflow-y: auto; overflow-x: hidden;">'+item['EXEC_REMARKS']+'</div></td>'
						+'</tr>';
				});
				$('#data-tbody-script').html(html_script);
				$('#getOrderId').val(data['info']['ID']);
				var html_info = '';
				data['info']['STATUS']=(data['info']['STATUS']=='0'?'正常':(data['info']['STATUS']=='999'?'完成':'业务删除'));
				data['info']['NSRLB']=(data['info']['NSRLB']=='0'?'个人':'企业');
				html_info+=''
					+'<tr class="even" role="row">'
					+'	<td>'+data['info']['TAM_ID']+'</td>'
					+'	<td>'+data['info']['TAMNAME']+'</td>'
					+'	<td>'+data['info']['USERID']+'</td>'
					+'	<td>'+data['info']['NSRLB']+'</td>'
					+'	<td>'+data['info']['NAME']+'</td>'
					+'	<td>'+data['info']['NSRSBH']+'</td>'
					+'	<td>'+data['info']['ZJHM']+'</td>'
					+'	<td>'+data['info']['VIN']+'</td>'
					+'	<td>'+data['info']['MMODEL']+'</td>'
					+'	<td>'+data['info']['PROCESS_NAME']+'</td>'
					+'	<td>'+data['info']['STATUS']+'</td>'
					+'	<td>'+data['info']['PROCESS_STATUS']+'</td>'
					+'	<td>'+data['info']['CREATETIME']+'</td>'
					+'</tr>';
				$('#data-tbody-info').html(html_info);
			}
		});
	}
	var buildEvent = function(){
		//刷新交易状态
		$('#btn-pay-pos').on('click',function(){
			$('#warning').html("");
			$('#ajax-modal').modal('show');
			$('#veri_form').attr('action','sermanager/deprocess/newPay.do');
			$('#veri_form').unbind('submit').on('submit',function(e){
				e.preventDefault();
				if(app.form_submit == true){
					alert("正在处理..请不要重复点击!");
					return;
				}
				app.form_submit = true;
				$(this).ajaxSubmit({
					data: {'orderid' : $('#getOrderId').val(), 't' : new Date().getTime()},
					success: function(data){
						app.form_submit = false;
						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							$('#ajax-modal').modal('hide');
							loadData();
						}else{
							$('#ajax-modal').modal('hide');
							alert(data['msg']);
						}
					},
					error : function(xhr, errorType, error) {
						app.form_submit = false;
						$('#ajax-modal').modal('hide');
						if(xhr['status'] == '600'){
							alert('未登录!');
							window.location.href = "login.html";
						} else if(xhr['status'] == '401'){
							alert('您没有该操作的访问权限!');
						} else {
							alert('出错了! error:'+xhr['status']);
						}
					}
				});
			});
		});
		//通过缴费
		$('#btn-pass-pos').on('click',function(){
			$('#warning').html("");
			$('#ajax-modal').modal('show');
			$('#veri_form').attr('action','sermanager/deprocess/newPass.do');
			$('#veri_form').unbind('submit').on('submit',function(e){
				e.preventDefault();
				if(app.form_submit == true){
					alert("正在处理..请不要重复点击!");
					return;
				}
				app.form_submit = true;
				$(this).ajaxSubmit({
					data: {'orderid' : $('#getOrderId').val()},
					success: function(data){
						app.form_submit = false;
						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							$('#ajax-modal').modal('hide');
							alert('操作成功');
							loadData();
						}else{
							$('#ajax-modal').modal('hide');
							alert(data['msg']);
						}
					},
					error : function(xhr, errorType, error) {
						app.form_submit = false;
						$('#ajax-modal').modal('hide');
						if(xhr['status'] == '600'){
							alert('未登录!');
							window.location.href = "login.html";
						} else if(xhr['status'] == '401'){
							alert('您没有该操作的访问权限!');
						} else {
							alert('出错了! error:'+xhr['status']);
						}
					}
				});
			});
		});
		//税票作废
		$('#btn-sp').on('click',function(){
			$('#warning').html(" *在本系统此功能完成之后,税局金三系统中再次无法作废，请在税局系统中再次作废！(操作后，会自动更新业务单据新的结存情况,请注意!)");
			$('#ajax-modal').modal('show');
			$('#veri_form').attr('action','sermanager/deprocess/newStamps.do');
			$('#veri_form').unbind('submit').on('submit',function(e){
				e.preventDefault();
				if(app.form_submit == true){
					alert("正在处理..请不要重复点击!");
					return;
				}
				app.form_submit = true;
				$(this).ajaxSubmit({
					data: {'orderid' : $('#getOrderId').val()},
					success: function(data){
						app.form_submit = false;
						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							$('#ajax-modal').modal('hide');
							alert('操作成功');
							loadData();
						}else{
							$('#ajax-modal').modal('hide');
							alert(data['msg']);
						}
					},
					error : function(xhr, errorType, error) {
						app.form_submit = false;
						$('#ajax-modal').modal('hide');
						if(xhr['status'] == '600'){
							alert('未登录!');
							window.location.href = "login.html";
						} else if(xhr['status'] == '401'){
							alert('您没有该操作的访问权限!');
						} else {
							alert('出错了! error:'+xhr['status']);
						}
					}
				});
			});
		});
		//完税证作废
		$('#btn-wsz').on('click',function(){
			$('#warning').html(" *在本系统此功能完成之后, 税局金三系统中会同时作废！(操作后，会自动更新业务单据新的结存情况,请注意!)");
			$('#ajax-modal').modal('show');
			$('#veri_form').attr('action','sermanager/deprocess/newPayPaid.do');
			$('#veri_form').unbind('submit').on('submit',function(e){
				e.preventDefault();
				if(app.form_submit == true){
					alert("正在处理..请不要重复点击!");
					return;
				}
				app.form_submit = true;
				$(this).ajaxSubmit({
					type: "POST",
					data: {'orderid' : $('#getOrderId').val()},
					success: function(data){
						app.form_submit = false;
						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							$('#ajax-modal').modal('hide');
							alert('操作成功');
							loadData();
						}else{
							$('#ajax-modal').modal('hide');
							alert(data['msg']);
						}
					},
					error : function(xhr, errorType, error) {
						app.form_submit = false;
						$('#ajax-modal').modal('hide');
						if(xhr['status'] == '600'){
							alert('未登录!');
							window.location.href = "login.html";
						} else if(xhr['status'] == '401'){
							alert('您没有该操作的访问权限!');
						} else {
							alert('出错了! error:'+xhr['status']);
						}
					}
				});
			});
		});
		//整个申报作废
		$('#btn-revoke').on('click',function(){
			$('#warning').html(" *在本系统此功能完成之后, 税局金三系统中会同时作废！");
			$('#ajax-modal').modal('show');
			$('#veri_form').attr('action','sermanager/deprocess/deleteDeclare.do');
			$('#veri_form').unbind('submit').on('submit',function(e){
				e.preventDefault();
				$(this).ajaxSubmit({
					type: "POST",
					data: {'orderid' : $('#getOrderId').val()},
					success: function(data){
						data = JSON.parse(data);
						if(data['result'] == 'SUCCESS'){
							$('#ajax-modal').modal('hide');
							alert('操作成功');
							loadData();
						}else{
							$('#ajax-modal').modal('hide');
							alert(data['msg']);
						}
					},
					error : function(xhr, errorType, error) {
						$('#ajax-modal').modal('hide');
						if(xhr['status'] == '600'){
							alert('未登录!');
							window.location.href = "login.html";
						} else if(xhr['status'] == '401'){
							alert('您没有该操作的访问权限!');
						} else {
							alert('出错了! error:'+xhr['status']);
						}
					}
				});
			});
		});
		$('#btn-ref').on('click',function(){
			loadData();
		});
		$('#btn-cancel').on('click',function(){
			app.loadPage('deprocess');
		});
		$('#btn-del').on('click',function(){
			if(app._status=="-1"){
				alert("业务已经删除！");
			}else if(app._status=="999"){
				 alert("流程状态已完成不能删除！");
			}else{
				if(confirm("你确定要删除？")){
					 app.ajax({
							url:app.rootPath + '/sermanager/deprocess/update.do',
							type:'POST',
							data: {'ids' : $('#getOrderId').val()},
							success:function(data){
								loadData();
						 }
					});
				 }
			} 
		});
	}
	
	var deprocess_view = {
			init : function() {
				app.mainContainer.html(tpl);
				buildEvent();
				loadData();
			}
		};
		return deprocess_view;
});