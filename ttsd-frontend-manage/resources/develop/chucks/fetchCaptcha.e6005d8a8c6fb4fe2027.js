webpackJsonp([20],{

/***/ 245:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

	//用来获取手机验证码，主要是注册和找回密码的时候用
	var commonFun = __webpack_require__(1);
	var imageCaptchaForm = globalFun.$('#imageCaptchaForm');
	var $imageCaptchaForm = $(imageCaptchaForm);
	//获取手机验证
	var $captchaSubmit = $('.image-captcha-confirm', $imageCaptchaForm),
	    $imageCaptchaText = $('.image-captcha-text', $imageCaptchaForm),
	    $imageCaptcha = $('.image-captcha', $imageCaptchaForm),
	    $errorBox = $('.error-box', $imageCaptchaForm);

	var $fetchCaptcha = $('#fetchCaptcha');

	//刷新验证码
	$imageCaptcha.on('click', function () {
	    commonFun.refreshCaptcha(this, '/register/user/image-captcha');
	    $imageCaptchaForm[0].imageCaptcha.value = '';
	}).trigger('click');

	var fetchCaptchaFun = function () {
	    function fetchCaptchaFun(DomForm, kind) {
	        _classCallCheck(this, fetchCaptchaFun);

	        this.DomContainer = document.getElementById(DomForm);
	        this.kind = kind;
	    }

	    _createClass(fetchCaptchaFun, [{
	        key: 'init',
	        value: function init() {
	            this.CaptchaTextCheck();
	            this.FetchCaptcha();
	        }
	    }, {
	        key: 'CaptchaTextCheck',
	        value: function CaptchaTextCheck() {
	            $imageCaptchaText.on('keyup', function (event) {
	                if (/\d{5}/.test(this.value)) {
	                    $errorBox.text('');
	                    $(this).removeClass('error');
	                } else {
	                    $errorBox.text('验证码只能为5位数字');
	                    $(this).addClass('error');
	                }
	            });
	        }
	    }, {
	        key: 'FetchCaptcha',
	        value: function FetchCaptcha() {
	            var that = this;
	            //点击获取验证码
	            $fetchCaptcha.on('click', function () {
	                var mobile = that.DomContainer.mobile.value;
	                $errorBox.text('');
	                $imageCaptchaForm[0].imageCaptcha.value;

	                layer.open({
	                    type: 1,
	                    area: ['380px', '210px'],
	                    shadeClose: true,
	                    content: $imageCaptchaForm.parents('.image-captcha-dialog')
	                });
	                that.getCaptchaOrCancel();
	                $imageCaptchaForm.find('.mobile').val(mobile);
	            });
	        }
	    }, {
	        key: 'getCaptchaOrCancel',
	        value: function getCaptchaOrCancel() {
	            var that = this;
	            $captchaSubmit.on('click', function (event) {
	                event.preventDefault();
	                var imageText = $imageCaptchaText.val();
	                if (imageText == '') {
	                    $errorBox.text('验证码不能为空');
	                    return;
	                }
	                !$imageCaptchaText.hasClass('error') && that.getSMSCode();
	            });
	        }
	    }, {
	        key: 'getSMSCode',
	        value: function getSMSCode() {
	            var captcha = $imageCaptchaText.val(),
	                that = this;
	            $captchaSubmit.prop('disabled', true);
	            var ajaxOption = void 0;
	            // 提交手机验证表单
	            if (that.kind == "register") {
	                ajaxOption = {
	                    url: '/register/user/send-register-captcha',
	                    type: 'POST',
	                    data: $imageCaptchaForm.serialize()
	                };
	            }
	            // else if(that.kind=='retrieve'){
	            //     ajaxOption={
	            //         type:'GET',
	            //         url: "/mobile-retrieve-password/mobile/"+that.DomContainer.mobile.value+"/imageCaptcha/"+captcha+"/send-mobile-captcha",
	            //     }
	            // }
	            commonFun.useAjax(ajaxOption, function (responseData) {
	                $captchaSubmit.prop('disabled', false);
	                //刷新验证码
	                commonFun.refreshCaptcha($imageCaptcha[0], '/register/user/image-captcha');

	                var data = responseData.data;
	                if (data.status && !data.isRestricted) {
	                    //获取手机验证码成功，关闭弹框，并开始倒计时
	                    layer.closeAll();
	                    commonFun.countDownLoan({
	                        btnDom: $fetchCaptcha
	                    });
	                } else if (!data.status && data.isRestricted) {
	                    $errorBox.text('短信发送频繁，请稍后再试');
	                } else if (!data.status && !data.isRestricted) {
	                    $errorBox.text('图形验证码不正确');
	                }
	            });
	        }
	    }]);

	    return fetchCaptchaFun;
	}();

	module.exports = fetchCaptchaFun;

/***/ }

});