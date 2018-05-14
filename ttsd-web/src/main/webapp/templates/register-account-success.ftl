<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="成功">
<div class="callBack_container" id="registerSuccess">
    <div class="head-banner">
    </div>
    <div class="account-head">
        <div class="progress-wrap clearfix">
            <dl class="progress-account fl">
                <dt><span class="account"></span></dt>
                <dd class="active">实名认证</dd>
            </dl>
            <dl class="arrow"></dl>
            <dl class="progress-bind-card fr">
                <dt><span class="bind-card"></span></dt>
                <dd>绑定银行卡</dd>
            </dl>
        </div>

    </div>
    <div class="success_tip_icon"></div>
    <p class="my_pay_tip">实名认证成功</p>
    <div class="handle_btn_container">
        <div class="see_other_project toLocationBtn">去绑卡</div>
    </div>
</div>
</@global.main>