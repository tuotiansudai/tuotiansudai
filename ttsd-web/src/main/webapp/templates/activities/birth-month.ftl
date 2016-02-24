<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.company_activity}" pageJavascript="${js.birth_month}" activeNav="" activeLeftNav="" title="生日月特权">
<style type="text/css">
    body {
        background: url('/images/sign/actor/birth/bg-web.png') center top repeat-y;
    }
</style>

<div class="birth-month-container">
    <div class="birth-time">
        <div class="time-list">
            <img src="${staticServer}/images/sign/actor/birth/birth-time.png" class="tip-time" alt="">
            <img src="${staticServer}/images/sign/actor/birth/close-btn.png" class="time-close" alt="">
        </div>
    </div>
    <div class="wp-img">
        <img src="${staticServer}/images/sign/actor/birth/top-picture.png" alt="生日月banner" width="100%">
    </div>
    <div class="wp-img-phone">
        <img src="${staticServer}/images/sign/actor/birth/app-top.png" alt="生日月banner" width="100%">
    </div>
    <div class="wp">
        <p>生日想要什么礼物？</p>
        <p class="text-gift">
            <img src="${staticServer}/images/sign/actor/birth/party.png" alt="生日月banner" width="100%" class="party-img">
            <img src="${staticServer}/images/sign/actor/birth/app-party.png" alt="生日月banner" width="100%" class="party-img-phone">
        </p>
        <p class="text-bg">通通都需要!!!</p>
        <p>
            <span><img src="${staticServer}/images/sign/actor/birth/monkey.png" alt="" class="monkey-phone"></span>
            <span>拓天速贷为你准备了</span>
        </p>
        <p class="img-position"><img src="${staticServer}/images/sign/actor/birth/huihuo.png"></p>
        <p class="text-font">不会赚怎么花？</br>超高的收益等你拿。</p>
        <p class="text-bg">Let's go！！！</p>
        <p class="text-pro">
            <span class="product-left">
                <img src="${staticServer}/images/sign/actor/birth/product-three.png">
                <a href="/loan-list?productType=WYX" class="click-btn time-btn" data-kick-off-date="${.now}"></a>
            </span>
            <span class="product-right">
                <img src="${staticServer}/images/sign/actor/birth/product-six.png">
                <a href="/loan-list?productType=JYF" class="click-btn time-btn" data-kick-off-date="${.now}"></a>
            </span>
        </p>
        <p class="text-bg">活动规则</p>
        <ul class="rule-list">
            <li>1.本活动适用于平台注册用户生日当月（以绑定的身份证为准）；</li>
            <li>2.活动期间投资产品享受首月收益加倍福利；</li>
            <li>3.翻倍所得收益，体现在该笔投资项目收益中，可在“我的账户”中查询;</li>
            <li>4.本次活动不限买入金额，不限购买笔数，多买多得;</li>
            <li>5.本次活动不可与平台其他优惠券同时使用。</li>
            <li class="intro-text">***活动遵循拓天速贷法律声明，最终解释权归拓天速贷平台所有。</li>
        </ul>
        
    </div>
</div>
</@global.main>