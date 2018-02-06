require("activityStyle/start_work_2018.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
require('swiper/dist/css/swiper.css');
let Swiper = require('swiper/dist/js/swiper.jquery.min');
let sourceKind = globalFun.parseURL(location.href);
let switchLock = false;
let $productListWap = $('#productListWap');

if ($(document).width() < 790) {
    (function (doc, win) {
        var docEl = doc.documentElement,
            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
            recalc = function () {
                var clientWidth = docEl.clientWidth;
                if (!clientWidth) return;
                var fSize = 20 * (clientWidth /375);
                fSize > 40 && (fSize = 39.36);
                docEl.style.fontSize = fSize + 'px';
            };

        if (!doc.addEventListener) return;
        win.addEventListener(resizeEvt, recalc, false);
        doc.addEventListener('DOMContentLoaded', recalc, false);
        $('body').css('visibility', 'visible');
    })(document, window);
}


$.when(commonFun.isUserLogin())
    .done(function () {
        recordList();
        $('.title_wrapper').show();
    });

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
    if (!switchLock) {
        switchLock = true;
        $.when(commonFun.isUserLogin())
            .done(function () {
                let exchangePrize = e.currentTarget.dataset.index;
                commonFun.useAjax({
                    type: 'POST',
                    dataType: 'json',
                    url:'/activity/start-work/exchange',
                    data: {exchangePrize}
                },function(data) {
                    if (data.success) {
                        $('#flex_content').show();
                        $('#pop_modal_container1').show();
                        $('#gold_count').html(data.count);
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
        let domStr = '<tr><th>物品</th><th>时间</th><th>消耗小金人个数</th></tr>';
        let list = data.prize;
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


// 移动端滑动效果
let descObj = {
    0:'蒙顶山春茶云雾茶礼盒装125g',
    1:'江小白45度清香型白酒125ml*12瓶',
    2:'小米 红米5A 全网通版 2GB+16GB',
    3:'周生生黄金羽毛吊坠计价2.31克',
    4:'AOSHIMSIT 按摩椅家用太空舱',
    5:'立久佳家用静音折叠跑步机',
    6:'海尔模卡55英寸 4K曲面液晶电视',
    7:'Apple iPhone8（64GB'
};

$productListWap.find('.swiper-slide').each(function (index,item) {
    let  _self = $(this);
    let imgUrl = require('../images/2018/new-year-increase-interest/product'+(index+1)+'.png');
    let img = new Image();
    img.src = imgUrl;
    img.alt=descObj[index];
    img.title = descObj[index];
    _self.append(img);
});

let mySwiper = new Swiper ('.swiper-container', {
    direction: 'horizontal',
    loop: true,
    // autoplay:3000,
    autoplayDisableOnInteraction:false,
    slidesPerView: '1.8',
    centeredSlides:true,
    spaceBetween: 20,
    loopAdditionalSlides:1
})

let $prevBtn = $('.prevBtn'),
    $nextBtn = $('.nextBtn');

$prevBtn.on('click',function () {
    mySwiper.slidePrev();
})
$nextBtn.on('click',function () {
    mySwiper.slideNext();
})
