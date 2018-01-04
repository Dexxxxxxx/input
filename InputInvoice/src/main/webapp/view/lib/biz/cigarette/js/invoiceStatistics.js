/**
 * 发票汇总表
 */
define([ 'jquery', 'app', 'ztree', 'jqueryForm','My97DatePicker','text!biz/summaryReport/summaryReport.html' ], function($, app, ztree, jqueryForm,My97DatePicker, tpl) {
	//加载汇总报表数据
	var loadData = function(){
		new app.Page({
			url : app.rootPath + '/summaryReport/list.do',
			from: 'search-form',
			dataTarget: 'data-tbody',
			before: function(){
				$('th[name="slOne"]').hide();
				$('.tdOne').hide();
				
				$('th[name="slTwo"]').hide();
				$('.tdTwo').hide();
				
				$('th[name="slThree"]').hide();
				$('.tdThree').hide();
				
				$('th[name="slFour"]').hide();
				$('.tdFour').hide();
				
				$('th[name="slFive"]').hide();	
				$('.tdFive').hide();
				
				$('th[name="slSix"]').hide();	
				$('.tdSix').hide();
				
				$('th[name="slSeven"]').hide();	
				$('.tdSeven').hide();
				
				$('th[name="slEight"]').hide();	
				$('.tdEight').hide();
				
				$('th[name="slNine"]').hide();	
				$('.tdNine').hide();
			},
			//请求后执行方法
			after : function(){
				//点击复选框隐藏与显示相关税率的信息
				$(function(){
					//点击显示
					$("#query").bind("click",function(){
						if($('input[name="check"]').prop("checked")){
							$('th[name="slOne"]').show();
							$('.tdOne').show();
						}else{
							$('th[name="slOne"]').hide();
							$('.tdOne').hide();
						}
					});
					
					$("#queryOne").bind("click",function(){
						if($('input[name="checkOne"]').prop("checked")){
							$('th[name="slTwo"]').show();	
							$('.tdTwo').show();
						}else{
							$('th[name="slTwo"]').hide();
							$('.tdTwo').hide();
						}
					});
					
					$("#queryTwo").bind("click",function(){
						if($('input[name="checkTwo"]').prop("checked")){
							$('th[name="slThree"]').show();	
							$('.tdThree').show();
						}else{
							$('th[name="slThree"]').hide();
							$('.tdThree').hide();
						}
					});
					
					$("#queryThree").bind("click",function(){
						if($('input[name="checkThree"]').prop("checked")){
							$('th[name="slFour"]').show();	
							$('.tdFour').show();
						}else{
							$('th[name="slFour"]').hide();
							$('.tdFour').hide();
						}
					});
					
					$("#queryFour").bind("click",function(){
						if($('input[name="checkFour"]').prop("checked")){
								$('th[name="slFive"]').show();	
								$('.tdFive').show();
						}else{
								$('th[name="slFive"]').hide();	
								$('.tdFive').hide();
						}
					});
					
					$("#queryFive").bind("click",function(){
						if($('input[name="checkFive"]').prop("checked")){
							$('th[name="slSix"]').show();	
							$('.tdSix').show();
						}else{
							$('th[name="slSix"]').hide();	
							$('.tdSix').hide();
						}
					});
					
					$("#querySix").bind("click",function(){
						if($('input[name="checkSix"]').prop("checked")){
							$('th[name="slSeven"]').show();	
							$('.tdSeven').show();
						}else{
							$('th[name="slSeven"]').hide();	
							$('.tdSeven').hide();
						}
					});
					
					$("#querySeven").bind("click",function(){
						if($('input[name="checkSeven"]').prop("checked")){
							$('th[name="slEight"]').show();	
							$('.tdEight').show();
						}else{
							$('th[name="slEight"]').hide();	
							$('.tdEight').hide();
						}
					});
					
					$("#queryEight").bind("click",function(){
						if($('input[name="checkEight"]').prop("checked")){
							$('th[name="slNine"]').show();	
							$('.tdNine').show();
						}else{
							$('th[name="slNine"]').hide();	
							$('.tdNine').hide();
						}
					});
					//实际金额与税额
					//正数金额
					var zzsjg = $("#trOne td:eq(2)").html();
					var jgzsSl17 = $("#trOne td:eq(3)").html();
					var jgzsSl13 = $("#trOne td:eq(4)").html();
					var jgzsSl11 = $("#trOne td:eq(5)").html();
					var jgzsSl06 = $("#trOne td:eq(6)").html();
					var jgzsSl05 = $("#trOne td:eq(7)").html();
					var jgzsSl04 = $("#trOne td:eq(8)").html();
					var jgzsSl03 = $("#trOne td:eq(9)").html();
					var jgzsSl015 = $("#trOne td:eq(10)").html();
					var jgzsSl0 = $("#trOne td:eq(11)").html();
					//负数金额
					var zfsjg = $("#trThree td:eq(2)").html();
					var jgfsSl17 = $("#trThree td:eq(3)").html();
					var jgfsSl13 = $("#trThree td:eq(4)").html();
					var jgfsSl11 = $("#trThree td:eq(5)").html();
					var jgfsSl06 = $("#trThree td:eq(6)").html();
					var jgfsSl05 = $("#trThree td:eq(7)").html();
					var jgfsSl04 = $("#trThree td:eq(8)").html();
					var jgfsSl03 = $("#trThree td:eq(9)").html();
					var jgfsSl015 = $("#trThree td:eq(10)").html();
					var jgfsSl0 = $("#trThree td:eq(11)").html();
					//实际正数金额
					var sjzje = Number(zzsjg)-Number(-zfsjg);
					var sjzje1 = sjzje.toFixed(2);
					var sjje17 = Number(jgzsSl17)-Number(-jgfsSl17);
					var sjje13 = Number(jgzsSl13)-Number(-jgfsSl13);
					var sjje11 = Number(jgzsSl11)-Number(-jgfsSl11);
					var sjje106 = Number(jgzsSl06)-Number(-jgfsSl06);
					var sjje05 = Number(jgzsSl05)-Number(-jgfsSl05);
				    var sjje04 = Number(jgzsSl04)-Number(-jgfsSl04);
				    var sjje03 = Number(jgzsSl03)-Number(-jgfsSl03);
				    var sjje015 = Number(jgzsSl015)-Number(-jgfsSl015);
				    var sjje0 = Number(jgzsSl0)-Number(-jgfsSl0);
				    $("#trFive td:eq(2)").html(sjzje1);
					$("#trFive td:eq(3)").html(sjje17);
					$("#trFive td:eq(4)").html(sjje13);
					$("#trFive td:eq(5)").html(sjje11);
					$("#trFive td:eq(6)").html(sjje106);
					$("#trFive td:eq(7)").html(sjje05);
					$("#trFive td:eq(8)").html(sjje04);
					$("#trFive td:eq(9)").html(sjje03);
					$("#trFive td:eq(10)").html(sjje015);
					$("#trFive td:eq(11)").html(sjje0);
				    //正数税额
					var zzssejg = $("#trSix td:eq(2)").html();
					var jgzsse17 = $("#trSix td:eq(3)").html();
					var jgzsse13 = $("#trSix td:eq(4)").html();
					var jgzsse11 = $("#trSix td:eq(5)").html();
					var jgzsse06 = $("#trSix td:eq(6)").html();
					var jgzsse05 = $("#trSix td:eq(7)").html();
					var jgzsse04 = $("#trSix td:eq(8)").html();
					var jgzsse03 = $("#trSix td:eq(9)").html();
					var jgzsse015 = $("#trSix td:eq(10)").html();
					var jgzsse0 = $("#trSix td:eq(11)").html();
					//负数税额
					var zfssejg = $("#trEight td:eq(2)").html();
					var jgfsse17 = $("#trEight td:eq(3)").html();
					var jgfsse13 = $("#trEight td:eq(4)").html();
					var jgfsse11 = $("#trEight td:eq(5)").html();
					var jgfsse06 = $("#trEight td:eq(6)").html();
					var jgfsse05 = $("#trEight td:eq(7)").html();
					var jgfsse04 = $("#trEight td:eq(8)").html();
					var jgfsse03 = $("#trEight td:eq(9)").html();
					var jgfsse015 = $("#trEight td:eq(10)").html();
					var jgfsse0 = $("#trEight td:eq(11)").html();
					//计算实际税额
				    var sjzse = Number(zzssejg)-Number(-zfssejg);
				    var sjzse1 = sjzse.toFixed(2);
				    var sjse17 = Number(jgzsse17)-Number(-jgfsse17);
					var sjse13 = Number(jgzsse13)-Number(-jgfsse13);
					var sjse11 = Number(jgzsse11)-Number(-jgfsse11);
					var sjse106 = Number(jgzsse06)-Number(-jgfsse06);
					var sjse05 = Number(jgzsse05)-Number(-jgfsse05);
				    var sjse04 = Number(jgzsse04)-Number(-jgfsse04);
				    var sjse03 = Number(jgzsse03)-Number(-jgfsse03);
				    var sjse015 = Number(jgzsse015)-Number(-jgfsse015);
				    var sjse0 = Number(jgzsse0)-Number(-jgfsse0);
				    $("#trTen td:eq(2)").html(sjzse1);
					$("#trTen td:eq(3)").html(sjse17);
					$("#trTen td:eq(4)").html(sjse13);
					$("#trTen td:eq(5)").html(sjse11);
					$("#trTen td:eq(6)").html(sjse106);
					$("#trTen td:eq(7)").html(sjse05);
					$("#trTen td:eq(8)").html(sjse04);
					$("#trTen td:eq(9)").html(sjse03);
					$("#trTen td:eq(10)").html(sjse015);
					$("#trTen td:eq(11)").html(sjse0);
				});
			},
			//取值和页面拼接方法
			buildData : function(result){
				var c = 1 ;
					$.each(result,function(i,item){
						var xmmc = item['XMMC'];
						//动态拼接页面
						if(xmmc != '正数发票份数' && xmmc != '正数废票份数' && xmmc != '负数发票份数' &&xmmc != '负数废票份数'){
							if(xmmc == '销项正数金额'){
								var jgzsSl17 = item['JG1']==null?'0':item['JG1'];
								var jgzsSl13 = item['JG2']==null?'0':item['JG2'];
								var jgzsSl11 = item['JG3']==null?'0':item['JG3'];
								var jgzsSl06 = item['JG4']==null?'0':item['JG4'];
								var jgzsSl05 = item['JG5']==null?'0':item['JG5'];
								var jgzsSl04 = item['JG6']==null?'0':item['JG6'];
								var jgzsSl03 = item['JG7']==null?'0':item['JG7'];
								var jgzsSl015 = item['JG8']==null?'0':item['JG8'];
								var jgzsSl0 = item['JG9']==null?'0':item['JG9'];
								//计算总数
								var zzsjg = Number(jgzsSl17)+Number(jgzsSl13)+Number(jgzsSl11)+Number(jgzsSl06)+Number(jgzsSl05)+Number(jgzsSl04)+Number(jgzsSl03)+Number(jgzsSl015)+Number(jgzsSl0);
								var zzsjg1 = zzsjg.toFixed(2);
								$("#trOne td:eq(2)").html(zzsjg1);
								$("#trOne td:eq(3)").html(jgzsSl17);
								$("#trOne td:eq(4)").html(jgzsSl13);
								$("#trOne td:eq(5)").html(jgzsSl11);
								$("#trOne td:eq(6)").html(jgzsSl06);
								$("#trOne td:eq(7)").html(jgzsSl05);
								$("#trOne td:eq(8)").html(jgzsSl04);
								$("#trOne td:eq(9)").html(jgzsSl03);
								$("#trOne td:eq(10)").html(jgzsSl015);
								$("#trOne td:eq(11)").html(jgzsSl0);
							}
							if(xmmc == '销项正废金额'){
								var jgSl17 = item['JG1']==null?'0':item['JG1'];
								var jgSl13 = item['JG2']==null?'0':item['JG2'];
								var jgSl11 = item['JG3']==null?'0':item['JG3'];
								var jgSl06 = item['JG4']==null?'0':item['JG4'];
								var jgSl05 = item['JG5']==null?'0':item['JG5'];
								var jgSl04 = item['JG6']==null?'0':item['JG6'];
								var jgSl03 = item['JG7']==null?'0':item['JG7'];
								var jgSl015 = item['JG8']==null?'0':item['JG8'];
								var jgSl0 = item['JG9']==null?'0':item['JG9'];
								//计算总数
								var zjg = Number(jgSl17)+Number(jgSl13)+Number(jgSl11)+Number(jgSl06)+Number(jgSl05)+Number(jgSl04)+Number(jgSl03)+Number(jgSl015)+Number(jgSl0);
								var zjg1 = zjg.toFixed(2);
								$("#trTwo td:eq(2)").html(zjg1);
								$("#trTwo td:eq(3)").html(jgSl17);
								$("#trTwo td:eq(4)").html(jgSl13);
								$("#trTwo td:eq(5)").html(jgSl11);
								$("#trTwo td:eq(6)").html(jgSl06);
								$("#trTwo td:eq(7)").html(jgSl05);
								$("#trTwo td:eq(8)").html(jgSl04);
								$("#trTwo td:eq(9)").html(jgSl03);
								$("#trTwo td:eq(10)").html(jgSl015);
								$("#trTwo td:eq(11)").html(jgSl0);
							}
							if(xmmc == '销项负数金额'){
								var jgfsSl17 = item['JG1']==null?'0':item['JG1'];
								var jgfsSl13 = item['JG2']==null?'0':item['JG2'];
								var jgfsSl11 = item['JG3']==null?'0':item['JG3'];
								var jgfsSl06 = item['JG4']==null?'0':item['JG4'];
								var jgfsSl05 = item['JG5']==null?'0':item['JG5'];
								var jgfsSl04 = item['JG6']==null?'0':item['JG6'];
								var jgfsSl03 = item['JG7']==null?'0':item['JG7'];
								var jgfsSl015 = item['JG8']==null?'0':item['JG8'];
								var jgfsSl0 = item['JG9']==null?'0':item['JG9'];
								//计算总数
								var zfsjg = Number(jgfsSl17)+Number(jgfsSl13)+Number(jgfsSl11)+Number(jgfsSl06)+Number(jgfsSl05)+Number(jgfsSl04)+Number(jgfsSl03)+Number(jgfsSl015)+Number(jgfsSl0);
								var zfsjg1 = zfsjg.toFixed(2);
								$("#trThree td:eq(2)").html(zfsjg1);
								$("#trThree td:eq(3)").html(jgfsSl17);
								$("#trThree td:eq(4)").html(jgfsSl13);
								$("#trThree td:eq(5)").html(jgfsSl11);
								$("#trThree td:eq(6)").html(jgfsSl06);
								$("#trThree td:eq(7)").html(jgfsSl05);
								$("#trThree td:eq(8)").html(jgfsSl04);
								$("#trThree td:eq(9)").html(jgfsSl03);
								$("#trThree td:eq(10)").html(jgfsSl015);
								$("#trThree td:eq(11)").html(jgfsSl0);
							}
							if(xmmc == '销项负废金额'){
								var jgSl17 = item['JG1']==null?'0':item['JG1'];
								var jgSl13 = item['JG2']==null?'0':item['JG2'];
								var jgSl11 = item['JG3']==null?'0':item['JG3'];
								var jgSl06 = item['JG4']==null?'0':item['JG4'];
								var jgSl05 = item['JG5']==null?'0':item['JG5'];
								var jgSl04 = item['JG6']==null?'0':item['JG6'];
								var jgSl03 = item['JG7']==null?'0':item['JG7'];
								var jgSl015 = item['JG8']==null?'0':item['JG8'];
								var jgSl0 = item['JG9']==null?'0':item['JG9'];
								//计算总数
								var zjg = Number(jgSl17)+Number(jgSl13)+Number(jgSl11)+Number(jgSl06)+Number(jgSl05)+Number(jgSl04)+Number(jgSl03)+Number(jgSl015)+Number(jgSl0);
								var zjg1 = zjg.toFixed(2);
								$("#trFour td:eq(2)").html(zjg1);
								$("#trFour td:eq(3)").html(jgSl17);
								$("#trFour td:eq(4)").html(jgSl13);
								$("#trFour td:eq(5)").html(jgSl11);
								$("#trFour td:eq(6)").html(jgSl06);
								$("#trFour td:eq(7)").html(jgSl05);
								$("#trFour td:eq(8)").html(jgSl04);
								$("#trFour td:eq(9)").html(jgSl03);
								$("#trFour td:eq(10)").html(jgSl015);
								$("#trFour td:eq(11)").html(jgSl0);
							}
							if(xmmc == '销项正数税额'){
								var jgzsse17 = item['JG1']==null?'0':item['JG1'];
								var jgzsse13 = item['JG2']==null?'0':item['JG2'];
								var jgzsse11 = item['JG3']==null?'0':item['JG3'];
								var jgzsse06 = item['JG4']==null?'0':item['JG4'];
								var jgzsse05 = item['JG5']==null?'0':item['JG5'];
								var jgzsse04 = item['JG6']==null?'0':item['JG6'];
								var jgzsse03 = item['JG7']==null?'0':item['JG7'];
								var jgzsse015 = item['JG8']==null?'0':item['JG8'];
								var jgzsse0 = item['JG9']==null?'0':item['JG9'];
								//计算总数
								var zzssejg = Number(jgzsse17)+Number(jgzsse13)+Number(jgzsse11)+Number(jgzsse06)+Number(jgzsse05)+Number(jgzsse04)+Number(jgzsse03)+Number(jgzsse015)+Number(jgzsse0);
								var zzssejg1 = zzssejg.toFixed(2);
								$("#trSix td:eq(2)").html(zzssejg1);
								$("#trSix td:eq(3)").html(jgzsse17);
								$("#trSix td:eq(4)").html(jgzsse13);
								$("#trSix td:eq(5)").html(jgzsse11);
								$("#trSix td:eq(6)").html(jgzsse06);
								$("#trSix td:eq(7)").html(jgzsse05);
								$("#trSix td:eq(8)").html(jgzsse04);
								$("#trSix td:eq(9)").html(jgzsse03);
								$("#trSix td:eq(10)").html(jgzsse015);
								$("#trSix td:eq(11)").html(jgzsse0);
							}
							if(xmmc == '销项正废税额'){
								var jgSl17 = item['JG1']==null?'0':item['JG1'];
								var jgSl13 = item['JG2']==null?'0':item['JG2'];
								var jgSl11 = item['JG3']==null?'0':item['JG3'];
								var jgSl06 = item['JG4']==null?'0':item['JG4'];
								var jgSl05 = item['JG5']==null?'0':item['JG5'];
								var jgSl04 = item['JG6']==null?'0':item['JG6'];
								var jgSl03 = item['JG7']==null?'0':item['JG7'];
								var jgSl015 = item['JG8']==null?'0':item['JG8'];
								var jgSl0 = item['JG9']==null?'0':item['JG9'];
								//计算总数
								var zjg = Number(jgSl17)+Number(jgSl13)+Number(jgSl11)+Number(jgSl06)+Number(jgSl05)+Number(jgSl04)+Number(jgSl03)+Number(jgSl015)+Number(jgSl0);
								var zjg1 = zjg.toFixed(2);
								$("#trSeven td:eq(2)").html(zjg1);
								$("#trSeven td:eq(3)").html(jgSl17);
								$("#trSeven td:eq(4)").html(jgSl13);
								$("#trSeven td:eq(5)").html(jgSl11);
								$("#trSeven td:eq(6)").html(jgSl06);
								$("#trSeven td:eq(7)").html(jgSl05);
								$("#trSeven td:eq(8)").html(jgSl04);
								$("#trSeven td:eq(9)").html(jgSl03);
								$("#trSeven td:eq(10)").html(jgSl015);
								$("#trSeven td:eq(11)").html(jgSl0);
							}
							if(xmmc == '销项负数税额'){
								var jgfsse17 = item['JG1']==null?'0':item['JG1'];
								var jgfsse13 = item['JG2']==null?'0':item['JG2'];
								var jgfsse11 = item['JG3']==null?'0':item['JG3'];
								var jgfsse06 = item['JG4']==null?'0':item['JG4'];
								var jgfsse05 = item['JG5']==null?'0':item['JG5'];
								var jgfsse04 = item['JG6']==null?'0':item['JG6'];
								var jgfsse03 = item['JG7']==null?'0':item['JG7'];
								var jgfsse015 = item['JG8']==null?'0':item['JG8'];
								var jgfsse0 = item['JG9']==null?'0':item['JG9'];
								//计算总数
								var zfssejg = Number(jgfsse17)+Number(jgfsse13)+Number(jgfsse11)+Number(jgfsse06)+Number(jgfsse05)+Number(jgfsse04)+Number(jgfsse03)+Number(jgfsse015)+Number(jgfsse0);
								var zfssejg1 = zfssejg.toFixed(2);
								$("#trEight td:eq(2)").html(zfssejg1);
								$("#trEight td:eq(3)").html(jgfsse17);
								$("#trEight td:eq(4)").html(jgfsse13);
								$("#trEight td:eq(5)").html(jgfsse11);
								$("#trEight td:eq(6)").html(jgfsse06);
								$("#trEight td:eq(7)").html(jgfsse05);
								$("#trEight td:eq(8)").html(jgfsse04);
								$("#trEight td:eq(9)").html(jgfsse03);
								$("#trEight td:eq(10)").html(jgfsse015);
								$("#trEight td:eq(11)").html(jgfsse0);
							}
							if(xmmc == '销项负废税额'){
								var jgSl17 = item['JG1']==null?'0':item['JG1'];
								var jgSl13 = item['JG2']==null?'0':item['JG2'];
								var jgSl11 = item['JG3']==null?'0':item['JG3'];
								var jgSl06 = item['JG4']==null?'0':item['JG4'];
								var jgSl05 = item['JG5']==null?'0':item['JG5'];
								var jgSl04 = item['JG6']==null?'0':item['JG6'];
								var jgSl03 = item['JG7']==null?'0':item['JG7'];
								var jgSl015 = item['JG8']==null?'0':item['JG8'];
								var jgSl0 = item['JG9']==null?'0':item['JG9'];
								//计算总数
								var zjg = Number(jgSl17)+Number(jgSl13)+Number(jgSl11)+Number(jgSl06)+Number(jgSl05)+Number(jgSl04)+Number(jgSl03)+Number(jgSl015)+Number(jgSl0);
								var zjg1 = zjg.toFixed(2);
								$("#trNine td:eq(2)").html(zjg1);
								$("#trNine td:eq(3)").html(jgSl17);
								$("#trNine td:eq(4)").html(jgSl13);
								$("#trNine td:eq(5)").html(jgSl11);
								$("#trNine td:eq(6)").html(jgSl06);
								$("#trNine td:eq(7)").html(jgSl05);
								$("#trNine td:eq(8)").html(jgSl04);
								$("#trNine td:eq(9)").html(jgSl03);
								$("#trNine td:eq(10)").html(jgSl015);
								$("#trNine td:eq(11)").html(jgSl0);
							}
						}
						//发票份数相关信息
							if(xmmc == '正数发票份数'){
								var zsfs = item['JG']==null?'0':item['JG'];
								$(".fs1").html("正数发票份数:"+zsfs)
							}
							if(xmmc == '正数废票份数'){
								var zsfpfs = item['JG']==null?'0':item['JG'];
								$(".fs2").html("正数废票份数:"+zsfpfs)
							}
							if(xmmc == '负数发票份数'){
								var fsfs = item['JG']==null?'0':item['JG'];
								$(".fs3").html("负数发票份数:"+fsfs)
							}
							if(xmmc == '负数废票份数'){
								var fsfpfs = item['JG']==null?'0':item['JG'];
								$(".fs4").html("负数废票份数:"+fsfpfs)
							}
					});
			}
		}).load();
	};
	var pageShow = function(){
		new app.Page({
			url : app.rootPath+ '/invoicemanager/invoice/listDisk.do',
			target : 'page-div-disk',
			from : 'search-form-disk',
			dataTarget : 'data-tbody-disk',
			buildData : function(data) {
				var html = '';
				$.each(data, function(i, item) {
					html += ''
						+'<tr>'
						+'  <td><div class="checker"><span><input type="checkbox"  class="checkboxes" value="'+item['DEV_ID']+'"></span></div></td>'
						+'	<td>'+item['COM_NAME']+'</td>'
						+'	<td>'+item['TAX_PAYER_ID']+'</td>'
						+'	<td>'+item['DEV_NAME']+'</td>'
						+'	<td>'+item['DEV_ID']+'</td>'
						+'</tr>';
				});
				return html;
			}
		}).load();
		$('#ajax-modal-choice').modal('show');
	}
	var buildEvent = function(){
		//显示盘号列表
		$('#jqbh').on('click', function() {
			$('#search_key').val("");
			$('#checkall').attr('checked',false);
			pageShow();
		});
		$('#checkall').on('click',function(){
			var isChecked = $(this).prop('checked');
			$('.checkboxes').prop('checked',isChecked);
		});
		//选择点击事件
		$('#confirm').on('click',function(){
			var ids = [];
			$('.checkboxes').each(function(){
				if($(this).is(':checked') == true){
					ids.push($(this).val());
				}
			});
			//将查出的数据放入框中
			if(ids.length==0){
				$('#jqbh').val("");
			}else{
				$('#jqbh').val(ids);
			}
		});
		//导出Excel
		$("#dc").click(function(){
			var input1 = $('<input>');
            input1.attr('type', 'hidden');
            input1.attr('name', 'start_date');
            input1.attr('value', $('#start_date').val());
            
            var input2 = $('<input>');
            input2.attr('type', 'hidden');
            input2.attr('name', 'end_date');
            input2.attr('value', $('#end_date').val());
            
            var input3 = $('<input>');
            input3.attr('type', 'hidden');
            input3.attr('name', 'fplx');
            input3.attr('value', $('#fplx option:selected').val());
            
			var form = $("<form>");
			form.attr('style', 'display:none');   //在form表单中添加查询参数
            form.attr('target', '');
            form.attr('method', 'post');
            form.attr('action', app.rootPath + '/invoicemanager/invoice/export.do?type=6');
            $('body').append(form);
            form.append(input1);   //将查询参数控件提交到表单上
            form.submit();
            form.remove();
		});
	};
	//主页面加载
	var invoice = {
			init : function() {//调用app.js的初始化方法
				app.mainContainer.html(tpl);
				buildEvent();
				loadData(); 
			}
		};
	return invoice;
});	