/**
 * 页面中的dialog  alert  confirm  progressbar  以及 table 各行换样式
 */
(function($) {
    $.fn.enhanceUIElements = function() {
        $(this).find('a.dialog-link').click($.onDialogLinkClick);
        $(this).find('a.parent-dialog-link').click($.onDialogLinkClick);
        return this;
    };
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
    //click event handler for dialog link
    $.onDialogLinkClick = function() {
    	if($(this).hasClass('dialog-false'))
    		 return false;
            var dialogTitle = this.title ? this.title : $(this).html();
            var dom = document;
            if($(this).hasClass('parent-dialog-link')){
            	dom = window.parent.document;
            }
            var effect = $(this).hasClass('no-effect');
            $(dom).jProgressbar.show();
            $.post(this.href,function(htmlData) {
	           try {
	              var obj = strToObj(htmlData);
	              htmlData = obj.desc;
				} catch (e) {
					if (! e instanceof EvalError) {
						throw e;
					}
				}
              $(dom).jProgressbar.hide();
                var htmlDataObj ='<div>' + htmlData + '</div>';
                var dialogInnerHtml = $(htmlDataObj).children().not('link').not('script').not('style').first();
                var dialogWidth = dialogInnerHtml.attr('width')? dialogInnerHtml.attr('width'):'auto';
                var dialogHeight = dialogInnerHtml.attr('height')? dialogInnerHtml.attr('height'):'auto';
                var dialog = $.openDialog(dom,dialogTitle,htmlData,dialogWidth,dialogHeight,effect);
                dialog.enhanceUIElements();
            });
            return false;
	};
     
    $.openDialog = function(dom,title,htmlData,width,height,effect) {
       var dialogDiv = '<div  class="common-form">'+htmlData+'</div>';
       var options={
               modal: true,
               title: title,
               width: width,
               height:height,
               position:{my:'center center',at:'center center',of:dom},
               minWidth:400,
               minHeight:240,
               resizable:false,
               show:effect?'':'scale',
               hide: 'scale',
               close: function(){
                   dialogInstance.dialog('destroy');
                   dialogInstance.remove();
               }
           };
       options.position.of=$('body',dom);
       var dialogInstance = $(dialogDiv).appendTo($(dom.body)).dialog(options);
        dialogInstance.find('.btn-cancel').click(function(){
            dialogInstance.dialog('close');
        });
        $('.ui-dialog-titlebar-close').click(function(){
        	if(dialogInstance.find('iframe')){
        		dialogInstance.find('iframe').remove();
        	}
        });
        return dialogInstance;
    } ;
    

    
    $.openConfirm = function(dom,title,content,width,height,callback,has) {
        var dialogDiv = '<div class="common-form"></div>';
        if(has){
        	   var dialogInstance = $(dialogDiv).appendTo($(dom.body)).dialog({
                   modal: true,
                   title: title,
                   width: width,
                   minWidth:400,
                   minHeight:240,
                   position:{my:'center center',at:'center center',of:dom},
                   resizable:false,
                   show: 'scale',
                   hide: 'explode',
                   close: function(){
                       dialogInstance.dialog('destroy');
                       dialogInstance.remove();
                   },
                   buttons: {
           			"确定": function() {
           				$( this ).dialog( "close" );
           				if(callback){
           					callback(true);
           				}
           			},
           			"取消": function() {
           				$( this ).dialog( "close" );
           				if(callback){
           					callback(false);
           				}
           			}
           		}
               });
               dialogInstance.append(content);
               dialogInstance.find('.btn-cancel').click(function(){
                   dialogInstance.dialog('close');
               });
               return dialogInstance;
        }else{
        	   var dialogInstance = $(dialogDiv).appendTo($(dom.body)).dialog({
                   modal: true,
                   title: title,
                   width: width,
                   minWidth:400,
                   minHeight:240,
                   resizable:false,
                   position:{my:'center center',at:'center center',of:dom},
                   show: 'scale',
                   hide: 'explode',
                   close: function(){
                       dialogInstance.dialog('destroy');
                       dialogInstance.remove();
                   },
                   buttons: {
           			"确定": function() {
           				$( this ).dialog( "close" );
           				if(callback){
           					callback(true);
           				}
           			}
           		}
               });
               dialogInstance.append(content);
               dialogInstance.find('.btn-cancel').click(function(){
                   dialogInstance.dialog('close');
               });
               return dialogInstance;
        }
     
    } ;
    
    $.openAlert = function(dom,title,content,width,height) {
        var dialogDiv = '<div class="common-form"></div>';
        var dialogInstance = $(dialogDiv).appendTo($(dom.body)).dialog({
            modal: true,
            title: title,
            width: width,
            minWidth:400,
            minHeight:240,
            resizable:false,
            position:{my:'center center',at:'center center',of:dom},
            show: 'scale',
            hide: 'explode',
            close: function(){
                dialogInstance.dialog('destroy');
                dialogInstance.remove();
            },
            buttons: {
    			"确定": function() {
    				$( this ).dialog( "close" );
    			}
    		}
        });
        dialogInstance.append(content);
        dialogInstance.find('.btn-cancel').click(function(){
            dialogInstance.dialog('close');
        });
        return dialogInstance;
    }  ;
})(jQuery);

