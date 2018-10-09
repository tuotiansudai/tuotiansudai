require('webStyle/investment/transfer_detail.scss');
require('webJsModule/coupon_alert');
//出借计算器和意见反馈
require('webJsModule/red_envelope_float');
require('publicJs/login_tip');
let commonFun= require('publicJs/commonFun');

//安心签业务
let anxinModule = require('webJsModule/anxin_signed');
var $transferForm = $('#transferForm');
var $transferDetailCon = $('#transferDetailCon'),
    $errorTip = $('.errorTip', $transferDetailCon),
    $questionList = $('.question-list', $transferDetailCon),
    $detailRecord = $('.detail-record', $transferDetailCon),
    $isAnxinAuthenticationRequired = $('#isAnxinAuthenticationRequired');
let isEstimate = $transferDetailCon.data('estimate');

//风险等级是否超出
let avalibableMoney = $transferDetailCon.data('available-invest-money');

let investAmount= $transferDetailCon.data('price');

let userLevel = $transferDetailCon.data('estimate-level');
let loanLevel = $transferDetailCon.data('loan-estimate-level');
let isOverLevel = userLevel<loanLevel;
// let isOverLevel = true;
//可用额度是否超出
let isOverQuota = avalibableMoney<investAmount*100;

// let isOverQuota = true;


$detailRecord.find('li').on('click', function() {
    var $this = $(this),
        num = $this.index();
    $this.addClass('active').siblings('li').removeClass('active');
    $('.detail-record-info', $transferDetailCon).eq(num).show().siblings('.detail-record-info').hide();

});

function showInputErrorTips(message) {
    layer.msg(message);
}

if ($errorTip.length > 0 && $errorTip.text() != '') {
    showInputErrorTips($errorTip.text());
}

$('#transferSubmit').on('click', function(event) {
    event.preventDefault();
    if ($('.header-login').data('wechat-login-name')) {
        location.href = '/login?redirect=' + location.href;
        return;
    }
    $.when(commonFun.isUserLogin())
        .fail(function() {
            //判断是否需要弹框登陆
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: ['auto', 'auto'],
                content: $('#loginTip')
            });
        })
        .done(function() {
            submitData();
        });
});

function submitData() {
    var transferApplicationId = parseInt($("#transferApplicationId").val()),
        transferAmount = $("#amount").val(),
        userBalance = $("#userBalance").val(),
        $transferDetail = $('.transfer-detail-content');
    commonFun.useAjax({
        url: '/transfer/' + transferApplicationId + '/purchase-check',
        type: 'GET'
    },function(data) {
        if (data.message == "SUCCESS") {
            layer.open({
                title: '温馨提示',
                btn: ['确定'],
                content: '该项目已被承接，请选择其他项目。',
                btn1: function(index, layero) {
                    layer.closeAll();
                    location.href = "/transfer-list";
                }
            });
        } else if (data.message == "CANCEL") {
            layer.open({
                title: '温馨提示',
                btn: ['确定'],
                content: '该项目已被取消，请选择其他项目。',
                btn1: function(index, layero) {
                    layer.closeAll();
                    location.href = "/transfer-list";
                }
            });
        } else if (data.message == "MULTITERM") {
            layer.open({
                title: '温馨提示',
                btn: ['确定'],
                content: '该项目已被承接或已取消，请选择其他项目。',
                btn1: function(index, layero) {
                    layer.closeAll();
                    location.href = "/transfer-list";
                }
            });
        } else {
            layer.open({
                type: 1,
                closeBtn: 0,
                title: '出借提示',
                shadeClose: false,
                btn: ['取消', '确认'],
                area: ['300px'],
                content: '<p class="pad-m-tb tc">确认出借？</p>',
                btn1: function() {
                    layer.closeAll();
                },
                btn2: function() {

                    if ($transferForm.attr('action') === '/transfer/purchase') {

                        var isInvestor = 'INVESTOR' === $transferDetail.data('user-role');
                        if (!isInvestor) {
                            location.href = '/login?redirect=' + encodeURIComponent(location.href);
                            return false;
                        }

                        var accountAmount = parseInt((userBalance * 100).toFixed(0)) || 0;
                        if (parseInt((transferAmount * 100).toFixed(0)) > accountAmount) {
                            location.href = '/recharge';
                            return false;
                        }
                    }
                    if($isAnxinAuthenticationRequired.val()=='false'){
                        if(!isEstimate){
                            //风险测评
                            layer.open({
                                type: 1,
                                title:false,
                                closeBtn: 0,
                                area: ['400px', '250px'],
                                shadeClose: true,
                                content: $('#riskAssessment')

                            });
                            return false;
                        }else {
                            if(isOverLevel){
                                layer.open({
                                    type: 1,
                                    title:'温馨提示',
                                    closeBtn: 0,
                                    area: ['400px', '250px'],
                                    shadeClose: true,
                                    content: $('#riskGradeForm')

                                });
                                return false;
                            }
                            if(isOverQuota){
                                layer.open({
                                    type: 1,
                                    title:'温馨提示',
                                    closeBtn: 0,
                                    area: ['400px', '250px'],
                                    shadeClose: true,
                                    content: $('#riskBeyondForm')

                                });
                                return false;
                            }

                            layer.open({
                                type: 1,
                                title:'风险提示',
                                closeBtn: 0,
                                area: ['400px', '250px'],
                                shadeClose: true,
                                content: $('#riskTipForm')

                            });
                            // $transferForm.submit();
                        }

                    }else{
                        anxinModule.getSkipPhoneTip();
                        return false;
                    }

                }
            });
        }
    });
}
$questionList.find('dl dd').hide();
$questionList.find('dl dd').eq(0).show();
$questionList.find('dl dt').eq(0).find('i').addClass('fa-chevron-circle-up').removeClass('fa-chevron-circle-down')
$questionList.find('dt').on('click', function(index) {
    var $this = $(this);
    $this.next('dd').toggle();
    if ($this.next('dd').is(':hidden')) {
        $this.find('i').removeClass('fa-chevron-circle-up').addClass('fa-chevron-circle-down');
    } else {
        $this.find('i').addClass('fa-chevron-circle-up').removeClass('fa-chevron-circle-down');
    }
})


