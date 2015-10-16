require(['jquery', 'mustache', 'text!/tpl/loaner-loan-table.mustache', 'text!/tpl/loaner-loan-repay-table.mustache', 'moment', 'underscore', 'daterangepicker', 'csrf',], function ($, Mustache, loanTemplate, loanRepayTemplate, moment, _) {
    //初始化页面
    var today = moment().format('YYYY-MM-DD'); // 今天
    var week = moment().subtract(1, 'week').format('YYYY-MM-DD');
    var month = moment().subtract(1, 'month').format('YYYY-MM-DD');
    var sixMonths = moment().subtract(6, 'month').format('YYYY-MM-DD');
    var pageIndex = 1;  //define pages

    // 页面初始化日期 条件筛选1个月
    var dataPickerElement = $('#date-picker');

    dataPickerElement.dateRangePicker({separator: ' ~ '}).val(today + '~' + today);

    $(document).on('click', '.pagination .nextPage', function () {
        pageIndex++;
        loadLoanData(pageIndex);
    });
    $(document).on('click', '.pagination .prevPage', function () {
        pageIndex--;
        loadLoanData(pageIndex);
    });

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

    var layerContainerElement = $('.layer-container');

    $('.layer-mask').click(function () {
        layerContainerElement.hide();
        return false;
    });

    $(".date-filter .select-item").click(function () {
        $(this).addClass("current").siblings(".select-item").removeClass("current");
        changeDatePicker();
        loadLoanData(1);
    });

    $(".status-filter .select-item").click(function () {
        $(this).addClass("current").siblings(".select-item").removeClass("current");
        loadLoanData(1);
    });

    //ajax require
    function loadLoanData(currentPage) {
        var dates = dataPickerElement.val().split('~');
        var startTime = $.trim(dates[0]) || '';
        var endTime = $.trim(dates[1]) || '';
        var status = $('.status-filter .select-item.current').data('status');

        var requestData = { startTime: startTime, endTime: endTime, status: status, index: currentPage || 1};

        var queryParams = '';
        _.each(requestData, function (value, key) {
            queryParams += key + "=" + value + '&';
        });

        $.ajax({
            url: '/loaner/loan-data?' + queryParams,
            type: 'get',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8'
        }).success(function (response) {
            var data = response.data;
            pageIndex = data.index;
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
            var html = Mustache.render(loanTemplate, data);
            $('.loan-content').html(html);

            $('.loan-content .show-loan-repay').click(function () {
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
                        var html = Mustache.render(loanRepayTemplate, data);
                        $('.layer-content').remove();
                        layerContainerElement.append(html).show();
                        $('.layer-container .close').click(function () {
                            layerContainerElement.hide();
                            return false;
                        });
                        $('.layer-container a.enabled-repay.normal').click(function () {
                            layerContainerElement.hide();
                            $("#normal-repay").submit();
                            return false;
                        });
                        $('.layer-container a.enabled-repay.advanced').click(function () {
                            if (data.hasConfirmingLoanRepay) {
                                return false;
                            }
                            layerContainerElement.hide();
                            $("#advanced-repay").submit();
                            return false;
                        });
                    }
                });
            })
        });
    }

    loadLoanData(1);

//define calendar
    $('.apply-btn').click(function () {
        loadLoanData(1);
    });
});

