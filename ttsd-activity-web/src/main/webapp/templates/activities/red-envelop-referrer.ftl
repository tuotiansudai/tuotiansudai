<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.red_envelop_split}" pageJavascript="${js.red_envelop_split}" activeNav="" activeLeftNav="" title="邀请好友送红包" keywords="拓天速贷,新手投资,新手加息券,新手红包" description="一次体验，受益终生，拓天速贷一周年，这是用心的回馈">
    <div class="red-envelop-container" id="redEnvelopReferrer">

        <div class="register-section-box">
            <div class="invite-box">
                <#if registerStatus=='referrer'>
                <em class="message-tip">您的好友${userName}送您</em>
                </#if>
                <#if registerStatus=='before'>
                    <em class="message-tip">恭喜您！您已获得</em>
                </#if>
                <span class="amount"><i>8.88</i>元</span>
                <span class="kind">投资红包</span>
            </div>

    <#if registerStatus=='referrer'>
            <div class="flow-box step-one">
                <form id="phoneForm" name="phoneForm" method="post" action="/activity/red-envelop-split/before-register">
                    <input type="text" name="mobile" placeholder="请输入您的手机号">
                    <input type="hidden" name="loginName" value="">
                    <input type="hidden" name="channel" value="">
                    <button type="submit" class="normal-button">立即领取8.88元红包</button>
                </form>
            </div>
    </#if>
    <#if registerStatus=='before'>
            <div class="flow-box step-two">
                <span class="reward-msg">
                    <em>将红包存入您的账户，开始提现吧！</em>
                    点击“获取验证码”，将会给<i class="phone">${mobile}</i>发送短信验证码
                </span>
                <form id="registerForm" name="registerForm" >
                    <span class="get-captcha">
                         <input type="text" name="captcha" placeholder="请输入您的验证码">
                        <button type="button" class="btn-captcha">获取验证码</button>
                    </span>

                    <input type="text" name="password" placeholder="请设置您的登录密码(6-20位数字字母组合)">
                    <input type="hidden" name="mobile" value="${mobile}">
                    <input type="hidden" name="channel" value="${channels}">
                    <input type="hidden" name="referrer" value="${loginName}">
                    <button type="submit" class="normal-button">立即将红包存入账户</button>
                    <span class="agreement">领取即同意拓天速贷《服务协议》</span>
                </form>
            </div>
    </#if>

            <div class="flow-box step-three" style="display: none">
                <span class="reward-msg">
                    <em>恭喜您！您已将红包存入账户。</em>
                    <em>立即使用红包提现吧！</em>
                </span>
                    <button type="button" class="normal-button" id="downloadApp">立即下载APP提现</button>
            </div>
        </div>

        <div class="title-benefit">
            <img src="${commonStaticServer}/activity/images/red_envelop_split/title.png">
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

        <#include "../module/register-agreement.ftl"/>
    </div>

</@global.main>