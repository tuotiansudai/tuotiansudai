//cnzz统计
function cnzzPushConstructor() {
    this.trackClick = function (category, action, label) {
        _czc.push(['_trackEvent', category, action, label]);
    }
}
cnzzPush = new cnzzPushConstructor();
