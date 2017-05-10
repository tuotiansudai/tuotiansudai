<#import "wechat-global-dev.ftl" as global>
<#include "../pageLayout/header.ftl" />
<#assign jsName = 'app_marketing' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.css"}>
<@global.main pageCss="${css.app_marketing}" pageJavascript="${js.app_marketing}"  title="新手福利_拓天新手投资_拓天速贷" >

<div class="app-marketing-container" id="shareAppContainer">
    <div class="marketing-header">
        <span class="contact">客服热线：400-169-1188（服务时间：9:00－20:00）</span>
        <span class="red-bag"><i>688元</i>红包</span>
    </div>
	<div class="share-container">

        <#include '../module/register-app.ftl' />

	</div>

    <#include '../module/register-reason.ftl' />

</div>
</@global.main>