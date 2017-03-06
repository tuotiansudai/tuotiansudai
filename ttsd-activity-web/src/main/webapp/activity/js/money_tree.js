require(['jquery', 'commonFun', 'layerWrapper'], function ($, commonFun, layer) {

    $appVersion = $('#appVersion');
    var sourceKind = globalFun.parseURL(location.href);
    $('#btn-shake').on('click', function () {

        if ($appVersion.val() == null || $appVersion.val() < '4.2') {
            //老版本  提示升级
            layer.msg("你的APP版本太低请升级")
        } else if (sourceKind.params.source == 'app') {
            $.when(commonFun.isUserLogin())
                .done(function () {
                    location.href = "app/tuotian/shake";
                })
                .fail(function () {
                    //未登录
                    location.href = "app/tuotian/login";
                });
        } else {
            location.href = "/app/download";
        }

    });

});