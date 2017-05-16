require("activityStyle/wechat/midsummer.scss");

let $inviteBoxFriend = $('#inviteBoxFriend');
let $inviteHeader = $('#inviteHeader');
if ($inviteBoxFriend.length) {
    let topImg = new Image();
    topImg.src = require('../../images/midsummer/img-top.jpg');
    topImg.onload = function () {
        $inviteHeader.append(topImg);
    };

//起投金额
    let amountObj = {
        '0': {
            temp: '20',
            amount: '20000',
            progress: '26%'
        },
        '1': {
            temp: '40',
            amount: '10000',
            progress: '44%'
        },
        '2': {
            temp: '60',
            amount: '5000',
            progress: '62%'
        },
        '3': {
            temp: '80',
            amount: '2000',
            progress: '77%'
        },
        '4': {
            temp: '100',
            amount: '50',
            progress: '93%'
        }
    };

    let inviteNumber = $('.invite-number', $inviteBoxFriend).val();
    let tempProgress = $('.temp-progress', $inviteBoxFriend);
    if (inviteNumber > 4 || inviteNumber=='') {
        inviteNumber = '4';
    }
    let currentObj = amountObj[inviteNumber];
    $('.temp-number', $inviteBoxFriend).text(currentObj.temp);
    $('.from-invest', $inviteBoxFriend).text(currentObj.amount);

    tempProgress.css({"width": currentObj.progress});

//如果为100，改变温度的字体大小
    if (currentObj.temp == '100') {
        $('.temp-number', $inviteBoxFriend).css({"font-size": "24px"});
    }
}

//按钮点击呼起好友
let $buttonLayer = $('#buttonLayer');
let $FloatingBox = $('#FloatingBox');

$FloatingBox.find('.btn-close').on('click',function(event) {
    $FloatingBox.hide();
});

$buttonLayer.on('click',function(event) {
    let targetName = event.target.className;

    if(/btn-invite/.test(targetName)) {
        //邀请微信好友
        $FloatingBox.show();

    } else if(/btn-help/.test(targetName)) {
        //帮助TA
        let mobile = $.trim($inviteHeader.data('mobile'));
        location.href='/activity/app-share?referrerMobile='+mobile;
    }

})











