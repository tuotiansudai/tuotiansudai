require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment','layerWrapper'], function ($) {
    $('.selectpicker').selectpicker();

    $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
    $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
    $('form button[type="reset"]').click(function () {
        location.href = "/activity-manage/activity-center-list";
    });

    var webShowLayer = function(webPath) {

        var img=new Image();
        img.src=webPath;
        img.onload=function(event) {
            layer.open({
                type: 1,
                shade: false,
                title: false,
                area: [this.width * 0.6+'px', this.height+'px'],
                content: '<img src ='+webPath+'>'
            });
        }

    }

    var appShowLayer = function(appPath) {
        var img=new Image();
        img.src=appPath;
        img.onload=function(event) {
            layer.open({
                type: 1,
                shade: false,
                title: false,
                area: [this.width+'px', this.height+'px'],
                content: '<img src ='+appPath+'>'
            });
        }
    }

    $('.webImg').click(function(){
        var webPath = $(this).find('img').attr('src');
        webShowLayer(webPath);
    });

    $('.appImg').click(function(){
        var appPath = $(this).find('img').attr('src');
        appShowLayer(appPath);
    });
    $('.btnAddActivity').click(function(){
        window.location.href = '/activity-manage/activity-center';
    });

});
