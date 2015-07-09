/**
 * Created by zhaoshuai on 2015/7/1.
 */
require.config({
    baseUrl: 'js',
    paths: {
        'jquery': 'libs/jquery-1.10.1.min',
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

    $('.cmxForm').validate({
        onkeyup:false,
        rules: {
            username: {
                required: true,
                regex: "^[a-zA-Z0-9]+$",
                rangeLength: [5, 25]
            },
            passWord: {
                required: true,
                minLength: 5
            },
            phoneNumber: {
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
                }
            }
        },
        messages: {
            userName: "5-25位非手机号用户名",
            passWord: "请输入6位以上字母混合密码",
            phoneNumber: "手机号码不正确",
            vCode: "验证码不正确"
        },
        success:function(label){
            label.text(" ").addClass('success');
        }
    });

});