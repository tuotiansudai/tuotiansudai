<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.share_reward}" pageJavascript="${js.share_reward}" activeNav="" activeLeftNav="" title="推荐奖励_拓天速贷" keywords="拓天速贷,推荐奖励,P2P理财,短期理财,短期投资,拓天速贷2级推荐机制" description="拓天速贷针对老用户推出2级推荐机制的推荐奖励,可以让您的财富快速升值.">

<div class="tour-slide"></div>

<div class="share-reward-container page-width">

    <div class="bg-column-normal">
        <div class="bg-column-title">
            <span class="title-word01"></span>
        </div>

        <div class="invite-box-friend anonymous">
            <dl>
                <dt>向好友发送您的邀请链接：</dt>
                <dd><input type="text" class="input-invite" value="https://tuotiansudai.com/register/user?referrer=cg007008">
                    <button type="button" class="btn-copy-link">复制链接</button>
                </dd>
            </dl>
        </div>

        <#--<div class="invite-box-friend clearfix non-anonymous">-->
            <#--<dl>-->
                <#--<dd>-->
                    <#--<input type="text" class="input-invite" value="https://tuotiansudai.com/register/user?referrer=cg007008">-->
                <#--</dd>-->
                <#--<dt class="clearfix">向好友发送您的邀请链接：  <button type="button" class="btn-copy-link fr">复制链接</button></dt>-->
            <#--</dl>-->

            <#--<div class="weixin-code">-->
                <#--<img src="${staticServer}/activity/images/share-reward/wei-code.png">-->
                <#--<span>微信扫码邀请好友</span>-->
            <#--</div>-->
        <#--</div>-->

    </div>

    <div class="bg-column-normal">
        <div class="bg-column-title">
            <span class="title-word02"></span>
        </div>
     </div>
</div>

<#--<#include "login-tip.ftl" />-->
</@global.main>