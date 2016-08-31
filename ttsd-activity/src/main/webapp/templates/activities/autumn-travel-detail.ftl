<#import "../macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" activeNav="" activeLeftNav="" title="活动中心_投资活动_拓天速贷" keywords="拓天活动中心,拓天活动,拓天投资列表,拓天速贷" description="拓天速贷活动中心为投资用户提供投资大奖,投资奖励,收益翻倍等福利,让您在赚钱的同时体验更多的投资乐趣.">
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


