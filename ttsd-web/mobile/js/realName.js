/**
 * Created by zhaoshuai on 2015/7/9.
 */
require.config({
    baseUrl: 'js',
    paths: {
        'jquery': 'libs/jquery-1.10.1.min',
        'validate': 'libs/jquery.validate.min',
        'validate-ex':'validate-ex'
    }
});
require(['jquery', 'validate','validate-ex'], function ($) {
    $('.realName').validate({
        rules:{
            yourName:{
                required:true
            },
            yourId:{
                required:true,
                isIdCardNo:true
            }
        },
        errorElement: 'small',
        messages:{
            yourName:'名字不能为空!',
            yourId:'身份证号码错误!'
        },
        submitHandler:function(form){
            var realName=$('.yourName').val();
            var idCard=$('.yourId').val();

            $.ajax({
                url:'/certification/realName',
                type: 'GET',
                dataType: 'json',
                success:function(status){
                    form.submit();
                }
            });
        }
    });
});