require(['jquery', 'mustache', 'text!/tpl/point-bill-table.mustache', 'moment', 'pagination', 'layerWrapper', 'daterangepicker'], function ($, Mustache, pointBillListTemplate, moment, pagination, layer) {
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

    var loadPointBillData = function (currentPage) {
        var dates = dataPickerElement.val().split('~');
        var startTime = $.trim(dates[0]) || '';
        var endTime = $.trim(dates[1]) || '';
        var businessType = $('.status-filter .select-item.current').data('status');

        var requestData = {startTime: startTime, endTime: endTime, businessType: businessType, index: currentPage || 1};
        paginationElement.loadPagination(requestData, function (data) {
            if(data.status){
                _.each(data.records, function (item) {
                    console.info("item = " + item.businessType)
                    switch (item.businessType) {
                        case 'SIGN_IN':
                            item.businessType = '签到奖励';
                            break;
                        case 'TASK':
                            item.businessType = '任务奖励';
                            break;
                        case 'INVEST':
                            item.businessType = '任务奖励';
                            break;
                        case 'EXCHANGE':
                            item.businessType = '财豆兑换';
                            break;
                    }
                    var html = Mustache.render(pointBillListTemplate, data);
                    $('.point-bill-list').html(html);
                });

            }
        });
    };

    changeDatePicker();
    loadPointBillData();

    $dateFilter.find(".select-item").click(function () {
        $(this).addClass("current").siblings(".select-item").removeClass("current");
        changeDatePicker();
        loadPointBillData();
    });
    $statusFilter.find(".select-item").click(function () {
        $(this).addClass("current").siblings(".select-item").removeClass("current");
        loadPointBillData();
    });

    //define calendar
    $('.apply-btn').click(function () {
        loadPointBillData();
        $(".date-filter .select-item").removeClass("current");
    });
});
