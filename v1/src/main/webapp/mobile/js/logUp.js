/**
 * Created by zhaoshuai on 2015/7/1.
 */
require.config({
    baseUrl: '/mobile/js',
    paths: {
        'jquery': 'libs/jquery-1.10.1.min',
        'validate': 'libs/jquery.validate.min',
        'validate-ex': 'validate-ex'
    }
});
require(['jquery', 'validate', 'validate-ex'], function ($) {

    $('.cmxForm').validate({
        //focusInvalid: false,
        rules: {
            username: {
                required: true,
                userName:true,
                rangelength: [5, 25],
                remote: {
                    url: '/mobile/register/userNameValidation?tempData='+new Date().getTime(),
                    type: 'GET',
                    dataType: 'json',
                    data: {
                        username: function () {
                            return $('.userName').val();
                        }
                    }
                }
            },
            password: {
                required: true,
                minlength: 6
            },
            phoneNumber: {
                isMobile: true,
                required: true,
                remote: {
                    url: '/mobile/register/mobilePhoneNumValidation?tempData='+new Date().getTime(),
                    type: 'GET',
                    dataType: 'json',
                    data: {
                        phoneNumber: function () {
                            return $('.phoneNumber').val();
                        }
                    }
                }
        },
            vCode: {
                required: true,
                vCode:true,
                remote: {
                    url: '/mobile/register/vCodeValidation?tempData='+new Date().getTime(),
                    type: 'GET',
                    dataType: 'json',
                    data: {
                        vCode: function () {
                            return $('.vCode').val();
                        },
                        phoneNumber: function () {
                            return $('.phoneNumber').val();
                        }
                    }
                }
            }
        },
        errorElement: 'div',
        messages: {
            username: {
                required: "用户名不能为空！",
                userName:'用户名不能包含空格！',
                rangelength: "用户名的长度必须在5至25个字符之间！",
                remote:"用户名已存在！"
            },
            password: {
                required:"密码不能为空!",
                minlength: "密码最少为6位！"
            },
            phoneNumber: {
                isMobile: "手机号码格式错误",
                required: "手机号不能为空！",
                remote:"手机号码已存在!"
            },
            vCode: {
                required: "验证码不能为空！",
                vCode:"验证码输入错误！",
                remote:"验证码输入错误！"
            }
        },
        submitHandler: function(form){
            form.submit(); //没有这一句表单不会提交
        }
    });

    /**
     * 手机端注册
     */

    //var userValue = $('.userName').val();
    //$('.logUp').on('click',function(){
    //        var userValue=$('.userName').val();
    //        var passValue=$('.passWord').val();
    //        var phoneValue=$('.phoneNumber').val();
    //        var vCodeValue=$('.vCode').val();
    //        $.ajax({
    //            url: '/mobile/register/mobileRegister?tempData='+new Date().getTime(),
    //            type: 'POST',
    //            data:{
    //                username:userValue,
    //                password:passValue,
    //                phoneNumber:phoneValue,
    //                vCode:vCodeValue,
    //                operationType:'1'
    //            },
    //            dataType: 'json',
    //            success: function (result) {
    //                if (result) {
    //                    window.location.href='/mobile/certification';
    //                }
    //            }
    //        });
    //    //}
    //});

    $('.check_input').bind('click',function(){
        if($('.check_input').prop('checked')){
            $(".logUp").css({'pointer-events': 'auto','background':'#edaa20' });
        }else{
            $(".logUp").css({'pointer-events': 'none','background':'gray' });
        }
    });

    /**
     * 手机端获取注册授权码
     */
    $('.phoneNumber').on('blur',function(){
        var cellphoneValue = $('.phoneNumber').val().trim();
        var cellphoneLength = ((cellphoneValue.length) < 11);
        if(cellphoneLength){
            $('.tip').css('display','none');
            $('#phoneNumber-error').css('display','block');
            $('.send_vCode').css({'pointer-events':'none','background':'gray'});
        } else {
            $('.tip').css('display','none');
            $('.send_vCode').css({'pointer-events':'auto','background':'#edaa20'});
            $('.send_vCode').on('click', function () {
                var userValue=$('.userName').val();
                var passValue=$('.passWord').val();
                var phoneValue=$('.phoneNumber').val();
                $.ajax({
                    url: '/mobile/register/mobileRegisterValidationCode?tempData='+new Date().getTime(),
                    type: 'POST',
                    data:{
                        username:userValue,
                        password:passValue,
                        phoneNumber:phoneValue,
                        operationType:'0'
                    },
                    dataType: 'json'
                });
                $('.phoneNumber').onblur(function(){
                    $('.send_vCode').css({'background': '#666','pointer-events':'none'});
                });
                var Num = 60;
                var Down = setInterval(countDown, 1000);
                countDown();
                function countDown() {
                    //alert('asd');
                    $('.send_vCode').html(Num + '秒后重新发送').css({
                        'background': '#666',
                        'color': '#fff',
                        'width': '100',
                        'pointer-events': 'none'
                    });
                    if (Num == 0) {
                        clearInterval(Down);
                        $('.send_vCode').html('重新获取验证码').css({'background': '#e9a922', 'pointer-events': 'auto'});
                        //$.ajax({
                        //    url: '/mobile/register/mobilePhoneNumValidation?tempData='+new Date().getTime(),
                        //    type: 'GET',
                        //    data:{
                        //        phoneNumber:  $('.phoneNumber').val()
                        //    },
                        //    dataType: 'json',
                        //    success:function(res){
                        //        if(res){
                        //            $('.send_vCode').css({'background': '#666','pointer-events':'none'});
                        //        }else{
                        //            $('.send_vCode').css({'background': '#e9a922','pointer-events':'auto'});
                        //        }
                        //    }
                        //});
                    }
                    Num--;
                }
            });
        }
    });
});