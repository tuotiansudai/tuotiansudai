require(['jquery', 'bootstrap','Validform','Validform_Datatype','jquery-ui','csrf','layerWrapper'], function ($) {
    $(function () {

        $('.bannerAD').click(function(){
            window.location.href = '/banner-manage/create';
        });

        var webPath = $('#webUrl')[0].src;
        var appPath = $('#appUrl')[0].src;

        var webShowLayer = function() {
            layer.open({
                type: 1,
                shade: false,
                title: false,
                area: ['1024px', '350px'], //宽高
                content: '<img src ='+webPath+'>'
            });
        }

        var appShowLayer = function() {
            layer.open({
                type: 1,
                shade: false,
                title: false,
                area: ['750px', '340px'], //宽高
                content: '<img src ='+appPath+'>'
            });
        }

        $('.webImg').click(function(){
            webShowLayer();
        });

        $('.appImg').click(function(){
            appShowLayer();
        });

    });
})