/**
 * 
 */
define([ 'jquery', 'app', 'ztree', 'jqueryForm','My97DatePicker','text!biz/tobacco/baccomanage.html' ], function($, app, ztree, jqueryForm,My97DatePicker, tpl) {
	var backmsg='';
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/tobacco/invoice/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				if($('#invoice_type').val()=='purchase'){
						app.invoiceType='007'
						$('#xsfORghf').html("销售方");
						$('#xsfORghf').css("width","4.5%");
						$('#bz').css("width","19%");
						$.each(data,function(i,item){
							var type="烟草收购";
							html += ''
								+'<tr data-id="'+item['ID']+'">'
								+'	<td>'+item['FPQQLSH']+'</td>'//交易流水号
								+'	<td>'+type+'</td>' //发票种类
								+'	<td>'+item['XSF_NAME']+'</td>'//销售方名称
								+'	<td>'+item['FPDM']+'</td>'//发票代码
								+'	<td>'+item['FPHM']+'</td>'//发票号码
								+'	<td>'+item['HJJE']+'</td>'//合计金额
								+'	<td>'+item['JSHJ']+'</td>'//价税合计
								+'	<td>'+item['KPR']+'</td>'//开票人
								+'	<td>'+item['KPF_NAME']+'</td>'//开票人
								+'	<td>'+item['BZ']+'</td>'//备注
								+'	<td>'+item['CREATETIME']+'</td>';//开票时间
							
							if(item['JYLSHID']==''){
								html += ''
									+'	<td>'
									+'		<div>'
									+'已回滚'
									+'		</div>'
									+'	</td>'
									+'</tr>';
							}else{
								if($('#invoice_status').val()=='normal'){
									html += ''
										+'	<td>'
										+'		<div>'
										+'				<button class="btn sbold green btn-sm" btn-type="cancel" data-id="'+item['ID']+'" data-fphm="'+item['FPHM']+'" data-fpdm="'+item['FPDM']+'" data-hjje="'+item['HJJE']+'">'
										+'				作废 <i class="fa fa-edit"></i>'
										+'				</button>'
										+'              <button class="btn sbold green btn-sm" btn-type="print" data-fphm="'+item['FPHM']+'" data-fpdm="'+item['FPDM']+'" data-id="'+item['ID']+'">'
										+'				打印 <i class="fa fa-print"></i>'
										+'				</button>'
										+'		</div>'
										+'	</td>'
										+'</tr>';
								}else if($('#invoice_status').val()=='cancel'){
									html += ''
										+'	<td>'
										+'		<div>'
										+'				<button class="btn sbold green " btn-type="redoInvoice" data-id="'+item['ID']+'" data-fphm="'+item['FPHM']+'" data-fpdm="'+item['FPDM']+'" data-hjje="'+item['HJJE']+'">'
										+'				重新开票 <i class="fa fa-edit"></i>'
										+'				</button>'
										+'		</div>'
										+'	</td>'
										+'</tr>';
								}else if($('#invoice_status').val()=='failed'){
									html += ''
										+'	<td>'
										+'		<div>'
										+'				<button class="btn sbold green btn-sm" btn-type="doInvoice" data-id="'+item['ID']+'" data-fphm="'+item['FPHM']+'" data-fpdm="'+item['FPDM']+'" data-hjje="'+item['HJJE']+'">'
										+'				开票 <i class="fa fa-edit"></i>'
										+'				</button>'
										+'		</div>'
										+'	</td>'
										+'</tr>';
								}
							}
						});
				}else{
						app.invoiceType='004'
						$('#xsfORghf').html("购买方");
						$('#xsfORghf').css("width","10%");
						$('#bz').css("width","10%");
						$.each(data,function(i,item){
							var type="烟草转售";
							html += ''
								+'<tr data-id="'+item['ID']+'">'
								+'	<td>'+item['FPQQLSH']+'</td>'//交易流水号
								+'	<td>'+type+'</td>' //发票种类
								+'	<td>'+item['GHF_NAME']+'</td>'//销售方名称
								+'	<td>'+item['FPDM']+'</td>'//发票代码
								+'	<td>'+item['FPHM']+'</td>'//发票号码
								+'	<td>'+item['HJJE']+'</td>'//合计金额
								+'	<td>'+item['JSHJ']+'</td>'//价税合计
								+'	<td>'+item['KPR']+'</td>'//开票人
								+'	<td>'+item['KPF_NAME']+'</td>'//开票人
								+'	<td>'+item['BZ']+'</td>'//备注
								+'	<td>'+item['CREATETIME']+'</td>';//开票时间
							
								if($('#invoice_status').val()=='normal'){
									html += ''
										+'	<td>'
										+'		<div>'
										+'				<button class="btn sbold green btn-sm" btn-type="cancel" data-id="'+item['ID']+'" data-fphm="'+item['FPHM']+'" data-fpdm="'+item['FPDM']+'" data-hjje="'+item['HJJE']+'">'
										+'				作废 <i class="fa fa-edit"></i>'
										+'				</button>'
										+'              <button class="btn sbold green btn-sm" btn-type="print" data-fphm="'+item['FPHM']+'" data-fpdm="'+item['FPDM']+'" data-id="'+item['ID']+'">'
										+'				打印 <i class="fa fa-print"></i>'
										+'				</button>'
										+'		</div>'
										+'	</td>'
										+'</tr>';
								}else if($('#invoice_status').val()=='cancel'){
									html += ''
										+'	<td>'
										+'		<div>'
										+'已作废'
										+'		</div>'
										+'	</td>'
										+'</tr>';
								}
						});
				}
				return html;
			}
		}).load();
		setting();
	};
	var loadData_items=function(){
		app.ajax({
			url:app.rootPath + '/invoice/tobacco/getInvoiceDetail.do?invoice_id='+app.pid,
			type:'POST',
			success:function(data){
				var html = ''; 
				$.each(data,function(i,item){
					var hsbz='';
					if(item['HSBZ']==0){
						hsbz='不含税';
					}else{
						hsbz='含税';
					}
					html += ''
						+'<tr>'
						+'	<td>'+item['SCALE_COUNT']+'</td>'
//						+'	<td>'+item['FPHXZ']+'</td>'
						+'	<td>'+item['SPMC']+'</td>'
//						+'	<td>'+item['SPBM']+'</td>'
//						+'	<td>'+item['ZXBM']+'</td>'
						+'	<td>'+item['DW']+'</td>'
						+'	<td>'+item['DJ']+'</td>'
						+'	<td>'+item['SPSL']+'</td>'
						+'	<td>'+item['JE']+'</td>'
						+'	<td>'+item['SPSM']+'</td>'
						+'	<td>'+item['SL']+'</td>'
						+'	<td>'+item['SE']+'</td>'
						+'	<td>'+hsbz+'</td>'
						+'</tr>';
				});
				
				
				$('#data-tbody-detail').html(html);
				$('#ajax-modal').modal('show');
			}
		});
	};
	var xmlCheck=function(sInputInfo){
		try
		{
			ret = sk.Operate(sInputInfo);
			return ret;
		}
		catch(e)
		{
			alert(e.message + ",errno:" + e.number);
		}
	}
	//1、参数设置
	var setting = function(){
		var ret='';
		var sInputInfo = ''
		$.ajax({
			url:app.rootPath + '/invoiceCall/setting.do',
			type:'POST',
			contentType: "application/json; charset=utf-8",
			success:function(data){
				if(data['result']=='SUCCESS'){
					sInputInfo=data['rows'];
					ret=xmlCheck(sInputInfo);
					settingCheck(ret);
				}else{
					alert("参数错误！")
					return;
				}
			},
			error:function(data){
				alert(data);
			}
		});
	}
	//参数设置校验
	var settingCheck=function(backInfo){
		var ss={
				"xml":backInfo
		};
		$.ajax({
			url:app.rootPath + '/invoiceCall/settingCheck.do',
			type:'POST',
			async:false,
			data:JSON.stringify(ss),
			contentType: "application/json; charset=utf-8",
			success:function(data){
				if(data['result']=='ERROR'){
					alert(data['msg']);
					backmsg=data['msg'];
					app.status=false;
				}else{
					app.status=true;
				}
			},
			error:function(data){
				alert(data);
			}
		});
	}
	//2、查余票
	var checkInvoice = function(){
		var sInputInfo = ''
			$.ajax({
				url:app.rootPath + '/invoiceCall/checkInvoice.do?fplxdm='+app.invoiceType,
				type:'POST',
				contentType: "application/json; charset=utf-8",
				success:function(data){
					if(data['result']=='SUCCESS'){
						sInputInfo=data['rows'];
						ret=xmlCheck(sInputInfo);
						checkInvoiceBack(ret);
					}else{
						alert("参数错误！")
					}
				},
				error:function(data){
					alert(data);
				}
			});
	}
	//查余票返回校验
	var checkInvoiceBack =function(backInfo){
		var ss={
				"xml":backInfo
		};
		$.ajax({
			url:app.rootPath + '/invoiceCall/checkInvoiceBack.do',
			type:'POST',
			async:false,
			data:JSON.stringify(ss),
			contentType: "application/json; charset=utf-8",
			success:function(data){
				if(data['result']=='ERROR'){
					alert(data['msg']);
					backmsg=data['msg'];
					app.checkstatus=false;
				}else{
					app.checkstatus=true;
				}
			},
			error:function(data){
				alert(data);
			}
		});
	}
	//3、作废（失败）发票重新开票
	var redoInvoice = function(fpqqlsh,xml,jylsh){
		ret=xmlCheck(xml);
		saveInvoice(fpqqlsh,ret,jylsh);
	}
	//保存发票信息
	var saveInvoice = function(fpqqlsh,ret,jylsh){
		var ss={
			"fpqqlsh":fpqqlsh,
			"xml":ret,
			"jylsh":jylsh
		}
		var fpdm='';
		var fphm='';
		$.ajax({
			url:app.rootPath + '/tobacco/purchase/saveInvoice.do',
			type:'POST',
			data:JSON.stringify(ss),
			async:false,
			contentType: "application/json; charset=utf-8",
			success:function(data){
				if(data['result'] == 'SUCCESS'){
					result=data['rows'];
					fpdm=result['fpdm'];
					fphm=result['fphm'];
//					alert("发票代码："+fpdm+"发票号码："+fphm+",请返回发票成功状态进行查询、打印...");
					if (confirm("开票成功 ，发票代码："+fpdm+"，发票号码："+fphm+" ，是否打印？")) {
						print(fpdm,fphm);
					}
				}else{
					alert(data['msg']);
				}
			},
			error:function(data){
				alert(data);
			}
		});
		
	}
	
	//4、打印
	var print = function(fpdm,fphm){
			var sInputInfo = '';
			$.ajax({
				url:app.rootPath + '/invoiceCall/print.do?fpdm='+fpdm+"&fphm="+fphm+"&fplxdm="+app.invoiceType,
				type:'POST',
				contentType: "application/json; charset=utf-8",
				success:function(data){
					if(data['result']=='SUCCESS'){
						sInputInfo=data['rows'];
						 var backInfo = xmlCheck(sInputInfo);
						 printCheck(backInfo);
					}else{
						alert("参数错误！")
						return;
					}
				},
				error:function(data){
					alert(data);
				}
			});
		
	}
	//打印检查
	var printCheck = function(backInfo){
		var ss={
				"xml":backInfo
		};
		$.ajax({
			url:app.rootPath + '/invoiceCall/checkPrint.do',
			type:'POST',
			data:JSON.stringify(ss),
			contentType: "application/json; charset=utf-8",
			success:function(data){
				if(data['result']=='ERROR'){
					alert(data['msg']);
				}
			},
			error:function(data){
				alert(data);
			}
		});
	}
	//5、执行作废
	var cancelInvoice = function(fpdm,fphm,hjje){
		var sInputInfo = '';
		$.ajax({
			url:app.rootPath + '/invoiceCall/cancel.do?fpdm='+fpdm+"&fphm="+fphm+"&fplxdm="+app.invoiceType+"&hjje="+hjje,
			type:'POST',
			contentType: "application/json; charset=utf-8",
			success:function(data){
				if(data['result']=='SUCCESS'){
					sInputInfo=data['rows'];
				 var backInfo =  xmlCheck(sInputInfo);
				 cancelCheck(backInfo);
				}else{
					alert("参数错误！")
					return;
				}
			},
			error:function(data){
				alert(data);
			}
		});
	}
	//作废检查
	var cancelCheck = function(backInfo){
		var ss={
				"xml":backInfo
		};
		app.ajax({
			url:app.rootPath + '/invoiceCall/checkCancel.do?xml='+backInfo,
			type:'POST',
			data:JSON.stringify(ss),
			contentType: "application/json; charset=utf-8",
			success:function(data){
				if(data['result']=='ERROR'){
					alert(data['msg']);
				}else{
					alert("作废操作成功");
					loadData();
				}
			},
			error:function(data){
				alert(data);
			}
		});
	}
	//6、打印设置
	var printSet = function(top,left,fplxdm){
		 $.ajax({
				url:app.rootPath + '/invoiceCall/printSet.do?top='+top+'&left='+left+'&fplxdm='+fplxdm,
				type:'POST',
				contentType: "application/json; charset=utf-8",
				success:function(data){
					if(data['result']=='SUCCESS'){
						ret=data['rows'];
						var backInfo=xmlCheck(ret);
						printSetCheck(backInfo);
						$('#defaultPrint').modal('hide');
					}
				},
				error:function(data){
					alert(data);
				}
			});
	}
	//打印设置检查
	var printSetCheck = function(backInfo){
		var ss={
				"xml":backInfo
		};
		$.ajax({
			url:app.rootPath + '/invoiceCall/printSetCheck.do',
			type:'POST',
			data:JSON.stringify(ss),
			contentType: "application/json; charset=utf-8",
			success:function(data){
				alert(data['msg']);
			},
			error:function(data){
				alert(data);
			}
		});
	}
	
	
	
	var buildEvent = function(){
		$('.datePicker').click(function(){
			WdatePicker();
		});
		//点击表格行事件
		$('#data-tbody').on('click','tr', function(){
			app.pid=$(this).attr('data-id');
			loadData_items();
		});
		//点击作废
		$('#data-tbody').on('click','button', function(e){
			e.stopPropagation();
			var btn = $(this);
			if(btn.attr('btn-type') == 'cancel'){
				if (confirm("确定是否作废？")) {
					app.pid=btn.attr('data-id');
					var fpdm=$(this).attr('data-fpdm');
					var fphm=$(this).attr('data-fphm');
					var hjje=$(this).attr('data-hjje');
					setting();
					cancelInvoice(fpdm,fphm,hjje);
				}
			}else if(btn.attr('btn-type') == 'print'){
				app.pid=btn.attr('data-id');
				var fpdm=$(this).attr('data-fpdm');
				var fphm=$(this).attr('data-fphm');
				setting();
				print(fpdm,fphm);
			}else if(btn.attr('btn-type') == 'redoInvoice'){
				var flag=0;
				if(!confirm("确定是否对已作废发票重新开票？")){
					return;
				}
				if(app.status){
					app.pid=btn.attr('data-id');
					var fpqqlsh='';
					var jylsh='';
					var xml='';
					app.ajax({
						url:app.rootPath + '/tobacco/purchase/redoInvoice.do?id='+app.pid,
						type:'POST',
						async:false,
						success:function(data){
							fpqqlsh = data['fpqqlsh'];
							jylsh=data['jylsh'];
							xml = data['xml'];
//							重新开票（作废后）
							checkInvoice();
							flag=1;
						}
					});
					if(flag==1){
						redoInvoice(fpqqlsh,xml,jylsh);
					}
				}else{
					alert("错误信息"+backmsg);
				}
			}else if(btn.attr('btn-type') == 'doInvoice'){
				var fpqqlsh='';
				var jylsh='';
				var xml='';
				var flag=0;
				app.fpid=btn.attr('data-id');
				if(app.status){
				app.ajax({
						url : app.rootPath+ '/tobacco/purchase/doInvoiceByOne.do?id='+ app.fpid,
						type : 'POST',
						async:false,
						success : function(data) {
						if (data != null) {
							fpqqlsh = data['fpqqlsh'];
							jylsh=data['jylsh'];
							xml = data['xml'];
								// 1、查询未开票
								checkInvoice();
								flag=1;
							}
						}
					});
				if(flag==1){
					redoInvoice(fpqqlsh,xml,jylsh);
				}
			}else{
				alert(backmsg);
			}
			loadData();
		}	
		});
		 $('#reSetTable').on('click',function(){
			$('#search_key').val("");
			$('#start_date').val("");
			$('#end_date').val("");
			loadData();
		});
		 $('#printSet').on('click',function(){
				$('#defaultPrint').modal('show');
			});
		 $('#sureDefaultPrint').on('click',function(){
			 var ret;
			 var fplxdm = $('#exampleInputName1').val();
			 var top = $('#exampleInputName2').val();
			 var left = $('#exampleInputName3').val();
			 if(top==''||!top.match(/^[\-\+]?\d+$/)){
				 alert('请输入正确的上边距（正负整数）');
				 return;
			 }
			 if(left==''||!left.match(/^[\-\+]?\d+$/)){
				 alert('请输入正确的左边距（正负整数）');
				 return;
			 }
			 printSet(top,left,fplxdm);
		 });
	};

	var baccomanage = {
			init : function() {
				app.mainContainer.html(tpl);
				buildEvent();
				loadData();
			}
		};
	
	return baccomanage;
});