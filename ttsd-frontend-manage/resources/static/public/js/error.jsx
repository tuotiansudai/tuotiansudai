define([], function () {
    let commonFun = require('publicJs/commonFun');

    let $errorContainer = $('#errorContainer');
    if ($errorContainer.length) {
        var $jumpTip = $('.jump-tip i', $errorContainer);
        commonFun.countDownLoan({
            btnDom: $jumpTip,
            time: 10,
            textCounting: ''
        }, function () {
            window.location = "/";
        });
    }
})
