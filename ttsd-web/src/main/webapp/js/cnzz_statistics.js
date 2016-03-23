define(['jquery'], function ($) {

    var cnzz_push = function(category,action,label) {
        _czc.push(["_trackEvent",category,action,label]);

    }
    return cnzz_push;
});


