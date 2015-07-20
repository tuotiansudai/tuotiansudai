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
        return this.optional(element) || (length == 11 && /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/.test(value));
    }, "请正确填写您的手机号码!"),
    jQuery.validator.addMethod("isIdCardNo", function (value, element) {
        var idCard = /^[^a-zA-Z]\d{18}|\d{15}/;
        return this.optional(element) || (idCard.test(value));
    }, "请输入正确的身份证号码!");
    jQuery.validator.addMethod('vCode',function(value,element){
        var vCode=/^[0-9]{6,}$/;
        return this.optional(element)||(vCode.test(value));
    },"请输入正确的验证码!");

    jQuery.validator.addMethod('userName',function(value,element){
        var spaceRegex = /\s+/g;
        return this.optional(element)||(!spaceRegex.test(value));
    },"用户名不能包含空格!");

});
