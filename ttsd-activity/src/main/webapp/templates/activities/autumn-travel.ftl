<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.autumn_travel}" pageJavascript="${js.autumn_travel}" activeNav="" activeLeftNav="" title="拓荒计划_旅游活动_拓天速贷" keywords="拓荒计划,免费旅游,旅游活动投资,拓天速贷" description="拓天速贷金秋拓荒计划,多条旅游线路免费玩,你旅游我买单,邀请好友注册也可获得免费旅游大奖.">
<div class="tour-slide">
</div>
<div class="autumn-tour-frame" id="pageone">
    <div class="vertical-line"></div>
    <div class="vertical-line"></div>
    <div class="vertical-line"></div>
    <div class="vertical-line"></div>
    <div class="vertical-line"></div>
    <div class="vertical-line"></div>
    <div class="vertical-line"></div>
    <div class="vertical-line"></div>
    <div class="bg-box">
        <div class="title-normal-box">
            <div class="title-normal-title01"></div>
            <div class="box-inner-content">
                <h1>活动期间投资满一定额度，即可获得当日对应旅游大奖</h1>
                <p>小贴士：用户如想获取本次旅游活动奖励，请用户每次投资时，必须通过本页面中的“立即投资”按钮进行投资，才能参与活动哦！通过其他方式投资无法享受活动奖励。</p>

            </div>
        </div>
        <div class="time-axis clearfix">
            <span class="fl date">${today?string('yyyy-MM-dd')}</span>
            <@global.isNotAnonymous>
                <span class="fr">我的投资额：${myInvestAmount}元<em class="note">［仅累计本日参与本活动的投资金额］</em></span>
            </@global.isNotAnonymous>
            <@global.isAnonymous>
                <a href="/login?redirect=/activity/autumn/travel" class="fr btn-normal">立即登录</a>
            </@global.isAnonymous>
        </div>

        <div class="prize-kind clearfix swiper-container">
            <div class="swiper-wrapper" id="sliderBox">
                <#list travelPrize as prize>
                    <div class="prize-box swiper-slide <#if prize_index == 1>active</#if>">
                        <div class="pk-title">投资满<span><@amount>${prize.investAmount?string.computer}</@amount></span>元即可获得</div>
                        <div class="img-info">
                            <a href="/activity/autumn/travel/${prize.id?string.computer}/detail" target="_blank"><img src="${prize.image}"></a>
                            <span class="kind-text">${prize.name}</span>
                            <div class="kind-bottom">
                                <span class="fl">商品价格<em>${prize.price}</em>元</span>
                                <@global.isAnonymous>
                                    <a href="/login?redirect=/activity/autumn/travel" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>
                                </@global.isAnonymous>
                                <@global.isNotAnonymous>
                                    <#if !isAppSource>
                                    <a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel" target="_blank">立即投资</a>
                                    <#else>
                                    <a href="/loan-list" class="fr btn-normal autumn-travel-invest-channel">立即投资</a>
                                    </#if>
                                </@global.isNotAnonymous>
                            </div>
                        </div>
                    </div>
                </#list>
                <div class="bg-shadow"></div>
            </div>
        </div>

        <div class="award-records clearfix" id="awardRecordsFrame">
            <div class="award-box fl">
                <ul class="award-category">
                    <li class="active"><span>获奖名单</span></li>
                    <@global.isNotAnonymous>
                        <li><span>我的获奖记录</span></li>
                    </@global.isNotAnonymous>
                </ul>
                <div class="winner-box switchContent">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>用户</th>
                            <th class="hideCol">投资金额(元)</th>
                            <th>奖品</th>
                            <th>获奖时间</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list userTravelPrize as item>
                            <tr>
                                <td>${item.mobile}</td>
                                <td class="hideCol">${item.investAmount}</td>
                                <td>${item.prize}</td>
                                <td>${item.createdTime?string('yyyy-MM-dd')}</td>
                            </tr>
                            <#else>
                            <tr>
                                <td colspan="4">暂无数据</td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div class="record-box switchContent" style="display: none;">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>用户</th>
                            <th class="hideCol">投资金额(元)</th>
                            <th>奖品</th>
                            <th>获奖时间</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list myTravelPrize as item>
                            <tr>
                                <td>${item.mobile}</td>
                                <td class="hideCol">${item.investAmount}</td>
                                <td>${item.prize}</td>
                                <td>${item.createdTime?string('yyyy-MM-dd')}</td>
                            </tr>
                            <#else>
                            <tr>
                                <td colspan="4">暂无数据</td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="award-box fr">
                <div class="scan-code">
                    <img src="${staticServer}/activity/images/autumn-tour/wx.png">
                <span>如何说走就走？ <br/>
                    码上教你<br/>
                    扫码查看活动攻略
                </span>
                </div>

            </div>
        </div>

        <div class="tc customized-button">
            <@global.isAnonymous>
                <a href="/login?redirect=/activity/autumn/travel" class="btn-normal autumn-travel-invest-channel">立即投资领奖</a>
            </@global.isAnonymous>
            <@global.isNotAnonymous>
                <a href="/loan-list" class="btn-normal autumn-travel-invest-channel">立即投资领奖</a>
            </@global.isNotAnonymous>
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
                <span class="pc">
                    <a href="/referrer/refer-list" class="btn-normal">立即邀请好友赢抽奖机会</a>
                    <br/>
                </span>
                <span class="mobile">
                    <a href="/referrer/refer-list" class="btn-normal">立即邀请好友赢抽奖机会</a>
                </span>
            </div>
        </div>

        <div class="lottery-draw fl">
            <#assign prizeType = 'travel'/>
            <#include "gift-circle.ftl"/>
        </div>

    </div>

    <div class="bg-box activity-rule">
        <b>活动规则</b>
        <span class="pc">
            1、本活动仅计算当日的投资额，用户在当日24点之前进行的多次投资，金额可累计计算，次日全部清零； <br/>
            2、本次活动期间，用户每人每天仅限量领取一个旅游奖品；<br/>
            3、当用户从当前页面进行投资时，当日所有投资均计为参加旅游活动所做的投资，如用户想参与奢侈品活动，必须通过奢侈品活动页面进行投资方可生效；<br/>
            4、拓天速贷会根据活动的情况，以等值，增值为基础调整旅游产品；<br/>
            5、中奖结果将于次日在本活动页面公布，由客服联系确认。红包和加息券实时发放，用户可在“我的账户-我的宝藏”中查看，实物奖品将于活动结束后七个工作日内统一安排发放；<br/>
            6、旅游奖品中奖并由客服确认信息后，不可再行更改。如无法按时参团请与旅行社接洽，旅途中相关问题及引起的不良后果与拓天速贷无关；<br/>
            7、旅游产品内容随淡旺季变化调整较为频繁，网页中的产品介绍、行程安排等图片及文字信息仅供参考，最终产品内容以用户和旅行社的签约合同为准；<br/>
            8、获取抽奖机会和抽奖过程中，如果出现恶意刷量等违规行为，拓天速贷将取消您获得奖励的资格，并有权撤销违规交易，收回活动中所得的奖品；<br/>
            9、拓天速贷在法律范围内保留对本活动的最终解释权。<br/>
        </span>
        <span class="mobile">
            1.本活动仅计算当日的投资额，次日全部清零；<br/>
            2.用户每人每天仅限量领取一个旅游奖品；<br/>
            3.用户从当前页面进行投资时，其所有投资均计为参加旅游活动所做的投资，如用户想参与奢侈品活动，必须通过奢侈品活动页面进行投资方可生效；<br/>
            4.中奖奖品，红包和加息券实时发放至“我的宝藏”当中，实物奖品将于活动结束后七个工作日内统一安排发放；<br/>
            5.拓天速贷在法律范围内保留对本活动的最终解释权。<br/>
            详细活动规则请查看电脑网页版活动页面。
        </span>
    </div>
</div>
</@global.main>


