require('mWebStyle/account/invest_detail.scss');
let commonFun = require('publicJs/commonFun');

$('#goBackIcon').on('click', function (e) {
    location.href = '/m/investor/invest-list';
});

$('.loan-detail-link, .invest-contract-link').on('click', function (e) {
    location.href = $(this).data('url');
});

