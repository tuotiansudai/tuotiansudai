<#import "macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.buy_free}" pageJavascript="${m_js.buy_free}" title="${serviceName!}">
<div class="my-account-content apply-transfer-success">

        <#if ["INVEST_PROJECT_TRANSFER", 'INVEST_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
            <div class="m-header">投资成功</div>
            <div class="info">
                <i class="icon-success"></i>
                <em>投资成功</em>
                <ul class="input-list">
                    <li>
                        <label>投资金额</label>
                        <span class="money">${amount}元</span>
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
            <div class="button-note">
                <a data-url="/m/loan-list" class="count-btn btn-wap-normal next-step">确定</a>
            </div>
        </#if>
        <#if "CUST_WITHDRAWALS" == service>
            <div class="m-header">提现成功</div>
            <div class="info">
                <i class="icon-success"></i>
                <em>提现成功</em>
                <ul class="input-list">
                    <li>
                        <label>到账卡号</label>
                        <span style="white-space: nowrap">${bankName!} ${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1 **** $2","r")}</span>
                    </li>
                    <li>
                        <label>提现金额</label>
                        <span class="money">${amount!}元</span>
                    </li>
                    <li>
                        <label>订单号</label>
                        <span>${orderId}</span>
                    </li>
                </ul>
            </div>
            <div class="button-note cash">
                <a data-url="/m/account" class="count-btn btn-wap-normal next-step">确定</a>
            </div>
            <div class="service">客服电话：400-169-1188（服务时间：9:00-20:00）</div>
        </#if>
        <#if "PTP_MER_BIND_CARD" == service>
            <div class="my-account-content apply-transfer-success">
                <div class="m-header">银行卡绑定成功</div>
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
                <a data-url="/m/personal-info" class="count-btn btn-wap-normal next-step">确定</a>
            </div>
        </#if>
        <#if "MER_RECHARGE_PERSON" == service>
            <div class="m-header">充值成功</div>
            <div class="info">
                <i class="icon-success"></i>
                <em>充值成功</em>
                <ul class="input-list">
                    <li>
                        <label>充值卡号</label>
                        <span style="white-space: nowrap">${bankName!} ${cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1 **** $2","r")}</span>
                    </li>
                    <li>
                        <label>充值金额</label>
                        <span><em class="money">${(amount/100)!}</em>元</span>
                    </li>
                    <li>
                        <label>订单号</label>
                        <span>${orderId}</span>
                    </li>
                </ul>
            </div>
            <div class="button-note">
                <a data-url="/m/account" href="javascript:;" class="count-btn btn-wap-normal next-step">确定</a>
            </div>
            <div class="service">客服电话：400-169-1188（服务时间：9:00-20:00）</div>
        </#if>
        <#if ['INVEST_TRANSFER_PROJECT_TRANSFER', 'INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
            <div class="my-account-content apply-transfer-success" >
                <div class="m-header">转让成功</div>
                <div class="info">
                    <i class="icon-success"></i>
                    <em>转让成功</em>
                    <ul class="input-list">
                        <li>
                            <label>投资金额</label>
                            <span class="money">${amount}元</span>
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

                <div class="button-note">
                    <a data-url="/m/transfer-list" href="javascript:;" class="count-btn btn-wap-normal next-step">确定</a>
                </div>
            </div>
        </#if>

</div>



</div>
</@global.main>
