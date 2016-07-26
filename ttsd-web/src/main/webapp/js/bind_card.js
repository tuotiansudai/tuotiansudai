require(['jquery', 'layerWrapper', 'jquery.ajax.extension'], function ($, layer) {
        var $bindCardBox = $('#bindCardBox'),
            $inputBankcard = $('.input-bankcard', $bindCardBox),
            $btnBindCard = $('.bind-card-submit', $bindCardBox),
            $btnReplaceCard = $('.replace-card-submit', $bindCardBox),
            $FormOpenFastPay = $('.open-fast-pay-form', $bindCardBox),
            $btnOpenFastPay = $('.open-fast-pay', $FormOpenFastPay),
            $bankList=$('#bankList');

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

        $bankList.on('mouseover mouseout', function(event) {
            event.preventDefault();
            $(this).hasClass('active')?$(this).removeClass('active'):$(this).addClass('active');
        });

    });

    function selectBank(obj){
        $.ajax({
            url: '/bind-card/limit-tips',
            data: {"bankCode":obj},
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8'
        }).done(function (data) {
            if(data!=''){
                $('.limit-tips').html('<span>'+data+'</span><i class="fa fa-question-circle-o text-b" title="限额由资金托管方提供，如有疑问或需要换卡，请联系客服400-169-1188"></i>');
            }else{
               $('.limit-tips').html(''); 
            }
        }).fail(function (data) {
            console.log(data);
        });

    }