require("activityStyle/dragon_boat.scss");
let commonFun= require('publicJs/commonFun');

let $dragonBoatContainer=$('#dragonBoatContainer'),
	topImg=require('../images/dragon-boat/top-img.jpg'),
	topImgphone=require('../images/dragon-boat/top-img-phone.jpg');

// 顶部banner
$dragonBoatContainer.find('.top-item .media-pc').attr('src',topImg).siblings('.media-phone').attr('src',topImgphone);


let $typeBtn=$('.type-btn',$dragonBoatContainer);

$typeBtn.on('click', function(event) {
	event.preventDefault();
	let $self=$(this);

	if(!$self.hasClass('disabled')){
		$self.addClass('active disabled').siblings().addClass('disabled');
		$self.find('.person-num').text(function(el,num){
			return parseInt(num)+1
		});
		setTimeout(function(){
			$self.removeClass('active');
		},3000);
	}else{
		layer.msg('您已支持甜粽子了哦');
	}
});
