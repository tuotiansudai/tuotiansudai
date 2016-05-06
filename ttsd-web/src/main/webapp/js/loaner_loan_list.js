require(['jquery', 'mustache', 'text!tpl/loaner-loan-table.mustache', 'text!tpl/loaner-loan-repay-table.mustache', 'moment', 'underscore', 'layerWrapper', 'daterangepicker', 'jquery.ajax.extension', 'pagination'], function ($, Mustache, loanListTemplate, loanRepayTemplate, moment, _, layer) {
    //初始化页面
    var today = moment().format('YYYY-MM-DD'); // 今天
    var week = moment().subtract(1, 'week').format('YYYY-MM-DD');
    var month = moment().subtract(1, 'month').format('YYYY-MM-DD');
    var sixMonths = moment().subtract(6, 'month').format('YYYY-MM-DD');

    // 页面初始化日期 条件筛选1个月
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
    $(".date-filter .select-item").eq(2).trigger('click');
    $(".status-filter .select-item").click(function () {
        $(this).addClass("current").siblings(".select-item").removeClass("current");
        loadLoanData();
    });

    //ajax require
    function loadLoanData(currentPage) {
        var dates = dataPickerElement.val().split('~');
        var startTime = $.trim(dates[0]) || '';
        var endTime = $.trim(dates[1]) || '';
        var status = $('.status-filter .select-item.current').data('status');

        var requestData = {startTime: startTime, endTime: endTime, status: status, index: currentPage || 1};

        paginationElement.loadPagination(requestData, function (data) {
            switch (status) {
                case 'REPAYING':
                    data.isRepaying = true;
                    break;
                case 'COMPLETE':
                    data.isComplete = true;
                    break;
                case 'CANCEL':
                    data.isCancel = true;
                    break;
            }
            var html = Mustache.render(loanListTemplate, data);
            $('.loan-list-content .loan-list').html(html);

            $('.loan-list .show-loan-repay').click(function () {
                $.ajax({
                    url: $(this).data('url'),
                    type: 'get',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).success(function (response) {
                    var data = response.data;
                    data.isLoanCompleted = status == 'COMPLETE' || _.every(data.records, function(item) {
                            return item.loanRepayStatus === 'COMPLETE';
                        });
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
                                case 'WAIT_PAY':
                                    item.status = '等待支付';
                                    break;
                                case 'OVERDUE':
                                    item.status = item.isEnabled ? '待还' : '逾期';
                                    break;
                            }
                        });
                        data.expectedAdvanceRepayDisplayAmount = data.expectedAdvanceRepayAmount / 100;
                        var html = Mustache.render(loanRepayTemplate, data);

                        layer.open({
                            type: 1,
                            title: false,
                            offset: 'auto',
                            area: ['850px'],
                            shadeClose: true,
                            content: html
                        });

                        $('a.normal-repay').click(function () {
                            if (data.loanAgentBalance < data.expectedNormalRepayAmount) {
                                $(".repay-alert").html(Mustache.render('实际需还金额{{expectedNormalRepayAmount}}元，您的账户余额仅有{{loanAgentBalance}}元',
                                    {
                                        'expectedNormalRepayAmount': data.expectedNormalRepayAmount / 100,
                                        'loanAgentBalance': data.loanAgentBalance / 100
                                    }
                                )).show();
                                return false;
                            }

                            $("#normal-repay-form").submit();
                            layer.closeAll();
                            return false;
                        });

                        $('a.advanced-repay').click(function () {
                            if (!data.hasWaitPayLoanRepay) {
                                if (data.loanAgentBalance < data.expectedAdvanceRepayAmount) {
                                    $(".repay-alert").html(Mustache.render('实际需还金额{{expectedAdvanceRepayAmount}}元，您的账户余额仅有{{loanAgentDisplayBalance}}元',
                                        {
                                            'expectedAdvanceRepayAmount': data.expectedAdvanceRepayAmount / 100,
                                            'loanAgentDisplayBalance': data.loanAgentBalance / 100
                                        }
                                    )).show();
                                    return false;
                                }

                                $("#advanced-repay-form").submit();
                                layer.closeAll();
                            }
                            return false;
                        });
                    }
                });
            });
        });
    }

    loadLoanData();

//define calendar
    $('.apply-btn').click(function () {
        loadLoanData();
        $(".date-filter .select-item").removeClass("current");
    });
});

