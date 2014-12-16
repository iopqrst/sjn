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
		<title>时间呢（shijianne.com）- 登录页面</title>
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
			<form class="form-signin" role="form" action="/login/login?toUrl=${param.toUrl }" method="post">
				<h2 class="form-signin-heading text-center">
					登&nbsp;&nbsp;录
				</h2>
				<input tabindex="1" type="text" name="mobile" value="${mobile }" class="form-control" placeholder="请输入您的手机号码" autocomplete="off"  />
				<input tabindex="2" type="password" name="password" class="form-control" placeholder="请输入您的密码" autocomplete="off"/>
				<div class="checkbox">
					<label>
						<input type="checkbox" value="1" name="remember2"/>
						记住密码
					</label>
				</div>
				<div class="text-center form-group">
					<button id="S_page_login" class="btn btn-lg btn-success btn-inline" type="button">
						&nbsp;&nbsp;&nbsp;&nbsp;登&nbsp;&nbsp;录&nbsp;&nbsp;&nbsp;&nbsp;
					</button>&nbsp; <a href="/reg">注册 ShiJianNe</a>
				</div>
			</form>
		</div>

		<jsp:include page="./common/footer.jsp"/>
		
		<script>
			seajs.use( [ 'source/vacc'], function(v) {
				v.v('login');
			});
		</script>
	</body>
</html>