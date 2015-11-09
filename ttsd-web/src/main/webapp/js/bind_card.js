require(['jquery', 'layer', 'csrf'], function ($, layer) {
    $(function () {
        var $bindCardBox=$('#bindCardBox'),
            $inputBankcard=$('.input-bankcard',$bindCardBox),
            $btnBindCard=$('.bind-card-submit',$bindCardBox),
            $selectCard=$('.select-bank',$bindCardBox),
            $FormOpenFastPay=$('.open-fast-pay-form',$bindCardBox),
            $btnOpenFastPay=$('.open-fast-pay',$FormOpenFastPay);

        $inputBankcard.keyup(function() {
            var $this=$(this),
                thisVal=this.value,
                reg_bank = /^\d{16,19}$/;
            if(reg_bank.test(thisVal)) {
                $btnBindCard.prop('disabled',false).addClass('btn-normal');
            }
            else {
                $btnBindCard.prop('disabled',true).addClass('btn-normal');
            }
        });
        /*开通快捷支付*/
        $btnOpenFastPay.click(function(){
            $FormOpenFastPay.submit();
            layer.open({
                type: 1,
                title: '登录到联动优势支付平台充值',
                area: ['560px', '270px'],
                shadeClose: true,
                content: $('#pop-bind-card')
            });
        });

        //绑卡提交
        $btnBindCard.click(function(){
            layer.open({
                type: 1,
                title: '登录到联动优势支付平台充值',
                area: ['520px', '290px'],
                shadeClose: true,
                content: $('#pop-fast-pay')
            });
        });


    });
});