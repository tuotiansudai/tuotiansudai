<#--<@global.main pageCss="${css.buy_transfer}" pageJavascript="${js.buy_transfer}" title="转让项目购买详情">-->

<div class="my-account-content apply-transfer buy-transfer" id="transfer_details" data-user-role="<@global.role hasRole="'INVESTOR'">INVESTOR</@global.role>" style="display: none">
    <div class="m-header"><em id="iconTransferDetail" class="icon-left"><i class="fa fa-angle-left"></i></em>立即投资 </div>
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

<#if anxinUser != true>
    <div class="transfer-notice">
        <div class="agreement-box">
            <span class="init-checkbox-style on">
                 <input type="checkbox" id="skipCheck" class="default-checkbox" checked>
             </span>
            <lable for="agreement">我已阅读并同意<a href="javascript:void(0)" class="link-agree-service">《安心签服务协议》</a>、<a
                    href="javascript:void(0)" class="link-agree-privacy">《隐私条款》</a>、<a href="javascript:void(0)"
                                                                                       class="link-agree-number">《CFCA数字证书服务协议》</a>、<a
                    href="javascript:void(0)" class="link-agree-number-authorize">《CFCA数字证书授权协议》</a> 和<a
                    href="javascript:void(0)" class="link-agree-number"> 《债权转让协议》</a></lable>
        </div>
    </div>
</#if>
<#include "component/anxin-agreement.ftl" />

</div>

<#--</@global.main>-->
