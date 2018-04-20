<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.share_reward}" pageJavascript="${js.share_reward}" activeNav="" activeLeftNav="" title="推荐奖励_拓天速贷" keywords="拓天速贷,推荐奖励,P2P投资,短期投资,短期投资,拓天速贷2级推荐机制" description="拓天速贷针对老用户推出2级推荐机制的推荐奖励,可以让您的财富快速升值.">

<div class="tour-slide"></div>

<div class="share-reward-container page-width" id="shareRewardContainer">

    <div class="bg-column-normal">
        <div class="bg-column-title">
            <span class="title-word01"></span>
        </div>

     <@global.isAnonymous>
        <div class="invite-box-friend anonymous">
            <dl>
                <dt>向好友发送您的邀请链接：</dt>
                <dd><input type="text" class="input-invite" disabled value="您需要登录才可以邀请好友">
                    <a class="btn-copy-link show-login" href="javascript:void(0);">去登陆</a>
                </dd>
            </dl>
        </div>
      </@global.isAnonymous>

    <@global.isNotAnonymous>

        <@global.noRole hasNoRole="'INVESTOR'">
           <#--已登录未认证-->
            <div class="invite-box-friend no-identification">
                <dl>
                    <dt>向好友发送您的邀请链接：</dt>
                    <dd><input type="text" class="input-invite"  disabled value="您的好友还不知道您是谁，先来实名认证吧">
                        <a class="btn-copy-link to-identification" href="javascript:void(0);" >实名认证</a>
                    </dd>
                </dl>
            </div>
        </@global.noRole>

        <@global.role hasRole="'INVESTOR','LOANER'">
        <#--已登录已认证-->
            <div class="invite-box-friend clearfix non-anonymous yes-identification">
                <dl>
                    <dd>
                        <input type="text" class="input-invite" id="clipboard_text1"  readonly data-mobile="<@global.security.authentication property='principal.mobile' />" >
                    </dd>
                    <dt class="clearfix">向好友发送您的邀请链接：  <a href="javascript:void(0);" class="btn-copy-link fr copy-button" id="copy_btn2"  data-clipboard-action="copy" data-clipboard-target="#clipboard_text1" >复制链接</a></dt>
                </dl>

                <div class="weixin-code">
                    <em class="img-code">
                        <!--[if gte IE 8]>
                        <span style="font-size:12px">请使用更高版本浏览器查看</span>
                        <![endif]-->
                    </em>
                    <span>将扫码后的页面<br/>分享给好友即可邀请</span>
                </div>
            </div>

        </@global.role>
    </@global.isNotAnonymous>

    </div>

    <div class="bg-column-normal">
        <div class="bg-column-title">
            <span class="title-word02"></span>
        </div>

        <div class="border-dashed-square earn-commission clearfix">
            <div class="arrow-step one"></div>
            <div class="arrow-step two"></div>
            <dl class="earn-list clearfix">
                <dd class="one">
                    <em class="icon-tag"></em>
                    <span class="btn-bar">
                        <i class="arr-left"></i><b>发送邀请链接给好友</b><i class="arr-right"></i>
                    </span>
                </dd>
                <dd class="two">
                    <em class="icon-tag"></em>
                    <span class="btn-bar">
                        <i class="arr-left"></i><b>好友注册并购买</b><i class="arr-right"></i>
                    </span>
                </dd>
                <dd class="three">
                    <em class="icon-tag"></em>
                    <span class="btn-bar">
                        <i class="arr-left"></i><b>您获得好友投资本金预期年化收益的1%</b><i class="arr-right"></i>
                    </span>
                </dd>

            </dl>

            <div class="mobile-btn-bar">
                  发送邀请链接给好友，好友注册并购买， <br/>
                     您获得好友投资本金预期年化收益的1%
            </div>

        </div>

        <div class="border-dashed-square earn-commission2 clearfix">
            <div class="arrow-step one"></div>
            <div class="arrow-step two"></div>

            <div class="invite-group-list">
                <div class="invite-group one">
                    <em class="icon-tag"></em>
                    <span>好友邀请其他人注册并购买</span>
                </div>
                <div class="invite-group two">
                    <em class="icon-tag"></em>
                    <span>您获得投资本金预期年化收益的1%</span>
                </div>
                <div class="invite-group three">
                    <em class="icon-tag"></em>
                    <span>好友获得投资本金预期年化收益的1%</span>
                </div>
            </div>

            <p class="for-mobile">好友邀请其他人注册并购买，您获得投资本金预期年化收益的1%
                好友获得投资本金预期年化收益的1%</p>

            </div>
     </div>

    <div class="bg-column-normal reward-rules">
        <div class="bg-column-title">
            <span class="title-word03"></span>
        </div>
        <div class="border-dashed-square reward-rules-normal clearfix">

            <div class="invite-group one">
                <em class="icon-tag"></em>
                <span>您邀请了好友悟空</span>
            </div>
            <div class="arrow-step"></div>
            <div class="invite-group two">
                <em class="icon-tag"></em>
                <span>悟空注册并投资了90天的标的10万元</span>
            </div>
            <div class="arrow-step"></div>
            <div class="invite-group three">
                <em class="icon-tag"></em>
                <span>您获得奖励246.57元</span>
            </div>

            <p class="for-mobile">您邀请了好友悟空，悟空注册并投资了90天的标的10万元
                您获得奖励246.57元。</p>
        </div>

        <div class="border-dashed-square reward-rules-normal clearfix">

            <div class="invite-group four">
                <em class="icon-tag"></em>
                <span>悟空邀请了好友八戒</span>
            </div>
            <div class="arrow-step"></div>
            <div class="invite-group two">
                <em class="icon-tag"></em>
                <span>八戒注册并投资了90天的标的10万元</span>
            </div>
            <div class="arrow-step"></div>
            <div class="invite-group five">
                <em class="icon-tag"></em>
                <span>您和悟空都可以获得奖励246.57元</span>
            </div>
            <p class="for-mobile">悟空邀请了好友八戒，八戒注册并投资了90天的标的10万元
                您和悟空都可以获得奖励246.57元</p>
        </div>

        <div class="border-dashed-square reward-rules-normal clearfix">

            <div class="invite-group two">
                <em class="icon-tag"></em>
                <span>八戒和悟空又这样投资了5次</span>
            </div>
            <div class="arrow-step"></div>
            <div class="invite-group six">
                <em class="icon-tag"></em>
                <span>您获得奖励=246X2X5=2460元</span>
            </div>
            <p class="for-mobile">八戒和悟空又这样投资了5次，您获得奖励=246X2X5=2460元</p>
        </div>

        <@global.isAnonymous>
            <div class="invite-box-friend anonymous">
                <dl>
                    <dt>向好友发送您的邀请链接：</dt>
                    <dd><input type="text" class="input-invite" disabled value="您需要登录才可以邀请好友">
                        <a class="btn-copy-link show-login" href="javascript:void(0);">去登陆</a>
                    </dd>
                </dl>
            </div>
        </@global.isAnonymous>

        <@global.isNotAnonymous>

            <@global.noRole hasNoRole="'INVESTOR'">
            <#--已登录未认证-->
                <div class="invite-box-friend no-identification">
                    <dl>
                        <dt>向好友发送您的邀请链接：</dt>
                        <dd><input type="text" class="input-invite"  disabled value="您的好友还不知道您是谁，先来实名认证吧">
                            <a class="btn-copy-link to-identification" href="javascript:void(0);" >实名认证</a>
                        </dd>
                    </dl>
                </div>
            </@global.noRole>

            <@global.role hasRole="'INVESTOR','LOANER'">
            <#--已登录已认证-->
                <div class="invite-box-friend clearfix non-anonymous yes-identification">
                    <dl>
                        <dd>
                            <input type="text" class="input-invite" id="clipboard_text2" readonly data-mobile="<@global.security.authentication property='principal.mobile' />" >
                        </dd>
                        <dt class="clearfix">向好友发送您的邀请链接：  <a href="javascript:void(0);" class="btn-copy-link fr copy-button" id="copy_btn2" data-clipboard-action="copy" data-clipboard-target="#clipboard_text2">复制链接</a></dt>
                    </dl>

                    <div class="weixin-code">
                        <em class="img-code">
                            <!--[if gte IE 8]>
                            <span style="font-size:12px">请使用更高版本浏览器查看</span>
                            <![endif]-->
                        </em>
                        <span>将扫码后的页面<br/>分享给好友即可邀请</span>
                    </div>
                </div>

            </@global.role>
        </@global.isNotAnonymous>
    </div>

    <div class="bg-column-normal recommended-ranking">
        <div class="bg-column-title">
            <span class="title-word04"></span>
        </div>

        <div class="border-dashed-square  clearfix">
            <table class="table">
                <tr>
                    <td class="pc-one-width">01</td>
                    <td>156****8605</td>
                    <td>推荐好友投资获得奖励已超过12,000元</td>
                </tr>
                <tr class="even">
                    <td>02</td>
                    <td>135****8850</td>
                    <td>推荐好友投资获得奖励已超过11,000元</td>
                </tr>
                <tr>
                    <td>03</td>
                    <td>186****3390</td>
                    <td>推荐好友投资获得奖励已超过7,000元</td>
                </tr>
                <tr class="even">
                    <td>04</td>
                    <td>186****3306</td>
                    <td>推荐好友投资获得奖励已超过6,000元</td>
                </tr>
                <tr>
                    <td>05</td>
                    <td>186****1006</td>
                    <td>推荐好友投资获得奖励已超过5,600元</td>
                </tr>
            </table>
        </div>

        <div class="note tc">说明：此排行提取的数据为2016/8/25之前的用户投资奖励</div>
    </div>

    <div class="border-dashed-square activity-rules">
        <b>活动规则</b>
        <p>
            >您要进行实名认证后才能享受推荐奖励； <br/>
            >推荐的好友及好友推荐的用户进行投资时，您可以获得奖励；<br/>
            >奖励额度为推荐的好友投资本金预期年化收益的1%，奖励计算方法：您的奖励=被推荐人投资金额X（1% / 365 X 标的期限）；<br/>
            >奖励在好友投资项目放款后，一次性以现金形式直接发放至您的账户，你可以在“我的账户”中查询；<br/>
            >活动遵守拓天速贷法律声明，最终解释权归拓天速贷平台所有
        </p>
    </div>
</div>

<div class="pop-layer-out" style="display: none">
    <div class="btn-to-close"></div>
    <p>您的好友可能猜不到你是谁
        先来进行实名认证吧！</p>

    <a href="/register/account?redirect=/activity/share-reward"  class="btn-to-identification"></a>
</div>

<#include "login-tip.ftl" />
</@global.main>