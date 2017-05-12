<#import "wechat-global-dev.ftl" as global>

<#assign jsName = 'dragon_success' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.css"}>


<@global.main pageCss="${css.dragon_success}" pageJavascript="${js.dragon_success}"  title="助力好友抢红包" >

<div class="wechat-invite-container" id="wechatInvite">
    <div class="result-container">
        <h3 class="title-item">领取成功！</h3>
        <div class="take-red">
            <p>
                <span class="money-number"><strong>10</strong>元</span>
                <span class="money-type">现金红包</span>
            </p>
        </div>
    </div>
</div>
<#include '../module/register-agreement.ftl' />
</@global.main>