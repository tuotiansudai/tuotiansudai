<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.third_anniversary_2018}" pageJavascript="${js.third_anniversary_2018}" activeNav="" activeLeftNav="" title='拓天速贷3周年庆_集世界杯队标_平分现金奖池' keywords="拓天速贷,8888元现金奖池,888世界杯奖池,阴历阳历生日之争,生日红包" description='拓天速贷3周年庆,集世界杯队标,平分现金奖池,阴历阳历生日之争可获得其活动期间累计投资额(年化)的0.5%-0.8%返现奖励,分享专享现金红包给好友,还可获得其活动期间累计投资额(年化)的0.1%-0.5%返现奖励.'>
<div class="july-banner">
</div>
<div class="july-container">
    <div class="part-wrap-line">
        <div class="part team-logo-wrap">
            <div class="part-top team-logo-title"></div>
            <div class="main-content borderYellow">
                <div class="content-section team-logo-rules content-page">
                    <div class="content-title time1">活动时间：7月1日-7月15日</div>
                    <div class="fonts">
                        <p>活动期间，<strong>每日登录</strong>拓天速贷，可获得一次<strong>“开球”</strong>机会，足球内含世界杯<strong>32个队标</strong>；
                        </p>

                        <p> 活动期间，累计投资<strong>每满500元</strong>，可再获得一次<strong>“开球”</strong>机会，如投资5000元，可获10次抽奖机会，上不封顶；</p>

                        <p class="last">活动结束后，所有<strong>集齐</strong>世界杯<strong>四强</strong>队标的用户，可平分<strong>8888元</strong>现金奖池。
                        </p>
                    </div>
                </div>
                <div id="openBallContent" class="content-section open-balls-content content-page marginTop40"
                     data-drawcount="${drawCount?c}">
                    <div class="big-ball"></div>
                    <a id="toInvestBtn" href="/loan-list" class="gold-btn to_invest_btn" style="display: none"></a>
                    <a href="javascript:;" class="gold-btn open-ball-btn" id="openBall" style="display: none"></a>

                    <@global.isNotAnonymous>
                        <h2 class="my-logo  <#if isSuccess>collected-all</#if>" style="display: none">我的队标</h2>
                        <div class="my-team-logos logos-con swiper-container" id="teamLogos">
                            <ul class="swiper-wrapper" id="myTeamLogos">
                                <script id="myTeamLogoTpl" type="text/template">
                                    {{if list}}
                                    {{each list as value index}}
                                    <li class="swiper-slide">
                                        <div class="team-logo {{value.teamName}}"><span class="amount">{{value.teamCount}}</span>
                                        </div>
                                        <br/>
                                        <div class="country-btn">{{teamName[value.teamName]}}</div>
                                    </li>
                                    {{/each}}
                                    {{/if}}
                                </script>
                            </ul>
                            <div class="swiper-pagination"></div>
                        </div>
                    </@global.isNotAnonymous>
                    <div class="prize">
                        <#if topFour??>
                            <p>世界杯四强是<strong>${topFour}</strong>，当前共有<strong>${collectSuccess}人集齐队标</strong></p>
                        </#if>
                    </div>
                </div>
            </div>

        </div>
    </div>
<#--3岁生日怎么过-->
    <div class="part-wrap-line">
        <div class="part part-two">
            <div class="part-top"></div>
            <div class="main-content borderYellow">
                <div class="content-section three-birthday content-page">
                    <div class="content-title time2">活动时间：7月1日-7月31日</div>
                    <div class="invest-records">
                        <@global.isNotAnonymous>
                            <ul class="clearfix">
                                <li class="first">当前累计年化<span class="wap-style">投资额 <strong><em
                                        id="myAmount"></em>元</strong></span></li>
                                <li class="second">当前返现<span class="wap-style">比例 <strong
                                        id="currentRate"></strong> </span></li>
                                <li class="thrid">当前返现<span class="wap-style">金额  <strong><em
                                        id="currentAward"></em>元</strong></span></li>
                            </ul>
                        </@global.isNotAnonymous>
                        <div class="progress-wrap">
                            <div class="progress">
                                <div class="percent"><span class="percent-left"></span><span class="percent-con"
                                                                                             style="width: 80%"><em
                                        class="ball"></em></span></div>
                            </div>
                            <div class="invest-money clearfix">
                                <div class="fl"><em id="redAmount"></em>元</div>
                                <div class="fr"><em id="blueAmount"></em>元</div>
                            </div>
                            <div class="support-amout clearfix">
                                <a class="support-btn red-square fl" href="javascript:;"><span>（<em id="redCount"></em>人）</span></a>
                                <a class="support-btn blue-square fr" href="javascript:;"><span>（<em
                                        id="blueCount"></em>人）</span></a>
                            </div>
                        </div>
                    </div>

                    <div class="which-to-chose">阳历生日&阴历生日，你过哪个？</div>
                    <div class="red-and-blue-rules">
                        <p> 红方代表过阳历生日，蓝方代表过阴历生日，选择您所支持的阵营并进行投资，活动结束后累计双方投资额（年化），投资额（年化）较大的为赢方，较小的为输方；</p>

                        <p> 赢方所有参与成员可获得其活动期间累计投资额（年化）的0.8%返现奖励，输方所有成员可获得其活动期间累计投资额（年化）的0.5%返现奖励。</p>
                    </div>
                    <div class="invest-wrap">
                        <a class="gold-btn to_invest_btn" href="/loan-list"></a>
                    </div>


                </div>
            </div>
        </div>
    </div>
