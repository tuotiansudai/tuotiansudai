<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.new_year}" pageJavascript="${js.new_year}" activeNav="" activeLeftNav="" title="新年活动_活动中心_拓天速贷" keywords="拓天速贷,新年砸金蛋,新年开神灯,元旦投资活动" description="拓天速贷元旦投资活动,邀请好友砸金蛋,投资许愿开神灯,投资元旦专享标100%中奖,拓天速贷帮你实现新年愿望.">
    <@global.isNotAnonymous>
    <div style="display: none" class="login-name" data-login-name='<@global.security.authentication property="principal.username" />'></div>
    <div style="display: none" class="mobile" id="MobileNumber" data-mobile='<@global.security.authentication property="principal.mobile" />'></div>
    </@global.isNotAnonymous>

<div class="new-year-slide" id="newYearSlide"></div>

<div class="new-year-frame" id="newYearDayFrame">

    <#--<div class="body-decorate" id="bodyDecorate">-->
        <#--<div class="bg-left"></div>-->
        <#--<div class="bg-right"></div>-->
     <#--</div>-->
<div class="page-width">
    <div class="reg-tag-current" style="display: none">
        <#include '../module/register.ftl' />
    </div>

    <div class="title-main-col">
        <i class="line-left"></i>
        <i class="line-right"></i>
        <em>签到砸金蛋，100%中奖</em>
    </div>

    <div class="box-normal clearfix reward-gift-box">

        <div class="draw-area-box">
            <img src="${commonStaticServer}/activity/images/new-year/panzi.jpg" class="draw">
            <img src="${commonStaticServer}/activity/images/new-year/egg.png" class="gold-egg">
            <img src="${commonStaticServer}/activity/images/new-year/chuizi.png" class="gold-hammer">
            <div class="my-chance" >
                <i></i><span>我的砸蛋机会：<em class="my-times" data-times="${time}">${time} </em>次
                <input value="${task}" type="hidden" id="rewardTaskStatus">
                <input value="${inActivityDate}" type="hidden" id="inActivityDate">
            </span>
            </div>
        </div>

        <div class="button-group tc">
            <a href="javascript:void(0);" class="normal-button" id = "loginIn" style="display: none">登录</a>

            <span class="signedIn-status">
                <#if signedIn?? && signedIn>
                    <a href="javascript:void(0);" class="normal-button" style="display: none">已签到</a>
                <#else >
                <button type="button" class="normal-button"  id="signToday" style="display: none">签到</button>
                </#if>
            </span>

            <a href="javascript:void(0);" class="normal-button to-referrer">邀请好友</a>
            <a href="/loan-list" class="normal-button">马上投资</a>
        </div>

        <div class="activity-rule-box clearfix">
            <div class="gift-info-box">
                <ul class="gift-record clearfix">
                    <li class="active"><span>中奖记录</span></li>
                    <li><span>我的奖品</span></li>
                </ul>
                <div class="record-list">
                    <ul class="record-model user-record" >
                    </ul>
                    <ul class="record-model own-record" style="display: none"></ul>
                </div>
            </div>
            <div class="text-box">
                <span>
                活动规则说明： <br/>
                1、活动期间每日签到即可获得一次免费砸蛋机会；<br/>
                2、活动期间每邀请1名好友注册可砸5次，上不封顶；<br/>
                3、活动期间投资新年专享标，每满5000元即可砸1次，如单笔投资10000元，可直接获得2次砸金蛋机会，每名用户活动期间凭投资最多砸10次；<br/>
                4、砸蛋机会需在活动有效期内使用，过期作废，请及时使用；<br/>
                5、活动中有恶意刷奖的行为，一经查出拓天速贷有权追溯已发放的奖励。
                    </span>
            </div>
        </div>
    </div>

    <div class="title-main-col">
        <i class="line-left"></i>
        <i class="line-right"></i>
        <em>新年开神灯，许你10个愿望</em>
    </div>

    <div class="box-normal clearfix">
        <div class="magic-lamp">
            <i class="lamp-left"></i>
            <i class="lamp-right"></i>
            <div class="slide-text">
                <ul class="slide-text-list">
                    <li>2016即将画上一个圆满的句号，</li>
                    <li>你的新年愿望是什么？</li>
                    <li>是一部心水已久的手机？</li>
                    <li>一次不看价签的买买买？</li>
                    <li>还是一张回家的车票？</li>
                    <li>一次放空身心的度假之旅？</li>
                    <li>在这个辞旧迎新的时刻，</li>
                    <li>能帮你实现一个愿望的是朋友</li>
                    <li>实现两个的是亲戚，实现三个的是爱人</li>
                    <li>实现五个的是父母</li>
                    <li>而拓天速贷，帮你实现十个愿望！</li>
                    <li>新年新气象，当然十全十美！</li>
                </ul>

            </div>
        </div>

        <p class="activity-note">活动期间用户投资新年专享标，累计投资金额达到一个目标，即可打开一盏神灯，神灯会送你一个奖励，愿望奖励可累计领取。<br/>
            栗子：如拓小天在活动第一天投资5000元，最后一天投资10000元，则拓小天在活动期间共投资了15000元，可开两盏神灯，获得20元红包+爱奇艺会员月卡。</p>

        <div class="total-invest-amount clearfix">
            <dl>
                <dt>您的累计投资金额：</dt>
                <dd>${investAmount}元</dd>
            </dl>
            <dl>
                <dt>距下一个奖品还差：</dt>
                <dd>${nextAmount}元</dd>
            </dl>
            <dl class="last tc">
                <dd><a href="/loan-list" class="btn-to-invest">立即投资</a> </dd>
            </dl>
        </div>

        <div class="reward-list-frame clearfix">
        <#--第一盏神灯-->
            <div class="reward-box">
                <div class="reward-detail">
                    <em>累计投资金额：5,000元</em>
                    <div class="reward-img">
                        <img src="${commonStaticServer}/activity/images/new-year/reward01.png" alt="20元红包2000元激活">
                    </div>
                     <i>20元红包（2000元激活）</i>
                    <s><span>目标完成</span></s>
                </div>
                <div class="reward-title">第一盏神灯：让红包飞</div>
            </div>
        <#--第二盏神灯-->
            <div class="reward-box">
                <div class="reward-detail">
                    <em>累计投资金额：10,000元</em>
                    <div class="reward-img">
                    <img src="${commonStaticServer}/activity/images/new-year/reward02.png" alt="爱奇艺会员月卡">
                    </div>
                    <i>爱奇艺会员月卡</i>
                    <s><span>目标完成</span></s>
                </div>
                <div class="reward-title">第二盏神灯：放肆宅</div>
            </div>
        <#--第三盏神灯-->
            <div class="reward-box">
                <div class="reward-detail">
                    <em>累计投资金额：20,000元</em>
                    <div class="reward-img">
                    <img src="${commonStaticServer}/activity/images/new-year/reward03.png" alt="报销50元电影票">
                        </div>
                    <i>报销50元电影票</i>
                    <s><span>目标完成</span></s>
                </div>
                <div class="reward-title">第三盏神灯：贺岁片</div>
            </div>
        <#--第四盏神灯-->
            <div class="reward-box">
                <div class="reward-detail">
                    <em>累计投资金额：30,000元</em>
                    <div class="reward-img">
                    <img src="${commonStaticServer}/activity/images/new-year/reward04.png" alt="报销50元电影票">
                        </div>
                    <i>50元话费</i>
                    <s><span>目标完成</span></s>
                </div>
                <div class="reward-title">第四盏神灯：常联系</div>
            </div>

        <#--第五盏神灯-->
            <div class="reward-box">
                <div class="reward-detail">
                    <em>累计投资金额：50,000元</em>
                    <div class="reward-img">
                    <img src="${commonStaticServer}/activity/images/new-year/reward05.png" alt="100元中国石化加油卡">
                        </div>
                    <i>100元中国石化加油卡</i>
                    <s><span>目标完成</span></s>
                </div>
                <div class="reward-title">第五盏神灯：出行基金</div>
            </div>

        <#--第六盏神灯-->
            <div class="reward-box">
                <div class="reward-detail">
                    <em>累计投资金额：100,000元</em>
                    <div class="reward-img">
                    <img src="${commonStaticServer}/activity/images/new-year/reward06.png" alt="100元中国石化加油卡">
                        </div>
                    <i>报销300元火车票</i>
                    <s><span>目标完成</span></s>
                </div>
                <div class="reward-title">第六盏神灯：返乡基金</div>
            </div>

        <#--第七盏神灯-->
            <div class="reward-box">
                <div class="reward-detail">
                    <em>累计投资金额：200,000元</em>
                    <div class="reward-img">
                    <img src="${commonStaticServer}/activity/images/new-year/reward07.png" alt="700元京东E卡">
                        </div>
                    <i>700元京东E卡</i>
                    <s><span>目标完成</span></s>
                </div>
                <div class="reward-title">第七盏神灯：年货go</div>
            </div>

        <#--第八盏神灯-->
            <div class="reward-box">
                <div class="reward-detail">
                    <em>累计投资金额：300,000元</em>
                    <div class="reward-img">
                    <img src="${commonStaticServer}/activity/images/new-year/reward08.png" alt="800元红包（50元激活）">
                        </div>
                    <i>800元红包（50元激活）</i>
                    <s><span>目标完成</span></s>
                </div>
                <div class="reward-title">第八盏神灯：份子钱</div>
            </div>

        <#--第九盏神灯-->
            <div class="reward-box">
                <div class="reward-detail">
                    <em>累计投资金额：500,000元</em>
                    <div class="reward-img">
                    <img src="${commonStaticServer}/activity/images/new-year/reward09.png" alt="1600元芒果卡">
                        </div>
                    <i>1600元芒果卡</i>
                    <s><span>目标完成</span></s>
                </div>
                <div class="reward-title">第九盏神灯：旅游基金</div>
            </div>

        <#--第十盏神灯-->
            <div class="reward-box">
                <div class="reward-detail">
                    <em>累计投资金额：700,000元</em>
                    <div class="reward-img">
                    <img src="${commonStaticServer}/activity/images/new-year/reward10.png" alt="小米手机5">
                        </div>
                    <i>小米手机5</i>
                    <s><span>目标完成</span></s>
                </div>
                <div class="reward-title">第十盏神灯：取悦你</div>
            </div>


        </div>
    </div>

    <div class="reminder-box">

        温馨提示： <br/>
        1、本活动仅适用于新年专享标，债权转让项目不参与活动； <br/>
        2、红包将于活动结束后三个工作日内发放，用户可在“我的账户-我的宝藏”中查看； <br/>
        3、实物奖品将于活动结束后10个工作日内由客服联系发放，请保持手机畅通，10个工作日内无法联系的用户，视为自动放弃奖励； <br/>
        4、为保证获奖结果的公平性，用户在活动期间投资的专享标不允许进行债权转让； <br/>
        5、拓天速贷在法律范围内保留对本活动的最终解释权。 <br/>
        6、市场有风险，投资需谨慎
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
                    <div class="btn-list"><a href="javascript:void(0)" class="double-btn my-treasure">去查看</a> <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a></div>
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
                        <p class="des-text">恭喜您获得砸金蛋机会一次</p>
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