<#import "../macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.anxin_sign}" pageJavascript="${m_js.anxin_sign}" title="安心签电子签章服务已开启">
<div class="bind-data" style="display: none" data-is-anxin-user="${anxinProp.anxinUser?c}"></div>
<div id="signature" style="display: none">
    <div class="goBack_wrapper">
        安心签电子签章服务
        <div class="go-back-container" id="goPage_1">
            <span class="go-back"></span>
        </div>
    </div>
    <div class="my-account-content anxin-electro-sign" id="anxinElectroSign">
        <div class="cfca-logo"></div>
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
        <@global.role hasRole="'INVESTOR'">
            <button type="button" class="btn-wap-normal next-step" id="openSafetySigned">立即开启</button>
        </@global.role>
        <button type="button" class="btn-wap-normal next-step" id="authentication_identity"
                style="<@global.role hasRole="'INVESTOR'">display: none</@global.role>">立即开启
        </button>
        <div class="agreement-box">
        <span class="init-checkbox-style on">
             <input type="checkbox" id="readOk" class="default-checkbox" checked>
         </span>
            <lable for="agreement">我已阅读并同意<a href="javascript:void(0)" class="link-agree-service">《安心签服务协议》</a>、<a
                    href="javascript:void(0)" class="link-agree-privacy">《隐私条款》</a>、<br/><a href="javascript:void(0)"
                                                                                            class="link-agree-number">
                《CFCA数字证书服务协议》</a>和<a href="javascript:void(0)" class="link-agree-number-authorize"> 《CFCA数字证书授权协议》</a>
            </lable>
        </div>
    </div>

</div>

<div id="openMessage" style="display: none">
    <div class="goBack_wrapper">
        安心签电子签章服务
        <div class="go-back-container" id="goPage_2">
            <span class="go-back"></span>
        </div>
    </div>
    <div id="shortMessage">
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
    </div>
</div>

<div id="authorization_message" style="display: none">
    <div class="goBack_wrapper">
        安心签代签署授权
        <div class="go-back-container" id="goPage_3">
            <span class="go-back"></span>
        </div>
    </div>
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
            <input type="text" maxlength="6" class="skip-phone-code" id="skipPhoneCode" placeholder="请输入验证码"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
            <div class="close_btn"></div>
            <span class="button-identify">
                 <button type="button" class="get-skip-code" id="getSkipCode" data-voice="false">获取验证码</button>
                 <i class="microphone" id="microPhone" data-voice="true"></i>
            </span>
            <div class="countDownTime" style="display: none">(<span class="seconds"></span>)秒后重新获取</div>
        </div>
        <div class="error" style="display: none">验证码不正确</div>
        <button type="button" class="btn-wap-normal next-step" id="toOpenSMS" disabled>立即授权</button>
        <div class="agreement-box">
            <span class="init-checkbox-style on">
                 <input type="checkbox" id="readOk1" class="default-checkbox" checked>
             </span>
            <lable for="agreement">我已阅读并同意<a href="javascript:void(0)" class="link-agree-free-SMS">《短信免责声明》</a></lable>
        </div>
    </div>
</div>
    <#include '../component/anxin-agreement.ftl'>
</@global.main>