require(['jquery', 'layer', 'csrf'], function ($, layer) {
    $(function () {
        //select bank
        var _bank = $('.select-bank li');
        _bank.click(function(){
            var text = $(this).find('input').attr('data-name');
            $('.jq-bank').val(text);
        });

        //校验银行卡号
        var reg_bank = /^\d{16,19}$/;
        $('.input-bankcard').blur(function(){
           var _val = $(this).val();
            if(reg_bank.test(_val)){
                $('.btn-ok').removeClass('grey').removeAttr('disabled');
            }else{
                $('.btn-ok').addClass('grey').attr('disabled','disabled');
            }
        });

        $('.open-fast-pay').click(function(){
            $(".open-fast-pay-form").submit();
            layer.open({
                type: 1,
                title: '登录到联动优势支付平台充值',
                area: ['560px', '270px'],
                shadeClose: true,
                content: $('#pop-bind-card')
            });
        });

        //绑卡提交
        $('.bind-card-submit').click(function(){
            layer.open({
                type: 1,
                title: '登录到联动优势支付平台充值',
                area: ['560px', '270px'],
                shadeClose: true,
                content: $('#pop-fast-pay')
            });
        });


    });
});