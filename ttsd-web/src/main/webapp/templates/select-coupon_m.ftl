<div class="my-account-content amount-overview" id="couponList" style="display: block;">
    <div class="select-btn">
        <p class="to-coupon">
            <a href="exchange-coupon.ftl">
                <i class="fa fa-plus-circle"></i>兑换码兑换
            </a>
        </p>
        <p class="info-list">
            <a href="use-rule.ftl">使用规则？</a>
            <a href="buy-loan.ftl">暂时不使用</a>
        </p>
    </div>
    <ul class="coupon-list-container">
<#if coupons?has_content>
    <#list coupons as coupon>
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

        <li class="coupon-item"
            data-coupon-id="${coupon.couponId?string.computer}"
            data-user-coupon-id="${coupon.id?string.computer}"
            data-coupon-type="${coupon.couponType}"
            data-product-type-usable="${coupon.productTypeList?seq_contains(loan.productType)?string('true', 'false')}"
            data-coupon-end-time="${coupon.endTime?string("yyyy-MM-dd")}T${coupon.endTime?string("HH:mm:ss")}">
            <div class="left-item">
                <dl>
                    <dt>${coupon.name}</dt>
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
                            <span class="price-text">${coupon.rate * 100}</span><span>%</span>
                            <#break>
                        <#case "BIRTHDAY_COUPON">
                            <span class="price-text"> ${coupon.name} </span><span></span>
                            <#break>
                        <#default>
                            <span class="price-text">${(coupon.amount / 100)?string.computer}</span><span>元</span>
                    </#switch>
                </p>
                <p>
                    <a href="">立即使用</a>
                </p>
            </div>
        </li>
        </#if>
    </#list>
</#if>
    </ul>
</div>

