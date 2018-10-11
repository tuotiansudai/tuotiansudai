<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.anxin_sign}" pageJavascript="${js.anxin_sign}" activeNav="我的账户" activeLeftNav="安心签" title="安心签">
<div class="safety-signed-frame" id="safetySignedList">
    <h2 class="column-title"><em>安心签</em></h2>
    <div class="safety-signed-list">
        <input type="hidden" data-skip-auth="${anxinProp.skipAuth?c}" class="bind-data">
        <ul class="info-list" >
            <li>
                <span class="info-title"> CFCA安心签服务</span>
                <span class="binding-set">
                    <i class="fa fa-check-circle ok"></i>已开启
                </span>
            </li>
            <li class="switch-sms">
                <span class="info-title">安心签免验服务</span>
                <span class="binding-set sms-open" style="display: none;">
                    <i class="fa fa-check-circle ok"></i>已开启
                    <a class="setlink" href="javascript:void(0);">关闭</a>
                </span>
                <span class="binding-set sms-close" style="display: none;">
                    <i class="fa fa-times-circle no"></i>已关闭
                    <a class="setlink" href="javascript:void(0);">开启</a>
                </span>
            </li>
          </ul>

        <div class="safety-advantage-box">
            <p>安心签是由中国金融认证中心（CFCA）为拓天速贷出借用户提供的一种电子缔约文件在线签署、存储和管理服务的平台功能。安心签采用的电子缔约技术完全符合我国法律及其相关法规，其形成的数据电文或电子缔约文件符合中国法律规定，与纸质文件具有同样的法律效力。</p>
            <ul class="advantage-list">
                <li>保密性</li>
                <li>不可篡改性</li>
                <li>可校验性</li>
            </ul>
            <p>安心签电子签章具有保密性、不可篡改性、可校验性，用户可自己对签章真伪进行检验，不用担心交易对手身份作假，更为全面地为用户资金保驾护航。</p>
        </div>
    </div>


    <div class="open-safety-box clearfix" id="safetyToOpen" style="display: none;">
        <span class="info">开通免短信授权服务，出借快人一步！</span>
        <span class="init-checkbox-style on">
             <input type="checkbox" id="readOk" class="default-checkbox" checked>
         </span>
        <label for="readOk" class="agreeOpen"><em>我已阅读并同意</em><a href="javascript:void(0);" class="link-agree-free-SMS">《短信免责申明》</a></label>

        <div class="button-bar">
            <button class="btn-normal ok fl" type="button">确认</button>
            <button class="btn cancel fr" type="button">取消</button>

        </div>
    </div>

    <div class="open-safety-box clearfix" id="safetyToClose" style="display: none;">
        <span class="info">关闭安心签免短信授权服务后，您每次交易均需进行短信验证码授权。</span>
        <div class="button-bar">
            <a href="javascript:void(0);" class="btn-normal fl cancel">取消</a>
            <a href="javascript:void(0);" class="btn fr ok">确认</a>
        </div>
    </div>
    <#include "../component/anxin-agreement.ftl" />
</div>
</@global.main>