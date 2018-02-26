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
        let isInvestor = $recommendRule.data('user-role');
        if(!isInvestor){
            commonFun.CommonLayerTip(
                {
                btn:['确定', '取消'],
                content:`<div class="record-tip-box"> <b class="pop-title">温馨提示</b> <span>您还未实名认证，不能获得推荐<br/>奖励。是否确认分享？</span></div> `,
                area:['280px', '180px'],
            },function () {
                    layer.closeAll();
                    $scanCode.show();
                    $rewardRules.hide();
                    $recommendRule.hide();
                },
                function () {
                    layer.closeAll();
                    return false;
                }
            )
        }else {
            $scanCode.show();
            $rewardRules.hide();
            $recommendRule.hide();
        }



    })
    $('body').on('click','#toRewardRules',function () {
        $scanCode.hide();
        $recommendRule.hide();
        $rewardRules.show();


    })

    $iconRecomend.on('click',function () {
        location.href='/m';
    })
    $('body').on('click','#iconScanCode',function () {
    $scanCode.hide();
    $rewardRules.hide();
    $recommendRule.show();
})
    $('#iconRewardRules').on('click',function (e) {
        e.preventDefault();
        location.href='/m/about/refer-reward'

})
}


