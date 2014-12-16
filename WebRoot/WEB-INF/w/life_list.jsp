<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="x11" uri="/WEB-INF/tlds/x11.tld"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<meta name="renderer" content="webkit">
		<!--[if IE]>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<![endif]-->
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>时间呢（shijianne.com）- 事件列表</title>
		<link href="/css/all.css" type="text/css" rel="stylesheet" />
		<link href="/css/ui-lightness/jquery-ui.css" rel="stylesheet" />
		
		<!--[if lt IE 9]>
	      <script src="js/html5shiv.min.js"></script>
	      <script src="js/respond.min.js"></script>
	    <![endif]-->
	</head>
	<body>
		<jsp:include page="./common/header.jsp"/>
		
		<div class="container sjn-content">		
			<c:if test="${itemSize == 0 }">
			<div class="alert alert-info" role="alert"><strong>${current }</strong>还没有上传任何记录，你知道该怎么做的， 是不是马上就去添加？ 
				<a href="/life/seek" class="alert-link"><strong>添加新纪录 Go！</strong></a>
			</div>
			<h2>OR</h2>
			</c:if>
			
			<div class="alert alert-danger" role="alert">
				<form class="form-inline" role="form" method="post" action="/life/history">
					查询以往记录：
				  <div class="form-group">
				    <input type="text" name="search_begin" class="form-control" placeholder="查询开始时间" value="${search_begin }"/>
				  </div>
				  <div class="form-group">至</div>
				  <div class="form-group">
				      <input class="form-control" name="search_end" type="text" placeholder="查询结束时间" value="${search_end }"/>
				  </div>
				  <button type="submit" class="btn btn-primary">查 询</button>
				</form>
			</div>
			
			<c:if test="${itemSize > 0 }">
			<div class="table-responsive">
				<div class="text-right">
					<a href="/life/seek">添加新纪录</a>
				</div>				
				<div class="panel panel-default">
					<div class="panel-heading">
						${current }上传数据：
					</div>
					<table class="table table-bordered responsive-utilities text-center">
						<thead>
							<tr>
								<th class="hidden-xs">编号</th>
								<th>开始时间</th>
								<th>结束时间</th>
								<th class="hidden-xs">花费时间(h)</th>
								<th>日期</th>
								<th class="hidden-xs">上传时间</th>
								<th class="hidden-xs">类别</th>
								<th class="hidden-xs" width="30%">做了什么</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${ itemList }" varStatus="i">
	 						<tr style="<x11:category fn="style" value="${item.category }"/>"> 
								<td class="hidden-xs">${i.index + 1}</td>
								<td><x11:timeline val="${item.beginPolt }"/></td>
								<td><x11:timeline val="${item.endPolt }"/></td>
								<td class="hidden-xs"><fmt:formatNumber pattern="#.#" value="${item.interval }"></fmt:formatNumber></td>
								<td>${item.whichDay }</td>
								<td class="hidden-xs">
									<fmt:formatDate value="${item.uploadTime }" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;
								</td>
								<td class="hidden-xs"><x11:category value="${item.category }" fn="name"/></td>
								<td class="hidden-xs">${item.doWhat }</td>
								<td>
									<a href="/life/regret/<x11:des fn="encrypt" value="${item.lid }"/>">修改</a> | 
									<a href="/life/del/<x11:des fn="encrypt" value="${item.lid }"/>">删除</a>
								</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			</c:if>
		</div>

		<jsp:include page="./common/footer.jsp"/>
		
		<script>
			seajs.use( [ 'source/life'], function(l) {
				l.list();
			});
		</script>
	</body>
</html>