define(function(require, exports, module) {
	
	var $ = require('jquery');
			require('bootstrap');
			require('blockUI');
	var bb = require('bootbox');
	
    //TODO FIXED IE10DEBUG
    (function() {
		'use strict';
		if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
			var msViewportStyle = document.createElement('style')
			msViewportStyle.appendChild(document
					.createTextNode('@-ms-viewport{width:auto!important}'))
			document.querySelector('head').appendChild(msViewportStyle)
		}
	})();
    
    //console.info(navigator.userAgent.toLowerCase());
    
    (function(){
    	//http://v3.bootcss.com/getting-started/#support-android-stock-browser
//    	$(function () {
//			var nua = navigator.userAgent
//			var isAndroid = (nua.indexOf('Mozilla/5.0') > -1 && nua.indexOf('Android ') > -1 
//					&& nua.indexOf('AppleWebKit') > -1 && nua.indexOf('Chrome') === -1)
//			if (isAndroid) {
//				$('select.form-control').removeClass('form-control').css('width', '100%')
//			}
//		});
    })();
    
	var fn = {
		isEmpty: function(str){
			str = str.replace(/^\s+/, '').replace(/\s+$/, '');
			if (str.length == 0 || null == str) {
				return true;
			} else {
				return false;
			}
		},
		gisMobile: function(str) {
			return /^0?(13|15|17|18|14)[0-9]{9}$/.test(str);
		},
		gisPwd : function(str) {
			 return /^[\W_a-zA-z0-9-.]{6,20}$/i.test(str);
		},
		browser : function(){
	    	var userAgent = navigator.userAgent.toLowerCase(), s, o = {};  
		    return {
		        version:(userAgent.match(/(?:firefox|opera|safari|chrome|msie)[\/: ]([\d.]+)/))[1],
		        safari:/version.+safari/.test(userAgent),
		        chrome:/chrome/.test(userAgent),
		        firefox:/firefox/.test(userAgent),
		        ie:/msie/.test(userAgent),
		        opera: /opera/.test(userAgent )
		    } /* 获得浏览器的名称及版本信息 */
		    return b;
	    },
	    gisSmartPhone : function() { //是否是智能设备
	    	return /Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent);
	    },
	    gTimeline : function() {//设置时间轴，因为将xbase加载放在了最后，所以你懂得
	    	
	    	if(!fn.ggetCookie('stoken')) return;
	    	
	    	require('select');
	    	$("#S_timeline_setting").on('click', openSettingTimelineDialog);
	    	$("#S_pop_timeline_setting").on('click', settingTimeline);
	    	
	    	//防止js没有加载完成，无法绑定事件 (这里如果绑定后其实全局所有的页面就不用再绑定了，前提条件是类必须为selectpicker）
	    	setTimeout(function(){
	    		//如果是在手机上则让下拉里列表按照手机的方式显示
	    		if(/Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent) ) {
				    $('.selectpicker').selectpicker('mobile');
				} else {
					$('.selectpicker').selectpicker();
				}
	    	},10);

    		/**
    		 * @function 打开设置时间轴范围的对话框
    		 */
    		function openSettingTimelineDialog() {
    			
    			$.blockUI();
    			$.ajax({
    				url:'/win/timeline?t=' + new Date().getTime(),
    				dataType:'json',
    				success:function(result){
    					if(result && result.beginPolt) {
    						$("#S_timeline_setting_dialog").modal('show'); //有数据才打开，否则出现闪屏
    						$(":input[name='iloveyou']").val(result.id);
    						
    						$(":input[name='sbeginPolt']").selectpicker('val', result.beginPolt);
    						$(":input[name='sendPolt']").selectpicker('val', result.endPolt);
    					} else {}
    					
    					$.unblockUI();
    				},
    				error:function(){
    					$.unblockUI();
    				}
    			});
    		}
    		
    		/**
    		 * 保存时间轴修改
    		 */
    		function settingTimeline() {
    			var bp = $(":input[name='sbeginPolt']").val();
    			var ep = $(":input[name='sendPolt']").val();
    			var what = $(":input[name='iloveyou']").val();
    			if('' == bp || '' == ep) {
    				return;
    			}
    			
    			$.blockUI();
    			$.ajax({
    				type:'post',
    				url:'/win/stimeline',
    				dataType:'json',
    				data: {beginPolt: bp, endPolt:ep, id: what, tid: new Date().getTime()},
    				success:function(result){
    					if('succ' == result.status) {
    						bb.alert("时间轴范围设置成功");
    					} else {
    						bb.alert('设置失败，请重新设置');					
    					}
    					$.unblockUI();
    					
    					$("#S_timeline_setting_dialog").modal('hide');
    				},
    				error:function(){
    					$.unblockUI();
    				}
    			});
    		}
	    },
	    gcategory: function() {
	    	if(!fn.ggetCookie('stoken')) return;
	    	$("#S_category_setting").on('click',function(){
	    		location['href'] = '/cat';
	    	});
	    },
	    glifeList: function() {
	    	if(!fn.ggetCookie('stoken')) return;
	    	$("#S_life_list").on('click', function(){
	    		location['href'] = '/life';
	    	});
	    },
	    gajax: function() {
	    	$.ajaxSetup({
	    	    contentType:"application/x-www-form-urlencoded;charset=utf-8",
	    	    complete:function(XMLHttpRequest,textStatus){
	    	          //通过XMLHttpRequest取得响应头，sessionstatus           
	    	    	var status = XMLHttpRequest.getResponseHeader("login_status"); 
	    	    	if(status === "timeout"){
	    	    		window.location['href'] = '/login';
	    	    	}
	    	    }
	    	});
	    },
	    greg: function() {
	    	//if(!fn.ggetCookie('stoken')) return;
	    	$("#S_nav_reg").on('click', function(){
	    		window.location['href'] = '/reg';
	    	});
	    },
	    ggetCookie: function(key) {
	    	var cookie = document.cookie;  
	        var cookieArray = cookie.split(';');  
	        var val = "";  
	        for (var i = 0; i < cookieArray.length; i++) {  
	            if ($.trim(cookieArray[i]).substr(0, key.length) == key) {  
	                val = $.trim(cookieArray[i]).substr(key.length + 1);  
	                break;  
	            }  
	        }  
	        return unescape(val);  
	    },//有问题
	    g_resizePage : function(){
	    	 var s = $(document).height();
	    	 console.info('----->' + (s));
	         $(".footer").css({'marginTop': (s - 60)});
	    },
	    g_13submit : function (fun){
        	var $inp = $('input'); //所有的input元素
        	$inp.keypress(function (event) {
        	    var key = event.which;
        	    if (key == 13) {
        	        fun();
        	    }
        	});
        }
	    
	};
	
	//初始页面共有的方法
	(function(){
		fn.gajax();
		fn.glifeList();
		fn.gcategory();
		fn.gTimeline();
		fn.greg();
		//fn.g_resizePage();
		//$(window).on('resize', fn.g_resizePage);
	})();
	
	module.exports = fn;
});