<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="" activeNav="我的账户" activeLeftNav="我的宝藏" title="我的宝藏">

<div class="content-container my-treasure-content">
    <h4 class="column-title"><em class="tc">我的宝藏</em></h4>
    <#list coupons as coupon>
    <div class="experience-ticket-box ${coupon.used?string('ticket-status-used', '')} ${coupon.expired?string('ticket-status-expired', '')}">
        <div class="vertical-line"></div>
        <div class="ticket-amount fl tc">
            <h3>${coupon.name}</h3>
            <strong><@amount>${coupon.amount?string(0)}</@amount>元</strong>
            <time>有效期：在${coupon.endTime?date}前使用</time>
        </div>
        <div class="ticket-info fr">
            <dl class="pad-s">
                <dt>使用条件：</dt>
                <dd>
                    在拓天速贷平台投资时使用。
                </dd>
                <dt>使用方法：</dt>
                <dd>
                    在投资金额输入框的下方勾
                    选“使用体验劵”即可使用。
                </dd>
                <dd class="tc"><a href="${coupon.valid?string('/loan-list','javascript:void(0);')}" class="btn-action">立即使用</a></dd>
            </dl>
        </div>
        <div class="sign-seal sign-used"><span>已<br/>使用</span></div>
        <div class="sign-seal sign-expired"><span>已<br/>过期</span></div>
    </div>
    <#else>
    <p class="no-treasure-tip tc pad-m">您当前没有宝藏，敬请期待！</p>
    </#list>

    <#if coupons?has_content>
    <div class="ticket-use-help clear-blank-m">
        <b>体验券使用规则：</b>
        <p>
            1.  体验券仅适用于标的投资；<br/>
            2.  体验券使用后的期限根据具体标的的投资期限，体验结束后系统自动收回本金，收益转 回用户账户内，详见“我的账号”→“资金管理”；<br/>
            3.  如体验券中有限制条件， 用户必须按照限制条件使用。<br/>
        </p>
    </div>
    </#if>
</div>
</@global.main>