//投资记录--直投项目
require('webStyle/account/loan_list.scss');
require('publicJs/plugins/daterangepicker.scss');
require('publicJs/pagination');
let moment = require('moment');
let tpl = require('art-template/dist/template');
require('publicJs/plugins/jquery.daterangepicker-0.0.7.js');

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
            $('#investList').html(tpl('investListTpl', data));
        });

};
$('body').on('click','.show-invest-repay',function (event) {
    event.preventDefault();
    var isBank = $(this).data('isbank');
    $.ajax({
        url: $(this).data('url'),
        type: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8'
    }).success(function (response) {
        var data = response.data;
        data.isBank = isBank;
        data.isLoanCompleted = status == 'COMPLETE';
        data.csrfToken = $("meta[name='_csrf']").attr("content");
        if (data.status) {
            _.each(data.records, function (item) {
                data.loanId = item.loan ? item.loan.id : '';

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
            //获取模版内容
            let $investRepayTemplate=$('#investRepayTemplate'),
                investTpl=$investRepayTemplate.html();

            let experienceTpl=$('#investExperienceRepayTemplate').html();
            let tplHtml = (data.loanId == 1) ? experienceTpl : investTpl;

            // 解析模板, 返回解析后的内容
            var render = _.template(tplHtml);
            var html = render(data);
            // 将解析后的内容填充到渲染元素
            layer.open({
                type: 1,
                title: '回款详情',
                skin:'repay-layer-pop',
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
    var number = parseFloat($(this).data('benefit'));
    var num = number == number.toFixed(0) ? number.toFixed(0) : number.toFixed(1);
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
    var num =$(this).data('benefit');
    layer.tips('您使用了' + num + '元投资红包', $(this), {
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
})
.on('mouseenter','.first-icon',function() {//first icon event
    layer.closeAll('tips');
    layer.tips('我获得了0.2%加息券和50元投资红包', $(this), {
        tips: [1, '#efbf5c'],
        time: 2000,
        tipsMore: true,
        area: 'auto',
        maxWidth: '500'
    });
})
.on('mouseenter','.max-icon',function() {//max icon event
    layer.closeAll('tips');
    layer.tips('我获得了0.5%加息券和100元投资红包', $(this), {
        tips: [1, '#efbf5c'],
        time: 2000,
        tipsMore: true,
        area: 'auto',
        maxWidth: '500'
    });
})
.on('mouseenter','.last-icon',function() {//last icon event
    layer.closeAll('tips');
    layer.tips('我获得了0.2%加息券和50元投资红包', $(this), {
        tips: [1, '#efbf5c'],
        time: 2000,
        tipsMore: true,
        area: 'auto',
        maxWidth: '500'
    });
    })
    .on('mouseenter','.coupon',function() {//last icon event
        var messge = $(this).data('benefit');
        layer.closeAll('tips');
        layer.tips(messge, $(this), {
            tips: [1, '#ff7200'],
            time: 2000,
            tipsMore: true,
            area: 'auto',
            maxWidth: '500'
        });
    })
    .on('mouseenter','.fee',function() {//last icon event
        var messge = $(this).data('benefit');
        layer.closeAll('tips');
        layer.tips(messge, $(this), {
            tips: [1, '#ff7200'],
            time: 2000,
            tipsMore: true,
            area: 'auto',
            maxWidth: '500'
        });
    })
    .on('mouseenter','.repay',function() {//last icon event
        var messge = $(this).data('benefit');
        layer.closeAll('tips');
        layer.tips(messge, $(this), {
            tips: [1, '#ff7200'],
            time: 2000,
            tipsMore: true,
            area: 'auto',
            maxWidth: '500'
        });
    })
    .on('mouseenter', '.extra-rate', function () {//extra-rate icon event
        var num = $(this).data('benefit');
        layer.closeAll('tips');
        layer.tips('投资奖励' + num + '%', $(this), {
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
