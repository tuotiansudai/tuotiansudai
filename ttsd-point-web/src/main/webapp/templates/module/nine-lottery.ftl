<div class="nine-lottery-group">
    <div class="lottery-left-group">
        <ul class="lottery-item" id="lotteryBox">
            <li class="lottery-unit lottery-unit-0" data-unit="0">
                <i class="gift-one"></i>
            </li>
            <li class="lottery-unit lottery-unit-1" data-unit="1">
                <i class="gift-two"></i>
            </li>
            <li class="lottery-unit lottery-unit-2" data-unit="2">
                <i class="gift-three"></i>
            </li>
            <li class="lottery-unit lottery-unit-7" data-unit="7">
                <i class="gift-eight"></i>
            </li>
            <li class="lottery-btn">
                <i class="gift-btn"></i>
            </li>
            <li class="lottery-unit lottery-unit-3" data-unit="3">
                <i class="gift-four"></i>
            </li>
            <li class="lottery-unit lottery-unit-6" data-unit="6">
                <i class="gift-seven"></i>
            </li>
            <li class="lottery-unit lottery-unit-5" data-unit="5">
                <i class="gift-six"></i>
            </li>
            <li class="lottery-unit lottery-unit-4" data-unit="4">
                <i class="gift-five"></i>
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
            <script type="text/html" id="recordListTpl">
                {{if recordlist.length>0}}
                {{each recordlist}}
                <li><span>恭喜{{$value.mobile}}抽中了{{$value.prizeValue}}</span></li>
                {{/each}}
                {{else}}
                <li class="record-img">
                    <p class="empty-img"></p>
                    <p>还没有奖品呢，快去抽奖吧！</p>
                </li>
                {{/if}}
            </script>
            <ul class="record-item own-record" id="myRecord" style="display: none"></ul>
            <script type="text/html" id="myRecordTpl">
                {{if myrecord.length>0}}
                {{each myrecord}}
                <li><span class="text-item">{{$value.prizeValue}}</span><span
                        class="time-item">{{$value.lotteryTime}}</span></li>
                {{/each}}
                {{else}}
                <li class="record-img">
                    <p class="empty-img"></p>
                    <p>还没有奖品呢，快去抽奖吧！</p>
                </li>
                {{/if}}
            </script>
        </div>
    </div>
</div>
<div class="tip-list-frame">
    <!-- 积分的提示-->
    <div class="tip-list" id="point">
        <div class="close-btn go-close"></div>
        <div class="text-tip">
            <p class="success-text">恭喜您</p>
            <p class="reward-text">抽中了<em class="prizeValue"></em>！</p>
        </div>
        <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a></div>
    </div>

    <!-- 真实奖品的提示 -->
    <div class="tip-list" id="concrete">
        <div class="close-btn go-close"></div>
        <div class="text-tip">
            <p class="success-text">恭喜您</p>
            <p class="reward-text">抽中了<em class="prizeValue"></em>！</p>
            <p class="des-text">拓天客服将会在7个工作日内联系您发放奖品</p>
        </div>
        <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a></div>
    </div>

    <!-- 虚拟奖品的提示 -->
    <div class="tip-list" id="virtual">
        <div class="close-btn go-close"></div>
        <div class="text-tip">
            <p class="success-text">恭喜您</p>
            <p class="reward-text">抽中了<em class="prizeValue"></em>！</p>
            <p class="des-text">奖品已发放至“我的宝藏”当中。</p>
        </div>
        <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a></div>
    </div>

    <!-- 没有抽奖机会 -->
    <div class="tip-list" id="nochance">
        <div class="close-btn go-close"></div>
        <div class="text-tip">
            <p class="login-text">您暂无抽奖机会啦～</p>
            <p class="des-text">赢取机会后再来抽奖吧！</p>
        </div>
        <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
    </div>

    <!-- 不在活动时间范围内 -->
    <div class="tip-list" id="expired">
        <div class="close-btn go-close"></div>
        <div class="text-tip">
            <p class="login-text">不在活动时间内~</p>
        </div>
        <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
    </div>

    <!-- 实名认证 -->
    <div class="tip-list" id="authentication">
        <div class="close-btn go-close"></div>
        <div class="text-tip">
            <p class="login-text">您还未实名认证~</p>
            <p class="des-text">请实名认证后再来抽奖吧！</p>
        </div>
        <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
    </div>

    <div class="tip-list" id="frequentOperation">
        <div class="close-btn go-close"></div>
        <div class="text-tip">
            <p class="login-text">您的操作太频繁，请稍后再试！</p>
        </div>
        <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
    </div>
    <div class="tip-list" id="pointChangingFail">
        <div class="close-btn go-close"></div>
        <div class="text-tip">
            <p class="login-text">抽奖失败，请稍后再试！</p>
        </div>
        <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
    </div>
</div>