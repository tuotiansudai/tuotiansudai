require('wapSiteStyle/account/amount_detail.scss');

let $amountDetail = $('#amountDetail');

$amountDetail.find('.menu-category span').on('click',function() {
    let $this = $(this);
    $this.addClass('current').siblings('span').removeClass('current');
});
