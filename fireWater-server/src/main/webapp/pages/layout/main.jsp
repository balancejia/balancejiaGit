<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<%@ include file="../common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>泰和鑫——WEB模板</title>
<link  rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/default/css/main.css" />
<link rel="stylesheet" type="text/css"href="${pageContext.request.contextPath}/theme/default/css/page.css" />
<link  rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/default/css/ui/jquery-ui-1.10.3.custom.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery-ui-1.10.3.custom.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/main.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/page.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/tab.js"></script>
<script type="text/javascript">
	 function addTabs(o){
		$.addTab(o);
		return false;
	} 
</script>
</head>
<body>
    	<%@ include file="top.jsp"%>
    	<div class="center">
	    	   	<%@ include file="left.jsp"%>
	    	   	<div class="content">
	    	  			 	
			    	   	<div id="tabs" >
				    	   	<div class="move prev"></div>
					
				        	<div class="move next" ></div>
								<div>
									<ul >
										<li>
											<a href="#tabs-1">主页</a>
									  </li>
									</ul>
								</div>
								<div id="tabs-1">
								<input class="moreDisplay" value="null" type="hidden"/>
										<iframe align="top" name="iframe" class="mainiframe"
													frameborder="0" scrolling="auto" src="${pageContext.request.contextPath}/pages/common/home.jsp"
													width="100%" noresize></iframe>
								</div>
					    </div>
				    	
	    	   </div>
    	  </div>
    		<%@ include file="footer.jsp"%>
</body>
</html>