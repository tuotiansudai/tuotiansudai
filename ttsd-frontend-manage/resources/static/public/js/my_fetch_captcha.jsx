//用来获取手机验证码，主要是注册和找回密码的时候用
let commonFun= require('publicJs/commonFun');
let registerForm=globalFun.$('#registerUserForm');
let imageCaptchaForm=globalFun.$('#registerUserForm');
let $imageCaptchaForm =$(imageCaptchaForm);
let isVoice = false;
let $voiceCaptcha = $('#voice_captcha');
let ValidatorObj= require('publicJs/validator');
//获取手机验证
let $captchaSubmit=$('.image-captcha-confirm',$imageCaptchaForm),
    $imageCaptchaText=$('.image-captcha-text',$imageCaptchaForm),
    $imageCaptcha = $('.image-captcha',$imageCaptchaForm),
    $errorBox=$('.error-box',$imageCaptchaForm);

let $fetchCaptcha=$('#fetchCaptcha');
let validator = new ValidatorObj.ValidatorForm();

//刷新验证码
$imageCaptcha.on('click',function() {
    commonFun.refreshCaptcha(this,'/register/user/image-captcha');
    $imageCaptchaForm[0].imageCaptcha.value='';
});

class fetchCaptchaFun{
    constructor(DomForm,kind) {
        this.DomContainer=document.getElementById(DomForm);
        this.kind=kind;
    }
    init() {
        this.CaptchaTextCheck();
        this.FetchCaptcha();
    }
    CaptchaTextCheck() {
        $imageCaptchaText.on('keyup',function(event) {
            if(/\d{5}/.test(this.value)) {
                $errorBox.text('');
                $(this).removeClass('error');
            }
            else {
                $errorBox.text('验证码只能为5位数字');
                $(this).addClass('error');
            }
        });
    }
    FetchCaptcha() {
        let that =this;
        that.getCaptchaOrCancel();
        //点击获取验证码
        $('#fetchCaptcha').on('click',function() {
            isVoice = false;
            // commonFun.refreshCaptcha($imageCaptcha[0],'/register/user/image-captcha');
            // let mobile=that.DomContainer.mobile.value;
            // $errorBox.text('');
            // $imageCaptchaForm[0].imageCaptcha.value='';
            // $imageCaptchaForm.find('.mobile').val(mobile);
            //verificationCode();
        });
        $('#voice_btn').on('click',function(){
            isVoice = true;
            commonFun.refreshCaptcha($imageCaptcha[0],'/register/user/image-captcha');
            let mobile=that.DomContainer.mobile.value;
            $errorBox.text('');
            $imageCaptchaForm[0].imageCaptcha.value='';
            layer.open({
                type:1,
                area:['320px'],
                shadeClose: true,
                content:$imageCaptchaForm.parents('.image-captcha-dialog')
            });

            $imageCaptchaForm.find('.mobile').val(mobile);
        })
    }
    getCaptchaOrCancel() {
        let that=this;
        $captchaSubmit.on('click',function(event) {
            event.preventDefault();
            var imageText=$imageCaptchaText.val();
            if(imageText=='') {
                $errorBox.text('验证码不能为空');
                return;
            }
            !$imageCaptchaText.hasClass('error') &&  that.getSMSCode();
        });
    }
    getSMSCode() {
        let that=this;

        getFetchaCountdown(that);

    }

}

function getFetchaCountdown(that) {
    let captcha=$imageCaptchaText.val()
    $captchaSubmit.prop('disabled',true);
    let ajaxOption,
        captchaSrc;
    // 提交手机验证表单
    if(that.kind=="register") {
        ajaxOption={
            url: '/register/user/send-register-captcha',
            type:'POST',
            data:$imageCaptchaForm.serialize()+'&voice='+isVoice
            // data:$imageCaptchaForm.serialize()
        };
        captchaSrc='/register/user/image-captcha';
    }
    else if(that.kind=='retrieve'){
        ajaxOption={
            type:'GET',
            url: "/mobile-retrieve-password/mobile/"+that.DomContainer.mobile.value+"/imageCaptcha/"+captcha+"/send-mobile-captcha/"+isVoice,
        };
        captchaSrc='/mobile-retrieve-password/image-captcha';
    }
    commonFun.useAjax(ajaxOption,function(responseData) {
        $captchaSubmit.prop('disabled',false);
        $voiceCaptcha.hide();
        //刷新验证码
        commonFun.refreshCaptcha($imageCaptcha[0], captchaSrc);

        let data = responseData.data;
        if (data.status && !data.isRestricted) {
            //获取手机验证码成功，关闭弹框，并开始倒计时
            layer.closeAll();
            commonFun.countDownLoan({
                btnDom:$fetchCaptcha,
                isAfterText:'重新发送'
            },function(){
                $voiceCaptcha.show();
            });

        } else if (!data.status && data.isRestricted) {
            $errorBox.text('短信发送频繁，请稍后再试');

        } else if (!data.status && !data.isRestricted) {
            $errorBox.text('图形验证码不正确');
        }
    });
}
module.exports =fetchCaptchaFun;