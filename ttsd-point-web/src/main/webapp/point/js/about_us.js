require(['jquery'], function ($) {
    $(function () {
        if($('#errorContainer').length) {
            setTimeout(function(){
                window.location="/";
            },10000);
        }
    });
});