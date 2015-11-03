require(['jquery', 'mustache', 'text!/tpl/fundtable.tpl', 'moment', 'daterangepicker'], function ($, Mustache, dealtableTpl, moment) {
    //初始化页面
    var today = moment().format('YYYY-MM-DD'); // 今天
    var week = moment().subtract(1, 'week').format('YYYY-MM-DD');
    var month = moment().subtract(1, 'month').format('YYYY-MM-DD');
    var sixMonths = moment().subtract(6, 'month').format('YYYY-MM-DD');
    var _page;  //define pages

    var dataPickerElement = $('#date-picker'),
        paginationElement = $('.pagination');

    dataPickerElement.dateRangePicker({separator: ' ~ '}).val(today + '~' + today);

    var changeDatePicker = function () {
        var duration = $(".date-filter .select-item.current").data('day');
        switch (duration) {
            case 1:
                dataPickerElement.val(today + '~' + today);
                break;
            case 7:
                dataPickerElement.val(week + '~' + today);
                break;
            case 30:
                dataPickerElement.val(month + '~' + today);
                break;
            case 180:
                dataPickerElement.val(sixMonths + '~' + today);
                break;
            default:
                dataPickerElement.val('');
        }
    };

    $(".date-filter .select-item").click(function () {
        $(this).addClass("current").siblings(".select-item").removeClass("current");
        changeDatePicker();
        loadLoanData();
    });

    $(".status-filter .select-item").click(function () {
        $(this).addClass("current").siblings(".select-item").removeClass("current");
        loadLoanData();
    });

    //define calendar
    $('.apply-btn').click(function () {
        loadLoanData();
    });


    var loadLoanData = function (currentPage) {
        var dates = dataPickerElement.val().split('~');
        var startTime = $.trim(dates[0]) || '';
        var endTime = $.trim(dates[1]) || '';
        var status = $('.status-filter .select-item.current').data('status');

        var requestData = {startTime: startTime, endTime: endTime, status: status, index: currentPage || 1};

        paginationElement.loadPagination(requestData, function (data) {
            var html = Mustache.render(investListTemplate, data);
            $('.invest-list').html(html);
            $('.invest-list .show-invest-repay').click(function () {
                $.ajax({
                    url: $(this).data('url'),
                    type: 'get',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).success(function (response) {
                    var data = response.data;
                    data.isLoanCompleted = status == 'COMPLETE';
                    data.csrfToken = $("meta[name='_csrf']").attr("content");
                    if (data.status) {
                        _.each(data.records, function (item) {
                            data.loanId = item.loanId;
                            switch (item.loanRepayStatus) {
                                case 'REPAYING':
                                    item.status = '待还';
                                    break;
                                case 'COMPLETE':
                                    item.status = '完成';
                                    break;
                                case 'CANCEL':
                                    item.status = '流标';
                                    break;
                                case 'CONFIRMING':
                                    item.status = '确认中';
                                    break;
                            }
                        });
                        var html = Mustache.render(investRepayTemplate, data);

                        layer.open({
                            type: 1,
                            title: false,
                            offset: '100px',
                            area: ['1000px'],
                            shadeClose: true,
                            content: html
                        });

                    }
                });
            });
        });
    };

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