<div class="leader-container <#if activityType= 'luxury'></#if><#if activityType= 'travel'>tour-theme</#if><#if activityType= 'national'>national-day-theme</#if>"   id="awardCom"><!--旅游：tour-theme，国庆：national-day-theme  -->
    <div class="leader-list">
        <div class="lottery-circle">
            <h3>我的抽奖机会：<span class="lottery-time">${drawTime}</span>次</h3>
            <div class="circle-shade">
                <div class="pointer-img" id="pointer" data-islogin="true">
                    <img src="${staticServer}/activity/images/sign/actor/circle/pointer.png" alt="pointer" width="100%"/>
                </div>
                <div class="rotate-btn" id="rotate">
                    <img src="${staticServer}/activity/images/sign/actor/circle/luxury-gift-new.png" alt="turntable" width="100%" class="luxury-img"/>
                    <img src="${staticServer}/activity/images/sign/actor/circle/tour-gift.png" alt="pointer" width="100%" class="tour-img"/>
                    <img src="${staticServer}/activity/images/sign/actor/circle/national-day-gift.png" alt="pointer" width="100%" class="national-img"/>
                </div>
            </div>
        </div>
        <div class="lottery-detail">
            <h3>我的抽奖机会：<span class="lottery-time">${drawTime}</span>次</h3>
            <ul class="gift-record">
                <li class="active"><span>中奖记录</span></li>
                <li><span>我的奖品</span></li>
            </ul>
            <div class="record-list" id="recordList">
                <ul class="record-model user-record active" id="GiftRecord">
                    <script id="GiftRecordTpl" type="text/html">
                    {{each record}}
                        <li>恭喜 {{$value.mobile}} 抽中了
                        {{if $value.prize=='TOURISM'}}
                            华东旅游大奖
                        {{else if $value.prize=='MANGO_CARD_100'}}
                            100元芒果卡
                        {{else if $value.prize=='LUXURY'}}
                            奢侈品大奖
                        {{else if $value.prize=='PORCELAIN_CUP'}}
                            青花瓷杯子
                        {{else if $value.prize=='RED_ENVELOPE_100'}}
                            100元现金红包
                        {{else if $value.prize=='RED_ENVELOPE_50'}}
                            50元现金红包
                        {{else if $value.prize=='INTEREST_COUPON_5'}}
                            0.5%加息券
                        {{else if $value.prize=='INTEREST_COUPON_2'}}
                            0.2%加息券
                        {{/if}}
                        .</li>
                    {{/each}}
                    </script>
                </ul>
                <ul class="record-model own-record" id="MyGift">

                    <script id="MyGiftTpl" type="text/html">
                    {{if gift}}
                    {{each gift}}
                    <li>
                        <span class="award-name">
                            {{if $value.prize=='TOURISM'}}
                                华东旅游大奖
                            {{else if $value.prize=='MANGO_CARD_100'}}
                                100元芒果卡
                            {{else if $value.prize=='LUXURY'}}
                                奢侈品大奖
                            {{else if $value.prize=='PORCELAIN_CUP'}}
                                青花瓷杯子
                            {{else if $value.prize=='RED_ENVELOPE_100'}}
                                100元现金红包
                            {{else if $value.prize=='RED_ENVELOPE_50'}}
                                50元现金红包
                            {{else if $value.prize=='INTEREST_COUPON_5'}}
                                0.5%加息券
                            {{else if $value.prize=='INTEREST_COUPON_2'}}
                                0.2%加息券
                            {{/if}}
                        </span>
                        <span class="award-time">{{$value.lotteryTime}}</span>
                    </li>
                    {{/each}}
                    {{else}}
                    <li class="empty-text">您暂时还没有奖品，快去抽奖吧！</li>
                    {{/if}}
                    </script>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="tip-list <#if activityType= 'luxury'></#if><#if activityType= 'travel'>tour-theme</#if><#if activityType= 'national'>national-day-theme</#if>" id="tipList"><!--旅游:tour-theme,国庆：national-day-theme   -->
    <script id="tipListTpl" type="text/html">
    <div class="tip-dom">
        <div class="close-btn go-close"></div>
        <div class="text-tip">
        {{if istype=='real'}}
            <p>恭喜您！</p>
            <p>{{tiptext}}！</p>
            <p class="des-text">拓天客服将会在7个工作日内联系您发放奖品</p>
        {{else if istype=='virtual'}}
            <p>恭喜您！</p>
            <p>{{tiptext}}！</p>
            <p class="des-text">奖品已发放至“我的宝藏”当中。</p>
        {{else if istype=='nologin'}}
            <p class="login-text">您还未登录~</p>
            <p class="des-text">请登录后再来抽奖吧！</p>
        {{else if istype=='notimes'}}
            <p class="login-text">您暂无抽奖机会啦~</p>
            <p class="des-text">赢取机会后再来抽奖吧！</p>
        {{else if istype=='timeout'}}
            <p class="login-text">不在活动时间内~</p>
            <p class="des-text">不在活动时间内！</p>
        {{else}}
        {{/if}}
        </div>
        <div class="btn-list">
            {{if istype=='real'}}
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            {{else if istype=='virtual'}}
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            {{else if istype=='nologin'}}
                <a href="/login?redirect=/activity/autumn/" class="go-on">去登录</a>
            {{else if istype=='notimes'}}
                <a href="javascript:void(0)" class="go-on go-close">知道了</a>
            {{else}}
            {{/if}}
        </div>
    </div>
    </script>
</div>