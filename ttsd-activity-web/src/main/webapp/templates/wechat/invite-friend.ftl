<#--<#import "wechat-global.ftl" as global>-->
<#import "wechat-global-dev.ftl" as global>

<#assign jsName = 'midsummer_wap' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.css"}>


<@global.main pageCss="${css.midsummer_wap}" pageJavascript="${js.midsummer_wap}"  title="助力好友抢红包" >

<div class="invite-box-friend" id="inviteBoxFriend">
    <div class="invite-header" id="inviteHeader"></div>

</div>
</@global.main>