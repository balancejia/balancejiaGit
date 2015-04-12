<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/theme/default/css/menu.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/menu.js"></script>
<script type="text/javascript">
	$.ajax({
		url : "${pageContext.request.contextPath}/sys/resource!getMenu.action",
		success : function(data) {
			var a = $('<div>'+data+'</div>');
			a.find('ul').eq(0).append('<li><a href="${pageContext.request.contextPath}/upload/test!toListPage.action" onclick="$.addTab(this);return false;" style="padding-left: 40px;">上传测试</a></li>');
			$('.left').append(a.html());
		}
	});
</script>
<div class="left">


</div>