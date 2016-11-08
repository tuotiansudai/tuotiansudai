
function cnzzPushConstructor() {
    this.trackClick=function(category, action, label) {
        var test=window.testlaney;
        _czc.push(['_trackEvent', category, action, label]);
    }
}
cnzzPush = new cnzzPushConstructor();