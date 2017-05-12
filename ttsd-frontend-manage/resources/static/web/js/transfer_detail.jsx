require('webStyle/investment/transfer_detail.scss');
require('webJsModule/coupon_alert');
//投资计算器和意见反馈
require('webJsModule/red_envelope_float');
require('publicJs/login_tip');
let commonFun= require('publicJs/commonFun');
//安心签协议
require('webJsModule/anxin_agreement');

var $transferDetailCon = $('#transferDetailCon'),
    $errorTip = $('.errorTip', $transferDetailCon),
    $questionList = $('.question-list', $transferDetailCon),
    $detailRecord = $('.detail-record', $transferDetailCon),
    $isAnxinAuthenticationRequired = $('#isAnxinAuthenticationRequired');

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
                title: '投资提示',
                shadeClose: false,
                btn: ['取消', '确认'],
                area: ['300px'],
                content: '<p class="pad-m-tb tc">确认投资？</p>',
                btn1: function() {
                    layer.closeAll();
                },
                btn2: function() {
                    var $transferForm = $('#transferForm');
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
                        $transferForm.submit();
                    }else{
                        getSkipPhoneTip();
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


//loan click checkbox
$('.skip-group .skip-icon').on('click', function(event) {
    event.preventDefault();

    $(this).hasClass('active') ? $(this).removeClass('active') && $('#skipCheck').val('false') && $('#checkTip').show() && $('#transferSubmit').prop('disabled', true) : $(this).addClass('active') && $('#skipCheck').val('true') && $('#checkTip').hide() && $('#transferSubmit').prop('disabled', false);
});

//skip tip click chechbox
$('.tip-item .skip-icon').on('click', function(event) {
    event.preventDefault();
    $(this).hasClass('active') ? $(this).removeClass('active') && $('#tipCheck').val('false') : $(this).addClass('active') && $('#tipCheck').val('true');
});

//show phone code tip
function getSkipPhoneTip() {
    layer.open({
        shadeClose: false,
        title: '安心签代签署授权',
        btn: 0,
        type: 1,
        area: ['400px', 'auto'],
        content: $('#getSkipPhone')
    });
}

var num = 60,
    Down;

//get phone code
$('#getSkipCode').on('click', function(event) {
    event.preventDefault();
    getCode(false);
});

//get phone code yuyin
$('#microPhone').on('click', function(event) {
    event.preventDefault();
    getCode(true);
});

function getCode(type) {
    $('#getSkipCode').prop('disabled',true);
    $('#microPhone').css('visibility', 'hidden');
    commonFun.useAjax({
        url: '/anxinSign/sendCaptcha',
        type: 'POST',
        data: {
            isVoice: type
        }
    },function (data) {
        $('#getSkipCode').prop('disabled',false);
        $('#microPhone').css('visibility', 'visible');
        if(data.success) {
            countDown();
            Down = setInterval(countDown, 1000);
        }
        else {
            layer.msg('请求失败，请重试或联系客服！');
        }
    });
}
//countdown skip
function countDown() {
    $('#getSkipCode').val(num + '秒后重新获取').prop('disabled', true);
    $('#microPhone').css('visibility', 'hidden');
    if (num == 0) {
        clearInterval(Down);
        $('#getSkipCode').val('重新获取验证码').prop('disabled', false);
        $('#microPhone').css('visibility', 'visible');
        num=60;
    }else{
        num--;
    }
}
//submit data skip phone code
$('#getSkipBtn').on('click', function(event) {
    event.preventDefault();
    var $self = $(this);
    if ($('#skipPhoneCode').val() != '') {
        $self.addClass('active').val('授权中...').prop('disabled', true);
        commonFun.useAjax({
            url: '/anxinSign/verifyCaptcha',
            type: 'POST',
            data: {
                captcha: $('#skipPhoneCode').val(),
                skipAuth: $('#tipCheck').val()
            }
        },function (data) {
            $self.removeClass('active').val('立即授权').prop('disabled', false);
            if(data.success){
                $('#isAnxinUser').val('true');
                if(data.data.message=='skipAuth'){
                    $isAnxinAuthenticationRequired.val('false');
                }
                $('.skip-group').hide();
                skipSuccess();
            }else{
                $('#skipError').text('验证码不正确').show();
            }
        });

    } else {
        $('#skipError').text('验证码不能为空').show();
    }
});

//skip success
function skipSuccess() {
    layer.closeAll();
    $('#skipSuccess').show();
    setTimeout(function() {
        $('#skipSuccess').hide();
        $('#skipPhoneCode').val('');
        num=0;
        $('#transferForm').submit();
    }, 3000)
}

$('#skipPhoneCode').on('keyup', function(event) {
    event.preventDefault();
    $(this).val() != '' ? $('#skipError').text('').hide() : $('#skipError').text('验证码不能为空').show();;
});