$(function(){
    $(document).enhanceUIElements();
});

jQuery.extend({
  jAlert:function (title,content,domFlag,width,height,time) {
	  if(!title)
		  return false;
	  if(!content)
		  return false;
	  var dom = document;
	  if(typeof domFlag==='string'&&domFlag=='parent')
		  dom = window.parent.document;
	  var dialogTitle=title;
	  var dialogWidth = (typeof width!=='number')?'200':width;
	  var dialogHeight = (typeof height!='number')?'200':width;
	 var dialogIn =  $.openAlert(dom,dialogTitle,content,dialogWidth,dialogHeight);
		 if(time){
			    setTimeout( function(){
			    	dialogIn.dialog('close');
			    } , time);
		 }
	  return false;
  },
  jConfirm:function(title,content,callback,domFlag,hasCancel,width,height,time){
	  if(!title)
		  return false;
	  if(!content)
		  return false;
	  var dom = document;
	  if(typeof domFlag==='string'&&domFlag=='parent')
		  dom = window.parent.document;
	  var has = true;
	  if(typeof hasCancel==='string'&&hasCancel=='noCancel'){
		  has= false;
	  }
	  var dialogTitle=title;
	  var dialogWidth = (typeof width!=='number')?'200':width;
	  var dialogHeight = (typeof height!='number')?'200':width;
	  var dialogIn =   $.openConfirm(dom,dialogTitle,content,dialogWidth,dialogHeight,callback,has);
	  if(time){
		    setTimeout( function(){
		    	dialogIn.dialog('close');
		    	if(callback){
		    		callback(true);
		    	}
		    } , time);
	 }
	  return false;
  }
});
(function($) {
	$.fn.jProgressbar = function() {

	};

	$.fn.jProgressbar.show= function(text) {
		text = text || "数据加载中.....";
		createElement(text);
		setPosition();
		waterfall.appendTo("body");
		$(window).bind('resize', function() {
			setPosition();
		});
	};

	$.fn.jProgressbar.hide = function(text) {
		if(waterfall){
			waterfall.remove();
		}else if($('#waterfall')){
			$('#waterfall').remove()
		}
		
	};

	function createElement(text) {
		if (!waterfall) {
			waterfall = $(document.createElement("div"));
			waterfall.attr("id", "waterfall");
			waterfall.css( {
				"height" : "100%",
				"width" : "100%",
				"filter" : "alpha(opacity = 50)",
				"-moz-opacity" : "0.5",
				"opacity" : "0.5",
				"background-color" : "#CCCCCC",
				"position" : "absolute",
				"left" : "0px",
				"top" : "0px",
				"z-index":1000
			});
		}
		if (!loadDiv) {
			loadDiv = document.createElement("div");
		}
		$(loadDiv).appendTo(waterfall);
		
		var content =' <div style="width:' +width+ 'px; height:' +Height+'px;"><div style="width:100%; height:30px; line-height:31px;padding-left:30px;font-weight:bolder; color:#000;">'+text+'</div><div align="center" class="progressbar"></div></div>';
		$(loadDiv).html(content);
	}

	function setPosition() {
		var leftOffset = ($(document).width() - width) / 2;
		var topOffset = ($(document).height() - Height) / 2;
		$(loadDiv).css( {
			"position" : "absolute",
			"height" : Height + "px",
			"width" : width + "px",
			"left" : leftOffset + "px",
			"top" : topOffset + "px"
		});
	}

	var waterfall;
	var loadDiv;
	var width = 290;
	var Height = 60;
})(jQuery);
$(function(){
	$('tr:even').addClass('trEven');
});

function strToObj(str){
	if(typeof(str)=="string"){
		if(str==null||str==""||str=="null")
			return null;
		else{
			return eval('('+str+')');
		}
	}else
		return str;
}

function closeDialog(o){
	
	$(o).closest('.ui-dialog').eq(0).find('.ui-dialog-titlebar-close').trigger('click');

}

function addTab(o){
	window.parent.addTabs(o);
	return false;
}
