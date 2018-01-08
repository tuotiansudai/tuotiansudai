
<#--<@global.main pageCss="${css.buy_loan}" pageJavascript="${js.buy_loan}" title="直投项目购买详情">-->

<div class="my-account-content apply-transfer show-page"  id="buyDetail" style="display: none" data-loan-status="${loan.loanStatus}" data-loan-progress="${loan.progress?string.computer}" data-loan-countdown="${loan.countdown?string.computer}"
     data-authentication="<@global.role hasRole="'USER'">USER</@global.role>" data-user-role="<@global.role hasRole="'INVESTOR'">INVESTOR</@global.role>">
    <div class="m-header"><em id="iconBuy" class="icon-left"><i class="fa fa-angle-left"></i></em>购买详情 </div>
    <#if coupons?has_content>
        <#if maxBenefitUserCoupon??>
            <#switch maxBenefitUserCoupon.couponType>
                <#case "INTEREST_COUPON">
                    <#assign maxBenefitCouponDesc =(maxBenefitUserCoupon.rate * 100)+'%'+maxBenefitUserCoupon.name >
                    <#break>
                <#case "BIRTHDAY_COUPON">
                    <#assign maxBenefitCouponDesc =maxBenefitUserCoupon.name >
                    <#break>
                <#default>
                    <#assign maxBenefitCouponDesc =(maxBenefitUserCoupon.amount / 100)+'元'+maxBenefitUserCoupon.name >
            </#switch>
            <#assign maxBenefitCouponId = maxBenefitUserCoupon.id >
        <#else>
            <#assign maxBenefitCouponDesc ='请选择优惠券'>
        </#if>
    <#else>
        <#assign maxBenefitCouponDesc ='无可用优惠券'>
    </#if>
    <div class="benefit-box">
        <div class="target-category-box" data-url="loan-transfer-detail.ftl">
            <div class="newer-title">
                <span>${loan.name}</span>
                <span class="tip-text">剩余可投 : <@amount>${loan.amountNeedRaised?string.computer}</@amount>元</span>
            </div>
            <ul class="loan-info clearfix">
                <li>
                    <span>
                        <i>最长${loan.raisingDays}天</i>
                    </span>
                    <em>项目期限</em>
                </li>
                <li>
                    <span>
                        <i>${loan.baseRate}+${loan.activityRate}%</i>
                    </span>
                    <em>预计年化收益</em>
                </li>
                <li>
                    <span>
                        <i>${loan.minInvestAmount}元</i>
                    </span>
                    <em>起投金额</em>
                </li>
            </ul>
        </div>
        <div class="bg-square-box"></div>
    </div>
    <form id="investForm" action="/invest" method="post">
        <input type="hidden" name="source" value="M">
        <input class="hid-loan" type="hidden" name="loanId" value="${loan.id?string.computer}"/>
    <div class="input-amount-box">
        <ul class="input-list">
            <li>
                <label>投资金额</label>
                <input type="text"
                       data-min-invest-amount="${loan.minInvestAmount}"
                       data-max-invest-amount="${loan.maxInvestAmount}"
                       data-no-password-remind="${loan.investor.remindNoPassword?c}"
                       data-no-password-invest="${loan.investor.noPasswordInvest?c}"
                       data-auto-invest-on="${loan.investor.autoInvest?c}"
                       data-user-balance="${(loan.investor.balance/100)?string.computer}"
                       data-amount-need-raised="${(loan.amountNeedRaised/100)?string.computer}" name="price" class="input-amount" placeholder="${loan.minInvestAmount}元起投">
                <em class="clear-font"><i id="clearFont" class="fa fa-times" aria-hidden="true"></i></em>
                <em>元</em>
            </li>
            <li class="mt-10">
                <label>预期收益</label>
                <span class="number-text"><strong id="expectedEarnings">0.00</strong>元</span>
            </li>
            <li id="select_coupon" class="select-coupon">
                <label>优惠券</label>
                <input id="couponText" type="text" value="${maxBenefitCouponDesc}" readonly="readonly">
                <input id="couponId" type="hidden" value="${(maxBenefitCouponId?string.computer)!}" name="userCouponIds">
                <em><i class="fa fa-angle-right"></i></em>
            </li>

        </ul>
    </div>

    <button id="investSubmit" type="submit" class="btn-wap-normal" disabled>立即投资</button>
    </form>





</div>

<#--</@global.main>-->
