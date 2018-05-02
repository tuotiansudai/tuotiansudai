<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.autumn_travel}" pageJavascript="${js.autumn_travel}" activeNav="" activeLeftNav="" title="拓荒计划_旅游活动_拓天速贷" keywords="拓荒计划,免费旅游,旅游活动投资,拓天速贷" description="拓天速贷金秋拓荒计划,多条旅游线路免费玩,你旅游我买单,邀请好友注册也可获得免费旅游大奖.">
<@global.isNotAnonymous>
<div style="display: none" class="login-name" data-login-name='<@global.security.authentication property="principal.username" />'></div>
<div style="display: none" class="mobile" id="MobileNumber" data-mobile='<@global.security.authentication property="principal.mobile" />'></div>
</@global.isNotAnonymous>
<div class="tour-slide">
    <div class="page-width">
        <div class="activity-date">第二期活动时间：<br/> 9月15日 ~ 9月21日</div>
    </div>
</div>
<div class="autumn-tour-frame" id="autumnTravelPage">
    <div class="reg-tag-current" style="display: none">
        <#include '../module/register.ftl' />
    </div>

    <div class="bg-box">
        <div class="title-normal-box">
            <div class="title-normal-title01"></div>
            <div class="box-inner-content">
                <div class="vertical-line"></div>
                <div class="vertical-line"></div>
                <div class="vertical-line"></div>
                <div class="vertical-line"></div>
                <div class="vertical-line"></div>
                <div class="vertical-line"></div>
                <h1>简单三步，免费去旅游！</h1>
                <p>
                    <em>步骤一：</em>进入”我要投资“页面； <br/>
                    <em>步骤二：</em>找到约定年化利率为7%的标的并对其投资；<br/>
                    <em>步骤三：</em>每日投资该标的达到指定额度，即可0元获得该商品，同时又能拿收益！
                </p>
            </div>
        </div>

        <div class="prize-kind clearfix swiper-container">
            <div class="swiper-wrapper" id="sliderBox">

                <div class="prize-box swiper-slide ">
                    <div class="pk-title clearfix">
                        <span><em>投资</em><i>5万</i>元</span>
                        <span><em>额外收益</em><i>1553</i>元</span>
                    </div>
                    <div class="img-info">
                        <a href="/activity/autumn/travel/1/detail" target="_blank">
                            <img src="/activity/images/autumn-tour/pp01.png"></a>
                        <span class="kind-text">华东五市双高五日游</span>
                        <div class="kind-bottom">
                            <span class="fl">价值<em>1599</em>元/人</span>

                            <a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>

                        </div>
                    </div>
                </div>
                <div class="prize-box swiper-slide">
                    <div class="pk-title clearfix">
                        <span><em>投资</em><i>10万</i>元</span>
                        <span><em>额外收益</em><i>3106</i>元</span>
                    </div>
                    <div class="img-info">
                        <a href="/activity/autumn/travel/2/detail" target="_blank">
                            <img src="/activity/images/autumn-tour/pp021.png"></a>
                        <span class="kind-text">越南岘港一地6日度假游</span>
                        <div class="kind-bottom">
                            <span class="fl">价值<em>2680</em>元/人</span>

                            <a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>

                        </div>
                    </div>
                </div>

                <div class="prize-box swiper-slide">
                    <div class="pk-title clearfix">
                        <span><em>投资</em><i>15万</i>元</span>
                        <span><em>额外收益</em><i>4660</i>元</span>
                    </div>
                    <div class="img-info">
                        <a href="/activity/autumn/travel/3/detail" target="_blank">
                            <img src="/activity/images/autumn-tour/pp031.png">
                        </a>
                        <span class="kind-text">日本和韵·九州4日游</span>
                        <div class="kind-bottom">
                            <span class="fl">价值<em>3880</em>元/人</span>

                            <a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>

                        </div>
                    </div>
                </div>
                <#--<div class="bg-shadow"></div>-->
            </div>
        </div>


        <#--<div class="prize-kind clearfix swiper-container">-->
            <#--<div class="swiper-wrapper" id="sliderBox">-->
                <#--<#list travelPrize as prize>-->
                    <#--<div class="prize-box swiper-slide <#if prize_index == 1>active</#if>">-->

                        <#--<div class="pk-title clearfix">-->
                            <#--<span><em>投资</em><i><@amount>${prize.investAmount?string.computer}</@amount></i>元</span>-->
                            <#--<span><em>额外收益</em><i>1000.00万</i>元</span>-->
                        <#--</div>-->

                        <#--&lt;#&ndash;<div class="pk-title">投资满<span><@amount>${prize.investAmount?string.computer}</@amount></span>元即可获得</div>&ndash;&gt;-->
                        <#--<div class="img-info">-->
                        <#--<#if !isAppSource>-->
                            <#--<a href="/activity/autumn/travel/${prize.id?string.computer}/detail" target="_blank">-->
                        <#--<#else>-->
                        <#--<a href="javascript:void(0)">-->
                        <#--</#if>-->
                            <#--<img src="${prize.image}"></a>-->
                            <#--<span class="kind-text">${prize.name}</span>-->
                            <#--<div class="kind-bottom">-->
                                <#--<span class="fl">商品价格<em>${prize.price}</em>元</span>-->
                                <#--<@global.isAnonymous>-->
                                    <#--<a href="/login?redirect=/activity/autumn/travel" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>-->
                                <#--</@global.isAnonymous>-->
                                <#--<@global.isNotAnonymous>-->
                                    <#--<a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>-->
                                <#--</@global.isNotAnonymous>-->
                            <#--</div>-->
                        <#--</div>-->
                    <#--</div>-->
                <#--</#list>-->
                <#--<div class="bg-shadow"></div>-->
            <#--</div>-->
        <#--</div>-->

        <div class="award-records clearfix" id="awardRecordsFrame">

            <div class="award-box fl">
                <div class="scan-code">
                    <img src="${commonStaticServer}/activity/images/autumn-tour/wx.png">
                <span>如何说走就走？ <br/>
                    码上教你<br/>
                    扫码查看活动攻略
                </span>
                </div>

            </div>

                <div class="tc customized-button">
                    <a href="/loan-list" class="btn-normal autumn-travel-invest-channel">立即投资领奖</a>
                </div>
        </div>
    </div>

    <div class="bg-box">
        <div class="title-normal-box steps-box">
            <div class="title-normal-title02"></div>
            <div class="box-inner-content">
                <h1>活动期间，新用户在平台完成注册、实名认证、绑卡、充值、投资均可获得一次抽奖机会。</h1>
                <ul class="steps-list clearfix">
                    <li class="<#if steps[0] == 1>to-finish</#if><#if steps[0] == 2>finished</#if>">
                        <i class="arrow-left"></i>
                        <i class="arrow-right"></i>
                        <em>
                            <#if steps[0] == 1>
                                <a href="/register/user">去注册 >></a>
                            </#if>
                            <#if steps[0] == 2>已注册<b class="fa fa-check-circle"></b></#if>
                        </em>
                        <s class="arrow-left"></s>
                        <s class="arrow-right"></s>
                    </li>

                    <li class="<#if steps[1] == 1>to-finish</#if><#if steps[1] == 2>finished</#if>">
                        <i class="arrow-left"></i>
                        <i class="arrow-right"></i>
                        <em>
                            <#if steps[1] == 0>认证</#if>
                            <#if steps[1] == 1>
                                <a href="/register/account">去认证 >></a>
                            </#if>
                            <#if steps[1] == 2>已认证<b class="fa fa-check-circle"></b></#if>
                        </em>
                        <s class="arrow-left"></s>
                        <s class="arrow-right"></s>
                    </li>

                    <li class="<#if steps[2] == 1>to-finish</#if><#if steps[2] == 2>finished</#if>">
                        <i class="arrow-left"></i>
                        <i class="arrow-right"></i>
                        <em>
                            <#if steps[2] == 0>绑卡</#if>
                            <#if steps[2] == 1>
                                <a href="/bind-card">去绑卡 >></a>
                            </#if>
                            <#if steps[2] == 2>已绑卡<b class="fa fa-check-circle"></b></#if>
                        </em>
                        <s class="arrow-left"></s>
                        <s class="arrow-right"></s>
                    </li>

                    <li class="<#if steps[3] == 1>to-finish</#if>">
                        <i class="arrow-left"></i>
                        <i class="arrow-right"></i>
                        <em>
                            <#if steps[3] == 0>充值</#if>
                            <#if steps[3] == 1>
                                <a href="/recharge">去充值 >></a>
                            </#if>
                        </em>
                        <s class="arrow-left"></s>
                        <s class="arrow-right"></s>
                    </li>

                    <li class="<#if steps[4] == 1>to-finish</#if>">
                        <i class="arrow-left"></i>
                        <i class="arrow-right"></i>
                        <em>
                            <#if steps[4] == 0>投资</#if>
                            <#if steps[4] == 1>
                                <@global.isAnonymous>
                                    <a href="/login?redirect=/activity/autumn/travel" class="autumn-travel-invest-channel">去投资 >></a>
                                </@global.isAnonymous>
                                <@global.isNotAnonymous>
                                    <a href="/loan-list" class="autumn-travel-invest-channel">去投资 >></a>
                                </@global.isNotAnonymous>
                            </#if>
                        </em>
                        <s class="arrow-left"></s>
                        <s class="arrow-right"></s>
                    </li>
                </ul>
            </div>

            <span class="activity-text">活动期间，每推荐一名好友注册也可获得一次抽奖机会；好友投资，还可再得一次抽奖机会。邀请越多机会越多。</span>
            <div class="tc customized-button">
                <span>
                    <a href="/referrer/refer-list" class="btn-normal">立即邀请好友赢抽奖机会</a>
                </span>

            </div>
        </div>

        <div class="lottery-draw tour-theme ">
            <#assign prizeType = 'travel'/>
            <#include "gift-circle-draw.ftl"/>
        </div>

    </div>

    <div class="bg-box activity-rule">
        <b>活动规则</b>
        <span class="pc">
