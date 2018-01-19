require('mWebStyle/account/personal_profile.scss');

let $bindCardBtn = $('#perCardUnboundCard'),
$goBackIcon=$('#goBackIcon');

$bindCardBtn.on('click', function () {
    window.location.href='/m/bind-card';
});

$goBackIcon.on('click', function (event) {
    location.href="/m/account";
});
