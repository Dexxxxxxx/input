/**
 * 
 */
define([ 'jquery', 'app', 'ztree', 'jqueryForm','text!biz/tobacco/baccomessage.html' ], function($, app, ztree, jqueryForm, tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/tobacco/message/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					var sp_type=item['TYPE']=='1'?'烟草收购':'烟草转售';
					html += ''
						+'<tr data-id="'+item['ID']+'">'
						+'  <td><div class="checker"><span><input type="checkbox"  class="checkboxes" value="'+item['ID']+'"></span></div></td>'
						+'	<td>'+item['NAME']+'</td>'//商品名称
						+'	<td>'+sp_type+'</td>'//商品种类
						+'	<td>'+item['CODE']+'</td>' //商品编码
						+'	<td>'+item['ZXBM']+'</td>' //自行编码
						+'	<td>'+item['DW']+'</td>'//商品单位
						+'	<td>'+item['SL']+'</td>'//商品税率
						+'	<td>'+item['CREATETIME']+'</td>'//创建时间
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
				app.baccomessage_id=btn.attr('data-id');
				app._function='edit';
				app.loadPage('baccomessage_view');
			} 
		});
		$('#btn-add').click(function(){
			app._function='add';
			app.loadPage('baccomessage_view');
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
					url:app.rootPath + '/tobacco/message/delete.do',
					type:'POST',
					data:'ids='+ids,
					success:function(data){
						loadData();
				   }
				});
		    }
		});
		
	}
	var baccomessage = {
		init : function() {
			app.mainContainer.html(tpl);
			loadData();
			buildEvent();
		}
	};

	return baccomessage;
});