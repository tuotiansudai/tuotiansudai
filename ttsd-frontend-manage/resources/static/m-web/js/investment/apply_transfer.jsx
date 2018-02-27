require('mWebStyle/investment/apply_transfer.scss');
require('mWebJsModule/anxin_agreement_pop');
let commonFun = require('publicJs/commonFun');
require('webJs/plugins/autoNumeric');

let $applyTransfer = $('#applyTransfer'),
    $btnWapNormal = $('.btn-wap-normal',$applyTransfer),
    $amountInputElement = $('.input-amount',$applyTransfer);

$applyTransfer.find('.bg-square-box').append(commonFun.repeatBgSquare(33));

$amountInputElement.autoNumeric('init');

$('.init-checkbox-style',$applyTransfer).initCheckbox(function(element) {
    var $parentBox=$(element).parents('.safety-status-box');
    //点击我已阅读并同意是否disable按钮
    $(element).hasClass('on');
});

function getInvestAmount() {
    var amount = 0;
    if (!isNaN($amountInputElement.autoNumeric("get"))) {
        amount = parseInt(($amountInputElement.autoNumeric("get") * 100).toFixed(0));
    }

    return amount;
}

function tipNotice() {

    layer.tips('建议金额:1,000,000.00~1,000,000.49元', '.input-amount', {
        tips: [1, '#F6AA16'],
        time: 0,
        maxWidth : 250
    });

    let $tips = $('.layui-layer-tips');
    $tips.css({'top':$tips.offset().top + 50});
}

$amountInputElement
    .on('keyup',function() {

        let value = getInvestAmount();
        if(/^(\d){4,}$/.test(value)) {
            $btnWapNormal.prop('disabled',false);
        } else {
            $btnWapNormal.prop('disabled',true);
        }
    })
    .on('focus',function() {
        tipNotice();
    })
    .on('focusout',function() {
        layer.closeAll();
    });

globalFun.$('#investForm').onsubmit = function() {


}





