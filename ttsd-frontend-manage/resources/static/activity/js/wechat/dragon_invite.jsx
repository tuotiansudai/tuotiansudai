require("activityStyle/wechat/dragon_invite.scss");

let $wechatInvite = $('#wechatInvite');

let redBag=require('../../images/dragon-boat/red-bag.png'),
	$redBag=$('#redBag');

$redBag.attr('src',redBag);



$('#takeRed',$wechatInvite).on('click', function(event) {
	event.preventDefault();
	$('.container-item',$wechatInvite).removeClass('active');
	$('.red-detail-container',$wechatInvite).addClass('active');
});
