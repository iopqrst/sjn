<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<meta name="renderer" content="webkit">
		<!--[if IE]>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<![endif]-->
		<meta name="viewport"
			content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>时间呢（shijianne.com）- 注册页面</title>
		<link href="/css/all.css" type="text/css" rel="stylesheet" />
		<link href="/css/login.css" type="text/css" rel="stylesheet" />
		<!--[if lt IE 9]>
	      <script src="js/html5shiv.min.js"></script>
	      <script src="js/respond.min.js"></script>
	    <![endif]-->
	</head>
	<body>
		<jsp:include page="./common/header.jsp"/>
		
		<div class="container sjn-content">
			<c:if test="${!empty error_msg }">
			<div class="alert alert-danger" role="alert">
				<strong>温馨提示：</strong>${error_msg } 
			</div>
			</c:if>
			
			<div class="container">
				<div class="col-md-12">
					<h1 class="margin-bottom-15">注&nbsp;&nbsp;册</h1>
					<form
						class="form-horizontal templatemo-container templatemo-login-form-1 margin-bottom-30"
						role="form" method="post" action="/reg/doReg">
						<div class="form-group">
							<div class="col-xs-12">
								<div class="control-wrapper">
									<label for="userphone" class="control-label fa-label">
									<span class="glyphicon glyphicon-phone"></span></label>
									<input tabindex="1" type="text" class="form-control" 
										id="userphone" name="mobile" value="${mobile }" 
										placeholder="请输入您的手机号码" autocomplete="off"/>
										
<!-- 									<span id="S_mobile_tip" class="base_v_msg_tip"> -->
<!-- 										手机号码格式不正确 -->
<!-- 									</span> -->
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-12">
								<div class="control-wrapper">
									<label for="password" class="control-label fa-label">
									<span class="glyphicon glyphicon-lock"></span></label> 
									<input tabindex="2" type="password" name="password"
										class="form-control" id="password" placeholder="请输入您的登录密码"/>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-12">
								<div class="control-wrapper">
									<label for="password" class="control-label fa-label">
									<span class="glyphicon glyphicon-lock"></span></label> 
									<div class="input-group">
										<input tabindex="23" type="text" name="vcode"
											class="form-control" id="vcode" placeholder="请输入您的验证码"/>
											
										<span class="input-group-btn">
									        <button id="S_get_vcode" class="btn btn-default" type="button">获取验证码</button>
									    </span>
									    
								    </div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-12">
								<div class="control-wrapper text-center">
									<input type="button" id="S_reg_page" value="注&nbsp;&nbsp;册" class="btn btn-success">
								</div>
							</div>
						</div>
					</form>
					<div class="text-center">
						<a href="/login" class="templatemo-create-new">
						登录&nbsp;<span class="glyphicon glyphicon-hand-right"></span>
						</a>
					</div>
				</div>
			</div>
			<div class="alert alert-danger" role="alert" style="display:none">
				<strong>我们承诺：</strong>
				<p>1、我们将尽力的保护用户的信息，丢失意味着丧失尊严</p>
				<p>2、不会使用用户信息谋取利益</p>
			</div>
		</div>

		<jsp:include page="./common/footer.jsp"/>
		
		<script>
			seajs.use( [ 'source/vacc'], function(v) {
				v.v('reg');
			});
		</script>
	</body>
</html>