require('mWebStyle/home_page.scss');

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
$('[data-url]',$homePageContainer).on('click',function(event) {
    event.preventDefault();
    let $this=$(this),
        url=$this.data('url');
    location.href=url;
});

//开标倒计时
(function() {
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
                        $this.parents('a').removeClass('preheat-btn').text('立即投资');
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

})();
