var streamcfg ;
var _t;
var stream_imgs = new Array();//所有上传文件保存至数据库生成的id
var lastFileId = "";
var tempImgs = new Array();//上次上传的文件id
function initStreamConfig(config){//初始化配置
	    streamcfg = {
	    showImg:config.showImg||false,
	    miniUpload:false,
		basePath:config.basePath,
		browseButton:config.browseButton||'<a id="browse_file" href="javascript:void(0);" class="browseButton dialog-link no-effect" title="上传">上传</a>',//点击弹出上传页面的按钮
		customUploadPageUrl:config.customUploadPageUrl||config.basePath+"/upload/upload!toUploadPage.action",//跳转至上传页面的路径
		uploadWindowHeight:config.uploadWindowHeight||300,//上传窗口的高度
		uploadwindowWidth:config.uploadwindowWidth||650,//上传窗口的宽度
    	messagerId :"i_stream_message_container", /** 消息显示容器的ID, 默认: i_stream_message_container */
    	fileShowTable:config.fileShowTable||'<table width="100%"  id="file_table" class="stream-table"></table>',//图片回显的table
    	btnClassName:config.btnClassName||"button",//上传页面的所有链接按钮的样式
		browseFileId :"i_select_files", /** 选择文件的ID, 默认: i_select_files */
		browseFileBtn : config.browseFileBtn ||'<a id="select_file" href="javascript:void(0)" class="button" title="请选择文件" >请选择文件</a>', /** 显示选择文件的样式, 默认: `<div>请选择文件</div>` */
		dragAndDropArea: config.messagerId ||"i_select_files", /** 拖拽上传区域，Id（字符类型"i_select_files"）或者DOM对象, 默认: `i_select_files` */
		dragAndDropTips:config.dragAndDropTips || '<span id="tips" style="color:#999999">或拖拽文件到此框</span>', /** 拖拽提示, 默认: `<span>把文件(文件夹)拖拽到这里</span>` */
		filesQueueId : config.messagerId ||"i_stream_message_container", /** 文件上传容器的ID, 默认: i_stream_files_queue */
		filesQueueHeight : config.filesQueueHeight ||100, /** 文件上传容器的高度（px）, 默认: 100 */
		multipleFiles: config.multipleFiles ||true, /** 多个文件一起上传, 默认: true */
		autoUploading: config.autoUploading ||false, /** 选择文件后是否自动上传, 默认: false */
		autoRemoveCompleted : config.autoRemoveCompleted ||false, /** 是否自动删除容器中已上传完毕的文件, 默认: false */
		maxSize: config.maxSize, /** 单个文件的最大大小，默认:2G*/
		retryCount : config.retryCount ||5, /** HTML5上传失败的重试次数 */
		postVarsPerFile : { /** 上传文件时传入的参数，默认: {} */
		param1: "val1",
		param2: "val2"
		},
		swfURL : config.swfURL ||config.basePath+"/script/stream/swf/FlashUploader.swf", /** SWF文件的位置 */
		tokenURL : config.tokenURL ||config.basePath+"/stream/upload?type=breakPoint", /** 根据文件名、大小等信息获取Token的URI（用于生成断点续传、跨域的令牌） */
		frmUploadURL :config.frmUploadURL || config.basePath+"/stream/upload?type=flash", /** Flash上传的URI */
		uploadURL : config.uploadURL ||config.basePath+"/stream/upload?type=html5", /** HTML5上传的URI */
		simLimit: config.simLimit ||10, /** 单次最大上传文件个数 */
		extFilters: config.extFilters||[".PNG",".BMP",".GIF",".JPG",".JPEG",".RAR",".ZIP",".TXT"] , /** 允许的文件扩展名, 默认:图片的格式以及压缩包*/
		onComplete: function(file) {//上传完图片显示
			comleteUp(file,_t.data_file_id);
		}
	};
	initFormPage();
	
}
function initFormPage(){//如果是修改，则需要把之前上传的图片展示出来
	var  browseBtn = $(streamcfg.browseButton);
	$(".stream_upload").hide().after(browseBtn);
	browseBtn.attr("href",streamcfg.customUploadPageUrl);
	browseBtn.enhanceUIElements();//让页面的input变成上传按钮
	var showStr = $('<ul id="showFileUl"></ul>');//表单页面添加显示框
	var browseBtn = $(streamcfg.browseButton);
    $(".stream_upload").next(browseBtn).after(showStr);
    lastFileId = $(".stream_upload").val();
	var cellStrs = "";
	if(lastFileId!=""){
		tempImgs=lastFileId.split(",");
		 $.ajax({
			type: "POST",
			url:streamcfg.basePath+"/upload/upload!getFile.action",
			data: "fileId="+lastFileId, 
			async:false,
			success:function(data){ 
			var res = eval(data);
			for(var i=0;i<res.length;i++){
				var showStr = $('<ul id="showFileUl"></ul>');
				cellStrs+='<li id="li'+res[i].fileId+'" class="stream-cell-file-show">';
				if(!streamcfg.showImg){
				  cellStrs+='<a href="'+streamcfg.basePath+'/sys/file!download.action?fileId='+res[i].fileId+'"><img width="16px" height="16px" class="left-img" src="'+streamcfg.basePath+'/script/stream/css/img/annex.png"/></a>';
				}else{//如果配置显示图片
				  var isImg = checkImg(res[i].oldName);
				  if(isImg&&res[i].fileId!=""&&res[i].fileId!=null){//如果是图片
					 cellStrs+='<img width="40px" height="40px" class="left-img" src="'+streamcfg.basePath+'/sys/file!download.action?fileId='+res[i].fileId+'"/>';
				   }else{
					  var ext = -1 !== res[i].oldName.indexOf(".") ? res[i].oldName.replace(/.*[.]/, "").toLowerCase() : "";
					  var showImg = whatImgShow(ext);
					  cellStrs+='<a href="'+streamcfg.basePath+'/sys/file!download.action?fileId='+res[i].fileId+'">'+
					  '<img width="40px" height="40px" class="left-img" src="'+streamcfg.basePath+'/script/stream/css/img/'+showImg+'"/></a>';
					}
				}
				cellStrs+='<span class="file-name-show"><strong>'+res[i].oldName+'</strong></span>'
				+'<a id="'+res[i].fileId+'" class="stream-cancel-show" onclick="removeRow(this,0);" href="javascript:void(0)" title="删除">删除</a>'
				+'</li>';
		   }
		  if(cellStrs!='')
			  $("#showFileUl").append(cellStrs);
		}});
	
	}
}
function initStreamPage(){//初始化弹出层页面
	
	$("#"+streamcfg.messagerId).attr("style",'width:'+streamcfg.uploadwindowWidth+'px;height:'+streamcfg.uploadWindowHeight+'px;');
	_t = new Stream(streamcfg);
	$('#start_upload').closest('.common-form').eq(0).dialog({beforeClose:fileSure});//点关闭按钮之前调用的方法
    var  selectBtn = $(streamcfg.browseFileBtn).eq(0);//获取选择文件按钮
    selectBtn.addClass(streamcfg.btnClassName);//给按钮加样式
	$('#start_upload').addClass(streamcfg.btnClassName);//给按钮加样式
	$('#end_upload').addClass(streamcfg.btnClassName);//给按钮加样式
	/*如果支持html5，则把选择文件的div的高度和宽度的样式去掉zili*/
	if(_t.bStreaming==true){
	  $("#i_select_files").removeAttr("style");
	}else{
	  $("#i_select_files").attr("style","width: 100px;height:30px");
	  $('#end_upload').remove();
	}
	$("#i_select_files").addClass("stream-browse-files-normal");
}
function  setValue(){//给action中存储所有上传文件名称的变量赋值
	  var tempStr = ""; 
	  if(stream_imgs.length>0){
		  for(var i=0;i<stream_imgs.length;i++){
			  tempStr += stream_imgs[i].fileId+",";
		  }
	     tempStr = tempStr.substring(0, tempStr.length-1);
	  }
	  if(tempImgs.length>0){//将之前上传的附加在此，即修改时上传附件
		  if(tempStr!=""){
		      tempStr += ",";
		  }
		  for(var i=0;i<tempImgs.length;i++){
			  tempStr += tempImgs[i]+",";
		  }
		  tempStr = tempStr.substring(0, tempStr.length-1);
	  }
	  $(".stream_upload").attr("value",tempStr);
}
function removeRow(obj,flag){//删除行,并且删除图片
	$('ul li').each(function(){
	    $("#li"+obj.id).remove();
	}); 
	delData(obj,flag);
  }