<#--邀好友拆红包，双方有惊喜-->
    <div class="part-wrap-line">
        <div class="part part-three">
            <div class="part-top"></div>
            <div class="main-content borderYellow">
                <div class="content-section content-page">
                    <div class="content-title time2">活动时间：7月1日-7月31日</div>
                    <div class="inveite-friend-desc">
                        <p>1、活动期间，用户投资后，<strong>微信扫描</strong>下方二维码，进入拓天速贷服务号，回复口令<strong>“拆红包”</strong>，可分享专属现金红包给好友，邀请好友领取；
                        </p>
                        <p> 2、根据累计领取好友人数，该投资用户可享受其活动期间累计投资额（年化）的<strong>0.1%-0.5%返现</strong>奖励；</p>

                        <div class="open-red-ware-tab">
                            <table>
                                <tr>
                                    <td class="ren">拆红包人数</td>
                                    <td>1</td>
                                    <td>2</td>
                                    <td>3</td>
                                </tr>
                                <tr>
                                    <td>投资返现比率（年化）</td>
                                    <td>0.1%</td>
                                    <td>0.2%</td>
                                    <td>0.5%</td>
                                </tr>
                            </table>
                        </div>
                        <p>3. 活动期间，帮投资用户拆红包的好友，可平分与投资用户等额的现金奖励；</p>
                        <p>4. 拆红包有效期：用户分享成功后72小时之内。</p>
                        <div class="qrcode qrcode-pc">
                            <img id="qrcodeImg" src="" alt="">
                        </div>
                        <div class="qrcpde-tip"></div>
                    </div>
                </div>
            </div>

        </div>
    </div>
    <#--温馨提示-->
    <div class="part-wrap-line">
        <div class="part part-four">
            <div class="part-top">
            </div>
            <div class="main-content borderYellow">
                <div class="content-section content-page ">
                    <p>1、阴历阳历生日之争活动，用户在活动期间必须在活动页面点选所支持的阵营，方视为成功参与活动并获得0.5%-0.8%返现奖励，如未点选阵营，将无法获得活动发放的返现奖励；</p>
                    <p>2、集世界杯队标，瓜分8888元现金奖励将于7月16日-7月18日期间发放至用户账户，可直接用于提现；</p>
                    <p>3、阴历阳历生日之争所获返现奖励，将于活动结束后3个工作日内统一发放至用户账户；</p>
                    <p>4、邀好友拆红包活动，拆开红包的好友需未注册过拓天速贷账户，并在活动页面完成注册+实名认证，实名认证后方可拆开红包；</p>
                    <p>5、邀好友拆红包活动，拆红包流程：点击活动页面的“拆红包”按钮-注册拓天速贷账户-完成实名认证-拆红包成功；</p>
                    <p>6、邀好友拆红包活动，投资用户及其好友的现金红包金额，将于分享成功后72小时内发放至双方用户账户，您可以直接提现；</p>
                    <p>7、截止发放时间为止，如果拆开红包的好友未登录拓天速贷并进行实名认证，双方将无法收到现金；</p>
                    <p>8、本活动仅限直投项目，债权转让、体验项目及新手专享项目不参与累计；</p>
                    <p>9、年化投资额计算公式</p>
                    <div class="calculation-formula">
                        <table>
                            <tr>
                                <td>60天-90天项目</td>
                                <td>年化投资额=实际投资额*90/360</td>
                            </tr>
                            <tr>
                                <td>120天-180天项目</td>
                                <td>年化投资额=实际投资额*180/360</td>
                            </tr>
                            <tr>
                                <td>330天-360天项目</td>
                                <td>年化投资额=实际投资额</td>
                            </tr>
                        </table>
                    </div>
                    <p>10、活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</p>
                    <p> 11、活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</p>
                </div>
            </div>

        </div>
    </div>
    <div class="tip-wrap" style="display: none">
        <div class="get-ball-tip">
            <div class="top"></div>
            <div class="bot"><span class="money"></span></div>
            <a class="known-btn" href="javascript:;"></a>
            <div class="con logos-con">
                <ul class="clearfix" id="getLogos">
                    <script id="getLogoTpl" type="text/template">
                        {{if list}}
                        {{each list as value index}}
                        <li>
                            <div class="team-logo {{value.teamName}}"><span
                                    class="amount">{{value.teamCount}}</span></div>
                            <br/>
                            <div class="country-btn">{{teamName[value.teamName]}}</div>
                        </li>
                        {{/each}}
                        {{/if}}
                    </script>
                </ul>
            </div>

        </div>
    </div>
    <#include "../../module/login-tip.ftl" />
</@global.main>