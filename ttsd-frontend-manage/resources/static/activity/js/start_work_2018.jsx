require("activityStyle/start_work_2018.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
require('swiper/dist/css/swiper.css');
let Swiper = require('swiper/dist/js/swiper.jquery.min');
let sourceKind = globalFun.parseURL(location.href);
let switchLock = false;

let startTime = $('.container_time').data('startTime');
let overTime = $('.container_time').data('overTime');
let activityTime = new Date(startTime.replace(/-/g, "/")).getTime(); // 活动开始时间
let activityOverTime = new Date(overTime.replace(/-/g, "/")).getTime();  // 活动结束时间

let mySwiper = new Swiper ('.swiper-container', {
    direction: 'horizontal',
    loop: true,
    autoplay:3000,
    slidesPerView: 'auto',
    centeredSlides:true,
    spaceBetween: 20,
    loopAdditionalSlides:1,
    autoplayDisableOnInteraction : false,
});

let $prevBtn = $('.prevBtn'),
    $nextBtn = $('.nextBtn');

$prevBtn.on('click',function () {
    mySwiper.slidePrev();
});
$nextBtn.on('click',function () {
    mySwiper.slideNext();
});

recordList();

if (isMobile()) {
    $('.title_wrapper_m').show();
}
else {
    $('.title_wrapper').show();
}

$('.close_btn1').on('click',() => {
    $('#flex_content').hide();
   $('#pop_modal_container1').hide();
});

$('.close_btn2').on('click',() => {
    $('#flex_content').hide();
    $('#pop_modal_container2').hide();
});

$('.invest_btn').on('click',() => {
    window.location.href = '/loan-list';
});

$('.get_prize_btn').on('click',(e) => {
    e = e || window.event;
    let targetDom = e.currentTarget || e.srcElement;
    let exchangePrize = $(targetDom).data('index');
    let currentSwiper = $(targetDom).parents('.swiper-slide');
    if ($(targetDom).hasClass('get_prize_btn_m') && !currentSwiper.hasClass('swiper-slide-active')) {
        return;
    }
    if (!switchLock) {
        switchLock = true;
        let currentTime = new Date().getTime();
        if (currentTime < activityTime) {
            layer.msg('活动未开始');
            switchLock = false;
        }
        else if (currentTime > activityOverTime) {
            layer.msg('活动已结束');
            switchLock = false;
        }
        else {
            $.when(commonFun.isUserLogin())
                .done(function () {
                    commonFun.useAjax({
                        type: 'POST',
                        dataType: 'json',
                        url:'/activity/start-work/exchange',
                        data: {exchangePrize}
                    },function(data) {
                        if (data.success) {
                            $('#flex_content').show();
                            $('#pop_modal_container1').show();
                            $('.gold_count').html(data.count);
                            recordList();
                            switchLock = false;
                        }
                        else {
                            $('#flex_content').show();
                            $('#pop_modal_container2').show();
                            switchLock = false;
                        }
                    });
                }).fail(function () {
                toLogin();
                switchLock = false;
            });
        }
    }
});


//去登录
function toLogin() {
    if (sourceKind.params.source == 'app') {
        location.href = "/login";
    }else {
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['auto', 'auto'],
            content: $('#loginTip')
        });
    }
}

function recordList() {
    commonFun.useAjax({
        type: 'GET',
        dataType: 'json',
        url:'/activity/start-work/prize',
    },function(data) {
        let domStr = '<tr><th class="goods_th">物品</th><th class="time_th">时间</th><th class="count_th">消耗小金人个数</th></tr>';
        let list = data.prize;
        if (!list.length) {
            $('.show_record_container').hide();

            return;
        }
        list.forEach((item) => {
            domStr += `<tr>
                          <td>${item.prize}</td>
                          <td>${item.exchangeTime}</td>
                          <td>${item.count}</td>
                        </tr>`
        });
        $('.hover_table').html(domStr);
    });
}

function isMobile() {
    return /Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent);
}



