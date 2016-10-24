<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.double_eleven}" pageJavascript="${js.double_eleven}" activeNav="" activeLeftNav="" title="拓天英豪榜_英豪榜活动_拓天速贷" keywords="拓天英豪榜,英豪榜活动,投资排行榜,拓天速贷" description="拓天速贷投资排行榜活动,狂欢大轰趴,连嗨三周半,打响排行保卫战,拓天英豪榜投资活动每日神秘大奖悬赏最具实力的投资人.">
<div class="hero-standings-container">
    <div class="top-intro-img">
        <img src="${staticServer}/activity/images/hero-standings/top-intro.png" alt="英豪榜" width="100%" class="top-img">
        <img src="${staticServer}/activity/images/hero-standings/top-intro-phone.png" alt="英豪榜" width="100%" class="top-img-phone">
    </div>
    <div class="hero-content-group">
        <div class="wp clearfix">
            <div class="reg-tag-current" style="display: none">
                <#include '../register.ftl' />
            </div>
        </div>
    </div>
</div>
</@global.main>