<#import "macro/global.ftl" as global>

<@global.main pageCss="${css.membership}" pageJavascript="${js.membership}" activeNav="我的会员" activeLeftNav="" title="我的会员_会员福利_拓天速贷" keywords="拓天会员,拓天会员积分,拓天保障,拓天速贷" description="拓天速贷会员中心为您提供会员专享投资顾问,会员专属特权,为广大投资用户提供丰富的投资福利." site="membership">

<div class="global-member-ship">
    <#if mobile??>
        <div class="user-info-block page-width">
            <#if membershipType == 'GIVEN' || membershipType == "PURCHASED" >
                <span class="date-time-item">有效期至：${expiredDate?string("yyyy-MM-dd")!}</span>
            </#if>
            <div class="info clearfix">
                <div class="avatar fl">
                    <span class="icon-avatar"></span>
                    <#if membershipType == 'GIVEN' || membershipType == "PURCHASED" >
                        <i class="vip-no-bg vip-5"></i>
                    <#else>
                        <i class="vip-no-bg vip-${membershipLevel!}"></i>
                    </#if>

                </div>
                <div class="text">
                    <p><span class="orange font20">您好！${mobile!}</span>
                        <#if membershipType == 'GIVEN' || membershipType == 'PURCHASED'>
                            <span class="font14">会员有效期还有：<strong
                                    class="font22">${(leftDays-1)?string('0')}</strong>天</span>
                        </#if>
                    </p>
                    <p class="font14">我的成长值：<strong class="font22">${membershipPoint!}</strong></p>
                </div>
            </div>
            <div class="progress">
                <div class="progress-bar">
                    <div class="progress-bar-fill" style="width: ${progressBarPercent!}%"></div>
                    <div class="vip-bg vip-0 <#if membershipLevel == 0>active</#if>"></div>
                    <div class="vip-bg vip-1 <#if membershipLevel == 1>active</#if>"></div>
                    <div class="vip-bg vip-2 <#if membershipLevel == 2>active</#if>"></div>
                    <div class="vip-bg vip-3 <#if membershipLevel == 3>active</#if>"></div>
                    <div class="vip-bg vip-4 <#if membershipLevel == 4>active</#if>"></div>
                    <div class="vip-bg vip-5 <#if membershipLevel == 5>active</#if>"></div>
                    <#if membershipType == 'UPGRADE'>
                        <#if membershipNextLevel <= 5 && membershipLevel != 5>
                            <div class="popup popup-${membershipNextLevel!}">
                                还需<strong>${membershipNextLevelValue!}</strong>成长值就能尊享<i
                                    class="vip-no-bg vip-${membershipNextLevel!}"></i>特权了哦！<i class="triangle"></i></div>
                        </#if>
                    </#if>
                    <#if membershipType == 'GIVEN' || membershipType == "PURCHASED" >
                        <div class="popup popup-5">您已成为<i class="vip-no-bg vip-5"></i>会员,尊享100%会员特权！<i class="triangle"></i></div>
                    </#if>
                </div>
            </div>
        </div>

        <div class="my-level">
            特权多多,稳赚收益 <a href="/loan-list">马上去投资></a>
        </div>

        <div class="levels">
            <div class="main-title">
                <div class="inner">
                    <h2>我的特权</h2>
                </div>
            </div>
            <div class="levels-list">
                <ul class="clearfix">

                    <#if membershipLevel == 0>
                        <li class="multi-ensuring">
                            <h3>多重保障</h3>

                            <p>三方托管、抵押模式、严格风控体系、平台信息透明、数据信息安全等保障</p>
                        </li>
                        <li class="anytime-withdraw">
                            <h3>随时提现</h3>

                            <p>24小时随时提现，16:00点前提现当日到账，16:00后提现次日到账</p>
                        </li>
                        <li class="birthday-privilege">
                            <h3>生日特权</h3>

                            <p>生日月投资，投90天产品首期收益翻1.5倍，投180天、360天产品首期收益翻2倍</p>
                        </li>
                        <li class="membership-giftbag-hui">
                            <h3>会员礼包</h3>

                            <p>每月发放投资红包，588、688、788、888元随机派送</p>
                        </li>
                        <li class="service-fee-discount-hui">
                            <h3>技术服务费</h3>

                            <p>平台向V0、V1会员收取收益的10%作为服务费，V2收取9%，V3、V4收取8%，V5仅收取7%</p>
                        </li>
                        <li class="vip-service-hui">
                            <h3>贵宾专线</h3>

                            <p>贵宾级客服服务，投资问题，意见建议专享直达</p>
                        </li>
                        <li class="spec-financial-advisor-hui">
                            <h3>专享投资顾问</h3>

                            <p>发标时间，平台活动，投资顾问第一时间通知到您</p>
                        </li>
                        <li class="birthday-benefits-hui">
                            <h3>生日福利</h3>

                            <p>V5专享，平台将会在会员生日时送上神秘礼包</p>
                        </li>
                    <#elseif membershipLevel == 1>
                        <li class="multi-ensuring">
                            <h3>多重保障</h3>

                            <p>三方托管、抵押模式、严格风控体系、平台信息透明、数据信息安全等保障</p>
                        </li>
                        <li class="anytime-withdraw">
                            <h3>随时提现</h3>

                            <p>24小时随时提现，16:00点前提现当日到账，16:00后提现次日到账</p>
                        </li>
                        <li class="birthday-privilege">
                            <h3>生日特权</h3>

                            <p>生日月投资，投90天产品首期收益翻1.5倍，投180天、360天产品首期收益翻2倍</p>
                        </li>
                        <li class="membership-giftbag">
                            <h3>会员礼包</h3>

                            <p>每月发放投资红包，588、688、788、888元随机派送</p>
                        </li>
                        <li class="service-fee-discount-hui">
                            <h3>技术服务费</h3>

                            <p>平台向V0、V1会员收取收益的10%作为服务费，V2收取9%，V3、V4收取8%，V5仅收取7%</p>
                        </li>
                        <li class="vip-service-hui">
                            <h3>贵宾专线</h3>

                            <p>贵宾级客服服务，投资问题，意见建议专享直达</p>
                        </li>
                        <li class="spec-financial-advisor-hui">
                            <h3>专享投资顾问</h3>

                            <p>发标时间，平台活动，投资顾问第一时间通知到您</p>
                        </li>
                        <li class="birthday-benefits-hui">
                            <h3>生日福利</h3>

                            <p>V5专享，平台将会在会员生日时送上神秘礼包</p>
                        </li>
                    <#elseif membershipLevel == 2>
                        <li class="multi-ensuring">
                            <h3>多重保障</h3>

                            <p>三方托管、抵押模式、严格风控体系、平台信息透明、数据信息安全等保障</p>
                        </li>
                        <li class="anytime-withdraw">
                            <h3>随时提现</h3>

                            <p>24小时随时提现，16:00点前提现当日到账，16:00后提现次日到账</p>
                        </li>
                        <li class="birthday-privilege">
                            <h3>生日特权</h3>

                            <p>生日月投资，投90天产品首期收益翻1.5倍，投180天、360天产品首期收益翻2倍</p>
                        </li>
                        <li class="membership-giftbag">
                            <h3>会员礼包</h3>

                            <p>每月发放投资红包，588、688、788、888元随机派送</p>
                        </li>
                        <li class="service-fee-discount">
                            <h3>技术服务费</h3>

                            <p>平台向V0、V1会员收取收益的10%作为服务费，V2收取9%，V3、V4收取8%，V5仅收取7%</p>
                        </li>
                        <li class="vip-service-hui">
                            <h3>贵宾专线</h3>

                            <p>贵宾级客服服务，投资问题，意见建议专享直达</p>
                        </li>
                        <li class="spec-financial-advisor-hui">
                            <h3>专享投资顾问</h3>

                            <p>发标时间，平台活动，投资顾问第一时间通知到您</p>
                        </li>
                        <li class="birthday-benefits-hui">
                            <h3>生日福利</h3>

                            <p>V5专享，平台将会在会员生日时送上神秘礼包</p>
                        </li>
                    <#elseif membershipLevel == 3>
                        <li class="multi-ensuring">
                            <h3>多重保障</h3>

                            <p>三方托管、抵押模式、严格风控体系、平台信息透明、数据信息安全等保障</p>
                        </li>
                        <li class="anytime-withdraw">
                            <h3>随时提现</h3>

                            <p>24小时随时提现，16:00点前提现当日到账，16:00后提现次日到账</p>
                        </li>
                        <li class="birthday-privilege">
                            <h3>生日特权</h3>

                            <p>生日月投资，投90天产品首期收益翻1.5倍，投180天、360天产品首期收益翻2倍</p>
                        </li>
                        <li class="membership-giftbag">
                            <h3>会员礼包</h3>

                            <p>每月发放投资红包，588、688、788、888元随机派送</p>
                        </li>
                        <li class="service-fee-discount">
                            <h3>技术服务费</h3>

                            <p>平台向V0、V1会员收取收益的10%作为服务费，V2收取9%，V3、V4收取8%，V5仅收取7%</p>
                        </li>
                        <li class="vip-service">
                            <h3>贵宾专线</h3>

                            <p>贵宾级客服服务，投资问题，意见建议专享直达</p>
                        </li>
                        <li class="spec-financial-advisor-hui">
                            <h3>专享投资顾问</h3>

                            <p>发标时间，平台活动，投资顾问第一时间通知到您</p>
                        </li>
                        <li class="birthday-benefits-hui">
                            <h3>生日福利</h3>

                            <p>V5专享，平台将会在会员生日时送上神秘礼包</p>
                        </li>
                    <#elseif membershipLevel == 4>
                        <li class="multi-ensuring">
                            <h3>多重保障</h3>

                            <p>三方托管、抵押模式、严格风控体系、平台信息透明、数据信息安全等保障</p>
                        </li>
                        <li class="anytime-withdraw">
                            <h3>随时提现</h3>

                            <p>24小时随时提现，16:00点前提现当日到账，16:00后提现次日到账</p>
                        </li>
                        <li class="birthday-privilege">
                            <h3>生日特权</h3>

                            <p>生日月投资，投90天产品首期收益翻1.5倍，投180天、360天产品首期收益翻2倍</p>
                        </li>
                        <li class="membership-giftbag">
                            <h3>会员礼包</h3>

                            <p>每月发放投资红包，588、688、788、888元随机派送</p>
                        </li>
                        <li class="service-fee-discount">
                            <h3>技术服务费</h3>

                            <p>平台向V0、V1会员收取收益的10%作为服务费，V2收取9%，V3、V4收取8%，V5仅收取7%</p>
                        </li>
                        <li class="vip-service">
                            <h3>贵宾专线</h3>

                            <p>贵宾级客服服务，投资问题，意见建议专享直达</p>
                        </li>
                        <li class="spec-financial-advisor">
                            <h3>专享投资顾问</h3>

                            <p>发标时间，平台活动，投资顾问第一时间通知到您</p>
                        </li>
                        <li class="birthday-benefits-hui">
                            <h3>生日福利</h3>

                            <p>V5专享，平台将会在会员生日时送上神秘礼包</p>
                        </li>
                    <#elseif membershipLevel == 5>
                        <li class="multi-ensuring">
                            <h3>多重保障</h3>

                            <p>三方托管、抵押模式、严格风控体系、平台信息透明、数据信息安全等保障</p>
                        </li>
                        <li class="anytime-withdraw">
                            <h3>随时提现</h3>

                            <p>24小时随时提现，16:00点前提现当日到账，16:00后提现次日到账</p>
                        </li>
                        <li class="birthday-privilege">
                            <h3>生日特权</h3>

                            <p>生日月投资，投90天产品首期收益翻1.5倍，投180天、360天产品首期收益翻2倍</p>
                        </li>
                        <li class="membership-giftbag">
                            <h3>会员礼包</h3>

                            <p>每月发放投资红包，588、688、788、888元随机派送</p>
                        </li>
                        <li class="service-fee-discount">
                            <h3>技术服务费</h3>

                            <p>平台向V0、V1会员收取收益的10%作为服务费，V2收取9%，V3、V4收取8%，V5仅收取7%</p>
                        </li>
                        <li class="vip-service">
                            <h3>贵宾专线</h3>

                            <p>贵宾级客服服务，投资问题，意见建议专享直达</p>
                        </li>
                        <li class="spec-financial-advisor">
                            <h3>专享投资顾问</h3>

                            <p>发标时间，平台活动，投资顾问第一时间通知到您</p>
                        </li>
                        <li class="birthday-benefits">
                            <h3>生日福利</h3>

                            <p>V5专享，平台将会在会员生日时送上神秘礼包</p>
                        </li>
                    <#else>
                        <i class="icon multi-ensuring"></i>
                        <i class="icon anytime-withdraw"></i>
                        <i class="icon service-fee-discount"></i>
                    </#if>



                </ul>
            </div>
        </div>

        <div class="buy-vip">
            <div class="main-title">
                <div class="inner">
                    <h2>会员购买</h2>
                </div>
            </div>
            <div class="buy-vip-list">
                <table>
                    <thead>
                    <tr>
                        <th>会员购买</th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>有效期</td>
                        <td>1个月</td>
                        <td>1年</td>
                    </tr>
                    <tr>
                        <td>价格</td>
                        <td>25元</td>
                        <td>180元</td>
                    </tr>
                    </tbody>
                </table>
                <p class="text-info">购买渠道即将开放，敬请期待！</p>
            </div>
        </div>
    <#else>
        <div class="user-info-block page-width no-login">
            <div class="info clearfix">
                <div class="avatar fl">
                    <span class="icon-avatar"></span>
                </div>
                <div class="text">
                    亲，成为会员可享受多种特权哦~ <br/>
                    了解更多请 <a href="/login" class="btn-normal show-login">登录</a>
                </div>
            </div>
            <div class="progress">
                <div class="progress-bar" style="margin-top: 45px;">
                    <div class="vip-bg vip-0"></div>
                    <div class="vip-bg vip-1"></div>
                    <div class="vip-bg vip-2"></div>
                    <div class="vip-bg vip-3"></div>
                    <div class="vip-bg vip-4"></div>
                    <div class="vip-bg vip-5"></div>
                    <div class="popup-number vip-0">0</div>
                    <div class="popup-number vip-1">5000</div>
                    <div class="popup-number vip-2">50,000</div>
                    <div class="popup-number vip-3">300,000</div>
                    <div class="popup-number vip-4">1,500,000</div>
                    <div class="popup-number vip-5">5,000,000</div>
                </div>
            </div>
            <div class="register">
                新用户请 <a href="/register/user">注册</a>
            </div>
        </div>

        <div class="my-level">
            特权多多,稳赚收益 <a href="/loan-list">去投资></a>
        </div>

        <div class="levels">
            <div class="main-title">
                <div class="inner">
                    <h2>会员福利</h2>
                </div>
            </div>
            <div class="levels-list">
                <ul class="clearfix">
                    <li class="multi-ensuring">
                        <h3>多重保障</h3>

                        <p>三方托管、抵押模式、严格风控体系、平台信息透明、数据信息安全等保障</p>
                    </li>
                    <li class="anytime-withdraw">
                        <h3>随时提现</h3>

                        <p>24小时随时提现，16:00点前提现当日到账，16:00后提现次日到账</p>
                    </li>
                    <li class="birthday-privilege">
                        <h3>生日特权</h3>

                        <p>生日月投资，投90天产品首期收益翻1.5倍，投180天、360天产品首期收益翻2倍</p>
                    </li>
                    <li class="membership-giftbag">
                        <h3>会员礼包</h3>

                        <p>每月发放投资红包，588、688、788、888元随机派送</p>
                    </li>
                    <li class="service-fee-discount">
                        <h3>技术服务费</h3>

                        <p>平台向V0、V1会员收取收益的10%作为服务费，V2收取9%，V3、V4收取8%，V5仅收取7%</p>
                    </li>
                    <li class="vip-service">
                        <h3>贵宾专线</h3>

                        <p>贵宾级客服服务，投资问题，意见建议专享直达</p>
                    </li>
                    <li class="spec-financial-advisor">
                        <h3>专享投资顾问</h3>

                        <p>发标时间，平台活动，投资顾问第一时间通知到您</p>
                    </li>
                    <li class="birthday-benefits">
                        <h3>生日福利</h3>

                        <p>V5专享，平台将会在会员生日时送上神秘礼包</p>
                    </li>
                </ul>
            </div>
        </div>

        <div class="level-table">
            <div class="main-title">
                <div class="inner">
                    <h2>会员特权</h2>
                </div>
            </div>
            <div class="table">
                <table>
                    <thead>
                    <tr>
                        <th>特权</th>
                        <th><i class="vip-badge vip-0"></i></th>
                        <th><i class="vip-badge vip-1"></th>
                        <th><i class="vip-badge vip-2"></th>
                        <th><i class="vip-badge vip-3"></th>
                        <th><i class="vip-badge vip-4"></th>
                        <th><i class="vip-badge vip-5"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>多重保障</td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                    </tr>
                    <tr>
                        <td>随时提现</td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                    </tr>
                    <tr>
                        <td>生日特权</td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                    </tr>
                    <tr>
                        <td>会员礼包</td>
                        <td></td>
                        <td></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                    </tr>
                    <tr>
                        <td>技术服务费（折后费率）</td>
                        <td>10%（基础费率）</td>
                        <td>10%（基础费率）</td>
                        <td>9%</td>
                        <td>8%</td>
                        <td>8%</td>
                        <td>7%</td>
                    </tr>
                    <tr>
                        <td>贵宾专线</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                    </tr>
                    <tr>
                        <td>专享投资顾问</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                    </tr>
                    <tr>
                        <td>生日福利</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td><i class="fa fa-check-circle-o" aria-hidden="true"></i></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </#if>
</div>
</@global.main>