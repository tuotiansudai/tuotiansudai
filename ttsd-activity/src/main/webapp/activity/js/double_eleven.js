require(['jquery', 'underscore','layerWrapper', 'template','jquery.ajax.extension','commonFun','register_common'], function($,_, layer, tpl) {
	$(function() {
		var browser = commonFun.browserRedirect();

		
		if (browser == 'mobile') {
            var urlObj=commonFun.parseURL(location.href);
            if(urlObj.params.tag=='yes') {
                $('.reg-tag-current').show();
            }
        }

	});
});