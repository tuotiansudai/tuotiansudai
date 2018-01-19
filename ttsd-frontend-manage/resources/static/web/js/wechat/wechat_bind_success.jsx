let commonFun = require('publicJs/commonFun');

let $downTime = $('#downTime');

commonFun.countDownLoan({
    btnDom: $downTime,
    time: 5,
    isAfterText:'',
    textCounting: 's'
}, function () {
    $('#after').hide();
    window.location = $downTime.data('redirect');
});

$('#okButton').click(function () {
    window.location = $downTime.data('redirect');
});