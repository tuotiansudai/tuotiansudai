/**
 * Created by zhaoshuai on 2015/7/1.
 */
require(['jquery', 'jquery.validate', 'validate-ex'], function ($) {

    $('.cmxForm').validate({
        rules: {
            username: {
                required: true,
                userName:true,
                rangelength: [5, 25],
                remote: {
                    url: '/mobile/register/userNameValidation?tempData='+new Date().getTime(),
                    type: 'GET',
                    dataType: 'json'
                }
            },
            password: {
                required: true,
                minlength: 6
            },
            phoneNumber: {
                required: true,
                digits: true,
                rangelength: [11,11],
                remote: {
                    url: '/mobile/register/mobilePhoneNumValidation?tempData='+new Date().getTime(),
                    type: 'GET',
                    dataType: 'json',
                },
                'phoneNumberExist':true
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
            },
            referrer:{
                remote:{
                    url: '/mobile/register/referrerValidation',
                    type: 'GET',
                    dataType: 'json'
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
                digits: "手机号码格式错误",
                required: "手机号不能为空！",
                rangelength:'请输入正确的手机号！',
                remote:"手机号码已存在或者不合法!"
            },
            vCode: {
                required: "验证码不能为空！",
                vCode:"验证码输入错误！",
                remote:"验证码输入错误！"
            },
            referrer:{
                remote:'此用户不存在！'
            }
        },
        submitHandler: function(form){
            form.submit();
        }
    });



    $('.check_input').bind('click',function(){
        if($('.check_input').prop('checked')){
            $(".logUp-a").css('background','#edaa20');
            $('.logUp').css('pointer-events', 'auto')
        }else{
            $(".logUp-a").css('background','#666');
            $('.logUp').css('pointer-events', 'none')
        }
    });

    $('.send_vCode').on('click', function () {
        var phoneValue = $('.phoneNumber').val();
        $.ajax({
            url: '/mobile/register/mobileRegisterValidationCode?tempData=' + new Date().getTime(),
            type: 'POST',
            data: {
                phoneNumber: phoneValue
            },
            dataType: 'json'
        });
        var Num = 60;
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