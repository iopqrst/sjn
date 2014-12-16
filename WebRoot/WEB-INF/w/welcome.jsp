<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<link rel="dns-prefetch" href="//shijianne.com">
		<meta name="renderer" content="webkit">
		<!--[if IE]>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<![endif]-->
		<meta name="viewport"
			content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>时间呢（shijianne.com）- 时间管理</title>
		<meta name="keywords" content="">
		<meta name="description" content="">
		<link href="/css/all.css" type="text/css" rel="stylesheet" />
		<!--[if lt IE 9]>
	      <script src="js/html5shiv.min.js"></script>
	      <script src="js/respond.min.js"></script>
	    <![endif]-->
	</head>
	<body>
	
		<jsp:include page="common/header.jsp" />
	
		<div class="container sjn-content">
			<br /> <br /> <br /> <br />
	
			<h1>Welcome to x11 - project !! building ... </h1>		
	
			<br /> <br /> <br /> <br />
		</div>
	
		<jsp:include page="common/footer.jsp" />
		<script type="text/javascript">
			seajs.use([ 'source/xbase','source/vacc' ], function(base,va) {
				base.gTimeline();
			});
		</script>
	</body>
</html>
