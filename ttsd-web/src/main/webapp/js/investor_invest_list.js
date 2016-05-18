require(['jquery','template', 'mustache', 'text!/tpl/investor-invest-repay-table.mustache', 'moment', 'pagination', 'layerWrapper', 'daterangepicker', 'jquery.ajax.extension'], function ($,tpl, Mustache, investRepayTemplate, moment, pagination, layer) {
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
            console.log(data);
            $('#investList').html(tpl('investListTpl', data));
        });
    };
    $('body').on('click','.show-invest-repay',function (event) {
        event.preventDefault();
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
        event.stopPropagation();
    })
    .on('mouseenter','.project-name',function() {
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
    })
    .on('mouseenter','.birth-icon',function() {//birth icon event
        layer.closeAll('tips');
        var num = parseFloat($(this).data('benefit'));
        var benefit = num + 1;
        layer.tips('您已享受生日福利，首月收益翻'+ benefit +'倍', $(this), {
            tips: [1, '#efbf5c'],
            time: 2000,
            tipsMore: true,
            area: 'auto',
            maxWidth: '500'
        });
    })
    .on('mouseenter','.coupon-icon',function() {//coupon icon event
        layer.closeAll('tips');
        var num = parseFloat($(this).data('benefit')).toFixed(0);
        layer.tips('您使用了' + num + '%加息券', $(this), {
            tips: [1, '#efbf5c'],
            time: 2000,
            tipsMore: true,
            area: 'auto',
            maxWidth: '500'
        });
    })
    .on('mouseenter','.money-icon',function() {//money icon event
        layer.closeAll('tips');
        var num = parseFloat($(this).data('benefit')).toFixed(0);
        layer.tips('您使用了' + num + '元红包', $(this), {
            tips: [1, '#efbf5c'],
            time: 2000,
            tipsMore: true,
            area: 'auto',
            maxWidth: '500'
        });
    })
    .on('mouseenter','.newbie-icon',function() {//ticket icon event
        layer.closeAll('tips');
        var num = parseFloat($(this).data('benefit')).toFixed(0);
        layer.tips('您使用了' + num + '元体验金', $(this), {
            tips: [1, '#efbf5c'],
            time: 2000,
            tipsMore: true,
            area: 'auto',
            maxWidth: '500'
        });
    })
    .on('mouseenter','.ticket-icon',function() {//ticket icon event
        layer.closeAll('tips');
        var num = parseFloat($(this).data('benefit')).toFixed(0);
        layer.tips('您使用了' + num + '元体验金', $(this), {
            tips: [1, '#efbf5c'],
            time: 2000,
            tipsMore: true,
            area: 'auto',
            maxWidth: '500'
        });
    });

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
