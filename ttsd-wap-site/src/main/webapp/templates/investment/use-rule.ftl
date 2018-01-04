<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'use_rule' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.use_rule}" pageJavascript="${js.use_rule}" title="使用规则">


<div class="rule-content amount-overview" id="couponList">
    <dl class="rule-item">
        <dt>加息券使用规则</dt>
        <dd>
            <p class="title-item">Q：加息券是什么？</p>
            <p>加息券是在标的年化收益的基础上，另外给予用户一定的额外年化收益。</p>
        </dd>
        <dd>
            <p class="title-item">Q：加息券如何使用？</p>
            <p>在相关标的页面中，点击“立即购买”，在购买界面输入购买金额（金额需大于或等于限制条件中的金额）后，选择使用加息券。</p>
        </dd>
    </dl>
    <dl class="rule-item">
        <dt>现金红包使用规则</dt>
        <dd>
            <p class="title-item">Q：红包如何使用？</p>
            <p>在相关标的页面中，点击“立即购买”，在购买界面输入购买金额（金额需大于或等于限制条件中的金额）后，选择使用红包。</p>
        </dd>
        <dd>
            <p class="title-item">Q：红包能否返现？</p>
            <p>在投资过程中使用红包，投资成功放款后即可返现，返现获得的现金可在“我的”中查询、提现。</p>
        </dd>
    </dl>
    <dl class="rule-item">
        <dt>其他相关问题</dt>
        <dd>
            <p class="title-item">Q：如何获得相关优惠券（加息券/红包）？</p>
            <p>用户可以通过抢夺标王、参与活动等获得相关奖励,具体情况请时时关注拓天速贷最新动态。</p>
        </dd>
        <dd>
            <p class="title-item">Q：红包能否返现？</p>
            <p>在投资过程中使用红包，投资成功放款后即可返现，返现获得的现金可在“我的”中查询、提现。</p>
        </dd>
    </dl>
    <div class="button-note">
        <a href="#" class="btn-wap-normal next-step" >更多问题？点击了解</a>
    </div>
</div>
</@global.main>
