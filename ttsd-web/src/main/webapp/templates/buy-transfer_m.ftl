<#--<@global.main pageCss="${css.buy_transfer}" pageJavascript="${js.buy_transfer}" title="转让项目购买详情">-->

<div class="my-account-content apply-transfer buy-transfer" id="transfer_details"
     data-has-bank-card="${hasBankCard?c}" data-user-role="<@global.role hasRole="'INVESTOR'">INVESTOR</@global.role>"
     data-authentication="<@global.role hasRole="'USER'">USER</@global.role>"
     data-estimate="${estimate?string('true', 'false')}"
     style="display: none">
    <input type="hidden" class="bind-data" data-is-anxin-user="${anxinUser?c}">
    <input type="hidden" data-is-authentication-required="${anxinAuthenticationRequired?c}" id="isAuthenticationRequired" data-page="transfer">
    <div class="m-header"><em id="iconTransferDetail" class="icon-left"><i></i></em>立即投资 </div>
    <div class="benefit-box transfer-box">
        <div class="target-category-box transfer" data-url="loan-transfer-detail.ftl">
            <div class="newer-title transfer-title">
                <span>${transferApplication.name!}</span>
            </div>
            <ul class="benefit-list">
                <li>预期收益: <span>${transferApplication.expecedInterest!}</span>元</li>
                <li>账户余额: <span>${transferApplication.balance!}</span>元</li>
            </ul>
        </div>
        <div class="bg-square-box"></div>
    </div>
    <form action="/transfer/purchase" method="post" id="transferForm">
        <div class="input-amount-box">
            <ul class="input-list">
                <li>
                    <label class="transfer-lable">转让价格</label>
                    <input class="transfer-price" type="text" value="${transferApplication.transferAmount}" readonly>
                    <em class="transfer-lable">元</em>
                </li>
            </ul>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="source" value="M"/>
        <input type="hidden" name="loanId" value="${transferApplication.loanId?string.computer}"/>
        <input type="hidden" id="transferApplicationId" name="transferApplicationId" value="${transferApplication.id?string.computer}"/>
        <input type="hidden" id="amount" name="amount" value="${transferApplication.transferAmount}"/>
        <input type="hidden" id="userBalance" name="userBalance" value="${transferApplication.balance!}"/>
        <input type="hidden" value="${anxinAuthenticationRequired?c}" id="isAnxinAuthenticationRequired">
        <input type="hidden" value="${anxinUser?c}" id="isAnxinUser">

        <button id="transferSubmit" type="submit" class="btn-wap-normal">立即投资</button>
    </form>
    <input id="errorMassageTransfer" type="hidden" value="<#if errorMessage?has_content>${errorMessage!}</#if>">

<#if anxinUser != true>
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

<#include "component/anxin-agreement.ftl" />
    <div class="invest-tips-m" style="text-align: center;color: #A2A2A2;margin-top: 40px">市场有风险，投资需谨慎！</div>
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
