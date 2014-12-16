<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="x11" uri="/WEB-INF/tlds/x11.tld"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<!--[if IE]>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<![endif]-->
		<meta name="viewport"
			content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>时间呢（shijianne.com） - 类别列表</title>
		<link href="/css/all.css" type="text/css" rel="stylesheet" />
		<!--[if lt IE 9]>
	      <script src="js/html5shiv.min.js"></script>
	      <script src="js/respond.min.js"></script>
	    <![endif]-->
	</head>
<body>
	<jsp:include page="./common/header.jsp" />

	<div class="container sjn-content">
		<div class="table-responsive">
			<div class="text-right">
				<a href="javascript:void(0);" id="S_category_page_add">添加新类别</a>
			</div>	
			<div class="panel panel-default">
				<div class="panel-heading">自定义的类别：</div>
				<table class="table table-bordered responsive-utilities text-center">
					<thead>
						<tr>
							<th class="hidden-xs">编号</th>
							<th>类别</th>
							<th>样式</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="cat" items="${ catList }" varStatus="i">
							<tr>
								<td class="hidden-xs">${i.index + 1}</td>
								<td>${cat.name }</td>
								<td style="${cat.style }">设定的样式</td>
								<td><c:if test="${!empty cat.uid }">
										<a
											href="javascript:void(0);" id="S_cat_modify_<x11:des fn="encrypt" value="${cat.id }"/>">修改</a> | 
										<a id="S_cat_del_<x11:des fn="encrypt" value="${cat.id }"/>"
											href="javascript:void(0);">删除</a>
									</c:if>&nbsp; <c:if test="${empty cat.uid }">
										系统默认
									</c:if></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<jsp:include page="./common/footer.jsp" />
	<jsp:include page="./common/pop_of_cat_list.jsp" />

	<script>
		seajs.use('source/category', function(cat) {
			cat.list();
		});
	</script>
</body>
</html>