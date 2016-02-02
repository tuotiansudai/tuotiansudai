<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.company_activity}" pageJavascript="${js.about_us}" activeNav="" activeLeftNav="" title="现金红包">
<style type="text/css">
    body {
        background: #8e0500 url('/images/sign/actor/redbag/red-bg.png') center top repeat-y;
    }
</style>

<div class="red-envelope-container" id="WhetherApp">
    <div class="actor-img">
        <img src="${staticServer}/images/sign/actor/redbag/re-top.png" alt="注册即送红包" class="red-top">
        <img src="${staticServer}/images/sign/actor/redbag/red-top-mobile.png" alt="注册即送红包" class="red-top-mobile">
        <img src="${staticServer}/images/sign/actor/redbag/red_envelope.png" alt="注册即送红包">
    </div>
    <div class="line-date-box">
        <div class="line-date">
            <div class="date-info">
                <i class="fa fa-circle left"></i><span>活动时间：2016年2月3日至2016年5月3日</span><i class="fa fa-circle right"></i>
            </div>
        </div>
    </div>
    <div class="mobile">
        <a href="/my-treasure" id="redEnvelope" data-kick-off-date="${.now}"><img src="${staticServer}/images/sign/actor/redbag/red-get.png" alt="注册即送红包"></a>
        <img src="${staticServer}/images/sign/actor/redbag/red-info.png" alt="注册即送红包">
    </div>
</div>

<div id="redEnvelopePopWindow" class="hide">
    <img src="${staticServer}/images/sign/actor/redbag/red-pop.png">
    <div class="close-tip"></div>
</div>
</@global.main>