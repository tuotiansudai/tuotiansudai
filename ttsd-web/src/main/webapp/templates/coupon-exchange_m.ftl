<#import "macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.exchange_coupon}" pageJavascript="${m_js.exchange_coupon}" title="优惠券兑换">


<div class="my-account-content amount-overview" id="couponList">
    <form  method="post" id="bankForm">
    <ul class="input-list align-flex-start">
        <li>
            <input type="text" name="couponNumber" id="couponNumber" value="" placeholder="请输入兑换码">
        </li>
    </ul>
        <input type="hidden" name="bankName" value="">
        <button type="submit" class="btn-wap-normal next-step"  disabled>兑换</button>
        </form>
</div>

<div class="my-account-content amount-overview" id="couponList">
    <div class="tip-item">
        <p>
            恭喜您：
        </p>
        <p>兑换码<span>WERTYUIOON94TI</span>兑换成功</p>
    </div>
    <ul class="coupon-list-container">
        <li class="coupon-item">
            <div class="left-item">
                <dl>
                    <dt>现金红包</dt>
                    <dd>单笔满500.00元可用</dd>
                    <dd>可用于60天以上标的</dd>
                    <dd>请在2016-12-22日之前使用</dd>
                    <dd class="coupon-line">拓天速贷平台赠送</dd>
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
                    <span class="price-text">28</span><span>元</span>
                </p>
                <p>
                    <a href="">立即使用</a>
                </p>
            </div>
        </li>
    </ul>
    <div class="button-note">
        <a href="#" class="btn-wap-normal next-step" >确定</a>
    </div>
</div>
</@global.main>
