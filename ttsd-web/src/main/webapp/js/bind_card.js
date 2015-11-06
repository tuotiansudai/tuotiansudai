require(['jquery', 'layer', 'csrf'], function ($, layer) {
    $(function () {
        var $bindCardBox=$('#bindCardBox'),
            $inputBankcard=$('.input-bankcard',$bindCardBox);

        $inputBankcard.keyup(function() {
            var $this=$(this),
                thisVal=this.value,
                reg_bank = /^\d{16,19}$/;
            if(reg_bank.test(thisVal)) {
                $this.next().find('input:submit').prop('disabled',false).addClass('btn-normal');
            }
            else {
                $this.next().find('input:submit').prop('disabled',true).addClass('btn-normal');
            }

        });

        //select bank
        var _bank = $('.select-bank li');
        _bank.click(function(){
            var text = $(this).find('input').attr('data-name');
            $('.jq-bank').val(text);
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