<#import "wechat-global-dev.ftl" as global>

<#assign jsName = 'wechat_invite' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.css"}>


<@global.main pageCss="${css.wechat_invite}" pageJavascript="${js.wechat_invite}"  title="助力好友抢红包" >

<div class="wechat-invite-container" id="wechatInvite">
    <div class="take-red-container">
        <div class="take-item">
            <h3 class="title-item">我送你一个现金红包，抓紧领取吧！</h3>
            <div class="take-red">
                <span></span>
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
    <div class="red-detail-container">
        <h3 class="title-item">恭喜您获得10元现金红包</h3>
        <div class="take-red">
            <p>
                <span class="money-number"><strong>10</strong>元</span>
                <span class="money-type">现金红包</span>
            </p>
        </div>
        <dl class="join-item">
            <dt>登录/注册成功后现金红包会直接发放倒您的账户</dt>
            <dd>
                <span class="register-link">注册领取</span>
            </dd>
            <dd>
                <span class="login-link">登录领取</span>
            </dd>
        </dl>
    </div>
    <div class="register-form-container">
        <h3 class="title-item">注册完成后现金红包会直接放到您的账户中哦~</h3>
        <form action="#" method="post" class="register-item">
            <div class="model-item">
                <input type="text" name="name" id="name" value="" tabindex="1"  class="int-item" placeholder="请输入您的手机号" />
            </div>

            <div class="model-item">
                <input type="password" name="name" id="name" value="" tabindex="2"  class="int-item" placeholder="请输入您的密码" />
            </div>

            <div class="model-item">
                <input type="text" name="name" id="name" value="" tabindex="3"  class="int-item" placeholder="请输入短信验证码" />
            </div>
            
            <div class="model-item text-model">
                <span class="fr login-link">登录</span>
            </div>
            <div class="model-item text-model">
                <i class="icon-check active"></i>
                <span class="agree-item">同意拓天速贷<strong class="agree-text">《服务协议》</strong></span>
                <input type="hidden" name="checkbox" id="checkbox" />
            </div>

            <div class="model-item text-model tc">
                <input type="submit" value="注册领取" class="register-btn"/>
            </div>
        </form>
    </div>
    <div class="login-form-container">
        <h3 class="title-item">登录完成后现金红包会直接放到您的账户中哦~</h3>
        <form action="#" method="post" class="login-item">
            <div class="model-item">
                <input type="text" name="name" id="name" value="" tabindex="1"  class="int-item"/>
            </div>

            <div class="model-item">
                <input type="password" name="name" id="name" value="" tabindex="2"  class="int-item"/>
            </div>

            <div class="model-item">
                <input type="text" name="name" id="name" value="" tabindex="3"  class="int-item"/>
            </div>
            
            <div class="model-item text-model">
                <span class="fr login-link">注册</span>
            </div>
            <div class="model-item text-model">
                <i class="icon-check"></i>
                <span class="agree-item">同意拓天速贷<strong class="agree-text">《服务协议》</strong></span>
                <input type="hidden" name="checkbox" id="checkbox" />
            </div>

            <div class="model-item text-model tc">
                <input type="submit" value="注册领取" class="login-btn"/>
            </div>
        </form>
    </div>
    <div class="result-container">
        <h3 class="title-item">领取成功！</h3>
        <div class="take-red">
            <p>
                <span class="money-number"><strong>10</strong>元</span>
                <span class="money-type">现金红包</span>
            </p>
        </div>
    </div>
</div>
<#include '../module/register-agreement.ftl' />
</@global.main>