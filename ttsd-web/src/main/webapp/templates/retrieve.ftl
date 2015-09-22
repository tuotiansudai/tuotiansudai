<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="忘记密码" pageCss="${css.forget_password}">
</@global.head>
<body>
<#include "header.ftl" />
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
                    <button type="button" class="send-yzm fetch-captcha grey" disabled="disabled">获取验证码</button>
                </div>
            </div>
            <div class="item-block">
                <div class="error"></div>
            </div>
            <div class="item-block">
                <button type="button" class="btn-send-form grey" disabled="disabled">提交</button>
            </div>
        </form>

    </div>
    <p class="txtc tips">找回密码过程中如有问题，请致电拓天速贷客服：400-169-1188 （工作日 9:00-22:00）</p>
</div>


<div class="layer-box">
    <div class="verification-code"></div>
    <div class="verification-code-main">
        <span>手机验证<i class="close">X</i></span>

        <p>
            <input type="text" class="verification-code-text" maxlength="5" placeholder="请输入图形验证码"/>
            <img src="/mobile-retrieve-password/image-captcha" alt="" class="verification-code-img"/>
        </p>
        <b>验证码输入错误</b>
        <button class="complete grey" disabled="disabled">完成</button>
    </div>
</div>





<#include "footer.ftl">
<@global.javascript pageJavascript="${js.forget_password}">
</@global.javascript>
</body>
</html>
<script>
    var API_PHONE = '../js/fast-pay.json';
</script>