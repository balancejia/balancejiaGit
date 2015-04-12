<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>泰和鑫——WEB模板</title>
</head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/theme/default/css/login.css" />
<link  rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/default/css/ui/jquery-ui-1.10.3.custom.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery-ui-1.10.3.custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/page.js"></script>
<script language="JavaScript">
	if (window != top)
		top.location.href = location.href;
	$(function(){
		$(document).jProgressbar.show();
		$.ajax({
			url:'${pageContext.request.contextPath}/user/login!input.action',
			async:false,
			success:function(data){
				$(document).jProgressbar.hide();
				$('div.login').append(data);
				
			}
		});
		
	});
</script>

<body>
	<div class="login">
		<img src="${pageContext.request.contextPath}/theme/default/images/login_logo.jpg"
				style="margin-top: 40px; margin-left: 30px; width: 380px;" />
		
	</div>
	
</body>
</html>