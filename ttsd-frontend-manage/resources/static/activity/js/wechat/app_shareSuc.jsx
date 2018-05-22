// document.cookie="registerMobile="+18810985132;
require('swiper/dist/css/swiper.css')
let Swiper = require('swiper/dist/js/swiper.jquery.min');
let param = JSON.parse('{"' + decodeURI(location.search.substring(1)).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g,'":"') + '"}')
$('#register_btn').on('click',() => {
    delCookie('registerMobile');
    location.href = '/activity/app-share?referrerMobile=' + param["referrerMobile"];
});

let getCookie = function(name) {
    let arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

let delCookie = function(name) {
    let exp = new Date();
    exp.setTime(exp.getTime() - 1);
    let cval = getCookie(name);
    if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
};

$('#fuliList').find('.swiper-slide').each(function (index,item) {
    let  _self = $(this);
    let imgUrl = require('../../images/landingpage/fuli'+(index+1)+'.png');
    let img = new Image();
    img.src = imgUrl;
    _self.append(img);
})

var mySwiper = new Swiper ('#fuliList', {
    direction: 'horizontal',
    loop: true,
    autoplay:5000,
    autoplayDisableOnInteraction:false,
    slidesPerView: 'auto',
    centeredSlides:true,
    spaceBetween: -20,
    loopAdditionalSlides:1,
    nextButton: '.prevBtn',
    prevButton: '.prevBtn',

});
let $prevBtn = $('.prevBtn'),
    $nextBtn = $('.nextBtn');

$prevBtn.on('click',function () {
    mySwiper.slidePrev();
})
$nextBtn.on('click',function () {
    mySwiper.slideNext();
})

