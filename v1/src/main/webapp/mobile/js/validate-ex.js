/**
 * Created by zhaoshuai on 2015/7/10.
 */
require.config({
    baseUrl: '/mobile/js',
    paths: {
        'jquery': 'libs/jquery-1.10.1.min',
        'validate': 'libs/jquery.validate.min'
    }
});
require(['jquery', 'validate'], function ($) {
    jQuery.validator.addMethod("isMobile", function (value, element) {
        var length = value.length;
        return this.optional(element) || (length == 11 && /^(((13[0-9]{1})|(14[0-9]{1})|(18[0-9]{1})|(17[0-9]{1})|(15[0-9]{1}))+\d{8})$/.test(value));
    }, "请正确填写您的手机号码!"),
    jQuery.validator.addMethod("isIdCardNo", function (value, element) {
        var idCard = /^[^a-zA-Z]\d{18}|\d{15}/;
        return this.optional(element) || (idCard.test(value));
    }, "请输入正确的身份证号码!");
    jQuery.validator.addMethod('vCode',function(value,element){
        var vCode=/^[0-9]{6,}$/;
        return this.optional(element)||(vCode.test(value));
    },"请输入正确的验证码!");
});
