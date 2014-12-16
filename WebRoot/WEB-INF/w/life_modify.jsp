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
		<meta name="viewport"
			content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>时间呢（shijianne.com）- 修改事件</title>
		<link href="/css/all.css" type="text/css" rel="stylesheet" />
		<link href="/css/ui-lightness/jquery-ui.css" rel="stylesheet" />
		<!--[if lt IE 9]>
	      <script src="js/html5shiv.min.js"></script>
	      <script src="js/respond.min.js"></script>
	    <![endif]-->
	</head>
	<body data-lid="<x11:des fn="encrypt" value="${item.lid}"/>">
		<jsp:include page="./common/header.jsp"/>
		
		<div class="container sjn-content">
			<c:if test="${!empty msg}">
			<div class="alert alert-danger" role="alert">
				<strong>错误提示：</strong>${msg }
			</div>
			</c:if>
			<c:if test="${!empty param.e}">
			<div class="alert alert-info" role="alert">
				<strong>温馨提示：</strong>处理过程中出现了一些问题，请刷新页面重新添加(是你填错了，还是我做错了，要不我们一起检查一下？)
			</div>
			</c:if>
			
			<form name="ItemModifyForm" action="/life/modify/<x11:des fn="encrypt" value="${item.lid}"/>" method="post" role="form">
				<div class="form-group">
					<label for="">
						选择日期：
					</label>
					
					<div class="input-group">
				      <div class="input-group-addon">
				      	<span class="glyphicon glyphicon-calendar"></span>
				      </div>
					  <input type="text" readonly="readonly" class="form-control" id="S_whichDay" value="${item.whichDay }">
				    </div>
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
							value="<x11:timeline val='${item.beginPolt }'/>" />
					</div>
					<input type="hidden" name="lifeItem.beginPolt"
							value="<fmt:formatNumber pattern="#.#" value="${item.beginPolt }"/>" />
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
							value="<x11:timeline val='${item.endPolt }'/>" readonly="readonly" />
						<input type="hidden" name="lifeItem.endPolt" value="<fmt:formatNumber pattern="#.#" value="${item.endPolt }"/>"/>
					</div>
					<a href="javascript:void(0);" id="S_last_data">使用上次数据</a>
				</div>
				
				<div class="form-group">
					<label for="">
						Do What：
					</label>
					<textarea rows="5" cols="10" class="form-control"
						name="lifeItem.doWhat" placeholder="如果连你都不在乎，谁还会在乎？！">${item.doWhat }</textarea>
				</div>
				
				<div class="form-group">
					<label for="S_item_category">
						类&nbsp;&nbsp;别：
					</label>
					<select id="S_item_category" name="category"
						class="form-control selectpicker show-tick">
						<c:forEach var="category" items="${categoryList}">
							<option value="<x11:des fn="encrypt" value="${category.id }"/>" style="${category.style}"  
							 <c:if test="${item.category == category.id }"> selected="selected"</c:if>>${category.name }</option>
						</c:forEach>
					</select>
					&nbsp;
					<a href="javascript:void(0);" id="S_cself">我要添加自己的类别</a>
				</div>
				
				<div class="form-group text-center">
					<button id="S_lm_update" type="button" class="btn btn-primary">
						&nbsp;&nbsp;保&nbsp;&nbsp;存&nbsp;&nbsp;
					</button>
					&nbsp;&nbsp;
					<button id="S_cancel" type="button" class="btn btn-default">
						&nbsp;&nbsp;返&nbsp;&nbsp;回&nbsp;&nbsp;
					</button>
				</div>	
			</form>
		</div>
		
		<jsp:include page="./common/footer.jsp"/>
		<jsp:include page="./common/pop_of_time.jsp"/>
		<jsp:include page="./common/pop_of_cat.jsp"/>
		
		<script>
			seajs.use( [ 'source/life', 'source/category' ], function(l, c) {
				l.page();
				c.udefineCat();
			});
		</script>
		
	</body>
</html>