require("activityStyle/depository.scss");

let $depositoryContainer=$('#depositoryContainer'),
	topImg=require('../images/depository/top-img.jpg'),
	topImgphone=require('../images/depository/top-img-phone.jpg');

// 顶部banner
$depositoryContainer.find('.top-item .media-pc').attr('src',topImg).siblings('.media-phone').attr('src',topImgphone);



$('.detail-btn span',$depositoryContainer).on('click', function(event) {
	event.preventDefault();
	$(this).hasClass('active')?$(this).removeClass('active').text('展开内容').parent().siblings('.detail-media').hide():$(this).addClass('active').text('收起内容').parent().siblings('.detail-media').show();
});