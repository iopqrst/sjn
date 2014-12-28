/**
 * 验证用户账号信息（注册信息等）
 */
define(function(require, exports, module) {

	var $ = require('jquery');
	var x = require('./xbase');

	// 判断验证码是否正确
	exports.vcode = function() {
		
	};
	
	var $mobile = $(":input[name='mobile']");
	var $password = $(":input[name='password']");
	var $vcode = $(":input[name='vcode']");
	// 登录验证
	exports.v = function(page) {
		
		var validateFn = {
			onFocus : {
				run : function ($obj, msg) {
					var vstate = $obj.data('v_state');
					if(vstate) return;
					if(msg) {
						validateFn.createTips($obj , msg);
					}
				}
			},
			error : {
				run : function ($obj, msg) {
					$obj.data('v_state', 1);
					if(msg) {
						validateFn.createTips($obj , msg);
					}
				}
			},
			succeed : {
				run : function ($obj, msg) {
					$obj.data('v_state', 2);
				}
			},
			destroy : function($obj){
			},
			formSubmit : function ($eles) {
		        var bool = true;
		        for (var i = 0; i < $eles.length; i++) {
		            if ($eles[i].data("v_state") == 2) {
		                bool = true;
		            } else {
		                bool = false;
		                console.info($eles[i]);
		                $eles[i].trigger('blur');
		                //break;
		            }
		        }
		        return bool;
		    },
		    createTips: function($obj, msg) {
		    	var $parent = $obj.parent();
		    	if($obj.attr("name") == "vcode") {
		    		if($parent.parent().find('.base_v_msg_tip').length < 1) {
		    			$parent.parent().append('<span class="base_v_msg_tip vcode">'+ msg +'</span>');
		    		}
		    	} else {
		    		if($parent.find('.base_v_msg_tip').length < 1) {
		    			$parent.append('<span class="base_v_msg_tip">'+ msg +'</span>');
		    		}
		    	}
		    },
		    destoryTips: function($obj) {
		    	console.info($obj.parent());
		    	$obj.parent().find(".base_v_msg_tip").remove();
		    	$obj.parent().parent().find(".base_v_msg_tip").remove();
		    },
		    sendVcode: function () {
		    	
		    	if(x.isEmpty($mobile.val()) || !x.gisMobile($mobile.val())) {
					validateFn.error.run($mobile, validMsg.mobile.error);
					return;
				} 
		    	
		    	$.ajax({
					type : 'post',
					url : '/reg/sendVcode',
					dataType : 'json',
					data : {'mobile': $mobile.val().trim()},
					success : function(result, status) {
						console.info(result);
						if(result) { //code=1 成功
							alert(result.msg);
						} else {
							alert('验证码发送失败，请稍后再试');
						}
					},
					error : function() {
						$.unblockUI();
					}
				});
		    }
		};
		
		var validMsg = {
			mobile : {
				empty : '请输入11位手机号码',
				error : '手机号码格式不正确',
				exist : '手机号已注册，请直接登录'
			},
			pwd : {
				empty : '6-20位字符，可使用数字字母符号组合',
				error : '密码长度应在6-20位字符之间',
			},
			code : {
				empty : '请输入短信验证码',
				error : '短信验证码不正确'
			}
		};

		$.fn.extend({
			
			vmobile : function(){
				
				var vstate = $mobile.data('v_state');
				
				var primaryMobile = $mobile.data('primaryData');
				if(vstate && primaryMobile == $mobile.val()) { //如果两个值相等说明之前做过校验，所以这次就不在校验了
					return;
				}
				
				if(x.isEmpty(this.val())) {
					validateFn.error.run(this, validMsg.mobile.empty);
					//validateFn.destroy(this);
				} else if(!x.gisMobile(this.val())) {
					validateFn.error.run(this, validMsg.mobile.error);
				} else {
					$mobile.data('primaryData', $mobile.val());

					if('reg' === page) {
						_uniqueMobile();
					} else {
						validateFn.succeed.run($mobile);	
					}
				}
			},
			vpassword : function() {
				var vstate = $password.data('v_state');
				
				var primaryPwd = $password.data('primaryData');
				if(vstate && primaryPwd == $password.val()) { //如果两个值相等说明之前做过校验，所以这次就不在校验了
					return;
				}
				
				if(x.isEmpty(this.val())) {
					validateFn.error.run(this, validMsg.pwd.empty);
				} else if(!x.gisPwd(this.val())) {
					validateFn.error.run(this, validMsg.pwd.error);
				} else {
					$password.data('primaryData', $password.val());
					validateFn.succeed.run($password);
				}
			},
			vcode : function() {
				var vstate = $vcode.data('v_state');
				
				var primaryCode = $vcode.data('primaryData');
				if(vstate && primaryCode == $vcode.val()) { //如果两个值相等说明之前做过校验，所以这次就不在校验了
					return;
				}
				
				if(x.isEmpty(this.val())) {
					validateFn.error.run(this, validMsg.code.empty);
					//validateFn.destroy(this);
				} else if(1120648 != parseInt(this.val(), 16)) {
					validateFn.error.run(this, validMsg.code.error);
				} else {
					$vcode.data('primaryData', $vcode.val());
					validateFn.succeed.run($vcode);
				} 
			}
			
		});
		
		var _uniqueMobile = function() {
			var params = {
				'mobile' : $mobile.val(),
				'ssid' : new Date().getTime()
			};
			
			$.ajax( {
				url : '/login/vmobile/stoken-s000',
				type : 'post',
				data : params,
				dataType : 'json',
				success : function(result) {
					
					if(result) {
						if(1 == result.status) {// 不可用
							validateFn.error.run($mobile, validMsg.mobile.exist);
						} else if(2 == result.status) {// 可用
							validateFn.succeed.run($mobile);
						}
						$mobile.data('v_state', result.status);
					};
					
				},
				error : function() {
				}
			});
		};

		$mobile.on('focus', function(){
			validateFn.onFocus.run($mobile, validMsg.mobile.empty);
		});
		
		$mobile.on('blur', function(){
			$mobile.vmobile();
		});
		
		$mobile.on('keyup', function(){
			validateFn.destoryTips($mobile);
		});

		$password.on('focus', function(){
			validateFn.onFocus.run($password, validMsg.pwd.empty)
		});
		
		$password.on('blur', function(){
			$password.vpassword();
		});
		
		$password.on('keyup', function(){
			validateFn.destoryTips($password);
		});
		
		$vcode.on('focus', function(){
			validateFn.onFocus.run($vcode, validMsg.code.empty);
		});

		$vcode.on('blur', function(){
			$vcode.vcode();
		});
		
		$vcode.on('keyup', function(){
			validateFn.destoryTips($vcode);
		});
		
		$("#S_get_vcode").on('click', validateFn.sendVcode);
		
		$("#S_page_login").on('click', function(){
			if(validateFn.formSubmit([$mobile, $password])) {
				document.forms[0].submit();
			}
		});
		
		$("#S_reg_page").on('click', function(){
			if(validateFn.formSubmit([$mobile, $vcode, $password])) {
				document.forms[0].submit();
			}
		});

		return;
	};

});