/**
 * Created by zhaoshuai on 2015/7/1.
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
    $('.cmxForm').validate({
        //focusInvalid: false,
        rules: {
            username: {
                required: true,
                rangelength: [5,25],
                remote: {
                    url: 'index.json',
                    type: 'GET',
                    dataType:'json',
                    data: {
                        username: function () {
                            return $('.userName').val();
                        }
                    }
                }
            },
            password: {
                required: true,
                minLength: 6
            },
            phoneNumber: {
                isMobile:true,
                required: true,
                digits: true,
                remote: {
                    url: 'index.json',
                    type: 'GET',
                    dataType:'json',
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
                    url: 'index.json',
                    type: 'GET',
                    dataType:'json',
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
            username: "5-25位非手机号用户名",
            passWord: "请输入6位以上字母混合密码",
            phoneNumber: "手机号码不正确",
            vCode: "验证码错误"
        },
        submitHandler:function(form){
            alert("submitted");
            form.submit();
        }
    });
    //var namevalue=$('.username').val();
    //$('.logUp').on('click',function(){
    //    alert('1');
    //    $(this).ajaxSubmit({
    //        url:'index.php'+'namevalue',
    //        type:'GET',
    //        success:function(data){
    //            alert('提交成功');
    //        }
    //       //$(this).resetForm(); // 提交后重置表单
    //    });
    //    return false; // 阻止表单自动提交事件
    //});
    var isName=$('.userName');
    var isPhoneNumber=$('.phoneNumber');
    $('.send_vCode').on('click', function () {
        if(isName.val()!==""&& isPhoneNumber.val()!==""){
            $.ajax({
                url:'index.php?operationType=',
                type:'GET',
                dataType:'json',
                success:function(response){
                    if(response.operation===1){
                        alert('1');
                        return true;
                    }else if(response.operation===0){
                        alert('0');
                        return false;
                    }
                }
            });
            var Num = 5;
            var Down = setInterval(countDown, 1000);

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
        }else{
            alert('请检查您的输入!');
        }
    });
});