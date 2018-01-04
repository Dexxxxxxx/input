/**
 * 
 */
define([ 'jquery', 'app','ztree', 'text!biz/security/nsrinfo.html' ], function($, app, ztree, tpl) {
	var loadData = function(){
		var menu_root ='纳税人信息管理';
		$('#menu_root').html(menu_root);
		new app.Page({
			url : app.rootPath + '/security/commInfo/list.do',
			target : 'page-div',
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					var enable = item['STATUS']=='1'?'已启用':'未启用';
					html += ''
						+'<tr data-u="'+item['ID']+'">'
						+'  <td><div><input type="checkbox" class="checkboxes checker_main" value="'+item['ID']+'" data-nsrsbh="'+item['NSRSBH']+'"></div></td>'
						+'	<td>'+item['NSRSBH']+'</td>'
						+'	<td>'+item['NAME']+'</td>'
						+'	<td>'+item['DZDH']+'</td>'
						+'	<td>'+item['YHZH']+'</td>'
						+'	<td>'+enable+'</td>'
						+'	<td>'
						+'		<div class="am-btn-toolbar">'
					    +'			<button class="btn green btn-sm" data-id="'+item['ID']+'" data-nsrsbh="'+item['NSRSBH']+'">'
					    +'			编辑 <i class="fa fa-edit"></i>'
					    +'			</button>'
						+'		</div>'
						+'	</td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
	};
	
	var buildEvent = function(){
		var id='';
		$('#data-tbody').on('click','button',function(e){
			e.stopPropagation();
			var btn = $(this);
			if(btn.attr('data-id') != null && btn.attr('data-id') != ''){
				app.nsrinfoid = btn.attr('data-id');
				app.nsrsbh = btn.attr('data-nsrsbh');
				app._function='edit';
				app.loadPage('nsrinfo_view');
			}else{
				alert('未选择查看的信息');
			}
		});
		$('#check_group').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.checkboxes').prop('checked',isChecked);
		});
		$('#btn-new').on('click',function(){
			app._function='add';
			app.loadPage('nsrinfo_view');
		});
		$('#btn-del').on('click',function(){
			var ids = [];
			var nsrids =[];
			$('.checkboxes').each(function(){
				if($(this).is(':checked') == true){
					ids.push($(this).val());
					nsrids.push($(this).attr('data-nsrsbh'))
				}
			});
			if(confirm("确定删除？")){
				app.ajax({
					url: app.rootPath + '/security/commInfo/delete.do',
					data: 'ids=' + ids+'&nsrids='+nsrids,
					success: function(data){
						loadData();
					}
				});
			}
		})
		//重置access_token
		$('#btn-reset').on('click',function(){
			var ids = [];
			var nsrsbh;
			$('.checkboxes').each(function(){
				if($(this).is(':checked') == true){
					ids.push($(this).val());
					nsrsbh=$(this).attr('data-nsrsbh');
				}
			});
			if(ids.length!=1){
				alert("请选择一条信息进行初始化");
				return;
			}
			if(confirm("确定初始化该税号信息？")){
				app.ajax({
					url: app.rootPath + '/security/commInfo/reset.do',
					data: 'id=' + ids+'&nsrsbh='+nsrsbh,
					success: function(data){
						alert("初始化成功");
						loadData();
					}
				});
			}
		})
	}

	var nsrinfo = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};
	return nsrinfo;
});