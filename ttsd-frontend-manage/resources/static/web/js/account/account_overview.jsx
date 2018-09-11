require('webStyle/account/account_overview.scss');
let commonFun= require('publicJs/commonFun');

let $accountOverview = $('#accountOverview');
let $tMonthBox=$('#tMonthBox'),
    $signBtn = $('#signBtn'),
    $signTip = $('#signLayer'),
    $closeSign = $('#closeSign'),
    $switchMenu=$('.payment-switch',$tMonthBox);

//本月已收回款，本月待收回款 切换
$switchMenu.find('em').click(function() {
    var $this=$(this),
        num=$switchMenu.find('em').index(this);
    $this.addClass('current').siblings().removeClass('current');
    $this.siblings('.total').eq(num).addClass('current');
    $('table',$tMonthBox).eq(num).show().siblings('table').hide();
});


if($('.amount-sum',$accountOverview).find('.icon-has-con').length) {
    $('.amount-sum h3',$accountOverview).on('click',function() {
        $(this).parents().toggleClass('open');
    });
}

tipshow('#tMonthBox','.month-title',6);
tipshow('.newProjects','.trade-detail',15);
$('.birth-icon',$accountOverview).on('mouseenter',function() {
    layer.closeAll('tips');
    var num = parseFloat($(this).attr('data-benefit'));
    var benefit = num + 1;
    layer.tips('您已享受生日福利，首月收益翻'+benefit+'倍', $(this), {
        tips: [1, '#efbf5c'],
        time: 2000,
        tipsMore: true,
        area: 'auto',
        maxWidth: '500'
    });
});

/**
 * @dom  {[string]}		当前列表的DOM节点
 * @active  {[string]}	触发事件的DOM节点
 * @length  {[number]}	限制文字长度触发提示框
 * @return {[function]}
 */
function tipshow(dom,active,length){
    $(dom).on('mouseenter',active,function() {
        layer.closeAll('tips');
        if($(this).text().length>length){
            layer.tips($(this).text(), $(this), {
                tips: [1, '#efbf5c'],
                time: 2000,
                tipsMore: true,
                area: 'auto',
                maxWidth: '500'
            });
        }
    });
}
$signBtn.on('click', function (event) {
    event.preventDefault();
    var _this = $(this),
        $signText = $(".sign-text",$signTip),
        $tomorrowText = $(".tomorrow-text",$signTip),
        $signPoint = $(".sign-point",$signTip),
        $introText = $('.intro-text',$signTip),
        $nextText = $('.next-text',$signTip),
        $signBtn = $("#signBtn");

        commonFun.useAjax({
            url: _this.data('url'),
            type: 'POST'
        },function(response) {
            if (response.data.status) {
                response.data.signIn == true ? $signText.html("您今天已签到") : $signText.html("签到成功");
                $tomorrowText.html("明日签到可获得" + response.data.nextSignInPoint + "积分");
                if(response.data.full == true){
                    $introText.html('已连续签到365天，获得全勤奖！');
                    $nextText.html('365元投资红包');
                }
                else{
                    $introText.html(response.data.currentRewardDesc);
                    $nextText.html(response.data.nextRewardDesc);
                }
                $signBtn.parent().addClass("no-click").find('span').removeClass('will-sign').addClass('finish-sign').html("已签到");
                $signPoint.find('span').html('+'+response.data.signInPoint);
                $signTip.fadeIn('fast');
            } else {
                location.href='/register/account?redirect=/account';
            }
        });

});
//hide sign tip
$closeSign.on('click', function (event) {
    event.preventDefault();
    location.href = "/account";
});


let isBankCard = $accountOverview.data('bankcard');
let isAccount = $accountOverview.data('account');

$('#cashMoneyBtn').on('click',function () {
    if(!isAccount == true){
        location.href = '/register/account';
    }else if(!isBankCard) {
        layer.open({
            type: 1,
            move: false,
            offset: "200px",
            title: '绑卡',
            area: ['490px', '220px'],
            shadeClose: false,
            closeBtn: 0,
            content: $('#bankCardDOM')
        });
    }else {
        location.href = '/withdraw'
    }
})


$('#accountBtn').on('click',function () {
    location.href = '/register/account'
})
$('.btn-close').on('click',function () {
    layer.closeAll();
})
//显示隐藏联动优势资金账号提示
$('#noticeBtn').on('mouseover',function () {
    $('.notice-tips').show();
})
$('#noticeBtn').on('mouseout',function () {
    $('.notice-tips').hide();
})