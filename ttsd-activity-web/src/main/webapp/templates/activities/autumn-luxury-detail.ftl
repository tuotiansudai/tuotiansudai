<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.autumn_luxury}" pageJavascript="${js.autumn_detail}" activeNav="" activeLeftNav="" title="拓天奢品_奢品活动_拓天速贷" keywords="拓天大奖,大奖活动,投资活动,拓天速贷" description="拓天速贷奢华投资活动,海量奢品拓手可得,拓天大奖活动让您左手投资赚收益,右手白拿奢侈品.">
<div class="luxury-list-container">
    <div class="wp clearfix detail-wp">
        <div class="left-bg"></div>
        <div class="right-bg"></div>
        <div class="detail-img">
            <#switch prizeId!"">
                <#case 1>
                    <img src="${commonStaticServer}upload/20160910/95571473486174977.jpg" width="100%">
                    <#break >
                <#case 2>
                    <img src="${commonStaticServer}upload/20160910/93931473486149974.jpg" width="100%">
                    <#break >
                <#case 3>
                    <img src="${commonStaticServer}upload/20160910/34671473486198463.jpg" width="100%">
                    <#break >
                <#case 4>
                    <img src="${commonStaticServer}upload/20160910/49071473486061671.jpg" width="100%">
                    <#break >
                <#case 5>
                    <img src="${commonStaticServer}upload/20160910/14001473486090062.jpg" width="100%">
                    <#break >
                <#case 6>
                    <img src="${commonStaticServer}upload/20160910/60391473486032623.jpg" width="100%">
                    <#break >
                <#case 7>
                    <img src="${commonStaticServer}upload/20160910/16411473486130494.jpg" width="100%">
                    <#break >
                <#case 8>
                    <img src="${commonStaticServer}upload/20160910/59501473486109950.jpg" width="100%">
                    <#break >
            </#switch>
        </div>
        <div class="detail-btn">
        	<@global.isAnonymous>
                <a href="/login?redirect=/activity/autumn/luxury">立即投资</a>
            </@global.isAnonymous>
            <@global.isNotAnonymous>
                <a href="/loan-list" class="autumn-luxury-invest-channel">立即投资</a>
            </@global.isNotAnonymous>
        </div>
    </div>
</div>
</@global.main>