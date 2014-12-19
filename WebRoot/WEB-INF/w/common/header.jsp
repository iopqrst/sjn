<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="/">时间呢（shijianne.com）</a>
		</div>
		<div class="navbar-collapse collapse">
			<c:if test="${empty cookieMap.stoken.value && hideNavLogin != true}">
				<form action="/login/login" method="post"
					class="navbar-form navbar-right" role="form" name="navLoginForm">
					<div class="form-group">
						<input type="text" name="mobile" placeholder="请输入手机号码"
							class="form-control">
					</div>
					<div class="form-group">
						<input type="password" name="password" placeholder="请输入密码"
							class="form-control">
					</div>
					<button type="submit" class="btn btn-success" id="S_nav_login">
						登 录
					</button>
					<button type="button" class="btn btn-success" id="S_nav_reg">
						注 册
					</button>
				</form>
			</c:if>

			<c:if test="${!empty cookieMap.stoken.value }">
				<ul class="nav navbar-nav navbar-right">
					<li>
						<a href="javascript:void(0);">您好，欢迎 &nbsp;
							${cookieMap.mobile.value }</a>
					</li>
					<li>
						<a id="S_life_list" href="javascript:void(0);">事件列表</a>
					</li>
					<li>
						<a id="S_category_setting" href="javascript:void(0);">设置类别</a>
					</li>
					<li>
						<a id="S_timeline_setting" href="javascript:void(0)">时间轴</a>
					</li>
					<li class="dropdown active">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">个人中心
							<span class="caret"></span> </a>
						<ul class="dropdown-menu" role="menu">
							<li>
								<a href="#"><span class="glyphicon glyphicon-user"></span>&nbsp;个人信息</a>
							</li>
							<li>
								<a href="#"><span class="glyphicon glyphicon-lock"></span>&nbsp;修改密码</a>
							</li>
							<li class="divider"></li>
							<li>
								<a href="/login/logout"><span class="glyphicon glyphicon-log-out"></span>&nbsp;退出</a>
							</li>
						</ul>
					</li>
				</ul>
			</c:if>
		</div>
	</div>
</div>