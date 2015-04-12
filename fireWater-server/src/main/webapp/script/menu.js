 $.onMenuLiAClick = function(){
	 var isCurrent = true;
	 if(!$(this).hasClass('current')){
		 isCurrent = false;
	 }
	 $('ul.menu li a').removeClass('current');
	if(!isCurrent){
		 $(this).addClass('current');
	}
		if($(this).closest("li").find('ul').length>0){
			$(this).next('span').toggleClass('fold').toggleClass('unfold');
			$(this).closest("li").children('ul').eq(0).toggle("slow");
			$(this).closest("li").siblings().find('ul').hide();
			return false;
		}
  };