function delData(obj,flag){
	   var fileId = obj.id;
	   if(flag==0){//删除上次上传，修改时采用
		   for(var i=0;i<tempImgs.length;i++){
			 if(tempImgs[i]==fileId){
				 tempImgs.splice(i,1);
			    break;
			   }
			 }   
	   }
	   else{//删除本次上传
	     for(var i=0;i<stream_imgs.length;i++){
		   if(stream_imgs[i].fileId==fileId){
		      stream_imgs.splice(i,1);
		      break;
		    	}
		     }
	   }
	$.ajax({			
		type : "POST",
		url:streamcfg.basePath+"/upload/upload!delFile.action",
		data: "fileId="+fileId,						
		success : function(eve){
			  setValue();
			 }		 
		});	
	}
function checkImg(name){
	var isImg = false;
	var ext = -1 !== name.indexOf(".") ? name.replace(/.*[.]/, "").toLowerCase() : "";
	var imgArray = new Array("gif","png","bmp","jpg","jpeg");
	for(var i=0;i<imgArray.length;i++){
		if(ext==imgArray[i]){
			isImg = true;
			break;
		}
	}
	return isImg;
}
function comleteUp(file,fileId){//弹出层，上传完图片显示
	if(lastFileId==""){//如果不是修改
	  var showStr = $('<ul id="showFileUl"></ul>');//表单页面添加显示框
	  var browseBtn = $(streamcfg.browseButton);
	}
    $(".stream_upload").next(browseBtn).after(showStr);
	var fileName = file.name;
	var isImg = checkImg(file.name);
	$("#"+file.id).find(".stream-uploaded").html("");
	$("#"+file.id).find(".stream-speed").html("已完成");
	$("#"+file.id).find(".stream-remain-time").html("");
	$("#"+file.id).find(".stream-cancel").attr("id",fileId);

	if(isImg&&fileId!=""&&fileId!=null){
		$("#"+file.id).find("img").attr("src",streamcfg.basePath+'/sys/file!download.action?fileId='+fileId);	
	}
    var s_file = new Object();
	s_file.fileId = fileId;
	s_file.fileName = file.name;
	s_file.isAdd = false;
	stream_imgs.push(s_file);
	setValue();
	         
 }
