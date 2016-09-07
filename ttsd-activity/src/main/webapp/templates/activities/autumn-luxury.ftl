<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.autumn_luxury}" pageJavascript="${js.autumn_luxury}" activeNav="" activeLeftNav="" title="拓天奢品_奢品活动_拓天速贷" keywords="拓天大奖,大奖活动,投资活动,拓天速贷" description="拓天速贷奢华投资活动,海量奢品拓手可得,拓天大奖活动让您左手投资赚收益,右手白拿奢侈品.">
<@global.isNotAnonymous>
<div style="display: none" class="login-name" data-login-name='<@global.security.authentication property="principal.username" />'></div>
<div style="display: none" class="mobile" data-mobile='<@global.security.authentication property="principal.mobile" />'></div>
</@global.isNotAnonymous>
<div class="luxury-list-container">
    <div class="luxury-top-item">
        <img src="${staticServer}/activity/images/sign/actor/luxury/top-banner.png" width="100%" class="pc-item">
        <img src="${staticServer}/activity/images/sign/actor/luxury/phone-banner.png" width="100%" class="phone-item">
    </div>
    <div class="wp clearfix">
        <div class="left-bg"></div>
        <div class="right-bg"></div>
        <div class="luxury-item clearfix">
            <div class="title-one">
                <img src="${staticServer}/activity/images/sign/actor/luxury/title-one.png" width="100%">
            </div>
            <div class="title-info">
                <h3>
                    <img src="${staticServer}/activity/images/sign/actor/luxury/title-text.png">
                </h3>
                <p class="pc-item">小贴士：用户如想获取本次奢侈品活动奖励，请用户每次投资时，必须通过本页面中的“立即投资”按钮进行投资，才能参与活动哦！通过其他方式投资无法享受活动奖励。</p>
                <p class="phone-item">小贴士：您必须通过本页面中的“立即投资”按钮进行投资，才能参与活动哦！通过其他方式投资无法享受活动奖励。</p>
            </div>
            <div class="user-account">
                <i class="title-icon left-icon"></i>
                <i class="title-icon right-icon"></i>
                <div class="bg-item">
                    <div class="date-time">
                        <i class="time-icon"></i>
                        <span>${today?string('yyyy-MM-dd')}</span>
                    </div>
                    <@global.isNotAnonymous>
                        <div class="account-text">
                            <span class="user-text">我的投资：<strong>${myInvestAmount}元</strong></span>
                            <span class="user-tip">(仅累计本日参与本活动的投资金额)</span>
                        </div>
                    </@global.isNotAnonymous>
                    <@global.isAnonymous>
                        <a class="login-btn" href="/login?redirect=/activity/autumn/luxury">立即登录</a>
                    </@global.isAnonymous>
                </div>
            </div>
            <div class="product-item">
                <ul class="product-list clearfix">
                    <#list luxuryPrize as prize>
                        <li>
                            <div class="picture-item">
                                <div class="product-img">
                                    <#if !isAppSource>
                                    <a href="/activity/autumn/luxury/${prize.id?string.computer}/detail" target="_blank">
                                    <#else>
                                    <a href="javascript:void(0)">
                                    </#if>
                                    <img src="${prize.image}" width="100%">
                                </a>
                                </div>
                                <div class="product-info">
                                    <p class="name-text">${prize.brand}</p>
                                    <p class="des-text">${prize.name}</p>
                                    <p class="price-text"><span>${prize.price}元</span><span class="bite-text">商品价格</span></p>
                                    <p class="intro-text"><span>投资满<strong><@amount>${prize.investAmount?string.computer}</@amount></strong>元可获得</span></p>
                                    <p class="btn-text">
                                        <@global.isAnonymous>
                                            <a href="/login?redirect=/activity/autumn/luxury" class="autumn-luxury-invest-channel" onclick="cnzzPush.trackClick('209奢侈品活动页面','立即投资' +   ${prize_index + 1})">立即投资</a>
                                        </@global.isAnonymous>
                                        <@global.isNotAnonymous>
                                            <a href="/loan-list" class="autumn-luxury-invest-channel" onclick="cnzzPush.trackClick('209奢侈品活动页面','立即投资' + ${prize_index + 1})">立即投资</a>
                                        </@global.isNotAnonymous>

                                    </p>
                                </div>
                            </div>
                            <div class="coupon-list">
                                <div class="coupon-item">
                                    <div class="info-text">
                                        <span class="coupon-name">${prize.name}</span>
                                        <span class="count-number">7折券</span>
                                        <i class="top-circle"></i>
                                        <i class="bottom-circle"></i>
                                    </div>
                                    <div class="tip-text">
                                        投资满<span><@amount>${prize.thirtyPercentOffInvestAmount?string.computer}</@amount></span>元可获得
                                    </div>
                                </div>
                                <div class="coupon-item">
                                    <div class="info-text">
                                        <span class="coupon-name">${prize.name}</span>
                                        <span class="count-number">8折券</span>
                                        <i class="top-circle"></i>
                                        <i class="bottom-circle"></i>
                                    </div>
                                    <div class="tip-text">
                                        投资满<span><@amount>${prize.twentyPercentOffInvestAmount?string.computer}</@amount></span>元可获得
                                    </div>
                                </div>
                                <div class="coupon-item">
                                    <div class="info-text">
                                        <span class="coupon-name">${prize.name}</span>
                                        <span class="count-number">9折券</span>
                                        <i class="top-circle"></i>
                                        <i class="bottom-circle"></i>
                                    </div>
                                    <div class="tip-text">
                                        投资满<span><@amount>${prize.tenPercentOffInvestAmount?string.computer}</@amount></span>元可获得
                                    </div>
                                </div>
                            </div>
                        </li>
                    </#list>
                </ul>
                <div class="line-item"></div>
            </div>
            <div class="winer-record">
                <div class="record-list">
                    <h3>
                        <span class="active">获奖名单</span>
                        <@global.isNotAnonymous>
                            <span>我的获奖记录</span>
                        </@global.isNotAnonymous>
                    </h3>
                    <div class="record-item">
                        <div class="record-data active" id="rewardList">
                            <table>
                                <thead>
                                <tr>
                                    <th>用户</th>
                                    <th class="mobile-hide">投资金额(元)</th>
                                    <th>奖品</th>
                                    <th>获奖时间</th>
                                </tr>
                                </thead>
                                <tbody class="user-list">
                                    <#list userLuxuryPrize as item>
                                    <tr>
                                        <td>${item.mobile}</td>
                                        <td class="mobile-hide">${item.investAmount}</td>
                                        <td class="name-text" title="${item.prize}">${item.prize}</td>
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
                        <div class="record-data">
                            <table>
                                <thead>
                                <tr>
                                    <th>用户</th>
                                    <th class="mobile-hide">投资金额(元)</th>
                                    <th>奖品</th>
                                    <th>获奖时间</th>
                                </tr>
                                </thead>
                                <tbody>
                                    <#list myLuxuryPrize as item>
                                    <tr>
                                        <td>${item.mobile}</td>
                                        <td class="mobile-hide">${item.investAmount}</td>
                                        <td class="name-text" title="${item.prize}">${item.prize}</td>
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
                </div>
                <div class="code-item">
                    <p>如何获得<span>更多奢侈品</span>？码上教你</p>
                    <p>
                        <img src="${staticServer}/activity/images/sign/actor/luxury/luxury-code.jpg">
                    </p>
                    <p>扫码查看活动攻略</p>
                </div>
            </div>
            <div class="btn-item">
                <@global.isAnonymous>
                    <a href="/login?redirect=/activity/autumn/luxury" onclick="cnzzPush.trackClick('211奢侈品活动页面','立即投资领奖')">立即投资领奖</a>
                </@global.isAnonymous>
                <@global.isNotAnonymous>
                    <a href="/loan-list" class="autumn-luxury-invest-channel" onclick="cnzzPush.trackClick('211奢侈品活动页面','立即投资领奖')">立即投资领奖</a>
                </@global.isNotAnonymous>
            </div>
        </div>
        <div class="luxury-item clearfix">
            <div class="title-two">
                <img src="${staticServer}/activity/images/sign/actor/luxury/title-two.png" width="100%">
            </div>
            <div class="step-list">
                <h5>活动期间，新用户在平台完成注册、实名认证、绑卡、充值、投资均可获得一次抽奖机会。</h5>
                <ul class="step-icon">
                    <li class="<#if steps[0] == 1>active</#if><#if steps[0] == 2>finished</#if>">
                        <div class="icon user-icon"></div>
                        <#if steps[0] == 1>
                            <div class="icon-text"><a href="/register/user" onclick="cnzzPush.trackClick('212奢侈品活动页面','去注册')">去注册</a></div></#if>
                        <#if steps[0] == 2>
                            <div class="icon-text">已注册</div></#if>
                    </li>
                    <li class="<#if steps[1] == 1>active</#if><#if steps[1] == 2>finished</#if>">
                        <div class="icon vali-icon"></div>
                        <#if steps[1] == 0>
                            <div class="icon-text">认证</div></#if>
                        <#if steps[1] == 1>
                            <div class="icon-text"><a href="/register/account" onclick="cnzzPush.trackClick('213奢侈品活动页面','去认证')">去认证</a></div></#if>
                        <#if steps[1] == 2>
                            <div class="icon-text">已认证</div></#if>
                        <div class="step-icon"></div>
                    </li>
                    <li class="<#if steps[2] == 1>active</#if><#if steps[2] == 2>finished</#if>">
                        <div class="icon card-icon"></div>
                        <#if steps[2] == 0>
                            <div class="icon-text">绑卡</div></#if>
                        <#if steps[2] == 1>
                            <div class="icon-text"><a href="/bind-card" onclick="cnzzPush.trackClick('214奢侈品活动页面','去绑卡')">去绑卡</a></div></#if>
                        <#if steps[2] == 2>
                            <div class="icon-text">已绑卡</div></#if>
                        <div class="step-icon"></div>
                    </li>
                    <li class="<#if steps[3] == 1>active</#if>">
                        <div class="icon money-icon"></div>
                        <#if steps[3] == 0>
                            <div class="icon-text">充值</div></#if>
                        <#if steps[3] == 1>
                            <div class="icon-text"><a href="/recharge" onclick="cnzzPush.trackClick('215奢侈品活动页面','去充值')">去充值</a></div></#if>
                        <div class="step-icon"></div>
                    </li>
                    <li class="<#if steps[4] == 1>active</#if>">
                        <div class="icon loan-icon"></div>
                        <#if steps[4] == 0>
                            <div class="icon-text">投资</div></#if>
                        <#if steps[4] == 1>
                            <@global.isAnonymous>
                                <a href="/login?redirect=/activity/autumn/luxury" class="icon-text autumn-luxury-invest-channel" onclick="cnzzPush.trackClick('216奢侈品活动页面','去投资')">去投资</a>
                            </@global.isAnonymous>
                            <@global.isNotAnonymous>
                                <a href="/loan-list" class="icon-text autumn-luxury-invest-channel" onclick="cnzzPush.trackClick('216奢侈品活动页面','去投资')">去投资</a>
                            </@global.isNotAnonymous>
                        </#if>
                        <div class="step-icon"></div>
                    </li>
                </ul>
                <p>活动期间，每推荐一名好友注册也可获得一次抽奖机会；好友投资，还可再得一次抽奖机会。邀请越多机会越多。</p>
                <p>
                    <@global.isAnonymous>
                        <a href="/login?redirect=/activity/autumn/luxury" class="btn-invite" onclick="cnzzPush.trackClick('217奢侈品活动页面','立即邀请好友赢抽奖机会')">立即邀请好友赢抽奖机会</a>
                    </@global.isAnonymous>
                    <@global.isNotAnonymous>
                        <a href="/referrer/refer-list" class="btn-invite" onclick="cnzzPush.trackClick('217奢侈品活动页面','立即邀请好友赢抽奖机会')">立即邀请好友赢抽奖机会</a>
                    </@global.isNotAnonymous>
                </p>
            </div>
            <div class="phone-step">
                <p>
                    <img src="${staticServer}/activity/images/sign/actor/luxury/intro-img.png" width="90%">
                </p>
                <p>
                    <@global.isAnonymous>
                        <a href="/login?redirect=/activity/autumn/luxury" class="btn-invite" onclick="cnzzPush.trackClick('217奢侈品活动页面','立即邀请好友赢抽奖机会')">立即邀请好友赢抽奖机会</a>
                    </@global.isAnonymous>
                    <@global.isNotAnonymous>
                        <a href="/referrer/refer-list" class="btn-invite" onclick="cnzzPush.trackClick('217奢侈品活动页面','立即邀请好友赢抽奖机会')">立即邀请好友赢抽奖机会</a>
                    </@global.isNotAnonymous>
                </p>
            </div>
            <#assign prizeType = 'luxury'/>
            <#include "gift-circle.ftl"/>
        </div>
        <div class="luxury-item clearfix foot-last">
            <div class="actor-info-text">
                <div class="actor-title">
                    活动规则
                </div>
                <ul class="rule-list">
                    <li>1.本活动仅计算当日的投资额，用户在当日24点之前进行的多次投资，金额可累计计算，次日全部清零；</li>
                    <li>2.本次活动期间，用户每人每天仅限量领取一个奢侈品奖品；</li>
                    <li>3.当用户从当前页面进行投资时，当日所有投资均计为参加奢侈品活动所做的投资，如用户想参与旅游活动，必须通过旅游活动页面进行投资方可生效；</li>
                    <li>4.拓天速贷会根据活动的情况，以等值，增值为基础调整旅游产品；</li>
                    <li>5.用户在当日投资中如获得领取折扣券资格，客服将于次日联系用户确认购买意向，用户使用折扣券可以商品标价的对应折扣金额购买奢侈品，并采用货到付款的形式进行支付；</li>
                    <li>6.中奖结果将于次日在本活动页面公布，由客服联系确认。红包和加息券实时发放，用户可在“我的账户-我的宝藏”中查看，实物奖品将于活动结束后七个工作日内统一安排发放。部分地区邮费自付，详询客服；</li>
                    <li>7.奢侈品奖品中奖或确认购买并由客服确认信息后，不可再行更改。如用户在签收时发现商品有质量问题请立即联系客服；</li>
                    <li>8.由于本活动是为答谢广大用户所提供的回馈性质的服务，因此您在本活动中兑换的商品均不提供发票；</li>
                    <li>9.获取抽奖机会和抽奖过程中，如果出现恶意刷量等违规行为，拓天速贷将取消您获得奖励的资格，并有权撤销违规交易，收回活动中所得的奖品；</li>
                    <li>10.拓天速贷在法律范围内保留对本活动的最终解释权。</li>
                </ul>
                <ul class="phone-rule">
                    <li>1.本活动仅计算当日的投资额，次日全部清零；</li>
                    <li>2.用户每人每天仅限量领取一个奢侈品奖品；</li>
                    <li>3.用户从当前页面进行投资时，当日所有投资均计为参加奢侈品活动所做的投资，如用户想参与旅游活动，必须通过旅游活动页面进行投资方可生效；</li>
                    <li>4.中奖奖品，红包和加息券实时发放至“我的宝藏”当中，实物奖品将于活动结束后七个工作日内统一安排发放；</li>
                    <li>5.拓天速贷在法律范围内保留对本活动的最终解释权。</li>
                    <li>详细活动规则请查看电脑网页版活动页面。</li>
                </ul>
            </div>
        </div>
    </div>
</div>
</@global.main>