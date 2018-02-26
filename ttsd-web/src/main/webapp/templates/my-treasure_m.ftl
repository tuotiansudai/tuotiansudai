<#import "macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.coupon}" pageJavascript="${m_js.coupon}" title="优惠券">

<div class="my-account-content amount-overview" id="couponList">
    <div class="m-header"><em class="icon-left" id="goBackIcon"><i></i></em>优惠券</div>
    <div class="select-btn">
        <p class="to-coupon">
            <a href="/m/my-treasure/coupon-exchange">
                <i class="icon"></i>兑换码兑换
            </a>
        </p>
    </div>
    <ul class="coupon-list-container">
            <#list unusedCoupons as coupon>
                <#if !coupon.shared>
                    <#assign minProductType=361>
                    <#list coupon.productTypeList as productType>
                        <#if productType.getDuration() < minProductType>
                            <#assign minProductType=productType.getDuration()>
                        </#if>
                    </#list>
                    <#if minProductType=30><#assign couponTips='可用于任意期限标的'></#if>
                    <#if minProductType=90><#assign couponTips='可用于60天及以上标的'></#if>
                    <#if minProductType=180><#assign couponTips='可用于120天及以上标的'></#if>
                    <#if minProductType=360><#assign couponTips='可用于200天及以上标的'></#if>

        <li class="coupon-item to-use_coupon">
            <div class="left-item">
                <dl>
                    <dt>${coupon.couponType.getName()}</dt>
                    <dd>单笔满${(coupon.investLowerLimit / 100)?string.computer}元使用</dd>
                    <dd>${couponTips!}</dd>
                    <dd>请在${coupon.endTime?string("yyyy-MM-dd")}日之前使用</dd>
                    <dd class="coupon-line">${coupon.couponSource!}</dd>
                </dl>
                <ul class="circle-line">
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                    <li></li>
                </ul>
            </div>
            <div class="right-item">
                <p>
                    <#switch coupon.couponType>
                        <#case "INTEREST_COUPON">
                            <span class="price-text">${coupon.rate * 100}</span><span class="price-unit">%</span>
                            <#break>
                        <#case "BIRTHDAY_COUPON">
                            <span class="price-text"> ${coupon.name} </span><span class="price-unit"></span>
                            <#break>
                        <#default>
                            <span class="price-text">${(coupon.couponAmount / 100)?string.computer}</span><span class="price-unit">元</span>
                    </#switch>
                </p>
                <p>
                    <a href="/m/loan-list">立即使用</a>
                </p>
            </div>
        </li>
                </#if>
            <#else>
            <li class="no-coupon">
                <em class="icon"></em>
                <p>暂无未使用的优惠券</p>
            </li>
            </#list>
    </ul>
</div>
</@global.main>