//**************************安心签*******************************

//勾选马上出借下方 协议复选框
$('.init-checkbox-style').initCheckbox(function(event) {
    //如果安心签协议未勾选，马上出借按钮需要置灰
    let checkboxBtn = event.children[0];
    let checkBool = $(checkboxBtn).prop('checked');
    if(checkboxBtn.id=='skipCheck') {
        $('#transferSubmit').prop('disabled',!checkBool);
        $('#checkTip').toggle();
    }
});
anxinModule.toAuthorForAnxin(function(data) {

    $('#isAnxinUser').val('true');
    $('.skip-group').hide();
    if(data.data.message=='skipAuth'){
        $isAnxinAuthenticationRequired.val('false');
    }
    $isAnxinAuthenticationRequired.val('false')
    if(!isEstimate){
        //风险测评
        layer.open({
            type: 1,
            title:false,
            closeBtn: 0,
            area: ['400px', '250px'],
            shadeClose: true,
            content: $('#riskAssessment')

        });
        return false;
    }else {
        if(isOverLevel){
            layer.open({
                type: 1,
                title:'温馨提示',
                closeBtn: 0,
                area: ['400px', '250px'],
                shadeClose: true,
                content: $('#riskGradeForm')

            });
            return false;
        }
        if(isOverQuota){
            layer.open({
                type: 1,
                title:'温馨提示',
                closeBtn: 0,
                area: ['400px', '250px'],
                shadeClose: true,
                content: $('#riskBeyondForm')

            });
            return false;
        }

        layer.open({
            type: 1,
            title:'风险提示',
            closeBtn: 0,
            area: ['400px', '250px'],
            shadeClose: true,
            content: $('#riskTipForm')

        });
        // $('#transferForm').submit();
    }



});

var $riskAssessment = $('#riskAssessment');
var $cancelAssessment = $('.cancelAssessment'),
    $confirmAssessment = $('.confirmAssessment'),
    $riskTips = $('#riskTips');

$cancelAssessment.on('click', function(event) {
    event.preventDefault();
    layer.closeAll();
    return false;

});
$confirmAssessment.on('click', function(event) {
    event.preventDefault();
    layer.closeAll();
    location.href = '/risk-estimate'
});
$riskTips.on('mouseover', function(event) {
    event.preventDefault();
    $('.risk-tip-content').show();
});
$riskTips.on('mouseout', function(event) {
    event.preventDefault();
    $('.risk-tip-content').hide();
});

$('.confirmInvest').on('click',function () {
    $('#transferForm').submit();
})

