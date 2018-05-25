<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.register_account}" pageJavascript="${m_js.register_account}" title="实名认证">

<div class="my-account-content certification-box" id="certificationBox">

    <div class="gif-container">
        <img id="gifImg" src="" alt="">
    </div>
    <div class="progress-container">
        正在处理中：<span id="secondsCountDown">5</span>
    </div>
    <#--<div class="m-header"><em id="iconRegister" class="icon-left"><i></i></em>实名认证</div>-->
    <#--<div class="info-container">-->
        <#--<div class="status-container">-->
            <#--<div class="icon-status icon-success"></div>-->
            <#--<div class="icon-status icon-failure"></div>-->
            <#--<p class="desc">银行卡绑定成功</p>-->
                <#--<p class="desc">银行卡绑定失败</p>-->
                <#--<p class="reason-error">身份证信息和真实姓名不匹配</p>-->
        <#--</div>-->
        <#--&lt;#&ndash;绑定银行卡&ndash;&gt;-->
        <#--<ul class="other-info-list">-->
            <#--<li class="clearfix"><span class="fl title">银行卡号</span><span class="fr content">  6212 **** 4017</span></li>-->
        <#--</ul>-->
        <#--&lt;#&ndash;申请提现&ndash;&gt;-->
        <#--<ul class="other-info-list">-->
            <#--<li class="clearfix"><span class="fl title">到账卡号</span><span class="fr content">  6212 **** 4017</span></li>-->
            <#--<li class="clearfix"><span class="fl title">提取金额</span><span class="fr content">  15,000.00元</span></li>-->
            <#--<li class="clearfix"><span class="fl title">订单号</span><span class="fr content">  39414095743040</span></li>-->
        <#--</ul>-->
    <#--&lt;#&ndash;充值&ndash;&gt;-->
        <#--<ul class="other-info-list">-->
            <#--<li class="clearfix"><span class="fl title">充值卡号</span><span class="fr content">  6212 **** 4017</span></li>-->
            <#--<li class="clearfix"><span class="fl title">充值金额</span><span class="fr content">  15,000.00元</span></li>-->
            <#--<li class="clearfix"><span class="fl title">订单号</span><span class="fr content">  39414095743040</span></li>-->
        <#--</ul>-->
    <#--</div>-->
    <#--<div class="btn-container">-->
        <#--<a href="/" class="btn-confirm">确定</a>-->
    <#--</div>-->
    <#--&lt;#&ndash;实名认证成功后下一步 去绑卡&ndash;&gt;-->
    <#--<div class="btn-container">-->
        <#--<a href="/" class="btn-confirm btn-next">下一步</a>-->
    <#--</div>-->
    <#--&lt;#&ndash;失败时重新尝试&ndash;&gt;-->
    <#--<div class="btn-container">-->
        <#--<a href="/" class="btn-confirm">再次尝试</a>-->
    <#--</div>-->
    <#--<div class="contact"><p>客服电话：400-169-1188（服务时间：9:00-20:00）</p></div>-->

<#--<h3>完成实名认证，即可开通联动优势个人资金管理账户资金安全有保障</h3>-->

<#--<form class="register-account-form" id="registerAccountForm" method="post">-->
<#--<ul class="input-list">-->
<#--<li>-->
<#--<label for="perName">真实姓名</label>-->
<#--<input type="text" name="userName" id="perName"-->
<#--placeholder="请输入您的真实姓名" maxlength="25">-->
<#--</li>-->
<#--<li>-->
<#--<label for="perNum">身份证号</label>-->
<#--<input type="text" name="identityNumber" id="perNum" autocomplete="off"-->
<#--placeholder="请输入您的身份证号" maxlength="20">-->
<#--</li>-->
<#--</ul>-->
<#--<div class="error-box"></div>-->

<#--<div class="button-layer">-->
<#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
<#--<input type="submit" class="btn-wap-normal next-step" value="立即认证" disabled/>-->
<#--</div>-->
<#--</form>-->

</div>
</@global.main>
