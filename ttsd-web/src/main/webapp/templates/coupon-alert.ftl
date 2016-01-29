<#if couponAlert??>
<div class="coupon-alert">
    <div class="coupon-model-list">
        <div class="coupon-close"></div>
        <div class="coupon-text">
            <span class="text-number"><@amount>${couponAlert.amount?string.computer}</@amount></span>
            <span class="text-unit">元</span>
        </div>
        <div class="coupon-name">${couponAlert.couponType.getName()}</div>
        <div class="coupon-time">
            请在${couponAlert.expiredDate?string('yyyy-MM-dd')}前使用
        </div>
        <a href="/my-treasure" class="coupon-link"></a>
    </div>
</div>
</#if>