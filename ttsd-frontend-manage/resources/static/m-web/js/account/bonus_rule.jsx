require('mWebStyle/account/bonus_rule.scss');
require('publicJs/plugins/jQuery.md5');
require('publicJs/plugins/jquery.qrcode.min');
let commonFun= require('publicJs/commonFun');

let $recommendRule = $('#recommendRule'),
    $rewardRules = $('#rewardRules'),
    $scanCode = $('#scanCode');
let $iconScanCode = $('#iconScanCode'),
    $toRewardRules = $('#toRewardRules'),
    $iconRewardRules = $('#iconRewardRules'),
    $iconRecomend = $('#iconRecomend');

if($scanCode.length){
    let $clipboardText = $('#clipboard_text');

    var mobile=$clipboardText.data('mobile')+'',
        md5Mobile=$.md5(mobile);//邀请好友

    if (window["context"] == undefined) {
        if (!window.location.origin) {
            window.location.origin = window.location.protocol + "//" + window.location.hostname+ (window.location.port ? ':' + window.location.port: '');
        }
    }

    var md5String=commonFun.decrypt.compile(md5Mobile,mobile),
        origin=location.origin;

    $clipboardText.val(origin+'/activity/landing-page?referrer='+md5String);

//动态生成二维码

    $('.img-code',$scanCode).qrcode(origin+'/activity/app-share?referrerMobile='+mobile);
}


//推荐送奖金
if($recommendRule.length) {
    let $sweepFace = $('#sweep_face',$(recommendRule));
    $sweepFace.on('click',function () {
        $scanCode.show();
        $rewardRules.hide();
        $recommendRule.hide();

    })
    $toRewardRules.on('click',function () {
        $scanCode.hide();
        $rewardRules.show();
        $recommendRule.hide();

    })

    $iconRecomend.on('click',function () {
       history.go(-1);
    })
$scanCode.on('click','#iconScanCode',function () {
    $scanCode.hide();
    $rewardRules.hide();
    $recommendRule.show();
})
$rewardRules.on('click','#iconRewardRules',function () {
    $rewardRules.hide();
    $recommendRule.show();
})
}
