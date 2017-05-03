<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.national_day}" pageJavascript="${js.national_day}" activeNav="" activeLeftNav="" title="拓疆先锋队_国庆创薪季_万元奖池来瓜分_拓天速贷" keywords="拓天速贷十月活动,拓天速贷拓疆先锋队活动,拓天速贷国庆创薪季活动,拓天速贷" description="拓天速贷十月拓疆先锋队活动,活动期间,参与全民加薪节即可瓜分万元奖池,新用户完成注册均可获得一次国庆创“薪”季活动抽奖机会.">
    <@global.isNotAnonymous>
    <div style="display: none" class="login-name" data-login-name='<@global.security.authentication property="principal.username" />'></div>
    <div style="display: none" class="mobile" id="MobileNumber" data-mobile='<@global.security.authentication property="principal.mobile" />'></div>
    </@global.isNotAnonymous>
<div class="tour-slide" id="tourSlide">
    <#--<ul class="page-width button-area">-->
        <#--<li><a href="javascript:void(0);">&nbsp;</a></li>-->
        <#--<li><a href="javascript:void(0);">&nbsp;</a></li>-->
        <#--<li><a href="javascript:void(0);">&nbsp;</a></li>-->
    <#--</ul>-->
</div>

<div class="national-day-frame" id="nationalDayFrame">
    <div class="seizeSeat"></div>
    <div class="reg-tag-current" style="display: none">
        <#include '../module/register.ftl' />
    </div>
 <div class="section-outer">
        <div class="section-title">
            <img src="${commonStaticServer}/activity/images/national-day/title001.png">
        </div>
        <div class="dotted-line-title"></div>
        <div class="section-inner">
            <p class="notice">如活动结束后，所有国庆专享标筹满1949000元，则参与本活动的全部用户将按投资比例瓜分10000元红包，红包适用于180天及以上标的，投资50元即可激活！<br/>
            如活动结束后，所有国庆专享标未筹满1949000元，则参与本活动的用户每人将获得50元投资红包，红包适用于180天及以上标的，需投资10000元激活！</P>

            <dl class="invest-percent-box clearfix">
                <dt>目前专享标共投资：<em class="total-invest">${allInvestAmount}</em>元</dt>
                <dd class="progress-bar">
                    <span class="progress-percent" style="width: ${investScale}%"></span>
                </dd>
                <dd class="count-total">
                    <span class="fl">已有<em>${userCount}</em>人参与</span>
                    <span class="fr">总额度：1,949,000元</span>
                </dd>

            </dl>
            <div class="mobile-padding">
                <h1 class="national-title">筹满后获奖实例演示：</h1>
                <table class="table tab-invest-show">
                    <tr>
                        <td width="30%">您在活动期间投资专享标（万元）</td>
                        <td width="24%">1</td>
                        <td width="24%">5</td>
                        <td>10</td>
                    </tr>
                    <tr>
                        <td>您获得红包(50元即可激活)</td>
                        <td>51.30元</td>
                        <td>256.54元</td>
                        <td>513.08元</td>
                    </tr>
                </table>
                <span>用户所获红包金额=10000*投资“国庆专享标”累计金额/1949000</span>

            </div>

            <div class="tc">
                <a href="/loan-list" class="btn-normal-day" target="_blank">马上投资国庆专享标！</a>
            </div>
        </div>
    </div>

