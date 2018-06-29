require('webStyle/account/account_common.scss');
require('webStyle/account/personal_info.scss');
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');
var $InfoBox = $('#personInfoBox'),
    $changeEmailLayer = $('.setEmail', $InfoBox),
    $updateBankCard = $('#update-bank-card'),
    countTimer,
    $riskTip=$('#riskTip'),
    $closeRisk=$('.close-risk',$riskTip),
    $voiceCaptcha = $('#voice_captcha');


var u = navigator.userAgent;
var isInWeChat = /(micromessenger|webbrowser)/.test(u.toLocaleLowerCase());
var isIos = /(iPhone|iPad|iPod|iOS)/i.test(u);
if (isInWeChat && isIos) {
    $('#get_authorization').removeAttr('target');
}

//判断是否显示评估弹框
commonFun.useAjax({
    url: '/risk-estimate/alert',
    type: 'GET'
},function(data) {
    if(data.data.status){
        layer.open({
            type: 1,
            title:false,
            closeBtn: 0,
            area: ['600px', '310px'],
            shadeClose: true,
            content: $riskTip
        });
    }
});

$closeRisk.on('click', function(event) {
    event.preventDefault();
    layer.closeAll();
});

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

//绑定邮箱
require.ensure([],function() {
    let validatorEmail = new ValidatorObj.ValidatorForm();
    let $changeEmailDOM = $('#changeEmailDOM'),
        changeEmailForm=globalFun.$('#changeEmailForm');
    let errorDom=$('.error-box',$changeEmailDOM);
    $changeEmailLayer.on('click', function (event) {
        if(!/realName/.test(event.target.className)) {
            layer.open({
                type: 1,
                move: false,
                offset: "200px",
                title: '绑定邮箱',
                area: ['490px', '220px'],
                shadeClose: true,
                content: $changeEmailDOM,
                cancel: function () {
                    changeEmailForm.email.value='';
                }
            });
        }
    });
    //绑定邮箱表单校验
    validatorEmail.add(changeEmailForm.email, [{
        strategy: 'isNonEmpty',
        errorMsg: '邮箱不能为空',
    }, {
        strategy: 'isEmail',
        errorMsg: '请输入有效邮箱'
    },{
        strategy: 'isEmailExist',
        errorMsg: '邮箱已经存在'
    }]);

    $(changeEmailForm.email).on('blur',function() {
        let errorMsg=validatorEmail.start(this);
        if(errorMsg) {
            errorDom.text(errorMsg).css('visibility','visible');
        }
        else {
            errorDom.text('').css('visibility','hidden');
        }
    });

    changeEmailForm.onsubmit = function(event) {
        event.preventDefault();

        let errorMsg=validatorEmail.start(changeEmailForm.email);
        if(errorMsg) {
            errorDom.text(errorMsg).css('visibility','visible');
        } else {
            commonFun.useAjax({
                url:"/bind-email",
                type:'POST',
                data:$(changeEmailForm).serialize()
            },function(response) {
                var data = response.data;
                    layer.closeAll();
                    changeEmailForm.email.value='';
                    if (data.status) {
                        layer.open({
                            type: 1,
                            title: '验证邮箱',
                            area: ['500px', '220px'],
                            shadeClose: true,
                            content: $('#change-email-success'),
                            btn: ['返回'],
                            yes: function (index, layero) {
                                layer.close(index);
                            }
                        });
                    } else {
                        layer.msg('邮箱绑定失败，请重试！', {type: 1, time: 2000});
                    }
            });
        }
    }

},'emailBind');

