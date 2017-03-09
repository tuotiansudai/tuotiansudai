require(['jquery','layerWrapper','clipboard','commonFun','logintip','md5','qrcode'], function($,layer,clipboard,commonFun) {

	window['Clipboard']=clipboard;

	var $shareReward=$('#shareRewardContainer'),
		$popWid=$('.pop-layer-out'),
		$copyButton=$('.copy-button',$shareReward);

	$('.btn-to-close',$popWid).on('click',function() {
		layer.closeAll();
	});
	if($copyButton.length) {


		//已登录已认证,复制功能
		var $clipboardText=$('.input-invite',$shareReward);
		var mobile=$clipboardText.data('mobile')+'',
			md5Mobile=$.md5(mobile);

        if (window["context"] == undefined) {
            if (!window.location.origin) {
                window.location.origin = window.location.protocol + "//" + window.location.hostname+ (window.location.port ? ':' + window.location.port: '');
            }
        }

		var md5String=commonFun.compile(md5Mobile,mobile),
			origin=location.origin;

		$clipboardText.val(origin+'/activity/invite-friend?referrer='+md5String);

		//动态生成二维码
		$('.img-code',$shareReward).qrcode(origin+'/activity/app-share?referrerMobile='+mobile);

		/*复制链接*/

		(function($) {
			var clipboard = new Clipboard('.copy-button');
			clipboard.on('success', function(e) {
				layer.msg("复制成功");
				e.clearSelection();
			});
			clipboard.on('error', function(e) {
				layer.msg("复制失败");
			});
		})(jQuery);
	}
});