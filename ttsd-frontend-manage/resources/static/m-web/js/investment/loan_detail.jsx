require('mWebStyle/investment/experience_detail.scss');
require('mWebStyle/investment/loan_detail.scss');

let $loanDetail = $('#loanDetail'),
    $iconHelp = $('.icon-help',$loanDetail);

$iconHelp.on('click',function() {
    $('.invest-refer-box',$loanDetail).toggle();
})

$('#toInvest').on('click',function () {
    location.href="/m/"
})