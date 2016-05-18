require(['jquery', 'layerWrapper', 'jquery.ajax.extension'], function ($, layer) {
        var $bindCardBox = $('#bindCardBox'),
            $inputBankcard = $('.input-bankcard', $bindCardBox),
            $btnBindCard = $('.bind-card-submit', $bindCardBox),
            $btnReplaceCard = $('.replace-card-submit', $bindCardBox),
            $FormOpenFastPay = $('.open-fast-pay-form', $bindCardBox),
            $btnOpenFastPay = $('.open-fast-pay', $FormOpenFastPay);

        $inputBankcard.keyup(function () {
            if (/^\d+$/.test($(this).val())) {
                $btnBindCard.prop('disabled', false);
                $btnReplaceCard.prop('disabled', false);
            } else {
                $btnBindCard.prop('disabled', true);
                $btnReplaceCard.prop('disabled', true);
            }
        });

        /*开通快捷支付*/
        $btnOpenFastPay.click(function () {
            $FormOpenFastPay.submit();
            layer.open({
                type: 1,
                title: '开通快捷支付功能',
                area: ['560px', '190px'],
                closeBtn:0,
                shadeClose: false,
                content: $('#pop-fast-pay')
            });
        });

        //绑卡提交
        $btnBindCard.click(function () {
            layer.open({
                type: 1,
                title: '登录到联动优势支付平台充值',
                area: ['520px', '290px'],
                shadeClose: true,
                content: $('#pop-bind-card')
            });
        });

        $btnReplaceCard.click(function() {
            layer.open({
                type: 1,
                title: '登录到联动优势支付平台换卡',
                area: ['520px', '290px'],
                shadeClose: true,
                content: $('#pop-replace-card')
            });
        });

        $('#pop-replace-card .close-btn').on('click',function(){
            layer.closeAll();
        });

    });
