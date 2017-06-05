<#import "../../macro/global-dev.ftl" as global>

<#assign jsName = 'coupon_special' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.css"}>

<@global.main pageCss="${css.coupon_special}" pageJavascript="${js.coupon_special}" activeNav="" activeLeftNav="" title="活动中心_投资活动_拓天速贷" keywords="拓天活动中心,拓天活动,拓天投资列表,拓天速贷" description="拓天速贷活动中心为投资用户提供投资大奖,投资奖励,收益翻倍等福利,让您在赚钱的同时体验更多的投资乐趣.">

<div class="activity-slide" id="topHeader"></div>

<div class="activity-frame-box page-width">
    <div class="red-box">
        <div class="red-title"></div>
        <p>活动期间，微信扫描二维码关注<em>“拓天速贷服务号”</em>，回复<em>“我要领券”</em>，
            即可领取<em>1000元红包+0.8%加息券</em>，每人限领1次。</p>
        <div class="scan-code">
        </div>
        <em class="icon-money one"></em>
        <em class="icon-money two"></em>
        <em class="icon-money three"></em>
        <em class="icon-money four"></em>
    </div>

    <div class="red-box reward-box">
        <img src="" id="redBag">
    </div>

    <div class="wechat-scan">
        <h1>微信扫一扫，以上福利全归您</h1>
        <dl class="img-list">
            <dd class="one"></dd>
            <dd class="two">
                <h2>立即关注“拓天速贷服务号”，回复“我要领券”领取大红包！</h2>
                <div class="img-cat"></div>
            </dd>
        </dl>
    </div>
</div>

</@global.main>