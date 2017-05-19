require("activityStyle/dragon_boat.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
let sourceKind = globalFun.parseURL(location.href);

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
		amonut=parseInt(leftAmount)+parseInt(rightAmount);

	if(parseInt(leftAmount)==0 && parseInt(rightAmount)==0){
		$leftPro.css('width', '50%');
		$rightPro.css('width', '50%');

	}else{
        $leftPro.css('width', (parseInt(leftAmount)/amonut)*100+'%');
        $rightPro.css('width', (parseInt(rightAmount)/amonut)*100+'%');
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
        if (sourceKind.params.source == 'app') {
            location.href = "app/tuotian/login";
        }else{
            //判断是否需要弹框登陆
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: ['auto', 'auto'],
                content: $('#loginTip')
            });
        }
    });
});
