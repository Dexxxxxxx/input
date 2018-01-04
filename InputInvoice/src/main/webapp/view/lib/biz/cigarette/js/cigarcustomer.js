/**
 * 
 */
define([ 'jquery', 'app', 'ztree','text!biz/cigarette/cigarcustomer.html' ], function($, app,ztree,tpl) {
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/cigarette/customer/list.do',
			target : 'page-div',
			limit : 10,
			from: 'search-form',
			dataTarget: 'data-tbody',
			buildData : function(data){
				var html = ''; 
				$.each(data,function(i,item){
					html += ''
						+'<tr>'
						+'  <td><div class="checker"><span><input type="checkbox"  class="checkboxes" value="'+item['ID']+'"></span></div></td>'
						+'	<td>'+item['GHDWDM']+'</td>'
						+'	<td>'+item['GHDWMC']+'</td>'
						+'	<td>'+item['GHDWNSRSBH']+'</td>'
						+'	<td>'+item['GHDWDZDH']+'</td>'
						+'	<td>'+item['GHDWYHZH']+'</td>'
						+'	<td>'
						+'		<div>'
						+'				<button class="btn sbold green btn-sm" btn-type="edit" data-id="'+item['ID']+'" data-dwdm="'+item['GHDWDM']+'">'
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
			app.customerid=btn.attr('data-id');
			if(btn.attr('btn-type') == 'edit'){
				//显示模态框
				app.ajax({
					url:app.rootPath + '/cigarette/customer/getById.do',
					type:'POST',
					data:'id='+app.customerid,
					success:function(data){
						$('#gf_id').val(data['ID']);
						$('#ghdwdm').val(data['GHDWDM']);
						$('#ghdwmc').val(data['GHDWMC']);
						$('#ghdwnsrsbh').val(data['GHDWNSRSBH']);
						$('#ghdwdzdh').val(data['GHDWDZDH']);
						$('#ghdwyhzh').val(data['GHDWYHZH']);
					}
				});
				$('#modal-customerinfo').modal('show');
				
			}
		});
		//卷烟客户增加
		$('#btn-add').click(function(){
			app.loadPage('cigarcustomer_view');
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
					url:app.rootPath + '/cigarette/customer/delete.do',
					type:'POST',
					data:'ids='+ids,
					success:function(data){
						alert('删除成功');
						loadData();
				   }
				});
		    }
		});
		$('#customerinfo_form').attr('action','cigarette/customer/update.do');
		$('#customerinfo_form').on('submit', function(e) {
			e.preventDefault();
				$(this).ajaxSubmit({
	                success: function(data){
	                	if(data.result=="SUCCESS"){
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
		});
		
		$('#checkall').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.checkboxes').prop('checked',isChecked);
		});
		//导入txt源数据
		$("#btn-import").click(function(){
			//$("#ajax-import").modal("show");
			$("#fileImport").hide();
			$(".selectFile").show();
			$("#file").val("");
		});
		
		$(document).click(function(){
			$("#fileImport").show();
			$(".selectFile").hide();
		});
		$("#fileImport").click(function(e){
			e.stopPropagation();
		});
		$(".selectFile").click(function(e){
			e.stopPropagation();
		});
		
		$('#uploadForm').attr('action','cigarette/customer/import.do');
		$('#uploadForm').on('submit', function(e) {
            	e.preventDefault();
//            	$(".selectFile").hide();
//    			$("#fileImport").show();
				$(this).ajaxSubmit({
                    success: function(data){
                    	var bb = data.replace(/(\")/g, "")
                    	var result=bb.substr(0, 1);
                    	if(result=='Y'){
                    		alert(bb.substr(1, bb.length));
                    		loadData();
                    		$("#ajax-import").modal("hide")
                    	}else if(result=='N'){
                    		alert(bb.substr(1, bb.length));
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
	}
	var cigarcustomer = {
		init : function() {
			app.mainContainer.html(tpl);
			buildEvent();
			loadData();
		}
	};

	return cigarcustomer;
});