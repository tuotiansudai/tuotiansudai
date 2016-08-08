<#import "macro/global.ftl" as global>
<div class="profile-box">
    <i class="profile"></i>
    <ul class="welcome-info">
        <@global.isNotAnonymous>
        <li class="username"><@global.security.authentication property="principal.mobile"/>，您好</li>
        <li>提问有新回答 <br/>
            <a href="#"> 点击查看</a>
        </li>
        </@global.isNotAnonymous>

        <@global.isAnonymous>
            <li class="username">游客</li>
        </@global.isAnonymous>
    </ul>
    <div class="button-layer">
    <@global.isNotAnonymous>
        <a href="#" class="btn">我的提问<em>(4)</em></a>
        <a href="#" class="btn">我的回答<em>(8)</em></a>
    </@global.isNotAnonymous>
    <@global.isAnonymous>
        <a href="${webServer}/login" class="btn">登录</a>
        <a href="${webServer}/register/user" class="btn">注册</a>
    </@global.isAnonymous>
    </div>
    <div class="vertical-line"></div>
</div>