<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.login}" pageJavascript="${js.login}" activeLeftNav="" title="拓天速贷-登录" >
<div class="login-container page-width">

        <img src="/images/sign/login-app.jpg" class="login-app">
        <div class="login-box">
            <h3>欢迎登录拓天速贷</h3>

            <form class="form-login" action="/login-handler" method="post" data-redirect-url="${redirect}">
                <label>
                    <em class="name">账<i></i>号:</em>
                    <input class="login-name unlock" type="text" value="" name="username" placeholder="请输入账号/手机号"/>
                </label>
                <label>
                    <em class="name">密<i></i>码:</em>
                    <input class="password unlock" type="password" value="" name="password" placeholder="请输入密码"/>
                </label>
                <label>
                    <em class="name">验证码:</em>
                    <input class="captcha unlock" type="text" value="" name="captcha" placeholder="请输入验证码" maxlength="5"/>
                    <em class="image-captcha">
                        <img src="/login/captcha" alt=""/>
                    </em>
                </label>
                <i class="error fa fa-times-circle"></i>
                <div class="forgot-password tc">
                    <div class="tr">
                        <a href="/register/user" class="register">免费注册</a>
                        <a href="/mobile-retrieve-password">忘记密码？</a>
                    </div>
                    <div class="clear-blank"></div>
                    <button class="login-submit btn-normal" type="button" >立即登录</button>
                </div>
                <div class="clear-blank"></div>
                <b>数据采用256位加密技术，保障您的信息安全！</b>
            </form>
        </div>


</div>
</@global.main>