//开启关闭免密投资
require.ensure([],function() {
    let $turnOnNoPasswordInvestLayer = $('.setTurnOnNoPasswordInvest', $InfoBox),
        $turnOffNoPasswordInvestLayer = $('.setTurnOffNoPasswordInvest', $InfoBox),
        $turnOnNoPasswordInvestDOM = $('#turnOnNoPasswordInvestDOM'),
        $turnOffPasswordInvestDOM = $('#turnOffNoPasswordInvestDOM'),//关闭免密支付按钮
        $turnOnNoPasswordInvestIdentifiedDOM = $('#turnOnNoPasswordInvestDOM_identified'),
        $btnTurnOnElement = $('.btn-turn-on',$turnOnNoPasswordInvestDOM),
        $noPasswordInvest = $('.setNoPasswordInvest',$InfoBox),//开启按钮
        $noPasswordInvestDOM = $('#noPasswordInvestDOM');
    let $imageCaptchaElement = $('#imageCaptcha'),
        $getCaptchaElement = $('.get-captcha',$turnOnNoPasswordInvestIdentifiedDOM);

    let $imageCaptchaTextElement = $('.image-captcha-text', $turnOnNoPasswordInvestIdentifiedDOM),
        $btnCancelElement = $('.btn-cancel',$turnOnNoPasswordInvestIdentifiedDOM),
        $btnCancelTurnOffNoPassword = $('.btn-cancel',$turnOffPasswordInvestDOM),//关闭免密支付弹框里面的取消按钮
        $btnCloseTurnOnElement = $('.btn-close-turn-on',$turnOnNoPasswordInvestDOM),
        $btnCloseTurnOffElement = $('.btn-close-turn-off', $turnOnNoPasswordInvestIdentifiedDOM),
        turnOnNoPasswordInvestForm= globalFun.$('#turnOnNoPasswordInvestForm'),
        $codeNumber = $('.code-number',$('#turnOnNoPasswordInvestForm')),
        imageCaptchaForm = globalFun.$('#imageCaptchaForm');
    let errorBox = $('.error-box',$(turnOnNoPasswordInvestForm));
    let $alreadyTurnOffDOM = $('#alreadyTurnOffDOM'),
        $TurnOffPassBtn = $('.btn-success',$alreadyTurnOffDOM);

    $TurnOffPassBtn.on('click',function(){
        layer.closeAll();
        location.href = "/personal-info";
    });
    $btnCancelElement.on('click',function(){
        layer.closeAll();
    });
    $btnCancelTurnOffNoPassword.on('click',function(){
        layer.closeAll();
    });
    $btnCloseTurnOnElement.on('click',function(){
        layer.closeAll();

    });

    function refreshTurnOffNoPasswordInvestLayer(){
        clearInterval(countTimer);
        $getCaptchaElement.html('获取验证码').prop('disabled',true);
        commonFun.refreshCaptcha(globalFun.$('#imageCaptcha'),'/no-password-invest/image-captcha');
        $('.captcha').val('');
        errorBox.html('');
        $codeNumber.addClass('code-number-hidden');
    }

    $turnOnNoPasswordInvestLayer.on('click', function () {
        commonFun.useAjax({
            url: "/checkLogin",
            type: 'GET'
        },function() {
            layer.open({
                type: 1,
                move: false,
                offset: "200px",
                title: '免密投资',
                area: ['490px', '220px'],
                shadeClose: false,
                closeBtn:0,
                content: $turnOnNoPasswordInvestDOM
            });
        });
    });

    //开启免密支付按钮
    $noPasswordInvest.on('click', function () {
        commonFun.useAjax({
            url: "/checkLogin",
            type: 'get'
        },function(response) {
            if (response) {
                refreshTurnOffNoPasswordInvestLayer();
                layer.open({
                    type: 1,
                    move: false,
                    area:'500px',
                    title: '免密投资',
                    closeBtn:0,
                    shadeClose: false,
                    content: $turnOnNoPasswordInvestIdentifiedDOM
                });
            }
        });
    });
//点击联动优势按钮
    $btnTurnOnElement.on('click',function(){
        layer.closeAll();
    });

//关闭免密支付按钮
    $turnOffNoPasswordInvestLayer.on('click', function () {
        commonFun.useAjax({
            url: "/checkLogin",
            type: 'GET'
        },function() {
            layer.open({
                type: 1,
                move: false,
                offset: "200px",
                title: '免密投资',
                area: ['490px', '220px'],
                shadeClose: false,
                closeBtn:0,
                content: $turnOffPasswordInvestDOM
            });
        });
    });
    var $turnOffPasswordBtn = $('.btn-turn-off',$turnOffPasswordInvestDOM);
    $turnOffPasswordBtn.on('click',function(e){

            commonFun.useAjax({
                url: '/no-password-invest/disabled',
                type: 'POST'
            },function(response) {
                if (response.data.status) {
                    // location.href = "/personal-info";
                    layer.open({
                        type: 1,
                        move: false,
                        offset: "200px",
                        title: '免密投资',
                        area: ['490px', '220px'],
                        shadeClose: false,
                        closeBtn:0,
                        content: $alreadyTurnOffDOM
                    });
                }
            });

    })
    //关闭免密投资功能
    let noPassValidator = new ValidatorObj.ValidatorForm();
    noPassValidator.add(imageCaptchaForm.imageCaptcha, [{
        strategy: 'isNonEmpty',
        errorMsg: '用户名不能为空'
    },{
        strategy: 'equalLength:5',
        errorMsg: '图形验证码位数不对'
    }]);

    $(imageCaptchaForm.imageCaptcha).on('blur',function() {
        let errorMsg = noPassValidator.start(this);
        if(errorMsg) {
            $getCaptchaElement.prop('disabled',true);
        }
        else {
            $getCaptchaElement.prop('disabled',false);
        }
    });
    let isVoice = false;

    $('.get-captcha').on('click',function(event){
        // $(imageCaptchaForm).submit();
        event.preventDefault();
        $getCaptchaElement.prop('disabled',true);
        fetchaCountDown();
    });
    $('#voice_btn').on('click',function(event){
        event.preventDefault();
        isVoice = true;
        $getCaptchaElement.prop('disabled',true);
        fetchaCountDown();
        console.log(isVoice);
    });
    function fetchaCountDown() {
        commonFun.useAjax({
            url:"/no-password-invest/send-captcha",
            type:'POST',
            data:$(imageCaptchaForm).serialize()+'&voice='+isVoice
        },function(response) {
            $getCaptchaElement.prop('disabled',false);
            let data =response.data;
            errorBox.html('');
            if (data.status && !data.isRestricted) {
                $codeNumber.removeClass('code-number-hidden');
                $voiceCaptcha.hide();
                var seconds = 60;

                countTimer = setInterval(function () {
                    $getCaptchaElement.html(seconds + '秒后重新发送').prop('disabled',true);
                    if (seconds == 0) {
                        clearInterval(countTimer);
                        $getCaptchaElement.html('重新发送').prop('disabled',false);
                        $voiceCaptcha.show();
                        console.log(isVoice)
                        //commonFun.refreshCaptcha(globalFun.$('#imageCaptcha'),'/no-password-invest/image-captcha');
                    }
                    seconds--;
                }, 1000);

            }

            if (!data.status && data.isRestricted) {
                $codeNumber.addClass('code-number-hidden');
                errorBox.html('短信发送频繁，请稍后再试');
            }

            if (!data.status && !data.isRestricted) {
                $codeNumber.addClass('code-number-hidden');
                errorBox.html('图形验证码不正确');
            }
            commonFun.refreshCaptcha(globalFun.$('#imageCaptcha'),'/no-password-invest/image-captcha');
        });
    }
    $imageCaptchaElement.click(function () {
        $imageCaptchaTextElement.val('');
        commonFun.refreshCaptcha(this,'/no-password-invest/image-captcha');
    });

    $('#readUmpayPass').on('click', function () {
        layer.closeAll();
    });

    let turnOnPassValidator = new ValidatorObj.ValidatorForm();
    //免密投资验证图形码
    turnOnPassValidator.newStrategy(turnOnNoPasswordInvestForm.captcha,'isNoPasswordCaptchaVerify',function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this,
            _arguments=arguments;
        let turnOffForm=globalFun.$('#turnOnNoPasswordInvestForm');
        var _phone = turnOffForm.mobile.value,
            _captcha=turnOffForm.captcha.value;
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:`/no-password-invest/mobile/${_phone}/captcha/${_captcha}/verify`
        },function(response) {
            if(response.data.status) {
                // 如果为true说明手机已存在
                getResult='';
                ValidatorObj.isHaveError.no.apply(that,_arguments);

            }
            else {
                getResult=errorMsg;
                ValidatorObj.isHaveError.yes.apply(that,_arguments);
            }
        });
        return getResult;
    });

    turnOnPassValidator.add(turnOnNoPasswordInvestForm.captcha, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入验证码'
    },{
        strategy: 'equalLength:6',
        errorMsg: '验证码格式不正确'
    },{
        strategy: 'isNoPasswordCaptchaVerify',
        errorMsg: '验证码不正确'
    }]);

    $(turnOnNoPasswordInvestForm.captcha).on('blur',function(event) {
        let errorMsg = turnOnPassValidator.start(this);
        errorBox.html(errorMsg);

    });
    //认证以后开启免密支付提交按钮
    turnOnNoPasswordInvestForm.onsubmit=function(event) {
        event.preventDefault();
        let thisForm = this;
        let errorMsg = turnOnPassValidator.start(thisForm.captcha);
        errorBox.html(errorMsg);
        if (!errorMsg) {
            $(thisForm).find(':submit').prop('disabled', true);
            commonFun.useAjax({
                url: "/no-password-invest/enabled",
                type: 'POST',
                data: $(thisForm).serialize()
            }, function (response) {
                $(thisForm).find(':submit').prop('disabled', false);
                var data = response.data;
                if (data.status) {
                    location.href = "/personal-info";
                }

            });
        }
    }

},'noPasswordInvest');

