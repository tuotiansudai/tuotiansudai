define(['jquery'], function ($) {
    var cnzzPush = function () {
    }
    cnzzPush.prototype.trackEvent = function (category, action, label) {
        _czc.push(['_trackEvent', category, action, label]);
    }
    return cnzzPush;

});





