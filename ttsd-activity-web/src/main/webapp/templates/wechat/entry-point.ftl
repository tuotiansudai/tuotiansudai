<#--<#import "wechat-global.ftl" as global>-->
<#import "wechat-global-dev.ftl" as global>

<#assign jsName = 'midsummer_wap' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.css"}>

<@global.main pageCss="${css.midsummer_wap}" pageJavascript="${js.midsummer_wap}"  title="活动中心_投资活动_拓天速贷" >

<div class="entry-point-container">

</div>
</@global.main>