//修改密码
require.ensure([],function() {
    let $changePassDOM = $('#changePassDOM'),
        changePasswordForm=globalFun.$('#changePasswordForm');
    let $changePasswordLayer = $('.setPass', $InfoBox);
    $changePasswordLayer.on('click', function () {
        layer.open({
            type: 1,
            move: false,
            offset: "200px",
            title: '修改密码',
            area: ['550px', '300px'],
            shadeClose: false,
            content: $changePassDOM,
            cancel: function () {
                changePasswordForm.reset();
            }
        });
    });

    //修改密码表单验证
    let passValidator = new ValidatorObj.ValidatorForm();

    passValidator.add(changePasswordForm.originalPassword, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入原密码'
    }, {
        strategy: 'checkPassword',
        errorMsg: '密码为6位至20位，不能全是数字'
    }],true);

    passValidator.add(changePasswordForm.newPassword, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入新密码'
    }, {
        strategy: 'checkPassword',
        errorMsg: '密码为6位至20位，不能全是数字'
    }],true);

    passValidator.add(changePasswordForm.newPasswordConfirm, [{
        strategy: 'equalTo:#newPassword',
        errorMsg: '与新密码不一致'
    }],true);

    let reInputs=$(changePasswordForm).find('input:password');
    for(let i=0,len=reInputs.length; i<len;i++) {
        reInputs[i].addEventListener("keyup", function() {
            passValidator.start(this);
        })
    }

    changePasswordForm.onsubmit = function(event) {
        event.preventDefault();

        let errorMsg;
        for(let i=0,len=reInputs.length;i<len;i++) {
            errorMsg = passValidator.start(reInputs[i]);
            if(errorMsg) {
                break;
            }
        }
        if (!errorMsg) {
            layer.closeAll();
            commonFun.useAjax({
                url:"/personal-info/change-password",
                type:'POST',
                data:$(changePasswordForm).serialize()
            },function(response) {
                var data = response.data;
                if (data.status) {
                    layer.msg('密码修改成功，请重新登录！', {type: 1, time: 2000}, function(){
                        globalFun.$('#logout-form').submit();
                    });
                } else {
                    layer.msg('密码修改失败，请重试！', {type: 1, time: 2000});
                    changePasswordForm.reset();
                }
            });
        }
    }

},'changePassword');

