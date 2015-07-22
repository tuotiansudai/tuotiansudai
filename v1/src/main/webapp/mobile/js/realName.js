/**
 * Created by zhaoshuai on 2015/7/9.
 */
require.config({
    baseUrl: '/mobile/js',
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
                    isIdCardNo:true,
                    minlength:15,
                    remote:{
                        url: '/mobile/certification/idCard/',
                        type: 'GET',
                        dataType:'json',
                        data: {
                            yourId: function () {
                                return $('.yourId').val();
                            }
                        }
                    }
                }
            },
            errorElement: 'small',
            messages:{
                yourName:'真实姓名不能为空！',
                yourId:{
                    required:'身份证号码不能为空！',
                    isIdCardNo:'身份证号码格式错误！',
                    minlength:'身份证号码至少为15位！',
                    remote:'身份证号码已存在！'
                }
            },
            submitHandler: function(form){
                form.submit(); //没有这一句表单不会提交
            }
        });

});


