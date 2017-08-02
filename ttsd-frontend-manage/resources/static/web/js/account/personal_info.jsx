require('webStyle/account/account_common.scss');
require('webStyle/account/personal_info.scss');
let commonFun= require('publicJs/commonFun');
let ValidatorObj= require('publicJs/validator');
var $InfoBox = $('#personInfoBox'),
    $changeEmailLayer = $('.setEmail', $InfoBox),
    $updateBankCard = $('#update-bank-card'),
    countTimer,
    $riskTip=$('#riskTip'),
    $closeRisk=$('.close-risk',$riskTip);





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
        $turnOffNoPasswordInvestDOM = $('#turnOffNoPasswordInvestDOM'),
        $btnTurnOnElement = $('.btn-turn-on',$turnOnNoPasswordInvestDOM),
        $noPasswordInvest = $('.setNoPasswordInvest',$InfoBox),
        $noPasswordInvestDOM = $('#noPasswordInvestDOM');
    let $imageCaptchaElement = $('#imageCaptcha'),
        $getCaptchaElement = $('.get-captcha',$turnOffNoPasswordInvestDOM);

    let $imageCaptchaTextElement = $('.image-captcha-text', $turnOffNoPasswordInvestDOM),
        $btnCancelElement = $('.btn-cancel',$turnOffNoPasswordInvestDOM),
        $btnCloseTurnOnElement = $('.btn-close-turn-on',$turnOnNoPasswordInvestDOM),
        $btnCloseTurnOffElement = $('.btn-close-turn-off', $turnOffNoPasswordInvestDOM),
        turnOffNoPasswordInvestForm= globalFun.$('#turnOffNoPasswordInvestForm'),
        $codeNumber = $('.code-number',$(turnOffNoPasswordInvestForm)),
        imageCaptchaForm = globalFun.$('#imageCaptchaForm');
    let errorBox = $('.error-box',$(turnOffNoPasswordInvestForm));

    $btnCancelElement.on('click',function(){
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

    $turnOffNoPasswordInvestLayer.on('click', function () {
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
                    content: $turnOffNoPasswordInvestDOM
                });
            }
        });
    });

    $btnTurnOnElement.on('click',function(){
        layer.closeAll();
        layer.open({
            type: 1,
            closeBtn:0,
            move: false,
            offset: "200px",
            title: '免密投资',
            area: ['490px', '220px'],
            shadeClose: true,
            content: $noPasswordInvestDOM
        });
    });

    //开启
    $noPasswordInvest.on('click', function () {
        var _this = $(this);
        commonFun.useAjax({
            url: _this.data('url'),
            type: 'POST'
        },function(response) {
            if (response.data.status) {
                location.href = "/personal-info";
            }
        });
    });

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

    $getCaptchaElement.on('click',function(event){
        // $(imageCaptchaForm).submit();
        event.preventDefault();
        $getCaptchaElement.prop('disabled',true);
        commonFun.useAjax({
            url:"/no-password-invest/send-captcha",
            type:'POST',
            data:$(imageCaptchaForm).serialize()
        },function(response) {
            $getCaptchaElement.prop('disabled',false);
            let data =response.data;
            errorBox.html('');
            if (data.status && !data.isRestricted) {
                $codeNumber.removeClass('code-number-hidden');
                var seconds = 60;
                countTimer = setInterval(function () {
                    $getCaptchaElement.html(seconds + '秒后重新发送').prop('disabled',true);
                    if (seconds == 0) {
                        clearInterval(countTimer);
                        $getCaptchaElement.html('重新发送').prop('disabled',false);
                        commonFun.refreshCaptcha(globalFun.$('#imageCaptcha'),'/no-password-invest/image-captcha');
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
    });

    $imageCaptchaElement.click(function () {
        $imageCaptchaTextElement.val('');
        commonFun.refreshCaptcha(this,'/no-password-invest/image-captcha');
    });

    $('#readUmpayPass').on('click', function () {
        layer.closeAll();
    });

    let turnOffPassValidator = new ValidatorObj.ValidatorForm();
    //免密投资验证图形码
    turnOffPassValidator.newStrategy(turnOffNoPasswordInvestForm.captcha,'isNoPasswordCaptchaVerify',function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this,
            _arguments=arguments;
        let turnOffForm=globalFun.$('#turnOffNoPasswordInvestForm');
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

    turnOffPassValidator.add(turnOffNoPasswordInvestForm.captcha, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入验证码'
    },{
        strategy: 'equalLength:6',
        errorMsg: '验证码格式不正确'
    },{
        strategy: 'isNoPasswordCaptchaVerify',
        errorMsg: '验证码不正确'
    }]);

    $(turnOffNoPasswordInvestForm.captcha).on('blur',function(event) {
        let errorMsg = turnOffPassValidator.start(this);
        errorBox.html(errorMsg);

    });

    turnOffNoPasswordInvestForm.onsubmit=function(event) {
        event.preventDefault();
        let thisForm = this;
        let errorMsg = turnOffPassValidator.start(thisForm.captcha);
        errorBox.html(errorMsg);
        if (!errorMsg) {
            $(thisForm).find(':submit').prop('disabled', true);
            commonFun.useAjax({
                url: "/no-password-invest/disabled",
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
    //验证原密码是否存在
    passValidator.newStrategy(changePasswordForm.originalPassword,'isNotExistPassword',function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this,
            _arguments=arguments;
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:'/personal-info/password/'+this.value+'/is-exist'
        },function(response) {
            if(response.data.status) {
                // 如果为true说明密码存在有效
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
    passValidator.add(changePasswordForm.originalPassword, [{
        strategy: 'isNonEmpty',
        errorMsg: '请输入原密码'
    }, {
        strategy: 'checkPassword',
        errorMsg: '密码为6位至20位，不能全是数字'
    },{
        strategy: 'isNotExistPassword',
        errorMsg: '原密码不正确'
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




