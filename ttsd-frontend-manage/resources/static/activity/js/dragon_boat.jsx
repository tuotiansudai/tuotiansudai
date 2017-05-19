require("activityStyle/dragon_boat.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');

let $dragonBoatContainer=$('#dragonBoatContainer'),
	topImg=require('../images/dragon-boat/top-img.jpg'),
	topImgphone=require('../images/dragon-boat/top-img-phone.jpg');

// 顶部banner
$dragonBoatContainer.find('.top-item .media-pc').attr('src',topImg).siblings('.media-phone').attr('src',topImgphone);



let $typeBtn=$('.type-btn',$dragonBoatContainer);

function setProgress(){
	let $progress=$('.progress-item',$dragonBoatContainer),
		$leftPro=$progress.find('.left-pro'),
		$rightPro=$progress.find('.right-pro'),
        leftAmount=$leftPro.attr('data-amout').replace(/,/g,''),
        rightAmount=$rightPro.attr('data-amout').replace(/,/g,''),
		amonut=parseFloat(leftAmount)+parseFloat(rightAmount);
	if(parseFloat(leftAmount)!=0 && parseFloat(rightAmount)!=0){
		$leftPro.css('width', (parseFloat(leftAmount)/amonut)*100+'%');
		$rightPro.css('width', (parseFloat(rightAmount)/amonut)*100+'%');
	}
}
setProgress();
$typeBtn.on('click', function(event) {
	event.preventDefault();
	let $self=$(this),
		group=$self.attr('data-group');

	$.when(commonFun.isUserLogin())
    .done(function() {
    	commonFun.useAjax({
	            url:"/activity/dragon/joinPK",
	            type:'POST',
	            data:{
	            	'group':group
	            }
	        }, function (data) {
                if (data == 'SUCCESS') {
                    $self.addClass('active').find('.person-num').text(function (el, num) {
                        return parseInt(num) + 1
                    });
                    setTimeout(function () {
                        $self.removeClass('active');
                    }, 3000);
                } else if (data == 'SWEET') {
                    layer.msg('您已支持甜粽子了哦');
                } else if (data == 'SALTY') {
                    layer.msg('您已支持咸粽子了哦');
                } else if (data == 'GAME_OVER') {
                    layer.msg('活动已结束');
                } else {
                    console.log(data);
                }
            }
	    );
    })
    .fail(function() {
        //判断是否需要弹框登陆
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['auto', 'auto'],
            content: $('#loginTip')
        });
    });
});
