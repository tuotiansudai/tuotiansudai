require(['jquery', 'commonFun','jquery.ajax.extension', 'register_common'], function($,commonFun) {
    $(function() {
        var browser = globalFun.browserRedirect();
        if (browser == 'mobile') {
            var urlObj = globalFun.parseURL(location.href);
            if (urlObj.params.tag == 'yes') {
                $('.reg-tag-current').show();
            }
        }
    });
});