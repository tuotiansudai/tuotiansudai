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
		amonut=parseFloat($leftPro.attr('data-amout'))+parseFloat($rightPro.attr('data-amout'));
	if(parseFloat($leftPro.attr('data-amout'))!=0 && parseFloat($rightPro.attr('data-amout'))!=0){
		$leftPro.css('width', (parseFloat($leftPro.attr('data-amout'))/amonut)*100+'%');
		$rightPro.css('width', (parseFloat($rightPro.attr('data-amout'))/amonut)*100+'%');
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
	        },function(data) {
	        	if(data=='SUCCESS'){
					$self.addClass('active').find('.person-num').text(function(el,num){
						return parseInt(num)+1
					});
					setTimeout(function(){
						$self.removeClass('active');
					},3000);
	        	}else{
	        		layer.msg('您已支持'+(data=='SWEET'?'甜':'咸')+'粽子了哦');
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
