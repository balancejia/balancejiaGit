<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
      <link href="${pageContext.request.contextPath}/script/stream/css/stream-v1.css" rel="stylesheet" type="text/css"/>
       <script type="text/javascript" src="${pageContext.request.contextPath}/script/stream/js/stream-v1.js"></script>
       <script type="text/javascript" src="${pageContext.request.contextPath}/script/stream/js/streamExpand.js"></script>
		<form id="roleForm" class="editPanel" name="roleForm" method="post" width="700" height="350" enctype="multipart/form-data">
		<table >
		<tr><th>姓名：</th><td><input type="text" /></td></tr>
		<tr><th>附件：</th><td><input  name="uploadifyId" class="stream_upload" type="text"/></td></tr>
		</table>
		<div style="margin: 0 auto; text-align:center;">
		
			<a href="javascript:void(0)" onclick="submitForm()" class="button confirm">保存</a>
		</div>
		</form>
		<script type="text/javascript">
		
		var streamConfig = {
				basePath:"${pageContext.request.contextPath}"//项目路径
				//showImg:true//,
				//extFilters:[".PNG",".BMP",".GIF",".JPG",".JPEG",".RAR",".ZIP",".TXT"],//允许上传文件的扩展名
				//simLimit:5,//单次最大上传文件个数
				/**maxSize:5242880  单个文件的最大大小，单位为Byte 5M**/
		    };
		initStreamConfig(streamConfig);
		function submitForm(){
			alert($(".stream_upload").val());
			
		}
    	</script>
