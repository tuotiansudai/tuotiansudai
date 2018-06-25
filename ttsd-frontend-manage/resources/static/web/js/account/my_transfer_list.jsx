//投资记录--转让项目
require('webStyle/account/loan_list.scss');
require('publicJs/plugins/daterangepicker.scss');
let moment = require('moment');
let commonFun= require('publicJs/commonFun');
require('publicJs/pagination');
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
            //获取模版内容
            let $investListTemplate=$('#investListTemplate'),
                investListTpl=$investListTemplate.html();
            // 解析模板, 返回解析后的内容
            let render = _.template(investListTpl);
            let html = render(data);

            // 将解析后的内容填充到渲染元素
            $('.invest-list').html(html);

            $('.invest-list .show-invest-repay').click(function () {
                commonFun.useAjax({
                    url: $(this).data('url'),
                    type: 'GET'
                },function(response) {
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

                        //获取模版内容
                        let $investRepayTemplate=$('#investRepayTemplate'),
                            investTpl=$investRepayTemplate.html();

                        let elementInvestRepay = $('#elementInvestRepay');

                        // 解析模板, 返回解析后的内容
                        var render = _.template(investTpl);
                        var html = render(data);
                        // 将解析后的内容填充到渲染元素
                        elementInvestRepay.html(html);

                        layer.open({
                            type: 1,
                            title: '回款详情',
                            skin:'repay-layer-pop',
                            //offset: '80px',
                            area: ['1000px'],
                            shadeClose: true,
                            content: elementInvestRepay
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
    $('body').on('mouseenter','.coupon',function() {//last icon event
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

let metaViewPort = $('meta[name=viewport]');//
metaViewPort.remove()
$('head').prepend($('<meta name="viewport" content="width=1024,user-scalable=yes" />'));