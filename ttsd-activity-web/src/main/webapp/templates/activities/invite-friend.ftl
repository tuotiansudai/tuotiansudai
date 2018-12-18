<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.invite_friend}" pageJavascript="${js.invite_friend}" activeNav="" activeLeftNav="" title="推荐奖励_拓天速贷" keywords="拓天速贷,推荐奖励,红包奖励,现金奖励,加息劵奖励,推荐机制" description="拓天速贷邀好友拿3重好礼,邀请好友得红包,好友出借送加息劵,还可拿1%现金奖励,拓天速贷可以让您的财富快速升值.">

<div class="tour-slide compliance-banner">
    <div class="invest-tip tip-width">市场有风险，出借需谨慎！</div>
</div>

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
                    <a class="btn-copy-link show-login" href="/login?redirect=/activity/invite-friend">去登录</a>
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
                        <a class="btn-copy-link to-identification" target="_blank" href="<#if !isAppSource>/register/account?redirect=/activity/invite-friend<#else>app/tuotian/authentication</#if>" >实名认证</a>
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
                    <p>您已成功邀请<span>${referrerCount}</span>位好友， 赚取红包<span>${referrerRedEnvelop}</span>元，赚取现金奖励<span>${referrerAmount}</span>元 <a href="<#if !isAppSource>/referrer/refer-list<#else>app/tuotian/refer-reward-list</#if>">查看邀请详情</a></p>
                    <p class="media-item-phone">
                        <a href="<#if !isAppSource>/referrer/refer-list<#else>app/tuotian/refer-reward-list</#if>" class="invite-btn">马上邀请好友</a>
                    </p>
                </div>
                <div class="weixin-code-out">
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
                <i class="left-icon"></i>
                <span>一重礼：好友注册就送礼，人脉即钱脉</span>
                <i class="right-icon"></i>
            </div>
            <div class="tip-item">
                邀请好友在平台完成相应操作即可获得奖励
            </div>
        </div>
        <ul class="info-item media-item">
            <li class="money-type">
                <div class="info-intro">
                    <span class="number-text"><strong>5</strong>元</span>
                    <span>出借红包</span>
                </div>
                <div class="info-name">邀请好友注册即可得</div>
            </li>
            <li class="money-type">
                <div class="info-intro">
                    <span class="number-text"><strong>10</strong>元</span>
                    <span>出借红包</span>
                </div>
                <div class="info-name">好友绑定银行卡可得</div>
            </li>
            <li class="coupon-type">
                <div class="info-intro">
                    <span><strong>+0.5</strong>%</span>
                </div>
                <div class="info-name">好友首次出借抵押类债权再得</div>
            </li>
        </ul>
        <table class="table invite-table">
            <thead>
                <tr>
                    <th>邀好友完成</th>
                    <th>您获得奖励</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>注册</td>
                    <td>5元出借红包</td>
                </tr>
                <tr>
                    <td>绑定银行卡</td>
                    <td>10元出借红包</td>
                </tr>
                <tr>
                    <td>首次出借抵押债权</td>
                    <td>+0.5%加息券</td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="bg-column-normal">
        <div class="titel-item">
            <div class="title-text">
                <i class="left-icon"></i>
                <span>二重礼：一大波红包加码，有情更有益</span>
                <i class="right-icon"></i>
            </div>
        </div>
        <div class="tip-item">
            邀请好友注册出借，每月有效邀请人数越多，红包越多。
        </div>
        <ul class="info-item info-list media-item">
            <li class="money-type">
                <div class="info-intro">
                    <span class="number-text"><strong>18</strong>元</span>
                    <span>出借红包</span>
                </div>
                <div class="info-name">有效邀请2～4人可得</div>
            </li>
            <li class="money-type">
                <div class="info-intro">
                    <span class="number-text"><strong>48</strong>元</span>
                    <span>出借红包</span>
                </div>
                <div class="info-name">有效邀请5～8人可得</div>
            </li>
            <li class="money-type">
                <div class="info-intro">
                    <span class="number-text"><strong>98</strong>元</span>
                    <span>出借红包</span>
                </div>
                <div class="info-name">有效邀请9～10人可得</div>
            </li>
            <li class="money-type">
                <div class="info-intro">
                    <span class="number-text"><strong>288</strong>元</span>
                    <span>出借红包</span>
                </div>
                <div class="info-name">有效邀请10人以上可得</div>
            </li>
        </ul>
        <table class="table invite-table">
            <thead>
                <tr>
                    <th>有效邀请人数</th>
                    <th>您获得奖励</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>2-4人</td>
                    <td>18元出借红包</td>
                </tr>
                <tr>
                    <td>5-8人</td>
                    <td>48元出借红包</td>
                </tr>
                <tr>
                    <td>9-10人</td>
                    <td>98元出借红包</td>
                </tr>
                <tr>
                    <td>10人以上</td>
                    <td>288元出借红包</td>
                </tr>
            </tbody>
        </table>
        <div class="btn-item">
            <p>
                <span>小贴士：</span>好友在注册后15天内出借额达到2000及以上（拓天体验金项目及债权转让除外），视为一个有效邀请。
            </p>
            <p class="media-item">
                <a href="/referrer/refer-list" class="invite-btn">立即邀请好友拿奖励</a>
            </p>
        </div>
    </div>
    <div class="bg-column-normal">
        <div class="titel-item">
            <div class="title-text">
                <i class="left-icon"></i>
                <span>三重礼：好友出借拿现金，双赢双收益</span>
                <i class="right-icon"></i>
            </div>
        </div>
        <div class="reward-item">
        </div>
        <div class="btn-item media-item">
            <p>
                <a href="/referrer/refer-list" class="invite-btn">立即邀请好友拿奖励</a>
            </p>
        </div>
    </div>

    <dl class="activity-rules">
        <dt>温馨提示：</dt>
        <dd>1.您要进行实名认证后才能享受推荐奖励；</dd>
        <dd>2.好友需通过你的专属链接注册才能建立推荐关系；</dd>
        <dd>3.注册奖励将于好友完成指定任务时实时发放；</dd>
        <dd>4.平台将于每月15日统一发放上月的推荐红包奖励，为便于您分散出借，98元及288元红包将根据面额拆分发放，您可以在电脑端“我的账户-我的宝藏”或App端“我的-优惠券”中查看；</dd>
        <dd>5.现金奖励额度为推荐的好友出借本金预期年化的0.5%，奖励计算方法：您的奖励=被推荐人出借金额X（0.5% / 365 X 标的期限）；</dd>
        <dd>6.现金奖励将于好友出借项目放款后，一次性以现金形式直接发放至您的账户，可以在“我的账户”中查询；</dd>
        <dd>7.活动中如发现恶意注册虚假账号、恶意刷奖等违规操作及作弊行为，若判定为违规操作及作弊行为，拓天速贷将取消您的获奖的资格，并有权撤销违规交易；</dd>
        <dd>8.活动遵守拓天速贷法律声明，最终解释权归拓天速贷平台所有。</dd>
    </dl>
</div>

</@global.main>