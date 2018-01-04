/**
 * 
 */
define([ 'jquery', 'app', 'ztree', 'jqueryForm', 'My97DatePicker','text!biz/cigarette/cigarinvoice.html' ],function($, app, ztree, jqueryForm, My97DatePicker, tpl) {
	var page=10;
	var _timeInterval;
	var setlen=5;
	var setTime=2000;		
	var loadData = function() {
				new app.Page(
						{
							url : app.rootPath + '/cigarette/invoice/list.do',
							target : 'page-div',
							from : 'search-form',
							dataTarget : 'data-tbody',
							buildData : function(data) {
								var html = '';
								app.invoiceType=$('#type').val();
								if(app.invoiceType=='026'){
									$('#setDefaultPrint').css('display','none');//设置打印边距
									$('#print').css('display','none');//打印
									$('#printlist').css('display','none');//打印清单
									$('#cancel').css('display','none');//作废
									$('#checkInvoice').css('display','');//查看电票
								}else{
									$('#setDefaultPrint').css('display','');
									$('#print').css('display','');
									$('#printlist').css('display','');
									$('#cancel').css('display','');
									$('#checkInvoice').css('display','none');
								}
								app.kplx=$('#kplx').val();
								if(app.kplx=='0'){
									//蓝票
									$('#rollback').css('display','none');
									$('#redInvoice').css('display','');
								}else{
									//红票
									$('#rollback').css('display','');
									$('#redInvoice').css('display','none');
								}
								
								$.each(data,function(i, item) {
									var fplx=item['TYPE']=='004'?'专票':(item['TYPE']=='007'?'普票':'电子发票');
									var qdbz=item['QDBZ']=='1'?'有':'无';
									var status=item['STATUS']=='1'?'正常':'已作废';
									var dystatus = item['DYSTATUS']=='1'?'已打印':'未打印';
									html += ''
										+'<tr data-id="'+item['ID']+'">'
										+'  <td><div class="checker"><span><input type="checkbox"  class="checkboxes" invoice-type="'+item['TYPE']+'" value="'+item['ID']+'" invoice-fpdm="'+item['FPDM']+'" invoice-fphm="'+item['FPHM']+'" data-url="'+item['URL']+'" ></span></div></td>'
										+'	<td>'+item['INVOICE_NO']+'</td>'
										+'	<td>'+fplx+'</td>'
										+'	<td>'+item['FPDM']+'</td>'
										+'	<td>'+item['FPHM']+'</td>'
										+'	<td>'+item['GHDWMC']+'</td>'
										+'	<td>'+item['XHDWMC']+'</td>'
										+'	<td>'+item['JSHJ']+'</td>'
										+'	<td>'+item['KPR']+'</td>'
										+'	<td>'+item['KPRQ']+'</td>'
										+'	<td>'+qdbz+'</td>'
										+'	<td>'+dystatus+'</td>'
										+'	<td>'+status+'</td>';
								});
								return html;
							}
						}).load();
				};
			var loadData_items = function() {
				app.ajax({
					url : app.rootPath+ '/cigarette/invoice/listInvoiceDetail.do?invoice_id='+ app.pid,
					type : 'POST',
					success : function(data) {
						var html = '';
						$.each(data, function(i, item) {
							html += '' 
								+ '<tr data-id="'+item['ID']+'">' 
								+ '<td>'+item['XH']+'</td>' 
								+ '<td>'+item['SPMC']+'</td>'
								+ '<td>'+item['DW']+'</td>'
								+ '<td>'+item['DJ']+'</td>'
								+ '<td>'+item['SPSL']+'</td>' 
								+ '<td>'+item['JE']+'</td>'
								+ '<td>'+item['SL']+'</td>'
								+ '<td>'+item['SE']+'</td>'
								+ '<td>'+item['HSBZ']+'</td>'
								+ '</tr>';
						});
						$('#data-tbody-detail').html(html);
						$('#ajax-modal').modal('show');
					}
				});
			};
			/**
			 * 基础事件绑定
			 */
			var buildBase = function(){
				$('#data-tbody').on('click','.checker',function(e){
					e.stopPropagation();
				});
				//发票明细模态
				$('#data-tbody').on('click', 'tr', function() {
					app.pid = $(this).attr('data-id');
					loadData_items();
				});
				$('#checkall').on('click',function(){
					var isChecked = $(this).prop('checked');
					$('.checkboxes').prop('checked',isChecked);
				});
				//显示全部
				$('#reSetTable').on('click', function() {
					$('#search_key').val("");
					$('#start_date').val("");
					$('#end_date').val("");
					loadData();
				});
				//设置打印边距
				$('#setDefaultPrint').on('click',function(){
					$('#defaultPrint').modal('show');
				});
				//批量打印
				$('#allPrint').on('click',function(){
					$('#allPrint-modal').modal('show');
				});
				
			}
			/* 事件 */
			var buildEvent = function() {
				//行事件
				$('#data-tbody').on('click','a', function(e){
					e.stopPropagation();
				});
				$('#data-tbody').on('click','button', function(e){
					e.stopPropagation();
					var btn = $(this);
					var invoice_id = btn.attr("data-id");
					//单条数据开票
					if(btn.attr('btn-type') == 'doInvoice'){
						
					}
				});

				$('#print').on('click', function() {
					var len=0;
					var flag=0;
					var fpdm='';
					var checkbox=$('.checkboxes');
					var ids=[];
					$('.checkboxes').each(function(){
						if($(this).is(":checked")==true){
							if(len==0){
								fpdm=$(this).attr('invoice-fpdm')
							}else{
								if(fpdm!=$(this).attr('invoice-fpdm')){
									flag=1;
									return;
								}
							}
							ids.push($(this).attr('invoice-fphm'));
						len++;
						}
					});
					var start_fphm=ids[0];
					var end_fphm=ids[len-1];
					var _st =parseInt(start_fphm, 10);
					var _end =parseInt(end_fphm, 10);
					if(flag==1){
						alert("发票代码不一致，不能批量打印");
					}
					if(_st+len-1!=_end){
						alert("发票号码不连续，不能批量打印");
					}else{
						var zs=_end-_st+1;
						if(confirm("发票代码："+fpdm+" 发票号码："+start_fphm+"至"+end_fphm+" 共"+zs+'张')){
							var ss=0;
							$('#ajax-modalbar').modal('show');
							$('#bar_progress').css('width','0%');
							$('#bar_number').html('0%');
							_timeInterval=setInterval(function(){
								var show_number=(ss/len).toFixed(2)*100;
								if(ss>=len){
									$('#ajax-modalbar').modal('hide');
									loadData();
									clearInterval(_timeInterval);
								}else if(ss+setlen>=len){
									$('#bar_progress').css('width',show_number+'%');
									$('#bar_number').html(show_number+'%');
									var fphms=ids.slice(ss,len);
									print(app.invoiceType,fpdm,fphms);
								}else{
									$('#bar_progress').css('width',show_number+'%');
									$('#bar_number').html(show_number+'%');
									var fphms=ids.slice(ss,ss+setlen);
									print(app.invoiceType,fpdm,fphms);
								}
								ss=ss+setlen;
							},setTime);
						}
					}
				});
				$('#printlist').on('click', function() {
					var len=0;
					var flag=0;
					var fpdm='';
					var checkbox=$('.checkboxes');
					var ids=[];
					$('.checkboxes').each(function(){
						if($(this).is(":checked")==true){
							if(len==0){
								fpdm=$(this).attr('invoice-fpdm')
							}else{
								if(fpdm!=$(this).attr('invoice-fpdm')){
									flag=1;
									return;
								}
							}
							ids.push($(this).attr('invoice-fphm'));
						len++;
						}
					  });
					if(flag==1){
						alert("发票代码不一致，不能批量打印");
					}else{
						var ss=0;
						$('#ajax-modalbar').modal('show');
						$('#bar_progress').css('width','0%');
						$('#bar_number').html('0%');
						_timeInterval=setInterval(function(){
							var show_number=(ss/len).toFixed(2)*100;
							if(ss>=len){
								$('#bar_progress').css('width','100%');
								$('#bar_number').html('100%');
								$('#ajax-modalbar').modal('hide');
								loadData();
								clearInterval(_timeInterval);
							}else if(ss+setlen>=len){
								$('#bar_progress').css('width',show_number+'%');
								$('#bar_number').html(show_number+'%');
								var fphms=ids.slice(ss,len);
								printList(app.invoiceType,fpdm,fphms);
							}else{
								$('#bar_progress').css('width',show_number+'%');
								$('#bar_number').html(show_number+'%');
								var fphms=ids.slice(ss,ss+setlen);
								printList(app.invoiceType,fpdm,fphms);
							}
							ss=ss+setlen;
						},setTime);
					}
				});
				$('#cancel').on('click', function() {
					var len=0;
					var checkbox=$('.checkboxes');
					var invoice_id='';
					var ids='';
					for(var i=0;i<checkbox.length;i++){
						if(checkbox[i].checked==true){
							len++;
							invoice_id=checkbox[i].value;
							ids+=checkbox[i].value;
							ids+=',';
						}
					}
					ids=ids.substring(0,ids.lastIndexOf(","));
					if(len <1){
						alert("选择一条信息");
					} else {
						if(confirm("你确定要作废？"))
					    {
							cancel(ids);
					    }
					}
				});
				$('#checkInvoice').on('click',function(){
					var len=0;
					var checkbox=$('.checkboxes');
					var url='';
					var ids='';
					var fpdm='';
					$('.checkboxes').each(function(){
						if($(this).is(":checked")==true){
							len++;
							url=$(this).attr('data-url');
						}
					  });
					ids=ids.substring(0,ids.lastIndexOf(","));
					if(len!=1){
						alert("选择一条信息");
					} else {
						if(url!=''){
							alert(url);
							window.open(url,'_blank');
						}
					}
				
				})
				$('#redInvoice').on('click', function() {
					var len=0;
					var checkbox=$('.checkboxes');
					var invoice_id='';
					var invoice_type='';
					$('.checkboxes').each(function(){
						if($(this).is(":checked")==true){
							len++;
							invoice_id=$(this).val();
							invoice_type=$(this).attr('invoice-type');
						}
					})
					if(len != 1){
						alert("选择一条信息");
					} else {
							if(invoice_type=='026'){
								if(confirm("你确定要冲红这张发票？")){
									app.ajax({
										url:app.rootPath + '/cigarette/invoice/e_redCancel.do?id='+invoice_id,
										type:'POST',
										success:function(data){
											alert("电票冲红成功");
										}
									});
								}
							}else{
								$('#ajax-modal-red').modal('show');
								$('#invoice_id').val(invoice_id);
							}
					}
				});
				
				//按钮事件
				$('#reSetTable').on('click', function() {
					$('#search_key').val("");
					$('#start_date').val("");
					$('#end_date').val("");
					loadData();
				});
				$('#setDefaultPrint').on('click',function(){
					$('#defaultPrint').modal('show');
				});
				$('#sureDefaultPrint').on('click',function(){
					var fplxdm=$('#exampleInputName1').val();
					var top=$('#exampleInputName2').val();
					var left=$('#exampleInputName3').val();
					printSet(fplxdm,top,left);
				});
				$('#savered').on('click',function(){
					var fpdm = $('#fpdm').val();
					var fphm = $('#fphm').val();
					var invoice_id = $('#invoice_id').val();
					if(fpdm==''){
						alert('发票代码不能为空');
						return;
					}
					if(fphm==''){
						alert('发票号码不能为空');
						return;
					}
					var ss={
							"fpdm": fpdm,
							"fphm":fphm,
							"id":invoice_id
					};
					$.ajax({
						url:app.rootPath + '/cigarette/invoice/redCancel.do',
						type:'POST',
						data:JSON.stringify(ss),
						contentType: "application/json; charset=utf-8",
						success:function(data){
							if(data['result']=='SUCCESS'){
								alert('红字发票同步成功');
							}else{
								alert(data['msg']);
							}
						},
						error:function(data){
							
						}
					});
				})
			};
			
//xml执行 及公共执行方法
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
//打印设置
			var printSet = function(fplxdm,top,left){
				//判断控件是否加载
				if(app.skStatus=='0'){
					alert(app.skMsg);
					return;
				}
				var backInfo = '';
				var sInputInfo = '<?xml version="1.0" encoding="gbk"?>'
					+'<business id="20003" comment="页边距设置">'
					+'<body yylxdm="1">'
					+'<fplxdm>'+fplxdm+'</fplxdm>'
					+'<top>'+top+'</top>'
					+'<left>'+left+'</left>'
					+'</body>'
					+'</business>';
				alert(sInputInfo);
				backInfo=xmlCheck(sInputInfo);
				alert(backInfo);
				var _backInfo=parseXML(backInfo);
				var ss=_backInfo.getElementsByTagName("returncode")[0].childNodes[0].nodeValue;
				if(ss==0){
					alert(_backInfo.getElementsByTagName("returnmsg")[0].childNodes[0].nodeValue);
					$('#defaultPrint').modal('hide');
				}else{
					alert(_backInfo.getElementsByTagName("returnmsg")[0].childNodes[0].nodeValue);
					$('#defaultPrint').modal('show');
				}
			}
//全局 打印返回校验
			var printCheck = function(fpdm,fphm){
				var ss={
						"fpdm": fpdm,
						"fphm":fphm
				};
				$.ajax({
					url:app.rootPath + '/cigarette/common/checkPrint.do',
					type:'POST',
					data:JSON.stringify(ss),
					contentType: "application/json; charset=utf-8",
					success:function(data){
						loadData();
					},
					error:function(data){
					}
				});
			}
//批量打印发票
			var print = function(fplxdm,fpdm,fphms){
				//判断控件是否加载
				if(app.skStatus=='0'){
					alert(app.skMsg);
					return;
				}
				for(var i=0;i<fphms.length;i++){
					var backInfo = '';
					var sInputInfo = '<?xml version="1.0" encoding="gbk"?>'
						+'<business id="20004" comment="发票打印">'
						+'<body yylxdm="1">'
						+'<kpzdbs>'+app.kpzdbs+'</kpzdbs>'
						+'<fplxdm>'+fplxdm+'</fplxdm>'
						+'<fpdm>'+fpdm+'</fpdm>'
						+'<fphm>'+fphms[i]+'</fphm>'
						+'<dylx>0</dylx>'
						+'<dyfs>1</dyfs>'
						+'</body>'
						+'</business>';
					alert(sInputInfo);
					backInfo=xmlCheck(sInputInfo);
					var _backInfo=parseXML(backInfo);
					var ss=_backInfo.getElementsByTagName("returncode")[0].childNodes[0].nodeValue;
					if(ss!=0){
						alert(_backInfo.getElementsByTagName("returnmsg")[0].childNodes[0].nodeValue);
						$('#ajax-modalbar').modal('hide');
						clearInterval(_timeInterval);
						return false;
					}else{
						printCheck(fpdm,fphms[i]);
					}
				}
			}
//批量打印清单
			var printList = function(fplxdm,fpdm,fphms){
				//判断控件是否加载
				if(app.skStatus=='0'){
					alert(app.skMsg);
					return;
				}
				for(var i=0;i<fphms.length;i++){
					var backInfo = '';
					var sInputInfo = '<?xml version="1.0" encoding="gbk"?>'
						+'<business id="20004" comment="发票打印">'
						+'<body yylxdm="1">'
						+'<kpzdbs>'+app.kpzdbs+'</kpzdbs>'
						+'<fplxdm>'+fplxdm+'</fplxdm>'
						+'<fpdm>'+fpdm+'</fpdm>'
						+'<fphm>'+fphms[i]+'</fphm>'
						+'<dylx>1</dylx>'
						+'<dyfs>1</dyfs>'
						+'</body>'
						+'</business>';
					backInfo=xmlCheck(sInputInfo);
					var _backInfo=parseXML(backInfo);
					var ss=_backInfo.getElementsByTagName("returncode")[0].childNodes[0].nodeValue;
					if(ss!=0){
						alert(_backInfo.getElementsByTagName("returnmsg")[0].childNodes[0].nodeValue);
						$('#ajax-modalbar').modal('hide');
						clearInterval(_timeInterval);
						return false;
					}
				}
			}
//批量作废（后台）
			var cancel = function(ids){
				$.ajax({
					url:app.rootPath + '/cigarette/invoice/cancel.do?ids='+ids,
					type:'POST',
					success:function(data){
						if(data['result']=='SUCCESS'){
							alert(data['msg']);
							loadData();
						}else{
							alert(data['msg']);
							loadData();
						}
					},
					error:function(data){
						alert(data);
					}
				});
			}
//批量冲红
			var redcancel = function(ids){
				$.ajax({
					url:app.rootPath + '/cigarette/invoice/redcancel.do?ids='+ids,
					type:'POST',
					success:function(data){
						if(data['result']=='SUCCESS'){
							alert(data['msg']);
							loadData();
						}else{
							alert(data['msg']);
							loadData();
						}
					},
					error:function(data){
						alert(data);
					}
				});
			}
//xml解析
			function parseXML(ssxml){
				  try{
			        xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
			        xmlDoc.async="false";
			        xmlDoc.loadXML(ssxml);
			        if(xmlDoc!=null){
			        	return xmlDoc;
			        }
				  	}catch(e){
				  		try {
				  			parser=new DOMParser();
				  			xmlDoc=parser.parseFromString(ssxml,"text/xml");
				  			if(xmlDoc!=null){
				  				return xmlDoc;
				  			}
				  		}catch(e){
				  			alert('解析xml错误 ： '+ e.message);
				  			return;
				  		}
				  	}
			}
			var cigarinvoice = {
				init : function() {
					app.mainContainer.html(tpl);
					buildBase();
					buildEvent();
					loadData();
				}
			};
			return cigarinvoice;
		});