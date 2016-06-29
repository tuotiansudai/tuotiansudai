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

    var appShowLayer = function() {
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
        webShowLayer();
    });

    $('.appImg').click(function(){
        appShowLayer();
    });
    $('.btnAddActivity').click(function(){
        window.location.href = '/activity-manage/activity-center';
    });

});
