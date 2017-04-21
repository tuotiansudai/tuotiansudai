require(['jquery', 'layerWrapper','commonFun','validator'], function($,layer,commonFun,validator) {

    //如果是pc页面需要调整padding值
    (function() {
        var redirect=globalFun.browserRedirect();
        if(redirect=='pc') {
            var wh=$(document).width(),
                pad=0.55*wh;
            $('.register-section-box').css("padding-top",pad);
        }
    })();

    $(function() {
        var $redEnvelopReferrer=$('#redEnvelopReferrer');
        var $redEnvelopSplit=$('#redEnvelopSplit');
        //会员发红包
        (function () {
            if(!$redEnvelopSplit.length) {
                return;
            }
            $('.envelop-box',$redEnvelopSplit).eq(1).hide();

            $('.envelop-button span',$redEnvelopSplit).on('click',function() {
                var $this=$(this),
                    thisNum=$this.index();

                $this.addClass('active').siblings('span').removeClass('active');
                $('.envelop-box',$redEnvelopSplit).eq(thisNum).show().siblings('.envelop-box').hide();
            });

            //立即邀请好友赚红包，如果url连接上有source=share,不需要跳转
            //
            $('.to-invite-friend',$redEnvelopSplit).on('click',function() {
                var $this=$(this),
                    url=$this.data('url');
                var locationUrl=location.href;
                var parseURL=globalFun.parseURL(locationUrl);
                if(parseURL.params.source=='share') {
                    layer.msg('本活动仅限使用拓天速贷app参加，<br/>请下载app后再进行邀请。', {offset: ['', '10%']});
                }
                else {
                    location.href=url;
                }
            });
        })();

        //被邀请人注册
        //第一步，输入手机号
        (function() {
            var phoneForm=globalFun.$('#phoneForm');
            if(!phoneForm) {
                return;
            }
            var validatorMobile = new validator();
            var locationUrl=location.href;
            var parseURL=globalFun.parseURL(locationUrl),
                channel=parseURL.params.channel,
                loginName=parseURL.params.loginName;

            phoneForm.loginName.value=loginName;
            phoneForm.channel.value=channel;

            validatorMobile.add(phoneForm.mobile, [{
                strategy: 'isNonEmpty',
                errorMsg: '手机号不能为空'
            }, {
                strategy: 'isMobile',
                errorMsg: '手机号格式不正确'
            },{
                strategy: 'isMobileExist',
                errorMsg: '手机号已存在'
            }]);

            phoneForm.onsubmit = function(event) {
                event.preventDefault();
                var errorMsg = validatorMobile.start(phoneForm.mobile);
                if(errorMsg) {
                    layer.msg(errorMsg);
                    return;
                }
                else {
                    phoneForm.submit();
                }
            }
        })();

        //第二步：获取验证码注册
        (function() {
            var registerForm=globalFun.$('#registerForm');
            var $fetchCaptcha=$(registerForm).find('.btn-captcha');
            if(!$(registerForm).length) {
                return;
            }
            //弹出注册服务协议
            $('.agreement',$(registerForm)).on('click',function() {
                layer.open({
                    type: 1,
                    title: '拓天速贷服务协议',
                    shadeClose: true,
                    move: false,
                    scrollbar: true,
                    skin:'register-skin',
                    content: $('#agreementBox')
                });

            });

            var validatorRegister = new validator();
            validatorRegister.add(registerForm.captcha, [{
                strategy: 'isNonEmpty',
                errorMsg: '验证码不能为空'
            },{
                strategy: 'isNumber:6',
                errorMsg: '验证码为6位数字'
            }]);

            validatorRegister.add(registerForm.password, [{
                strategy: 'isNonEmpty',
                errorMsg: '密码不能为空'
            }, {
                strategy: 'checkPassword',
                errorMsg: '密码为6位至20位，不能全是数字'
            }]);

            //获取手机验证码
            $fetchCaptcha.on('click',function(event) {
                var $this=$(this);
                var mobile=registerForm.mobile.value;
                event.preventDefault();
                $this.prop('disabled',true);

                var ajaxOption={
                        url: '/register/user/'+mobile+'/send-register-captcha',
                        type: 'get',
                        dataType: 'json'
                    };
                commonFun.useAjax(ajaxOption,function(responseData) {
                    $fetchCaptcha.prop('disabled',false);
                    //刷新验证码
                    var data = responseData.data;
                    if (data.status && !data.isRestricted) {
                        //获取手机验证码成功，关闭弹框，并开始倒计时
                        commonFun.countDownLoan({
                            btnDom:$fetchCaptcha
                        });

                    } else if (!data.status && data.isRestricted) {
                        layer.msg('短信发送频繁，请稍后再试');

                    } else if (!data.status && !data.isRestricted) {
                        layer.msg('图形验证码不正确');
                    }
                });

            });

            registerForm.onsubmit = function(event) {
                event.preventDefault();
                var reInputs=$(registerForm).find('input:visible');
                var errorMsg;

                for(var i=0,len=reInputs.length;i<len;i++) {
                    errorMsg = validatorRegister.start(reInputs[i]);
                    if(errorMsg) {
                        layer.msg(errorMsg);
                        break;
                    }
                }
                if (!errorMsg) {
                    commonFun.useAjax({
                        url: '/activity/red-envelop-split/user-register',
                        type:'POST',
                        data:$(registerForm).serialize()
                    },function(responseData) {
                        if(responseData) {
                            var $stepTwo=$('.step-two',$redEnvelopReferrer);
                            var $stepThree=$('.step-three',$redEnvelopReferrer);
                            $stepTwo.hide();
                            $stepThree.show();
                            $('.message-tip',$redEnvelopReferrer).hide();
                        }
                        else {
                            layer.msg('验证码不正确');
                        }
                    });
                }
            };
        })();

        //第三步：下载APP提现
        (function() {
            var downloadApp=globalFun.$('#downloadApp');
            if(!downloadApp) {
                return;
            }
            globalFun.addEventHandler(downloadApp,'click',globalFun.toExperience.bind(globalFun));
        })();
    })
});