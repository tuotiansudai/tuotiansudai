<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.transfer_detail}" pageJavascript="${js.transfer_detail}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="transfer-detail-content" data-user-role="<@global.role hasRole="'INVESTOR'">INVESTOR</@global.role>">
    <div class="detail-intro">
        <div class="transfer-top">
            <span class="product-name">${transferApplication.name!}</span>
            <span class="product-type">原始项目：<a href="/loan/${transferApplication.loanId?string.computer}" target="_blank">${transferApplication.loanName!}</a></span>
            <span class="product-tip">拓天速贷提醒您：投资非存款，投资需谨慎！</span>
        </div>
        <div class="transfer-info">
            <div class="transfer-info-dl">
                <dl>
                    <dt>转让价格</dt>
                    <dd><em><@percentInteger>${transferApplication.transferAmount!}</@percentInteger></em>
                        <i><@percentFraction>${transferApplication.transferAmount!}</@percentFraction></i>元
                    </dd>
                </dl>
                <dl>
                    <dt>项目本金</dt>
                    <dd><em><@percentInteger>${transferApplication.investAmount!}</@percentInteger></em>
                        <i><@percentFraction>${transferApplication.investAmount!}</@percentFraction></i>元
                    </dd>
                </dl>
                <dl>
                    <dt>预期年化收益</dt>
                    <dd><em>${transferApplication.baseRate!}%</em></dd>
                </dl>
                <dl>
                    <dt>剩余期数</dt>
                    <dd><em>${transferApplication.leftPeriod!}</em></dd>
                </dl>
            </div>
            <div class="info-detail">
                <ul class="detail-list">
                    <li>
                        <span>项目到期时间：${transferApplication.dueDate?string("yyyy-MM-dd")}</span>
                    </li>
                    <li>
                        <span>下次回款：${transferApplication.nextRefundDate?string("yyyy-MM-dd")}/${transferApplication.nextExpecedInterest!}元</span>
                    </li>
                    <li>
                        <span>还款方式：按期还收益，到期付本金！</span>
                    </li>
                    <li>
                        <span>转让截止时间：${transferApplication.deadLine?string("yyyy-MM-dd HH:mm:ss")}</span>
                    </li>
                    <li>
                        <span><a href="${staticServer}/pdf/loanAgreementSample.pdf" target="_blank">债权转让协议书(范本)</a></span>
                    </li>
                </ul>
            </div>
        </div>
        <div class="transfer-operat">
            <#if (transferApplication.transferStatus.name() == "SUCCESS")>
                <p class="img-status"><img src="${staticServer}/images/sign/loan/transfered.png"></p>
                <p class="status-text">转让完成时间：${transferApplication.transferTime?string("yyyy-MM-dd HH:mm:ss")}</p>
            <#elseif (transferApplication.transferStatus.name() == "CANCEL")>
                <p class="img-status"><img src="${staticServer}/images/sign/loan/transfercancel.png"></p>
                <p class="status-text"></p>
            <#else>
                <form action="/transfer/purchase" method="post" id="transferForm">
                    <p class="get-money">
                        <span class="name-text" id="tipLayer">认购金额：</span>
                        <span class="money-text">
                            <strong>${transferApplication.transferAmount!}</strong>元
                        </span>
                        <#if errorMessage?has_content>
                            <span class="errorTip hide"><i class="fa fa-times-circle"></i>${errorMessage!}</span>
                        </#if>
                    </p>
                    <p><span class="name-text">预计收益：</span><span class="money-text"><strong>${transferApplication.expecedInterest!}</strong>元</span></p>
                    <p class="user-money"><span class="name-text">账户余额：${transferApplication.balance!} 元</span><span class="money-text"><strong><a href="/recharge">去充值 >></a></strong></span></p>
                    <input type="hidden" id="amount" name="amount" value="${transferApplication.transferAmount}"></input>
                    <input type="hidden" id="userBalance" name="userBalance" value="${transferApplication.balance!}" ></input>
                    <input type="hidden" id="loanId" name="loanId" value="${transferApplication.loanId?string.computer}" ></input>
                    <input type="hidden" id="transferInvestId" name="transferInvestId" value="${transferApplication.id?string.computer}" ></input>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <p><button id="transferSubmit" class="btn-pay btn-normal" type="button">马上投资</button></p>
                </form>
            </#if>
        </div>
    </div>
    <div class="detail-record">
        <div class="transfer-top">
            <span class="product-name">债权承接记录</span>
        </div>
        <div class="transfer-table">
            <#if (transferApplicationReceiver.status?string) == "true">
                <table>
                    <thead>
                    <tr>
                        <th>承接人</th>
                        <th>转让价格(元)</th>
                        <th>承接方式</th>
                        <th>预计收益(元)</th>
                        <th>项目本金(元)</th>
                        <th>承接时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${transferApplicationReceiver.transferApplicationReceiver!}</td>
                        <td>${transferApplicationReceiver.receiveAmount!}</td>
                        <td>
                            <#if transferApplicationReceiver.source == "WEB"><i class="fa fa-internet-explorer" aria-hidden="true"></i>
                            <#elseif transferApplicationReceiver.source == "ANDROID"><i class="fa fa-android" aria-hidden="true">
                            <#elseif transferApplicationReceiver.source == "IOS"><i class="fa fa-apple" aria-hidden="true"></i>
                            <#elseif transferApplicationReceiver.source == "AUTO">自动
                            <#else>
                            </#if>
                        </td>
                        <td>${transferApplicationReceiver.expecedInterest!}</td>
                        <td>${transferApplicationReceiver.investAmount!}</td>
                        <td>${transferApplicationReceiver.transferTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    </tr>
                    </tbody>
                </table>
            <#else >
                <p class="tc text-b">暂无承接记录</p>
            </#if>
        </div>
    </div>
    <#include "coupon-alert.ftl" />
</div>
    <#include "red-envelope-float.ftl" />
</@global.main>