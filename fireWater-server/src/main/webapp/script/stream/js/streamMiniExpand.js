var streamcfg ;
var _t;
var stream_imgs = new Array();//所有上传文件保存至数据库生成的id
var lastFileId = "";
var tempImgs = new Array();//上次上传的文件id
function initStreamConfig(config){//初始化配置
	    streamcfg = {
		basePath:config.basePath,
		miniUpload:true,
		showImg:config.showImg||false,
		uploadWindowHeight:config.uploadWindowHeight||300,//上传窗口的高度
		uploadwindowWidth:config.uploadwindowWidth||650,//上传窗口的宽度
    	messagerId :"i_stream_message_container", /** 消息显示容器的ID, 默认: i_stream_message_container */
    	btnClassName:config.btnClassName||"button",//上传页面的所有链接按钮的样式
		browseFileId :"i_select_files", /** 选择文件的ID, 默认: i_select_files */
		browseFileBtn : config.browseFileBtn ||'<a id="select_file" href="javascript:void(0)" class="browseButton" title="浏览">浏览</a>', /** 显示选择文件的样式, 默认: `<div>请选择文件</div>` */
		filesQueueId : config.messagerId ||"i_stream_message_container", /** 文件上传容器的ID, 默认: i_stream_files_queue */
		filesQueueHeight : config.filesQueueHeight ||100, /** 文件上传容器的高度（px）, 默认: 100 */
		multipleFiles: config.multipleFiles ||true, /** 多个文件一起上传, 默认: true */
		autoUploading: config.autoUploading ||true, /** 选择文件后是否自动上传, 默认: false */
		autoRemoveCompleted : false, /** 是否自动删除容器中已上传完毕的文件, 默认: true */
		maxSize: config.maxSize||5242880, /** 单个文件的最大大小，5M*/
		retryCount : config.retryCount ||5, /** HTML5上传失败的重试次数 */
		postVarsPerFile : { /** 上传文件时传入的参数，默认: {} */
		param1: "val1",
		param2: "val2"
		},
		swfURL : config.swfURL ||config.basePath+"/script/stream/swf/FlashUploader.swf", /** SWF文件的位置 */
		tokenURL : config.tokenURL ||config.basePath+"/stream/upload?type=breakPoint", /** 根据文件名、大小等信息获取Token的URI（用于生成断点续传、跨域的令牌） */
		frmUploadURL :config.frmUploadURL || config.basePath+"/stream/upload?type=flash", /** Flash上传的URI */
		uploadURL : config.uploadURL ||config.basePath+"/stream/upload?type=html5", /** HTML5上传的URI */
		simLimit: config.simLimit ||5, /** 单次最大上传文件个数 */
		extFilters: config.extFilters , /** 允许的文件扩展名, 默认:图片的格式以及压缩包*/
		onComplete: function(file) {//上传完图片显示
			comleteUp(file,_t.data_file_id);
		}
	};
	    initFormPage();//修改时，回显图片
	    initStreamPage();//初始化页面
	    
}
function initFormPage(){////如果是修改，则需要把之前上传的图片展示出来
	changeInputToBtn();//让页面的input变成上传按钮
	var showStr = $('<ul id="showFileUl" style="padding:0"></ul>');//表单页面添加显示框
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
		  var browseBtn = $(streamcfg.browseButton);
		  if(cellStrs!='')
			  $("#showFileUl").append(cellStrs);
		}});
	
	}
}
function initStreamPage(){//初始化上传页面样式
	_t = new Stream(streamcfg);
	if(_t.bStreaming==true){
		  $("#i_select_files").removeAttr("style");
	}else{
	   $("#i_select_files").attr("style","width: 100px;height:30px");
	   $('#end_upload').remove();
	}
	$("#i_select_files").addClass("stream-browse-files-mini");
}
function changeInputToBtn(){//让页面的input变成上传按钮
	var selectDiv = $('<div id="i_select_files" style="width: 100px;height:30px" ></div>');
	var showDiv = $('<div id="i_stream_message_container"></div>');
	$(".stream_upload").hide().after(showDiv).after(selectDiv);
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
function comleteUp(file,fileId){//上传完图片显示
	var fileName = file.name;
	var fileId;
	$("#"+file.id).find(".stream-uploaded").html("");
	$("#"+file.id).find(".stream-speed").html("");
	$("#"+file.id).find(".stream-remain-time").html("");
	$("#"+file.id).find(".stream-process-bar").remove();
	$("#"+file.id).find(".stream-cell-infos").remove();
	
	$("#"+file.id).find(".stream-percent").remove();//width="40px;" height="40px;"
	$("#"+file.id).find(".stream-file-name").width('auto');
	$("#"+file.id).find(".stream-cancel").attr("id",fileId);
	if(fileId!=""&&fileId!=null){
		if(streamcfg.showImg){
			var isImg = checkImg(file.name);
			if(isImg){
				$("#"+file.id).find("img").attr("src",streamcfg.basePath+'/sys/file!download.action?fileId='+fileId);	
			 }else{
				$("#"+file.id).find("img").wrap('<a href="'+streamcfg.basePath+'/sys/file!download.action?fileId='+fileId+'"></a>');
			 }
		}else{
			 $("#"+file.id).find("img").wrap('<a href="'+streamcfg.basePath+'/sys/file!download.action?fileId='+fileId+'"></a>');
		}
	}
    var s_file = new Object();
	s_file.fileId = fileId;
	s_file.fileName = file.name;
	stream_imgs.push(s_file);
	setValue();
 }