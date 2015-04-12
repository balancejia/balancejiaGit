<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<link href="${pageContext.request.contextPath}/script/stream/css/stream-v1.css" rel="stylesheet" type="text/css"/>
	<link href="${pageContext.request.contextPath}/script/lightbox/css/lightbox.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/script/lightbox/js/lightbox.js"></script>
       <script type="text/javascript" src="${pageContext.request.contextPath}/script/stream/js/stream-v1.js"></script>
       <script type="text/javascript" src="${pageContext.request.contextPath}/script/stream/js/streamMiniExpand.js"></script>
		<form id="roleForm" class="editPanel" name="roleForm" method="post" width="700" height="350" enctype="multipart/form-data">
		<table >
		<tr><th>姓名：</th><td><input type="text" /></td></tr>
	    <tr><th>附件1：</th><td>
	    <input  name="uploadifyId0" class="stream_upload0" type="text"/></td></tr>
		<tr><th>附件2：</th><td>
		<input  name="uploadifyId1"  class="stream_upload1" type="text"/></td></tr>
		</table>
		<div style="margin: 0 auto; text-align:center;">
		
			<a href="javascript:void(0)" onclick="submitForm()" class="button confirm">保存</a>
		</div>
		</form>
		<script type="text/javascript">
		
		var streamConfig = {
				basePath:"${pageContext.request.contextPath}",
				showImg:true,
				modPath:"person",
				isLogin:false,
				inputOrder:0,
				extFilters:[".PNG",".BMP",".GIF",".JPG",".JPEG"],//允许上传文件的扩展名
				afterUpload: function(fileId) {//上传完图片的回调函数
					//写你的回调方法，fileId为数据库保存的文件id
				}
		    };
		initStreamConfig(streamConfig);
		var streamConfig1 = {
				basePath:"${pageContext.request.contextPath}",
				showImg:true,
				isLogin:false,
				inputOrder:1,
				afterUpload: function(fileId) {//上传完图片的回调函数
					//写你的回调方法，fileId为数据库保存的文件id
				}
		    };
		initStreamConfig(streamConfig1);
		function submitForm(){
			alert("uploadify0:"+$(".stream_upload0").val()+"uploadify1:"+$(".stream_upload1").val());
		}
    	</script>
