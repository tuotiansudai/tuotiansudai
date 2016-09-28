require(['jquery','layerWrapper','logintip','copyclip'], function($,layer) {

	var $shareReward=$('#shareRewardContainer'),
		$inviteFriend=$('.invite-box-friend',$shareReward),
		$popWid=$('.pop-layer-out'),
		$toIdentification=$('.to-identification',$shareReward),
		$copyButton=$('.copy-button',$shareReward),
		$copyLinkbox=$('.input-invite',$shareReward);

	$('.btn-to-close',$popWid).on('click',function() {
		layer.closeAll();
	});
		//已登录已认证,复功能
		var client = new ZeroClipboard($copyButton);
		var copyLinkVal=$('.copy-link',$copyLinkbox).text();
		client.on( "ready", function( readyEvent ) {
			client.on( "aftercopy", function( event ) {
				// event.data["text/plain"]
				layer.msg('复制成功');

			} );
		} );


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