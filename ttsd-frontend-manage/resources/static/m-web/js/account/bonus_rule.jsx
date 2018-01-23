require('mWebStyle/account/bonus_rule.scss');
// let touchSlide = require('publicJs/touch_slide');

let $recommendRule = $('#recommendRule'),
    $rewardRules = $('#rewardRules'),
    $scanCode = $('#scanCode');
let $iconScanCode = $('#iconScanCode'),
    $toRewardRules = $('#toRewardRules'),
    $iconRewardRules = $('#iconRewardRules'),
    $iconRecomend = $('#iconRecomend');

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
       location.href = '/m'
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
