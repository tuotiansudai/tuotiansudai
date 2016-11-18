require(['jquery'], function ($) {
    $(function () {
        if($('#errorContainer').length) {
            setInterval(function(){
                $('.jump-tip i').text()<1?window.location="/":$('.jump-tip i').text(function(index,num){return parseInt(num)-1});
            },1000);
        }
    });
});