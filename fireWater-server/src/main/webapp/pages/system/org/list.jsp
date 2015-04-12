<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>组织机构</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/jquery-1.9.1.js"></script>
<script language="JavaScript">
	function ztreeClick(id){
		var href = '${pageContext.request.contextPath}/user/user!list.action'
		if(typeof id ==='string'){
			href +='?deptId='+id;
		}
		window.frames['useriframe'].window.location.href=href;
	}
	$(function(){
		$.post('${pageContext.request.contextPath}/user/dept!treeList.action?callback=ztreeClick',function(data){
			$('div.left').append(data);
		});
	});
</script>
<style type="text/css">
  .panel{
  	height: auto;
  }
  .panel .left{
  	width: 200px; 
	float: left;
  }
  .panel .right{
	  float: left;
  }
</style>
</head>
<body>
	<div class="panel">
		<div class="left"></div>
		<div class="right">
		<iframe align="top" name="useriframe"  frameborder="0" scrolling="auto" 
		src="${pageContext.request.contextPath}/user/user!list.action" width="100%" noresize></iframe>
		</div>
	</div>
	<script type="text/javascript">
	$('.panel').width($('.content',parent.document).width());
	$('iframe').height($('.content',parent.document).height()-80);
	$('.right').width($('.panel').width()-$('.left').width()-10);
	</script>
</body>
</html>