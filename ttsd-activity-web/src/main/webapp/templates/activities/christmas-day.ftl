<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.christmas_day}" pageJavascript="${js.christmas_day}" activeNav="" activeLeftNav="" title="圣诞节活动_活动中心_拓天速贷" keywords="圣诞礼物,圣诞节投资,拓天活动,拓天速贷" description="拓天速贷圣诞节活动帮圣诞老人投资铺路,圣诞节投资可享1%加息券,并按投资比例瓜分20000元红包大奖,还可领取圣诞礼物.">
    <@global.isNotAnonymous>
    <div style="display: none" class="login-name" data-login-name='<@global.security.authentication property="principal.username" />'></div>
    <div style="display: none" class="mobile" id="MobileNumber" data-mobile='<@global.security.authentication property="principal.mobile" />'></div>
    </@global.isNotAnonymous>
<div class="activity-slide"></div>

<div class="christmas-day-frame" id="christmasDayFrame">

    <div class="reg-tag-current" style="display: none">
        <#include '../module/register.ftl' />
    </div>
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
                <div class="title-christmas"><span>活动一：为圣诞老人铺路</span></div>
                <div class="model-content clearfix">
                        <div class="gift-box">
                            <em class="icon-balloon">1重礼</em>
                            <b class="sub-title-one">专享标加息0.5%</b>
                            专享标在现有约定年化利率基础上<i>加息0.5%</i>。
                        </div>

                        <div class="gift-box">
                            <em class="icon-balloon">2重礼</em>
                            <b class="sub-title-two">用券投资加息1%</b>
                            投资圣诞专享标单笔达3万元及以上，即赠送一张0.5%加息券，用券投资专享标<i>加息1%</i>。
                        </div>

                        <div class="gift-box">
                            <em class="icon-balloon">3重礼</em>
                            <b class="sub-title-three">瓜分20000元红包</b>
                            如活动结束后，圣诞专享标投满420万，所有投资专享标的用户按投资比例瓜分<i>20000元红包</i>。
                        </div>

                        <div class="gift-box clearfix last">
                        <span>
                            栗子：沈先生预计投资20万，第一笔投资专享标3万元加息0.5%，第二笔使用0.5%加息券投资专享标17万元，可加息1%。同时可获得红包200000*20000/4200000=952元；<br/>
                            小贴士：<br/>
                            用户所获红包金额=活动期间投资专享标金额*20000/4200000。加息券每人限领1张，有效期3天。
                        </span>
                        </div>
                        <div class="clearfix"></div>
                        <dl class="invest-percent-box clearfix">
                            <dt>目前专享标共投资：<em class="total-invest">${allInvestAmount}</span></em>元</dt>
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
1、如活动结束后，专享标投满420万，则按参与用户的投资额比例发放20000元红包，该红包投资50元即可使用；如活动结束后，专享标未投满420万，则投资专享标的用户每人发放38元红包，需投资30000元可使用；<br/>
                            2、用户所获红包将于活动结束后三个工作日内发放，可在“我的财富-我的宝藏”中查看；<br/>
3、为了保证获奖结果的公平性，获奖用户在活动期间所进行的圣诞专享标投标不允许进行债权转让。<br/>
                        </span>
                </div>
            </div>
        </div>

        <div class="activity-section-two">
        <#--活动二未开始-->
        <#if isStart == 0>
            <div class="christmas-border clearfix">
                <div class="title-christmas"><span>活动二：圣诞拆礼物</span></div>
                <div class="model-content clearfix">
                    <div class="tc color-sec margin-top25">
                        活动时间：12月24日24:00-12月31日24:00 <br/>
                        圣诞礼物还在路上，快去投资专享标，帮圣诞老人铺路吧！
                    </div>
                    <div class="tc margin-top25">
                        <a href="/loan-list" class="normal-button-day" target="_blank">马上投资圣诞专享标</a>
                    </div>

                    <div class="tc" >
                        <img src="${commonStaticServer}/activity/images/christmas-day/gift.jpg" class="img-gift">
                    </div>

                </div>
            </div>
        <#else>

        <#--活动已经开始-->
            <div class="christmas-border clearfix activity-second">
                <div class="title-christmas"><span>活动二：圣诞拆礼物</span></div>
                <div class="model-content clearfix">
                    <div class="color-sec info-activity-note">
                        <b class="tc ">活动时间：12月24日24:00-12月31日24:00</b>
                        蟹蟹各位大大们为圣诞老人铺路，昨晚，他已经偷偷爬过烟囱，把礼物挂在圣诞树上，还等什么，赶快拆礼物吧，Merry Christmas！ <br/>
                        温馨提示：在活动二期限内完成以下操作，才可获得拆礼物机会，每位用户最多可获得10次拆奖机会；
                    </div>
                    <div class="gift-box">
                        <div class="icon-balloon-group">
                            <em class="left">
                                <i>
                                    <#if steps[0] == 0>
                                        <a href="/register/user">注册</a>
                                    </#if>
                                    <#if steps[0] == 1>
                                        注册
                                    </#if>
                                </i>
                            </em>
                            <em class="center hack">
                                <i>
                                <#if steps[1] == 0>
                                    <a href="/register/account">实名认证</a>
                                </#if>
                                <#if steps[1] == 1>
                                   实名认证
                                </#if>
                                </i>
                            </em>
                            <em class="right">
                                <i>
                                    <#if steps[2] == 0>
                                        <a href="/loan-list">首次投资</a>
                                    </#if>
                                    <#if steps[2] == 1>
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
                                <img src="${commonStaticServer}/activity/images/christmas-day/reward-box.png">
                            </div>
                        </div>

                        <div class="luck-draw-times">
                            <span>我的拆奖机会：<i class="draw-time"></i>次</span>
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

        <#--签到成功-->
            <div class="tip-list" data-return="signOk">
                <div class="close-btn go-close"></div>
                <div class="text-tip">
                    <p class="success-text">签到成功！</p>
                    <p class="des-text">恭喜您获得10积分，并获得砸金蛋机会一次</p>
                </div>
                <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
            </div>

        <#--签到失败-->
            <div class="tip-list" data-return="signNo">
                <div class="close-btn go-close"></div>
                <div class="text-tip">
                    <p class="login-text">请与客服联系</p>
                </div>
                <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
            </div>

        </div>

</div>
</@global.main>


