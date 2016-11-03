require(['jquery', 'layerWrapper', 'jquery.validate', 'coupon-alert', 'red-envelope-float', 'jquery.ajax.extension'], function ($, layer) {
    var $createForm = $('#createForm'),
        $agreement = $createForm.find('.agreement'),
        $isSkipAuth=$('#isSkipAuth');
    $createForm.validate({
        debug: true,
        rules: {
            price: {
                required: true,
                max: parseFloat($('#tipText').attr('data-max')),
                min: parseFloat($('#tipText').attr('data-min'))
            }
        },
        onkeyup: function () {
            $('#tipText').removeClass('active');
        },
        errorPlacement: function (error, element) {
            $('#tipText').addClass('active');
        },
        submitHandler: function (form) {
            
            if($isSkipAuth.val()=='true'){
                applyTip();
                return;
            }else{
                if($('#skipCheck').val()=='true'){
                    getSkipPhoneTip();
                }else{
                    $agreement.next('span.error').show();
                    return;
                }
                return false;
            }
        }
    });

    function applyTip(){
        layer.open({
                type: 1,
                title: '温馨提示',
                btn: ['确定', '取消'],
                area: ['330px', '185px'],
                shadeClose: true,
                content: '<p class="pad-m tc">是否确认转让？</p>',
                btn1: function () {
                    var transferInvestId = $('#transferInvestId').val();
                    $.ajax({
                        url: '/transfer/invest/' + transferInvestId + '/is-transferable',
                        type: 'GET'
                    }).done(function (data) {
                        if (true == data.data.status) {
                            sendData();
                            layer.closeAll();
                        } else {
                            layer.msg(data.message);
                        }
                    })
                },
                btn2: function () {
                    layer.closeAll();
                }
            });
    }

    function sendData() {
        $.ajax({
            url: '/transfer/apply',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({
                'transferAmount': parseFloat($('#transferAmount').val()) * 100,
                'transferInvestId': $('#transferInvestId').val()
            }),
            beforeSend: function (data) {
                $createForm.find('button[type="submit"]').prop('disabled', true);
            }
        }).done(function (data) {
            if (data == true) {
                layer.open({
                    title: '温馨提示',
                    btn: 0,
                    area: ['400px', '150px'],
                    content: $('#successTip').html(),
                    success: function (layero, index) {
                        setInterval(function () {
                            if ($('.layui-layer-content .count-time').text() < 2) {
                                window.location.href = '/transferrer/transfer-application-list/TRANSFERRING';
                            } else {
                                $('.layui-layer-content .count-time').text(function (index, num) {
                                    return parseInt(num) - 1
                                });
                            }
                        }, 1000);
                    }
                });
            } else {
                layer.msg('申请失败，请重试！');
            }
        })
            .fail(function () {
                layer.msg('请求失败，请重试！');
            })
            .always(function () {
                $createForm.find('button[type="submit"]').prop('disabled', false);
            });
    }

    $agreement.find('.fa').on('click', function () {
        var $this = $(this).parent(),
            className;
        $this.toggleClass('checked');
        if ($this.hasClass('checked')) {
            className = 'fa fa-check-square';
            $this.next('span.error').hide();
            $('#skipCheck').length>0?$('#skipCheck').val('true'):false;
        }
        else {
            className = 'fa fa-square-o';
            $('#skipCheck').length>0?$('#skipCheck').val('false'):false;
        }
        $this.find('i')[0].className = className;
    });
    $('#cancleBtn').on('click', function (event) {
        event.preventDefault();
        history.go(-1);
    });



    //skip tip click chechbox
    $('.tip-item .skip-icon').on('click', function(event) {
        event.preventDefault();
        $(this).hasClass('active')?$(this).removeClass('active') && $('#tipCheck').val('false'):$(this).addClass('active')&& $('#tipCheck').val('true');
    });


    //show phone code tip
    function getSkipPhoneTip(){
        layer.open({
            shadeClose: false,
            title: '安心签代签署授权',
            btn: 0,
            type: 1,
            area: ['400px', 'auto'],
            content: $('#getSkipPhone')
        });
    }

    var num = 60,Down;

    //get phone code
    $('#getSkipCode').on('click', function(event) {
        event.preventDefault();
        getCode(false);
    });
    
    //get phone code yuyin
    $('#microPhone').on('click', function(event) {
        event.preventDefault();
        getCode(true);
    });
    function getCode(type){
        $.ajax({
            url: '/anxinSign/sendCaptcha',
            type: 'POST',
            dataType: 'json',
            data:{
                isVoice:type
            }
        })
        .done(function(data) {
            countDown();
            Down = setInterval(countDown, 1000);
        })
        .fail(function() {
            layer.msg('请求失败，请重试或联系客服！');
        });
    }
    //countdown skip
    function countDown() {
        $('#getSkipCode').val(num + 's').prop('disabled',true);
        $('#microPhone').css('visibility', 'hidden');
        if (num == 0) {
            clearInterval(Down);
            $('#getSkipCode').val('重新获取验证码').prop('disabled',false);
            $('#microPhone').css('visibility', 'visible');
        }
        num--;
    }
    //submit data skip phone code
    $('#getSkipBtn').on('click',  function(event) {
        event.preventDefault();
        var $self=$(this);
        if($('#skipPhoneCode').val()!=''){
            $.ajax({
                url: '/anxinSign/verifyCaptcha',
                type: 'POST',
                dataType: 'json',
                data: {
                    captcha: $('#skipPhoneCode').val(),
                    skipAuth:$('#tipCheck').val()
                }
            })
            .done(function(data) {
                $self.removeClass('active').val('立即授权').prop('disabled', false);
                data.success?skipSuccess():$('#skipError').text('验证码不正确').show();
            })
            .fail(function() {
                $self.removeClass('active').val('立即授权').prop('disabled', false);
                layer.msg('请求失败，请重试！');
            })
            .always(function() {
                $self.addClass('active').val('授权中...').prop('disabled', true);
            });
        }else{
            $('#skipError').text('验证码不能为空').show();
        }
    });

    //skip success
    function skipSuccess(){
        layer.closeAll();
        $('#skipSuccess').show();
        setInterval(function(){
            $('#skipSuccess').hide();
            applyTip();
        },3000)
    }

    $('#skipPhoneCode').on('keyup', function(event) {
        event.preventDefault();
        $(this).val()!=''?$('#skipError').text('').hide():$('#skipError').text('验证码不能为空').show();;
    });

    $('#serviceLayer').on('click', function(event) {
        event.preventDefault();
        layer.open({
            type: 1,
            title: '安心签服务协议',
            area: ['950px', '600px'],
            shadeClose: true,
            move: false,
            scrollbar: true,
            skin:'register-skin',
            content: $('#serviceBox')
        });
    });
    $('#privacyLayer').on('click', function(event) {
        event.preventDefault();
        layer.open({
            type: 1,
            title: '隐私条款',
            area: ['950px', '600px'],
            shadeClose: true,
            move: false,
            scrollbar: true,
            skin:'register-skin',
            content: $('#privacyBox')
        });
    });
    $('#numberLayer').on('click', function(event) {
        event.preventDefault();
        layer.open({
            type: 1,
            title: 'CFCA数字证书服务协议',
            area: ['950px', '600px'],
            shadeClose: true,
            move: false,
            scrollbar: true,
            skin:'register-skin',
            content: $('#numberBox')
        });
    });

});