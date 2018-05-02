<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.autumn_luxury}" pageJavascript="${js.autumn_luxury}" activeNav="" activeLeftNav="" title="拓天奢品_奢品活动_拓天速贷" keywords="拓天大奖,大奖活动,投资活动,拓天速贷" description="拓天速贷奢华投资活动,海量奢品拓手可得,拓天大奖活动让您左手投资赚收益,右手白拿奢侈品.">
<@global.isNotAnonymous>
<div style="display: none" class="login-name" data-login-name='<@global.security.authentication property="principal.username" />'></div>
<div style="display: none" class="mobile" id="MobileNumber" data-mobile='<@global.security.authentication property="principal.mobile" />'></div>
</@global.isNotAnonymous>
<div class="tour-slide">
    <div class="page-width">
        <div class="activity-date">第二期活动时间：9月11日~9月17日</div>
    </div>

</div>

<div class="luxury-list-container">

    <div class="wp clearfix">
        <div class="left-bg"></div>
        <div class="right-bg"></div>
        <div class="reg-tag-current" style="display: none">
            <#include '../module/register.ftl' />
        </div>
        <div class="luxury-item clearfix">
            <div class="title-one">
                <img src="${commonStaticServer}/activity/images/sign/actor/luxury/title-one.png">
            </div>
            <div class="title-info clearfix">
                <h1>简单三步，奖品收益两手拿！</h1>
                <P>
                    <em>步骤一：</em>进入”我要投资“页面；<br/>
                    <em>步骤二：</em>找到约定年化利率为7%的标的并对其投资；<br/>
                    <em>步骤三：</em><span class="info"><b>”零元兑“</b>玩法：每日投资该标的达到指定额度，即可0元获得该商品，同时又能拿收益！<br/>
                    <b class="space">”折扣兑“</b>玩法：如每日投资该标的不满1万元，可投资少量金额以获得该商品折扣券，同时又能拿收益，详见积分商城！</span>
                </P>
             </div>
            <div class="line-item"></div>
            <div class="prize-kind clearfix swiper-container">
                <div class="swiper-wrapper" id="sliderBox">

                    <div class="prize-box swiper-slide ">
                        <div class="pk-title clearfix">
                            <span><em>投资</em><i>10万</i>元</span>
                            <span><em>额外收益</em><i>3106</i>元</span>
                        </div>
                        <div class="img-info">
                            <a href="/activity/autumn/luxury/1/detail" target="_blank">
                                <img src="/activity/images/sign/actor/luxury/pp01.png">
                            </a>
                            <#--<a href="/upload/20160910/60391473486032623.jpg" target="_blank">-->
                                <#--<img src="/activity/images/sign/actor/luxury/pp01.png">-->
                            <#--</a>-->
                            <span class="kind-text">YSL蓝色钱包</span>
                            <div class="kind-bottom">
                                <span class="fl">商品价格<em>5000</em>元</span>
                                <a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>
                            </div>
                        </div>
                    </div>
                    <div class="prize-box swiper-slide active">
                        <div class="pk-title clearfix">
                            <span><em>投资</em><i>12万</i>元</span>
                            <span><em>额外收益</em><i>3728</i>元</span>
                        </div>
                        <div class="img-info">
                            <a href="/activity/autumn/luxury/2/detail" target="_blank">
                                <img src="/activity/images/sign/actor/luxury/pp02.png"></a>
                            <span class="kind-text">LV红色漆皮钱包</span>
                            <div class="kind-bottom">
                                <span class="fl">商品价格<em>6000</em>元</span>
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
                            <a href="/activity/autumn/luxury/3/detail" target="_blank">
                                <img src="/activity/images/sign/actor/luxury/pp03.png"></a>
                            <span class="kind-text">Dior粉色漆皮钱包</span>
                            <div class="kind-bottom">
                                <span class="fl">商品价格<em>7000</em>元</span>
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
                            <a href="/activity/autumn/luxury/4/detail" target="_blank">
                                <img src="/activity/images/sign/actor/luxury/pp04.png"></a>
                            <span class="kind-text">浪琴手表黛绰维纳系列</span>
                            <div class="kind-bottom">
                                <span class="fl">商品价格<em>9100</em>元</span>
                                <a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>

                            </div>
                        </div>
                    </div>


                    <div class="prize-box swiper-slide">
                        <div class="pk-title clearfix">
                            <span><em>投资</em><i>18万</i>元</span>
                            <span><em>额外收益</em><i>5592</i>元</span>
                        </div>
                        <div class="img-info">
                            <a href="/activity/autumn/luxury/5/detail" target="_blank">
                                <img src="/activity/images/sign/actor/luxury/pp05.png">
                            </a>
                            <span class="kind-text">浪琴手表康卡斯系列</span>
                            <div class="kind-bottom">
                                <span class="fl">商品价格<em>7000</em>元</span>
                                <a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>

                            </div>
                        </div>
                    </div>
                    <div class="prize-box swiper-slide">
                        <div class="pk-title clearfix">
                            <span><em>投资</em><i>20万</i>元</span>
                            <span><em>额外收益</em><i>6213</i>元</span>
                        </div>
                        <div class="img-info">
                            <a href="/activity/autumn/luxury/6/detail" target="_blank">
                                <img src="/activity/images/sign/actor/luxury/pp06.png"></a>
                            <span class="kind-text">浪琴手表黛绰维纳系列</span>
                            <div class="kind-bottom">
                                <span class="fl">商品价格<em>11360</em>元</span>

                                <a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>

                            </div>
                        </div>
                    </div>
                    <div class="prize-box swiper-slide">
                        <div class="pk-title clearfix">
                            <span><em>投资</em><i>24万</i>元</span>
                            <span><em>额外收益</em><i>7456</i>元</span>
                        </div>
                        <div class="img-info">
                            <a href="/activity/autumn/luxury/7/detail" target="_blank">
                                <img src="/activity/images/sign/actor/luxury/pp07.png"></a>
                            <span class="kind-text">LV小号手袋开心果绿</span>
                            <div class="kind-bottom">
                                <span class="fl">商品价格<em>11600</em>元</span>
                                <a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>

                            </div>
                        </div>
                    </div>
                    <div class="prize-box swiper-slide">
                        <div class="pk-title clearfix">
                            <span><em>投资</em><i>30万</i>元</span>
                            <span><em>额外收益</em><i>9320</i>元</span>
                        </div>
                        <div class="img-info">
                            <a href="/activity/autumn/luxury/8/detail" target="_blank">
                                <img src="/activity/images/sign/actor/luxury/pp08.png"></a>
                            <span class="kind-text">prada 黑色杀手包</span>
                            <div class="kind-bottom">
                                <span class="fl">商品价格<em>15000</em>元</span>
                                <a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>

                            </div>
                        </div>
                    </div>


                    <div class="prize-box code-item-box">
                        <div class="code-item">
                            <p>如何获得<span>更多奢侈品</span>？码上教你</p>
                            <p>
                                <img src="${commonStaticServer}/activity/images/sign/actor/luxury/luxury-code.jpg">
                            </p>
                            <p>扫码查看活动攻略</p>
                        </div>
                    </div>

                </div>
            </div>

            <div class="btn-item">
                <a href="/loan-list" class="autumn-luxury-invest-channel" target="_blank">立即投资</a>

            </div>
        </div>
        <div class="luxury-item hack clearfix">
            <div class="title-two">
                <img src="${commonStaticServer}/activity/images/sign/actor/luxury/title-two.png">
            </div>
            <div class="step-list">
                <h5>活动期间，新用户在平台完成注册、实名认证、绑卡、充值、投资均可获得一次抽奖机会。</h5>
                <ul class="step-icon">
                    <li class="<#if steps[0] == 1>active</#if><#if steps[0] == 2>finished</#if>">
                        <div class="icon user-icon"></div>
                        <#if steps[0] == 1>
                            <div class="icon-text"><a href="/register/user">去注册</a></div></#if>
                        <#if steps[0] == 2>
                            <div class="icon-text">已注册</div></#if>
                    </li>
                    <li class="<#if steps[1] == 1>active</#if><#if steps[1] == 2>finished</#if>">
                        <div class="icon vali-icon"></div>
                        <#if steps[1] == 0>
                            <div class="icon-text">认证</div></#if>
                        <#if steps[1] == 1>
                            <div class="icon-text"><a href="/register/account">去认证</a></div></#if>
                        <#if steps[1] == 2>
                            <div class="icon-text">已认证</div></#if>
                        <div class="step-icon"></div>
                    </li>
                    <li class="<#if steps[2] == 1>active</#if><#if steps[2] == 2>finished</#if>">
                        <div class="icon card-icon"></div>
                        <#if steps[2] == 0>
                            <div class="icon-text">绑卡</div></#if>
                        <#if steps[2] == 1>
                            <div class="icon-text"><a href="/bind-card">去绑卡</a></div></#if>
                        <#if steps[2] == 2>
                            <div class="icon-text">已绑卡</div></#if>
                        <div class="step-icon"></div>
                    </li>
                    <li class="<#if steps[3] == 1>active</#if>">
                        <div class="icon money-icon"></div>
                        <#if steps[3] == 0>
                            <div class="icon-text">充值</div></#if>
                        <#if steps[3] == 1>
                            <div class="icon-text"><a href="/recharge">去充值</a></div></#if>
                        <div class="step-icon"></div>
                    </li>
                    <li class="<#if steps[4] == 1>active</#if>">
                        <div class="icon loan-icon"></div>
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
                <p>活动期间，每推荐一名好友注册也可获得一次抽奖机会；好友投资，还可再得一次抽奖机会。邀请越多机会越多。</p>
                <p>
                    <@global.isAnonymous>
                        <a href="/login?redirect=/activity/autumn/luxury" class="btn-invite">立即邀请好友赢抽奖机会</a>
                    </@global.isAnonymous>
                    <@global.isNotAnonymous>
                        <a href="/referrer/refer-list" class="btn-invite">立即邀请好友赢抽奖机会</a>
                    </@global.isNotAnonymous>
                </p>
            </div>
            <div class="phone-step">
                <p>
                    <img src="${commonStaticServer}/activity/images/sign/actor/luxury/intro-img.png" width="90%">
                </p>
                <p>
                    <@global.isAnonymous>
                        <a href="/login?redirect=/activity/autumn/luxury" class="btn-invite">立即邀请好友赢抽奖机会</a>
                    </@global.isAnonymous>
                    <@global.isNotAnonymous>
                        <a href="/referrer/refer-list" class="btn-invite">立即邀请好友赢抽奖机会</a>
                    </@global.isNotAnonymous>
                </p>
            </div>
            <div class="luxury-draw-theme">
                <#assign prizeType = 'luxury'/>
                <#include "gift-circle.ftl"/>
            </div>
        </div>
        <div class="luxury-item hack clearfix foot-last">
            <div class="actor-info-text">
                <div class="actor-title">
                    温馨提示
                </div>
                <p class="pc">
                    1、活动期间，用户在”我要投资“页面投资7%标的，即视为参与活动； <br/>
                    2、本活动仅计算当日的投资额，用户在当日24点之前进行的多次投资，金额可累计计算，次日全部清零；<br/>
                    3、本次活动期间，用户每人每天仅限量领取一个奢侈品奖品；<br/>
                    4、如用户在当日投资中获得领取奖品或折扣券资格，客服将于该债权放款后三个工作日内电话联系确认并发放奖品；<br/>
                    5、用户使用折扣券可以商品标价的对应折扣金额购买奢侈品，并采用货到付款的形式进行支付；<br/>
                    6、奢侈品奖品中奖或确认购买并由客服确认信息后，不可再行更改，部分地区邮费自付，如用户在签收时发现商品有质量问题请立即联系客服；<br/>
                    7、用户在抽奖活动中所获得的红包和加息券实时发放，可在“我的账户-我的宝藏”中查看；<br/>
                    8、由于本活动是为答谢广大用户所提供的回馈性质的服务，因此您在本活动中兑换的商品均不提供发票；<br/>
                    9、获取抽奖机会和抽奖过程中，如果出现恶意刷量等违规行为，拓天速贷将取消您获得奖励的资格，并有权撤销违规交易，收回活动中所得的奖品；<br/>
                    拓天速贷在法律范围内保留对本活动的最终解释权。<br/>
                    10、市场有风险，投资需谨慎。
                </p>

                <p class="nopc">
                    1、用户在”我要投资“页面投资7%标的，即视为参与活动； <br/>
                    2、本活动仅计算当日的投资额，次日全部清零；<br/>
                    3、用户每人每天仅限量领取一个奢侈品奖品；<br/>
                    4、如用户在当日投资中获得领取奖品或折扣券资格，客服将于该债权放款后三个工作日内电话联系确认并发放奖品；<br/>
                    5、用户在抽奖活动中所获得的红包和加息券实时发放，可在“我的账户-我的宝藏”中查看；<br/>
                    6、拓天速贷在法律范围内保留对本活动的最终解释权；<br/>
                    7、市场有风险，投资需谨慎。<br/>
                    详细活动规则请查看电脑网页版活动页面
                </p>

            </div>
        </div>
    </div>
</div>
</@global.main>