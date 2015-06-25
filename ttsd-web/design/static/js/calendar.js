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
    var month = oDate.setMonth(oDate.getMonth() - 6);
    var oWeek = new Date();
    var week = oWeek.setDate(oWeek.getDate() - 7);
    $('#daterangepicker')
        .dateRangePicker({separator: ' ~ '})
        .val((oYear + '-' + oMonth + '-' + oToday) + '~' + (oYear + '-' + parseInt(oMonth + 1) + '-' + oToday))
        .bind('datepicker-apply', filterChanged);
    //六个月：
    $('.rec_sixmonth').on('click', function () {
        $('#daterangepicker').val((oDate.toLocaleDateString()) + '~' + (oYear + '-' + parseInt(oMonth + 1) + '-' + oToday));
    });
    //今天：
    $('.rec_today').on('click', function () {
        $('#daterangepicker').val((oYear + '-' + parseInt(oMonth + 1) + '-' + oToday) + '~' + (oYear + '-' + parseInt(oMonth + 1) + '-' + oToday));
    });
    //一个月：
    $('.rec_month').on('click', function () {
        $('#daterangepicker').val((oYear + '-' + oMonth + '-' + oToday) + '~' + (oYear + '-' + parseInt(oMonth + 1) + '-' + oToday));
    });
    //一周：
    $('.rec_week').on('click', function () {
        $('#daterangepicker').val((oWeek.toLocaleDateString()) + '~' + (oYear + '-' + parseInt(oMonth + 1) + '-' + oToday));
    });
    //全部：
    $('.rec_all').on('click', function () {
        $('#daterangepicker').val('');
    });
    function filterChanged() {
        var dates = $('#daterangepicker').val().split('~');
        var startDay = dates[0];
        var endDay = dates[1];
        var selectedType = $('.rec_type').find(".active").attr('data-value');
        var url = '/static/jsons/table.json?startday=' + startDay + '&endday=' + endDay + '&type=' + selectedType;
        var arr = [startDay, endDay, selectedType];
        return arr;
    }

    filterChanged();
    var getarr = filterChanged();
    getAjax(getarr[0], getarr[1], 1, getarr[2]);

    $(".start_end span").click(function () {
        $(this).addClass("active").siblings("span").removeClass("active");
        $(".rec_type li").eq(0).addClass("active").siblings("li").removeClass("active");
        getarr = filterChanged();
        getAjax(getarr[0], getarr[1], 1, getarr[2]);
    })
    $(".rec_type li").click(function () {
        $(this).addClass("active").siblings("li").removeClass("active");
        getarr = filterChanged();
        getAjax(getarr[0], getarr[1], 1, getarr[2]);

    })
    function getAjax(stime, etime, page, rec_type) {
        $(".query_type strong").css("opacity", '1');
        var rec_typestr = '';
        if (rec_type != 0) {
            rec_typestr = "&type=" + rec_type;
        }
        if (stime == '' || stime == 'undefined') {
            var url = "/static/jsons/table.json?page=" + page + rec_typestr;
        } else {
            var url = "/static/jsons/table.json?startday=" + stime + "&endday=" + etime + "&page=" + page + rec_typestr;
        }
        $.get(url, function (res) {

            if (res.status === 'success') {
                $(".query_type strong").css("display", 'none');
                var ret = Mustache.render(dealtableTpl, res.data);
                $('.result').html(ret);
            }
            $('.nextbtn').click(function(){
                if(res.data.totalPages<=res.data.currentPage){
                    alert('下一页没有更多数据了!');
                    return false;
                }
                var cPage=res.data.currentPage+1;
                $('.page_record').find('small').eq(2).html(cPage);
                url = "/static/jsons/table.json?startday=" + stime + "&endday=" + etime + "&page=" + cPage + rec_typestr;
                $.get(url,function(){
                    return false;
                });
            });
            $('.prevbtn').on('click', function () {
                if(res.data.currentPage<=1){
                    alert('上一页没有更多数据了');
                    return false;
                }
                //在这里ajax请求
                var cPage=res.data.currentPage-1;
                $('.page_record').find('small').eq(2).html(cPage);
                url = "/static/jsons/table.json?startday=" + stime + "&endday=" + etime + "&page=" + cPage + rec_typestr;
                $.get(url,function(){
                    return false;
                });

            });
            ;
        });
    }
})