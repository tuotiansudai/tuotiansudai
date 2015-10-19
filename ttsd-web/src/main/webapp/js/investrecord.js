require(['jquery', 'mustache', 'text!../../tpl/invest-record-table.mustache', 'moment', 'daterangepicker'], function ($, Mustache, dealtableTpl, moment) {
    //初始化页面
    var _now_day =  moment().format('YYYY-MM-DD'); // 今天
    var _week =  moment().subtract(1, 'week').format('YYYY-MM-DD');
    var _month =  moment().subtract(1, 'month').format('YYYY-MM-DD');
    var _sixMonth =  moment().subtract(6, 'month').format('YYYY-MM-DD');
    var _page;  //define pages

    console.log('今天'+_now_day+'周：'+_week +'月：'+_month +'半年：'+_sixMonth)
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
            rec_typestr = "&loanStatus=" + selectedType;
        }
        if (startDay == '' || startDay == 'undefined') {
            var url = API_AJAX + "?index=" + page + rec_typestr;
        } else {
            var url = API_AJAX + "?startTime=" + startDay + "&endTime=" + endDay + "&index=" + page + rec_typestr;
        }

        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8'
        }).done(function (res) {
            var ret = Mustache.render(dealtableTpl, res);
            $('#tpl').html(ret);
            _page = res.data.index;

            $('#tpl .plan').click(function () {
                var dataRepay = $(this).attr('data-repay');
                var str = '';
                $.ajax({
                    url: dataRepay,
                    type: 'get',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (res) {
                    console.log(res);
                    if (res.data.status) {
                        var _res = res.data.records;
                        for (var i = 0; i < _res.length; i++) {
                            // 序号	应回款时间	应回款金额	应收本金	应收利息	利息管理费
                            // 实收总额	实收利息	实收罚息	实扣利息管理费	状态
                            str += "<tr><td>" +
                                (i+1)
                                + "</td><td>" +
                                _res[i]['repayDate']
                                + "</td><td>" +
                                _res[i]['amount']
                                + "</td><td>" +
                                _res[i]['corpus']
                                + "</td><td>" +
                                _res[i]['expectedInterest']
                                + "</td><td>" +
                                _res[i]['expectedFee']
                                + "</td>";
                            if(_res[i]['status'] == 'COMPLETE') {
                                str += "<td>" +
                                    _res[i]['actualAmount']
                                    + "</td><td>" +
                                    _res[i]['actualInterest']
                                    + "</td><td>" +
                                    _res[i]['defaultInterest']
                                    + "</td><td>" +
                                    _res[i]['actualFee']
                                    + "</td><td>" +
                                    '完成'
                                    + "</td></tr>";
                            }else{
                                str += "<td> - </td><td> - </td><td> - </td><td> - </td><td>还款中</td></tr>";
                            }
                        }
                        $('.table-list tbody').find('tr').remove();
                        $('.table-list tbody').append(str);
                    }
                });
                $('.layer-box').show();
                return false;

            });

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
            getAjax(1);
        }
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

    //还款计划
    $('.layer-box .close').click(function () {
        $('.layer-box').hide();
        return false;
    });
    $('.layer-fix').click(function () {
        $('.layer-box').hide();
    });

});
