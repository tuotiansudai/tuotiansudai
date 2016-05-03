require(['underscore', 'jquery', 'jquery.validate', 'jquery.validate.extension', 'jquery.ajax.extension'], function (_, $) {

    var registerAccountForm = $('.register-step-two .register-account-form');

    registerAccountForm.validate({
        focusCleanup: true,
        focusInvalid: false,
        onfocusout: function (element) {
            this.element(element);
        },
        submitHandler: function (form) {
            $('.register-account').toggleClass('loading');
            form.submit();
        },
        onkeyup: function (element, event) {
            var excludedKeys = [16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];

            if ((event.which !== 9 || this.elementValue(element) !== "") && $.inArray(event.keyCode, excludedKeys) === -1) {
                this.element(element);
            }
        },
        rules: {
            userName: {
                required: true
            },
            identityNumber: {
                required: true,
                regex: /^[1-9]\d{13,16}[a-zA-Z0-9]$/,
                isExist: "/register/account/identity-number/{0}/is-exist"
            }
        },
        messages: {
            userName: {
                required: "请输入姓名"
            },
            identityNumber: {
                required: '请输入身份证',
                regex: '身份证格式不正确',
                isExist: "身份证已存在"
            }
        }
    });
});

var registerAccountCnzzAddress = [
    {
        'function':'/recharge',
        'category':'75实名认证页',
        'action':'认证',
        'label':'充值'
    },
    {
        'function':'/account',
        'category':'76实名认证页',
        'action':'认证',
        'label':'提现'
    },
    {
        'function':'/bind-card',
        'category':'77实名认证页',
        'action':'认证',
        'label':'绑卡'
    },
    {
        'function':'/loan',
        'category':'78实名认证页',
        'action':'认证',
        'label':'马上投资'
    },
    {
        'function':'/point',
        'category':'79实名认证页',
        'action':'认证',
        'label':'签到'
    },
    {
        'function':'/auto-invest/agreement',
        'category':'80实名认证页',
        'action':'认证',
        'label':'授权自动投标'
    },
    {
        'function':'/personal-info',
        'category':'82实名认证页',
        'action':'认证',
        'label':'开启免密'
    }
];

function getReferrer() {
    var referrer = '';
    try {
        referrer = window.top.document.referrer;
    } catch(e) {
        if(window.parent) {
            try {
                referrer = window.parent.document.referrer;
            } catch(e2) {
                referrer = '';
            }
        }
    }
    if(referrer === '') {
        referrer = document.referrer;
    }
    return referrer;
};

function statisticsCnzzByRegister(){
    var referrer = getReferrer();
    for(var index in registerAccountCnzzAddress){
        if(referrer.indexOf(registerAccountCnzzAddress[index].function) != -1){
            cnzzPush.trackClick(registerAccountCnzzAddress[index].category,registerAccountCnzzAddress[index].action,registerAccountCnzzAddress[index].label);
            break;
        }
    }
}
