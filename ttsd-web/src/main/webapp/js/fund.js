require(['jquery', 'mustache', 'text!/tpl/fundtable.tpl', 'moment', 'daterangepicker'], function ($, Mustache, dealtableTpl, moment) {
    //初始化页面
    var _now_day = moment().format('YYYY-MM-DD'); // 今天
    var _week = moment().subtract(1, 'week').format('YYYY-MM-DD');
    var _month = moment().subtract(1, 'month').format('YYYY-MM-DD');
    var _sixMonth = moment().subtract(6, 'month').format('YYYY-MM-DD');
    var _page;  //define pages

    // 页面初始化日期 条件筛选1个月
    $('#daterangepicker')
        .dateRangePicker({separator: ' ~ '})
        .val(_now_day + '~' + _now_day);
    //ajax require
    function getAjax(page) {
        var dates = $('#daterangepicker').val().split('~');
        var startDay = $.trim(dates[0]);
        var endDay = $.trim(dates[1]);
        var selectedType = $('.query-type').find(".current").attr('data-value');
        //$(".query_type strong").css("opacity", '1');
        var rec_typestr = '';
        if (selectedType) {
            rec_typestr = "&status=" + selectedType;
        }
        if (startDay == '' || startDay == 'undefined') {
            var url = _API_FUND + "?index=" + page + rec_typestr;
        } else {
            var url = _API_FUND + "?startTime=" + startDay + "&endTime=" + endDay + "&index=" + page + rec_typestr;
        }
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8'
        }).done(function (res) {
            if (selectedType == 'REPAYING') {
                res.step1 = true;
                var _plan = '<a class="re_plan" href="">提前还款</a>';
                $('.jq-re-plan').append(_plan);
            } else if (selectedType == 'COMPLETE') {
                res.step2 = true;

            } else {
                res.step3 = true;
            }

            var ret = Mustache.render(dealtableTpl, res);
            $('#tpl').html(ret);
            _page = res.data.index;
        });
    }

    getAjax(1);

//select calendar
    $(".start-end .jq-n").click(function () {
        $(this).addClass("current").siblings(".jq-n").removeClass("current");
        //$(".rec_type .jq-n").eq(0).addClass("current").siblings(".jq-n").removeClass("current");
        filterChanged();
    });

// options
    $(".query-type .jq-n").click(function () {
        $(this).addClass("current").siblings(".jq-n").removeClass("current");
        getAjax(1);

    });

//define calendar
    $('.apply-btn').click(function () {
        getAjax(1);
    });
    function filterChanged() {
        var _days = $(".start-end span.current").attr('day');
        if (_days) {
            if (_days == 1) {
                $('#daterangepicker').val(_now_day + '~' + _now_day);
            } else if (_days == 7) {
                $('#daterangepicker').val(_week + '~' + _now_day);
            } else if (_days == 30) {
                $('#daterangepicker').val(_month + '~' + _now_day);
            } else {
                //半年的
                $('#daterangepicker').val(_sixMonth + '~' + _now_day);
            }
        } else {
            $('#daterangepicker').val('');
        }
        getAjax(1);
    }


//分页
    $(document).on('click', '.pagination .nextPage', function () {
        _page++;
        getAjax(_page);
    });
    $(document).on('click', '.pagination .prevPage', function () {
        _page--;
        getAjax(_page);
    });


});