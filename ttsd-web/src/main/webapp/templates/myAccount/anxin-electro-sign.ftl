<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.anxin_sign}" pageJavascript="${js.anxin_sign}" activeNav="我的账户" activeLeftNav="安心签" title="安心签">
<div class="safety-signed-frame" id="safetySignedFrame">
    <h2 class="column-title"><em>安心签</em></h2>
    <div class="sign-img"></div>
    <input type="hidden" class="bind-data" data-is-anxin-user="${anxinProp.anxinUser?c}">

     <div class="safety-status-box closed tc">
        <span class="status-text">安心签电子签章服务</span>
        <@global.role hasRole="'INVESTOR'">
            <button class="btn-normal btn-open" id="openSafetySigned">立即开启</button>
        </@global.role>
         <a href="/register/account" class="btn-normal btn-open" style="width: 70px; <@global.role hasRole="'INVESTOR'">display: none</@global.role>">立即开启</a>
         <span class="init-checkbox-style on">
             <input type="checkbox" id="agreeOpen1" class="btn-open default-checkbox" checked>
         </span>
             <label for="agreeOpen1" class="agreeOpen"><em>我已阅读并同意</em>
                 <a href="javascript:void(0);" class="anxin_layer link-agree-service" >《安心签平台服务协议》</a>、
                 <a href="javascript:void(0);" class="anxin_layer link-agree-privacy" >《隐私条款》</a>、
                 <a href="javascript:void(0);" class="anxin_layer link-agree-number" >《CFCA数字证书服务协议》</a>
                 和
                 <a href="javascript:void(0);" class="anxin_layer link-agree-number-authorize" >《CFCA数字证书授权协议》</a>
             </label>
    </div>

      <div class="safety-status-box opened tc">
            <span class="status-text"><i></i>安心签电子签章服务已开启</span>
            <span class="to-open-check">开通免短信授权服务，投资快人一步！</span>
            <button class="btn-normal btn-open" id="openAuthorization">立即授权开通</button>
          <span class="init-checkbox-style on">
              <input type="checkbox" id="agreeOpen2" class="default-checkbox" checked>
            </span>
            <label for="agreeOpen2" class="agreeOpen"><em>我已阅读并同意</em><a href="#" class="anxin_layer link-agree-free-SMS">《短信免责申明》</a> </label>
      </div>

    <div class="safety-advantage-box">
        <p>安心签是由中国金融认证中心（CFCA）为拓天速贷投资用户提供的一种电子缔约文件在线签署、存储和管理服务的平台功能。安心签采用的电子缔约技术完全符合我国法律及其相关法规，其形成的数据电文或电子缔约文件符合中国法律规定，与纸质文件具有同样的法律效力。</p>
    <ul class="advantage-list">
        <li>保密性</li>
        <li>不可篡改性</li>
        <li>可校验性</li>
    </ul>
        <p>安心签电子签章具有保密性、不可篡改性、可校验性，用户可自己对签章真伪进行检验，不用担心交易对手身份作假，更为全面地为用户资金保驾护航。</p>
    </div>

    <div class="open-safety-box clearfix" id="getSkipPhone" style="display: none">
        <div class="tip-item tc">
            <span>
                <input type="text" maxlength="6" class="skip-phone-code" id="skipPhoneCode" placeholder="请输入验证码">
                <input type="hidden" id="tipCheck" value="true">
            </span>
            <span>
                <input type="button" class="get-skip-code" value="获取短信验证码" id="getSkipCode">
                <input type="button" class="microphone" id="microPhone" >
            </span>
            <div class="error" style="display: none">验证码不正确</div>
            <div class="button-bar"><button class="btn-normal one" id="toOpenSMS">立即开通</button></div>
        </div>

    </div>
    <#include "../component/anxin-agreement.ftl" />
 </div>
</@global.main>