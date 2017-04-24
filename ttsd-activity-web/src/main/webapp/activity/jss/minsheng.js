require(['jquery','carousel'], function($) {
    $(function() {
    	var options={
		        "width":$(window).width()<700?300:1000,
		        "height":$(window).width()<700?105:350,
		        "posterWidth":$(window).width()<700?210:700,
		        "posterHeight":$(window).width()<700?105:350,
		        "scale":0.6,
		        "dealy":"2000",
		        "algin":"middle",
		        "isAutoplay":"false"
		    };
		$('.caroursel').attr('data-setting',JSON.stringify(options));
    	Caroursel.init($('.caroursel'));
    });
});