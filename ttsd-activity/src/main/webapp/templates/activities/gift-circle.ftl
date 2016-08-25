<div class="leader-container <#if prizeType='travel'>tour-theme</#if>" id="awardCom"><!--旅游主题class替换为tour-theme-->
    <div class="leader-list">
        <div class="lottery-circle">
            <h3>我的抽奖机会：1次</h3>
            <div class="circle-shade">
                <div class="pointer-img" id="pointer" data-islogin="true">
                    <img src="${staticServer}/activity/images/sign/actor/circle/pointer.png" alt="pointer" width="100%"/>
                </div>
                <div class="rotate-btn" id="rotate">
                    <img src="${staticServer}/activity/images/sign/actor/circle/luxury-gift.png" alt="turntable" width="100%" class="luxury-img"/>
                    <img src="${staticServer}/activity/images/sign/actor/circle/tour-gift.png" alt="pointer" width="100%" class="tour-img"/>
                </div>
            </div>
        </div>
        <div class="lottery-detail">
            <h3>我的抽奖机会：${drawTime}次</h3>
            <ul class="gift-record">
                <li class="active">中奖纪录</li>
                <li>我的奖品</li>
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
                            0.5加息券
                        {{else if $value.prize=='INTEREST_COUPON_2'}}
                            0.2加息券
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
                                0.5加息券
                            {{else if $value.prize=='INTEREST_COUPON_2'}}
                                0.2加息券
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
<div class="tip-list" id="tipList"><!--旅游主题class替换为tour-theme-->
    <script id="tipListTpl" type="text/html">
    <div class="tip-dom">
        <div class="close-btn go-close"></div>
        <div class="text-tip">
            <p>恭喜您！</p>
            <p>{{tiptext}}！</p>
            <p class="des-text">拓天客服将会在7个工作日内联系您发放奖品</p>
        </div>
        <div class="btn-list">
            <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
        </div>
    </div>
    </script>
</div>