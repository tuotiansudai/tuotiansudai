/**
 * Created by CBJ on 2015/10/26.
 */
require(['jquery','layer','jquery.validate'], function ($,layer) {
    $(function () {
    var $InfoBox=$('#personInfoBox'),
        $setEmail=$('.setEmail',$InfoBox),
        $setPass=$('.setPass',$InfoBox),
        $btnChangeEmail=$('#btnChangeEmail'),
        $changeEmailDOM=$('#changeEmailDOM'),
        $EmailForm=$('form',$changeEmailDOM),

        $btnChangePass=$('#btnChangePass'),
        $changePassDOM=$('#changePassDOM'),
        $PassForm=$('form',$changePassDOM);

        $setEmail.on('click', function(){
            layer.open({
                type: 1,
                title :'绑定邮箱',
                area: ['500px', '230px'],
                shadeClose: true,
                content: $changeEmailDOM
            });
        });
        $EmailForm.validate({
            rules: {
                email: {
                    required: true,
                    email: true
                }
            },
            messages: {
                email: "请输入有效的邮箱地址"
            },
            submitHandler: function() {
                //var index = layer.open();
                //layer.close(index);
                layer.closeAll();
                layer.open({
                    type: 1,
                    title :'验证邮箱',
                    area: ['500px', '220px'],
                    shadeClose: true,
                    content: $('#CESuccess'),
                    btn:['返回'],
                    yes: function(index, layero){
                        layer.close(index);
                        console.log('000');
                    }
                });

            }
        });

        $setPass.on('click', function(){
            layer.open({
                type: 1,
                title :'重置密码',
                area: ['600px', '360px'],
                shadeClose: true,
                content: $changePassDOM
            });
        });
        $PassForm.validate({
            rules: {
                oldPassword:{
                    required:true,
                    //checkPassword:true
                },
                newPassword:{
                    required:true,
                   // checkPassword:true,
                    rangelength:[5,15]
                },
                newPassword2:{
                    required:true,
                   // checkPassword:true,
                    rangelength:[5,15],
                    equalTo: "#newPassword"
                }
            },
            messages: {
                oldPassword:{
                    required:"请输入密码"
                },
                newPassword:{
                    required:"请输入新密码",
                    rangelength: $.validator.format("请输入 {0} 到 {1}的字符长度"),
                },
                newPassword2:{
                    required:"请输入确认密码",
                    equalTo:"两次输入密码不一致，请重新输入",
                    rangelength: $.validator.format("请输入 {0} 到 {1}的字符长度"),
                }
            },
            submitHandler: function(index) {
                console.log('ppp');
            }
        });

        //jQuery.validator.addMethod("checkPassword", function(value, element) {
        //    var checkPassword = /.{5,12}$/;
        //    return this.optional(element) || (checkPassword.test(value));
        //}, "密码长度为5-12");
    });
});
