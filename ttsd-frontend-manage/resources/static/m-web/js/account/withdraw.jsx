require('mWebStyle/account/withdraw.scss');


let $withdrawContainer = $('#withdrawContainer'),
    $amount = $('#amount');


$amount.on('focusout', function (event) {
    event.preventDefault();
    let $self = $(this),
        isMoney = /^\d+(?=\.{0,1}\d+$|$)/;
    if ($self.val() != '' && isMoney.test($self.val())) {
        $('#toCash').prop('disabled', false);
    } else {
        $('#toCash').prop('disabled', true);
        $self.val('');
    }
});