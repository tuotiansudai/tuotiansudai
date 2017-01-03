<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.red_envelop_split}" pageJavascript="${js.red_envelop_split}" activeNav="" activeLeftNav="" title="邀请好友送红包" keywords="拓天速贷,新手投资,新手加息券,新手红包" description="一次体验，受益终生，拓天速贷一周年，这是用心的回馈">
    <div class="red-envelop-container">

        <div class="register-section-box">
            <div class="invite-box">
                <em class="message-tip">您的好友王强送你</em>
                <span class="amount"><i>8.88</i>元</span>
                <span class="kind">现金红包</span>
            </div>

            <div class="flow-box step-one">
                <form id="phoneForm">
                    <input type="text" name="phone" placeholder="请输入您的手机号">
                    <button type="button" class="normal-button">立即领取8.88元红包</button>
                </form>
            </div>

            <div class="flow-box step-two">
                <span class="reward-msg">
                    <em>将红包存入您的账户，开始提现吧！</em>
                    点击“获取验证码”，将会给<i class="phone">13026387149</i>发送短信验证码
                </span>
                <form id="captchaForm">
                    <span class="get-captcha">
                         <input type="text" name="captcha" placeholder="请输入您的验证码">
                        <button type="button" class="btn-captcha">获取验证码</button>
                    </span>

                    <input type="text" name="phone" placeholder="请设置您的登录密码(6-20位数字字母组合)">
                    <button type="button" class="normal-button">立即领取8.88元红包</button>
                    <span class="agreement">领取即同意拓天速贷《服务协议》</span>
                </form>
            </div>

            <div class="flow-box step-three">
                <span class="reward-msg">
                    <em>恭喜您！您已将红包存入账户。</em>
                    <em>立即使用红包提现吧！</em>
                </span>
                    <button type="button" class="normal-button">立即下载APP提现</button>
            </div>

        </div>

        <div class="benefit-box">
            <dl class="benefit-column">
                <dt>预期收益高</dt>
                <dd>预期年化收益<i>10%-13%</i></dd>
            </dl>
            <dl class="benefit-column">
                <dt>交易模式安全</dt>
                <dd>中国金融认证中心（CFCA）<i>权威认证</i></dd>
            </dl>
            <dl class="benefit-column">
                <dt>资金安全</dt>
                <dd><i>联动优势</i>第三方支付服务</dd>
            </dl>
            <dl class="benefit-column">
                <dt>投资门槛低</dt>
                <dd><i>50</i>元起投</dd>
            </dl>
        </div>
    </div>

</@global.main>