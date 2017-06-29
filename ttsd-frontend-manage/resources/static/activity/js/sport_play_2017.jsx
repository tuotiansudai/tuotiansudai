require("activityStyle/sport_play_2017.scss");
let commonFun= require('publicJs/commonFun');

let $sportPlayContainer = $('#sportPlayContainer');
let topimg=require('../images/2017/sport-play/top-img.jpg'),
    topimgPhone=require('../images/2017/sport-play/top-img-phone.jpg');

$sportPlayContainer.find('.top-img .media-pc').attr('src',topimg).siblings('.media-phone').attr('src',topimgPhone);

