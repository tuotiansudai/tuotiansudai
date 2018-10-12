require('webStyle/account/transfer_apply_detail.scss');
require('webJsModule/coupon_alert');

require('webJsModule/anxin_agreement');
let ValidatorObj= require('publicJs/validator');
let commonFun= require('publicJs/commonFun');
//安心签业务
let anxinModule = require('webJsModule/anxin_signed');

var createForm =globalFun.$('#createForm'),
    $agreement = $(createForm).find('.agreement'),
    $isAnxinAuthenticationRequired=$('#isAnxinAuthenticationRequired');

let validator = new ValidatorObj.ValidatorForm();
let $tipText=$('#tipText'),
    minTip = parseFloat($tipText.attr('data-min')),
    maxTip = parseFloat($tipText.attr('data-max'));

let $getSkipPhone = $('#getSkipPhone');

createForm.onsubmit = function(event) {
    event.preventDefault();
    applyTip();
};

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
                            anxinModule.getSkipPhoneTip();
                        }else{
                            $('#skipCheck').val() == 'true'?anxinModule.getSkipPhoneTip():$agreement.next('span.error').show();
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
            'transferAmount': parseFloat($('#transferAmount').html()) * 100,
            'transferInvestId': $('#transferInvestId').val()
        }),
        beforeSend: function (data) {
            // $(createForm).find('button[type="submit"]').prop('disabled', true);
        }
    },function(data) {
        // $(createForm).find('button[type="submit"]').prop('disabled', false);
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


//**************************安心签*******************************

//勾选马上投资下方 协议复选框
$('.init-checkbox-style').initCheckbox();

anxinModule.toAuthorForAnxin(function(data) {

    $('#isAnxinUser').val('true');
    $('.skip-group').hide();
    if(data.skipAuth=='true'){
        $isAnxinAuthenticationRequired.val('false');
    }
    sendData();

});

