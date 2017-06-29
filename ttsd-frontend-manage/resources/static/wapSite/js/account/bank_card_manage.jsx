require('wapSiteStyle/account/bank_card_manage.scss');

let $bankCardManage = $('#bankCardManage'),
    $bankCardSelect = $('#bankCardSelect');

if($bankCardManage.length) {
    $bankColumn = $('.bank-column',$bankCardManage);

    let bankInfo = sessionStorage.getItem('bankInfo');
    if(bankInfo) {
        $bankColumn.find('.bank-info').text(bankInfo).removeClass('key');
    }

    $bankColumn.on('click',function() {
        location.href='/myAccount/bank-card-select.ftl';
    });
}


if($bankCardSelect.length) {
    let $bankList = $('.bank-list',$bankCardSelect);
    $bankList.find('li').on('click',function() {
        let  $this = $(this);
        $this.addClass('checked').siblings('li').removeClass('checked');
        let thisBank = $this.find('span').html();

        sessionStorage.setItem("bankInfo",thisBank);
    })
}



