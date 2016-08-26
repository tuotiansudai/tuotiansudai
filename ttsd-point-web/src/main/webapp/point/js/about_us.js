require(['jquery'], function ($) {
    $(function () {
        if($('#errorContainer').length) {
            setTimeout(function(){
                window.location="/";
            },10000);
        }
        setInterval(function(){
            $('.jump-tip i').text()<1?window.location="/":$('.jump-tip i').text(function(index,num){return parseInt(num)-1});
        },1000);
    });
});