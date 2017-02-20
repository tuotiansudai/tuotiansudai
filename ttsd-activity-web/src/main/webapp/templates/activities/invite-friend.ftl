<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.invite_friend}" pageJavascript="${js.invite_friend}" activeNav="" activeLeftNav="" title="推荐奖励_拓天速贷" keywords="拓天速贷,推荐奖励,P2P理财,短期理财,短期投资,拓天速贷2级推荐机制" description="拓天速贷针对老用户推出2级推荐机制的推荐奖励,可以让您的财富快速升值.">

<div class="tour-slide"></div>

<div class="share-reward-container page-width" id="shareRewardContainer">

    <div class="bg-column-normal invite-line">
        

     <@global.isAnonymous>
        <div class="invite-box-friend anonymous">
            <dl>
                <dt>向好友发送您的邀请链接：</dt>
                <dd>
                    <input type="text" class="input-invite" disabled value="您需要登录才可以邀请好友">
                </dd>
                <dd>
                    <a class="btn-copy-link show-login" href="javascript:void(0);">去登陆</a>
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
                        <a class="btn-copy-link to-identification" href="javascript:void(0);" >实名认证</a>
                    </dd>
                </dl>
            </div>
        </@global.noRole>

        <@global.role hasRole="'INVESTOR','LOANER'">
        <#--已登录已认证-->
            <div class="invite-box-friend clearfix non-anonymous yes-identification">
                <div class="invite-list">
                    <i class="icon-line top"><span></span></i>
                    <i class="icon-line bottom"><span></span></i>
                    <h3>邀好友拿<span>3</span>重礼包</h3>
                    <p>您已成功邀请<span>1</span>位好友， 赚取红包<span>100</span>元，赚取现金奖励<span>110</span>元 <a href="">查看邀请详情</a></p>
                </div>
                <div class="weixin-code">
                    <em class="img-code">
                        <!--[if gte IE 8]>
                        <span style="font-size:12px">请使用更高版本浏览器查看</span>
                        <![endif]-->
                    </em>
                    <span>将扫码后的页面分享给好友即可邀请</span>
                </div>
                <div class="and-text">
                    <span>或</span>
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
        
    </div>
    <div class="bg-column-normal"></div>
    <div class="bg-column-normal"></div>
    <div class="bg-column-normal"></div>
    <dl class="activity-rules">
        <dt>温馨提示：</dt>
        <dd>1.您要进行实名认证后才能享受推荐奖励；</dd>
        <dd>2.好友需通过你的专属链接注册才能建立推荐关系；</dd>
        <dd>3.注册奖励将于好友完成指定任务时实时发放；</dd>
        <dd>4.平台将于每月初1-3日统一发放上月的推荐红包奖励，为便于您分散投资，98元及288元红包将根据面额拆分发放，您可以在电脑端“我的账户-我的宝藏”或App端“我的-优惠券”中查看；</dd>
        <dd>5.现金奖励额度为推荐的好友投资本金预期年化收益的1%，奖励计算方法：您的奖励=被推荐人投资金额X（1% / 365 X 标的期限）；</dd>
        <dd>6.现金奖励将于好友投资项目放款后，一次性以现金形式直接发放至您的账户，可以在“我的账户”中查询；</dd>
        <dd>7.活动中如发现恶意注册虚假账号、恶意刷奖等违规操作及作弊行为，若判定为违规操作及作弊行为，拓天速贷将取消您的获奖的资格，并有权撤销违规交易；</dd>
        <dd>8.活动遵守拓天速贷法律声明，最终解释权归拓天速贷平台所有。</dd>
    </dl>
</div>

<div class="pop-layer-out" style="display: none">
    <div class="btn-to-close"></div>
    <p>您的好友可能猜不到你是谁
        先来进行实名认证吧！</p>

    <a href="/register/account?redirect=/activity/share-reward"  class="btn-to-identification"></a>
</div>

<#include "login-tip.ftl" />
</@global.main>