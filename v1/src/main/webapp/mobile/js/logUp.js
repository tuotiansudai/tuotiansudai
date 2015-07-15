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
                        }
                    }
                }
            }
        },
        errorElement: 'div',
        messages: {
            username: {
                required: "用户名不能为空！",
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
        }
        //,
        //submitHandler: function (form) {
        //    form.submit();
        //    alert('1');
        //}
    });

    /**
     * 手机端注册
     */

    $('.logUp').on('click',function(){

            var userValue=$('.userName').val();
            var passValue=$('.passWord').val();
            var phoneValue=$('.phoneNumber').val();
            var vCodeValue=$('.vCode').val();
            $.ajax({
                url: '/mobile/register/mobileRegister?tempData='+new Date().getTime(),
                type: 'POST',
                data:{
                    username:userValue,
                    password:passValue,
                    phoneNumber:phoneValue,
                    vCode:vCodeValue,
                    operationType:'1'
                },
                dataType: 'json',
                success: function (result) {
                    if (result) {
                        window.location.href='/mobile/templates/certification.ftl?tempData='+new Date().getTime();
                    }
                }
            });

    });

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
    $('.send_vCode').on('click', function () {
        var userValue=$('.userName').val();
        var passValue=$('.passWord').val();
        var phoneValue=$('.phoneNumber').val();
        $.ajax({
            url: '/mobile/register/mobileRegister',
            type: 'POST',
            data:{
                username:userValue,
                password:passValue,
                phoneNumber:phoneValue,
                operationType:'0'
            },
            dataType: 'json'
        });
        var Num = 5;
        var Down = setInterval(countDown, 1000);
        countDown();
        function countDown() {
            $('.send_vCode').html(Num + '秒后重新发送').css({
                'background': '#666',
                'color': '#fff',
                'width': '100',
                'pointer-events': 'none'
            });
            if (Num == 0) {
                clearInterval(Down);
                $('.send_vCode').html('重新获取验证码').css({'background': '#e9a922', 'pointer-events': 'auto'});
            }
            Num--;
        }
    });
});