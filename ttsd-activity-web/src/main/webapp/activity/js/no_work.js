/**
 * [name]:no work today
 * [author]:xuqiang
 * [date]:2016-11-18
 */
require(['jquery', 'layerWrapper', 'template', 'logintip','jquery.validate', 'jquery.validate.extension', 'jquery.form', 'jquery.ajax.extension','commonFun','register_common'],
	function($, layer, tpl) {
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