require("activityStyle/rebate_station_2018.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/plugins/jquery.qrcode.min');
require('publicJs/login_tip');
let sourceKind = globalFun.parseURL(location.href);
let timer;

if ($(document).width() < 790) {
    commonFun.calculationFun(document,window);
}

function getPercentLight() {
    let currentPer = 500;
    let percentArr= [100,200,300,400,500,1000];
    for (let i = 0;i < percentArr.length;i++) {
        let item = percentArr[i];
        if (currentPer < item) {
            let index = percentArr.indexOf(item) - 1;
            $('.light_line').css('width',20 * index + '%');
            for(let j = 1; j < index + 2;j++) {
                let lightCircle = 'circle' + j;
                $('.' + lightCircle).addClass('light');
            }
            return;
        }
        else {
            if (currentPer > percentArr[percentArr.length - 1]) {
                $('.light_line').css('width','100%');
                for(let j = 1; j < 7;j++) {
                    let lightCircle = 'circle' + j;
                    $('.' + lightCircle).addClass('light');
                }
            }
        }
    }
}

getPercentLight();

$('.active_one').qrcode({
    text: 'http://www.baidu.com',
    width: 200,
    height: 200,
    colorDark : '#000000',
    colorLight : '#ffffff',
});

$('.active_one_pop').qrcode({
    text: 'http://www.baidu.com',
    width: 140,
    height: 126,
    colorDark : '#000000',
    colorLight : '#ffffff',
});

$('.active_two').qrcode({
    text: 'http://www.baidu.com',
    width: 140,
    height: 126,
    colorDark : '#000000',
    colorLight : '#ffffff',
});

$.when(commonFun.isUserLogin())
    .done(function () {
        alreadyLogged();
    })
    .fail(function(){
        // noLogged();
        alreadyLogged();
    });

$('.invest_cash_btn').on('click', () => {
    $.when(commonFun.isUserLogin())
        .done(function () {
            location.href = '/loan-list';
        })
        .fail(function(){
            toLogin();
        });
});

$('.login_btn').on('click', () => {
    toLogin();
});

$('.see_more').on('click',() => {
    if (!$('.see_more').html()) return;
    $('.table_container').addClass('table_auto');
    $('.see_more').hide();
    $('.see_less').show();
});

$('.see_less').on('click',() => {
    $('.table_container').removeClass('table_auto');
    $('.see_more').show();
    $('.see_less').hide();
});

//活动一 返现榜
$('.handle_btn').on('click',(e) => {
    let overTime = e.currentTarget.dataset.overtime;
    countTimePop(overTime);
    if (!isMobile()) {
        $('.cashBack_popModal').show();
    }
    else {
        location.href = '/activity/invite-help/wechat/'+ e.currentTarget.dataset.helpId + '/invest/help';
    }

});

// 活动二 人人可领10元现金
$('.everyone_detail').on('click',() => {
    location.href = '/activity/invite-help//wechat/everyone/help/detail';
    if (!isMobile()) {
        $('.help_popModal').show();
    }
    else {
        location.href = '/activity/invite-help/wechat/everyone/help/detail';
    }
});

$('.close_btn').on('click',function () {
    clearInterval(timer);
    $('.hour1').html('');
    $('.hour2').html('');
    $('.minutes1').html('');
    $('.minutes2').html('');
    $('.seconds1').html('');
    $('.seconds2').html('');
   $(this).parent().parent().hide();
});

function alreadyLogged() {
    $('.part1').find('.already_login').show();
    $('.part2').find('.already_login').show();
}

function noLogged() {
    $('.part1').find('.no_login').show();
    $('.part2').find('.no_login').show();
}

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
            content: $('#loginTip'),
            success:function () {
                $('input').on('focus',function () {
                    let height = -$(window).scrollTop();
                    $('body').scrollTop(height);
                })

            }
        });
    }
}

