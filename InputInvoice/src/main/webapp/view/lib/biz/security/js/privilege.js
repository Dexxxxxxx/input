/**
 * 
 */
define([ 'jquery', 'app', 'text!biz/security/privilege.html' ], function($, app, tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/security/privilege/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr>'
						+'  <td><div class="checker"><span><input type="checkbox"  class="checkboxes" value="'+item['ID']+'"></span></div></td>'
						+'	<td>'+item['PNAME']+'</td>'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['URL']+'</td>'
//						+'	<td class="am-hide-sm-only">'+item['PID']+'</td>'
						+'	<td class="am-hide-sm-only">'+item['ICON']+'</td>'
						+'	<td class="am-hide-sm-only">'+item['ORDERBY']+'</td>'
						+'	<td class="am-hide-sm-only">'+item['PTYPE']+'</td>'
						+'	<td>'
						+'		<div class="am-btn-toolbar">'
						+'			<div class="am-btn-group am-btn-group-xs">'
						+'				<button class="btn sbold green btn-sm" btn-type="edit" data-id="'+item['ID']+'">'
						+'				编辑 <i class="fa fa-edit"></i>'
						+'				</button>'
						+'			</div>'
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
				app.privilege_id=btn.attr('data-id');
				app._function='edit';
				app.loadPage('privilege_view');
			} else if(btn.attr('btn-type') == 'del'){
				var privilege_id=btn.attr('data-id');
				app.ajax({
					url:app.rootPath + '/security/privilege/delete.do',
					type:'POST',
					data:'id='+privilege_id,
					success:function(data){
						alert("删除成功!");
						loadData();
					}
				});
			}
		});
		$('#btn-add').click(function(){
			app._function='add';
			app.loadPage('privilege_view');
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
					url:app.rootPath + '/security/privilege/delete.do',
					type:'POST',
					data:'ids='+ids,
					success:function(data){
						loadData();
				   }
				});
		    }
		});
		
	}
			
	var privilege = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};

	return privilege;
});