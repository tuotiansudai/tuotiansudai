<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.national_day}" pageJavascript="${js.national_day}" activeNav="" activeLeftNav="" title="拓荒计划_旅游活动_拓天速贷" keywords="拓荒计划,免费旅游,旅游活动投资,拓天速贷" description="拓天速贷金秋拓荒计划,多条旅游线路免费玩,你旅游我买单,邀请好友注册也可获得免费旅游大奖.">

<div class="tour-slide" id="tourSlide">
    <ul class="page-width button-area">
        <li><a href="javascript:void(0);">&nbsp;</a></li>
        <li><a href="javascript:void(0);">&nbsp;</a></li>
        <li><a href="javascript:void(0);">&nbsp;</a></li>
    </ul>
</div>

<div class="national-day-frame" id="nationalDayFrame">
    <div class="seizeSeat"></div>
    <div class="reg-tag-current" style="display: none">
        <#include '../register.ftl' />
    </div>
 <div class="section-outer">
        <div class="section-one-title"><a name="actegory01"></a> </div>
        <div class="section-inner">
            <p class="notice">如活动结束后，所有国庆专享标筹满1949000元，则参与本活动的全部用户将按投资比例瓜分10000元红包，红包适用于90天及以上标的，投资50元即可激活！<br/>
            如活动结束后，所有国庆专享标未筹满1949000元，则参与本活动的用户每人将获得50元投资红包，红包适用于180天及以上标的，需投资10000元激活！</P>

            <dl class="invest-percent-box clearfix">
                <dt>目前专享标共投资：${allInvestAmount}元</dt>
                <dd class="progress-bar">
                    <span class="progress-percent" style="width: ${investScale}%"></span>
                </dd>
                <dd class="count-total">
                    <span class="fl">已有<em>${userCount}</em>人参与</span>
                    <span class="fr">总额度：1，949，000元</span>
                </dd>

            </dl>
            <div class="mobile-padding">
                <h1 class="national-title">筹满后获奖实例演示：</h1>
                <table class="table tab-invest-show">
                    <tr>
                        <td width="30%">您在活动期间投资专享标（万元）</td>
                        <td width="24%">1</td>
                        <td width="24%">2</td>
                        <td >10</td>
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
                <a href="#" class="btn-normal-day">马上投资国庆专享标！</a>
            </div>
        </div>
    </div>

<div class="section-outer">
    <div class="section-two-title"></div>
    <div class="section-inner">
        <div class="bg-box-normal tc">
            <span class="my-integral">我的积分：${myPoint}</span>
        </div>

        <div class="bg-box-normal">
            <dl class="example-show">
                <dt>活动期间投资即可获得双倍积分。</dt>
                <dd>举个栗子：<img src="${staticServer}/activity/images/national-day/icon-example.png" class="main-title" alt="">
                    <br/>
                    您在活动期间投资360天标的20000元，将获得20000X2=40,000的积分（活动结束后投资只能获得1倍的积分）
                </dd>
            </dl>

            <div class="tc">
                <a href="#" class="btn-normal-day">去投资赚积分</a>
            </div>
        </div>

        <div class="bg-box-normal">
            <dl class="example-show">
                <dt>活动期间邀请好友注册并投资，可额外获得好友年化投资额的0.1倍积分，上不封顶。</dt>
                <dd>举个栗子：<img src="${staticServer}/activity/images/national-day/icon-example.png" class="main-title" alt="">
                    <br/>
                    您邀请好友注册，好友投资360天标的2万元，您可获得2000积分
                </dd>
            </dl>

            <div class="tc">
                <a href="#" class="btn-normal-day">去投资赚积分</a>
            </div>
        </div>

        <div class="to-see-point tr">
            <a href="/point-shop" target="_blank"> 积分商城看一看>></a>
        </div>

        </div>
    </div>

