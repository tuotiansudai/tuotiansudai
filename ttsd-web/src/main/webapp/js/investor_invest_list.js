require(['jquery', 'mustache', 'text!/tpl/investor-invest-table.mustache', 'text!/tpl/investor-invest-repay-table.mustache', 'moment', 'pagination', 'layerWrapper', 'daterangepicker'], function ($, Mustache, investListTemplate, investRepayTemplate, moment, pagination, layer) {
    var today = moment().format('YYYY-MM-DD'), // 今天
        week = moment().subtract(1, 'week').format('YYYY-MM-DD'),
        month = moment().subtract(1, 'month').format('YYYY-MM-DD'),
        sixMonths = moment().subtract(6, 'month').format('YYYY-MM-DD'),
        $dateFilter = $('.date-filter'),
        $statusFilter=$('.status-filter');


    var dataPickerElement = $('#date-picker'),
        paginationElement = $('.pagination');

    dataPickerElement.dateRangePicker({separator: ' ~ '});

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
                            }
                        });
                        var html = Mustache.render(investRepayTemplate, data);

                        layer.open({
                            type: 1,
                            title: false,
                            offset: '80px',
                            area: ['1000px'],
                            shadeClose: true,
                            content: html
                        });

                    }
                });
            });
        });
        $('.invest-list').on('mouseenter','.project-name',function() {
            layer.closeAll('tips');
            if($.trim($(this).text()).length>15){
                layer.tips($(this).text(), $(this), {
                    tips: [1, '#efbf5c'],
                    time: 2000,
                    tipsMore: true,
                    area: 'auto',
                    maxWidth: '500'
                });
            }
        });
        $('.invest-list').on('mouseenter','.birth-icon',function() {
            layer.closeAll('tips');
            var num = parseFloat($(this).attr('data-benefit'));
            var benefit = num + 1;
            layer.tips('您已享受生日福利，首月收益翻'+benefit+'倍', $(this), {
                tips: [1, '#efbf5c'],
                time: 2000,
                tipsMore: true,
                area: 'auto',
                maxWidth: '500'
            });
        });
    };

    changeDatePicker();
    loadLoanData();

    $dateFilter.find(".select-item").click(function () {
        $(this).addClass("current").siblings(".select-item").removeClass("current");
        changeDatePicker();
        loadLoanData();
    });
    $statusFilter.find(".select-item").click(function () {
        $(this).addClass("current").siblings(".select-item").removeClass("current");
        loadLoanData();
    });

    //define calendar
    $('.apply-btn').click(function () {
        loadLoanData();
        $(".date-filter .select-item").removeClass("current");
    });
});
