require('mWebStyle/account/personal_profile.scss');

let $bindCardBtn = $('#perCardUnboundCard'),
$goBackIcon=$('#goBackIcon'),
$toPerName = $('#toPerName');

$bindCardBtn.on('click', function () {

    if($(this).data('has-name') === true){
        window.location.href='/m/bind-card';
    }else{
        window.location.href='/m/register/account';
    }
});
$toPerName.on('click', function () {
    window.location.href='/m/register/account';
});

$goBackIcon.on('click', function (event) {
    location.href="/m/account";
});
