<#import "../../macro/global.ftl" as global>

<@global.main pageCss="${css.sport_play_2017}" pageJavascript="${js.sport_play_2017}" activeNav="" activeLeftNav="" title="运动达人VS职场骄子_活动中心_拓天速贷" keywords="运动达人,职场骄子,活动中心,抽奖,拓天速贷" description="拓天速贷活动期间用户每日登陆即有一次免费抽签机会,累计投资还可兑换不同实物奖品,运动奖VS职场奖,总有一款适合你.">
<div class="sport-play-container" id="sportPlayContainer">
    <div class="top-img compliance-banner">
        <div class="invest-tip tip-width">市场有风险，投资需谨慎！</div>
    </div>
    <div class="shaking-item">
        <div class="wp clearfix">
            <h3 class="title-one"></h3>
            <ul class="info-item">
                <li>1.活动期间用户每日登录即有一次免费抽签机会；</li>
                <li>2. 活动期间单笔投资每满1万元，即可增加1次抽签机会，如单笔投资5万元，则可增加5次抽签机会，以此类推；</li>
                <li>3. 当日所获免费抽签机会，仅限当日使用，如当日未使用，则机会失效。</li>
            </ul>
            <div class="lottery-times">
                剩余抽签机会：<span><strong class="draw-time">${drawCount}</strong>次</span>
            </div>
            <div class="draw-item">
                <div class="draw-model"></div>
            </div>
        </div>
    </div>
    <div class="wp clearfix">
        <div class="content-item">
            <h3 class="title-two"></h3>
            <div class="user-info">
                <@global.isAnonymous>
                    <p>活动期间内累计投资金额：<span class="to-login">登录查看</span></p>
                    <p>当前选择奖品：<span class="to-login">登录查看</span></p>
                </@global.isAnonymous>
                <@global.isNotAnonymous>
                    <p>我的累计投资金额：<span>${investAmount}元</span></p>
                    <p>当前选择奖品：<span id="selectGift"><#if exchangePrize?? >${exchangePrize}</#if><#if !exchangePrize?? >您还未兑换奖品</#if></span></p>
                </@global.isNotAnonymous>
                
                <p class="tip-text">活动期间，根据累计投资金额，可兑换不同档奖品， 每档奖品默认可2选1兑换，不同档的奖品不可同时获得。</p>
            </div>
            <div class="gift-list">
                <div class="gift-item">
                    <h3>累计投资5万</h3>

                    <div class="row-item select-item <#if prize=="HOUSEHOLE_DUMBBELL">active</#if>" data-name="HOUSEHOLE_DUMBBELL">
                        <i class="select-icon left-item"></i>
                        <p class="gift-img one-1"></p>
                        <p>家用哑铃一对</p>
                    </div>
                    <div class="row-item">
                        <p class="text-item">兑换</p>
                    </div>

                    <div class="row-item select-item <#if prize=="USB_LIGHTER">active</#if>" data-name="USB_LIGHTER" >
                        <i class="select-icon right-item"></i>
                        <p class="gift-img one-2"></p>
                        <p>USB充电打火机</p>
                    </div>
                </div>
                <div class="gift-item">
                    <h3>累计投资10万</h3>
                    <div class="row-item select-item <#if prize=="CAR_REFRIGRRATOR">active</#if>" data-name="CAR_REFRIGRRATOR" >
                        <i class="select-icon left-item"></i>
                        <p class="gift-img two-1"></p>
                        <p>科敏车载冰箱</p>
                    </div>
                    <div class="row-item">
                        <p class="text-item">兑换</p>
                    </div>
                    <div class="row-item select-item <#if prize=="MOBILE_HDD_500G">active</#if>" data-name="MOBILE_HDD_500G">
                        <i class="select-icon right-item"></i>
                        <p class="gift-img two-2"></p>
                        <p>东芝移动硬盘 500G</p>
                    </div>
                </div>
                <div class="gift-item">
                    <h3>累计投资12万</h3>
                    <div class="row-item select-item <#if prize=="ARCTIC_WOLF_TENT">active</#if>" data-name="ARCTIC_WOLF_TENT">
                        <i class="select-icon left-item"></i>
                        <p class="gift-img three-1"></p>
                        <p>北极狼自动帐篷</p>
                    </div>
                    <div class="row-item">
                        <p class="text-item">兑换</p>
                    </div>
                    <div class="row-item select-item <#if prize=="GOLF_MAN_BRIEFCASE">active</#if>" data-name="GOLF_MAN_BRIEFCASE">
                        <i class="select-icon right-item"></i>
                        <p class="gift-img three-2"></p>
                        <p>GOLF男士商务公文包</p>
                    </div>
                </div>
                <div class="gift-item">
                    <h3>累计投资28万</h3>
                    <div class="row-item select-item <#if prize=="MUTE_SPINNING">active</#if>" data-name="MUTE_SPINNING">
                        <i class="select-icon left-item"></i>
                        <p class="gift-img four-1"></p>
                        <p>伊吉康室内静音动感单车</p>
                    </div>
                    <div class="row-item">
                        <p class="text-item">兑换</p>
                    </div>
                    <div class="row-item select-item <#if prize=="WEIGHTING_DRAW_BAR_BOX">active</#if>" data-name="WEIGHTING_DRAW_BAR_BOX">
                        <i class="select-icon right-item"></i>
                        <p class="gift-img four-2"></p>
                        <p>美而美智能称重拉杆箱</p>
                    </div>
                </div>
                <div class="gift-item">
                    <h3>累计投资38万</h3>
                    <div class="row-item select-item <#if prize=="MANGO_TOURISM_CARD_2000">active</#if>" data-name="MANGO_TOURISM_CARD_2000">
                        <i class="select-icon left-item"></i>
                        <p class="gift-img five-1"></p>
                        <p>2000元芒果旅游卡</p>
                    </div>
                    <div class="row-item">
                        <p class="text-item">兑换</p>
                    </div>
                    <div class="row-item select-item <#if prize=="SMARTISAN_NUTS_PRO">active</#if>" data-name="SMARTISAN_NUTS_PRO">
                        <i class="select-icon right-item"></i>
                        <p class="gift-img five-2"></p>
                        <p>锤子坚果pro</p>
                    </div>
                </div>
                <div class="gift-item">
                    <h3>累计投资60万</h3>
                    <div class="row-item select-item <#if prize=="APPLE_WATCH_SERIES_2">active</#if>" data-name="APPLE_WATCH_SERIES_2">
                        <i class="select-icon left-item"></i>
                        <p class="gift-img six-1"></p>
                        <p>Apple Watch Series 2</p>
                    </div>
                    <div class="row-item">
                        <p class="text-item">兑换</p>
                    </div>
                    <div class="row-item select-item <#if prize=="APPLE_IPAD_128G">active</#if>" data-name="APPLE_IPAD_128G">
                        <i class="select-icon right-item"></i>
                        <p class="gift-img six-2"></p>
                        <p>Apple iPad 128G</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="page-width rule-item clearfix">

        <i class="zs01"></i>
        <i class="zs02"><em>温馨提示</em></i>
        <i class="zs03"></i>
        <i class="zs04"></i>
        <p>1.本活动仅限直投项目，债权转让及新手专享项目不参与累计； <br/>
            2.活动二中不同档的奖品不可同时获得，奖品将以用户活动期间在本活动页面所选择兑换的奖品为准，奖品在活动结束后不可进行添加和更换；<br/>
            3.活动中所有红包、加息券奖励将即时发放，用户可在PC端“我的宝藏”或APP端“优惠券”中进行查看；<br/>
            4.实物奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；<br/>
            5.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；<br/>
            6.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。
        </p>
    </div>


    <#include "../../module/login-tip.ftl" />
    <div class="lottery-tip" id="lotteryTip"></div>
    <script type="text/html" id="lotteryTipTpl">
        <i class="lottery-close close-tip"></i>
        <div class="lottery-content">
        {{if returnCode==0}}
            <p class="info-text">恭喜您抽中了</p>
            <p class="gift-name">{{prizeValue}}</p>
        {{else if returnCode==1}}
            <p class="tip-text">目前没有抽奖机会！</p>
            <p class="tip-text">单笔投资每满1万元</p>
            <p class="tip-text">即可获得抽签机会哦~</p>
        {{else if returnCode==3}}
            <p class="no-chance">不在活动时间范围内</p>
        {{/if}}
        </div>
        <div class="lottery-link">
        {{if returnCode==3}}
            <a href="javascript:void(0)" class="close-tip">知道了</a>
        {{else}}
            <a href="/loan-list">去投资</a>
            <a href="javascript:void(0)" class="close-tip">知道了</a>
        {{/if}}
    </script>
    <div class="exchange-tip" id="exchangeTip"></div>
    <script type="text/html" id="exchangeTipTpl">
        <i class="exchange-close close-tip"></i>
        <div class="exchange-content">
            {{if returnCode==0}}
                <p class="title-item">恭喜您，兑换成功</p>
            {{else if returnCode==1}}
                <p>亲，您还差{{amount}}元</p>
                <p>才能兑换该档次奖励哦~</p>
            {{else if returnCode==2}}
                <p class="tip-info">用户不存在</p>
            {{else if returnCode==3}}
                <p class="tip-info">不在活动时间范围内</p>
            {{else if returnCode==5}}
                <p class="tip-info">您还没有选择奖品哦~</p>
            {{/if}}
        </div>
        <div class="exchange-link">
            {{if returnCode==1}}
                <a href="javascript:void(0)" class="close-item close-tip">知道了</a>
                <a href="/loan-list">去投资</a>
            {{else}}
                <a href="javascript:void(0)" class="exchange-btn close-tip">确定</a>
            {{/if}}
        </div>
    </script>
</div>

</@global.main>