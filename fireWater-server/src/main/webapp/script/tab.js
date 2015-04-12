$(function(){
	var refresh=false;
	if($('#thxTab').attr('data')==='refresh'){
		refresh = true;
	}
	var tabs = $( "#tabs" ).tabs();
	var left = 10;
	 $(tabs).find('ul li a').click(function(){
		 $(this).parent().removeAttr('style').siblings().css('background','#eee');
		});
	var tabTitleTemplate = "<li ><a href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close' role='presentation'>Remove Tab</span></li>";
	var tabCounter = 2;
	  jQuery.extend({
		  addTab:function (objA) {
			   $(document).jProgressbar.show();
			   var label = $(objA).text();
			   if($(objA).attr('title')){
				   label = $(objA).attr('title');
			   }
			
			   //--判断是否存在
			   if(!isTab(label,$(objA).attr("href"))){
				   var id = "tabs-" + tabCounter;
					var li = $( tabTitleTemplate.replace( /#\{href\}/g, "#" + id ).replace( /#\{label\}/g, label ) );
					$(li).find('a').bind('click',findMenuForTab);
				    $('#tabs').find('ul li').css('background','#eee');
					var tabContentTemplate=$("#tabs-1").find("iframe").clone();
					var tabContentHtml = $( tabContentTemplate).attr("src",$(objA).attr("href"));
					tabs.find( ".ui-tabs-nav" ).append( li );
					tabs.append( "<div id='" + id + "'><input class='moreDisplay' value='none' type='hidden'/></div>" );
					$("#"+id).append(tabContentHtml);
					tabs.tabs( "refresh" );
					//--判断li标签总宽度是否大于tabdiv的宽度
					moreHandle();
					var iframe=$("#"+id).find('iframe').eq(0);
					$(iframe).bind('load',hideProgressbar);
					var index= tabs.find(".ui-tabs-nav").find("li").length-1;
					tabs.tabs({ active:index });
					tabCounter++;
			   }
		  }
	  });
	tabs.delegate( "span.ui-icon-close", "click", function() {
		littleHandle($(this).closest( "li" ).width());
		var panelId = $( this ).closest( "li" ).remove().attr( "aria-controls" );
		$( "#" + panelId ).remove();
		tabs.tabs( "refresh" );
		tabs.find(".ui-tabs-nav").find("li.ui-tabs-active").find('a').trigger('click');
	});
	function isTab(title,href){
	
		var lis =tabs.find( ".ui-tabs-nav" ).find("li");
		var flag =false;
		for(var i =0; i<lis.length;i++){
			var li = lis[i];
			var a = $(li).find("a");
			 var contentId = $(a).attr('href');
			  var src=$(contentId).find('iframe').eq(0).attr('src');
			if($(li).find("a").text()==title&&src==href){
				tabs.tabs({ active:i });
				flag=true;
				if(refresh)
					$(contentId).find('iframe').eq(0).attr('src',src);
				$(document).jProgressbar.hide();
				
			}
			 $(li).css('background','#eee');
		}
		return flag;
	}
	function littleHandle(liWidth){
		var tabWidth = $(tabs).width();
		var tabLiWidth = 0;
		$(tabs).find('ul li').each(function(){
			tabLiWidth +=$(this).width()+1;
		});
        if (tabLiWidth > tabWidth) {
        	if(left<0){
        		left +=liWidth;
        		if(left>0){
        			left =10;
        			 $(tabs).find("ul").css('left',left);
        		}
        		 $(tabs).find("ul").css('left',left);
        	}
        	
        } else {
        	left =10;
        	$(tabs).find("ul").css('left',10);
            $(tabs).find("div.move").hide();
        }
	}
	function moreHandle(){
		var tabWidth = $(tabs).width();
		var tabLiWidth = 0;
		$(tabs).find('ul li').each(function(){
			tabLiWidth +=$(this).width()+1;
		});
		var step=0;
        if (tabLiWidth > tabWidth) {
            $(tabs).find("div.move").show();
            step-=$(tabs).find('ul li').last().width();
            $(tabs).find("ul").css('left',step);
            $(tabs).children(".next").click(function() {
               if (Math.abs(left) <  (tabWidth - tabLiWidth+100)) {
                    left-=100;
                    $(tabs).find("ul").css('left',step+left);
                }
            });
            $(tabs).children(".prev").click(function() {
                if (left < 0) {
                    left+=100;
                    if(left>0){
                    	left =10;
                    }
                    $(tabs).find("ul").css('left',left);
               }

            });
        } else {
            $(tabs).find("div.move").hide();
        }
	}
	function hideProgressbar(){
		$(document).jProgressbar.hide();
	}
	//--点击标签时选中对应菜单
	/*$( "#tabs a " ).click(function(){
		$('.menu li a').each(function(){
    		if($(this).hasClass('current')){
    			$(this).trigger('click');
    			$('.menu  li a').removeClass('current');
    		}
    	});
	});*/
	function findMenuForTab(){
		var text =$(this).text();
		 $(document).jProgressbar.show();
		 $(this).parent().removeAttr('style').siblings().css('background','#eee');
		
	    var contentId = $(this).attr('href');
	    var href=$(contentId).find('iframe').eq(0).attr('src');
	    if(refresh){
	    	$(contentId).find('iframe').eq(0).attr('src',href);
	    }
	   $('.menu li a').each(function(){
		   var flag = false;
		   if($(this).attr('title')){
			   if($(this).attr('title')==text&&$(this).attr('href')==href){
				   flag = true; 
			   }
		   }else{
			   if($(this).text()==text&&$(this).attr('href')==href){
				   flag = true; 
			   }
		   }
		   if(flag){
			   if( !$(this).hasClass('current')){
				   var menus = $(this).closest('ul');
				   if($(menus).css('display')!='block'){
					   $(menus).prev('a').trigger('click');
				   }
				   $(this).trigger('click');
				   $(document).jProgressbar.hide();
				   return true;
			   }
			   
		   }
	   });
	   $(document).jProgressbar.hide();
	}
});


