<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 引入标签c,s,fmt,tp... -->
<%@ include file="../common/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=8" />
		<title>列表</title>
		<link rel="stylesheet" type="text/css"href="${pageContext.request.contextPath}/theme/default/css/page.css" />
		<link rel="stylesheet" type="text/css"href="${pageContext.request.contextPath}/theme/default/css/ui/jquery-ui-1.10.3.custom.css" />
	 	<script type="text/javascript"src="${pageContext.request.contextPath}/script/jquery-1.9.1.js"></script>
	 	<!--   
	 	<script type="text/javascript" src="${pageContext.request.contextPath}/script/stream/js/jquery-1.7.2.min.js"></script>
	 	-->
	   <script type="text/javascript"src="${pageContext.request.contextPath}/script/jquery-ui-1.10.3.custom.js"></script>
		<script type="text/javascript"src="${pageContext.request.contextPath}/script/page.js"></script>
	</head>
  <body>
		<!-- 底页 -->
		<div class="footPanel">
			<div class="handlePanel">
			<a href="test!toAddStreamPage.action" class="button dialog-link">stream控件demo</a>
			<a href="test!toAddStreamMiniPage.action" class="button dialog-link no-effect">streamMini控件demo</a>
			 <div class="pagePanel">
	
			</div>
		</div>
		</div>
	</body>
	
</html>

