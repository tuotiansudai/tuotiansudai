require("activityStyle/start_work_2018.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/login_tip');
require('swiper/dist/css/swiper.css')
let Swiper = require('swiper/dist/js/swiper.jquery.min');
let sourceKind = globalFun.parseURL(location.href);


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
                }
                else {
                    $('#flex_content').show();
                    $('#pop_modal_container2').show();
                }
            });
    }).fail(function () {
        toLogin();
    });

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
