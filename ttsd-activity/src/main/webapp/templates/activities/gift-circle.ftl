<div class="leader-container <#if activityType= 'luxury'></#if><#if activityType= 'travel'>tour-theme</#if>"  id="awardCom"><!--旅游：tour-theme  -->
    <div class="leader-list">
    <input type="hidden" val="${activityType}" id="themeType">
        <div class="lottery-circle">
            <h3>我的抽奖机会：<span class="lottery-time">${drawTime}</span>次</h3>
            <div class="circle-shade">
                <div class="pointer-img" id="pointer" data-islogin="true"></div>
                <div class="rotate-btn" id="rotate"></div>
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
                        {{else if $value.prize=='MEMBERSHIP_V5'}}
                            1个月V5会员
                        {{else if $value.prize=='RED_INVEST_15'}}
                            15元投资红包
                        {{else if $value.prize=='RED_INVEST_50'}}
                            50元投资红包
                        {{else if $value.prize=='TELEPHONE_FARE_10'}}
                            10元话费
                        {{else if $value.prize=='IQIYI_MEMBERSHIP'}}
                            1个月爱奇艺会员
                        {{else if $value.prize=='CINEMA_TICKET'}}
                            电影票一张
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
                            {{else if $value.prize=='MEMBERSHIP_V5'}}
                                1个月V5会员
                            {{else if $value.prize=='RED_INVEST_15'}}
                                15元投资红包
                            {{else if $value.prize=='RED_INVEST_50'}}
                                50元投资红包
                            {{else if $value.prize=='TELEPHONE_FARE_10'}}
                                10元话费
                            {{else if $value.prize=='IQIYI_MEMBERSHIP'}}
                                1个月爱奇艺会员
                            {{else if $value.prize=='CINEMA_TICKET'}}
                                电影票一张
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

<div class="tip-list <#if activityType= 'luxury'></#if><#if activityType= 'travel'>tour-theme</#if>" id="tipList"><!--旅游:tour-theme  -->

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
        {{else if istype=='membership'}}
            <p>恭喜您！</p>
            <p>{{tiptext}}！</p>
            <p class="des-text">奖品即时生效，赶快去投资吧！</p>
        {{else if istype=='nologin'}}
            <p class="login-text">您还未登录~</p>
            <p class="des-text">请登录后再来抽奖吧！</p>
        {{else if istype=='notimes'}}
            <p class="login-text">您暂无抽奖机会啦~</p>
            <p class="des-text">赢取机会后再来抽奖吧！</p>
        {{else if istype=='timeout'}}
            <p class="login-text">不在活动时间内~</p>
            <p class="des-text">不在活动时间内！</p>
        {{/if}}
        </div>
        <div class="btn-list">
            {{if istype=='real'}}
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            {{else if istype=='virtual'}}
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            {{else if istype=='membership'}}
                <a href="/loan-list" class="go-on">去投资</a>
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            {{else if istype=='nologin'}}
                <a href="/login?redirect=/activity/autumn/" class="go-on">去登录</a>
            {{else if istype=='notimes'}}
                <a href="javascript:void(0)" class="go-on go-close">知道了</a>
            {{/if}}
        </div>
    </div>
    </script>
</div>