<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.register}" pageJavascript="${js.input_password}" activeNav="" activeLeftNav="" title="忘记密码">

    <div class="forget-phone-box tc" id="inputGetPassword">

        <form action="/mobile-retrieve-password" method="post">
            <ul class="retrieve-box input-password-box">
                <li class="re-title">通过认证手机找回密码</li>
                <li>
                    <label for="">新的密码：</label>
                    <input type="hidden" name="mobile" value="${mobile}">
                    <input type="hidden" name="captcha" value="${captcha}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input name="password" type="password" id="newPassword" maxlength="20" placeholder="请输入密码"/>

                </li>
                <li>
                    <label for="">确认密码：</label>
                    <input name="repeatPassword" type="password" maxlength="20" placeholder="再次输入密码"/>
                </li>

                <li class="clear-blank-m">
                    <input type="submit" class="btn-send-form-second btn-success" value="确认" >
                </li>
                </ul>

        </form>
        <div class="pad-m tips_message">找回密码过程中如有问题，请致电拓天速贷客服：400-169-1188 （服务时间：9:00－20:00）</div>
    </div>

</@global.main>