require.ensure([],function(){
    //出借人和借款人分开
    let $applyBorrowerDOM = $('#applyBorrowerDOM');//申请成为借款人
    let $turnToBorrowerDOM = $('#turnToBorrowerDOM');//切换成借款人
    let $turnToLenderDOM = $('#turnLenderDOM');//切换成投资人
    let $changeRoleBtn = $('.change-role-btn');//切换按钮

    let userRole = $InfoBox.data('user-role');
    let loanerRole = $InfoBox.data('loaner-role');
    let isInvestor = 'INVESTOR'===userRole;
    let isLoaner = 'LOANER'===loanerRole;
    let hasApplyLoaner = $InfoBox.data('apply-already');

    let $applyBorrowerBtn = $('.btn-apply-borrower'),
        $turnToBorrowerBtn = $('.btn-turn-borrower'),
        $turnToLoanerBtn = $('.btn-turn-Lender');

    $changeRoleBtn.on('click',function () {

            if(isInvestor){
                if(hasApplyLoaner){
                    layer.open({
                        type: 1,
                        move: false,
                        offset: "200px",
                        title: '温馨提示',
                        area: ['490px', '220px'],
                        shadeClose: false,
                        closeBtn:0,
                        content: $turnToBorrowerDOM
                    });
                }else {
                    layer.open({
                        type: 1,
                        move: false,
                        offset: "200px",
                        title: '温馨提示',
                        area: ['490px', '220px'],
                        shadeClose: false,
                        closeBtn:0,
                        content: $applyBorrowerDOM
                    });
                }
            }
            if(isLoaner){
                layer.open({
                    type: 1,
                    move: false,
                    offset: "200px",
                    title: '温馨提示',
                    area: ['490px', '220px'],
                    shadeClose: false,
                    closeBtn:0,
                    content: $turnToLenderDOM
                });
            }


    })
    $turnToBorrowerBtn.on('click',function () {
        commonFun.useAjax({
            url: '/XXXXXXXXXXXXXXXXXXX',
            type: 'POST'
        },function(data) {
            if(data.data.status){
                location.reload();
            }
        });
    })
    $turnToLoanerBtn.on('click',function () {
        commonFun.useAjax({
            url: '/XXXXXXXXXXXXXXXXXXX',
            type: 'POST'
        },function(data) {
            if(data.data.status){
                location.reload();
            }
        });
    })
    $('.btn-close').on('click', function () {
        layer.closeAll();
    });


},'changeRole')



