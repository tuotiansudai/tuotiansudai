require('mWebStyle/investment/exchange_coupon.scss');
let commonFun = require('publicJs/commonFun');
$('#submitCode').on('click', function() {
    var exchangeCode = $('#couponByCode').val(),
        $errorText=$('#errorText');
    $(this).prop('disabled',true);
    if ($.trim(exchangeCode).length != 14) {
        commonFun.CommonLayerTip({
            btn: ['确定'],
            area:['280px', '150px'],
            content: `<div class="record-tip-box"><b class="pop-title">温馨提示</b> <p>兑换码不正确</p></div> `,
        },function() {
            layer.closeAll();
        })
        $('#couponByCode').val('');
        $("#submitCode").removeAttr("disabled");
    } else {
        $.ajax({
            url: '/m/'+exchangeCode+'/exchange',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8'
        }).done(function(data){
            $("#submitCode").removeAttr("disabled");
            var message = data.message;
            if (data.status) {
                commonFun.CommonLayerTip({
                    btn: ['确定'],
                    area:['280px', '150px'],
                    content: `<div class="record-tip-box"><b class="pop-title">温馨提示</b> <span>${message}</span></div> `,
                },function() {
                    layer.closeAll();
                })
                $('#couponSuccess').show();

            } else {

                $('#couponByCode').val('');
            }
        }).fail(function() {
            $("#submitCode").removeAttr("disabled");
            commonFun.CommonLayerTip({
                btn: ['确定'],
                area:['280px', '150px'],
                content: `<div class="record-tip-box"><b class="pop-title">温馨提示</b> <p>兑换失败，请重试</p></div> `,
            },function() {
                layer.closeAll();
            })
            $('#couponByCode').val('');
        });
    }
});
$('#couponByCode').on('keyup',function () {
    var exchangeCode = $('#couponByCode').val();

    if ($.trim(exchangeCode).length == 14) {
        $("#submitCode").removeAttr("disabled");
    }else {

    }
})
//
$('#iconExchangeCoupon').on('click',function () {
    location.href='/m/coupon-exchange_m.ftl'
})
$('#iconExchangeSuccess').on('click',function () {
    $('#couponSuccess').hide();
})
