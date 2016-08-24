<div class="leader-container tour-theme" id="awardCom">
    <div class="leader-list">
        <div class="lottery-circle">
            <h3>我的抽奖机会：1次</h3>
            <div class="circle-shade">
                <div class="pointer-img" id="pointer">
                    <img src="${staticServer}/activity/images/sign/actor/circle/pointer.png" alt="pointer" width="100%"/>
                </div>
                <div class="rotate-btn" id="rotate">
                    <img src="${staticServer}/activity/images/sign/actor/circle/luxury-gift.png" alt="turntable" width="100%" class="luxury-img"/>
                    <img src="${staticServer}/activity/images/sign/actor/circle/tour-gift.png" alt="pointer" width="100%" class="tour-img"/>
                </div>
            </div>
        </div>
        <div class="lottery-detail">
            <h3>我的抽奖机会：1次</h3>
            <ul class="gift-record">
                <li class="active">中奖纪录</li>
                <li>我的奖品</li>
            </ul>
            <div class="record-list" id="recordList">
                <ul class="record-model user-record active" id="GiftRecord">
                    <!-- <script id="GiftRecordTpl" type="text/html">
                    {{each other}}
                        <li>恭喜 {{$value.loginName}} 抽中了 
                        {{if $value.prize=='InterestCoupon5'}}
                        0.5%加息券
                        {{else if $value.prize=='Cash20'}}
                        20元现金
                        {{else if $value.prize=='JingDong300'}}
                        300元京东购物卡
                        {{else if $value.prize=='Iphone6s'}}
                        iPhone 6s Plus
                        {{else if $value.prize=='MacBook'}}
                        MacBook Air
                        {{/if}}
                        .</li>
                    {{/each}}
                    </script> -->
                </ul>
                <ul class="record-model own-record" id="MyGift">
                    <!-- <script id="MyGiftTpl" type="text/html">
                    {{if tdmygift}}
                    {{each tdmygift}}
                    <li>
                        <span class="award-name">
                            {{if $value.prize=='InterestCoupon5'}}
                            0.5%加息券
                            {{else if $value.prize=='Cash20'}}
                            20元现金
                            {{else if $value.prize=='JingDong300'}}
                            300元京东购物卡
                            {{else if $value.prize=='Iphone6s'}}
                            iPhone 6s Plus
                            {{else if $value.prize=='MacBook'}}
                            MacBook Air
                            {{/if}}
                        </span>
                        <span class="award-time">{{$value.time}}</span>
                    </li>
                    {{/each}}
                    {{else}}
                    <li class="empty-text">您暂时还没有奖品，快去抽奖吧！</li>
                    {{/if}}
                    </script> -->
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="tip-list tour-theme" id="tipList">
    <div class="tip-dom">
        <div class="close-btn go-close"></div>
        <div class="text-tip">
            <p>恭喜您！</p>
            <p>恭喜你抽中了0.5%加息券！</p>
            <p class="des-text">拓天客服将会在7个工作日内联系您发放奖品</p>
        </div>
        <div class="btn-list">
            <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
        </div>
    </div>
</div>