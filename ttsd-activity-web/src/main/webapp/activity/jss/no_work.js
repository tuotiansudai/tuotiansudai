/**
 * [name]:no work today
 * [author]:xuqiang
 * [date]:2016-11-18
 */
require(['jquery', 'layerWrapper', 'template', 'logintip','jquery.validate', 'jquery.validate.extension', 'jquery.form', 'jquery.ajax.extension','commonFun','register_common'],
	function($, layer, tpl) {
	$(function() {
		var browser = globalFun.browserRedirect(),
			$inviteBtn=$('.invite-btn-group').find('.invite-item');

		if (browser == 'mobile') {

			var urlObj=globalFun.parseURL(location.href);
			if(urlObj.params.tag=='yes') {
				$('.reg-tag-current').show();

			}
		}
		$inviteBtn.attr('data-expired')=='true'?$inviteBtn.attr('href','javascript:void(0)').addClass('end-time'):false;



	});
});