1．活动期间，用户在”我要投资“页面投资7%标的，即视为参与活动；<br/>
2．本活动仅计算当日的投资额，用户在当日24点之前进行的多次投资，金额可累计计算，次日全部清零；<br/>
3．本次活动期间，用户每人每天仅限量领取一个旅游产品；<br/>
4．如用户在当日投资中获得免费旅游大奖，客服将于该债权放款后三个工作日内电话联系确认；<br/>
5．旅游奖品中奖并由客服确认信息后，不可再行更改。如无法按时参团请与旅行社接洽，旅途中相关问题及引起的不良后果与拓天速贷无关；<br/>
6．旅游产品内容随淡旺季变化价格调整较为频繁，请用户在投资前注意网页中的出团日期，避免延误您的行程；网页中的产品介绍、行程安排等图片及文字信息仅供参考，最终产品内容以用户和旅行社的签约合同为准；<br/>
7．用户在抽奖活动中所获得的红包和加息券实时发放，可在“我的账户-我的宝藏”中查看；<br/>
8．获取抽奖机会和抽奖过程中，如果出现恶意刷量等违规行为，拓天速贷将取消您获得奖励的资格，并有权撤销违规交易，收回活动中所得的奖品；<br/>
拓天速贷在法律范围内保留对本活动的最终解释权。<br/>
9．市场有风险，投资需谨慎。
        </span>
        <span class="mobile">
            1．用户在”我要投资“页面投资7%标的，即视为参与活动；<br/>
2．本活动仅计算当日的投资额，次日全部清零；<br/>
3．用户每人每天仅限量领取一个旅游产品；<br/>
4．如用户在当日投资中获得免费旅游大奖，客服将于该债权放款后三个工作日内电话联系确认；<br/>
5．用户在抽奖活动中所获得的红包和加息券实时发放，可在“我的账户-我的宝藏”中查看；<br/>
6．拓天速贷在法律范围内保留对本活动的最终解释权；<br/>
7．市场有风险，投资需谨慎。<br/>
详细活动规则请查看电脑网页版活动页面
        </span>
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


