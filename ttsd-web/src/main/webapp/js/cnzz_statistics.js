define(['jquery'], function ($) {
    var cnzzPush = function () {
    }
    cnzzPush.prototype.trackClick = function (category, action, label) {
        _czc.push(['_trackEvent', category, action, label]);
    }
    return cnzzPush;

});





