/**
 * 
 */
define([ 'jquery', 'app', 'ztree', 'jqueryForm','text!biz/tobacco/baccobuyer.html' ], function($, app, ztree, jqueryForm, tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/tobacco/buyer/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr data-id="'+item['ID']+'">'
						+'  <td><div class="checker"><span><input type="checkbox"  class="checkboxes" value="'+item['ID']+'"></span></div></td>'
						+'	<td>'+item['NAME']+'</td>'//公司名称
						+'	<td>'+item['NSRSBH']+'</td>' //纳税人识别号
						+'	<td>'+item['ADDRESS']+'</td>'//地址
						+'	<td>'+item['PHONE']+'</td>'//电话
						+'	<td>'+item['KHH']+'</td>'//开户行
						+'	<td>'+item['YHZH']+'</td>'//银行账号
						+'	<td>'
						+'		<div>'
						+'				<button class="btn sbold green btn-sm" btn-type="edit" data-id="'+item['ID']+'">'
						+'				编辑 <i class="fa fa-edit"></i>'
						+'				</button>'
						+'		</div>'
						+'	</td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	};
	var buildEvent = function(){
		$('#data-tbody').on('click','button', function(){
			var btn = $(this);
			if(btn.attr('btn-type') == 'edit'){
				app.baccobuyer_id=btn.attr('data-id');
				app._function='edit';
				app.loadPage('baccobuyer_view');
			} 
		});
		$('#btn-add').click(function(){
			app._function='add';
			app.loadPage('baccobuyer_view');
		});
		
		$('#checkall').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.checkboxes').prop('checked',isChecked);
		});
		$('#btn-del').click(function(){
			if(confirm("你确定要删除？"))
		    {
				var ids='';
				$('.checkboxes').each(function(){
					if($(this).is(":checked")==true){
					ids+=$(this).val();
						ids+=',';
					}
				  });
				ids=ids.substring(0,ids.lastIndexOf(","));
				app.ajax({
					url:app.rootPath + '/tobacco/buyer/delete.do',
					type:'POST',
					data:'ids='+ids,
					success:function(data){
						loadData();
				   }
				});
		    }
		});
		
	}
	var baccobuyer = {
		init : function() {
			app.mainContainer.html(tpl);
			loadData();
			buildEvent();
		}
	};

	return baccobuyer;
});