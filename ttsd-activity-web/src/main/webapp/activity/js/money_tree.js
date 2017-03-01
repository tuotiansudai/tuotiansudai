require(['jquery','commonFun'], function ($,commonFun) {

    var sourceKind=globalFun.parseURL(location.href);
    $('#btn-shake').on('click',function() {

        //未登录
        if(sourceKind.params.source=='app') {
            $.when(commonFun.isUserLogin())
                .done(function(){
                    location.href="app/tuotian/shake";
                })
                .fail(function(){
                    location.href="app/tuotian/login";
                });
        }else{
            location.href="/app/download";
        }

    });
});