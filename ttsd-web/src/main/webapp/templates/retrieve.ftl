<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.forget_password}" pageJavascript="${js.forget_password}" activeNav="" activeLeftNav="" title="忘记密码">
<div class="main">
    <div class="forget-phone-box">
        <div class="hd">
            通过认证手机找回密码
        </div>
        <form class="retrieve-form" action="">
            <div class="item-block">
                <label for="">手机号：</label>
                <input class="phone-txt" name="mobile" type="text" maxlength="11" value="${mobile}" placeholder="请输入手机号"/>
            </div>
            <div class="item-block">
                <label for="">验证码：</label>

                <div class="yzm">
                    <input type="text" name="captcha" class="yzm-txt" placeholder="请输入验证码"/>
                    <button type="button" class="send-yzm fetch-captcha" disabled="disabled">获取验证码</button>
                </div>
            </div>
            <div class="item-block">
                <div class="error"></div>
            </div>
            <div class="item-block">
                <button type="button" class="btn-send-form" disabled="disabled">提交</button>
            </div>
        </form>

    </div>
    <p class="txtc tips tips_message">找回密码过程中如有问题，请致电拓天速贷客服：400-169-1188 （工作日 9:00-22:00）</p>
</div>


<div class="layer-box">

    <div class="verification-code-main">
        <div>
            <label for="">图形验证码：</label>
            <input type="text" class="verification-code-text" maxlength="5" placeholder="请输入图形验证码"/>
            <img src="/mobile-retrieve-password/image-captcha" alt="" class="verification-code-img"/>

        </div>
        <b>验证码输入错误</b>
        <div class="btn_complete">
            <button class="complete " >确认</button>
        </div>
    </div>

</div>
</@global.main>