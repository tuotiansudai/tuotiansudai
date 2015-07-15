/**
 * Created by zhaoshuai on 2015/7/1.
 */
require.config({
    baseUrl: 'js',
    paths: {
        'jquery': 'libs/jquery-1.10.1.min',
<<<<<<< HEAD
        'validate': 'libs/jquery.validate.min',
        'validate-ex': 'validate-ex'
    }
});
require(['jquery', 'validate', 'validate-ex'], function ($) {
=======
        'validate': 'libs/jquery.validate.min'
    }
});
require(['jquery', 'validate'], function ($) {
    $('.send_vCode').on('click', function () {
        var Num = 5;
        var Down = setInterval(countDown, 1000);

        function countDown() {
            $('.send_vCode').html(Num + '秒后重新发送').css({'background': '#666', 'color': '#fff', 'width': '100','pointer-events': 'none'});
            if (Num == 0) {
                clearInterval(Down);
                $('.send_vCode').html('重新获取验证码').css({'background': '#e9a922','pointer-events': 'auto'});
            }
            Num--;
        }
    });
    var oUserName=$('.userName');
    var oPassWord=$('.passWord');
    var oPhoneNumber=$('.phoneNumber');
    var obj=['oUserName','oPassWord','oPhoneNumber'];
    for(var i=0;i<obj.length;i++){
        obj[i].index=i;
        obj[i].onblur=function(){
            alert(this.index);
        }
    }

>>>>>>> parent of 0d29b76... newmobilandpccode
    $('.cmxForm').validate({
        onkeyup:false,
        rules: {
            userName: {
                required: true,
<<<<<<< HEAD
                rangelength: [5, 25],
                remote: {
                    url: 'http://127.0.0.1:8088/mobile/register/userNameValidation',
                    type: 'GET',
                    dataType: 'json',
                    data: {
                        userName: function () {
                            return $('.userName').val();
                        }
                    }
                }
=======
                regex: "^[a-zA-Z0-9]+$",
                rangeLength: [5, 25]
>>>>>>> parent of 0d29b76... newmobilandpccode
            },
            passWord: {
                required: true,
                minLength: 5
            },
            phoneNumber: {
<<<<<<< HEAD
                isMobile: true,
                required: true,
                remote: {
                    url: 'http://127.0.0.1:8088/mobile/register/mobilePhoneNumValidation',
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
                remote: {
                    url: 'http://127.0.0.1:8088/mobile/register/vCodeValidation',
                    type: 'GET',
                    dataType: 'json',
                    data: {
                        vCode: function () {
                            return $('.vCode').val();
                        }
                    }
=======
                required: true,
                digits: true,
                rangeLength: [11, 11]
            },
            vCode: {
                required: true,
                regex: "/^[0-9]+$/",
                digits: true
            }
        },
        remote: {
            url: "check-email.php",     //后台处理程序
            type: "GET",               //数据发送方式
            dataType: "json",           //接受数据格式
            data: {                     //要传递的数据
                username: function() {
                    return $("#username").val();
>>>>>>> parent of 0d29b76... newmobilandpccode
                }
            }
        },
        messages: {
<<<<<<< HEAD
            userName: {
                required: "用户名不能为空！",
                rangelength: "用户名的长度必须在5至25个字符之间！",
                remote:"用户名已存在！"
            },
            passWord: {
                required:"密码不能为空!",
                minLength: "密码最少为6位！"
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
=======
            userName: "5-25位非手机号用户名",
            passWord: "请输入6位以上字母混合密码",
            phoneNumber: "手机号码不正确",
            vCode: "验证码不正确"
        },
        success:function(label){
            label.text(" ").addClass('success');
        }
    });

>>>>>>> parent of 0d29b76... newmobilandpccode
});