<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.autumn_luxury}" pageJavascript="${js.autumn_detail}" activeNav="" activeLeftNav="" title="拓天奢品_奢品活动_拓天速贷" keywords="拓天大奖,大奖活动,投资活动,拓天速贷" description="拓天速贷奢华投资活动,海量奢品拓手可得,拓天大奖活动让您左手投资赚收益,右手白拿奢侈品.">
<div class="luxury-list-container">
    <div class="wp clearfix detail-wp">
        <div class="left-bg"></div>
        <div class="right-bg"></div>
        <div class="detail-img">
            <#switch prizeId!"">
                <#case 1>
                    <img src="upload/20160910/3471473485766113.jpg" width="100%">
                    <#break >
                <#case 2>
                    <img src="upload/20160910/54941473485786239.jpg" width="100%">
                    <#break >
                <#case 3>
                    <img src="upload/20160910/7561473485815624.jpg" width="100%">
                    <#break >
                <#case 4>
                    <img src="upload/20160910/75001473485681804.jpg" width="100%">
                    <#break >
                <#case 5>
                    <img src="upload/20160910/42911473485742523.jpg" width="100%">
                    <#break >
                <#case 6>
                    <img src="upload/20160910/47191473485652945.jpg" width="100%">
                    <#break >
                <#case 7>
                    <img src="upload/20160910/42911473485742523.jpg" width="100%">
                    <#break >
                <#case 8>
                    <img src="upload/20160910/27481473485719594.jpg" width="100%">
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