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

    let topImg = new Image();
    topImg.src = require('../../images/wechat/midsummer/img-top.jpg');
    topImg.onload = function() {
        $inviteBoxFriend.find('.invite-header').append(topImg);
    }

}







