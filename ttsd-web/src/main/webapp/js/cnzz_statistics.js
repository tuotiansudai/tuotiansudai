var cnzzPushConstructor = function () {
};
cnzzPushConstructor.prototype.trackClick = function (category, action, label) {
    alert(category);
    _czc.push(['_trackEvent', category, action, label]);
};
cnzzPush = new cnzzPushConstructor();