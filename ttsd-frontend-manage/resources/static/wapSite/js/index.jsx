require('wapSiteStyle/home_page.scss');

let $homePageContainer = $('#homePageContainer'),
    $imgScroll = $('.banner-img-list', $homePageContainer);
let viewport = globalFun.browserRedirect();

//首页大图轮播和最新公告滚动,单独打一个包方便cdn缓存
require.ensure(['webJsModule/image_show_slider'], function(require){
    let imageSlide = require('webJsModule/image_show_slider');

    let imgCount=$imgScroll.find('li').length;
    //如果是手机浏览器，更换手机图片
    if(imgCount>0 && viewport=='mobile') {
        $imgScroll.find('img').each(function(key,option) {
            let appUrl=option.getAttribute('data-app-img');
            option.setAttribute('src',appUrl);
        });
    }
    if(imgCount>0) {
        $imgScroll.find('li').eq(0).css({"z-index":2});
        let runimg=new imageSlide.runImg('bannerBox','30',imgCount);
        runimg.info();
        window.onload = function() {
            $('ul.countNum')[0].style.left=(document.body.scrollWidth - imgCount * 14)/2+'px';
        }
    }

},'imageSlider');


