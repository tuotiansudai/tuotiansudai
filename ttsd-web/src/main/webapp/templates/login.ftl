<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.login}" pageJavascript="${js.login}" activeLeftNav="" title="登录拓天速贷_拓天速贷" keywords="拓天速贷,拓天会员,新手投资,拓天速贷用户" description="拓天速贷为投资投资人士提供规范、安全、专业的互联网金融信息服务,让您获得稳定收益和高收益的投资投资产品.">
<div class="login-container page-width" id="loginContainer">
        <div class="login-app"></div>
        <div class="login-box">
            <#--<div class="head_title_text">-->
                <#--<h3 id="green">欢迎登录拓天速贷</h3>-->
                <#--<a href="/register/user" class="register">免费注册</a>-->
            <#--</div>-->
                <h3 id="green"><span class="welcome_text">欢迎登录拓天速贷</span><a href="/register/user" class="register">免费注册</a></h3>
            <form class="form-login" id="formLogin"  data-redirect-url="${redirect}" autocomplete="off">
                <label>
                    <input validate class="login-name unlock" type="text" value="" name="username" placeholder="请输入账号/手机号" maxlength="25"/>
                </label>
                <label>
                    <input validate class="password unlock" type="password" value="" name="password" placeholder="请输入密码"/>
                </label>
                <label>
                    <input validate class="captcha unlock" type="text" value="" name="captcha" placeholder="请输入验证码" maxlength="5"/>
                    <em class="image-captcha"><img src="" id="imageCaptcha"/></em>
                </label>
                <input type="hidden" name="source" value="WEB" />
                <div class="bottom_tip_text">
                    <i class="error-box fa fa-times-circle"></i>
                    <div class="tr">
                        <a href="/mobile-retrieve-password">忘记密码？</a>
                    </div>
                </div>
                <div class="forgot-password tc">
                    <div class="clear-blank"></div>
                    <button class="login-submit btn-normal" type="submit" >立即登录</button>
                </div>
                <div class="clear-blank"></div>
                <b>数据采用256位加密技术，保障您的信息安全！</b>
            </form>
        </div>


</div>
</@global.main>