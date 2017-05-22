require("activityStyle/wechat/dragon_share.scss");
let Clipboard= require('publicJs/plugins/clipboard');


window['Clipboard']=Clipboard;
let redBag=require('../../images/dragon-boat/red-bag.png'),
	$redBag=$('#redBag'),
	$shareBtn=$('#shareBtn'),
	$shareCoupon=$('#shareCoupon');

$redBag.attr('src',redBag);

//判断终端是ios 还是 android
let equipment = globalFun.equipment(),
    isIos = equipment.ios;

if(isIos) {
    $('.copy-btn',$shareCoupon).text('优惠券兑换码');

}

let clipboard = new Clipboard('.copy-btn');
clipboard.on('success', function(e) {
	layer.msg("复制成功");
	e.clearSelection();
});
clipboard.on('error', function(e) {
	layer.msg("复制失败");
});


// 分享红包给好友
$shareBtn.on('click', function(event) {
	event.preventDefault();
	$('#shareBox').show();
});

$('.share-close').on('click',function(event) {
    event.preventDefault();
    $('#shareBox').hide();
});







