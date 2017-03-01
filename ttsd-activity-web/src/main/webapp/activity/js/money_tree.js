require(['jquery','commonFun'], function ($,commonFun) {
    $('#btn-shake').on('click',function() {

        //未登录
        $.when(commonFun.isUserLogin())
            .done(function(){
                location.href="app/tuotian/shake";
            })
            .fail(function(){
                location.href="app/tuotian/login";
            });
    });
});