<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="x11" uri="/WEB-INF/tlds/x11.tld"%>
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
		<title>时间呢（shijianne.com）- 添加事件</title>
		<link href="/css/all.css" type="text/css" rel="stylesheet" />
		<link href="/css/ui-lightness/jquery-ui.css" rel="stylesheet" />

		<!--[if lt IE 9]>
	      <script src="js/html5shiv.min.js"></script>
	      <script src="js/respond.min.js"></script>
	    <![endif]-->
	</head>
	<body>
		<jsp:include page="./common/header.jsp" />

		<div class="container sjn-content">
			<c:if test="${!empty param.e}">
				<div class="alert alert-danger" role="alert">
					<strong>错误提示：</strong>处理过程中出现了一些问题，请重新添加
				</div>
			</c:if>

			<form name="ItemAddForm" action="timeFlies" method="post" role="form">
				<div class="form-group">
					<label for="S_whichDay">
						选择日期：
					</label>
					<!-- <div class="form-group">
						<div class="select-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</div> -->
						<select id="S_whichDay" name="lifeItem.whichDay"
								class="form-control selectpicker show-tick">
							<c:forEach var="d" items="${dateList}">
								<option value="${d }">
									${d }
								</option>
							</c:forEach>
						</select>
					<!-- </div> -->
				</div>

				<div class="form-group">
					<label for="S_beginPolt">
						开始时间：
					</label>
					<div class="input-group">
						<div class="input-group-addon">
							<span class="glyphicon glyphicon-time"></span>
						</div>
						<input type="text" id="S_beginPolt" class="form-control"
							readonly="readonly"
							value="<c:if test="${empty sdefault.timeline }">--选择开始时间--</c:if><c:if test="${!empty sdefault.timeline }">${sdefault.timeline }</c:if>" />
						<input type="hidden" name="lifeItem.beginPolt"
							value="<fmt:formatNumber pattern="#.#" value="${sdefault.begin }"/>" />
					</div>
				</div>

				<div class="form-group">
					<label for="S_endPolt">
						结束时间：
					</label>
					<div class="input-group">
						<div class="input-group-addon">
							<span class="glyphicon glyphicon-time"></span>
						</div>
						<input type="text" id="S_endPolt" class="form-control"
							value="--选择结束时间--" readonly="readonly" />
						<input type="hidden" name="lifeItem.endPolt" />
					</div>
					<a href="javascript:void(0);" id="S_last_data">使用上次数据</a>
				</div>

				<div class="form-group">
					<label for="">
						Do What：
					</label>
					<textarea rows="5" cols="10" class="form-control"
						name="lifeItem.doWhat" placeholder="如果连你都不在乎，谁还会在乎？！"></textarea>
				</div>

				<div class="form-group">
					<label for="S_item_category">
						类&nbsp;&nbsp;别：
					</label>
					<select id="S_item_category" name="category"
						class="form-control selectpicker show-tick">
						<c:forEach var="category" items="${categoryList}">
							<option value="<x11:des fn="encrypt" value="${category.id }"/>"
								style="${category.style}">
								${category.name }
							</option>
						</c:forEach>
					</select>
					&nbsp;
					<a href="javascript:void(0);" id="S_cself">没有想要的？</a>
				</div>

				<div class="form-group text-center">
					<button id="S_save" type="button" class="btn btn-primary">
						&nbsp;&nbsp;保&nbsp;&nbsp;存&nbsp;&nbsp;
					</button>
					&nbsp;&nbsp;
					<button id="S_cancel" type="button" class="btn btn-default">
						&nbsp;&nbsp;返&nbsp;&nbsp;回&nbsp;&nbsp;
					</button>
				</div>
			</form>
		</div>

		<jsp:include page="./common/footer.jsp" />
		<jsp:include page="./common/pop_of_time.jsp" />
		<jsp:include page="./common/pop_of_cat.jsp" />
		<script>
			seajs.use( [ 'source/life', 'source/category' ], function(l, c) {
				l.page();
				c.udefineCat();
			});
		</script>
	</body>
</html>