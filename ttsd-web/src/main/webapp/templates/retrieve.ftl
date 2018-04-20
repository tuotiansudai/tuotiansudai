<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.forget_password}" pageJavascript="${js.forget_password}" activeNav="" activeLeftNav="" title="找回密码_拓天会员_拓天速贷" keywords="拓天速贷,拓天会员,新手投资,拓天速贷用户" description="拓天速贷会员密码找回提供安全、便捷有保障的全球资产配置服务.">

    <div class="forget-phone-box tc" id="retrievePasswordBox">
        <form class="retrieve-form" id="retrieveForm">
       <ul class="retrieve-box">
           <li class="re-title">通过认证手机找回密码</li>
           <li>
               <label for="" class="title">手机号：</label>
               <input class="phone-txt" name="mobile" type="text" maxlength="11" value="${mobile!''}"
                      placeholder="请输入手机号"/>
           </li>
           <li class="get-captcha">
               <label for="" class="title">验证码：</label>
               <button type="button" class="fetch-captcha btn-normal" disabled="disabled" id="fetchCaptcha">获取验证码</button>
               <input type="text" name="captcha" class="yzm-txt" minlength="6" maxlength="6" placeholder="请输入验证码"/>
               <span class="voice-captcha" id="voice_captcha" style="display: none;">如收不到短信，可使用 <a href="javascript:;" id="voice_btn">语音验证</a> </span>

           </li>
           <div class="error-box"></div>
           <li class="clear-blank-m tc">
               <input type="submit" class="btn-send-form btn-success"  value="提交"/>
           </li>
       </ul>
        </form>
        <div class="pad-m tips_message">找回密码过程中如有问题，请致电拓天速贷客服：<span>400-169-1188 （服务时间：9:00－20:00）</span></div>
    </div>


<div class="image-captcha-dialog" style="display: none;">
    <form class="image-captcha-form" id="imageCaptchaForm" >
        <div class="image-captcha-inner">
            <img src="" alt="" class="image-captcha"/>
            <input type="text" class="image-captcha-text" name="imageCaptcha" maxlength="5" placeholder="请输入图形验证码"/>
            <input type="hidden" name="mobile">

            <div class="tc">
                <div class="error-box tl"></div>
                <input type="submit" class="image-captcha-confirm btn-normal" value="确定"/>
            </div>

        </div>

    </form>
</div>

</@global.main>