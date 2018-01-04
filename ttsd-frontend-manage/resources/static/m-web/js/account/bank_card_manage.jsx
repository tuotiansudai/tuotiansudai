require('mWebStyle/account/bank_card_manage.scss');

let $bankCardManage = $('#bankCardManage'),
    $bankCardSelect = $('#bankCardSelect');

if($bankCardManage.length) {
    let $bankColumn = $('.bank-column',$bankCardManage),
        bankForm = globalFun.$('#bankForm'),
        cardNumberDom = globalFun.$('#cardNumber'),
        $submitBtn = $(bankForm).find('button');

    let bankInfo = sessionStorage.getItem('bankInfo');
    let cardNumber = sessionStorage.getItem('cardNumber');
    if(bankInfo) {
        $bankColumn.find('.bank-info').html(bankInfo).removeClass('key');
        bankForm.bankName.value = bankInfo;
    }
    cardNumberDom.value = cardNumber || '';
    isDisabledButton();

    //持卡人提示
    $bankCardManage.find('.icon-notice').on('click',function() {
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['280px', '180px'],
            shadeClose: false,
            skin: 'tip-square-box',
            btn: ['我知道了'],
            content: $('.tip-user-info')
        },function(index, layero){
          // alert('pp');
        });
    });

    $bankColumn.on('click',function() {
        let  cardNumber = cardNumberDom.value;
        sessionStorage.setItem("cardNumber",cardNumber);
        window.location.href='/myAccount/bank-card-select.ftl';
    });

    !function () {
        cardNumberDom.onkeyup = function (event) {
            isDisabledButton();
            if((event.which >= 48 && event.which <= 57) ||(event.which >= 96 && event.which <= 105 )){
                var v = this.value;
                if(/\S{5}/.test(v)){
                    this.value = v.replace(/\s/g, '').replace(/(.{4})/g, "$1 ");
                }
            };
        }
    }();

    //按钮是否点亮
    function isDisabledButton() {
        let bankName = bankForm.bankName.value,
            bankNumber = bankForm.cardNumber.value.replace(/ /g,'');
        let isDisableBtn = bankName && /^(\d{15,})$/.test(bankNumber);

        $submitBtn.prop('disabled', !isDisableBtn);
    }
}

if($bankCardSelect.length) {
    let $bankList = $('.bank-list',$bankCardSelect);
    let bankInfo = sessionStorage.getItem('bankInfo');
    let bankIndex = sessionStorage.getItem('bankIndex');

    if(bankInfo) {
        $bankList.find('li').eq(bankIndex).addClass('checked').siblings('li').removeClass('checked');
    }

    $bankList.find('li').on('click',function() {
        let  $this = $(this),
            index = $this.index();
        $this.addClass('checked').siblings('li').removeClass('checked');
        let thisBank = $this.find('span').html();

        sessionStorage.setItem("bankInfo",thisBank);
        sessionStorage.setItem("bankIndex",index);
        window.location.href='/myAccount/bank-card-manage.ftl';
    })
}



