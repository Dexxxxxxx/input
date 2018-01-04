/**
 * 
 */
define([ 'jquery', 'app', 'ztree', 'jqueryForm','My97DatePicker', 'text!biz/tobacco/baccopurchase.html' ], function($, app, ztree, jqueryForm,My97DatePicker, tpl) {
	var backmsg='';
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/tobacco/orig/list.do',
			target : 'page-div',
			from: 'search-form',
			limit:20,
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				if($('#invoice_status').val()=='yes'){
					$('#btn-doinvoices').attr("disabled", true);
				}else{
					$('#btn-doinvoices').attr("disabled", false);
				}
				$.each(data,function(i,item){
					var status=$('#invoice_status').val();
					html += ''
						+'<tr data-id="'+item['INVOICE_NO']+'">'
						+'  <td><div class="checker"><input type="checkbox" class="checkboxes" id_card="'+item['ID_CARD']+'" value="'+item['INVOICE_NO']+'"></div></td>'
						+'	<td>'+item['INVOICE_NO']+'</td>'
						+'	<td>'+item['ORG_CD']+'</td>'
						+'	<td>'+item['SCALE_COUNT']+'</td>'
						+'	<td>'+item['FARMER_CD']+'</td>'
						+'	<td>'+item['FARMER_NAME']+'</td>'
						+'	<td>'+item['ID_CARD']+'</td>'
						+'	<td>'+item['ADD_NAME']+'</td>'
						+'	<td>'+item['SUM_WEIGHT']+'</td>'
						+'	<td>'+item['SUM_AMOUNT']+'</td>'
//						+'	<td>'+item['STATUS']+'</td>'//是否已开票
						+'	<td>'+item['RECORD_TIME']+'</td>';
//						+'	<td>'+item['INVOICE_TIME']+'</td>'//开票时间
						if(status=="yes"){
							html +=	'<td align="center">'
								+'		<div>'
								+'				<button class="btn sbold green btn-xs" btn-type="check" data-id="'+item['JYLSH']+'">'
								+'				该套票详情'
								+'				</button>'
								+'				<button class="btn sbold green btn-xs" btn-type="rollback" data-id="'+item['JYLSH']+'">'
								+'				回滚'
								+'				</button>'
								+'		</div>'
								+'	</td>'
								+'</tr>';
						}else{
							html +=	'<td align="center">'
								+'		<div>'
								+'				<button class="btn sbold green btn-sm"  btn-type="doinvoice1" id_card="'+item['ID_CARD']+'" data-id="'+item['INVOICE_NO']+'">'
								+'				单条开票'
								+'				</button>'
								+'		</div>'
								+'	</td>'
								+'</tr>';
						}
				});
				return html;
			}
		}).load();
		//全局参数设置
		setting();
	};
	var loadData_items=function(){
		app.ajax({
			url:app.rootPath + '/tobacco/orig/listDetail.do?invoice_no='+app.pid,
			type:'POST',
			success:function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr>'
						+'	<td>'+item['FARMER_NAME']+'</td>'
						+'	<td>'+item['LEVEL_NO']+'</td>'
						+'	<td>'+item['LEVEL_NAME']+'</td>'
						+'	<td>'+item['PRICE']+'</td>'
						+'	<td>'+item['WEIGHT']+'</td>'
						+'	<td>'+item['AMOUNT']+'</td>'
						+'	<td>'+item['SUB_PRICE1']+'</td>'
						+'	<td>'+item['SUB_AMOUNT1']+'</td>'
						+'	<td>'+item['SUB_PRICE2']+'</td>'
						+'	<td>'+item['SUB_AMOUNT2']+'</td>'
						+'	<td>'+item['SUB_PRICE3']+'</td>'//是否已开票
						+'	<td>'+item['SUB_AMOUNT3']+'</td>'
						+'	<td>'+item['SUB_PRICE4']+'</td>'//开票时间
						+'	<td>'+item['SUB_AMOUNT4']+'</td>'
						+'	<td>'+item['DEDUCT_AMOUNT']+'</td>'
						+'</tr>';
				});
				
				
				$('#data-tbody-detail').html(html);
				$('#ajax-modal').modal('show');
			}
		});
	};
	//执行xml
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
	var setting =function(){
		var ret='';
		var sInputInfo = ''
			$.ajax({
				url:app.rootPath + '/invoiceCall/setting.do',
				type:'POST',
				async:false,
				contentType: "application/json; charset=utf-8",
				success:function(data){
					if(data['result']=='SUCCESS'){
						sInputInfo=data['rows'];
//						sInputInfo = new Base64().decode(sInputInfo);
						ret=xmlCheck(sInputInfo);
						settingCheck(ret);
					}else{
						alert("参数错误！")
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
		var ret='';
		var sInputInfo = ''
			$.ajax({
				url:app.rootPath + '/invoiceCall/checkInvoice.do?fplxdm=007',
				type:'POST',
				async:false,
				contentType: "application/json; charset=utf-8",
				success:function(data){
					if(data['result']=='SUCCESS'){
						sInputInfo=data['rows'];
//						sInputInfo = new Base64().decode(sInputInfo);
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
	//3、执行开票(单张、多张)
	var doInvoice = function(fpqqlsh,xml,jylsh){
		ret=xmlCheck(xml);
		saveInvoice(fpqqlsh,ret,jylsh);
	}
	//4、保存发票信息
	var saveInvoice = function(fpqqlsh,ret,jylsh){
		var ss={
				"fpqqlsh":fpqqlsh,
				"xml":ret,
				"jylsh":jylsh
			}
			$.ajax({
				url:app.rootPath + '/tobacco/purchase/saveInvoice.do',
				type:'POST',
				data:JSON.stringify(ss),
				async:false,
				contentType: "application/json; charset=utf-8",
				success:function(data){
					if(data['result'] == 'SUCCESS'){
						loadData();
					}else{
						alert(data['msg']);
					}
				},
				error:function(data){
					alert(data);
				}
			});
			
			showFpInfo(jylsh);
			
	}
	//6、打印
	var print = function(fpdm,fphm){
		var sInputInfo = '';
		var backInfo = '';
		$.ajax({
			url:app.rootPath + '/invoiceCall/print.do?fpdm='+fpdm+"&fphm="+fphm+"&fplxdm=007",
			type:'POST',
			contentType: "application/json; charset=utf-8",
			success:function(data){
				if(data['result']=='SUCCESS'){
					sInputInfo=data['rows'];
//					sInputInfo = new Base64().decode(sInputInfo);
					backInfo=xmlCheck(sInputInfo);
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
	//打印校验
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
	//7、模态显示(发票开具情况)
	var showFpInfo = function(jylsh){
		app.ajax({
			url:app.rootPath + '/tobacco/invoice/listInvoice.do?jylsh='+jylsh,
			type:'POST',
			success:function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr data-id="'+item['ID']+'">'
						+'	<td>'+item['FPQQLSH']+'</td>'
						+'	<td>'+item['FPDM']+'</td>'
						+'	<td>'+item['FPHM']+'</td>'
						+'	<td>'+item['HJJE']+'</td>'
						+'	<td>'+item['HJSE']+'</td>'
						+'	<td>'+item['JSHJ']+'</td>';
					if(item['TYPE']=='1'){
						if(item['STATUS']=='1'){
							html +=''
								+'<td>'+"成功"+'</td>'
								+'<td>'
								+'		<div>'
								+'				<button class="btn sbold green btn-sm" btn-type="print" data-fphm="'+item['FPHM']+'" data-fpdm="'+item['FPDM']+'"  data-id="'+item['ID']+'">'
								+'				打印 <i class="fa fa-print"></i>'
								+'				</button>'
								+'		</div>'
								+'	</td>'
								+'</tr>';
						}else if(item['STATUS']=='-1'){
							html +=''
								+'<td>'+"成功"+'</td>'
								+'<td>'
								+'		<div>'
								+'已作废'	
								+'		</div>'
								+'	</td>'
								+'</tr>';
						}else{
							html +=	''
								+'<td>'+"失败"+'</td>'
								+'<td>'
								+'		<div>'
								+'				<button class="btn sbold green btn-sm" btn-type="edit" data-id="'+item['ID']+'">'
								+'				开票 <i class="fa fa-edit"></i>'
								+'				</button>'
								+'		</div>'
								+'	</td>'
								+'</tr>';
						}
					}else{
						html +=	''
							+'<td>'+"失败"+'</td>'
							+'<td>'
							+'		<div>'
							+'				<button class="btn sbold green btn-sm" btn-type="edit" data-id="'+item['ID']+'">'
							+'				开票 <i class="fa fa-edit"></i>'
							+'				</button>'
							+'		</div>'
							+'	</td>'
							+'</tr>';
					}
				});
				$('#data-tbody-FpInfo').html(html);
				$('#ajax-FpInfo').modal('show');
//				$('.fiex').hide();
				
			}
		});
		
	}
	
	var buildEvent = function(){
		//全选（不选）操作
		$('#check_group').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.checkboxes').prop('checked',isChecked);
		});
		//阻止冒泡
		$('#data-tbody').on('click','.checker',function(e){
			e.stopPropagation();
		});
		//点击表格行事件
		$('#data-tbody').on('click','tr', function(){
			app.pid=$(this).attr('data-id');
			loadData_items();
		});
		$('#reSetTable').on('click',function(){
			$('#search_key').val("");
			$('#start_date').val("");
			$('#end_date').val("");
			loadData();
		});
		//点击开票
		$('#data-tbody').on('click','button', function(e){
			e.stopPropagation();
			var btn = $(this);
			if(btn.attr('btn-type') == 'check'){
				var jylsh=btn.attr('data-id');
				showFpInfo(jylsh);
			}else if(btn.attr('btn-type') == 'rollback'){
				if(!confirm("确定是否对该套发票原信息回滚？")){
					return;
				}
				var jylsh=btn.attr('data-id');
				$.ajax({
					url:app.rootPath + '/tobacco/purchase/rollBack.do?jylsh='+jylsh,
					type:'POST',
					success:function(data){
						if(data['result']=='SUCCESS'){
							alert("回滚操作成功");
							loadData();
						}else if(data['result']=='ERROR'){
							alert(data['msg']);
						}
					}
				});
			}else if(btn.attr('btn-type') == 'doinvoice1'){
				$('.fiex').show();
				var invoice_no=btn.attr('data-id');
				var id_card=btn.attr('id_card');
				setTimeout(function(){
							checkInvoice();
							if(app.status&&app.checkstatus){
								$.ajax({
									url:app.rootPath + '/tobacco/purchase/doInvoice1.do?invoice_no='+invoice_no+'&id_card='+id_card,
									type:'POST',
									async:false,
									success:function(data){
										if(data['result']=='SUCCESS'){
											//循环调用开票信息
											$.each(data['rows'],function(i,item){
												var fpqqlsh=item['fpqqlsh'];
												var jylsh=item['jylsh'];
												var xml=item['xml'];
												doInvoice(fpqqlsh,xml,jylsh);
											});
											$('.fiex').hide();
										}else{
											alert(data['msg']);
											$('.fiex').hide();
										}
									}
								});
								loadData();
							}else{
								alert(backmsg);
								$('.fiex').hide();
							}
				},0);
			}
		});
		$('#btn-doinvoices').on('click', function(){
			var start_date=$('#start_date').val();
			var end_date=$('#end_date').val();
			if(start_date==''||end_date==''){
				if(!confirm("没指定有效的时间区间，确认是否执行合并开票？")){
					return;
				}
			}else{
				if(!confirm("所选的起始日期："+start_date+" 截止日期："+end_date+" 确认执行合并开票？")){
				return;
				}
			}
			$('.fiex').show();
			setTimeout(function(){
				var ids='';
				var id_card='';
				var type=true;
				$('.checkboxes').each(function(){
					if($(this).is(":checked")==true){
						if(id_card==''){
							id_card=$(this).attr('id_card');
							ids+=$(this).val();
							ids+=',';
						}else if($(this).attr('id_card')!=id_card){
								alert('合并开票时必须为同一烟农');
								type=false;
								$('.fiex').hide();
								return type;
						}else{
								ids+=$(this).val();
								ids+=',';
							}
						}
				  });
				ids=ids.substring(0,ids.lastIndexOf(","));
				if(ids==''){
					alert('至少勾选一条记录');
					$('.fiex').hide();
					return;
				}else if(type==false){
					$('.fiex').hide();
					return;
				}else{
						checkInvoice();
						if(app.status&&app.checkstatus){
							$.ajax({
								url:app.rootPath + '/tobacco/purchase/doInvoice.do?ids='+ids+'&id_card='+id_card,
								type:'POST',
								async:false,
								success:function(data){
									if(data['result']=='SUCCESS'){
										//循环调用开票信息
										$.each(data['rows'],function(i,item){
											var fpqqlsh=item['fpqqlsh'];
											var jylsh=item['jylsh'];
											var xml=item['xml'];
											doInvoice(fpqqlsh,xml,jylsh);
										});
										$('.fiex').hide();
									}else{
										alert(data['msg']);
										$('.fiex').hide();
									}
								}
							});
							loadData();
						}else{
							alert(backmsg);
							$('.fiex').hide();
						}
						
					}
			},0);
			
		});
		$('#data-tbody-FpInfo').on('click','button', function(e){
			e.stopPropagation();
			var btn = $(this);
			//单条数据开票
			if(btn.attr('btn-type') == 'edit'){
				app.fpid=btn.attr('data-id');
				app.ajax({
						url : app.rootPath+ '/tobacco/purchase/doInvoiceByOne.do?id='+ app.fpid,
						type : 'POST',
						success : function(data) {
						if (data != null) {
							var fpqqlsh = data['fpqqlsh'];
							var jylsh=data['jylsh'];
							var xml = data['xml'];
								// 1、查询未开票
								checkInvoice();
								if(app.status&&app.checkstatus){
									//2、开票
									doInvoice(fpqqlsh,xml,jylsh);
								}else{
									alert(backmsg);
								}
							}
						}
					});
				}
				//单条数据打印
				else if (btn.attr('btn-type') == 'print') {
				app.fpid = btn.attr('data-id');
				var fpdm = $(this).attr('data-fpdm');
				var fphm=$(this).attr('data-fphm');
				print(fpdm,fphm);
			}
			
		});
		
	};

	var baccopurchase = {
			init : function() {
				app.mainContainer.html(tpl);
				buildEvent();
				loadData();
			}
		};
	return baccopurchase;
});