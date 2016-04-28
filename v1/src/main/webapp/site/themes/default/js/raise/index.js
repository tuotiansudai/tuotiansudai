// JavaScript Document

$(function(){
	$('.sear-bg-input').focus(function(){
		var $val=$('.sear-bg-input').val();
		if($val==this.defaultValue){
			$(this).val('');
			}
		});
	$('.sear-bg-input').blur(function(){
		var $val=$('.sear-bg-input').val();
		if($val==''){
			$(this).val(this.defaultValue);
			}
		});
	
//	$(".list-ul li:last-child,.true-list li:last-child").css('margin-right','0');
	$(".true-list li").each(function(i,n){
		if((i+1)%4==0){
			$(n).css("margin-right",0);
		}
	});
	
	var top=$("#slideBox").height()||$(".wrapper-hd").height();
	$(window).scroll(function(){
		if($(window).scrollTop() > top){
			$(".sc-tip").css({
				"position":"fixed",
				"top":"0px",
				"z-index":"10",
				"background":"#595959",
				});
			$(".sc-tip").children().find("a").css("color","#fff");
		}
		else{
			$(".sc-tip").css({
				"position":"relative",
				"background":"#fff",
			});
			$(".sc-tip").children().find("a").css("color","#000");
		}
	});	
	
	$(".order-sel-dl").hide();
	$(".order-sel-all").children('a').click(function(){
		var $dl = $(".order-sel-dl.status");
		if($dl.is(":visible")){
			$dl.slideUp();
		}else{
			$dl.slideDown();
		}
	});
	$(".order-sel-default").children('a').click(function(){
		var $dl = $(".order-sel-dl.order");
		if($dl.is(":visible")){
			$dl.slideUp();
		}else{
			$dl.slideDown();
		}
	});
	$("body").click(function(e){
		if(!$(e.target).is(".status-href img")){
			$(".order-sel-dl.status").slideUp();
		}
		if(!$(e.target).is(".order-href img")){
			$(".order-sel-dl.order").slideUp();
		}
	});
	$(".order-sel-dl a").click(function(){
		clearPage();
	});
	
	var $tab01li=$('.tab01-menu>ul>li');
	$tab01li.click(function(){
		$(this).addClass('current-tab').siblings().removeClass('current-tab');
		var $index=$tab01li.index(this);
		$('.tab01-con>div').eq($index).show().siblings().hide();	
	});
	
	var $tabfqli=$('.fq-tab-mene>ul>li');
	$tabfqli.click(function(){
		$('.fq-tab-mene>ul>li').removeClass('current-fq-con');
		$(this).addClass('current-fq-con');
		var $index=$tabfqli.index(this);
		$('.fq-tab-con>div').eq($index).show().siblings().hide();
	});
	$("#form").on("click",".list-ul li", function(){
		window.location.href=CONTEXT_PATH+"/raise/loanDetail/"+$(this).find("input").val();
	});
});