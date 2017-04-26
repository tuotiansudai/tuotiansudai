require("activityStyle/wechat/midsummer.scss");
let $entryPointCon=$('#entryPointCon');

let $inviteBoxFriend=$('#inviteBoxFriend');

if($entryPointCon.length) {
    //获取手机屏幕高度
    let winHeight = screen.height,
        winWidth = screen.width;
    $entryPointCon.css({"width":winWidth * 0.92,"height":winHeight*0.92});
}

if($inviteBoxFriend.length) {

    let $inviteHeader = $('#inviteHeader');
    let topImg = new Image();
    topImg.src = require('../../images/wechat/midsummer/img-top.jpg');
    topImg.onload = function() {
        $inviteHeader.append(topImg);
    }

//起投金额
    let amountObj ={
        '0':{
            temp:'20',
            amount:'20000',
            progress:'26%'
        },
        '1':{
            temp:'40',
            amount:'10000',
            progress:'44%'
        },
        '2':{
            temp:'60',
            amount:'5000',
            progress:'64%'
        },
        '3':{
            temp:'80',
            amount:'2000',
            progress:'80%'
        },
        '4':{
            temp:'100',
            amount:'50',
            progress:'96%'
        }
    };

    let inviteNumber =$('.invite-number',$inviteBoxFriend).val();
    let tempProgress = $('.temp-progress',$inviteBoxFriend);
    if(inviteNumber>4) {
        inviteNumber = '4';
    }
    let currentObj = amountObj[inviteNumber];
    $('.temp-number',$inviteBoxFriend).text(currentObj.temp);
    $('.from-invest',$inviteBoxFriend).text(currentObj.amount);

    tempProgress.css({"width":currentObj.progress});

//如果为100，改变温度的字体大小
    if(currentObj.temp=='100') {
        $('.temp-number',$inviteBoxFriend).css({"font-size":"24px"});
    }

    //按钮点击呼起好友
    let $buttonLayer = $('#buttonLayer');
    $buttonLayer.on('click',function(event) {
        let targetName = event.target.className;

        if(/btn-invite/.test(targetName)) {
            //邀请微信好友

        } else if(/btn-share/.test(targetName)) {
            //分享至朋友圈

        } else if(/btn-help/.test(targetName)) {
            //帮助TA

        }

    })
}










