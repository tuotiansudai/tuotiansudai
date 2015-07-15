/**
 * Created by zhaoshuai on 2015/7/1.
 */
require.config({
    baseUrl: 'js',
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
                    url: 'http://127.0.0.1:8088/mobile/register/userNameValidation',
                    type: 'GET',
                    dataType: 'jsonp',
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
                    url: 'http://192.168.100.11:8088/mobile/register/mobilePhoneNumValidation',
                    type: 'GET',
                    dataType: 'jsonp',
                    data: {
                        phoneNumber: function () {
                            return $('.phoneNumber').val();
                        }
                    }
                }
            },
            vCode: {
                required: true,
                remote: {
                    url: 'http://127.0.0.1:8088/mobile/register/vCodeValidation',
                    type: 'GET',
                    dataType: 'jsonp',
                    data: {
                        vCode: function () {
                            return $('.vCode').val();
                        }
                    }
                }
            }
        },
        errorElement: 'span',
        messages: {
            userName: {
                required: "用户名不能为空！",
                rangelength: "用户名的长度必须在5至25个字符之间！",
                remote:"用户名已存在！"
            },
            password: {
                required: "请输入密码",
                minlength: "密码至少6位"
            },
            phoneNumber: {
                isMobile: "手机号码格式错误",
                required: "手机号不能为空！",
                remote:"手机号码已存在!"
            },
            vCode: {
                required: "验证码不能为空！",
                remote:"验证码输入错误！"
            }
        },
        submitHandler: function (form) {
            form.submit();
            alert('1');
        }
    });

    $('.send_vCode').on('click', function () {
        $.ajax({
            url: 'http://192.168.100.11:8088/mobile/register/mobileRegister',
            type: 'GET',
            dataType: 'json',
            success: function (status) {
                if (status=='true') {
                    alert('注册成功');
                } else if (status=='false') {
                    alert('注册失败！');
                    return false;
                }
            }
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