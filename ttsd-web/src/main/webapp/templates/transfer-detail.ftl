<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.transfer_detail}" pageJavascript="${js.transfer_detail}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="transfer-detail-content">
    <div class="detail-intro">
        <div class="transfer-top">
            <span class="product-name">ZR20160315-001</span>
            <span class="product-type">原始项目：速盈利个人借款</span>
            <span class="product-tip">拓天速贷提醒您：理财非存款，投资需谨慎！</span>
        </div>
        <div class="transfer-info">
            <div class="transfer-info-dl">
                <dl>
                    <dt>转让价格</dt>
                    <dd><em>999999</em>
                    <i>.00</i>元
                    </dd>
                </dl>
                <dl>
                    <dt>代收本金</dt>
                    <dd><em>100000</em><i>.00</i>元</dd>
                </dl>
                <dl>
                    <dt>年化收益</dt>
                    <dd><em>20%</em></dd>
                </dl>
                <dl>
                    <dt>剩余期数</dt>
                    <dd><em>2</em></dd>
                </dl>
            </div>
            <div class="info-detail">
                <ul class="detail-list">
                    <li>
                        <span>项目到期时间：2017-01-22</span>
                    </li>
                    <li>
                        <span>下次回款：2016-08-22/10,000.00元</span>
                    </li>
                    <li>
                        <span>还款方式：先付利息后还本金。</span>
                    </li>
                    <li>
                        <span>转让截止时间：2016-04-13 0点</span>
                    </li>
                    <li>
                       <span><a href="${staticServer}/pdf/loanAgreementSample.pdf" target="_blank">债权转让协议书(范本)</a></span>
                    </li>
                </ul>
            </div>
        </div>
        <div class="transfer-operat">
            <p class="get-money"><span class="name-text">认购金额：</span><span class="money-text"><strong>999,999.00</strong>元</span> </p>
            <p><span class="name-text">预计收益：</span><span class="money-text"><strong>1200.00</strong>元</span></p>
            <p class="user-money"><span class="name-text">账户余额：200 元</span><span class="money-text"><strong><a href="#">去充值 >></a></strong></span></p>
            <p><a href="#" class="btn-normal invest-btn">马上投资</a></p>
        </div>
    </div>
    <div class="detail-record">
        <div class="transfer-top">
            <span class="product-name">债权承接记录</span>
        </div>
        <div class="transfer-table">
            <table>
                <thead>
                    <tr>
                        <th>承接人</th>
                        <th>承接金额(元)</th>
                        <th>承接方式</th>
                        <th>预期利息(元)</th>
                        <th>项目优惠(元)</th>
                        <th>待收本金(元)</th>
                        <th>承接时间</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>liu******</td>
                        <td>9,999,999.00</td>
                        <td><i class="fa fa-internet-explorer"></i></td>
                        <td>200.00</td>
                        <td>200.00</td>
                        <td>10,000,000.00</td>
                        <td>2016-03-17 13:23:37</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <#include "coupon-alert.ftl" />
</div>
<#include "red-envelope-float.ftl" />
</@global.main>