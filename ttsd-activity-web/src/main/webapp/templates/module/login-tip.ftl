<div class="login-tip" id="loginTip" style="display: none">
    <i class="close-btn"></i>
    <div class="login-box">
        <h3>欢迎登录拓天速贷</h3>

        <form class="form-login" action="/login" method="post" id="loginInForm" name="loginInForm" autocomplete="off">
            <div class="clearfix">
                <label>
                    <em class="name user"></em>
                    <input class="login-name unlock" type="text" value="" name="username" placeholder="请输入账号/手机号" maxlength="25"/>
                </label>
                <label>
                    <em class="name pass"></em>
                    <input class="password unlock" type="password" value="" name="password" placeholder="请输入密码"/>
                </label>
                <label>
                    <em class="name capt"></em>
                    <em class="image-captcha">
                        <img src="/login/captcha" id="imageCaptcha" alt="" width="138" height="40"/>
                    </em>
                    <input class="captcha unlock" type="text" value="" name="captcha" placeholder="请输入验证码" maxlength="5"/>

                </label>
            </div>

            <input type="hidden" name="source" value="WEB"/>
            <div class="error-box"></div>
            <div class="forgot-password tc clearfix">
                <input type="submit" class="login-submit btn-normal" value="登录">
                <div class="fl">
                    <a href="/mobile-retrieve-password">忘记密码？</a>
                </div>
                <div class="fr">
                    还没有账号？<a href="/register/user" class="register">免费注册>></a>
                </div>
                </div>
        </form>
    </div>
    <div class="code-item">
        <p class="code-img"></p>

        <p>扫描二维码</p>

        <p>下载拓天速贷APP 投资快人一步</p>
    </div>
    </div>

