<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.christmas_day}" pageJavascript="${js.christmas_day}" activeNav="" activeLeftNav="" title="圣诞节活动_活动中心_拓天速贷" keywords="圣诞礼物,圣诞节投资,拓天活动,拓天速贷" description="拓天速贷圣诞节活动帮圣诞老人投资铺路,圣诞节投资可享1%加息券,并按投资比例瓜分20000元红包大奖,还可领取圣诞礼物.">
    <@global.isNotAnonymous>
    <div style="display: none" class="login-name" data-login-name='<@global.security.authentication property="principal.username" />'></div>
    <div style="display: none" class="mobile" id="MobileNumber" data-mobile='<@global.security.authentication property="principal.mobile" />'></div>
    </@global.isNotAnonymous>
<div class="activity-slide"></div>
<div class="christmas-day-frame" id="christmasDayFrame">
    <div class="christmas-day-inner">
        <div class="christmas-border box-notice">
            <div class="man-decorate"></div>
            <div class="model-content">
                <span class="text-note">
                    据不可靠消息称： <br/>
圣诞老人在天朝送礼物途中因雾霾爆表迷路了！<br/>
可孩子们不能没有童年<br/>
请各位大大们帮圣诞老人铺条路，让孩子们在圣诞节来临时能准时收到圣诞老人带来的丰厚礼物吧
                </span>
            </div>
        </div>

        <div class="activity-section-one">
            <div class="christmas-border clearfix">
                <div class="title-christmas">活动一：为圣诞老人铺路</div>
                <div class="model-content clearfix">
                        <div class="gift-box">
                            <em class="icon-balloon">1重礼</em>
                            <b>专享标加息0.5%</b>
                            专享标在现有预期年化收益基础上<i>加息0.5%</i>。
                        </div>

                        <div class="gift-box">
                            <em class="icon-balloon">2重礼</em>
                            <b>用券投资加息1%</b>
                            投资圣诞专享标单笔达3万元及以上，即赠送一张0.5%加息券，用券投资专享标<i>加息1%</i>。
                        </div>

                        <div class="gift-box">
                            <em class="icon-balloon">3重礼</em>
                            <b>瓜分20000元红包</b>
                            专享标总额420万，活动结束后，所有投资专享标的用户按投资比例瓜分<i>20000元红包</i>。
                        </div>

                        <div class="gift-box clearfix last">
                        <span>
                            栗子：沈先生预计投资20万，第一笔投资专享标3万元加息0.5%，第二笔使用0.5%加息券投资专享标17万元，可加息1%。同时可获得红包200000*20000/4200000=952元；<br/>
                            小贴士：<br/>
                            用户所获红包金额=活动期间投资专享标金额*20000/3000000。加息券每人限领1张，有效期3天；
                        </span>
                        </div>
                        <div class="clearfix"></div>
                        <dl class="invest-percent-box clearfix">
                            <dt>目前专享标共投资：<em class="total-invest">${allInvestAmount}</em>元</dt>
                            <dd class="progress-bar">
                                <#--<i class="progress-end" style="left: calc( 80% - 57px);"></i>-->
                                <div class="progress-percent" style="width: ${investScale}">
                                    <div class="progress-value"></div>
                                </div>
                            </dd>
                            <dd class="count-total">
                                <span class="fl">已有<em>${userCount}</em>人参与</span>
                                <span class="fr">总额度：<em>4,200,000元</em></span>
                            </dd>

                        </dl>
                        <div class="tc">
                            <a href="/loan-list" class="normal-button-day" target="_blank">马上投资圣诞专享标</a>
                        </div>
                        <div class="gift-box clearfix last">
                        <span>
                            活动说明：<br/>
