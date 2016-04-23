<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.transfer_detail}" pageJavascript="${js.transfer_detail}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="transfer-detail-content">
    <div class="detail-intro">
        <div class="transfer-top">
            <span class="product-name">${transferApplication.name!}</span>
            <span class="product-type">原始项目：${transferApplication.loanName!}</span>
            <span class="product-tip">拓天速贷提醒您：理财非存款，投资需谨慎！</span>
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
                    <dt>代收本金</dt>
                    <dd><em><@percentInteger>${transferApplication.investAmount!}</@percentInteger></em>
                        <i><@percentFraction>${transferApplication.investAmount!}</@percentFraction></i>元
                    </dd>
                </dl>
                <dl>
                    <dt>年化收益</dt>
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
                        <span>还款方式：${transferApplication.loanType!}</span>
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
            <#else>
                <p class="get-money"><span class="name-text">认购金额：</span><span class="money-text"><strong>${transferApplication.transferAmount!}</strong>元</span> </p>
                <p><span class="name-text">预计收益：</span><span class="money-text"><strong>${transferApplication.expecedInterest!}</strong>元</span></p>
                <p class="user-money"><span class="name-text">账户余额：${transferApplication.balance!} 元</span><span class="money-text"><strong><a href="#">去充值 >></a></strong></span></p>
                <p><a href="#" class="btn-normal invest-btn">马上投资</a></p>
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
                            <th>预期收益(元)</th>
                            <th>待收本金(元)</th>
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