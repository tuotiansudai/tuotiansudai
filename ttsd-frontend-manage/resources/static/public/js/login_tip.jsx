import {layer} from 'publicJs/wrapper_layer';
import {useAjax,refreshCaptcha,isUserLogin} from 'publicJs/common';
import {ValidatorForm} from 'publicJs/validator';

var $loginTipBox=$('#loginTip');
let loginInForm = document.getElementById('loginInForm');
let errorDom=$(loginInForm).find('.error-box');
//刷新验证码
$('#imageCaptcha').on('click',function() {
    refreshCaptcha(this,'/login/captcha');
    loginInForm.captcha.value='';
});

function popLoginTip() {
    //判断是否登陆，如果没有登陆弹出登录框
    $.when(isUserLogin())
        .fail(function(){
            console.log('未登陆');
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: ['auto', 'auto'],
                content: $('#loginTip')
            });
            $('.close-btn',$loginTipBox).on('click',function(event) {
                event.preventDefault();
                layer.closeAll();
            })
        });
}

//验证表单
let validator = new ValidatorForm();
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
        useAjax({
            url:'/login',
            type:'POST',
            data:dataParam
        },function(data) {
            if (data.status) {
                window.location.reload();
            } else {
                refreshCaptcha(loginInForm.imageCaptcha,'/login/captcha');
                globalFun.removeClass(thisButton,'loading');
                errorDom.text(data.message);
            }
        });

    }
};

export default popLoginTip;