<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.dragon_invite}" pageJavascript="${js.dragon_invite}"  title="助力好友抢红包" >

<div class="wechat-invite-container" id="wechatInvite">
    <div class="take-red-container container-item active">
        <div class="take-item">
            <h3 class="title-item"><#if activityEnd>活动已结束！<#else>我送你一个投资红包，抓紧领取吧！</#if></h3>
            <div class="take-red">
                <span id="takeRed" data-time="${activityEnd?c}"></span>
            </div>
        </div>
        <dl class="intro-item">
            <dt>拓天速贷为您的资金安全保驾护航</dt>
            <dd>
                <p>
                    <span class="intro-one"></span>
                </p>
                <p class="name-item">CFCA权威认证</p>
                <p>携手中国金融认证中心<br />投资合同受法律保护</p>
            </dd>
            <dd>
                <p>
                    <span class="intro-two"></span>
                </p>
                <p class="name-item">风控严谨</p>
                <p>六重风控，22道手续<br />历史全额兑付，0逾期0坏账</p>
            </dd>
            <dd>
                <p>
                    <span class="intro-three"></span>
                </p>
                <p class="name-item">稳健安全</p>
                <p>预期年化收益8%～11%<br />房/车抵押债权安全系数高</p>
            </dd>
        </dl>
    </div>
    <div class="red-detail-container container-item">
        <h3 class="title-item">恭喜您获得10元投资红包</h3>
        <div class="take-red">
            <p>
                <span class="money-number"><strong>10</strong>元</span>
                <span class="money-type">投资红包</span>
            </p>
        </div>
        <dl class="join-item">
            <dt>登录/注册成功后投资红包会直接发放到您的账户</dt>
            <dd>
                <a href="/activity/dragon/wechat/toRegister?sharerUnique=${sharerUnique!}" class="register-link">注册领取</a>
            </dd>
            <dd>
                <a href="/activity/dragon/wechat/toLogin?sharerUnique=${sharerUnique!}" class="login-link">登录领取</a>
            </dd>
        </dl>
    </div>
</div>
</@global.main>