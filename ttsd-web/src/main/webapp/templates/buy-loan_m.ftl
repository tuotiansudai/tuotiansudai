
<#--<@global.main pageCss="${css.buy_loan}" pageJavascript="${js.buy_loan}" title="直投项目购买详情">-->
<div class="my-account-content apply-transfer show-page"  id="buyDetail" style="display: none"
     data-has-bank-card="${hasBankCard?c}" data-loan-status="${loan.loanStatus}"
     data-loan-progress="${loan.progress?string.computer}" data-loan-countdown="${loan.countdown?string.computer}"
     data-authentication="<@global.role hasRole="'USER'">USER</@global.role>"
     data-estimate="${estimate???string('true', 'false')}"
     data-user-role="<@global.role hasRole="'INVESTOR'">INVESTOR</@global.role>"
     data-estimate-type="${(estimate.type)!''}" data-estimate-level="${(estimate.lower)!''}"  data-estimate-limit="${estimateLimit!''}"
     data-loan-estimate-type="${loan.estimate!''}" data-loan-estimate-level="${loan.estimateLevel!''}" >
    <div class="m-header"><em id="iconBuy" class="icon-left"><i></i></em>购买详情 </div>
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
        <input type="hidden" class="bind-data" data-is-anxin-user="${loan.investor.anxinUser?c}">
        <input type="hidden" data-is-authentication-required="${loan.investor.authenticationRequired?c}" id="isAuthenticationRequired" data-page="buy">
        <div class="target-category-box" data-url="loan-transfer-detail.ftl">
            <div class="newer-title buy-title">
                <span>${loan.name}</span>
                <span class="tip-text">剩余可投 : <@amount>${loan.amountNeedRaised?string.computer}</@amount>元</span>
            </div>
            <ul class="loan-info clearfix">
                <li>
                    <span>
                        <i>最长${loan.duration}天</i>
                    </span>
                    <em>项目期限</em>
                </li>
                <li>
                    <span>
                        <i><@percentInteger>${loan.baseRate+loan.activityRate}</@percentInteger><@percentFraction>${loan.baseRate+loan.activityRate}</@percentFraction>
                        <#if extraLoanRates??>
                            ~ <@percentInteger>${loan.baseRate+loan.activityRate+loan.maxExtraLoanRate}</@percentInteger><@percentFraction>${loan.baseRate+loan.activityRate+loan.maxExtraLoanRate}</@percentFraction>
                        </#if>%</i>
                    </span>
                    <em>约定年化利率</em>
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
                       data-duration="${loan.duration}"
                       data-product-type="${loan.productType}"
                       data-min-invest-amount="${loan.minInvestAmount}"
                       data-max-invest-amount="${loan.maxInvestAmount}"
                       data-no-password-remind="${loan.investor.remindNoPassword?c}"
                       data-no-password-invest="${loan.investor.noPasswordInvest?c}"
                       data-auto-invest-on="${loan.investor.autoInvest?c}"
                       data-user-balance="${(loan.investor.balance/100)?string.computer}"
                       data-amount-need-raised="${(loan.amountNeedRaised/100)?string.computer}" value="${loan.investor.maxAvailableInvestAmount}" name="amount" class="input-amount" placeholder="${loan.minInvestAmount}元起投">
                <em class="clear-font"><i id="clearFont" class="icon-close"></i></em>
                <em>元</em>
            </li>
            <li class="mt-10">
                <label>预期收益</label>
                <span class="number-text"><strong id="expectedEarnings">0.00</strong><strong class="experience-income"></strong>
                    元</span>
            </li>
            <li id="select_coupon" class="select-coupon">
                <label>优惠券</label>
                <span id="couponText" type="text">${maxBenefitCouponDesc}</span>
                <input id="couponId" type="hidden" value="${(maxBenefitCouponId?string.computer)!}" name="userCouponIds">
                <input type="hidden" id="maxBenifit" value="${(maxBenefitUserCoupon.couponId?string.computer)!}">
                <em><i class="fa fa-angle-right"></i></em>
            </li>

        </ul>
    </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button id="investSubmit" type="submit" class="immediate-investment btn-wap-normal" >
   立即投资</button>
    </form>
    <input id="errorMassage" type="hidden" value="<#if errorMessage?has_content>${errorMessage!}</#if>">


<@global.role hasRole="'INVESTOR'">
    <#if !loan.investor.anxinUser>
        <div class="transfer-notice">
            <div class="agreement-box">
            <span class="init-checkbox-style on">
                 <input type="checkbox" id="skipCheck" class="default-checkbox" checked>
             </span>
                <lable for="agreement">我已阅读并同意<a href="javascript:void(0)" class="link-agree-service">《安心签服务协议》</a>、<a
                        href="javascript:void(0)" class="link-agree-privacy">《隐私条款》</a>、<a href="javascript:void(0)"
                                                                                           class="link-agree-number">《CFCA数字证书服务协议》</a>和<a
                        href="javascript:void(0)" class="link-agree-number-authorize">《CFCA数字证书授权协议》</a> </lable>
            </div>
        </div>
    </#if>
</@global.role>


<#include "component/anxin-agreement.ftl" />
    <div class="sectionNone"></div>
    <div class="invest-tips-m" style="text-align: center;color: #A2A2A2">市场有风险，投资需谨慎！</div>
</div>


<div id="authorization_message" style="display: none">
    <div class="goBack_wrapper">
        安心签代签署授权
        <div class="go-back-container" id="goPage_3">
            <span class="go-back"></span>
        </div>
    </div>
    <div class="my-account-content anxin-electro-sign" id="anxinAuthorization">
        <div class="cfca-info">
            安心签是由中国金融认证中心（CFCA）为拓天速贷投资用户提供的一种电子缔约文件在线签署、存储和管理服务的平台功能。它形成的电子缔约文件符合中国法律规定，与纸质文件具有同样的法律效力。
        </div>

        <div class="cfca-advantage">
            <span>
                <i></i>
                保密性
            </span>
            <span>
                <i></i>
                不可篡改性
            </span>
            <span>
                <i></i>
                可校验性
            </span>
        </div>
        <div class="identifying-code">
            <input type="text" maxlength="6" class="skip-phone-code" id="skipPhoneCode" placeholder="请输入验证码" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
            <div class="close_btn"></div>
            <span class="button-identify">
                 <button type="button" class="get-skip-code" id="getSkipCode" data-voice="false">获取验证码</button>
                 <i class="microphone" id="microPhone" data-voice="true"></i>
            </span>
            <div class="countDownTime" style="display: none">(<span class="seconds"></span>)秒后重新获取</div>
        </div>
        <div class="error" style="display: none">验证码不正确</div>
        <button type="button" class="btn-wap-normal next-step" id="toOpenSMS" disabled>立即授权</button>
        <div class="agreement-box">
            <span class="init-checkbox-style on" style="width: .75rem">
                 <input type="checkbox" id="readOk1" class="default-checkbox" checked>
             </span>
            <lable for="agreement">我已阅读并同意<a href="javascript:void(0)" class="link-agree-free-SMS">《短信免责声明》</a></lable>
        </div>
    </div>
</div>

<#--</@global.main>-->
