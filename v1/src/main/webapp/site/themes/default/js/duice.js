	var indx = 1;
	var looper = 3500;
	var myTimer;
$(document).ready(function(){
	if($('#thscrll') && $('#thscrll img').length > 0) {
	  $('#thscrll').css({"padding-bottom":"15px"}); }

  if($("#fcimg li").length >1){
  	$("#fcimg").after( $('<div></div><ul id="fcnum"></ul>')); 
    for(i=1;i<=$("#fcimg li").length;i++){
		if(i==1) $("#fcnum").append($("<li class=\"crn\"> </li>")); 
		else $("#fcnum").append($("<li> </li>")); 
	}
    myTimer = setInterval('showFImg("#fcimg li","#fcnum li","crn")', looper);
	$("#fcnum li").click(function(){
		indx  =  $("#fcnum li").index(this);
		showFImg("#fcimg li","#fcnum li","crn");
		try{
			clearInterval(myTimer);
			myTimer = setInterval('showFImg("#fcimg li","#fcnum li","crn")', looper);
		}catch(e){}
		return false;
	});	
	$("#fcimg").hover(function(){
		if(myTimer){ clearInterval(myTimer); }
	 },function(){
			myTimer = setInterval('showFImg("#fcimg li","#fcnum li","crn")', looper);
	 });
  }
});

function showFImg(il,nl,cs){
  if($(il).length >1){
	crobj = $(il).eq(indx);
	$(il).not(crobj).hide();
	$(nl).removeClass(cs)
	$(nl).eq(indx).addClass(cs);
	crobj.stop(true,true).fadeIn('slow');
	indx = (++indx) % ($(il).length);
  }
}
<<<<<<< HEAD
//Ò»ï¿½ï¿½ï¿½Ø²ï¿½ï¿½ï¿½www.16sucai.com
=======
//Ò»Á÷ËØ²ÄÍøwww.16sucai.com
>>>>>>> features/zrz/interest_coupon_add_new_register