<div class="section-outer">
    <div class="section-title">
        <img src="${commonStaticServer}/activity/images/national-day/title002.png">
    </div>
    <div class="dotted-line-title"></div>
    <div class="section-inner">
        <div class="bg-box-normal tc">
            <span class="my-integral">我的积分：${myPoint}</span>
        </div>

        <div class="bg-box-normal">
            <dl class="example-show">
                <dt>活动期间投资即可获得双倍积分。</dt>
                <dd>举个栗子：<img src="${commonStaticServer}/activity/images/national-day/icon-example.png" class="main-title" alt="">
                    <br/>
                    您在活动期间投资360天标的20000元，将获得20000X2=40,000的积分（活动结束后投资只能获得1倍的积分）
                </dd>
            </dl>

            <div class="tc">
                <a href="/loan-list" class="btn-normal-day" target="_blank">去投资赚积分</a>
            </div>
        </div>

        <div class="bg-box-normal">
            <dl class="example-show">
                <dt>活动期间邀请好友注册并投资，可额外获得好友年化投资额的0.1倍积分，上不封顶。</dt>
                <dd>举个栗子：<img src="${commonStaticServer}/activity/images/national-day/icon-example.png" class="main-title" alt="">
                    <br/>
                    您邀请好友注册，好友投资360天标的2万元，您可获得2000积分
                </dd>
            </dl>

            <div class="tc">
                <a href="/referrer/refer-list" class="btn-normal-day" target="_blank">邀请好友赚积分</a>
            </div>
        </div>

        <div class="to-see-point tr">
            <a href="/point-shop" target="_blank"> 积分商城看一看>></a>
        </div>

        </div>
    </div>

<div class="section-outer">
    <div class="section-title">
        <img src="${commonStaticServer}/activity/images/national-day/title003.png">
    </div>
    <div class="dotted-line-title"></div>
    <div class="section-inner clearfix">
        <p class="notice tc"><em>活动期间，</em><i>1、</i>新用户在平台完成注册、实名认证、绑卡、充值、投资均可获得一次抽奖机会。</p>

        <ul class="steps-list clearfix">
            <li class="<#if steps[0] == 1>active</#if><#if steps[0] == 2>finished</#if> step-one">
                <em class="arrow"></em>
                <#if steps[0] == 1>
                    <div class="icon-text"><a href="/register/user">去注册</a></div></#if>
                <#if steps[0] == 2>
                    <div class="icon-text">已注册</div></#if>
            </li>
            <li class="<#if steps[1] == 1>active</#if><#if steps[1] == 2>finished</#if> step-two">
                <em class="arrow"></em>
                <#if steps[1] == 0>
                    <div class="icon-text">认证</div></#if>
                <#if steps[1] == 1>
                    <div class="icon-text"><a href="/register/account">去认证</a></div></#if>
                <#if steps[1] == 2>
                    <div class="icon-text">已认证</div></#if>

            </li>
            <li class="<#if steps[2] == 1>active</#if><#if steps[2] == 2>finished</#if> step-three">
                <em class="arrow"></em>
                <#if steps[2] == 0>
                    <div class="icon-text">绑卡</div></#if>
                <#if steps[2] == 1>
                    <div class="icon-text"><a href="/bind-card">去绑卡</a></div></#if>
                <#if steps[2] == 2>
                    <div class="icon-text">已绑卡</div></#if>

            </li>

            <li class="<#if steps[3] == 1>active</#if> step-four">
                <em class="arrow"></em>
                <#if steps[3] == 0>
                    <div class="icon-text">充值</div></#if>
                <#if steps[3] == 1>
                    <div class="icon-text"><a href="/recharge">去充值</a></div></#if>

            </li>

            <li class="<#if steps[4] == 1>active</#if> step-five">
                <#if steps[4] == 0>
                    <div class="icon-text">投资</div></#if>
                <#if steps[4] == 1>
                    <@global.isAnonymous>
                        <a href="/login?redirect=/activity/autumn/luxury" class="icon-text autumn-luxury-invest-channel">去投资</a>
                    </@global.isAnonymous>
                    <@global.isNotAnonymous>
                        <a href="/loan-list" class="icon-text autumn-luxury-invest-channel">去投资</a>
                    </@global.isNotAnonymous>
                </#if>

            </li>
            </ul>

        <p class="notice tc"><em>活动期间，</em><i>2、</i>每推荐一名好友注册也可获得一次抽奖机会；好友投资，还可再得一次抽奖机会。邀请越多机会越多。</p>

    <div class="tc">
        <a href="/referrer/refer-list" class="btn-normal-day" target="_blank">立即邀请好友赢抽奖机会</a>
    </div>

        <!-- <#assign activityPrizeType = 'national-day-theme'/> -->
        <div class="national-day-theme" id="nationalDayCircle">
            <#include "gift-circle.ftl"/>
        </div>

    </div>
