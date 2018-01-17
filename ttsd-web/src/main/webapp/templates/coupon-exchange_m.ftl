<#import "macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.exchange_coupon}" pageJavascript="${m_js.exchange_coupon}" title="优惠券兑换">


<div class="my-account-content amount-overview" id="couponExchange">
    <div class="m-header"><em id="iconExchangeCoupon" class="icon-left"><i class="fa fa-angle-left"></i></em>兑换码兑换</div>
    <form  method="post" id="exchangeForm">
    <ul class="input-list align-flex-start">
        <li>
            <input type="text" id="couponByCode" maxlength="14" value="" placeholder="请输入兑换码">
        </li>
    </ul>
        <button type="button" class="btn-wap-normal next-step" id="submitCode" disabled>兑换</button>
        </form>

</div>
<div class="my-account-content amount-overview" id="couponSuccess" style="display: none;">

    <script type="text/html" id="exchangeSuccessData">
        <div class="m-header"><em id="iconExchangeSuccess" class="icon-left"><i class="fa fa-angle-left"></i></em>兑换码兑换</div>
        <div class="tip-item">
            <p>
                恭喜您：
            </p>
            <p>兑换码<span>{{data.exchangeCode}}</span>兑换成功</p>
        </div>
        <ul class="coupon-list-container">
            <li class="coupon-item">
                <div class="left-item">
                    <dl>
                        <dt>{{data.name}}</dt>
                        <dd>单笔满{{data.investLowerLimit}}元可用</dd>
                        <dd>{{data.minDay}}</dd>
                        <dd>请在{{data.endTime}}日之前使用</dd>
                        <dd class="coupon-line">{{data.couponSource}}</dd>
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
                        <span class="price-text">{{data.amountRate}}</span><span>{{data.unit}}</span>
                    </p>
                    <p>
                        <a class="to-use-btn" href="javascript:;">立即使用</a>
                    </p>
                </div>
            </li>
        </ul>
        <div class="button-note">
            <a href="javascript:;" id="exchangeSuccessConfirm" class="btn-wap-normal next-step" >确定</a>
        </div>

    </script>


</div>

</@global.main>
