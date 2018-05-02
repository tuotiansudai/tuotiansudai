<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.divide_money}" pageJavascript="${js.divide_money}" activeNav="" activeLeftNav="" title="瓜分体验金活动_活动中心_拓天速贷" keywords="拓天速贷,体验金奖励,邀请好友,新手注册" description="拓天速贷瓜分体验金活动,活动期间投资并邀请好友注册即可获得投资额等额体验金奖励,新注册用户可获得体验金翻倍奖励,体验金收益可提现.">

<div class="tour-slide compliance-banner">
    <div class="invest-tip tip-width">市场有风险，投资需谨慎！</div>
</div>

<div class="share-reward-container page-width" id="shareRewardContainer">
    
    <div class="titel-item">
        <div class="title-text">
            <span class="title-one"></span>
        </div>
    </div>
    <div class="bg-column-normal">
        <div class="actor-info">
            <h3>“老”有所得</h3>
            <p>4月13日前注册的老用户，在活动期间投资并邀请好友完成注册，即可获得与活动期间累计投资额等额的体验金奖励！</p>
            <h3>“新”有所享</h3>
            <p>4月13日之后（含4月13日）新注册的用户，在活动期间投资并邀请好友完成注册，即可获得活动期间累计投资额<span>双倍的体验金</span>奖励！</p>
        </div>
    </div>
    <div class="titel-item">
        <div class="title-text">
            <span class="title-two"></span>
        </div>
    </div>
    <div class="bg-column-normal">
        <div class="reward-item">
        </div>
        <div class="btn-item">
            <p>
                <span>小贴士：</span>体验金投资年化收益13%，到期可提现，投的越多，得到越多哦！
            </p>
        </div>
    </div>
    <div class="bg-column-normal">
        <div class="titel-item">
            <div class="title-text">
                <span class="title-four"></span>
            </div>
        </div>
     <@global.isAnonymous>
        <div class="invite-box-friend clearfix">
            <div class="info-link">
                <p>登陆后显示邀请链接</p>
                <a class="btn-copy-link show-login" href="javascript:void(0);">立即登录</a>
            </div>
        </div>
    </@global.isAnonymous>
    <@global.isNotAnonymous>
        <@global.noRole hasNoRole="'INVESTOR'">
           <#--已登录未认证-->
            <div class="invite-box-friend clearfix">
                <div class="info-link">
                    <p>认证后显示邀请链接</p>
                    <a class="btn-copy-link to-identification" target="_blank" href="<#if !isAppSource>/register/account?redirect=/activity/divide-money<#else>app/tuotian/authentication</#if>" >实名认证</a>
                </div>
            </div>
        </@global.noRole>
        <@global.role hasRole="'INVESTOR','LOANER'">
        <#--已登录已认证-->
            <div class="invite-box-friend clearfix">
                <div class="weixin-code">
                    <span class="title-code">方式1</span>
                    <em class="img-code">
                        <!--[if gte IE 8]>
                        <span style="font-size:12px">请使用更高版本浏览器查看</span>
                        <![endif]-->
                    </em>
                    <span>将扫码后的页面分享给好友即可邀请</span>
                </div>
                <div class="link-item">
                    <dl>
                        <dt>方式2</dt>
                        <dd>
                            <input type="text" class="input-invite" id="clipboard_text1"  readonly data-mobile="<@global.security.authentication property='principal.mobile' />" >
                        </dd>
                        <dd class="clearfix tc"><a href="javascript:void(0);" class="btn-copy-link  copy-button" id="copy_btn2"  data-clipboard-action="copy" data-clipboard-target="#clipboard_text1" >复制链接发送给好友</a></dd>
                    </dl>
                    <p class="info-text">好友通过您发送的链接完成注册即邀请成功</p>
                </div>
                <div class="info-link">
                    <a class="btn-copy-link" target="_blank" href="<#if !isAppSource>/referrer/refer-list<#else>app/tuotian/refer-reward</#if>">马上邀请好友</a>
                </div>
            </div>
        </@global.role>
    </@global.isNotAnonymous>
    </div>
    <div class="titel-item">
        <div class="title-text">
            <span class="title-three"></span>
        </div>
    </div>
    <div class="bg-column-normal">
        <ul class="activity-rules">
            <li>1.用户所获体验金将于活动结束后3个工作日内统一发放，用户可在App端“我的-我的体验金”或PC端“我的账户”中查看；</li>
            <li>2.获得的体验金仅限于投资拓天体验金项目，项目到期后，平台回收体验金，收益归用户所有；</li>
            <li>3.体验金不能转出，但体验金投资产生的收益可以提现；</li>
            <li>4.用户首次提现体验金投资所产生的收益时，需要投资其他定期项目（债权转让项目除外）累计满1000元才可以提现；</li>
            <li>5.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</li>
        </ul>
    </div>
</div>

<#include "../module/login-tip.ftl" />
</@global.main>