function isMobile() {
    return /Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent);
}

function scrollUp(domName,time=1000) {
    let $self = domName;
    let lineHeight = $self.find("p:first").height();
    let z = 0;//向上滚动top值
    function up() {//向上滚动
        $self.animate({//中奖结果
            'top': (z - lineHeight) + 'px'
        }, time, 'linear', function () {
            $self.css({'top': '0px'})
                .find("p:first").appendTo($self);
            up();
        });
    }
    up();
}

function formatTime(time) {
    let currentTime = new Date(time);
    let year = currentTime.getFullYear();
    let month = currentTime.getMonth() + 1;
    let day = currentTime.getDate();
    month < 10 ? '0' + month : month;
    day < 10 ? '0' + day : day;
    return year + '.' + month + '.' + day;
}

function countTimePop(str) {
    $('.countDown_wrapper').show();
    $('.time_over').hide();
    let end = new Date(str).getTime();
    let now = new Date().getTime();
    let leftTime = (end-now)/1000;
    timerCount();
    timer = setInterval(() => {
        timerCount();
    },1000);
    function timerCount() {
        let h,m,s;
        if (leftTime > 0) {
            if (leftTime <= 1) {
                clearInterval(timer);
                $('.countDown_wrapper').hide();
                $('.time_over').show();
                return;
            }
            h = Math.floor(leftTime/60/60%24);
            m = Math.floor(leftTime/60%60);
            s = Math.floor(leftTime%60);
            h = h < '10' ? '0' + h : h + '';
            m = m < '10' ? '0' + m : m + '';
            s = s < '10' ? '0' + s : s + '';

            $('.hour1').html(h.charAt(0));
            $('.hour2').html(h.charAt(1));
            $('.minutes1').html(m.charAt(0));
            $('.minutes2').html(m.charAt(1));
            $('.seconds1').html(s.charAt(0));
            $('.seconds2').html(s.charAt(1));
            --leftTime;
        }
        else {
            clearInterval(timer);
            $('.countDown_wrapper').hide();
            $('.time_over').show();
        }
    }
}

function countDownList(domElement) {
    return $(domElement).each(function () {
        let $this = $(this);
        let end = new Date($this.data('overtime')).getTime();
        let now = new Date().getTime();
        let leftTime = (end - now)/1000;
        timerCount();
        timer = setInterval(() => {
            timerCount();
        },1000);
        function timerCount() {
            let h,m,s;
            if(leftTime > 0) {
                if (leftTime <= 1) {
                    clearInterval(timer);
                    $this.html('助力结束');
                    $this.siblings('.handle_btn').html('查看详情');
                    return;
                }
                h = Math.floor(leftTime/60/60%24);
                m = Math.floor(leftTime/60%60);
                s = Math.floor(leftTime%60);
                h = h < 10 ? '0' + h : h;
                m = m < 10 ? '0' + m : m;
                s = s < 10 ? '0' + s : s;
                $this.html(h + ':' + m + ':' + s);
                leftTime--;
            }else {
                $this.html('助力结束');
                $this.siblings('.handle_btn').html('查看详情');
            }
        }
    });
};

countDownList('.overTime');


if ($('.part1').find('.help_list').find('.already_login').find('p').length > 2) {
    scrollUp($('.part1').find('.help_list').find('.already_login'));
}

function getPageInfo() {
    commonFun.useAjax({
        type: 'GET',
        url: '/activity/year-end-awards/ranking/' + date
    }, function (data) {
        if (data.status) {
            if (_.isNull(data.records) || data.records.length == 0) {
                $contentRanking.html('');
                showMoreData(data.records.length);
                return;
            }
            //获取模版内容
            let ListTpl = $('#tplTable').html();
            // 解析模板, 返回解析后的内容
            let render = _.template(ListTpl);
            let html = render(data);
            $contentRanking.html(html);
            showMoreData(data.records.length);
        }
    });
}





