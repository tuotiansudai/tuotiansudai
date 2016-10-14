require(['jquery','layerWrapper','commonFun','logintip','copyclip','md5','qrcode'], function($,layer) {

	var $shareReward=$('#shareRewardContainer'),
		$inviteFriend=$('.invite-box-friend',$shareReward),
		$popWid=$('.pop-layer-out'),
		$toIdentification=$('.to-identification',$shareReward),
		$copyButton=$('.copy-button',$shareReward);

	$('.btn-to-close',$popWid).on('click',function() {
		layer.closeAll();
	});
	if($copyButton.length) {
		//已登录已认证,复制功能
		var client = new ZeroClipboard($copyButton),
			$clipboardText=$('.input-invite',$shareReward);
		var mobile=$clipboardText.data('mobile')+'',
			md5Mobile=$.md5(mobile);
		var md5String=commonFun.compile(md5Mobile,mobile),
			origin=location.origin;

		$clipboardText.val(origin+'/activity/landing-page?referrer='+md5String);

		//动态生成二维码
		$('.img-code',$shareReward).qrcode('https://tuotiansudai.com/activity/app-share?referrerMobile='+mobile);

		/*复制链接*/
		client.on( "ready", function( readyEvent ) {
			client.on( "aftercopy", function( event ) {
				// event.data["text/plain"]
				layer.msg('复制成功');

			} );
		} );
	}

	//已登录未认证,去认证
	$toIdentification.on('click',function() {
		var $this=$(this);
			layer.open({
				type: 1,
				title: false,
				closeBtn:0,
				area: ['550px','410px'],
				shadeClose: true,
				move: false,
				scrollbar: true,
				skin:'pop-personal-win',
				content: $popWid
			});
	});

	cnzzPush.trackClick('200APP分享', '推荐奖励落地页', '页面加载');



});