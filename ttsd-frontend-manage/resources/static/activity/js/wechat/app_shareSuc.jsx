// document.cookie="registerMobile="+18810985132;
let commonFun = require('publicJs/commonFun');
let urlObj = globalFun.parseURL(location.href);
let shareMobile = urlObj.params.mobileShare;
require('swiper/dist/css/swiper.css')
if ($(document).width() <= 1024) {
    commonFun.calculationRem(document,window);
}

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
    nextButton: '.nextBtn',
    prevButton: '.prevBtn',

});
$('.open-app').click(function (e) {
    commonFun.toDownloadApp();
})
$('.close-app').click(function (e) {
    e.stopPropagation();
    $('.app-container-landing').hide();
})
let mobileStr;
if(shareMobile){
    mobileStr = shareMobile.substring(0,3)+'****'+shareMobile.substring(8,11);
}
wx.ready(function () {
    wx.onMenuShareAppMessage({
        title: '明明可以自己偷偷赚钱，但我还是想叫上你', // 分享标题
        desc: '友谊的小船变巨轮，'+mobileStr+'送您6888元体验金，邀您一起来赚钱', // 分享描述
        link: webServer + '/activity/app-share?referrerMobile=' + mobileShare + '&come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: commonStaticServer + '/images/icons/red_ware_money.png', // 分享图标
    });

    wx.onMenuShareTimeline({
        title: '明明可以自己偷偷赚钱，但我还是想叫上你', // 分享标题
        desc: '友谊的小船变巨轮，'+mobileStr+'送您6888元体验金，邀您一起来赚钱', // 分享描述
        link: webServer + '/activity/app-share?referrerMobile=' + mobileShare + '&come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
        imgUrl: commonStaticServer + '/images/icons/red_ware_money.png', // 分享图标
    });
});



