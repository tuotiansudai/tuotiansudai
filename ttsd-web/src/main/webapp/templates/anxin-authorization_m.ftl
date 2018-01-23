<#import "macro/global_m.ftl" as global>

<#assign jsName = 'anxin_authorization' >
<#assign cssName = 'anxin_electro_sign' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${cssName}":"http://localhost:3008/wapSite/js/account/${cssName}.css"}>

<@global.main pageCss="${css.anxin_electro_sign}" pageJavascript="${js.anxin_authorization}" title="安心签授权中">


<div class="my-account-content anxin-electro-sign" id="anxinAuthorization">
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

    <div class="identifying-code">
        <input type="text" maxlength="6" class="skip-phone-code" id="skipPhoneCode" placeholder="请输入验证码">
        <span class="button-identify">
             <button type="button" class="get-skip-code" id="getSkipCode" data-voice="false">获取验证码</button>

             <i class="microphone" id="microPhone" data-voice="true"></i>
        </span>

    </div>

    <button type="button" class="btn-wap-normal next-step" id="toOpenSMS" disabled>立即授权</button>

    <div class="agreement-box">
        <span class="init-checkbox-style on">
             <input type="checkbox" id="readOk" class="default-checkbox" checked>
         </span>

        <lable for="agreement">我已阅读并同意<a href="javascript:void(0)" class="link-agree-free-SMS">《短信免责声明》</a></lable>

    </div>
</div>
    <#--<#include '../module/anxin-agreement.ftl'>-->
</@global.main>
