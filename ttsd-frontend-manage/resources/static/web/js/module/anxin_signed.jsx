//安心签的业务逻辑
let commonFun= require('publicJs/commonFun');
//安心签协议
require('webJsModule/anxin_agreement');

let $getSkipPhone = $('#getSkipPhone'); //安心签弹框盒

let $skipPhoneCode = $('#skipPhoneCode'),
    $methodToMsg = $('#methodToMsg'), //通过何种方式去获取验证码
    $getSkipCode = $('.get-skip-code',$methodToMsg), //短信验证码按钮
    $microPhone = $('.microphone',$methodToMsg);  //语音验证码按钮

let $skipError = $('#skipError'), //错误提示位置
    $getSkipBtn = $('#getSkipBtn'); //立即授权按钮

let $skipSuccess = $('#skipSuccess'); //授权成功弹框

let $skipCheck = $('#skipCheck');
$skipCheck && $skipCheck.prop('checked',true);

//弹出安心签弹框
function getSkipPhoneTip(){
    layer.open({
        shadeClose: false,
        title: '安心签代签署授权',
        btn: 0,
        type: 1,
        area: $(window).width()>700?['400px', 'auto']:['320px','auto'],
        content: $getSkipPhone
    });
}

//安心签授权成功弹框
function skipSuccess(callback){
    layer.closeAll();
    setTimeout(function(){
        $skipSuccess.hide();
        $skipPhoneCode.val('');
        callback && callback();
    },3000)
}

// 安心签弹框中获取短信验证码和语音请求
// 语音验证码 isVoice:true
// 短信验证码 isVoice:false

function getSMSCode(type) {
    $getSkipCode.prop('disabled',true);
    $microPhone.css('visibility', 'hidden');
    commonFun.useAjax({
        url: '/anxinSign/sendCaptcha',
        type: 'POST',
        data:{
            isVoice:type
        }
    },function(data) {
        $getSkipCode.prop('disabled',false);
        $microPhone.css('visibility', 'visible');
        if(data.success) {
            //开始倒计时
            $microPhone.css('visibility', 'hidden');
            commonFun.countDownLoan({
                btnDom:$getSkipCode,
                isAfterText:'重新获取验证码'
            },function() {
                $microPhone.css('visibility', 'visible');
            });

        }
        else {
            layer.msg('请求失败，请重试或联系客服！');
        }
    });
}

//对验证码进行验证
function checkSkipCode() {
    let thisVal = $skipPhoneCode.val();
    let error_msg = '';
    if(!/\d{6}/.test(thisVal)) {
        error_msg= '验证码必须为6位数字'
    }
    $skipError.text(error_msg);

    return error_msg;

}

$skipPhoneCode.on('blur',function() {
    checkSkipCode();
})

//通过何种方式获取短信验证码
$methodToMsg.on('click',function(event) {
    let targetId = event.target.id;
    //如果点击的是microPhone就是语音验证码
    let method = (targetId=='microPhone')?true:false;
    getSMSCode(method);

});

//去授权,安心签授权弹框表单提交
function toAuthorForAnxin(callback) {
    $getSkipBtn.on('click',function(event) {
        let $target=$(event.target);
        //首先检查验证码
        let errorSkip = checkSkipCode();
        if(errorSkip) {
            return;
        }
        let skipCode = $skipPhoneCode.val(),
            tipCheck = $('#tipCheck').prop('checked'); //是否同意短信免责申明
        $target.addClass('active').val('授权中...').prop('disabled', true);

        commonFun.useAjax({
            url: '/anxinSign/verifyCaptcha',
            type: 'POST',
            data: {
                captcha: skipCode,
                skipAuth:tipCheck
            }
        },function(data) {console.log(data)
            $target.removeClass('active').val('立即授权').prop('disabled', false);

            if(data.success){
                callback && callback(data);
                skipSuccess();
            }else{
                $skipError.text('验证码不正确').show();
            }

        });

    });
}

module.exports = {
    getSkipPhoneTip:getSkipPhoneTip,
    toAuthorForAnxin:toAuthorForAnxin
};
