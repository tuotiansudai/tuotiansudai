<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.integral_draw}" pageJavascript="${js.integral_draw}" activeNav="" activeLeftNav="" title="拓荒计划_旅游活动_拓天速贷" keywords="拓荒计划,免费旅游,旅游活动投资,拓天速贷" description="拓天速贷金秋拓荒计划,多条旅游线路免费玩,你旅游我买单,邀请好友注册也可获得免费旅游大奖.">
<@global.isNotAnonymous>
<div style="display: none" class="login-name" data-login-name='<@global.security.authentication property="principal.username" />'></div>
<div style="display: none" class="mobile" id="MobileNumber" data-mobile='<@global.security.authentication property="principal.mobile" />'></div>
</@global.isNotAnonymous>
<div class="tour-slide">
</div>
<div class="autumn-tour-frame" id="autumnTravelPage">
    <div class="reg-tag-current" style="display: none">
        <#include '../register.ftl' />
    </div>



</div>
</@global.main>


