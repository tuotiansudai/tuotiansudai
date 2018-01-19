<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'certification' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.certification}" pageJavascript="" title="实名认证">

<div class="my-account-content certification-success" id="certificationBox">

        <i class="icon-success"></i>
        <em>实名认证成功</em>
        <a href="#" class="btn-wap-normal next-step" >确定</a>


</div>
</@global.main>
