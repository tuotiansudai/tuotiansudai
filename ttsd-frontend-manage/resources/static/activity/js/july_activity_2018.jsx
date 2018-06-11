require("activityStyle/july_activity_2018.scss");
require('publicJs/plugins/jquery.qrcode.min');

require('swiper/dist/css/swiper.css')

let sourceKind = globalFun.parseURL(location.href);
let equipment = globalFun.equipment();
let url;
if (sourceKind.port) {
    url = sourceKind.protocol + '://' + sourceKind.host + ':' + sourceKind.port;
} else {
    url = sourceKind.protocol + '://' + sourceKind.host;
}

let Swiper = require('swiper/dist/js/swiper.jquery.min');
let slideLen = 5;
if($(document).width() <= 1024){
    slideLen = 3;
}else {
    slideLen = 5;
}
var mySwiper = new Swiper ('.my-team-logos', {
    direction: 'horizontal',
    loop: true,
    autoplay:0,
     slidesPerGroup: slideLen,
    slidesPerView:slideLen,
    nextButton: '.nextBtn',
    prevButton: '.prevBtn',
    freeMode:true,
    pagination : '.swiper-pagination',
    paginationClickable:true
});
let $qrcodeBox = $('#qrcodeBox');
let qrcodeWidth = $('#qrcodeBox').width();
let qrcodeHeight = $('#qrcodeBox').height();
$qrcodeBox.qrcode({
    text: url + '/we-chat/active/authorize?redirect=/activity/super-scholar/view/question',
    width: qrcodeWidth,
    height: qrcodeHeight,
    colorDark: '#1e272e',
    colorLight: '#ffffff',
}).find('canvas').hide();
var canvas = $qrcodeBox.find('canvas').get(0);
$('#rqcodeImg').attr('src', canvas.toDataURL('image/jpg'))
