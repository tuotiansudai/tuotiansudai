require("activityStyle/depository.scss");
require('publicJs/plugins/jquery.SuperSlide.min');

let $depositoryContainer=$('#depositoryContainer'),
	topImg=require('../images/depository/top-img.jpg'),
	topImgphone=require('../images/depository/top-img-phone.jpg');

// 顶部banner
$depositoryContainer.find('.top-item .media-pc').attr('src',topImg).siblings('.media-phone').attr('src',topImgphone);


$('#slideBox').slide({
	mainCell:".bd ul",
	effect:"leftLoop",
	autoPlay:true
});