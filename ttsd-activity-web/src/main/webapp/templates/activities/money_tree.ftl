<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.money_tree}" pageJavascript="${js.money_tree}" activeNav="" activeLeftNav="" title="摇钱树_拓天活动_拓天速贷" keywords="拓天速贷,拓天活动.摇钱树" description="拓天速贷摇钱树活动">

<div class="image-banner-slide"></div>
<div class="invite-des-container">
    <div class="invite-des-box-bg">
        <div class="invite-des-box">
            <p>每日摇一摇摇钱树，即有机会领取掉落的体验金，体验金最低面额<b>50</b>元；</p>
            <p>活动期间每邀请<b>1</b>个好友注册，即可增加一次摇奖机会；</p>
            <p>用户每日最多可额外获得<b>3</b>次摇奖机会，超出部分的邀请不予累计。</p>
        </div>
    </div>
</div>

<div class="money-tree-container">
    <img src="${staticServer}/activity/images/money-tree/app-ewm-img.png" alt="二维码" class="tree-qrcode">
    <input id="appVersion" type="hidden" value="${appVersion!}"/>
    <div class="btn-shake-bg">
        <a href="javascript:void(0)" class="btn-taketree" id="btn-shake">去摇钱</a>
    </div>

    <div class="example-des-box">
        <p><font>举个栗子：</font>拓小天自活动开始之日起，累计投资<b>10</b>万元，则拓小天每日摇一摇都有机会摇到<b>50-1000元</b>体验金哦！</p>
    </div>

    <div class="tip-des-box">
        <p class="tip-text">温馨提示：</p>
        <p>1. 摇一摇所获得的体验金将即时存入用户账户，可在“我的-我的体验金”中查看；</p>
        <p>2. 用户所获得的体验金仅限拓天速贷平台内使用，不可折现，不同账户积分不可合并使用；</p>
        <p>3. 体验金可用于投资体验标，投资所获收益可随时提现；</p>
        <p>4. 活动截至时间：2017年6月7日</p>
    </div>
</div>


</@global.main>