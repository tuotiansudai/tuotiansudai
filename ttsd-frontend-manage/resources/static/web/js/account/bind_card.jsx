require('webStyle/account/bind_card.scss');
let commonFun= require('publicJs/commonFun');

var $bindCardBox = $('#bindCardBox'),
    // $inputBankcard = $('.input-bankcard', $bindCardBox),
    $btnBindCard = $('.bind-card-submit', $bindCardBox),
    // $btnReplaceCard = $('.replace-card-submit', $bindCardBox),
    $FormOpenFastPay = $('.open-fast-pay-form', $bindCardBox),
    $btnOpenFastPay = $('.open-fast-pay', $FormOpenFastPay),
    $bankList=$('#bankList');


var u = navigator.userAgent;
var isInWeChat = /(micromessenger|webbrowser)/.test(u.toLocaleLowerCase());
var isIos = /(iPhone|iPad|iPod|iOS)/i.test(u);
if (isInWeChat && isIos) {
    $('#bind-card').removeAttr('target');
}


// $inputBankcard.keyup(function () {
//     if (/^\d+$/.test($(this).val())) {
//         $btnBindCard.prop('disabled', false);
//         $btnReplaceCard.prop('disabled', false);
//     } else {
//         $btnBindCard.prop('disabled', true);
//         $btnReplaceCard.prop('disabled', true);
//     }
// });

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
    // layer.open({
    //     type: 1,
    //     title: '登录到联动优势支付平台充值',
    //     area: ['520px', '290px'],
    //     shadeClose: true,
    //     content: $('#pop-bind-card')
    // });
    alert(9)
});

// $bankList.on('mouseover mouseout', function(event) {
//     event.preventDefault();
//     $(this).hasClass('active')?$(this).removeClass('active'):$(this).addClass('active');
// });


$('.bank-checked',$bindCardBox).on('click',function() {
    commonFun.useAjax({
        url: '/bind-card/limit-tips',
        data: {"bankCode":this.value},
        type: 'GET'
    },function(data) {
        if(data !=''){
            $('.limit-tips',$bindCardBox).html('<span>'+ data +'</span><i class="fa fa-question-circle-o text-b" title="限额由资金托管方提供，如有疑问或需要换卡，请联系客服400-169-1188"></i>');
        }else{
            $('.limit-tips',$bindCardBox).html('');
        }
    });

});

let metaViewPort = $('meta[name=viewport]');//
metaViewPort.remove()
$('head').prepend($('<meta name="viewport" content="width=1024,user-scalable=yes" />'));