1、用户投资专享标所获红包将于活动结束后三个工作日内，以红包形式发放，用户可在“我的宝藏”中查看，该红包适用于180天及以上标的，投资50元即可激活；<br/>
2、为了保证获奖结果的公平性，获奖用户在活动期间所进行的圣诞专享标投标不允许进行债权转让。<br/>
                        </span>
                        </div>
                </div>
            </div>
        </div>

        <div class="activity-section-two">
        <#--活动二未开始-->
        <#if isStart == 0>
            <div class="christmas-border clearfix">
                <div class="title-christmas">活动二：圣诞拆礼物</div>
                <div class="model-content clearfix">
                    <div class="tc color-sec margin-top25">
                        专享标总投资额达到420万，将开启活动二 —— 圣诞拆礼物。快去投资吧！
                    </div>
                    <div class="tc margin-top25">
                        <a href="/loan-list" class="normal-button-day" target="_blank">马上投资圣诞专享标</a>
                    </div>

                    <div class="tc" >
                        <img src="${staticServer}/activity/images/christmas-day/gift.jpg" class="img-gift">
                    </div>

                </div>
            </div>
        <#else>

        <#--活动已经开始-->
            <div class="christmas-border clearfix activity-second">
                <div class="title-christmas">活动二：圣诞拆礼物</div>
                <div class="model-content clearfix">
                    <div class="color-sec info-activity-note">
                        <b class="tc ">活动时间：${christmasPrizeStartTime?string('yyyy-MM-dd HH:mm:ss')}-12.31 24:00</b>
                        蟹蟹各位大大们为圣诞老人铺路，昨晚，他已经偷偷爬过烟囱，把礼物挂在圣诞树上，还等什么，赶快拆礼物吧，Merry Christmas！ <br/>
                        温馨提示：在活动二期限内完成以下操作，才可获得拆礼物机会，每位用户最多可获得10次拆奖机会；
                    </div>
                    <div class="gift-box">
                        <div class="icon-balloon-group">
                            <em class="left">
                                <i>
                                    <#if steps[0] == 1>
                                        <a href="/register/user">注册</a>
                                    </#if>
                                    <#if steps[0] == 2>
                                        注册
                                    </#if>
                                </i>
                            </em>
                            <em class="center hack">
                                <i>
                                <#if steps[1] == 1>
                                    <a href="/register/account">实名认证</a>
                                </#if>
                                <#if steps[1] == 2>
                                   实名认证
                                </#if>
                                </i>
                            </em>
                            <em class="right">
                                <i>
                                    <#if steps[2] == 1>
                                        <a href="/register/account">首次投资</a>
                                    </#if>
                                    <#if steps[2] == 2>
                                        首次投资
                                    </#if>
                                </i>
                            </em>
                        </div>
                        <div class="user-info-info">
                            <b class="user-kind">新用户</b>
                            每完成注册、实名认证、首次投资，都可以获得一次拆礼物机会。
                        </div>

                    </div>

                    <div class="gift-box">
                        <div class="icon-balloon-group">
                            <em class="center"><i><a href="/referrer/refer-list" target="_blank"> 邀请好友</a></i></em>
                        </div>
                        <div class="user-info-info">
                            <b class="user-kind">老用户</b>
                            每邀请一个好友注册，可获得一次拆礼物机会。
                        </div>

                    </div>

                    <div class="gift-box">
                        <div class="icon-balloon-group">
                            <em class="center"><i><a href="/loan-list" target="_blank"> 马上投资</a></i></em>
                        </div>
                        <div class="user-info-info">
                            <b class="user-kind">所有用户</b>
                            活动期间每投资90天及以上标的累计金额每满2000元，即可获得一次拆礼物机会。
                        </div>
                    </div>
                   <div class="clearfix"></div>
                    <div class="reward-gift-box">
                        <div class="rotate-btn">
                            <div class="gift-case pointer-img">
                                <img src="${staticServer}/activity/images/christmas-day/reward-box.png">
                            </div>
                        </div>

                        <div class="luck-draw-times">
                            <span>我的拆奖机会：10次</span>
                        </div>
                        <div class="gift-info-list">
                            <em class="silk-left"></em>
                            <em class="silk-right"></em>
                            <em class="hat"></em>

                            <div class="record-list-box">
                                <div class="menu-switch clearfix">
                                    <span class="active">中奖记录</span>
                                    <span>我的奖品</span>
                                </div>
                                <div class="record-list">
                                    <ul class="user-record" id="userRecordList">
                                    </ul>
                                    <ul class="own-record" id="ownRecordList" style="display: none">
                                    </ul>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
         </div>

        </#if>
        <div class="christmas-border activity-info">
            <div class="model-content">
                <span class="text-note">
                    活动说明： <br/>
1.拆礼物所获得的红包和加息券将即时发放，请到我的账户中查看；<br/>
2.实物奖品将于活动结束后7个工作日内寄出，若在7个工作日内无法联系到获奖用户，将视为自动放弃奖励；<br/>
3.为了保证获奖结果的公平性，获奖用户在活动期间所进行的圣诞专享标投标不允许进行债权转让；<br/>
4.拓天速贷在法律范围内保留对本活动的最终解释权；<br/>
5.市场有风险，投资需谨慎。<br/>
                </span>
            </div>
        </div>
    </div>
    <#include "login-tip.ftl" />
    <a href="javascript:void(0)" class="show-login no-login-text"></a>

    <div class="tip-list" style="display: none">
        <div class="close-btn go-close"></div>
        <div class="text-tip"></div>
        <div class="btn-list"></div>
    </div>

</div>
</@global.main>


