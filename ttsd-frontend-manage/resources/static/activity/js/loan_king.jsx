require("activityStyle/loan_king.scss");
require('activityJsModule/fast_register');

let topimg=require('../images/loan-king/top-img.jpg'),
	topimgphone=require('../images/loan-king/top-img-phone.jpg'),
	$loanKingContainer=$('#loanKingContainer');

$loanKingContainer.find('.top-item .media-pc').attr('src',topimg).siblings('.media-phone').attr('src',topimgphone);

let loanimg=require('../images/loan-king/loan-list.png'),
	loanmgphone=require('../images/loan-king/loan-list-phone.png');

$loanKingContainer.find('.loan-list .media-pc').attr('src',loanimg).siblings('.media-phone').attr('src',loanmgphone);