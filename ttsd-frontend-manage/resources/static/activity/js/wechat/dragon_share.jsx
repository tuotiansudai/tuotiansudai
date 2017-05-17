require("activityStyle/wechat/dragon_share.scss");
let Clipboard= require('publicJs/plugins/clipboard');
require('publicJs/plugins/jquery.qrcode.min');


window['Clipboard']=Clipboard;
let redBag=require('../../images/dragon-boat/red-bag.png'),
	$redBag=$('#redBag');

$redBag.attr('src',redBag);


(function($) {
	let clipboard = new Clipboard('.copy-btn');
	clipboard.on('success', function(e) {
		layer.msg("复制成功");
		e.clearSelection();
	});
	clipboard.on('error', function(e) {
		layer.msg("复制失败");
	});
})(jQuery);










