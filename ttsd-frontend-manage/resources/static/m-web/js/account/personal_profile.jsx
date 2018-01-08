require('mWebStyle/account/personal_profile.scss');

let $bindCardBtn = $('#perCardUnboundCard');

$bindCardBtn.on('click', function () {
    window.location.href='/m/bind-card';
});