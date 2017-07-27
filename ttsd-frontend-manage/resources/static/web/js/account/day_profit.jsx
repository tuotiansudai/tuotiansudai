//资金管理
require('webStyle/account/loan_list.scss');
require('publicJs/plugins/daterangepicker.scss');
let moment = require('moment');
require('publicJs/pagination');
require('publicJs/plugins/jquery.daterangepicker-0.0.7.js');

//初始化页面
var today = moment().format('YYYY-MM-DD'); // 今天
var week = moment().subtract(1, 'week').format('YYYY-MM-DD');
var month = moment().subtract(1, 'month').format('YYYY-MM-DD');
var sixMonths = moment().subtract(6, 'month').format('YYYY-MM-DD');

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
var loadLoanData = function (currentPage) {
    var dates = dataPickerElement.val().split('~');
    var startTime = $.trim(dates[0]) || '';
    var endTime = $.trim(dates[1]) || '';

    var requestData = {startTime: startTime, endTime: endTime, index: currentPage || 1};
        paginationElement.loadPagination(requestData, function (data) {
            //获取模版内容
            let $userTemplate=$('#userBillTableTemplate'),
                userTpl=$userTemplate.html();
            // 解析模板, 返回解析后的内容
            let render = _.template(userTpl);
            let html = render(data);

            $('.user-bill-list').html(html);
        });
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

//define calendar
$('.apply-btn').click(function () {
    loadLoanData();
    $(".date-filter .select-item").removeClass("current");
});

loadLoanData();
