require('webStyle/investment/transfer_apply_detail.scss');
require('webJsModule/coupon_alert');
//投资计算器和意见反馈
require('webJsModule/red_envelope_float');
//安心签协议
require('webJsModule/anxin_agreement');

var $createForm = $('#createForm'),
    $agreement = $createForm.find('.agreement'),
    $isAnxinAuthenticationRequired=$('#isAnxinAuthenticationRequired');
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
        applyTip();
    }
});

function applyTip(){
    layer.open({
        type: 1,
        title: '温馨提示',
        btn: ['确定', '取消'],
        area: ['330px'],
        shadeClose: true,
        content: '<p class="pad-m tc">是否确认转让？</p>',
        btn1: function () {
            var transferInvestId = $('#transferInvestId').val();
            $.ajax({
                url: '/transfer/invest/' + transferInvestId + '/is-transferable',
                type: 'GET'
            }).done(function (data) {
                if (true == data.data.status) {
                    if($isAnxinAuthenticationRequired.val()=='false'){
                        sendData();
                        layer.closeAll();
                    }else{
                        if($('#isAnxinUser').val() == 'true'){
                            getSkipPhoneTip();
                        }else{
                            $('#skipCheck').val() == 'true'?getSkipPhoneTip():$agreement.next('span.error').show();;
                        }
                        return false;
                    }

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
        $createForm.find('button[type="submit"]').prop('disabled', false);
    }
    else {
        className = 'fa fa-square-o';
        $('#skipCheck').length>0?$('#skipCheck').val('false'):false;
        $createForm.find('button[type="submit"]').prop('disabled', true);
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
    $('#getSkipCode').prop('disabled',true);
    $('#microPhone').css('visibility', 'hidden');
    $.ajax({
        url: '/anxinSign/sendCaptcha',
        type: 'POST',
        dataType: 'json',
        data:{
            isVoice:type
        }
    })
        .done(function(data) {
            $('#getSkipCode').prop('disabled',false);
            $('#microPhone').css('visibility', 'visible');
            if(data.success) {
                countDown();
                Down = setInterval(countDown, 1000);
            }
            else {
                layer.msg('请求失败，请重试或联系客服！');
            }
        })
        .fail(function() {
            $('#getSkipCode').prop('disabled',false);
            $('#microPhone').css('visibility', 'visible');
            layer.msg('请求失败，请重试或联系客服！');
        });
}
//countdown skip
function countDown() {
    $('#getSkipCode').val(num + '秒后重新获取').prop('disabled',true);
    $('#microPhone').css('visibility', 'hidden');
    if (num == 0) {
        clearInterval(Down);
        $('#getSkipCode').val('重新获取验证码').prop('disabled',false);
        $('#microPhone').css('visibility', 'visible');
        num=60;
    }else{
        num--;
    }

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
                if(data.success){
                    $('#isAnxinUser').val('true') && $('.skip-group').hide();
                    if(data.skipAuth=='true'){
                        $isAnxinAuthenticationRequired.val('false');
                    }
                    skipSuccess();
                }else{
                    $('#skipError').text('验证码不正确').show();
                }
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
    setTimeout(function(){
        $('#skipSuccess').hide();
        $('#skipPhoneCode').val('');
        num=0;
        sendData();
    },3000)
}

$('#skipPhoneCode').on('keyup', function(event) {
    event.preventDefault();
    $(this).val()!=''?$('#skipError').text('').hide():$('#skipError').text('验证码不能为空').show();;
});

