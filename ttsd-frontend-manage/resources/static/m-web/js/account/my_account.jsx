require('mWebStyle/account/my_account.scss');

let $myAccount = $('#myAccount'),
    $summaryBox = $('.summary-box',$myAccount);

$summaryBox.on('click',function() {
    let $this = $(this);

    let key = $this.data('key');
    location.href = 'overview.ftl?key='+key;
});