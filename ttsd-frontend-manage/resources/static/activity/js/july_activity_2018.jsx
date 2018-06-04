require("activityStyle/july_activity_2018.scss");
require('publicJs/plugins/jquery.qrcode.min');

require('swiper/dist/css/swiper.css')
let Swiper = require('swiper/dist/js/swiper.jquery.min');

var mySwiper = new Swiper ('.my-team-logos', {
    direction: 'horizontal',
    loop: false,
    autoplay:5000,
     slidesPerGroup: 5,
    slidesPerView:5,
    // loopedSlides:5,
    nextButton: '.nextBtn',
    prevButton: '.prevBtn',
    freeMode:true,
    pagination : '.swiper-pagination',
    paginationClickable:true
});
