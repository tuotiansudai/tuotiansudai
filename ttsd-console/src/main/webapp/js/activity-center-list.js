require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment','layerWrapper'], function ($) {
    $('.selectpicker').selectpicker();

    $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
    $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
    $('form button[type="reset"]').click(function () {
        location.href = "/activity-manage/activity-center-list";
    });

    var webPath = $('#webPicture')[0].src;
    var appPath = $('#appPicture')[0].src;

    var webShowLayer = function() {
        layer.open({
            type: 1,
            shade: false,
            title: false,
            area: ['250px', '250px'],
            content: '<img src ='+webPath+'>'
        });
    }

    var appShowLayer = function() {
        layer.open({
            type: 1,
            shade: false,
            title: false,
            area: ['250px', '250px'],
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
