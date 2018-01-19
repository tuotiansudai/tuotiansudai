<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.new_year_increase}" pageJavascript="${js.new_year_increase}"  title="新年更有钱">

<div class="new-year-container clearfix" id="newYearIncrease" data-activitystatus="${activityStatus}" data-success="<#if drawSuccess??>${drawSuccess?c}</#if>" data-drew="<#if drewCoupon??>${drewCoupon?c}</#if>">
    <div class="new-banner">
    </div>
    <div class="main-content">
        <div class="activity-page content">
            <div class="interest-rate-coupons"></div>
            <h4 class="activity-time"><em></em> <span>活动时间：2月1日-2月28日</span></h4>
            <div class="coupon coupon3"></div>
            <div class="coupon coupon5"></div>
    <#if activityStatus == "START" || activityStatus == "NOT_START">
            <button id="toGet" class="get-coupon-btn"></button>
    <#else>
        <button id="hasEnd" class="get-coupon-btn btn-end"></button>
    </#if>
        </div>
    </div>
    <div class="my-layer" style="display: none">每个用户只能领取一次哦！</div>

</div>
</@global.main>