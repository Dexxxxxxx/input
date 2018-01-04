/**
 * 
 */
define([ 'jquery', 'app', 'bootstrap', 'My97DatePicker','text!biz/report/tamreport.html' ], function($, app, bootstrap, My97DatePicker,tpl) {
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
		$('#start_date').val(new Date().Format("yyyy-MM-dd"));
		$('#end_date').val(new Date().Format("yyyy-MM-dd"));
		$('#title').html($('#start_date').val()+'--'+$('#end_date').val()+'累计');
		$('#search-form').submit();
	};
	var buildEvent = function(){
		$('#search-form').attr('action','report/tamreport/count.do');
		$('#search-form').on('submit', function(e) {
            e.preventDefault();
            	$(this).ajaxSubmit({
                    success: function(data){
                    	data = JSON.parse(data);
                    	if(data['result'] == 'SUCCESS'){
                    		//当日统计
                    		$('#daycount').html(data['rows']['byday']['ZZSL']);
                    		$('#daycountall').html(data['rows']['byday']['ZZSL']);
                    		$('#daypospayment').html(data['rows']['byday']['ZSHJ']);
                    		$('#daypospaymentall').html(data['rows']['byday']['ZSHJ']);
                    		$('#dayzj').html(data['rows']['byday']['ZSHJ']);
                    		$('#daypayment').html(data['rows']['byday']['ZSHJ']);
                    		var html='';
                    		$.each(data['rows']['bymonth'],function(i,item){
        						html += ''
        					+'	<div class="table-toolbar">'
        					+'	<span class="caption-subject bold uppercase">'+item['time']+'月累计</span>'
        					+'		<div class="dataTables_wrapper no-footer">'
        					+'		<div class="table-scrollable">'
        					+'				<table class="table table-striped table-bordered table-hover order-column dataTable no-footer"id="sample_1" role="grid" aria-describedby="sample_1_info">'
        					+'					<thead>'
        					+'						<tr role="row">'
        					+'							<th class="sorting_disabled" tabindex="0" aria-controls="sample_1" rowspan="1" colspan="1" style="width: 12.5%;">数量 </th>'
        					+'							<th class="sorting_disabled" tabindex="0" aria-controls="sample_1" rowspan="1" colspan="1" style="width: 12.5%;">征收合计</th>'
        					+'							<th class="sorting_disabled" tabindex="0" aria-controls="sample_1" rowspan="1" colspan="1" style="width: 12.5%;">减：退税</th>'
        					+'							<th class="sorting_disabled" tabindex="0" aria-controls="sample_1" rowspan="1" colspan="1" style="width: 12.5%;">滞纳金</th>'
        					+'							<th class="sorting_disabled" tabindex="0" aria-controls="sample_1" rowspan="1" colspan="1" style="width: 12.5%;">其他收入</th>'
        					+'							<th class="sorting_disabled" tabindex="0" aria-controls="sample_1" rowspan="1" colspan="1" style="width: 12.5%;">合计</th>'
        					+'						</tr>'
        					+'					</thead>'
        					+'					<tbody id="data-tbody"></tbody>'
        					+'						<tr>'
        					+'							<td>数量</td>'
        					+'							<td id="monthcount" class="text-right">'+item['ZZSL']+'</td>'
        					+'							<td class="text-right">0</td>'
        					+'							<td class="text-right">0</td>'
        					+'							<td class="text-right">0</td>'
        					+'							<td id="monthcountall" class="text-right">'+item['ZZSL']+'</td>'
        					+'						</tr>'
        					+'						<tr>'
        					+'							<td>POS机缴款</td>'
        					+'							<td id="monthpospayment" class="text-right">'+item['ZSHJ']+'</td>'
        					+'							<td class="text-right">0</td>'
        					+'							<td class="text-right">0</td>'
        					+'							<td class="text-right">0</td>'
        					+'							<td id="monthpospaymentall" class="text-right">'+item['ZSHJ']+'</td>'
        					+'						</tr>'
        					+'						<tr>'
        					+'							<td>总计</td>'
        					+'							<td id="monthzj" class="text-right">'+item['ZSHJ']+'</td>'
        					+'							<td class="text-right">0</td>'
        					+'							<td class="text-right">0</td>'
        					+'							<td class="text-right">0</td>'
        					+'							<td id="monthpayment" class="text-right">'+item['ZSHJ']+'</td>'
        					+'						</tr>'
        					+'				</table>'
        					+'			</div>'
        					+'			<div class="row" id="page-div"></div>'
        					+'		</div>'
        					+'	</div>'
        					});
                    		$('#body').html(html);
                    		$('#title').html($('#start_date').val()+'--'+$('#end_date').val()+'累计');
                    	} else {
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
		});
		$('.datePicker').click(function(){
			WdatePicker();
		}); 
		$('#tamname').on('click', function(e){
			$('#search_key').val('');
			new app.Page({
				url : app.rootPath + '/report/tamreport/listtam.do',
				target : 'page-div-tail',
				from: 'tam-search',
				dataTarget: 'data-tbody-tail',
				buildData : function(data){
					var html = '';
					$.each(data,function(i,item){
						item['POSTYPE']=(item['POSTYPE']=='1'?'税库银':'现金');
						html += ''
							+'<tr data-id='+item['ID']+' data-name='+item['NAME']+'>'
							+'	<td>'+item['DNAME']+'</td>'
							+'	<td>'+item['TAM_ID']+'</td>'
							+'	<td>'+item['NAME']+'</td>'
							+'	<td>'+item['POSTYPE']+'</td>'
							+'	<td>'+item['POS_ID']+'</td>'
							+'</tr>';
					});
					return html;
				}
			}).load();
			$('#ajax-modal').modal('show');
		});
		
		$('#data-tbody-tail').on('click','tr', function(){
			//将tam机名称赋值到input显示标签
			$('#tamname').val($(this).attr('data-name'));
			//将tamid编号赋值到input隐藏标签
			$('#tamid').val($(this).attr('data-id'));
			//关闭模态
			$('#ajax-modal').modal('hide');
			
		});
		$('#showall').on('click', function(e){
			$('#tamname').val('所有TAM机');
			$('#tamid').val('999');
			$('#ajax-modal').modal('hide');
		});
		$('#excel').on('click',function(){
			
	            var form = $("<form>");   //定义一个form表单
	            form.attr('style', 'display:none');   //在form表单中添加查询参数
	            form.attr('target', '');
	            form.attr('method', 'post');
	            form.attr('action', app.rootPath + '/report/tamreport/exec.do');

	            var input1 = $('<input>');
	            input1.attr('type', 'hidden');
	            input1.attr('name', 'start_date');
	            input1.attr('value', $('#start_date').val());
	            
	            var input2 = $('<input>');
	            input2.attr('type', 'hidden');
	            input2.attr('name', 'tamname');
	            input2.attr('value', $('#tamname').val());
	            
	            var input3 = $('<input>');
	            input3.attr('type', 'hidden');
	            input3.attr('name', 'tam');
	            input3.attr('value', $('#tamid').val());
	            
	            var input4 = $('<input>');
	            input4.attr('type', 'hidden');
	            input4.attr('name', 'end_date');
	            input4.attr('value', $('#end_date').val());
	            
	            $('body').append(form);  //将表单放置在web中 
	            form.append(input1);   //将查询参数控件提交到表单上
	            form.append(input2);
	            form.append(input3);
	            form.append(input4);
	            form.submit();
	            form.remove();
	            $('#title').html($('#start_date').val()+'--'+$('#end_date').val()+'累计')
		});
	}
	
	var tamreport = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return tamreport;
});