<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'account_overview' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/${cssName}.css"}>

<@global.main pageCss="${css.account_overview}" pageJavascript="${js.account_overview}" title="登录账户">

<div class="my-account-content account-overview">
    <div class="account-summary">
        <div class="collection">
            <span class="summary-box ">
                <b>8,000,000.00</b>
                <em>待收回款（元）</em>
            </span>
        </div>
        <div class="amount-balance">
            <span class="summary-box ">
                <b>8,000,000.00</b>
                <em>待收回款（元）</em>
            </span>
            <span class="summary-box ">
                <b>8,000,000.00</b>
                <em>待收回款（元）</em>
            </span>
        </div>


    </div>
</div>
</@global.main>
