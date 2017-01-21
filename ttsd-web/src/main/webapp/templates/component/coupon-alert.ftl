<#if couponAlert??>
<div class="coupon-alert">
    <div class="coupon-model-list"  id="couponAlertFrame">
        <div class="coupon-close"></div>
        <div class="coupon-text">
            <span class="text-number"><@amount>${couponAlert.amount?string.computer}</@amount></span>
            <span class="text-unit">元</span>
        </div>
        <div class="coupon-name">${couponAlert.couponType.getName()}</div>
        <div class="coupon-tip">
            已经放到您的口袋了<br/>快来投资吧
        </div>
        <div class="coupon-place">
            亲可以在“我的帐户－我的宝藏”中找到哦！
        </div>
        <#if (couponAlert.couponType == 'NEWBIE_COUPON')>
            <a href="/loan/1" class="coupon-link" id="beginGetFree">开始体验</a>
        <#else>
            <a href="/my-treasure" class="coupon-link">立即使用</a>
        </#if>

    </div>
</div>
</#if>

