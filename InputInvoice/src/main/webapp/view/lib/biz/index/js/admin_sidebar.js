/**
 * 
 */
define([ 'jquery', 'app'], function($, app) {
	
	//目前只能支持2级菜单
	var buildMenu = function(data){
		var rolePrivilege = data['rolePrivilege'];
		var tpl = '';
		//var active;
		$.each(rolePrivilege, function(i, item){
			if('' != item['URL'] && !!item['JS'] && '' != item['JS']){
				pageConfig[item['URL']] = {"js":item['JS']};
			}
			if(item['PTYPE'] == '1' && item['PID'] == '-1'){
				tpl += ''
				+'<li class="nav-item start open">'
				+'	<a href="javascript:;" class="nav-link nav-toggle">'
				+'		<i class="'+item['ICON']+'"></i>'
				+'		<span class="title">'+item['NAME']+'</span>'
				+'		<span class="selected"></span>'
				+'		<span class="arrow open"></span>'
				+'	</a>'
				+'	<ul class="sub-menu">'
				+ buildMenuChild(rolePrivilege, item['ID'])
				+'	</ul>'
				+'</li>';
			}
		});
		$('#admin_sidebar').html(tpl);
	};
	
	var buildMenuChild = function(rp, id){
		var tplChild = '';
		$.each(rp, function(i, item){
			if(item['PTYPE'] == '1' && item['PID'] == id && item['PID'] != '-1' && item['ID'] != id){
				tplChild += ''
					+'	<li class="nav-item start">'
					+'		<a href="javascript:void(0);" class="nav-link " target-url="'+item['URL']+'">'
					+'			<i class="'+item['ICON']+'"></i>'
					+'			<span class="title">'+item['NAME']+'</span>'
					+'			<span class=""></span>'
					+'		</a>'
					+'	</li>';
			}
		});
		return tplChild;
	};
	var configUserInfo = function(data){
		var user = data['user'];
		$('#login-user-name').html(user['NAME']);
		$('#logout-a').on('click',function(e){
			if(confirm('确定要退出系统?')){
				app.ajax({
					url : app.rootPath + '/security/login/logout.do',
	    			type : 'GET',
	    			dataType : 'json',
	    			async : 'true',
	    			data : 't=' + new Date().getTime(),
	    			success : function(data) {
	    				window.location.href = "login.html";
	    			}
				});
			}
		});
		$('#changePwd').unbind('click').on('click',function(e){
			$('#pwd_form .span-text:gt(0)').html("");
			$(':input','#pwd_form').val('');
			$('#ajax-modal-pwd').modal('show');
			$('#pwd_form').attr('action',app.rootPath+'/security/login/updatePassword.do');
			$('#pwd_form').unbind('submit').on('submit', function(e) {
	            e.preventDefault();
	            $(this).ajaxSubmit({
	                success: function(data){
//	                	data = JSON.parse(data);
	                	if(data['result'] == 'SUCCESS'){
	                		$('#ajax-modal-pwd').modal('hide');
	                		alert('密码修改成功，请重新登录！');
	                		window.location.href = "login.html";
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
	 						_ajax_config.error('出错了! error:'  + xhr['status']);
	 					}
	 				}
	            });
			});
		});
	};
	var checkPwd=function(){
		$('#newPassword-error').html("");
		$('#newPassword2-error').html("");
		$('#newPassword').on('blur',function(){
			if(!$(this).val()){
				$('#newPassword-error').html("输入6位以上密码");
			} else {
				$('#newPassword-error').html("");
			}
		});
		$('#newPassword2').on('blur',function(){
			if(!$(this).val()){
				$('#newPassword2-error').html("输入6位以上密码");
			} else {
				if($(this).val() != $('#newPassword').val()){
					$('#newPassword2-error').html("两次密码输入不一致");
				} else {
					$('#newPassword2-error').html("");
				}
			}
		});
	};
	var admin_sidebar = {
		init : function() {
			checkPwd();
			app.ajax({
				url : app.rootPath + '/security/login/getLoginUserInfo.do?t=' + new Date().getTime(),
    			type : 'GET',
    			dataType : 'json',
    			async : 'true',
    			data : '',
    			success : function(data) {
    				if(!!!data || data['user'] == null){
    					alert('未登录!');
						window.location.href = "login.html";
						return;
    				}
    				app.appUser = data['user'];
    				configUserInfo(data);
    				buildMenu(data);
    				//构建pageConfig对象 需要key和js
    				if(location.hash.substr(1) == ''){
    					app.loadPage('index');
    				}
    				//$(window).hashchange();
    				//判断用户的密码是否为系统初始化密码 data['defaultPasswordFlag']>0
    				if(data['defaultPasswordFlag']>0){
    					$('#changePwd').click();
    				}
    			}
			});
		}
	};

	return admin_sidebar;
});