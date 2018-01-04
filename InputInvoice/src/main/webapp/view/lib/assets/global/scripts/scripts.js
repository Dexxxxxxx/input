jQuery(document)
		.ready(
				function() {
					$('#randCodeImage').click(function(){
						this.src = 'randCodeImage?t=' + new Date().getTime();
					});
//					$.backstretch("view/lib/assets/global/img/1.jpg");
					$(
							'.login-form input[type="text"], .login-form input[type="password"], .login-form textarea')
							.on("focus", function() {
								$(this).removeClass("input-error")
							});
					$(".login-form").on("submit", function(c) {
						c.preventDefault();
						var a = true;
						var d = $(this).find('input[name="username"]').eq(0);
						var b = $(this).find('input[name="password"]').eq(0);
						var c = $(this).find('input[name="randCode"]').eq(0);
						if (d.val() == "") {
							a = false;
							d.addClass("input-error")
						} else {
							d.removeClass("input-error")
						}
						if (b.val() == "") {
							a = false;
							b.addClass("input-error");
						} else {
							b.removeClass("input-error");
						}
						if (c.val() == "") {
							a = false;
							c.addClass("input-error");
						} else {
							c.removeClass("input-error");
						}
						
						if (a == false) {
							return
						}
						$("#login-with").html("正在验证中.......");
						$(this).ajaxSubmit({
							success : function(e) {
								$('#randCodeImage').attr("src", 'randCodeImage?t=' + new Date().getTime());
								if (e.result == "SUCCESS") {
									if ("" != e.rows["user"]["ID"]) {
										$("#login-with").html("验证通过,正在进入系统");
										window.location.href = "index.html";
									}
								} else {
									$("#login-with").html(e.msg)
								}
							},
							error : function(e) {
								$('#randCodeImage').attr("src", 'randCodeImage?t=' + new Date().getTime());
								$("#login-with").html(e)
							}
						})
					})
				});