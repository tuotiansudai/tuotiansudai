require("activityStyle/icp_intro.scss");

let topimg=require('../images/icp-intro/top-img.jpg'),
	topimgphone=require('../images/icp-intro/top-img-phone.jpg'),
	$icpIntroContainer=$('#icpIntroContainer');

$icpIntroContainer.find('.top-item .media-pc').attr('src',topimg).siblings('.media-phone').attr('src',topimgphone);