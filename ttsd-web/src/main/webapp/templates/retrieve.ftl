<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.register}" pageJavascript="${js.forget_password}" activeNav="" activeLeftNav="" title="找回密码_拓天会员_拓天速贷" keywords="拓天速贷,拓天会员,新手理财,拓天速贷用户" description="拓天速贷会员密码找回提供安全、便捷有保障的全球资产配置服务.">

    <div class="forget-phone-box tc" id="retrievePasswordBox">
        <form class="retrieve-form" action="">
       <ul class="retrieve-box">
           <li class="re-title">通过认证手机找回密码</li>
           <li>
               <label for="">手机号：</label>
               <input class="phone-txt" name="mobile" type="text" maxlength="11" value="${mobile}" placeholder="请输入手机号"/>
           </li>
           <li class="get-captcha">
               <label for="">验证码：</label>
                   <input type="text" name="captcha" class="yzm-txt" minlength="6" maxlength="6" placeholder="请输入验证码"/>
                   <button type="button" class="fetch-captcha btn-normal" disabled="disabled">获取验证码</button>
           </li>
           <li class="clear-blank-m">

               <input type="submit" class="btn-send-form btn-success"  value="提交"/>

           </li>
       </ul>
        </form>
        <div class="pad-m tips_message">找回密码过程中如有问题，请致电拓天速贷客服：400-169-1188 （服务时间：9:00－20:00）</div>
    </div>

    <div class="verification-code-main" style="display: none;">
        <div class="pad-m">
            <div class="image-code-check">
                <label for="">图形验证码：</label>
                <input type="text" class="verification-code-text input-control" maxlength="5" placeholder="请输入图形验证码"/>
                <img src="/mobile-retrieve-password/image-captcha" alt="" class="verification-code-img"/>

            </div>
            <b class="error">验证码输入错误</b>
            <div class="tc pad-m">
                <button class="image-captcha-confirm btn-success" >确认</button>
            </div>
        </div>
    </div>

</@global.main>