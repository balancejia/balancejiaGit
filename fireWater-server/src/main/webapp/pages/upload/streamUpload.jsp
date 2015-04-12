<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="i_select_files" style="width: 100px;height:30px" >
</div>
    <div id="i_stream_message_container" class="stream-main-upload-box"  >
	
	</div>

	<a href="javascript:void(0)" id="start_upload" title="开始上传" onclick="javascript:_t.upload();">开始上传</a>
	<a href="javascript:void(0)"  id="end_upload" title="停止上传" onclick="javascript:_t.stop();">停止上传</a>
	<a href="javascript:void(0)"  class="button" id="file_sure" title="确定" onclick="fileSure(this)">确定</a>
<script type="text/javascript">
	initStreamPage();
 </script>
