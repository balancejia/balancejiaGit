<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/theme/default/css/menu.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/menu.js"></script>
<script type="text/javascript">
$(function(){
	$('ul.menu li ul').hide();
	$('ul.menu li a').click($.onMenuLiAClick);
});
</script>
<div class="left">
	<ul class="menu">
		<li class="menu-title"><img
			src="${pageContext.request.contextPath}/theme/default/images/menu-title.png">功能菜单</li>
		<li><a href="javascript:void(0);"><span class="level_one">&nbsp;</span>系统管理</a>
		<ul  >
				<li><a href="${pageContext.request.contextPath}/pages/system/org/list.jsp"
					onclick="$.addTab(this);return false;" style="padding-left: 40px;">组织机构管理</a><span
					class="unfold"></span>
				<ul  >
						<li><a href="${pageContext.request.contextPath}/user/role!list.action"
							onclick="$.addTab(this);return false;"
							style="padding-left: 60px;">角色管理</a></li>
					</ul></li>
				<li><a href="${pageContext.request.contextPath}/sys/dictName!list.action"
					onclick="$.addTab(this);return false;" style="padding-left: 40px;">字典管理</a></li>
				<li><a href="${pageContext.request.contextPath}/sys/log!list.action"
					onclick="$.addTab(this);return false;" style="padding-left: 40px;">日志管理</a></li>
				<li><a href="${pageContext.request.contextPath}/sys/resource!list.action"
					onclick="$.addTab(this);return false;" style="padding-left: 40px;">资源管理</a></li>
			</ul></li>
		<li><a href="javascript:void(0);"><span class="level_one">&nbsp;</span>联系人管理</a>
		<ul  >
				<li><a href="${pageContext.request.contextPath}/contacts/contacts!list.action"
					onclick="$.addTab(this);return false;" style="padding-left: 40px;">联系人管理</a></li>
			</ul></li>
		<li><a href="javascript:void(0);"><span class="level_one">&nbsp;</span>客户端数据管理</a>
		<ul >
				<li><a href="${pageContext.request.contextPath}/cappsoft/cappsoft!list.action"
					onclick="$.addTab(this);return false;" style="padding-left: 40px;">客户端软件版本管理</a></li>
				<li><a href="${pageContext.request.contextPath}/opinion/opinion!list.action"
					onclick="$.addTab(this);return false;" style="padding-left: 40px;">意见反馈</a></li>
				<li><a href="${pageContext.request.contextPath}/news!list.action"
					onclick="$.addTab(this);return false;" style="padding-left: 40px;">通知公告</a></li>
			</ul></li>
		<li><a href="javascript:void(0);"><span class="level_one">&nbsp;</span>测试</a></li>
	</ul>
</div>




