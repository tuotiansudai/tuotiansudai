/**
 * Created by zhaoshuai on 2015/7/10.
 */
require.config({
    baseUrl: 'js',
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
        var yourId = /^[^a-zA-Z]\d{18}|\d{15}/;
        return this.optional(element) || (yourId.test(value));
    }, "请输入正确的身份证号码!");
    function isIdCardNo(num){
        var len = num.length, re;
        if (len == 15){
            re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{2})(\w)$/);
        } else if (len == 18){
            re = new RegExp(/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\w)$/);
        }else{
            alert("输入的数字位数不对！"); return false;
        }
        var a = num.match(re);
        if (a != null) {
            if (len == 15) {
                var D = new Date("19"+a[3]+"/"+a[4]+"/"+a[5]);
                var B = D.getYear()==a[3]&&(D.getMonth()+1)==a[4]&&D.getDate()==a[5];
            }else{
                var D = new Date(a[3]+"/"+a[4]+"/"+a[5]);
                var B = D.getFullYear()==a[3]&&(D.getMonth()+1)==a[4]&&D.getDate()==a[5];
            }
            if (!B) {alert("输入的身份证号 "+ a[0] +" 里出生日期不对！"); return false;}
        }
        if(!re.test(num)){alert("身份证最后一位只能是数字和字母!");return false;}
        return false;
    }
});
