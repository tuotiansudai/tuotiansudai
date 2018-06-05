<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.double_eleven}" pageJavascript="${js.double_eleven}" activeNav="" activeLeftNav="" title="拓天双11活动_活动中心_拓天速贷" keywords="撩妹神器,撩妹礼包,投资活动,拓天速贷" description="拓天速贷双11活动,推出撩妹神器'息'结良缘限时专享标,收益增加1.1%,并送'撩妹礼包',你撩妹,我买单,单身狗抽奖还可获100%中奖机会.">
<div class="double-eleven-container" id="doubleElevenContainer">
    <@global.isNotAnonymous>
        <input type="hidden" value="<@global.security.authentication property="principal.mobile" />" id="MobileNumber"/>
    </@global.isNotAnonymous>
    <div class="top-intro-img">
        <img src="${commonStaticServer}/activity/images/double-eleven/top-intro.jpg" alt="双十一" width="100%" class="top-img">
        <img src="${commonStaticServer}/activity/images/double-eleven/top-intro-phone.png" alt="双十一" width="100%" class="top-img-phone">
    </div>
    <div class="content-group">
        <div class="wp clearfix">
            <div class="reg-tag-current" style="display: none">
                <#include '../module/register.ftl' />
            </div>
            <div class="model-group actor-info">
                <div class="content-item after-line">
                    <p class="title-text">清空女票的购物车要多少钱？别报警，拓天速贷此番祭出</p>
                    <p>撩妹神器：</p>
                    <p><span><img src="${commonStaticServer}/activity/images/double-eleven/info-text.png" width="100%"></span></p>
                    <p>女神喜欢的香槟玫瑰、法国大餐、星级酒店，统统满足她！所谓“但愿人长久，钱里共婵娟”。兄弟只能帮到这了，接下来你懂得哦！</p>
                </div>
            </div>
            <div class="model-group time-count">
                <div class="content-item after-line">
                    <div class="model-item">
                        <h3>
                            <span>
                                <i class="left-line"></i>
                                <img src="${commonStaticServer}/activity/images/double-eleven/title-one.png" >
                                <i class="right-line"></i>
                            </span>
                        </h3>
                        <p>活动期间，每日上午11:11、下午1:11祭出一个约定年化利率在现有基础上增加1.1%的高息项目，</p>
                        <p>请您在“我要投资”页面找到带有“限时专享”标识的项目进行投资，即可参与活动。</p>
                    </div>
                    <div class="product-group">
                        <i class="date-icon">
                            <span>${today}</span>
                        </i>
                        <input type="hidden" id="nowTimeCount" data-time="${now}">
                        <ul id="timeCount">
                            <li>
                                <p><img src="${commonStaticServer}/activity/images/double-eleven/first-time.png" ></p>
                                <p class="tip-info">即将登场</p>
                                <p class="time-text">
                                    <span data-date="${today}" data-now="${now}" data-end="11:11:00" data-activityEnd="${activityEnd}">00:00:00</span>
                                </p>
                                <p class="btn-item">
                                    <a href="/loan-list">马上投资</a>
                                </p>
                            </li>
                            <li>
                                <p><img src="${commonStaticServer}/activity/images/double-eleven/twice-time.png" ></p>
                                <p class="tip-info">即将登场</p>
                                <p class="time-text">
                                    <span data-date="${today}" data-now="${now}" data-end="13:11:00" data-activityEnd="${activityEnd}">00:00:00</span>
                                </p>
                                <p class="btn-item">
                                    <a href="/loan-list">马上投资</a>
                                </p>

                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="model-group red-bag">
                <div class="content-item after-line">
                    <div class="model-item">
                        <h3>
                            <span>
                                <i class="left-line"></i>
                                <img src="${commonStaticServer}/activity/images/double-eleven/title-two.png" >
                                <i class="right-line"></i>
                            </span>
                        </h3>
                        <p>活动期间，投资90天、180天或360天标的，<span>单笔1111元</span>以上即送<span>“撩妹礼包”</span>(每人限领1个)，</p>
                        <p>礼包于活动结束后三个工作日内发放，用户可在PC端“我的账户-我的宝藏”或App端“我的财富-我的宝藏”中查看。</p>
                        <div class="coupon-group">
                            <h3>
                                <img src="${commonStaticServer}/activity/images/double-eleven/bag-title.png" >
                            </h3>
                            <ul class="coupon-item">
                                <li>
                                    <p><span>20元</span>红包</p>
                                </li>
                                <li>
                                    <p><span>50元</span>红包</p>
                                </li>
                                <li>
                                    <p><span>100元</span>红包</p>
                                </li>
                                <li>
                                    <p><span>0.5%</span>加息券</p>
                                </li>
                            </ul>
                        </div>
                        <div class="loan-group">
                            <a href="/loan-list">马上投资</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="model-group invite-people">
                <div class="content-item after-line">
                    <div class="model-item">
                        <h3>
                            <span>
                                <i class="left-line"></i>
                                <img src="${commonStaticServer}/activity/images/double-eleven/title-three.png" >
                                <i class="right-line"></i>
                            </span>
                        </h3>
                        <p class="font-14 text-c actor-phone">活动期间，新用户在平台完成注册、实名认证、绑卡、充值、投资均可获得一次100%中奖的抽奖机会；</p>
                        <ul class="loan-list-group">
                        <li class="<#if steps[0] == 1>active</#if><#if steps[0] == 2>finish</#if>">
                            <span class="step step-one"></span>
                            <#if steps[0] == 1>
                                <a href="/register/user" class="step-btn">去注册</a></#if>
                            <#if steps[0] == 2>
                                <a href="javascript:void(0)" class="step-btn">已注册</a>
                            </#if>
                            </li>
                            <li class="<#if steps[1] == 1>active</#if><#if steps[1] == 2>finish</#if>">
                                <i class="after-icon"></i>
                                <span class="step step-two"></span>
                                <#if steps[1] == 0>
                                    <a href="javascript:void(0)" class="step-btn">认证</a></#if>
                                <#if steps[1] == 1>
                                    <a href="/register/account" class="step-btn">去认证</a></#if>
                                <#if steps[1] == 2>
                                    <a href="javascript:void(0)" class="step-btn">已认证</a></#if>
                            </li>
                            <li class="<#if steps[2] == 1>active</#if><#if steps[2] == 2>finish</#if>">
                                <i class="after-icon"></i>
                                <span class="step step-three"></span>
                                <#if steps[2] == 0>
                                    <a href="javascript:void(0)" class="step-btn">绑卡</a></#if>
                                <#if steps[2] == 1>
                                    <a href="/bind-card" class="step-btn">去绑卡</a></#if>
                                <#if steps[2] == 2>
                                    <a href="javascript:void(0)" class="step-btn">已绑卡</a></#if>
                            </li>
                            <li class="<#if steps[3] == 1>active</#if>">
                                <i class="after-icon"></i>
                                <span class="step step-four"></span>
                                <#if steps[3] == 0>
                                    <a href="javascript:void(0)" class="step-btn">充值</a></#if>
                                <#if steps[3] == 1>
                                    <a href="/recharge" class="step-btn">去充值</a></#if>
                            </li>
                            <li class="<#if steps[4] == 1>active</#if>">
                                <i class="after-icon"></i>
                                <span class="step step-five"></span>
                                <#if steps[4] == 0>
                                    <a href="javascript:void(0)" class="step-btn">投资</a></#if>
                                <#if steps[4] == 1>
                                    <@global.isAnonymous>
                                        <a href="/login?redirect=/activity/autumn/luxury" class="step-btn">去投资</a>
                                    </@global.isAnonymous>
                                    <@global.isNotAnonymous>
                                        <a href="/loan-list" class="step-btn">去投资</a>
                                    </@global.isNotAnonymous>
                                </#if>
                            </li>
                        </ul>
                        <p class="font-14 text-c actor-phone">活动期间，每推荐一名好友注册也可获得一次抽奖机会；好友投资，还可再得一次抽奖机会。邀请越多机会越多。</p>
                        <dl class="actor-word">
                            <dt>活动期间</dt>
                            <dd>1、新用户在平台完成注册、实名认证、绑卡、充值、投资均可获得一次100%中奖的抽奖机会；</dd>
                            <dd>2、每推荐一名好友注册也可获得一次抽奖机会；好友投资，还可再得一次抽奖机会。邀请越多机会越多。</dd>
                        </dl>
                        <div class="loan-group">
                            <a href="/referrer/refer-list" class="font-18">立即邀请好友赢抽奖机会</a>
                        </div>
                    </div>

                    <#--抽奖模块开始-->

                    <div class="nine-lottery-group" >
                        <div class="lottery-left-group">
                            <h3>我的抽奖机会：<span>0</span>次</h3>
                            <ul class="lottery-item" id="lotteryBox">
                                <li class="lottery-unit lottery-unit-0" data-unit="0">
                                    <img src="${commonStaticServer}/activity/images/model/double-eleven/gift-one.png">
                                </li>
                                <li class="lottery-unit lottery-unit-1" data-unit="1">
                                    <img src="${commonStaticServer}/activity/images/model/double-eleven/gift-two.png">
                                </li>
                                <li class="lottery-unit lottery-unit-2" data-unit="2">
                                    <img src="${commonStaticServer}/activity/images/model/double-eleven/gift-three.png">
                                </li>
                                <li class="lottery-unit lottery-unit-7" data-unit="7">
                                    <img src="${commonStaticServer}/activity/images/model/double-eleven/gift-eight.png">
                                </li>
                                <li class="lottery-btn pointer-img">
                                    <img src="${commonStaticServer}/activity/images/model/double-eleven/lottery-btn.png">
                                </li>
                                <li class="lottery-unit lottery-unit-3" data-unit="3">
                                    <img src="${commonStaticServer}/activity/images/model/double-eleven/gift-four.png">
                                </li>
                                <li class="lottery-unit lottery-unit-6" data-unit="6">
                                    <img src="${commonStaticServer}/activity/images/model/double-eleven/gift-seven.png">
                                </li>
                                <li class="lottery-unit lottery-unit-5" data-unit="5">
                                    <img src="${commonStaticServer}/activity/images/model/double-eleven/gift-six.png">
                                </li>
                                <li class="lottery-unit lottery-unit-4" data-unit="4">
                                    <img src="${commonStaticServer}/activity/images/model/double-eleven/gift-five.png">
                                </li>
                            </ul>
                        </div>

                        <div class="lottery-right-group" id="lotteryList">
                            <h3>
                                <span class="active">中奖纪录</span>
                                <span>我的奖品</span>
                            </h3>
                            <div class="record-group record-list">
                                <ul class="record-item user-record" id="recordList"></ul>
                                <ul class="record-item own-record" id="myRecord" style="display: none"></ul>

                            </div>
                        </div>

                    </div>
                    <#--结束-->
                </div>
            </div>
            <div class="model-group tip-text">
                <div class="content-item">
                    <dl class="rule-list-group">
                        <dt>温馨提示：</dt>
                        <dd>1、抽奖活动的中奖结果将于次日由客服联系确认，红包在中奖后即时发放，实物奖品及话费、爱奇艺会员将于活动结束后七个工作日内统一安排发放，部分地区邮费自付，详询客服；</dd>
                        <dd>2、活动中如果出现恶意刷量等违规行为，拓天速贷将取消您获得奖励的资格，并有权撤销违规交易，拓天速贷在法律范围内保留对本活动的最终解释权。</dd>
                        <dd class="wx-code"><img src="${commonStaticServer}/activity/images/double-eleven/wx-code.png" ></dd>
                        <dd class="rule-intro">抢先了解活动攻略，赚钱快人一步</dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
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

    </div>
</div>
<#include "login-tip.ftl" />
</@global.main>