<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'anxin_electro_sign' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.anxin_electro_sign}" pageJavascript="${js.anxin_electro_sign}" title="安心签电子签章服务">


<div class="my-account-content anxin-electro-sign" id="anxinElectroSign">
    <div class="cfca-logo">

    </div>
    <div class="cfca-info">
        安心签是由中国金融认证中心（CFCA）为拓天速贷投资用户提供的一种电子缔约文件在线签署、存储和管理服务的平台功能。它形成的电子缔约文件符合中国法律规定，与纸质文件具有同样的法律效力。
    </div>

    <div class="cfca-advantage">
        <span>
            <i></i>
            保密性
        </span>
        <span>
            <i></i>
            不可篡改性
        </span>
        <span>
            <i></i>
            可校验性
        </span>
    </div>

    <button type="button" class="btn-wap-normal next-step" id="openSafetySigned">立即开启</button>

    <div class="agreement-box">
        <span class="init-checkbox-style on">
             <input type="checkbox" id="readOk" class="default-checkbox" checked>
         </span>

        <lable for="agreement">我已阅读并同意<a href="javascript:void(0)" class="link-agree-service">《安心签服务协议》</a>、<a href="javascript:void(0)" class="link-agree-privacy">《隐私条款》</a> 和<a href="javascript:void(0)" class="link-agree-number"> 《CFCA数字证书服务协议》</a></lable>

    </div>
</div>

<#include '../module/anxin-agreement.ftl'>
</@global.main>
