<#import "../macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" activeNav="" activeLeftNav="" title="拓荒计划_旅游活动_拓天速贷" keywords="拓荒计划,免费旅游,旅游活动投资,拓天速贷" description="拓天速贷金秋拓荒计划,多条旅游线路免费玩,你旅游我买单,邀请好友注册也可获得免费旅游大奖.">
<style type="text/css">
    body {
        background: #ebfbff;
    }
    .autumn-travel-detail img {
        width: 100%;
    }
    .btn-normal {
        margin-top:40px;
        border-bottom:4px solid #c75f28;
        font-size:22px;
        border-radius: 0;
        letter-spacing: 0;
        font-weight: bold;
        padding: 9px 50px;
    }
</style>
<div class="page-width autumn-travel-detail">
    <img src="${introduce}">

    <div class="tc">
        <@global.isAnonymous>
            <a href="/login?redirect=/activity/autumn/travel" class="btn-normal autumn-travel-invest-channel">立即投资</a>
        </@global.isAnonymous>
        <@global.isNotAnonymous>
            <a href="/loan-list" class="btn-normal">立即投资</a>
        </@global.isNotAnonymous>
    </div>
</div>
</@global.main>


