require('webStyle/account/account_common.scss');
require('webStyle/account/ump_account.scss');
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');
var $InfoBox = $('#personInfoBox'),
    $changeEmailLayer = $('.setEmail', $InfoBox),
    $updateBankCard = $('#update-bank-card'),
    countTimer,
    $riskTip=$('#riskTip'),
    $closeRisk=$('.close-risk',$riskTip),
    $voiceCaptcha = $('#voice_captcha');







//修改绑定的银行卡
$updateBankCard.on('click', function(){
    var url = $(this).data('url');
    commonFun.useAjax({
        url: '/bind-card/is-replacing',
        type: 'GET'
    },function(data) {
        if (data) {
            layer.open({
                type: 1,
                title:false,
                area: ['400px', '120px'],
                btn:['确定'],
                shadeClose: true,
                content: '<p class="tc" style="margin-top:20px;">您已经提交了更换银行卡申请，请耐心等待结果。</p>',
                btn1:function(){
                    layer.closeAll();
                }
            });
        } else {
            commonFun.useAjax({
                url: '/bind-card/is-manual',
                type: 'GET'
            },function() {
                if (data) {
                    layer.open({
                        type: 1,
                        title:false,
                        area: ['600px', '180px'],
                        btn:['我已联系客服，确认更换'],
                        shadeClose: true,
                        content: '<p class="tc" style="margin-top:20px;line-height:20px">您的账户余额或待收本息不为0，为了您的资金安全，请先联系客服<br />并提交相关材料后再点击更换，否则无法更换成功。</p><p class="tc" style="margin-top:20px;line-height:20px">客服电话：400-169-1188（服务时间：9:00－20:00）</p>',
                        btn1:function(){
                            location.href = url;
                        }
                    });
                } else {
                    location.href = url;
                }
            });
        }
    });

});


//支付密码
(function(){
    let $resetUmpayPassDOM = $('#resetUmpayPassDOM');
    let resetUmpayPasswordForm = globalFun.$('#resetUmpayPasswordForm');
    let $resetUmpayPasswordLayer = $('.setUmpayPass', $InfoBox);
    let errorDom=$('.error-box',$resetUmpayPassDOM);
    let $successUmpayPass = $('#successUmpayPass');
    $resetUmpayPasswordLayer.on('click', function() {
        layer.open({
            type: 1,
            move: false,
            offset: "200px",
            title: '修改支付密码',
            area: ['500px', '300px'],
            shadeClose: false,
            content: $resetUmpayPassDOM,
            cancel: function () {
                resetUmpayPasswordForm.reset();
                errorDom.css('visibility','hidden');
            }
        });
    });

    //修改支付密码表单验证
    let umpayValidator = new ValidatorObj.ValidatorForm();
    umpayValidator.add(resetUmpayPasswordForm.identityNumber, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入身份证'
    }, {
        strategy: 'identityValid',
        errorMsg: '请输入有效身份证'
    }]);

    $(resetUmpayPasswordForm.identityNumber).on('blur',function() {
        let errorMsg = umpayValidator.start(this);
        if(errorMsg) {
            errorDom.text(errorMsg).css('visibility','visible');
        }
        else {
            errorDom.text('').css('visibility','hidden');
        }
    });

    resetUmpayPasswordForm.onsubmit = function(event) {
        event.preventDefault();
        let UmpayForm=this;
        $(UmpayForm).find(':submit').prop('disabled',true);
        if($(UmpayForm.identityNumber).hasClass('valid')) {
            commonFun.useAjax({
                url:"/personal-info/reset-umpay-password",
                type:'POST',
                data:$(UmpayForm).serialize()
            },function(response) {
                var data = response.data;
                $(UmpayForm).find(':submit').prop('disabled',false);
                if (data.status) {
                    layer.closeAll();
                    layer.open({
                        type: 1,
                        move: false,
                        offset: "200px",
                        title: '修改支付密码',
                        area: ['500px', '300px'],
                        shadeClose: false,
                        content: $successUmpayPass
                    });
                } else {
                    errorDom.text('您输入的身份证号与当前账号不符，请重新输入。').css('visibility','visible');
                }
            });
        }
    }
})();

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
    var status = $('.status-filter .select-item.current').data('status').split(',');

    var requestData = {startTime: startTime, endTime: endTime, status: status, index: currentPage || 1};
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





