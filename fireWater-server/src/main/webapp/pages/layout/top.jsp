<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<!-- 标签 -->
	<%@include file="../common/taglib.jsp" %>
	
<div class="top">
    <h1>WEB模板</h1>
       <a href="${pageContext.request.contextPath}/user/logout.action">
    <span class="back">
       		退出
    </span>
       </a>
         <span class="setting">
        <a href="${pageContext.request.contextPath }/user/user!resetPwd.action" class="dialog-link"> 个人设置</a>
        </span>
    	
</div>
