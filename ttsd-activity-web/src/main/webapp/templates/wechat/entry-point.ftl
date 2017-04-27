<#--<#import "wechat-global.ftl" as global>-->
<#import "wechat-global-dev.ftl" as global>
<#assign jsName = 'midsummer_wap' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.css"}>

<@global.main pageCss="${css.midsummer_wap}" pageJavascript="${js.midsummer_wap}"  title="助力好友抢红包" >

<div class="entry-point-container" id="entryPointCon">
    <div class="red-key-box">
        <em>50</em>元
        <span>红包</span>
    </div>

    <div class="button-layer">
        <a href="#" class="btn-normal bind-card">绑定拓天账号立即领取</a>

        <span class="note">活动解释权归拓天速贷所有</span>
    </div>

</div>
</@global.main>