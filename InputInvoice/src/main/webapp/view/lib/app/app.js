/**
 * -----------
 */
var pageConfig = {
		"index" : {"js":"biz/index/js/index"},
		"admin_sidebar" : {"js":"biz/index/js/admin_sidebar"}
};
define([ "jquery", "hashchange", "bootstrap", "jqueryForm", "css!app.css"], function($, h, bootstrap, jqueryForm, css ) {
	changeLoadPage = function(key) {
		var js = '';
		if (!pageConfig.hasOwnProperty(key)) {
			key = key.substr(0, key.indexOf('_'));
			js =  pageConfig[key]['js'].substr(0, pageConfig[key]['js'].indexOf('.js')) + "_view.js";
		} else {
			js = pageConfig[key]['js'];
		}
		app.mainContainer.html(app.loadingImg);
		require([js], function(js, html) {
			js.init();
			Layout.windowResize();
		});
	};
	function getRootPath() {
		var pathName = window.location.pathname.substring(1);
		var webName = pathName == '' ? '' : pathName.substring(0, pathName
				.indexOf('/'));
		var a = window.location.protocol + '//' + window.location.host + '/'
				+ webName;
		return a;
	}
	;

	var app = {
		init : function() {
			require([pageConfig['admin_sidebar']['js']], function(admin_sidebar) {
				admin_sidebar.init();
				$('#admin_sidebar').on('click', 'a', function() {
					var key = $(this).attr('target-url');
					//active
					if (key &&  key != '') {
						$('#admin_sidebar li').removeClass('active');
						$('#admin_sidebar li').removeClass('open');
						$(this).parent('li').parent('ul').parent('li').addClass('active');
						$(this).parent('li').parent('ul').parent('li').addClass('open');
						$(this).parent('li').addClass('active');
						$(this).parent('li').addClass('open');
						app.loadPage(key);
						$('a[data-target=".navbar-collapse"]').click();
					}
				});
			});
			$(window).hashchange(function() {
				if (location.hash) {
					changeLoadPage(location.hash.substr(1));
				}
			});
		},
		loadPage : function(key) {
			self.location.hash = key;
			//$(window).hashchange();
		},
		loadingImg : 'loading.......',
		rootPath : getRootPath(),
		mainContainer : $("#admin-content"),
		appUser : null,
		ajax : function(ajax_config){
	    	var _ajax_config = {
	    			url : '',
	    			type : 'POST',
	    			dataType : 'json',
	    			contentType : 'application/x-www-form-urlencoded',
	    			async : 'false',
	    			data : '',
	    			success : function(data) {},
	    			error : function(msg) {
	    				if(null == msg){
	    					alert('发送错误!');
	    				} else {
	    					alert(msg);
	    				}
	    			}
	    	};
	    	$.extend(_ajax_config, ajax_config);
	    	$.ajax({
				url : _ajax_config.url,
				type : _ajax_config.type,
				dataType : _ajax_config.dataType,
				contentType : _ajax_config.contentType,
				async : _ajax_config.async,
				data : _ajax_config.data,
				success : function(data) {
					if(data['result'] == 'SUCCESS'){
						_ajax_config.success(data['rows']);
					} else if(data['result'] == 'ERROR'){
						_ajax_config.error(data['msg']);
					}
					Layout.windowResize();
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
	    },
	    
	    /**
	     * 用于分页grid
	     * config{
	     * url : 数据请求地址,
		 * target : 分页容器id,
		 * from: 查询条件表单id,
		 * buildPage : 是否显示分页,
		 * dataTarget: 'data-tbody'//数据所在的tbody,
		 * start: 1//当前第几页,
		 * limit : 10//每页数据量,默认10,
		 * buildData : function(data){}//构建grid数数据表格代码,
		 * after : function(){}//请求前执行,
		 * before : function(){}//请求后执行
	     * }
	     */
	    Page : function(_config){
			var _this = this;
			var cfg = {
					url : '',
					target : 'page-div',
					from: 'search-form',
					buildPage : true,
					dataTarget: 'data-tbody',
					start: 1,
					tmp_start: true,
					limit : 10,
					count : 0,
					pageSize : 0,
					buildData : function(data){return '';},
					after : function(){},
					before : function(){},
					data : {}
			};
			$.extend(cfg, _config);
			
			$('#'+cfg.target).unbind('change').on('change', ' input', function(){
				var val = $(this).val();
				if(val <= 1){
					cfg.start = 1;
				} else if(val >= cfg.pageSize){
					cfg.start = cfg.pageSize;
				} else {
					cfg.start = val;
				}
				cfg.tmp_start = false;
				_this.load();
			});
			$('#'+cfg.target).unbind('click').on('click', 'span', function(){
				var dataBtn = $(this).attr('data-btn');
				if(dataBtn == 'prev'){
					if(cfg.start <= 1){
						return;
					} else {
						cfg.start = parseInt(cfg.start,10) - 1;
					}
					cfg.tmp_start = false;
					_this.load();
				} else if(dataBtn == 'next'){
					if(cfg.start >= cfg.pageSize){
						return;
					} else {
						cfg.start = parseInt(cfg.start,10) + 1;
					}
					cfg.tmp_start = false;
					_this.load();
				}
			});
			
			var buildPage = function(data){
				cfg.count = parseInt(data['total'],10);
				var num = cfg.count/cfg.limit;
				if(cfg.count == 0){
					cfg.pageSize = 1;
				} else if(cfg.count > 0 && cfg.count%cfg.limit == 0){
					cfg.pageSize = num;
				} else {
					num = (num+"").substr(0,(num+"").indexOf('.'))
					cfg.pageSize = parseInt(num,10) + 1;
				}
				var html = ''
				    +'<div class="col-md-5 col-sm-5">'
					+'<div class="dataTables_info" role="status" aria-live="polite">当前'+cfg.start+'/'+cfg.pageSize+'页.共'+cfg.count+'条.</div>'
					+'</div>'
					+'<div class="col-md-7 col-sm-7" style="text-align:right;">'
					+'	<div class="dataTables_paginate paging_bootstrap_extended">'
					+'		<div class="pagination-panel">'
					+'			<span class="btn btn-sm default prev" data-btn="prev"><i class="fa fa-angle-left"></i></span>'
					+'			<input type="text" class="pagination-panel-input form-control input-sm input-inline input-mini" maxlenght="5" value="'+cfg.start+'" style="text-align: center; margin: 0 5px;">'
					+'			<span class="btn btn-sm default next" data-btn="next"><i class="fa fa-angle-right"></i></span>'
					+'		</div>'
					+'	</div>'
					+'</div>';
				$('#'+cfg.target).html(html);
			};
			
			//'start=&limit='
			$('#'+cfg.from).append('<input type="hidden" name="start" value="'+cfg.start+'"/><input type="hidden" name="limit" value="'+cfg.limit+'"/>');
			$('#'+cfg.from).append('<input type="hidden" name="t" value="-1"/>');
			$('#'+cfg.from).unbind('submit').attr('action', cfg.url).on('submit', function(e) {
				cfg.before();
	            e.preventDefault();
	            if(cfg.tmp_start == true){
	            	cfg.start = 1;
	            	$('#'+cfg.from).find('input[name=start]').val(cfg.start);
	            }
	            $('#'+cfg.from).find('input[name=t]').val(new Date().getTime());
	            $('#my-modal-loading').modal({"dimmer": false});
	            $(this).ajaxSubmit({
	            	 success : function(data){
	            		cfg.tmp_start = true;
//	            		data = JSON.parse(data);   ???????
	            		//构建grid
	            		cfg.data = data['rows'];
	 					$('#'+cfg.dataTarget).html(cfg.buildData(data['rows']));
	 					//构建分页html
	 					if(cfg.buildPage == true){
	 						buildPage(data);
	 					}
	 					//执行回调
	 					cfg.after();
	 					$('#my-modal-loading').modal('close');
	 					Layout.windowResize();
		      	    },
		      	    error : function(xhr, errorType, error) {
		      	    	cfg.tmp_start = true;
		      	    	$('#my-modal-loading').modal('close');
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
			
			this.load = function(){
				$('#'+cfg.from).find('input[name=start]').val(cfg.start);
				$('#'+cfg.from).find('input[name=limit]').val(cfg.limit);
				$('#'+cfg.from).submit();
			}
		}
	};

	return app;
});