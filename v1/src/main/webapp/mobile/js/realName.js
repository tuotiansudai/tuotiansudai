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
            }
        });


    $('.realName_submit').on('click', function () {
        var nameValue = $('.yourName').val();
        var idValue = $('.yourId').val();
        $.ajax({
            url: '/mobile/certification/realName',
            type: 'POST',
            dataType: 'json',
            data: {
                realName: nameValue,
                idCard: idValue
            },
            success: function (data) {
                if (data.success == "true") {
                    window.location.href='/user/center';
                }
                if (data.success == "false") {
                    var aTip=$('.tipMask');
                    var clientH=$(window).height();
                    aTip.css({'height':clientH,'display':'block'});
                }
            }
        });
    });
    var aTip=$('.tipMask');
    var clientH=$(window).height();
    aTip.on('click',function(){
        $(this).css('display','none');
    });
});


