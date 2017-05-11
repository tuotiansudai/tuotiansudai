<#import "../macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="${js.autumn_detail}" activeNav="" activeLeftNav="" title="拓荒计划_旅游活动_拓天速贷" keywords="拓荒计划,免费旅游,旅游活动投资,拓天速贷" description="拓天速贷金秋拓荒计划,多条旅游线路免费玩,你旅游我买单,邀请好友注册也可获得免费旅游大奖.">
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
    <#switch prizeId!"">
        <#case 1>
            <img src="${commonStaticServer}upload/20160913/97751473762817444.jpg" width="100%">
            <#break >
        <#case 2>
            <img src="${commonStaticServer}upload/20160913/90391473762899533.jpg" width="100%">
            <#break >
        <#case 3>
            <img src="${commonStaticServer}upload/20160913/54491473762873600.jpg" width="100%">
            <#break >
    </#switch>

    <div class="tc">
        <a href="/loan-list" class="btn-normal autumn-travel-invest-channel">立即投资</a>
    </div>
</div>
</@global.main>