</div>

<div class="section-outer">

    <div class="section-inner clearfix">
        <dl class="activity-explain">
            <dt> <em><全民加薪节&emsp;&emsp;瓜分万元奖池></em>活动说明：</dt>
            <dd>1、活动期间投资“国庆专享标”，投资即可参与活动。 <br/>
                2、如活动结束后，所有“国庆专享标”筹满1949000元，则参与本活动的全部用户将按投资比例瓜分10000元红包，红包适用于180天及以上标的，投资50元即可激活！用户所获红包大奖将于活动结束后三个工作日内，由客服联系确认后，以红包形式返至用户，用户可在“我的宝藏”中查看、投资；<br/>
                3、如活动结束后，所有“国庆专享标”未筹满1949000元，则参与本活动的用户每人将获得50元投资红包。投资红包将于活动结束后三个工作日内发放，用户可在“我的账户-我的宝藏”中查看、投资使用；<br/>4、用户投资国庆专享标实行阶梯奖励制。1000≤投资额<10000时，年化收益增加0.3%；10000≤投资额<50000时，年化收益增加0.5%；投资50000元及以上年化收益增加1%；<br/>
                <span class="for-mobile"> 5、活动截止时间为10月7日24：00，如活动结束时国庆专享未筹满，继续投资本专享标的用户仍可获得50元投资红包，直至专享标筹满为止；</span></dd>
            <dt class="ex-title for-mobile"><国庆抽壕礼&emsp;&emsp;幸运大悦宾>活动说明：</dt>
            <dd class="for-mobile">所获红包将即时发放，用户可在“我的账户-我的宝藏”中查看； <br/>
                实物奖品将于活动结束后七个工作日内由客服联系确认后统一安排发放，部分地区邮费自付，详询客服；</dd>
            <dd class="for-mobile">如用户在抽奖活动中抽得券类奖品（电影票、爱奇艺一个月会员），请主动联系客服并将兑换截图发至客服，经客服确认后，将以微信红包形式将兑换金额返还至用户（电影票金额需≤50元）；</dd>
            <dt class="ex-title for-mobile">特别说明：</dt>
            <dd class="for-mobile">活动中如果出现恶意刷量等违规行为，拓天速贷将取消您获得奖励的资格，并有权撤销违规交易，拓天速贷在法律范围内保留对本活动的最终解释权。</dd>
            <dd class="for-app">详细规则请查看电脑网页版活动页面 <br/>
                拓天速贷在法律范围内保留对本活动的最终解释权。</dd>

        </dl>

        <div class="ms-code tc">
            <img src="${commonStaticServer}/activity/images/national-day/ms-code.png"  /> <br/>
            <span>如何抽到更多好礼？扫码教你</span>
        </div>
    </div>
</div>


    <div class="tip-list-frame">
    <#--真实奖品的提示-->
        <div class="tip-list" data-return="concrete">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text">恭喜您！</p>
                <p class="reward-text">抽中了<em class="prizeValue"></em>！</p>
                <p class="des-text">拓天客服将会在7个工作日内联系您发放奖品</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a></div>
        </div>

    <#--虚拟奖品的提示-->
        <div class="tip-list" data-return="virtual">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text">恭喜您！</p>
                <p class="reward-text">抽中了<em class="prizeValue"></em>！</p>
                <p class="des-text">奖品已发放至“我的宝藏”当中。</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a></div>
        </div>

    <#--没有抽奖机会-->
        <div class="tip-list" data-return="nochance">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您暂无抽奖机会啦～</p>
                <p class="des-text">赢取机会后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
        </div>

    <#--不在活动时间范围内-->
        <div class="tip-list" data-return="expired">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">不在活动时间内~</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
        </div>

    <#--实名认证-->
        <div class="tip-list" data-return="authentication">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您还未实名认证~</p>
                <p class="des-text">请实名认证后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
        </div>

    </div>

</div>
</@global.main>

