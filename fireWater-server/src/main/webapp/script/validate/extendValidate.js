// JavaScript Document
/*
*对validate的扩展
*  
*/       

  
// 字符验证       
 $.validator.addMethod("stringCheck", function(value, element) {
  var a =this.optional(element);
  var b = /^[a-zA-Z_\u4e00-\u9fa5]+[\w\u4e00-\u9fa5]*$/.test(value.replace(/(^\s+)|(\s+$)/g, ""));
   return this.optional(element) || /^[a-zA-Z_\u4e00-\u9fa5]+[\w\u4e00-\u9fa5]*$/.test(value.replace(/(^\s+)|(\s+$)/g, ""));       

 }, "只能包括汉字、字母、数字和下划线,不能以数字开头");   
 

// 中文字两个字节       

 $.validator.addMethod("byteRangeLength", function(value, element, param) {       
   var length = value.length;       
   for(var i = 0; i < value.length; i++){       
       if(value.charCodeAt(i) > 255){       
       length++;       
       }       
   } 
   var result =this.optional(element);   
   if ( $.isArray(param) ) {
		result=result||( length >= Number(param[0]) && length <= Number(param[1]) );
	} else if ( typeof param === "string" ) {
		var parts = param.split(',');
		var a =length >=  Number(parts[0]) && length <= Number(parts[1]);
		result=result||( length >=  Number(parts[0]) && length <= Number(parts[1]) );
	}   
  
   return result}, $.validator.format("长度介于{0}和{1}个字节之间(一个汉字2个字节)"));   
 
// Accept a value from a file input based on a required mimetype
$.validator.addMethod("accept", function(value, element, param) {
	// Split mime on commas in case we have multiple types we can accept
	
	var typeParam = typeof param === "string" ? param.replace(/\s/g, "").replace(/,/g, "|") : "image/*",
	optionalValue = this.optional(element);
	

	// Element is optional
	if (optionalValue) {
		return optionalValue;
	}

	if (jQuery(element).attr("type") === "file") {
		// If we are using a wildcard, make it regex friendly
		typeParam = typeParam.replace(/\*/g, ".*");

		// Check if the element has a FileList before checking each file
		if (element.files && element.files.length) {
			for (var i = 0; i < element.files.length; i++) {
				var file = element.files[i];

				// Grab the mimetype from the loaded file, verify it matches
				if (!file.type.match(new RegExp( ".?(" + typeParam + ")$", "i"))) {
					return false;
				}
			}
		}
	}

	// Either return true because we've validated each file, or because the
	// browser does not support element.files and the FileList feature
	return true;
}, jQuery.validator.format("文件类型不正确"));
// Accept a value from a file input based on a required mimetype
$.validator.addMethod("acceptType", function(value, element, param) {
	// Split mime on commas in case we have multiple types we can accept
	
	
	var  optionalValue = this.optional(element);
	// Element is optional
	if (optionalValue) {
		return optionalValue;
	}

	if (jQuery(element).attr("type") === "file") {
		
			if(typeof param === "string"){
			  var typeParams = param.split(',');
			  var flag = false;
			  for(var i = 0 ; i < typeParams.length ; i ++){
				  if(value.replace(/.+\./,'').toUpperCase()==typeParams[i].toUpperCase()){
					 flag = true; 
				  }
			   }
			   return flag;
			}	
		}
	return true;
}, jQuery.validator.format("文件名后缀不符合"));

// 身份证号码验证       

$.validator.addMethod("isIdCardNo", function(value, element) {       
   return this.optional(element) || isIdCardNo(value);       

}, "身份证号码不合法");    
    

// 手机号码验证       

$.validator.addMethod("isMobile", function(value, element) {    
   var length = value.replace(/(^\s+)|(\s+$)/g, "").length;   
   var mobile = /^1\d{10}$/;   
   return this.optional(element) || (length == 11 && mobile.test(value.replace(/(^\s+)|(\s+$)/g, "")));       

}, "手机号码不合法");      
    

 // 电话号码验证       

 $.validator.addMethod("isTel", function(value, element) {       
   var tel = /^\d{3,4}-?\d{7,9}$/;    //电话号码格式010-12345678  
   return this.optional(element) || (tel.test(value.replace(/(^\s+)|(\s+$)/g, "")));       

 }, "电话号码不合法");   
// 联系电话(手机/电话皆可)验证   

 $.validator.addMethod("isPhone", function(value,element) {   
   var length = value.replace(/(^\s+)|(\s+$)/g, "").length;   
   var mobile = /^1\d{10}$/;   
   var tel = /^\d{3,4}-?\d{7,9}$/;   
   return this.optional(element) || (tel.test(value.replace(/(^\s+)|(\s+$)/g, "")) || mobile.test(value.replace(/(^\s+)|(\s+$)/g, "")));   
 

}, "联系电话不合法");   
//邮政编码
  $.validator.addMethod("isZipCode", function(value, element) {       
   var tel = /^[0-9]{6}$/;       
   return this.optional(element) || (tel.test(value.replace(/(^\s+)|(\s+$)/g, "")))}, "邮政编码不合法");    
