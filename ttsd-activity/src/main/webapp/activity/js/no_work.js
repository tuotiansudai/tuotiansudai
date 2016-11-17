/**
 * [name]:give iphone7 activity
 * [author]:xuqiang
 * [date]:2016-10-20
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