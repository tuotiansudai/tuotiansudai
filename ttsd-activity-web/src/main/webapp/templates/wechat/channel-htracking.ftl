<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.app_marketing}" pageJavascript="${js.app_marketing}"  title="新手福利_拓天新手投资_拓天速贷" >

<div class="app-marketing-container" id="shareAppContainer">
    <div class="marketing-header">
        <span class="contact">客服热线：400-169-1188（服务时间：9:00－20:00）</span>
        <span class="red-bag"><i>668元</i>红包</span>
    </div>
	<div class="share-container">
        <#include '../module/register-app.ftl' />
	</div>

    <dl class="novice-spree">
        <dt></dt>
        <dd></dd>
    </dl>

    <#include '../module/register-reason.ftl' />

    <#include "../pageLayout/header.ftl" />

</div>
</@global.main>