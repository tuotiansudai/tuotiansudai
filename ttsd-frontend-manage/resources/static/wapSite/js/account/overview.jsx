require('wapSiteStyle/account/overview.scss');


let $accountOverview = $('#accountOverview');

$accountOverview.find('.menu-category span').on('click',function() {
    let $this = $(this),
        index = $this.index();
    $this.addClass('current').siblings('span').removeClass('current');

    $('.overview-content').eq(index).show().siblings('.overview-content').hide();
});

