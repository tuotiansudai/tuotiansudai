require("activityStyle/rebate_station_2018.scss");
let commonFun= require('publicJs/commonFun');
require('publicJs/plugins/jquery.qrcode.min');
require('publicJs/login_tip');
let sourceKind = globalFun.parseURL(location.href);
let url = location.href;
let timer;
let timer1;

let data = {
    'myCashChain': [1.00,200.00,300.00,350.00,1500.11],
    'nextNode': 6,
    'nextAmount': 25000,// 分
    'helpFriends': [
        {
            id: 1,
            openId: '1111222aaa',
            nickName: "朱坤",
            headImgUrl: '',
            createdTime: '2018-04-13 18:39:58',
            updatedTime: '2018-04-13 18:39:58'
        }
    ],
    'helpModel': {
        id: 1,
        loadnId: 22222222,
        investAmount: 222,
        annualizedAmount: 500,
        loginName: 'chenzhonghui',
        userName: '陈忠辉',
        mobile: 1881111111,
        openId: null,
        type: 'INVEST_HELP',
        helpUerCount: 2,
        reward: 100,
        startTime: '2018-04-13 17:39:58',
        endTime: '2018-04-13 17:39:58',
        cashBack: false

    }
};

let data1 = {
    "drawEndTime": "2018-04-20 18:46:42",
    "helpModel": {
        "id": 2,
        "loanId": 0,
        "investId": 0,
        "investAmount": 0,
        "annualizedAmount": 0,
        "loginName": "chenzhonghui",
        "userName": "陈忠慧",
        "mobile": "18245135693",
        "openId": "oTq6DwECk6MRYlhZhvj_hnDrKRxE",
        "type": "EVERYONE_HELP",
        "helpUserCount": 10,
        "reward": 2000,
        "startTime": "2018-04-18 18:46:42",
        "endTime": "2018-04-19 18:46:42",
        "cashBack": false
    },
    "helpFriends": [
        {
            "id": 1,
            "openId": "oTq6DwECk6MRYlhZhvj_hnDrKRxE",
            "nickName": "朱坤",
            "headImgUrl": "http://thirdwx.qlogo.cn/mmopen/FicVOq4OuXKhoKUF7uaHDgZdS5ZibPl69lfJljElNG2xU8oCtLW5aJHKSxmEicsZuRqsnRVkKeQicLlK6IsuL35GqQma3bgqVficI/132",
            "createdTime": "2018-04-13 18:39:58",
            "updatedTime": "2018-04-13 18:41:00"
        },
        {
            "id": 2,
            "openId": "oTq6DwECk6MRYlhZhvj_hnDrKRx1",
            "nickName": "NICAI",
            "headImgUrl": "http://thirdwx.qlogo.cn/mmopen/FicVOq4OuXKhoKUF7uaHDgZdS5ZibPl69lfJljElNG2xU8oCtLW5aJHKSxmEicsZuRqsnRVkKeQicLlK6IsuL35GqQma3bgqVficI/132",
            "createdTime": "2018-04-12 10:00:00",
            "updatedTime": "2018-04-12 10:00:00"
        }
    ]
};


if ($(document).width() < 790) {
    commonFun.calculationFun(document,window);
}


function getList(data,parent) {
    if (parent == '.help_popModal') {
        $('.invite_count').html(data.helpModel.helpUserCount);
        $('.invite_reward').html(data.helpModel.reward / 100);
    }
    let list = data.helpFriends;
    if (!list.length) {
        $(parent).find('.list_tip_text').show();
        return;
    }
    let dom = '';
    $(parent).find('.friend_list').show();
    list.forEach((item) => {
        let headImgUrl = item.headImgUrl;
        if (!item.headImgUrl) {
            headImgUrl = '../../../activity/images/default_portrait.png';
        }
        item.nickName = item.nickName ? item.nickName : '';
        dom += `
             <div class="list_item">
                <img class="portrait" src="${headImgUrl}" />
                <div class="nickName">${item.nickName}</div>
                <div class="finish_time">${item.createdTime}</div>
            </div>
        `
    });
    $(parent).find('.friend_list').html(dom);
}


function getPercentLight(data) {
    if (data.nextNode) {
        $('.differ_count').html(data.nextNode);
        $('.differ_amount').html(data.nextAmount / 100);
        $('.differ_friends').show();
    }
    let has_get = data.helpModel.reward / 100;
    $('.has_get').html(has_get);
    let currentPer = has_get;
    let percentArr= data.myCashChain;
    for (let j = 0;j < percentArr.length;j++) {
        let circleName = '.circle' + (j + 1);
        $(circleName).html(percentArr[j]);
    }
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

$('.active_one').qrcode({
    text: url,
    width: 200,
    height: 200,
    colorDark : '#000000',
    colorLight : '#ffffff',
});

$('.active_one_pop').qrcode({
    text: url,
    width: 140,
    height: 126,
    colorDark : '#000000',
    colorLight : '#ffffff',
});

$('.active_two').qrcode({
    text: url,
    width: 140,
    height: 126,
    colorDark : '#000000',
    colorLight : '#ffffff',
});

$.when(commonFun.isUserLogin())
    .done(function () {
        $('.state_btn').addClass('invest_cash_btn');
        alreadyLogged();
    })
    .fail(function(){
        $('.state_btn').addClass('login_now');
       noLogged();
       //   alreadyLogged();
    });

$('.help_list').on('click','.invest_cash_btn', () => {
    if ($(document).width() < 790) {
        location.href = 'm/loan-list';
    }
    else {
        location.href = '/loan-list';
    }

});

$('.login_btn').on('click', () => {
    toLogin();
});


$('.help_list').on('click','.login_now', () => {
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
        commonFun.useAjax({
            type: 'GET',
            url: '/activity/invite-help/'+ e.currentTarget.dataset.helpId +'/invest/help'
        }, function (data) {
            getList(data,'.cashBack_popModal');
            getPercentLight(data);
        });
        $('.cashBack_popModal').show();
    }
    else {
        location.href = '/activity/invite-help/wechat/'+ e.currentTarget.dataset.helpId + '/invest/help';
    }

});

// 活动二 人人可领10元现金
$('.invite_everyone_detail').on('click',() => {
    if (!isMobile()) {
        commonFun.useAjax({
            type: 'GET',
            url: '/activity/invite-help/everyone/help/detail'
        }, function (data) {
            $('.userName').html(data.name);
            if(data.helpModel){
                getList(data,'.help_popModal');
            }else{
                $('#helpPopText').show();
            }
        });
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
   $(this).siblings('.percent_wrapper').find('.light').removeClass('light');
   $(this).siblings('.percent_wrapper').find('.light_line').css('width',0);
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


function countTimePop(str) {
    $('.countDown_wrapper').show();
    $('.time_over').hide();
    let end = new Date(str.replace(/-/g,'/')).getTime();
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
        let end = new Date($this.data('overtime').replace(/-/g,'/')).getTime();
        let now = new Date().getTime();
        let leftTime = (end - now)/1000;
        timer1Count();
        timer1 = setInterval(() => {
            timer1Count();
        },1000);
        function timer1Count() {
            let h,m,s;
            if(leftTime > 0) {
                if (leftTime <= 1) {
                    clearInterval(timer1);
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




if ($('.part1').find('.help_list').find('.my_help_list').find('p').length > 2) {
    scrollUp($('.part1').find('.help_list').find('.my_help_list'));
}









