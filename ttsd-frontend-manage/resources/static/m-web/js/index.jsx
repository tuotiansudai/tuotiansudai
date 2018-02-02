require('mWebStyle/home_page.scss');
require('webJs/plugins/autoNumeric');
let commonFun = require('publicJs/commonFun');
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

//点击进入相应的标的详情
$('[data-url]').on('click',function(event) {
    event.preventDefault();
    event.stopPropagation();
   if(!$(this).hasClass('btn-invest')&&!$(this).hasClass('btn-normal')||$(this).hasClass('preheat-btn')){
        let $this=$(this),
            url=$this.data('url');
        location.href=url;
   }

});

//开标倒计时

    let $preheat=$('.preheat',$homePageContainer);

    function countDownLoan(domElement) {
        return $(domElement).each(function () {
            let $this = $(this);
            let countdown=$this.data('time');
            if(countdown > 0) {
                let timer= setInterval(function () {
                    let $minuteShow=$this.find('.minute_show'),
                        $secondShow=$this.find('.second_show'),
                        minute=Math.floor(countdown/60),
                        second=countdown%60;
                    if (countdown == 0) {
                        //结束倒计时
                        clearInterval(timer);
                        $('.preheat-status').removeClass('preheat-btn').text('立即投资');
                        $this.remove();
                    }
                    minute=(minute <= 9)?('0' + minute):minute;
                    second=(second <= 9)?('0' + second):second;
                    $minuteShow.text(minute);
                    $secondShow.text(second);
                    countdown--;
                },1000);
            }

        });
    };
    countDownLoan($preheat);


let $appContainer = $('.app-container')
$('.close-app').click(function (e) {
    e.stopPropagation();
    $appContainer.hide();
})
$('.open-app').on('click',function () {
    if(commonFun.phoneModal() == 'android'){
        location.href = 'http://tuotiansudai.com/app/tuotiansudai.apk';
    }else if(commonFun.phoneModal() == 'ios'){
       location.href = 'https://itunes.apple.com/cn/app/id1039233966';
    }
})
let $amontDom = $('.money');
$amontDom.autoNumeric('init');

//点击btn跳转到购买
$('.goToDetail').on('click',function (e) {
    e.preventDefault();
    e.stopPropagation();
    var url = $(this).data('url')
    $.when(commonFun.isUserLogin())
        .done(function () {
            location.href = url+'#buyDetail';
        }).fail(function () {
        location.href = '/m/login'
    })

})

$('.goToExDetail').on('click',function (e) {
    e.preventDefault();
    e.stopPropagation();
    var url = $(this).data('url')
    $.when(commonFun.isUserLogin())
        .done(function () {
            location.href = url+'#applyTransfer';
        }).fail(function () {
        location.href = '/m/login'
    })

})

$('.goToTranDetail').on('click',function (e) {
    e.preventDefault();
    e.stopPropagation();

    var url = $(this).data('url')
    $.when(commonFun.isUserLogin())
        .done(function () {
            location.href = url+'#transferDetail';
        }).fail(function () {
        location.href = '/m/login'
    })

})
//邀请好友判断是否登录
$('.inviting-friend').click(function () {
    $.when(commonFun.isUserLogin())
        .done(function () {
            location.href = '/m/about/refer-reward';
        }).fail(function () {
        location.href = '/m/login'
    })
})
//
commonFun.calculationFun();
