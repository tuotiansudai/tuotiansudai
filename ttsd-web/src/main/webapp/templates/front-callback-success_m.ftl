<#import "macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.buy_free}" pageJavascript="${m_js.buy_free}" title="直投项目购买成功">
    <#if ["INVEST_PROJECT_TRANSFER", 'INVEST_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
    <div class="my-account-content apply-transfer-success" >
        <div class="info">
            <i class="icon-success"></i>
            <em>投资成功</em>

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
    </div>
    <div class="button-note">
        <a id="countDownBtn" href="javascript:;" class="btn-wap-normal next-step" >确定</a>
    </div>
    </#if>

    <#if "PTP_MER_BIND_CARD" == service>
    <div class="my-account-content apply-transfer-success" >
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
        <a id="countDownBtn" href="/m/personal-info" class="btn-wap-normal next-step" >确定</a>
    </div>
    </#if>

</@global.main>
