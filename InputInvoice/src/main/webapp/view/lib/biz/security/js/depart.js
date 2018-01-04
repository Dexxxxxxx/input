/**
 * 
 */
define([ 'jquery', 'app', 'ztree','text!biz/security/depart.html' ], function($, app,ztree,tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/security/depart/list.do',
			target : 'page-div',
			from: 'search-form',
			limit :10,
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr>'
						+'  <td><div class="checker"><span><input type="checkbox"  class="checkboxes" value="'+item['ID']+'"></span></div></td>'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['PNAME']+'</td>'
						+'	<td>'+item['CODE']+'</td>'
						+'	<td>'+item['ADDCODE']+'</td>'
						+'	<td>'+item['CONTACTS']+'</td>'
						+'	<td>'+item['PHONE']+'</td>'
						+'	<td>'+item['ADDRESS']+'</td>'
						+'	<td>'+item['DESCRIPTION']+'</td>'
						+'	<td>'+item['CREATETIME']+'</td>'
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
				app.depart_id=btn.attr('data-id');
				app._function='edit';
				app.loadPage('depart_view');
			}
		});
		$('#btn-add').click(function(){
			app._function='add';
			app.loadPage('depart_view');
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
					url:app.rootPath + '/security/depart/delete.do',
					type:'POST',
					data:'ids='+ids,
					success:function(data){
						alert('删除成功');
						loadData();
				   }
				});
		    }
		});
		$('#btn-fn').click(function(){
			$('#ajax-modal').modal();
		});
		
	}
			
	var handleSample2 = function () {
		
    }
	var depart = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
			handleSample2();
		}
	};

	return depart;
});