function checkFileIsUploading(){//stream-uploaded
    /*var isUploading = false;
	var uploadArray = new Array();
	uploadArray = $(".stream-uploaded");
	for(var i=0;i<uploadArray.length;i++){
		if($.trim(uploadArray[i].innerText)!=""){
			isUploading = true;
			break;
		}
	}
	return isUploading;*/
	return _t.uploading;
}
function fileSure(){//点击确定后的事件
	var isUploading = checkFileIsUploading();
	if(isUploading){
		$.jConfirm("您还有文件正在上传","确定取消上传吗?",function(r){
			if(r){
				_t.stop();
				$('#start_upload').closest('.common-form').eq(0).dialog('destroy').remove();//关闭窗口
				resultShow();
			}
		});
		return false;
	}else{
		$('#start_upload').closest('.common-form').eq(0).dialog('destroy').remove();//关闭窗口
		resultShow();
	}
}
function whatImgShow(ext){//zili选择文件后，判断显示哪个文件图标
	var imgArray = new Array("gif","png","bmp","jpg","jpeg");
	for(var i=0;i<imgArray.length;i++){
		if(ext==imgArray[i]){
			return  "img.gif";
		}
	}
	if(ext=="rar"||ext=="zip"){
		return "rar.gif";
	}
	return ext+".gif";
	}
function  resultShow(){
	  var cellStrs='';
	  for(var i=0;i<stream_imgs.length;i++){
		  if(!stream_imgs[i].isAdd){
		   cellStrs += '<li id="li'+stream_imgs[i].fileId+'" class="stream-cell-file-show">';
		   if(!streamcfg.showImg){
		    cellStrs+='<a href="'+streamcfg.basePath+'/sys/file!download.action?fileId='+stream_imgs[i].fileId+'"><img width="16px" height="16px" class="left-img" src="'+streamcfg.basePath+'/script/stream/css/img/annex.png"/></a>';
		   }else{//如果配置显示图片
			   var isImg = checkImg(stream_imgs[i].fileName);
			   if(isImg&&stream_imgs[i].fileId!=""&&stream_imgs[i].fileId!=null){//如果是图片
				   cellStrs+='<img width="40px" height="40px" class="left-img" src="'+streamcfg.basePath+'/sys/file!download.action?fileId='+stream_imgs[i].fileId+'"/>';
			   }else{
				   var ext = -1 !== stream_imgs[i].fileName.indexOf(".") ? stream_imgs[i].fileName.replace(/.*[.]/, "").toLowerCase() : "";
				   var showImg = whatImgShow(ext);
				   cellStrs+='<a href="'+streamcfg.basePath+'/sys/file!download.action?fileId='+stream_imgs[i].fileId+'">'+
				   '<img width="40px" height="40px" class="left-img" src="'+streamcfg.basePath+'/script/stream/css/img/'+showImg+'"/></a>';
			   }
		   }
		   cellStrs+='<span class="file-name-show"><strong>'+stream_imgs[i].fileName+'</strong></span>'
		   +'<a id="'+stream_imgs[i].fileId+'" class="stream-cancel-show" onclick="removeRow(this);" href="javascript:void(0)" title="删除">删除</a>'
		   +'</li>';
		   stream_imgs[i].isAdd = true;
		  }
	}
	  if(cellStrs!='')
	    $("#showFileUl").append(cellStrs);
}
//通过元素串获取元素的id
function  getIdFromDomStr(str){
	var idStr = str.substring(str.indexOf("id="));
	var startIndex = idStr.indexOf('"')+1;//从第一个引号后开始
	var endIndex = idStr.substring(startIndex).indexOf('"')+startIndex;//结束位置
	return idStr.substring(startIndex,endIndex);
 };