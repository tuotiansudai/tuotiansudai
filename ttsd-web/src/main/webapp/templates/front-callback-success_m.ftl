<#import "macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.buy_free}" pageJavascript="${m_js.buy_free}" title="${serviceName!}">
<div class="my-account-content apply-transfer-success">
    <div class="m-header">${serviceName!}</div>
    <div class="info">
        <i class="icon-success"></i>
        <em>${serviceName!}</em>
        <#if ["INVEST_PROJECT_TRANSFER", 'INVEST_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
            <ul class="input-list">
                <li>
                    <label>投资金额</label>
                    <span>${amount}元</span>
                </li>
                <li>
                    <label>所投项目</label>
                    <span>${loanName}</span>
                </li>
                <li>
                    <label>项目编号</label>
                    <span>${loanId?string.computer}</span>
                </li>
            </ul>
        </#if>
        <#if "CUST_WITHDRAWALS" == service>
            <ul class="input-list">
                <li>
                    <label>到账卡号</label>
                    <span style="white-space: nowrap">${bankName!} ${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1 **** $2","r")}</span>
                </li>
                <li>
                    <label>提现金额</label>
                    <span>${amount!}元</span>
                </li>
                <li>
                    <label>订单号</label>
                    <span>${orderId}</span>
                </li>
            </ul>
        </#if>
        <#if "PTP_MER_BIND_CARD" == service>
            <div class="my-account-content apply-transfer-success">
                <div class="info">
                    <i class="icon-success"></i>
                    <em>银行卡绑定成功</em>
                    <ul class="input-list">
                        <li>
                            <label>银行卡号</label>
                            <span>${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="button-note">
                <a id="countDownBtn" href="/m/personal-info" class="btn-wap-normal next-step">确定</a>
            </div>
        </#if>
        <#if "MER_RECHARGE_PERSON" == service>
            <ul class="input-list">
                <li>
                    <label>充值卡号</label>
                    <span style="white-space: nowrap">${bankName!} ${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1 **** $2","r")}</span>
                </li>
                <li>
                    <label>充值金额</label>
                    <span>${(amount/100)!}元</span>
                </li>
                <li>
                    <label>订单号</label>
                    <span>${orderId}</span>
                </li>
            </ul>
        </#if>
        <#if ['INVEST_TRANSFER_PROJECT_TRANSFER', 'INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
            <div class="my-account-content apply-transfer-success" >

                <ul class="input-list">
                    <li>
                        <label>投资金额</label>
                        <span>${amount}元</span>
                    </li>
                    <li>
                        <label>所投项目</label>
                        <span>${loanName}</span>
                    </li>
                    <li>
                        <label>项目编号</label>
                        <span>${loanId?string.computer}</span>
                    </li>
                </ul>


            </div>
        </#if>
    </div>
</div>
<div class="button-note">
    <a id="countDownBtn" href="javascript:;" class="btn-wap-normal next-step">确定</a>
</div>

<div class="footer">
    <p>客服电话：400-169-1188（服务时间：9:00-20:00）</p>
</div>
</@global.main>
