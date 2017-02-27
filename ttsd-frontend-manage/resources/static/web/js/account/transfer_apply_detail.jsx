require('webStyle/account/transfer_apply_detail.scss');
require('webJsModule/coupon_alert');

require('webJsModule/anxin_agreement');
let ValidatorForm= require('publicJs/validator');
let commonFun= require('publicJs/commonFun');

var createForm =globalFun.$('#createForm'),
    $agreement = $(createForm).find('.agreement'),
    $isAnxinAuthenticationRequired=$('#isAnxinAuthenticationRequired');

let validator = new ValidatorForm();
let $tipText=$('#tipText'),
    minTip = parseFloat($tipText.attr('data-min')),
    maxTip = parseFloat($tipText.attr('data-max'));

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
            layer.open({
                title: '温馨提示',
                btn: 0,
                area: ['400px', '150px'],
                content: $('#successTip').html(),
                success: function (layero, index) {
                    setInterval(function () {
                        if ($('.layui-layer-content .count-time').text() < 2) {
                            window.location.href = '/transferrer/transfer-application-list/TRANSFERRING';
                        } else {
                            $('.layui-layer-content .count-time').text(function (index, num) {
                                return parseInt(num) - 1
                            });
                        }
                    }, 1000);
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

//skip tip click chechbox
$('.tip-item .skip-icon').on('click', function(event) {
    event.preventDefault();
    $(this).hasClass('active')?$(this).removeClass('active') && $('#tipCheck').val('false'):$(this).addClass('active')&& $('#tipCheck').val('true');
});


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

var num = 60,Down;

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
    $('#getSkipCode').val(num + '秒后重新获取').prop('disabled',true);
    $('#microPhone').css('visibility', 'hidden');
    if (num == 0) {
        clearInterval(Down);
        $('#getSkipCode').val('重新获取验证码').prop('disabled',false);
        $('#microPhone').css('visibility', 'visible');
        num=60;
    }else{
        num--;
    }

}
//submit data skip phone code
$('#getSkipBtn').on('click',  function(event) {
    event.preventDefault();
    var $self=$(this);
    if($('#skipPhoneCode').val()!=''){
        commonFun.useAjax({
            url: '/anxinSign/verifyCaptcha',
            type: 'POST',
            data: {
                captcha: $('#skipPhoneCode').val(),
                skipAuth:$('#tipCheck').val()
            }
        },function(data) {
            $self.removeClass('active').val('立即授权').prop('disabled', false);
            if(data.success){
                $('#isAnxinUser').val('true') && $('.skip-group').hide();
                if(data.skipAuth=='true'){
                    $isAnxinAuthenticationRequired.val('false');
                }
                skipSuccess();
            }else{
                $('#skipError').text('验证码不正确').show();
            }
        });
    }else{
        $('#skipError').text('验证码不能为空').show();
    }
});

//skip success
function skipSuccess(){
    layer.closeAll();
    $('#skipSuccess').show();
    setTimeout(function(){
        $('#skipSuccess').hide();
        $('#skipPhoneCode').val('');
        num=0;
        sendData();
    },3000)
}

$('#skipPhoneCode').on('keyup', function(event) {
    event.preventDefault();
    $(this).val()!=''?$('#skipError').text('').hide():$('#skipError').text('验证码不能为空').show();;
});

