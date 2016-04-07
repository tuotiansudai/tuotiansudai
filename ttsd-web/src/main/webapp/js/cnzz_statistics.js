cnzzPush = function () {
}
cnzzPush.prototype.trackClick = function (category, action, label) {
    console.log("category==" + category);
    _czc.push(['_trackEvent', category, action, label]);
}


cnzzPush = new cnzzPush();