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

    //初始化页面
    var oDate = new Date();
    var oYear = oDate.getFullYear();
    var oMonth = oDate.getMonth();
    var oToday = oDate.getDate();
    var _page,_hasNextPage,_hasPreviousPage;  //define pages
    // 页面初始化日期 条件筛选1个月
    $('#daterangepicker')
        .dateRangePicker({separator: ' ~ '})
        .val((oYear + '-' + oMonth + '-' + oToday) + '~' + (oYear + '-' + parseInt(oMonth + 1) + '-' + oToday));
    //ajax require
    function getAjax(page) {
        var dates = $('#daterangepicker').val().split('~');
        var startDay = $.trim(dates[0]);
        var endDay = $.trim(dates[1]);
        var selectedType = $('.rec-type').find(".active").attr('data-value');
        $(".query_type strong").css("opacity", '1');
        var rec_typestr = '';
        if (selectedType) {
            rec_typestr = "&type=" + selectedType;
        }
        if (startDay == '' || startDay == 'undefined') {
            var url = "/tuotian/ttsd-web/design/static/jsons/table.json?page=" + page + rec_typestr;
        } else {
            var url = "/tuotian/ttsd-web/design/static/jsons/table.json?startday=" + startDay + "&endday=" + endDay + "&page=" + page + rec_typestr;
        }
        $.get(url, function (res) {
            if (res.status === 'success') {
                $(".query-type strong").css("display", 'none');
                var ret = Mustache.render(dealtableTpl, res.data);
                $('.result').html(ret);
                _page = res.data['currentPage'];
                _hasNextPage = res.data['hasNextPage'];
                _hasPreviousPage = res.data['hasPreviousPage'];
                console.log('分页'+ _page);
            }
        });
    }
    getAjax(1);

    //select calendar
    $(".start-end span").click(function () {
        $(this).addClass("active").siblings("span").removeClass("active");
        $(".rec_type li").eq(0).addClass("active").siblings("li").removeClass("active");
        filterChanged();

    });

    // options
    $(".rec-type li").click(function () {
        $(this).addClass("active").siblings("li").removeClass("active");
        getAjax(1);

    });

    //define calendar
    $('.apply-btn').click(function () {
        getAjax(1);
    })

    function filterChanged() {
        var _year = oYear,
            _month = oMonth,
            _today = oToday,
            _days = $(".start-end span.active").attr('day');
        if(_days){
            if(_days < oToday){
                if(parseInt(_days) == 1){
                    _month = oMonth+1;
                    _today = oToday;

                }else{

                    _today = oToday - _days;

                }
            }else if(_days == oToday) {

                _today = 1;

            }else{
                if(_days == 180){ //  six month
                    if(oMonth+1>6){
                        _month = parseInt(oMonth)-5;

                    }else{
                        _month = 12+oMonth-5;
                        _year = oYear -1;
                    }

                }else if (_days == 30){ // one month
                    if(oMonth+1 == 2){
                        _month = 12;
                        _year = oYear -1;
                    }else{
                        _month = oMonth;
                    }
                }else{ //一周
                    if(oMonth+1 == 2){
                        _month = 12;
                        _year = oYear -1;

                    }else{
                        _month = oMonth;
                        _year = oYear ;

                    }
                    _days = _days || 0;
                    oToday = oToday || 0;
                    _today = 30+ oToday - _days;
                }
            }
            $('#daterangepicker').val((_year + '-' + _month + '-' + _today) + '~' + (oYear + '-' + parseInt(oMonth + 1) + '-' + oToday));

        }else{
            $('#daterangepicker').val('');
        }
        getAjax(1);
    }


    //点击翻页按钮
    $('body').on('click','.prevbtn',function(){
        // prevbtn button
        if(_hasPreviousPage){
            _page--;
            getAjax(_page);
            $('.prevbtn').removeAttr('disabled');
        }else{
            $('.prevbtn').attr('disabled','true');
        }
    });
    $('body').on('click','.nextbtn',function(){
        // nextbtn button
        if(_hasNextPage){
            _page++;
            getAjax(_page);
            $('.nextbtn').removeAttr('disabled');
        }else{
            $('.nextbtn').attr('disabled','true');
        }
    });

})