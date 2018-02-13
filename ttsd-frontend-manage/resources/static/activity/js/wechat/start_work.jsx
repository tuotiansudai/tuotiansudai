require('activityStyle/wechat/start_work.scss');
let commonFun = require('publicJs/commonFun');
let ifClickBtn = false;
let sourceKind = globalFun.parseURL(location.href);
(function (doc, win) {
    var docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            var clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            var fSize = 20 * (clientWidth /375);
            fSize > 40 && (fSize = 39.36);
            docEl.style.fontSize = fSize + 'px';
        };

    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
    $('body').css('visibility', 'visible');
})(document, window);

let isGet = $('.container').data('get');
let isSuc = $('.container').data('success');
let startTime = $('.container').data('startTime');
let overTime = $('.container').data('overTime');
let activityTime = new Date(startTime.replace(/-/g, "/")).getTime(); // 活动开始时间
let activityOverTime = new Date(overTime.replace(/-/g, "/")).getTime();  // 活动结束时间

if (!isGet) {
    $('.get_it_btn').show();
}
else {
    $('.got_it_btn').show();
}

if (isSuc) {
    $('.pop_modal_container').show();
}
else if (isSuc === false) {
    $('.pop_modal_container_again').show();
}

$('.get_it_btn').on('click',function () {
    if (!ifClickBtn) {
        ifClickBtn = true;
        let currentTime = new Date().getTime();
        if (currentTime < activityTime) {
            layer.msg('活动未开始');
        }
        else if (currentTime > activityOverTime) {
            layer.msg('活动已结束');
        }
        else {
            $.when(commonFun.isUserLogin())
                .done(function () {
                    location.href = '/activity/start-work/draw';
                })
                .fail(function () {
                    ifClickBtn = false;
                    toLogin();
            })
        }
    }

});

$('.closeBtn').on('click',function () {
    $('.modal_container').hide();
});

$('.see_my_redPocket').on('click',function () {
   location.href = '/my-treasure';
});

//去登录
function toLogin() {
    if (sourceKind.params.source == 'app') {
        location.href = "/login";
    }else {
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['auto', 'auto'],
            content: $('#loginTip')
        });
    }
}


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
                    window.location.reload();
                } else {
                    commonFun.refreshCaptcha(globalFun.$('#imageCaptcha'), "/login/captcha");
                    globalFun.removeClass(thisButton,'loading');
                    errorDom.text(data.message);
                }
            });

        }
    };

},'login_tip');



