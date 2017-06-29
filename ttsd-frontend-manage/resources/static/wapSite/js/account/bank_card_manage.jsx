require('wapSiteStyle/account/bank_card_manage.scss');

let $bankCardManage = $('#bankCardManage'),
    $bankColumn = $('.bank-column',$bankCardManage);

$bankColumn.on('click',function() {

    location.href='/myAccount/bank-card-select.ftl';
});
