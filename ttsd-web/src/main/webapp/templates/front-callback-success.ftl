<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="成功">
    <#if error??>
        <p class="pay-tip">${error}</p>
    <#else>
        <#if "PTP_MER_BIND_CARD" == service>
            <div class="callBack_container">
                <div class="success_tip_icon"></div>
                <p class="my_pay_tip">银行卡绑定成功</p>
                <div class="handle_btn_container">
                    <div class="my_personal-info">查看我的账户</div>
                    <div class="go_to_recharge toRecharge">去充值</div>
                </div>
            </div>
        </#if>

        <#if "PTP_MER_REPLACE_CARD" == service>
            <div class="callBack_container change_card_calBack">
                <div class="success_tip_icon success_tip_icon_changeCard"></div>
                <p class="my_pay_tip">申请提交成功</p>
                <p class="my_pay_tip_sub">换卡申请最快1个工作日内完成</p>
                <div class="change_card_desc">
                    如果您的账户有余额、有投资中的项目，需要您提交如下审核资料至kefu@tuotiansudai.com人工审核。<br/>
                    需提交资料：<br/>
                    1、手持身份证正反面照片。<br/>
                    2、手持原绑定银行卡正反面照片。<br/>
                    3、手持新绑定的银行卡正反面照片。<br/>
                    照片要求：<br/>
                    需露出脸和手臂的上半身照。<br/>
                    身份证、银行卡证件信息清晰可见。<br/>
                    如需帮助请致电客服  400-169-1188 （服务时间： 9:00-20:00）<br/>
                </div>
                <div class="go_to_invest investBtn" >去投资</div>
            </div>
        </#if>

        <#if "MER_RECHARGE_PERSON" == service>
        <div class="callBack_container">
            <div class="success_tip_icon"></div>
            <p class="my_pay_tip">充值成功，充值金额 ${amount} 元</p>
            <div class="handle_btn_container">
                <div class="see_my_account">查看我的账户</div>
                <div class="go_to_invest investBtn">去投资</div>
            </div>
        </div>
        </#if>

        <#if ["INVEST_PROJECT_TRANSFER", 'INVEST_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
            <div class="callBack_container">
                <div class="success_tip_icon"></div>
                <p class="my_pay_tip">支付成功，您已成功投资 ${amount} 元</p>
                <div class="handle_btn_container">
                    <div class="see_my_account">查看我的账户</div>
                    <div class="see_other_project" id="toProject">看看其他项目</div>
                </div>
            </div>
        </#if>
        <#if ['INVEST_TRANSFER_PROJECT_TRANSFER', 'INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
        <div class="invest-success-container" id="successBox">
            <div class="invest-text-model">
                <i class="success-tip"></i>
                <p class="pay-tip">支付成功，您已成功购买该债权。</p>
                <p class="pay-text">
                    <span class="count-time">5</span>秒后将自动返回“我的账户”。如果页面没有跳转，您可以 <a href="/account">点击这里</a>您还可以<a
                        href="/loan-list">继续购买</a>
                </p>
            </div>
        </div>
        </#if>
        <#if "CUST_WITHDRAWALS" == service>
        <div class="invest-success-container" id="successBox">
            <div class="invest-text-model">
                <p class="pay-tip">申请提现成功,申请提现金额${amount!} 元</p>
                <p class="pay-text">
                    <span class="count-time">5</span>秒后将自动返回“个人资料”，如果没有跳转，您可以 <a href="/account">点击这里</a>
                </p>
            </div>
        </div>
        </#if>
    </#if>

<div class="invest-success-phone">
    <#if error??>
        <p class="pay-tip">${error}</p>
    <#else>
        <#if "PTP_MER_BIND_CARD" == service>
            <p class="pay-tip">银行卡绑定成功</p>
            <p class="pay-text">
                <span class="count-time">5</span>秒后将自动返回“个人资料”，如果没有跳转，您可以 <a href="/personal-info">点击这里</a>
            </p>
            <p class="pay-text">您还可以 <a href="/recharge">去充值</a></p>
        </#if>

        <#if "PTP_MER_REPLACE_CARD" == service>
            <p class="pay-tip">提交成功</p>
            <p class="pay-text">您的换卡申请已提交，换卡申请最快两小时处理完成。</p>
            <p class="pay-text">
                <span class="count-time">5</span>秒后将自动返回“个人资料”，如果没有跳转，您可以 <a href="/personal-info">点击这里</a>
            </p>
        </#if>

        <#if ["INVEST_PROJECT_TRANSFER", 'INVEST_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
            <p class="pay-tip">支付成功，您已成功投资 ${amount} 元</p>
            <p class="pay-text">
                <span class="count-time">5</span>秒后将自动返回“我的账户”。如果页面没有跳转，您可以 <a href="/account">点击这里</a>您还可以<a
                    href="/loan-list">继续投资</a>
            </p>
        </#if>

        <#if ['INVEST_TRANSFER_PROJECT_TRANSFER', 'INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
            <p class="pay-tip">支付成功，您已成功购买该债权。</p>
            <p class="pay-text">
                <span class="count-time">5</span>秒后将自动返回“我的账户”。如果页面没有跳转，您可以 <a href="/account">点击这里</a>您还可以<a
                    href="/loan-list">继续投资</a>
            </p>
        </#if>
    </#if>

    <p class="pay-problem">如有其他疑问请致电客服 400-169-1188<br/>（服务时间：9:00－20:00）</p>
</div>
</@global.main>