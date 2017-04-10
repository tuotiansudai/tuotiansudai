<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.divide_money}" pageJavascript="${js.divide_money}" activeNav="" activeLeftNav="" title="瓜分体验金活动_活动中心_拓天速贷" keywords="拓天速贷,体验金奖励,邀请好友,新手注册" description="拓天速贷瓜分体验金活动,活动期间投资并邀请好友注册即可获得投资额等额体验金奖励,新注册用户可获得体验金翻倍奖励,体验金收益可提现.">

<div class="tour-slide"></div>

<div class="share-reward-container page-width" id="shareRewardContainer">
    
    <div class="bg-column-normal">
        <div class="titel-item">
            <div class="title-text">
                <span>二重礼：一大波红包加码，有情更有益</span>
            </div>
        </div>
        <div class="tip-item">
            邀请好友注册投资，每月有效邀请人数越多，红包越多。
        </div>
        <ul class="info-item info-list media-item">
            <li class="money-type">
                <div class="info-intro">
                    <span class="number-text"><strong>18</strong>元</span>
                    <span>现金红包</span>
                </div>
                <div class="info-name">有效邀请2～4人可得</div>
            </li>
            <li class="money-type">
                <div class="info-intro">
                    <span class="number-text"><strong>48</strong>元</span>
                    <span>现金红包</span>
                </div>
                <div class="info-name">有效邀请5～8人可得</div>
            </li>
            <li class="money-type">
                <div class="info-intro">
                    <span class="number-text"><strong>98</strong>元</span>
                    <span>现金红包</span>
                </div>
                <div class="info-name">有效邀请9～10人可得</div>
            </li>
            <li class="money-type">
                <div class="info-intro">
                    <span class="number-text"><strong>288</strong>元</span>
                    <span>现金红包</span>
                </div>
                <div class="info-name">有效邀请10人以上可得</div>
            </li>
        </ul>
        <div class="btn-item">
            <p>
                <span>小贴士：</span>好友在注册后15天内投资额达到2000及以上（拓天体验金项目及债权转让除外），视为一个有效邀请。
            </p>
        </div>
    </div>
    <div class="bg-column-normal">
        <div class="titel-item">
            <div class="title-text">
                <span>三重礼：好友投资拿现金，双赢双收益</span>
            </div>
        </div>
        <div class="reward-item">
            <img src="${staticServer}/activity/images/divide-money/reward-img.png" width="100%" class="media-item">
            <img src="${staticServer}/activity/images/divide-money/reward-img-phone.png" width="100%" class="media-item-phone">
        </div>
        <div class="btn-item">
            <p>
                <span>小贴士：</span>好友在注册后15天内投资额达到2000及以上（拓天体验金项目及债权转让除外），视为一个有效邀请。
            </p>
        </div>
    </div>
    <div class="bg-column-normal">
     <@global.isAnonymous>
        <div class="invite-box-friend anonymous">
            <dl>
                <dt>向好友发送您的邀请链接：</dt>
                <dd>
                    <input type="text" class="input-invite" disabled value="您需要登录才可以邀请好友">
                </dd>
                <dd>
                    <a class="btn-copy-link show-login" href="javascript:void(0);">去登录</a>
                </dd>
            </dl>
        </div>
    </@global.isAnonymous>
    <@global.isNotAnonymous>
        <@global.noRole hasNoRole="'INVESTOR'">
           <#--已登录未认证-->
            <div class="invite-box-friend anonymous">
                <dl>
                    <dt>向好友发送您的邀请链接：</dt>
                    <dd>
                        <input type="text" class="input-invite"  disabled value="您的好友还不知道您是谁，先来实名认证吧">
                    </dd>
                    <dd>
                        <a class="btn-copy-link to-identification" target="_blank" href="<#if !isAppSource>/register/account?redirect=/activity/divide-money<#else>app/tuotian/authentication</#if>" >实名认证</a>
                    </dd>
                </dl>
            </div>
        </@global.noRole>
        <@global.role hasRole="'INVESTOR','LOANER'">
        <#--已登录已认证-->
            <div class="invite-box-friend clearfix non-anonymous yes-identification">
                <div class="weixin-code">
                    <em class="img-code">
                        <!--[if gte IE 8]>
                        <span style="font-size:12px">请使用更高版本浏览器查看</span>
                        <![endif]-->
                    </em>
                    <span>将扫码后的页面分享给好友即可邀请</span>
                </div>
                <dl>
                    <dd>
                        <input type="text" class="input-invite" id="clipboard_text1"  readonly data-mobile="<@global.security.authentication property='principal.mobile' />" >
                    </dd>
                    <dt class="clearfix"><a href="javascript:void(0);" class="btn-copy-link fl copy-button" id="copy_btn2"  data-clipboard-action="copy" data-clipboard-target="#clipboard_text1" >复制链接发送给好友</a></dt>
                    <dd>好友通过您发送的链接完成注册即邀请成功</dd>
                </dl>
            </div>
        </@global.role>
    </@global.isNotAnonymous>
    </div>
    <div class="bg-column-normal">
        <div class="titel-item">
            <div class="title-text">
                <span>三重礼：好友投资拿现金，双赢双收益</span>
            </div>
        </div>
        <ul class="activity-rules">
            <li>1.用户所获体验金将于活动结束后3个工作日内统一发放，用户可在App端“我的-我的体验金”或PC端“我的账户”中查看；</li>
            <li>2.获得的体验金仅限于投资拓天体验金项目，项目到期后，平台回收体验金，收益归用户所有；</li>
            <li>3.体验金不能转出，但体验金投资产生的收益可以提现；</li>
            <li>4.用户首次提现体验金投资所产生的收益时，需要投资其他定期项目（债权转让项目除外）累计满1000元才可以提现；</li>
            <li>5.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</li>
        </ul>
    </div>
</div>

<#include "login-tip.ftl" />
</@global.main>