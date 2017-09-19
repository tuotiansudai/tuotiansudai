require('webStyle/account/transfer_apply_detail.scss');
require('webJsModule/coupon_alert');

require('webJsModule/anxin_agreement');
let ValidatorObj= require('publicJs/validator');
let commonFun= require('publicJs/commonFun');

var createForm =globalFun.$('#createForm'),
    $agreement = $(createForm).find('.agreement'),
    $isAnxinAuthenticationRequired=$('#isAnxinAuthenticationRequired');

let validator = new ValidatorObj.ValidatorForm();
let $tipText=$('#tipText'),
    minTip = parseFloat($tipText.attr('data-min')),
    maxTip = parseFloat($tipText.attr('data-max'));

let $getSkipPhone = $('#getSkipPhone');

validator.add(createForm.price, [{
    strategy: 'isNonEmpty',
    errorMsg: '转让价格'
},{
    strategy: 'minValue:'+minTip,
    errorMsg: '转让价格最小为'+minTip
},{
    strategy: 'maxValue:'+maxTip,
    errorMsg: '转让价格最大为'+maxTip
}]);

$(createForm.price).on('keyup',function(event) {

    let errorMsg = validator.start(this);
    if(errorMsg) {

    }
});

createForm.onsubmit = function(event) {
    event.preventDefault();
    let errorMsg;
    errorMsg = validator.start(createForm.price);
    if(errorMsg) {
        return;
    }
    applyTip();
}

function applyTip(){
    layer.open({
        type: 1,
        title: '温馨提示',
        btn: ['确定', '取消'],
        area: ['330px'],
        shadeClose: true,
        content: '<p class="pad-m tc">是否确认转让？</p>',
        btn1: function () {
            var transferInvestId = $('#transferInvestId').val();
            commonFun.useAjax({
                url: '/transfer/invest/' + transferInvestId + '/is-transferable',
                type: 'GET'
            },function(data) {
                if (true == data.data.status) {
                    if($isAnxinAuthenticationRequired.val()=='false'){
                        sendData();
                        layer.closeAll();
                    }else{
                        if($('#isAnxinUser').val() == 'true'){
                            getSkipPhoneTip();
                        }else{
                            $('#skipCheck').val() == 'true'?getSkipPhoneTip():$agreement.next('span.error').show();;
                        }
                        return false;
                    }

                } else {
                    layer.msg(data.message);
                }
            });
        },
        btn2: function () {
            layer.closeAll();
        }
    });
}

function sendData() {
    commonFun.useAjax({
        url: '/transfer/apply',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'transferAmount': parseFloat($('#transferAmount').val()) * 100,
            'transferInvestId': $('#transferInvestId').val()
        }),
        beforeSend: function (data) {
            $(createForm).find('button[type="submit"]').prop('disabled', true);
        }
    },function(data) {
        $(createForm).find('button[type="submit"]').prop('disabled', false);
        if (data == true) {
            let $successTip = $('#successTip');
            layer.open({
                title: '温馨提示',
                btn: 0,
                area: ['400px'],
                content: $successTip.html(),
                success: function (layero, index) {
                    let $countTime = $successTip.find('.count-time');
                    commonFun.countDownLoan({
                        btnDom:$countTime,
                        time:3
                    },function() {
                        window.location.href = '/transferrer/transfer-application-list/TRANSFERRING';
                    });
                }
            });
        } else {
            layer.msg('申请失败，请重试！');
        }
    });
}

$agreement.find('.fa').on('click', function () {
    var $this = $(this).parent(),
        className;
    $this.toggleClass('checked');
    if ($this.hasClass('checked')) {
        className = 'fa fa-check-square';
        $this.next('span.error').hide();
        $('#skipCheck').length>0?$('#skipCheck').val('true'):false;
        $(createForm).find('button[type="submit"]').prop('disabled', false);
    }
    else {
        className = 'fa fa-square-o';
        $('#skipCheck').length>0?$('#skipCheck').val('false'):false;
        $(createForm).find('button[type="submit"]').prop('disabled', true);
    }
    $this.find('i')[0].className = className;
});
$('#cancleBtn').on('click', function (event) {
    event.preventDefault();
    history.go(-1);
});

$('.init-checkbox-style').initCheckbox();


//show phone code tip
function getSkipPhoneTip(){
    layer.open({
        shadeClose: false,
        title: '安心签代签署授权',
        btn: 0,
        type: 1,
        area: ['400px', 'auto'],
        content: $('#getSkipPhone')
    });
}

let $getSkipCode=$('#getSkipCode'),
    $microPhone=$('#microPhone');


$getSkipPhone.on('click',function(event) {
    let $target=$(event.target),
        targetId = event.target.id;
    if(targetId=='getSkipCode') {
        // 短信验证码
        event.preventDefault();
        getCode(false);
    } else if(targetId=='microPhone') {
        // 语音验证码
        getCode(true);
    } else if(targetId=='getSkipBtn') {
        //去授权,安心签授权弹框表单提交

        let $skipPhoneCode = $('#skipPhoneCode'),
            $skipError = $('#skipError'),
            skipCode = $skipPhoneCode.val();
        if(!skipCode) {
            $skipError.text('验证码不能为空').show();
            return;
        }
        $target.addClass('active').val('授权中...').prop('disabled', true);
        commonFun.useAjax({
            url: '/anxinSign/verifyCaptcha',
            type: 'POST',
            data: {
                captcha: $('#skipPhoneCode').val(),
                skipAuth:$('#tipCheck').val()
            }
        },function(data) {
            $target.removeClass('active').val('立即授权').prop('disabled', false);
            if(data.success){
                $('#isAnxinUser').val('true') && $('.skip-group').hide();
                if(data.skipAuth=='true'){
                    $isAnxinAuthenticationRequired.val('false');
                }
                skipSuccess();
            }else{
                $skipError.text('验证码不正确').show();
            }
        });
    }
});

function getCode(type){
    $('#getSkipCode').prop('disabled',true);
    $('#microPhone').css('visibility', 'hidden');
    commonFun.useAjax({
        url: '/anxinSign/sendCaptcha',
        type: 'POST',
        data:{
            isVoice:type
        }
    },function(data) {
        $('#getSkipCode').prop('disabled',false);
        $('#microPhone').css('visibility', 'visible');
        if(data.success) {
            commonFun.countDownLoan({
                btnDom:$getSkipCode,
                isAfterText:'重新获取验证码'
            },function() {
                $microPhone.css('visibility', 'visible');
            });
        }
        else {
            layer.msg('请求失败，请重试或联系客服！');
        }
    });
}

//skip success
function skipSuccess(){
    layer.closeAll();
    $('#skipSuccess').show();
    setTimeout(function(){
        $('#skipSuccess').hide();
        $('#skipPhoneCode').val('');
        sendData();
    },3000)
}

$('#skipPhoneCode').on('keyup', function(event) {
    event.preventDefault();
    $(this).val()!=''?$('#skipError').text('').hide():$('#skipError').text('验证码不能为空').show();;
});

