require('mWebStyle/account/bank_card_manage.scss');

let $bankCardManage = $('#bankCardManage'),
    $bankCardSelect = $('#bankCardSelect');
$('#iconBindcard').on('click',function () {
    location.href = '/m/account'
})
if ($bankCardManage.length) {
    let $bankColumn = $('.bank-column', $bankCardManage),
        bankForm = globalFun.$('#bankForm'),
        cardNumberDom = globalFun.$('#cardNumber'),
        $submitBtn = $(bankForm).find('button');
    isDisabledButton();

    //持卡人提示
    $bankCardManage.find('.icon-notice').on('click', function () {
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['280px', '180px'],
            shadeClose: false,
            skin: 'tip-square-box',
            btn: ['我知道了'],
            content: $('.tip-user-info')
        }, function () {
            layer.closeAll();
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
            bankNumber = bankForm.cardNumber.value;
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



