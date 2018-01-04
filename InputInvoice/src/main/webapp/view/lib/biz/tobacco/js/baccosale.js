/**
 * 
 */
define([ 'jquery', 'app', 'ztree', 'jqueryForm','text!biz/tobacco/baccosale.html' ], function($, app, ztree, jqueryForm, tpl) {
	var backmsg='';
	var z={
			a:false,b:false,c:false,d:false,e:false,f:false
			};//用户新增购方信息页面 submit 是否能进行提交
	var loadData = function(){
		$('#buyer_form').attr('action','tobacco/buyer/checkAndSave.do');
		$('#buyer_form').on('submit',function(e){
			e.preventDefault();
			if(true){
				$('#modal-buyer').modal('hide');
				$(this).ajaxSubmit({
					success: function(data){
						if(data['result'] == 'SUCCESS'){
							var data=data['rows'];
							$('#buyerid').val(data['ID']);
							$('#buyername').html(data['NAME']);
							$('#buyernsrsbh').html(data['NSRSBH']);
							$('#buyeraddress').html(data['ADDRESS']);
							$('#buyerphone').html(data['PHONE']);
							$('#buyerkhh').html(data['KHH']);
							$('#buyeryhzh').html(data['YHZH']);
							$('#ajax-modal-newBuyer').modal('hide');
							buyer.id=data['ID'];
							buyer.name=data['NAME'];
							buyer.nsrsbh=data['NSRSBH'];
							buyer.address=data['ADDRESS'];
							buyer.phone=data['PHONE'];
							buyer.khh=data['KHH'];
							buyer.yhzh=data['YHZH'];
						}else{
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
			} else {
				$('#submit-error').html("所填写信息不正确");
			}
		});
	};
	//商品对象
	function items(id,comname,code,count,dj,je,dw,sl,se) //声明对象
    {
       this.id = id;
       this.comname= comname;
       this.code= code;
       this.dw = dw;
       this.count=count;
       this.dj=dj;
       this.je=je;
       this.sl=sl;
       this.se=se;
      
    }
	//增加商品
	var addItems=function(items){
		if(msg.length==8){
			alert("发票只能添加8条信息");
			return;
		}
		for(var i=0;i<msg.length;i++){
			if(msg[i]['id']==items['id']){
				alert("商品添加重复！")
				return;
			}
		}
		msg.push(new items(items.id,items.comname,items.code,items.count,items.dj,items.je,items.dw,items.sl,items.se));
	};
	//显示商品表
	var showItemList=function(){
		var html='';
		$.each(msg,function(i,item){
			html += ''
				+'<tr data-id="'+item['id']+'">'
				+'	<td>'+item['code']+'</td>'
				+'	<td>'+item['comname']+'</td>'
				+'	<td>'+item['dw']+'</td>'
				+'	<td ><input type="text" class="countinput" data-id="'+item['id']+'" style="border:none;height:30px;" value="'+item['count']+'"></td>'
				+'	<td ><input type="text" class="djinput" data-id="'+item['id']+'" style="border:none;height:30px;" value="'+item['dj']+'"></td>'
				+'	<td>'+item['je']+'</td>'
				+'	<td>'+item['sl']+'</td>'
				+'	<td>'+item['se']+'</td>'
				+'	<td>'
				+'		<div>'
				+'				<button class="btn sbold green btn-sm" btn-type="remove" data-id="'+item['id']+'">'
				+'				移除<i class="fa fa-edit"></i>'
				+'				</button>'
				+'		</div>'
				+' </td>'
				+'</tr>';
		});
		$('#data-tbody-itemsList').html(html);
	}
	//购方信息
	var buyer={
			id:'',
			name:'',
			nsrsbh:'',
			address:'',
			phone:'',
			khh:'',
			yhzh:''
	}
	//商品列表
	var msg=[];
	var buildEvent=function(){
		$('#addBuyer').on('click',function(){
			new app.Page({
				url : app.rootPath + '/tobacco/buyer/list.do',
				target : 'page-div-buyer',
				from: 'subm_buyer',
				dataTarget: 'data-tbody-buyer',
				buildData : function(data){
					var html = ''; 
					$.each(data,function(i,item){
						html += ''
							+'<tr data-id="'+item['ID']+'">'
							+'	<td>'+item['NAME']+'</td>'
							+'	<td>'+item['NSRSBH']+'</td>'
							+'	<td>'+item['ADDRESS']+'</td>'
							+'</tr>';
					});
					return html;
				}
			}).load();
			$('#modal-buyer').modal('show');
		});
		
		$('#addNewBuyer').on('click',function(){
			$('#newbuyername').val("");
			$('#newbuyernsrsbh').val("");
			$('#newbuyeraddress').val("");
			$('#newbuyerphone').val("");
			$('#newbuyerkhh').val("");
			$('#newbuyeryhzh').val("");
			$('#ajax-modal-newBuyer').modal('show');
		});
		$('#addItems').on('click',function(){
			new app.Page({
				url : app.rootPath + '/tobacco/message/list.do?type=2',
				target : 'page-div-items',
				from: 'subm_items',
				dataTarget: 'data-tbody-items',
				buildData : function(data){
					var html = ''; 
					$.each(data,function(i,item){
						html += ''
							+'<tr data-id="'+item['ID']+'">'
							+'	<td>'+item['NAME']+'</td>'
							+'	<td>'+item['CODE']+'</td>'
							+'	<td>'+item['DW']+'</td>'
							+'</tr>';
					});
					return html;
				}
			}).load();
			$('#modal-items').modal('show');
		});
		//点击购方信息表格行事件
		$('#data-tbody-buyer').on('click','tr', function(){
			var buyerid=$(this).attr('data-id');
			app.ajax({
				url:app.rootPath + '/tobacco/buyer/getById.do?id='+buyerid,
				type:'POST',
				success:function(data){
					buyer.id=data['ID'];
					buyer.name=data['NAME'];
					buyer.nsrsbh=data['NSRSBH'];
					buyer.address=data['ADDRESS'];
					buyer.phone=data['PHONE'];
					buyer.khh=data['KHH'];
					buyer.yhzh=data['YHZH'];
					$('#buyerid').val(data['ID']);
					$('#buyername').html(data['NAME']);
					$('#buyernsrsbh').html(data['NSRSBH']);
					$('#buyeraddress').html(data['ADDRESS']);
					$('#buyerphone').html(data['PHONE']);
					$('#buyerkhh').html(data['KHH']);
					$('#buyeryhzh').html(data['YHZH']);
					$('#modal-buyer').modal('hide');
				}
			});
			
			
		});
		//点击商品信息表格事件
		$('#data-tbody-items').on('click','tr', function(){
			var itemsid=$(this).attr('data-id');
			app.ajax({
				url:app.rootPath + '/tobacco/message/getById.do?id='+itemsid,
				type:'POST',
				success:function(data){
					items.id=data['ID'];
					items.comname=data['NAME'];
					items.code=data['CODE'];
					items.dw=data['DW'];
					items.count="0.00";
					items.dj="0.00";
					items.je=items.count*items.dj;
					items.sl=data['SL'];
					items.se=data['SL']*items.je/100;
					addItems(items);
					showItemList();
				}
			});
			
		});
		//移除商品列表
		$('#data-tbody-itemsList').on('click','button', function(e){
			e.stopPropagation();
			var btn = $(this);
			if(btn.attr('btn-type') == 'remove'){
				var	itemsid=btn.attr('data-id');
				for(var i=0;i<msg.length;i++){
					if(msg[i]['id']==itemsid){
						msg.splice(i,i+1);
					}
				}
				showItemList();
			}
		});
		$('#doInvoice').on('click',function(){
			var buy = eval(buyer);
			var item = eval(msg);
			var json = {};
			json.buyer = buy;
			json.items = item;
			json = JSON.stringify(json);
			$.ajax({
				url:app.rootPath + '/tobacco/sale/doInvoice.do',
				type:'POST',
				data:json,
				async:false,
				contentType: "application/json; charset=utf-8",
				success:function(data){
					if(data['result'] == 'SUCCESS'){
						$.each(data['rows'],function(i,item){
							var fpqqlsh=item['fpqqlsh'];
							var xml=item['xml'];
							//1、参数设置
							setting();
							//2、查询未开票
							checkInvoice();
							//3、开票
							doInvoice(fpqqlsh,xml);
						});
					}else{
						alert(data['msg'])
					}
				}
			});
			
		});
		$('#data-tbody-itemsList').on('change','input', function(e){
			e.stopPropagation();
			var btn = $(this);
			//商品数量
			if(btn.attr('class') == 'countinput'){
				var	itemsid=btn.attr('data-id');
				//小数时，保留两位小数
				if(!$.trim($(this).val()).match(/^\d+(\.\d{2})?$/)){
//					/^\d+(\.\d+)?$/     可包含n位小数
					alert("输入数字不合法,输入为数字保留两位小数");
					changeCount(0,itemsid);
					return;
				}else{
					changeCount($.trim($(this).val()),itemsid);
					return;
				}
			}
			//商品单价
			if(btn.attr('class') == 'djinput'){
				var	itemsid=btn.attr('data-id');
				//小数时，保留两位小数
				if(!$.trim($(this).val()).match(/^\d+(\.\d{2})?$/)){
//					/^\d+(\.\d+)?$/     可包含n位小数
					alert("输入数字不合法,输入为数字保留两位小数");
					changeCount(0,itemsid);
					return;
					
				}else{
					changeDj($.trim($(this).val()),itemsid);
					return;
				}
			}
		});
		var changeCount=function(count,itemsid){
			for(var i=0;i<msg.length;i++){
				if(msg[i]['id']==itemsid){
					msg[i]['count']=count;
					msg[i]['je']=(msg[i]['dj']*count).toFixed(2);
					msg[i]['se']=(msg[i]['sl']*msg[i]['je']/100).toFixed(3);
				}
			}
			showItemList();
		}
		var changeDj=function(dj,itemsid){
			for(var i=0;i<msg.length;i++){
				if(msg[i]['id']==itemsid){
					msg[i]['dj']=dj;
					msg[i]['je']=(msg[i]['count']*dj).toFixed(2);
					msg[i]['se']=(msg[i]['sl']*msg[i]['je']/100).toFixed(3);
				}
			}
			showItemList();
		}
		
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
				var sInputInfo = ''
					$.ajax({
						url:app.rootPath + '/invoiceCall/setting.do',
						type:'POST',
						contentType: "application/json; charset=utf-8",
						success:function(data){
							if(data['result']=='SUCCESS'){
								sInputInfo=data['rows'];
								xmlCheck(sInputInfo);
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
		//2、税控钥匙查询
		var keyCheck= function(){
			
		}
		//3、查余票
		var checkInvoice = function(){
				var ret='';
				var sInputInfo = ''
				var fplxdm='004'
					$.ajax({
						url:app.rootPath + '/invoiceCall/checkInvoice.do?fplxdm='+fplxdm,
						type:'POST',
						async:false,
						contentType: "application/json; charset=utf-8",
						success:function(data){
							if(data['result']=='SUCCESS'){
								sInputInfo=data['rows'];
								ret=xmlCheck(sInputInfo);
								checkInvoiceBack(ret);
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
		//4、执行开票
		var doInvoice = function(fpqqlsh,xml){
					try {
						ret = sk.Operate(xml);
						saveInvoice(fpqqlsh,ret)
					} catch (e) {
						alert(e.message + ",errno:" + e.number);
					}
				}
		var saveInvoice=function(fpqqlsh,ret){
			var ss={
				"fpqqlsh":fpqqlsh,
				"xml":ret
			}
			$.ajax({
				url:app.rootPath + '/tobacco/sale/saveInvoice.do',
				type:'POST',
				async:false,
				data:JSON.stringify(ss),
				contentType: "application/json; charset=utf-8",
				success:function(data){
					if(data['result'] == 'SUCCESS'){
						result=data['rows'];
						fpdm=result['fpdm'];
						fphm=result['fphm'];
						if (confirm("开票成功 ，发票代码："+fpdm+"，发票号码："+fphm+" ，是否打印？")) {
							print(fpdm,fphm);
						}
						msg.length=0;
						showItemList();
					}else{
						alert(data['msg']);
					}
				},
				error:function(data){
					alert(data);
				}
			});
		}
		//6、打印
		var print = function(fpdm,fphm){
			var sInputInfo = '';
			var backInfo = '';
			$.ajax({
				url:app.rootPath + '/invoiceCall/print.do?fpdm='+fpdm+"&fphm="+fphm+"&fplxdm=004",
				type:'POST',
				contentType: "application/json; charset=utf-8",
				success:function(data){
					if(data['result']=='SUCCESS'){
						sInputInfo=data['rows'];
//						sInputInfo = new Base64().decode(sInputInfo);
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
	};
	var baccosale = {
		init : function() {
			app.mainContainer.html(tpl);
			loadData();
			buildEvent();
		}
	};

	return baccosale;
});