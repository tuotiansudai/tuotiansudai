<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'anxin_electro_success' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.anxin_electro_success}" pageJavascript="${js.anxin_electro_success}" title="安心签电子签章服务已开启">


<div class="my-account-content anxin-electro-success" id="anxinElectroSign">

    <div class="success-section">
        <div class="electro-logo"></div>

        <h2>安心签电子签章服务已开启</h2>
    </div>


    <div class="open-SMS-free">
        <span>开通免短信授权服务，投资快人一步！</span>
        <button type="button" class="btn-wap-normal next-step" id="openAuthorization">立即开启</button>
    </div>

</div>

</@global.main>
