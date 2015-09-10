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
        <form action="/mobile-retrieve-password" method="post">
            <div class="item-block">
                <label for="">新的密码：</label>
                <input type="hidden" name="mobile" value="${mobile}">
                <input type="hidden" name="captcha" value="${captcha}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input class="phone-txt jq-ps-1" name="password" type="password" placeholder="请输入密码"/>
            </div>
            <div class="item-block">
                <label for="">确认密码：</label>
                <input class="phone-txt jq-ps-2" type="password" placeholder="再次输入密码"/>
            </div>
            <div class="item-block">
                <button type="submit" class="btn-send-form grey" disabled="disabled">确认</button>
            </div>
        </form>

    </div>
    <p class="txtc tips">找回密码过程中如有问题，请致电拓天速贷客服：400-169-1188 （工作日 9:00-22:00）</p>
</div>






<#include "footer.ftl">
<@global.javascript pageJavascript="${js.input_password}">
</@global.javascript>
</body>
</html>