/**
 * Created by zhaoshuai on 2015/7/10.
 */
require(['jquery', 'jquery.validate'], function ($) {
    jQuery.validator.addMethod("isIdCardNo", function (value, element) {
        var idCard = /^[^a-zA-Z]\d{18}|\d{15}/;
        return this.optional(element) || (idCard.test(value));
    }, "请输入正确的身份证号码!");
    jQuery.validator.addMethod('vCode', function (value, element) {
        var vCode = /^[0-9]{6,}$/;
        return this.optional(element) || (vCode.test(value));
    }, "请输入正确的验证码!");

    jQuery.validator.addMethod('userName', function (value, element) {
        var spaceRegex = /\s+/g;
        return this.optional(element) || (!spaceRegex.test(value));
    }, "用户名不能包含空格!");

    jQuery.validator.addMethod('password', function (value, element) {
        var spaceRegex = /^(?=.*[^\d])(.{6,20})$/g;
        return this.optional(element) || (spaceRegex.test(value));
    }, "密码不符合规则!");

    jQuery.validator.addMethod('phoneNumberExist', function (value) {
        if ($('.phoneNumber ').siblings().css('display') == 'block') {
            $('.send_vCode').css({'pointer-events': 'none', 'background': '#666'});
        } else {
            if (value.length === 11) {
                $.ajax({
                    url: '/mobile/register/mobilePhoneNumValidation?tempData=' + new Date().getTime(),
                    dataType: 'json',
                    type: 'GET',
                    data: {phoneNumber: value},
                    success: function (data) {
                        if (data) {
                            $('.send_vCode').css({'pointer-events': 'auto', 'background': '#edaa20'});

                        } else {
                            $('.send_vCode').css({'pointer-events': 'none', 'background': '#666'});
                        }
                    }
                });
            } else {
                $('.send_vCode').css({'pointer-events': 'none', 'background': '#666'});
            }
        }
        return true;
    }, "");

});
