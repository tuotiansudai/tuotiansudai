require('mWebStyle/account/bank_card_manage.scss');
let commonFun = require('publicJs/commonFun');
let $bankCardManage = $('#bankCardManage'),
    $bankCardSelect = $('#bankCardSelect');
$('#iconBindcard').on('click',function () {
    location.href=document.referrer;
})
if ($bankCardManage.length) {
    let $bankColumn = $('.bank-column', $bankCardManage),
        bankForm = globalFun.$('#bankForm'),
        cardNumberDom = globalFun.$('#cardNumber'),
        $submitBtn = $(bankForm).find('button');
    isDisabledButton();

    //持卡人提示
    $bankCardManage.find('.icon-notice').on('click', function () {
        $('.shade_mine').show();
        commonFun.CommonLayerTip({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['280px', '180px'],
            shadeClose: false,
            shade: 0,
            skin: 'tip-square-box',
            btn: ['我知道了'],
            content: $('.tip-user-info')
        }, function () {
            layer.closeAll();
            $('.shade_mine').hide();
        });
    });

    $bankColumn.on('click', function () {
        $bankCardManage.hide();
        $bankCardSelect.show();
    });

    !function () {
        cardNumberDom.onkeyup = function (event) {
            isDisabledButton();
        }
    }();

    //按钮是否点亮
    function isDisabledButton() {
        let bankName = $bankCardManage.find('.bank-show').html().trim() !== '请选择银行',
            bankNumber = bankForm.cardNumber.value.replace(/\s+/g, "");
        let isDisableBtn = bankName && /^(\d{15,})$/.test(bankNumber);

        $submitBtn.prop('disabled', !isDisableBtn);
    }
}

if ($bankCardSelect.length) {
    let $bankList = $('.bank-list', $bankCardSelect);

    $bankList.find('li').on('click', function () {
        let $this = $(this);
        $this.addClass('checked').siblings('li').removeClass('checked');
        let thisBank = $this.find('span').html();
        $bankCardManage.find('.bank-show').removeClass('key');
        $bankCardManage.find('.bank-show').html(thisBank);

        $bankCardManage.show();
        $bankCardSelect.hide();
    })
}

$('#cardNumber').on('keyup',(e) => {

    $('#cardNumber').attr('maxLength',29);

    if (e.keyCode != 8) {
        if ($('#cardNumber').val().length === 4 || $('#cardNumber').val().length === 9 || $('#cardNumber').val().length === 14 || $('#cardNumber').val().length === 19 || $('#cardNumber').val().length === 24) {
            let text = $('#cardNumber').val() + ' ';
            $('#cardNumber').val(text);
        }
    }
    else {
        if ($('#cardNumber').val().length === 5) {
            let text = $('#cardNumber').val().substring(0,4);
            $('#cardNumber').val(text);
        }
        else if ($('#cardNumber').val().length === 10) {
            let text = $('#cardNumber').val().substring(0,9);
            $('#cardNumber').val(text);
        }
        else if ($('#cardNumber').val().length === 15) {
            let text = $('#cardNumber').val().substring(0,14);
            $('#cardNumber').val(text);
        }
        else if ($('#cardNumber').val().length === 20) {
            let text = $('#cardNumber').val().substring(0,19);
            $('#cardNumber').val(text);
        }
        else if ($('#cardNumber').val().length === 25) {
            let text = $('#cardNumber').val().substring(0,24);
            $('#cardNumber').val(text);
        }
    }
});
$('#cardNumber').on("paste",(e) => {
    let pastedText = undefined;
    if (window.clipboardData && window.clipboardData.getData) { // IE
        pastedText = window.clipboardData.getData('Text');
    } else {
        pastedText = e.originalEvent.clipboardData.getData('Text'); //e.clipboardData.getData('text/plain');
    }
    let inputVal = pastedText.replace(/\s+/g, "");
    //let text = inputVal.substring(0,4) + ' ' +  inputVal.substring(4,8) + ' ' + inputVal.substring(8,12) + ' ' + inputVal.substring(12,16) + ' ' + inputVal.substring(16,18);
    let spaceNum = parseInt(inputVal.length / 4);
    let remainder = inputVal.length % 4;
    let maxLength = spaceNum + inputVal.length;
    $('#cardNumber').attr('maxLength',maxLength);
    let text = '';
    for (var i = 0;i < spaceNum;i++) {
        text += inputVal.substring(i * 4,(i + 1) * 4) + ' ';
    }
    if (remainder) {
        text += inputVal.substring(i * 4,i * 4 + remainder);
    }
    $('#cardNumber').val(text);
});
$('.btn-wap-normal').on('click',(e) => {
    e.preventDefault();
    $('#cardNumber').val($('#cardNumber').val().replace(/\s+/g, ""));
    $('#bankForm').submit();
});

