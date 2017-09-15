require('webJsModule/coupon_alert');
require('webStyle/home_page_v2.scss');
require('webStyle/spritecss/homepage.css');
//投资计算器和意见反馈
require('webJsModule/red_envelope_float');
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
    }
    let startMarquee2=new imageSlide.startMarquee();
    startMarquee2.init();
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
                        $this.parents('a').removeClass('preheat-btn').text('立即购买');
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

//预约投资,目前预约投资功能不需要，以后可能会需要

// require.ensure(['webJs/plugins/autoNumeric','publicJs/validator','publicJs/commonFun'], function(){
//     require('webJs/plugins/autoNumeric');
//
//     let commonFun= require('publicJs/commonFun');
//     let ValidatorForm= require('publicJs/validator');
//
//     let $bookInvestForm = $('.book-invest-form',$homePageContainer);
//     //初始化radio
//     $bookInvestForm.find('.init-radio-style').on('click', function () {
//         let $this = $(this);
//         $bookInvestForm.find('.init-radio-style').removeClass('on');
//         $bookInvestForm.find('input:radio').prop('checked', false);
//         $this.addClass("on");
//         $this.find('input:radio').prop('checked', true);
//     });
//
//     $('input.autoNumeric',$homePageContainer).autoNumeric();
//
//     //点击我要预约按钮
//     $('.book-invest-box',$homePageContainer).on('click',function(event) {
//         event.preventDefault();
//         $.when(commonFun.isUserLogin())
//             .done(function() {
//                 $bookInvestForm.find('.init-radio-style').removeClass('on');
//                 $bookInvestForm.find('input[name="bookingAmount"]').val('');
//                 layer.open({
//                     title: '预约投资',
//                     type: 1,
//                     skin: 'book-box-layer',
//                     area: ['680px'],
//                     content: $('.book-invest-frame',$homePageContainer)
//                 });
//             })
//             .fail(function() {
//                 //判断是否需要弹框登陆
//                 layer.open({
//                     type: 1,
//                     title: false,
//                     closeBtn: 0,
//                     area: ['auto', 'auto'],
//                     content: $('#loginTip')
//                 });
//             });
//     });
//
//     //预约校验
//     let bookInvestForm=globalFun.$('#bookInvestForm');
//     let validatorBook = new ValidatorObj.ValidatorForm();
//     validatorBook.add(bookInvestForm.bookingAmount, [{
//         strategy: 'isNonEmpty',
//         errorMsg: '请输入预计投资金额'
//     },{
//         strategy: 'isNumber',
//         errorMsg: '请输入正确的投资金额'
//     }]);
//
//     let rebookInputs=$(bookInvestForm).find('input:text');
//
//     Array.prototype.forEach.call(rebookInputs,function(el) {
//         globalFun.addEventHandler(el,'blur',function() {
//             let errorMsg = validatorBook.start(this);
//             if(errorMsg) {
//                 tipsError(errorMsg,this);
//             }
//         });
//     });
//
//     function toBookInvest() {
//         let amount = $(bookInvestForm).find('input[name="bookingAmount"]').val().replace(/,/gi, '');
//         $(bookInvestForm).find('input[name="bookingAmount"]').val(amount);
//         let data = $(bookInvestForm).serialize();
//         $.when(commonFun.isUserLogin())
//             .fail(function() {
//                 location.href='/login';
//             })
//             .done(function() {
//                 commonFun.useAjax({
//                     type: 'GET',
//                     url: '/booking-loan/invest?' + data
//                 },function(response) {
//                     if (response.data.status) {
//                         layer.closeAll();
//                         layer.open({
//                             type: 1,
//                             title: '&nbsp',
//                             area: ['400px', '185px'],
//                             content: '<div class="success-info-tip"> <i class="icon-tip"></i> <div class="detail-word"><h2>恭喜您预约成功！</h2> 当有可投项目时，客服人员会在第一时间与您联系，请您耐心等候并保持电话畅通。</div> </div>'
//                         });
//                     }
//                 });
//             });
//     }
//     function tipsError(errorMsg,dom) {
//         layer.tips(errorMsg, dom, {
//             tips: [1, '#efbf5c'],
//             time: 3000,
//             tipsMore: true,
//             area: 'auto',
//             maxWidth: '500'
//         });
//     }
//     bookInvestForm.onsubmit = function(event) {
//         event.preventDefault();
//         let errorMsg,checkedRadio;
//         errorMsg = validatorBook.start(rebookInputs[0]);
//         if(errorMsg) {
//             tipsError(errorMsg,rebookInputs[0]);
//             return;
//         }
//         checkedRadio=$(bookInvestForm).find(":radio:checked").length;
//
//         if(!checkedRadio) {
//             tipsError('请选择您希望投资的项目',bookInvestForm.productType[0]);
//             return;
//         }
//         toBookInvest();
//     }
//
// },'bookInvest');
//
//
