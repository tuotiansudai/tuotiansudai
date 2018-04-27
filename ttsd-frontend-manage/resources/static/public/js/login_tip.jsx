require.ensure([],function() {
    let commonFun= require('publicJs/commonFun');
    let ValidatorObj=require('publicJs/validator');
    var $loginTipBox=$('#loginTip');
    let loginInForm = document.getElementById('loginInForm');
    let errorDom=$(loginInForm).find('.error-box');
    require("publicStyle/module/login_tip.scss");
    //刷新验证码
    $('#imageCaptcha').on('click',function() {
        commonFun.refreshCaptcha(this, "/login/captcha");
        loginInForm.captcha.value='';
    });

    commonFun.refreshCaptcha(globalFun.$('#imageCaptcha'), "/login/captcha", false);

    $('.close-btn',$loginTipBox).on('click',function(event) {
        event.preventDefault();
        layer.closeAll();
    });
//验证表单
    let validator = new ValidatorObj.ValidatorForm();
    validator.add(loginInForm.username, [{
        strategy: 'isNonEmpty',
        errorMsg: '用户名不能为空'
    }]);

    validator.add(loginInForm.password, [{
        strategy: 'isNonEmpty',
        errorMsg: '密码不能为空'
    }, {
        strategy: 'checkPassword',
        errorMsg: '密码为6位至20位，不能全是数字'
    }]);
    validator.add(loginInForm.captcha, [{
        strategy: 'isNonEmpty',
        errorMsg: '验证码不能为空'
    }]);

    let reInputs=$(loginInForm).find('input:text,input:password');

    Array.prototype.forEach.call(reInputs,function(el) {
        globalFun.addEventHandler(el,'blur',function() {
            let errorMsg = validator.start(this);
            if(errorMsg) {
                errorDom.text(errorMsg);
            }
            else {
                errorDom.text('');
            }
        });
    });

    loginInForm.onsubmit = function(event) {
        event.preventDefault();
        let thisButton=$(event.target).find(':submit')[0];
        let errorMsg;
        for(let i=0,len=reInputs.length;i<len;i++) {
            errorMsg = validator.start(reInputs[i]);
            if(errorMsg) {
                errorDom.text(errorMsg);
                return;
            }
        }
        if (!errorMsg) {
            globalFun.addClass(thisButton,'loading');
            //弹框登陆为ajax的form表单提交
            var dataParam = $(loginInForm).serialize();
            commonFun.useAjax({
                url:'/login',
                type:'POST',
                data:dataParam
            },function(data) {
                if (data.status) {
                    window.location.href = location.href+'?time='+((new Date()).getTime());
                } else {
                    commonFun.refreshCaptcha(globalFun.$('#imageCaptcha'), "/login/captcha");
                    globalFun.removeClass(thisButton,'loading');
                    errorDom.text(data.message);
                }
            });

        }
    };

},'login_tip');