<div class="section-outer">
    <div class="section-three-title"></div>
    <div class="section-inner clearfix">
        <p class="notice tc"><em>活动期间，</em><i>1、</i>新用户在平台完成注册、实名认证、绑卡、充值、投资均可获得一次抽奖机会。</p>

        <ul class="steps-list clearfix">
            <li class="<#if steps[0] == 1>active</#if><#if steps[0] == 2>finished</#if> step-one">
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
                <div class="step-icon"></div>
            </li>
            <li class="<#if steps[2] == 1>active</#if><#if steps[2] == 2>finished</#if> step-three">
                <em class="arrow"></em>
                <#if steps[2] == 0>
                    <div class="icon-text">绑卡</div></#if>
                <#if steps[2] == 1>
                    <div class="icon-text"><a href="/bind-card">去绑卡</a></div></#if>
                <#if steps[2] == 2>
                    <div class="icon-text">已绑卡</div></#if>
                <div class="step-icon"></div>
            </li>

            <li class="<#if steps[3] == 1>active</#if> step-four">
                <em class="arrow"></em>
                <#if steps[3] == 0>
                    <div class="icon-text">充值</div></#if>
                <#if steps[3] == 1>
                    <div class="icon-text"><a href="/recharge">去充值</a></div></#if>
                <div class="step-icon"></div>
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
                <div class="step-icon"></div>
            </li>
            </ul>

        <p class="notice tc"><em>活动期间，</em><i>2、</i>每推荐一名好友注册也可获得一次抽奖机会；好友投资，还可再得一次抽奖机会。邀请越多机会越多。</p>

    <div class="tc">
        <a href="#" class="btn-normal-day">立即邀请好友赢抽奖机会</a>
    </div>
        <#include "gift-circle.ftl"/>
        <!-- <#assign activityPrizeType = 'national-day-theme'/> -->
    </div>

<div class="section-outer">
    <div class="section-three-title"></div>
    <div class="section-inner clearfix">
        <dl class="activity-explain">
            <dt> <em><全民加薪节&emsp;&emsp;瓜分万元奖池></em>活动说明：</dt>
            <dd>1、活动期间投资“国庆专享标”，单笔投资满10000元即可参与活动。 <br/>
                2、如活动结束后，所有“国庆专享标”筹满1949000元，则参与本活动的全部用户将按投资比例瓜分10000元红包，红包适用于90天及以上标的，投资50元即可激活！用户所获红包大奖将于活动结束后三个工作日内，由客服联系确认后，以红包形式返至用户，用户可在“我的宝藏”中查看、投资；<br/>
                3、如活动结束后，所有“国庆专享标”未筹满1949000元，则参与本活动的用户每人将获得50元投资红包。投资红包将于活动结束后三个工作日内发放，用户可在“我的账户-我的宝藏”中查看、投资使用；<br/>
                <span class="for-mobile"> 4、活动截止时间为10月7日24：00，如活动结束时国庆专享未筹满，继续投资本专享标的用户仍可获得50元投资红包，直至专享标筹满为止；</span></dd>
            <dt class="ex-title for-mobile"><国庆抽壕礼&emsp;&emsp;幸运大悦宾>活动说明：</dt>
            <dd class="for-mobile">所获红包将即时发放，用户可在“我的账户-我的宝藏”中查看； <br/>
                实物奖品将于活动结束后七个工作日内由客服联系确认后统一安排发放，部分地区邮费自付，详询客服；</dd>
            <dt class="ex-title for-mobile">特别说明：</dt>
            <dd class="for-mobile">活动中如果出现恶意刷量等违规行为，拓天速贷将取消您获得奖励的资格，并有权撤销违规交易，拓天速贷在法律范围内保留对本活动的最终解释权。</dd>
            <dd class="for-app">详细规则请查看电脑网页版活动页面 <br/>
                拓天速贷在法律范围内保留对本活动的最终解释权。</dd>

        </dl>

        <div class="ms-code tc">
            <img src="${staticServer}/activity/images/national-day/ms-code.png"  /> <br/>
            <span>如何抽到更多好礼？扫码教你</span>
        </div>
    </div>
</div>
</@global.main>







