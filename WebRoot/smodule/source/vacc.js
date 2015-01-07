/**
 * 验证用户账号信息（注册信息等）
 */
define(function(require, exports, module) {

	var $ = require('jquery');
	var x = require('./xbase');
			require('storage');
	
	var $mobile = $(":input[name='mobile']");
	var $password = $(":input[name='password']");
	var $vcode = $(":input[name='vcode']");
	
	var VALID_STATE = {
		exception : -1,	//异常时
		init	  : 0,  //初始值
		fail	  : 1,	// 验证失败
		success	  : 2 	//验证通过
	};

	var validMsg = {
		mobile : {
			empty : '请输入11位手机号码',
			error : '手机号码格式不正确',
			exist : '手机号已注册，请直接登录'
		},
		pwd : {
			empty : '6-20位字符组合',
			error : '密码长度应在6-20位字符之间',
		},
		code : {
			empty : '请输入短信验证码',
			error : '验证码不正确',
			params_error : '手机号或者验证码不能为空'
		}
	};

	var validateFn = {
		onFocus : {
			run : function($obj, msg) {

				if($.trim($obj.val()).length != 0) {
					return; //不等于空就不要继续向下执行
				}

				var vstate = $obj.data('v_state');
				
				// 如果vstate存在值，则onFocus事件不会去修改它的值
				// 如果vstate不存在值，赋值初始值
				if (!vstate) { 
					$obj.data('vstate', VALID_STATE.init);
				}
				
				if (msg) {
					validateFn.createTips($obj, msg);
				}
			}
		},
		error : {
			run : function($obj, msg) {
				$obj.data('v_state', VALID_STATE.fail);
				if (msg) {
					validateFn.createTips($obj, msg);
				}
			}
		},
		succeed : {
			run : function($obj, msg) {
				$obj.data('v_state', VALID_STATE.success);
			}
		},
		createTips : function($obj, msg) { //
			var $parent = $obj.parent();
			if ($obj.attr("name") == "vcode") {
				if ($parent.parent().find('.base_v_msg_tip').length < 1) {
					$parent.parent().append(
							'<span class="base_v_msg_tip vcode">' + msg
									+ '</span>');
				}
			} else {
				if ($parent.find('.base_v_msg_tip').length < 1) {
					$parent.append('<span class="base_v_msg_tip">' + msg
							+ '</span>');
				}
			}
		},
		destoryTips : function($obj) {
			//console.info($obj.parent());
			$obj.parent().find(".base_v_msg_tip").remove();
			$obj.parent().parent().find(".base_v_msg_tip").remove();
		},
		uniqueMobile : function() { // 校验手机号的唯一性
			var params = {
				'mobile' : $mobile.val(),
				'ssid' : new Date().getTime()
			};

			$.ajax({
				url : '/login/vmobile/stoken-s000',
				type : 'post',
				data : params,
				dataType : 'json',
				success : function(result) {

					if (result) {
						if (1 == result.status) {// 不可用
							validateFn.error
									.run($mobile, validMsg.mobile.exist);
						} else if (2 == result.status) {// 可用
							validateFn.succeed.run($mobile);
						}
					}
					;

				},
				error : function() {
					$mobile.data('v_state', VALID_STATE.exception); // 异常
				}
			});
		},
		sendVcode : function() { // 发送短信校验码
			// mobile empty
			if (x.isEmpty($mobile.val())) {
				validateFn.error.run($mobile, validMsg.mobile.empty);
				return;
			}

			// mobile pattern error
			if (!x.gisMobile($mobile.val())) {
				validateFn.error.run($mobile, validMsg.mobile.error);
				return;
			}

			$.ajax({
				type : 'post',
				url : '/reg/sendVcode',
				dataType : 'json',
				data : {
					'mobile' : $mobile.val().trim()
				},
				success : function(result, status) {
					//console.info(result);
					if (result) { // code=1 成功
						alert(result.msg);
					} else {
						alert('验证码发送失败，请稍后再试');
					}
				},
				error : function() {
					$mobile.data('v_state', VALID_STATE.exception); // 异常
					$.unblockUI();
				}
			});
		},
		validVcode : function() { // 校验短信校验码是否正确
			var params = {
				'mobile' : $mobile.val(),
				'vcode' : $vcode.val(),
				'ssid' : new Date().getTime()
			};

			$.ajax({
				url : '/reg/validVcode/stoken-s000',
				type : 'post',
				data : params,
				dataType : 'json',
				success : function(result) {

					if (result) {
						if (1 == result.code) {// 正确
							validateFn.succeed.run($vcode);
						} else if (0 == result.code) {// 验证码不正确
							validateFn.error.run($vcode, validMsg.code.error);
						} else if (-3 == result.code) {
							validateFn.error.run($vcode,
									validMsg.code.params_error);
						}
					}
					;

				},
				error : function() {
					$vcode.data('v_state', VALID_STATE.exception); // 异常
				}
			});
		},
		formSubmit : function($eles) { // 表单提交
			var bool = true;
			for (var i = 0; i < $eles.length; i++) {
				if ($eles[i].data("v_state") == VALID_STATE.success) { // 所有v_state = 2时 ，验证通过
					bool = true;
				} else {
					bool = false;
					//console.info($eles[i]);
					$eles[i].trigger('blur');
					// break;
				}
			}
			console.info('bool = ' + bool);
			return bool;
		},
	};

	// 扩展登陆注册方法
	$.fn.extend({

		vmobile : function(page) {

			var vstate = $mobile.data('v_state');

			var primaryMobile = $mobile.data('primaryData');
			if (vstate && primaryMobile == $mobile.val()) { // 如果两个值相等说明之前做过校验，所以这次就不在校验了
				return;
			}

			if (x.isEmpty(this.val())) {
				validateFn.error.run(this, validMsg.mobile.empty);
			} else if (!x.gisMobile(this.val())) {
				validateFn.error.run(this, validMsg.mobile.error);
			} else {
				$mobile.data('primaryData', $mobile.val());

				if ('reg' === page) {
					validateFn.uniqueMobile();
				} else {
					validateFn.succeed.run($mobile);
				}
			}
		},
		vpassword : function() {
			var vstate = $password.data('v_state');

			var primaryPwd = $password.data('primaryData');
			if (vstate && primaryPwd == $password.val()) { // 如果两个值相等说明之前做过校验，所以这次就不在校验了
				return;
			}

			if (x.isEmpty(this.val())) {
				validateFn.error.run(this, validMsg.pwd.empty);
			} else if (!x.gisPwd(this.val())) {
				validateFn.error.run(this, validMsg.pwd.error);
			} else {
				$password.data('primaryData', $password.val());
				validateFn.succeed.run($password);
			}
		},
		vcode : function() {
			var vstate = $vcode.data('v_state');

			var primaryCode = $vcode.data('primaryData');
			if (vstate && primaryCode == $vcode.val()) { // 如果两个值相等说明之前做过校验，所以这次就不在校验了
				return;
			}

			if (x.isEmpty(this.val())) {
				validateFn.error.run(this, validMsg.code.empty);
			} else if ($.trim(this.val()).length != 6) {
				validateFn.error.run(this, validMsg.code.error);
			} else {
				$vcode.data('primaryData', $vcode.val());

				validateFn.validVcode();
			}
		}
	});

	// 登录/注册验证
	exports.v = function(page) {

		$mobile.on('focus', function() {
			validateFn.onFocus.run($mobile, validMsg.mobile.empty);
		});

		$mobile.on('blur', function() {
			$mobile.vmobile(page);
		});

		$mobile.on('keyup', function() {
			validateFn.destoryTips($mobile);
		});

		$password.on('focus', function() {
			validateFn.onFocus.run($password, validMsg.pwd.empty)
		});

		$password.on('blur', function() {
			$password.vpassword();
		});

		$password.on('keyup', function() {
			validateFn.destoryTips($password);
		});

		// 注册页面
		if ('reg' === page) {

			$("#S_get_vcode").on('click', validateFn.sendVcode);

//			$vcode.on("keydown", function() {
//				$("#S_reg_page").trigger('click');
//			});

			$vcode.on('focus', function() {
				validateFn.onFocus.run($vcode, validMsg.code.empty);
			});

			$vcode.on('blur', function() {
				$vcode.vcode();
			});

			$vcode.on('keyup', function() {
				validateFn.destoryTips($vcode);
			});

			$("#S_reg_page").on('click', function() {
				if (validateFn.formSubmit([ $mobile, $password, $vcode ])) {
					document.forms[0].submit();
				}
			});
		}

		// 登陆页面
		if ('login' === page) {
			
			// 账号密码读取
			var _ntoken = localStorage.getItem("ntoken");
			var _nexpires = localStorage.getItem("nexpires");
			var _nkeys = localStorage.getItem("nkeys");
			
			var _current = new Date().getTime();
			
			if(_current > _nexpires) { // 如果当前时间大于失效时间则失效，清空localStorage
				localStorage.removeItem("ntoken");
				localStorage.removeItem("nexpires");
				localStorage.removeItem("nkeys");
			} else {
				$mobile.val(_ntoken);
				$password.val(_nkeys);
				
				$("#S_rememberme").get(0).checked = true;
				$(":input[name='remember']").val(1); //记住密码的选项
			}
			
			$password.on("keydown", function(ev) {
				//console.info(ev.which);
				if(ev.which === 13) {
					$password.trigger('blur');
					setTimeout(function() {
						$("#S_page_login").trigger('click');
					}, 100);
				}
			});

			$("#S_page_login").on('click', function() {
				if (validateFn.formSubmit([ $mobile, $password ])) {
					
					// 勾选了记住密码，并且没有保存过登录信息
					if(1 == $(":input[name='remember']").val() 
							&& !localStorage.getItem("ntoken")) {
						localStorage.setItem("ntoken",$mobile.val());
						localStorage.setItem("nexpires",(new Date().getTime() + 1000 * 60 * 60 * 24 * 15));
						localStorage.setItem("nkeys",$password.val());
					}
					
					document.forms[0].submit();
				}
			});
			
			$("#S_rememberme").click(function(){
				var r = $(this).get(0).checked ? 1 : 0; //1勾选记住密码
				$(":input[name='remember']").val(r);
			});
		}

		return;
	};

});
