<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'scan_code' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.scan_code}" pageJavascript="${js.scan_code}" title="扫描二维码">


<div class="my-account-content" id="scanCode">
    <div class="code-item">
        <p>
            <span></span>
        </p>
        <p>扫描二维码，一起赚钱吧</p>
    </div>
    <dl class="info-item">
        <dt>扫码说明:</dt>
        <dd>1、好友扫描二维码成功，打开注册链接；</dd>
        <dd>2、好友按照提示完成注册并成功投资，您就可以获得奖励啦！</dd>
    </dl>
</div>
</@global.main>
