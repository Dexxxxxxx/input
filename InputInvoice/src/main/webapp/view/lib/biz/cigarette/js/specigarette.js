/**
 * 
 */
define([ 'jquery', 'app', 'ztree', 'jqueryForm','My97DatePicker','text!biz/cigarette/specigarette.html' ], function($, app, ztree, jqueryForm,My97DatePicker, tpl) {
	var showFpType='';//点击单张开票时刷新发票列表 0=批量 1=单条数据
	var page=10;
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/cigarette/specigarette/list.do?type=1',
			target : 'page-div',
			from: 'search-form',
			limit:page,
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr data-id="'+item['INVOICE_NO']+'">'
						+'  <td class="sss"><div class="checker"><span><input type="checkbox"  class="checkboxes" data-ghdwmc="'+item['GHDWMC']+'" value="'+item['INVOICE_NO']+'"></span></div></td>'
						+'	<td name="orig_id" >'+item['INVOICE_NO']+'</td>'
						+'	<td name="com_name">'+item['GHDWMC']+'</td>'
						+'	<td name="gf_nsrsbh">'+item['GHDWNSRSBH']+'</td>'
						+'	<td name="addrTel">'+item['GHDWDZDH']+'</td>'
						+'	<td name="gf_bank_account">'+item['GHDWYHZH']+'</td>'
						+'	<td>'+item['SALE_DATE']+'</td>'
						if($('#invoice_status').val()=='0'){
							$("#btn-FpInfo").attr('disabled',false);
							html +=''
							+'	<td>'
							+'		<div>'
							+'			<button name="edit" class="btn sbold green btn-sm" btn-type="edit" data-id="'+item['GHDWDM']+ '" data-mc="'+item['GHDWMC']+ '" invoice-no="'+item['INVOICE_NO']+ '" >'
							+'				修改<i class="fa fa-edit"></i>'
							+'			</button>'
							+'		</div>'
							+'	</td>'
							+'</tr>';
						}else{
							$("#btn-FpInfo").attr('disabled',true);
							html +=''
							+'	<td>'
							+'		<div>'
							+'			<button name="status" class="btn sbold green btn-sm" btn-type="status" data-id="'+item['INVOICE_NO']+ '" >'
							+'				发票详情<i class="fa fa-edit"></i>'
							+'			</button>'
							+'		</div>'
							+'	</td>'
							+'</tr>';
						}
				});
				return html;
			}
		}).load();
	};
	var loadData_items=function(){
		app.ajax({
			url:app.rootPath + '/cigarette/gencigarette/getById.do?invoice_no='+app.pid,
			type:'POST',
			success:function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr>'
						+'	<td style="width: 15%;">'+item['SPXH']+'</td>'
						+'	<td style="width: 15%;">'+item['SPMC']+'</td>'
						+'	<td style="width: 12.5%;">'+item['GGXH']+'</td>'
//						+'	<td>'+item['DW']+'</td>'
						+'	<td style="width: 10%;">'+item['SPSL']+'</td>'
						+'	<td style="width: 10%;">'+item['DJ']+'</td>'
						+'	<td style="width: 12.5%;">'+item['SL']+'</td>'
						+'	<td style="width: 15%;">'+item['JSHJ']+'</td>'
						+'</tr>';
				});
				$('#data-tbody-detail').html(html);
				$('#ajax-modal').modal('show');
			}
		});
	}
	/**
	 * 基础事件绑定
	 */
	var buildBase = function(){
		$('#data-tbody').on('click','tr', function(){
			app.pid=$(this).attr('data-id');
			loadData_items();
		});
		$('#data-tbody').on('click','.checker',function(e){
			e.stopPropagation();
		});
		$('#page_status').on('change',function(){
			page=$('#page_status').val();
			loadData();
		});
		$('#checkall').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.checkboxes').prop('checked',isChecked);
		});
		$('#reSetTable').on('click',function(){
			$('#search_key').val("");
			$('#start_date').val("");
			$('#end_date').val("");
			loadData();
		});
		$('#doSearch').on('click',function(){
			loadData();
		});
	}
	var buildEvent=function(){
		//点击开票获取待开发票信息
		$('#btn-FpInfo').click(function(){
			showFpType='0';
			var ids='';
			var ghdwmc='';
			var flag = 0;
			var i=0;
			$('.checkboxes').each(function(){
				if($(this).is(":checked")){
					i++;
					ids+=$(this).val();
					ids+=',';
					ghdwmc=$(this).attr('data-ghdwmc');
					if(ghdwmc==''){
						flag=1;
						return false;
					}
				}
			  });
			if(flag==1){
				alert("已勾选第"+i+"行，购货单位名称为空，请完善购方信息");
				return;
			}
			app.ids=ids.substring(0,ids.lastIndexOf(","));
			if(ids==''){
				alert('至少勾选一条记录');
				return;
			}else{
				$('.fiex').show();
				$.ajax({
					url:app.rootPath + '/cigarette/specigarette/doInvoice.do',
					type:'post',
					dataType:'json',
					data:{'ids':app.ids},
					success:function(data){
						if(data['result']=='SUCCESS'){
							var order_no=data['rows'];
							showFpInfo('',order_no);
							loadData();
						}else{
							alert(data['msg']);
							if(data['rows']!=null){
								showFpInfo('',data['rows']);
								loadData();
							}else{
								$('#ajax-FpInfo').modal('hide');
								loadData();
							}
						}
					}
				});
				$('.fiex').hide();
			}
			
		});
//行事件
		$('#data-tbody-FpInfo').on('click', 'button', function(){
			var btn =$(this).attr('btn-type');
			var invoice_id = $(this).attr('data-id');
			if(btn=='doInvoiceOne'){
				doInvoiceByOne(invoice_id)
			}else if(btn=='print'){
				print(invoice_id);
			}else if(btn=='printlist'){
				printlist(invoice_id);
			}
			
		});
		//单张开具主方法
		function doInvoiceByOne(invoice_id){
			$('.fiex').show();
			app.ajax({
				url:app.rootPath + '/cigarette/specigarette/doInvoiceByOne.do',
				type:'post',
				dataType:'json',
				data:{'id':invoice_id},
				success:function(data){
					var order_no = data['order_no'];//开票批次号
					var invoice_no=data['invoice_no'];//发票源编号
					var status = data['status'];//发票源数据状态
					if(status=='1'){
						loadData();
					}
					if(showFpType=='0'){
						showFpInfo('',order_no);
					}else if(showFpType=='1'){
						showFpInfo(invoice_no,'');
					}
				}
			});
			$('.fiex').hide();
		}
//修改客户信息
		$('#data-tbody').on('click','button[btn-type="edit"]',function(e){
			e.stopPropagation();
			var ghdwdm=$(this).attr('data-id');
			var invoice_no = $(this).attr('invoice-no');
			var ghdwmc = $(this).attr('data-mc');
			app.ajax({
				url:app.rootPath + '/cigarette/customer/getById.do',
				type:'post',
				dataType:'json',
				data:{'ghdwdm':ghdwdm},
				success:function(data){
					$('#invoice_no').val(invoice_no);
					$('#ghdwdm').val(ghdwdm);
					$('#ghdwmc').val(ghdwmc);
					if(data!=null){
						$('#ghdwnsrsbh').val(data['GHDWNSRSBH']);
						$('#ghdwdzdh').val(data['GHDWDZDH']);
						$('#ghdwyhzh').val(data['GHDWYHZH']);
					}
				}
			});
			$("#modal-customerinfo").modal('show');
		});
//查看开具发票信息
		$('#data-tbody').on('click','button[name="status"]',function(e){
			e.stopPropagation();
			showFpType='1';
			$('#btn-doinvoice').attr("disabled","disabled");
			$('#btn-doEInvoice').attr("disabled","disabled");
			var invoice_no =$(this).attr('data-id');
			showFpInfo(invoice_no);
			
		});
//保存修改客户信息
		$('#customerinfo_form').attr('action','cigarette/customer/update.do');
		$('#customerinfo_form').on('submit', function(e) {
			e.preventDefault();
			var com_addr = $("#com_addr").val();
			if(com_addr == ''){
				alert("带*号的项必须填写！");
			}else{
				$(this).ajaxSubmit({
	                success: function(data){
	                	if(data.result=="SUCCESS"){
	                		alert(data.msg);
	                		loadData();
	                		$("#modal-customerinfo").modal('hide');
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
			}
		});
	};
		//7、模态显示(发票开具情况)
		var showFpInfo = function(invoice_no,order_no){
			var ss={
					"invoice_no":invoice_no,
					"order_no":order_no
				}
			app.ajax({
				url:app.rootPath + '/cigarette/specigarette/getInvoiceList.do',
				type:'POST',
				data:JSON.stringify(ss),
				async:false,
				contentType: "application/json; charset=utf-8",
				success:function(data){
					var html = ''; 
					$.each(data,function(i,item){
						var invoice_type="";
						var buttn='';
						if(item['TYPE']=='004'){
							invoice_type='专票';
							if(item['STATUS']=='0'){
								buttn += ''
									+'	<td>'
									+'		<div>'
									+'			<button class="btn sbold green btn-xs" btn-type="doInvoiceOne" invoice-type="'+item['TYPE']+'" data-id="'+item['ID']+'">'
									+'				开  票   <i class="fa fa-edit"></i>'
									+'			</button>'
									+'		</div>'
									+'	</td>';
							}else if(item['STATUS']=='1'){
								buttn += ''
									+'<td>已开具</td>';
							}else if(item['STATUS']=='-1'){
								buttn += ''
									+'<td>已作废</td>';
							}else if(item['STATUS']=='-2'){
								buttn += ''
									+'<td>已冲红</td>';
							}
						};
						html += ''
							+'<tr>'
							+'	<td>'+item['INVOICE_NO']+'</td>'
							+'	<td>'+invoice_type+'</td>'
							+'	<td>'+item['FPDM']+'</td>'
							+'	<td>'+item['FPHM']+'</td>'
							+'	<td>'+item['HJJE']+'</td>'
							+'	<td>'+item['HJSE']+'</td>'
							+'	<td>'+item['JSHJ']+'</td>'
							+buttn
							+'</tr>';
					});
					if(html!=''){
						$('#data-tbody-FpInfo').html(html);
						$('#btn-doinvoice').attr("disabled","disabled");
						$('#ajax-FpInfo').modal('show');
					}
				}
			});
		}
	//打印校验
	var specigarette = {
			init : function() {
				app.mainContainer.html(tpl);
				buildBase();
				buildEvent();
				loadData();
			}
		};
	
	return specigarette;
});