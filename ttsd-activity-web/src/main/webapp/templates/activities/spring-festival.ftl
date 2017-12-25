<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.spring_festival}" pageJavascript="${js.spring_festival}" activeNav="" activeLeftNav="" title="春节活动_活动中心_拓天速贷" keywords="拓天速贷,签到领福袋,投资拆红包,春节投资活动" description="拓天速贷春节投资活动,每日签到领取福袋,点亮窗花拆红包,春节投资拿年货大礼包,累计投资礼包加量,拓天速贷年货派不停.">
<div class="spring-festival-container">
    <div class="top-container">
        <img src="${commonStaticServer}/activity/images/spring-festival/top-img.jpg" width="100%" class="top-img">
        <img src="${commonStaticServer}/activity/images/spring-festival/top-img-phone.jpg" width="100%" class="top-img-phone">
    </div>
    <div class="wp">
        <div class="model-container">
            <i class="tree tree-left"></i>
            <i class="tree tree-right"></i>
            <div class="title-item">
                <span class="title-sm">签到领福袋</span>
            </div>
            <h3 class="title-info">活动期间，每日在本活动页面签到即可领取1个福袋。</h3>
            <div class="bag-item">
                <img src="${commonStaticServer}/activity/images/spring-festival/bag-img.png" class="bag-img">
                <img src="${commonStaticServer}/activity/images/spring-festival/bag-icon.png" width="100%" class="bag-icon">
            </div>
            <div class="btn-item">
                <#if isActivity?? && !isActivity>
                    <span class="check-in active" >活动已结束</span>
                <#else>
                    <span id="loginCheck" style="display:none">去登录签到</span>
                    <#if signedIn?? && signedIn && isDraw?? && isDraw>
                        <span class="check-in active"style="display:none">已签到</span>
                    <#elseif signedIn?? && !signedIn && isDraw?? && !isDraw>
                        <span class="check-in" id="checkIn" style="display:none">签到领福袋</span>
                    <#elseif signedIn?? && signedIn && isDraw?? && !isDraw>
                        <span class="check-in" id="drawBtn" style="display:none">领取福袋</span>
                    </#if>
                </#if>
            </div>
        </div>
        <div class="model-container">
            <i class="tree tree-left"></i>
            <i class="tree tree-right"></i>
            <div class="title-item">
                <span class="title-sm">点亮窗花拆红包</span>
            </div>
            <h3 class="title-info">活动期间累计投资满一定金额，可点亮一个窗花，获得该窗花对应的红包奖励，红包奖励可累计获得。</h3>
            <div class="red-bag-item">
                <ul class="money-item clearfix">
                    <li class="<#if taskProgress[0] == 1>active</#if>">
                        <h3>累计投资满1000元可点亮窗花一<br />获得<span>30元</span>投资红包</h3>
                        <div class="money-content">
                            <p><span>30</span>元</p>
                            <p>投资红包</p>
                        </div>
                        <div class="text-item">
                            已获得该奖励
                        </div>
                    </li>
                    <li class="<#if taskProgress[1] == 1>active</#if>">
                        <h3>累计投资满5000元可点亮窗花二<br />获得<span>60元</span>投资红包</h3>
                        <div class="money-content">
                            <p><span>60</span>元</p>
                            <p>投资红包</p>
                        </div>
                        <div class="text-item">
                            已获得该奖励
                        </div>
                    </li>
                    <li class="<#if taskProgress[2] == 1>active</#if>">
                        <h3>累计投资满12000元可点亮窗花三<br />获得<span>160元</span>投资红包</h3>
                        <div class="money-content">
                            <p><span>160</span>元</p>
                            <p>投资红包</p>
                        </div>
                        <div class="text-item">
                            已获得该奖励
                        </div>
                    </li>
                    <li class="<#if taskProgress[3] == 1>active</#if>">
                        <h3>累计投资满30000元可点亮窗花四<br />获得<span>240元</span>投资红包</h3>
                        <div class="money-content">
                            <p><span>240</span>元</p>
                            <p>投资红包</p>
                        </div>
                        <div class="text-item">
                            已获得该奖励
                        </div>
                    </li>
                </ul>
            </div>
            <div class="tip-item">
                温馨提示：为便于您分散投资，红包将根据面额拆分发放。红包将于投资成功后即时发放至您的账户。
            </div>
            <div class="btn-item">
                <a href="/loan-list">立即投资拆红包</a>
            </div>
        </div>
        <div class="model-container">
            <i class="tree tree-left"></i>
            <i class="tree tree-right"></i>
            <div class="title-item">
                <span class="title-sm mx-title">节日投资，拿年货大礼包</span>
            </div>
            <h3 class="title-info">根据用户活动期间累计投资额，可领取相应的大礼包奖励</h3>
             <div class="red-bag-item">
                <ul class="gift-item clearfix">
                    <li>
                        <i class="angle angle-left-top"></i>
                        <i class="angle angle-right-top"></i>
                        <i class="angle angle-left-bottom"></i>
                        <i class="angle angle-right-bottom"></i>
                        <h5>年货大礼包</h5>
                        <div class="gift-info">
                            <p>投资满50000元可获得</p>
                            <p>100元京东E卡+0.5%加息券</p>
                        </div>
                        <div class="gift-img">
                            <div class="coupon-item">
                                <p><span>+0.5</span>%</p>
                                <p>加息券</p>
                            </div>
                            <img src="${commonStaticServer}/activity/images/spring-festival/gift-one.png">
                        </div>
                    </li>
                    <li>
                        <i class="angle angle-left-top"></i>
                        <i class="angle angle-right-top"></i>
                        <i class="angle angle-left-bottom"></i>
                        <i class="angle angle-right-bottom"></i>
                        <h5>健康大礼包</h5>
                        <div class="gift-info">
                            <p>投资满100000元可获得</p>
                            <p>眼部按摩仪+0.5%加息券</p>
                        </div>
                        <div class="gift-img">
                            <div class="coupon-item">
                                <p><span>+0.5</span>%</p>
                                <p>加息券</p>
                            </div>
                            <img src="${commonStaticServer}/activity/images/spring-festival/gift-two.png">
                        </div>
                    </li>
                    <li>
                        <i class="angle angle-left-top"></i>
                        <i class="angle angle-right-top"></i>
                        <i class="angle angle-left-bottom"></i>
                        <i class="angle angle-right-bottom"></i>
                        <h5>爱家大礼包</h5>
                        <div class="gift-info">
                            <p>投资满200000元可获得</p>
                            <p>三星空气净化器+0.5%加息券</p>
                        </div>
                        <div class="gift-img">
                            <div class="coupon-item">
                                <p><span>+0.5</span>%</p>
                                <p>加息券</p>
                            </div>
                            <img src="${commonStaticServer}/activity/images/spring-festival/gift-three.png" class="sky-img">
                        </div>
                    </li>
                    <li>
                        <i class="angle angle-left-top"></i>
                        <i class="angle angle-right-top"></i>
                        <i class="angle angle-left-bottom"></i>
                        <i class="angle angle-right-bottom"></i>
                        <h5>享乐大礼包</h5>
                        <div class="gift-info">
                            <p>投资满300000元可获得</p>
                            <p>索尼微单相机+0.5%加息券</p>
                        </div>
                        <div class="gift-img">
                            <div class="coupon-item">
                                <p><span>+0.5</span>%</p>
                                <p>加息券</p>
                            </div>
                            <img src="${commonStaticServer}/activity/images/spring-festival/gift-four.png">
                        </div>
                    </li>
                </ul>
            </div>
            <div class="btn-item">
                <a href="/loan-list">立即投资拿礼包</a>
            </div>
        </div>
        <div class="model-container mb-50">
            <i class="tree tree-left"></i>
            <i class="tree tree-right"></i>
            <div class="title-item">
                <span class="title-sm">活动规则</span>
            </div>
            <ul class="rule-list">
                <li>1、根据用户活动期间累计投资额，可领取相应的大礼包奖励；</li>
                <li>2、本活动仅限90天、180天、360天抵押类产品，债权转让及新手专享项目不参与活动；</li>
                <li>3、实物奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；</li>
                <li>4、为了保证获奖结果的公平性，在活动三中获奖的用户在活动期间所进行的投标不允许债权转让；</li>
                <li>5、活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</li>
            </ul>
        </div>
    </div>
    <div class="money-tip" id="moneyTip">
        <div class="content-item">
            <i class="tip tip-left-top"></i>
            <i class="tip tip-right-top"></i>
            <i class="tip tip-left-bottom"></i>
            <i class="tip tip-right-bottom"></i>
            <h3>恭喜您获得福袋礼品</h3>
            <div class="top-com">
                <div class="left-name">
                    <span id="bagType"></span>
                    <em></em>
                    <i class="circle-top"></i>
                    <i class="circle-bottom"></i>
                </div>
                <div class="right-coupon">
                    <p>
                        <span class="num-text" id="numText">0.00</span>
                        <span class="unit-text" id="numBite"></span>
                    </p>
                </div>
            </div>
            <div class="btn-item">
                <a href="/loan-list">投资激活礼包</a>
            </div>
        </div>
    </div>
    <a href="javascript:void(0)" class="show-login no-login-text"></a>
    <#include "login-tip.ftl" />
</div>
</@global.main>