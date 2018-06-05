<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.bank_callback}" pageJavascript="${m_js.bank_callback}" title="个人资料">

<div class="my-account-content personal-profile">
    <div class="m-header"><em id="iconRegister" class="icon-left"><i></i></em>实名认证</div>
    <div class="info-container">
        <div class="status-container">
            <div class="icon-status icon-failure"></div>
            <p class="desc">银行卡绑定失败</p>
            <p class="reason-error">身份证信息和真实姓名不匹配</p>
        </div>
    <#--绑定银行卡-->
        <ul class="other-info-list">
            <li class="clearfix"><span class="fl title">银行卡号</span><span class="fr content">  6212 **** 4017</span></li>
        </ul>
    <#--申请提现-->
        <ul class="other-info-list">
            <li class="clearfix"><span class="fl title">到账卡号</span><span class="fr content">  6212 **** 4017</span></li>
            <li class="clearfix"><span class="fl title">提取金额</span><span class="fr content">  15,000.00元</span></li>
            <li class="clearfix"><span class="fl title">订单号</span><span class="fr content">  39414095743040</span></li>
        </ul>
    <#--充值-->
        <ul class="other-info-list">
            <li class="clearfix"><span class="fl title">充值卡号</span><span class="fr content">  6212 **** 4017</span></li>
            <li class="clearfix"><span class="fl title">充值金额</span><span class="fr content">  15,000.00元</span></li>
            <li class="clearfix"><span class="fl title">订单号</span><span class="fr content">  39414095743040</span></li>
        </ul>
    </div>

<#--失败时重新尝试-->
    <div class="btn-container">
        <a href="/" class="btn-confirm">再次尝试</a>
    </div>
</div>

</@global.main>


