/**
 * Created by zhaoshuai on 2015/6/18.
 */
require.config({
    baseUrl: "../js",//此处存放文件路径
    paths: {
        "jquery": "libs/jquery-1.10.1.min",//此处存放baseURL路径下要引入的JS文件
        "daterangepicker": "libs/jquery.daterangepicker",//此处存放baseURL路径下要引入的JS文件
        "mustache": "libs/mustache.min",
        'text': 'libs/text'
    }
});
require(['jquery', 'mustache', 'text!../tpl/dealtable.tpl', 'daterangepicker'], function ($, Mustache, dealtableTpl) {

    var oDate = new Date();
    var oYear = oDate.getFullYear();
    var oMonth = oDate.getMonth();
    var oToday = oDate.getDate();

    $(".start-end span").click(function () {
        $(this).addClass("active").siblings("span").removeClass("active");
        $(".rec_type li").eq(0).addClass("active").siblings("li").removeClass("active");
        var getarr = filterChanged();
        getAjax(getarr[0], getarr[1], 1, getarr[2]);
    });
    $(".rec-type li").click(function () {
        $(this).addClass("active").siblings("li").removeClass("active");
        var getarr = filterChanged();
        getAjax(getarr[0], getarr[1], 1, getarr[2]);

    });

    $('#daterangepicker')
        .dateRangePicker({separator: ' ~ '})
        .val((oYear + '-' + oMonth + '-' + oToday) + '~' + (oYear + '-' + parseInt(oMonth + 1) + '-' + oToday))
        .bind('datepicker-apply', filterChanged);
    function filterChanged() {
        var dates = $('#daterangepicker').val().split('~');
        var startDay = dates[0];

        var endDay = dates[1];
        var selectedType = $('.rec-type').find(".active").attr('data-value');
        //var url = '/static/jsons/table.json?startday=' + startDay + '&endday=' + endDay + '&type=' + selectedType;
        var attr = new Array();
        attr= [startDay,endDay,selectedType];
        //console.log(attr)
        return attr;
    }
    function getAjax(stime, etime, page, rec_type) {
        $(".query_type strong").css("opacity", '1');
        var rec_typestr = '';
        if (rec_type != 0) {
            rec_typestr = "&type=" + rec_type;
        }
        if (stime == '' || stime == 'undefined') {
            var url = "/tuotian/ttsd-web/design/static/jsons/table.json?page=" + page + rec_typestr;
        } else {
            var url = "/tuotian/ttsd-web/design/static/jsons/table.json?startday=" + stime + "&endday=" + etime + "&page=" + page + rec_typestr;
        }
        $.get(url, function (res) {
            if (res.status === 'success') {
                $(".query-type strong").css("display", 'none');
                var ret = Mustache.render(dealtableTpl, res.data);
                $('.result').html(ret);
            }
        });
    }
    filterChanged();
    var getarr = filterChanged();

    getAjax(getarr[0], getarr[1], 1, getarr[2]);




})