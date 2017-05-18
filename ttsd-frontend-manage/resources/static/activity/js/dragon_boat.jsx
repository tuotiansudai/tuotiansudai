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
	let $self=$(this),
		group=$self.attr('data-group');


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
        		layer.msg('您已支持'+data=='SWEET'?'甜':'咸'+'粽子了哦');
        	}
        }
    );
	
});
