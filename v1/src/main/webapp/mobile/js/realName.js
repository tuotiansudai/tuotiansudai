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
                    isIdCardNo:true,
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
                yourName:'请检查您的输入',
                yourId:'请输入正确的身份证号码'
            },
            submitHandler:function(form){
                form.submit();
            }
        });
});