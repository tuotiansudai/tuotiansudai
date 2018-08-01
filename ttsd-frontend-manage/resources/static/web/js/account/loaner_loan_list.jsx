//投资记录--我的借款
require('webStyle/account/loan_list.scss');
require('publicJs/plugins/daterangepicker.scss');
let moment = require('moment');
let commonFun= require('publicJs/commonFun');
require('publicJs/pagination');
require('publicJs/plugins/jquery.daterangepicker-0.0.7.js');

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
            data.isRepaying = false;
            data.isComplete = false;
            data.isCancel = false;
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
            let render = _.template($('#loanListTemplate').html());
            let html = render(data);
            $('.loan-list-content .loan-list').html(html);

            $('.loan-list .show-loan-repay').click(function () {
                var isBank = $(this).data('isbank');
                commonFun.useAjax({
                    url: $(this).data('url'),
                    type: 'GET',
                    contentType: 'application/json; charset=UTF-8'
                },function(response) {
                    var data = response.data;
                    data.csrfToken = $("meta[name='_csrf']").attr("content");
                    if (data.status) {
                        data.isLoanCompleted = _.every(data.records, function(item) {
                            return item.loanRepayStatus === 'COMPLETE';
                        });
                        _.each(data.records, function (item) {
                            switch (item.loanRepayStatus) {
                                case 'REPAYING':
                                    item.status = item.isEnabled ? '还款' : '待还';
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
                                    item.status = item.isEnabled ? '逾期还款' : '逾期';
                                    break;
                            }
                        });

                        let renderRepay = _.template($('#loanRepayTemplate').html());
                        let html = renderRepay(data);

                        layer.open({
                            type: 1,
                            title: false,
                            offset: 'auto',
                            area: ['850px'],
                            shadeClose: true,
                            content: html
                        });

                        if (data.isNormalRepayEnabled) {
                            $('a.normal-repay').click(function () {
                                 if (parseFloat(data.loanerBalance) < parseFloat(data.normalRepayAmount)) {
                                    showBalanceNotEnoughAlert(data.loanerBalance, data.normalRepayAmount,isBank);
                                    return false;
                                 }

                                $("#normal-repay-form").submit();
                                layer.closeAll();
                                return false;
                            });
                        }

                        if (data.isAdvanceRepayEnabled) {
                            $('a.advanced-repay').click(function () {
                                if (parseFloat(data.loanerBalance) < parseFloat(data.advanceRepayAmount)) {
                                    showBalanceNotEnoughAlert(data.loanerBalance, data.advanceRepayAmount,isBank);
                                    return false;
                                }
                                $("#advanced-repay-form").submit();
                                layer.closeAll();
                                return false;
                            });
                        }


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

var showBalanceNotEnoughAlert = function (balance, repayAmount,isBank) {
    layer.closeAll();
    let loanCategory ;
    if(isBank){
        loanCategory='富滇银行存管';
    }else {
        loanCategory='联动优势';
    }
    layer.open({
        type: 1,
        closeBtn: 0,
        skin: 'demo-class',
        shadeClose: false,
        title: '账户余额不足',
        btn: ['关闭', '去充值'],
        area: ['400px', '160px'],
        content:`<p class="pad-m-tb tc">应还金额 ${repayAmount}元，您的${loanCategory}账户余额仅有${balance}元</p>`,
        btn1: function () {
            layer.closeAll();
        },
        btn2: function () {
            if (isBank){
                window.location.href = "/recharge";
            }else{
                window.location.href = "/ump/recharge";
            }
        }
    });
}

// require(['jquery', 'mustache', 'text!tpl/loaner-loan-table.mustache', 'text!tpl/loaner-loan-repay-table.mustache', 'moment', 'underscore', 'layerWrapper', 'daterangepicker', 'jquery.ajax.extension', 'pagination'],
//     function ($, Mustache, loanListTemplate, loanRepayTemplate, moment, _, layer) {
//
// });

