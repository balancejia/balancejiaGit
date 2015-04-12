$(function(){
	$(".content").height($("body").height()-$(".top").height()-$(".footer").height()-9);
	$(".content").width($("body").width()-$(".center .left").width()-8);
	$("iframe").height($(".content").height()-60);
	$(".center .left").height($("body").height()-$(".top").height()-$(".footer").height());
});