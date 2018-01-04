/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap','ztree','My97DatePicker', 'text!biz/invoiceStatistics/invoiceCollect.html' ], function($, app, bootstrap, ztree,My97DatePicker, tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/invoiceStatistics/invoiceInfo/listCollect.do',
			from: 'search-form',
			limit: 20,
			dataTarget: 'data-tbody',
			before: function(){
				$('.sl_hide').hide();
				$('.sl_checkboxes').each(function(){
					if($(this).is(':checked') == true){
						var ss=$(this).val();
						if(ss==1.5){
							ss=15;
						}
						$('.sl_'+ss).show();
					}
				});
			},
			buildData : function(data){
				$.each(data,function(i,item){
					var sl=item['SL']
					if(sl==1.5){
						sl=15;
					}
					$('#je_'+sl).html(item['JE'])
					$('#se_'+sl).html(item['SE'])
				});
			}
		}).load();
	};
	
	var buildEvent = function(){
		$('.sl_checkboxes').click(function(){
			var ss=$(this).val();
			if(ss==1.5){
				ss=15;
			}
			if($(this).is(':checked') == true){
				$('.sl_'+ss).show();
			}else{
				$('.sl_'+ss).hide();
			}
			
		});
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
		});
		$('#authpointcheck_group').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.authpointchecks').prop('checked',isChecked);
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
		
	}
	
	var incomeInvoiceManage = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return incomeInvoiceManage;
});