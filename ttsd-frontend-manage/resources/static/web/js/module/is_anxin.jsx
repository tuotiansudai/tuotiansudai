let commonFun= require('publicJs/commonFun');
let $isAuthenticationRequired=$('#isAuthenticationRequired');
//勾选马上投资下方 协议复选框
$('.skip-group .skip-icon').on('click', function(event) {
    event.preventDefault();

    $(this).hasClass('active') ? $(this).removeClass('active') && $('#skipCheck').val('false') && $('#checkTip').show() && $('#transferSubmit').prop('disabled', true) : $(this).addClass('active') && $('#skipCheck').val('true') && $('#checkTip').hide() && $('#transferSubmit').prop('disabled', false);
});

//勾选 安心签弹框中的复选框
$('.tip-item .skip-icon').on('click', function(event) {
    event.preventDefault();
    $(this).hasClass('active') ? $(this).removeClass('active') && $('#tipCheck').val('false') : $(this).addClass('active') && $('#tipCheck').val('true');
});

//弹出安心签弹框
function getSkipPhoneTip() {
    layer.open({
        shadeClose: false,
        title: '安心签代签署授权',
        btn: 0,
        type: 1,
        area: ['400px', 'auto'],
        content: $('#getSkipPhone')
    });
}

//获取短信验证码
$('#getSkipCode').on('click', function(event) {
    event.preventDefault();
    getCode(false);
});

//获取语音验证码
$('#microPhone').on('click', function(event) {
    event.preventDefault();
    getCode(true);
});

function getCode(type) {
    $('#getSkipCode').prop('disabled',true);
    $('#microPhone').css('visibility', 'hidden');
    commonFun.useAjax({
        url: '/anxinSign/sendCaptcha',
        type: 'POST',
        data: {
            isVoice: type
        }
    },function (data) {
        $('#getSkipCode').prop('disabled',false);
        $('#microPhone').css('visibility', 'visible');
        if(data.success) {
        	commonFun.countDownLoan({
                btnDom: $('#getSkipCode'),
                isAfterText: '重新获取验证码'
            },function() {
                $('#microPhone').css('visibility', 'visible');
            });
        }
        else {
            layer.msg('请求失败，请重试或联系客服！');
        }
    });
}

//安心签授权弹框表单提交
$('#getSkipBtn').on('click', function(event) {
    event.preventDefault();
    var $self = $(this);
    if ($('#skipPhoneCode').val() != '') {
        $self.addClass('active').val('授权中...').prop('disabled', true);
        commonFun.useAjax({
            url: '/anxinSign/verifyCaptcha',
            type: 'POST',
            data: {
                captcha: $('#skipPhoneCode').val(),
                skipAuth: $('#tipCheck').val()
            }
        },function (data) {
            $self.removeClass('active').val('立即授权').prop('disabled', false);
            if(data.success){
                $('#isAnxinUser').val('true') && $('.skip-group').hide();
                if(data.skipAuth=='true'){
                    $isAnxinAuthenticationRequired.val('false');
                }
                $('.skip-group').hide();
                skipSuccess();
            }else{
                $('#skipError').text('验证码不正确').show();
            }
        });

    } else {
        $('#skipError').text('验证码不能为空').show();
    }
});

//安心签授权成功弹框
function skipSuccess() {
    layer.closeAll();
    $('#skipSuccess').show();
    setTimeout(function() {
        $('#skipSuccess').hide();
        $('#skipPhoneCode').val('');
        $('#isPage').trigger('click');
    }, 3000)
}

$('#skipPhoneCode').on('keyup', function(event) {
    event.preventDefault();
    $(this).val() != '' ? $('#skipError').text('').hide() : $('#skipError').text('验证码不能为空').show();;
});