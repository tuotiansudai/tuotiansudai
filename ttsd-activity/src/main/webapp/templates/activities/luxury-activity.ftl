<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.luxury_activity}" pageJavascript="${js.luxury_activity}" activeNav="" activeLeftNav="" title="拓天排行榜_拓天活动_拓天速贷" keywords="拓天速贷,拓天排行榜,拓天天豆,投资拓天,霸道总裁" description="拓天速贷二期活动,投资送天豆,参与抽大奖,拓天速贷霸道总裁送您钱,车,房.">
<div class="rank-list-container">
    <div class="rank-phone-model">
        <img src="${staticServer}/activity/images/sign/actor/luxury/top-banner.png" width="100%">
    </div>
    <div class="wp clearfix">
        <div class="left-bg"></div>
        <div class="right-bg"></div>
        <div class="leader-container hide" id="awardCom">
            <div class="leader-list active">
                <div class="lottery-circle">
                    <h3 class="td-list">
                        <span class="td-total">我的天豆：<strong class="my-td-bean">2414</strong></span>
                        <span class="td-tip">每次抽奖将消耗 1000 天豆</span>
                    </h3>

                    <div class="circle-shade">
                        <div class="pointer-img" id="pointerTd">
                            <img src="${staticServer}/activity/images/sign/actor/ranklist/pointer.png" alt="pointer"/>
                        </div>
                        <div class="rotate-btn">
                            <img id="rotateTd" src="${staticServer}/activity/images/sign/actor/ranklist/turntable.png"
                                 alt="turntable"/>
                        </div>
                    </div>
                </div>
                <div class="lottery-detail">
                    <ul class="gift-record" id="tdChangeBtn">
                        <li class="active">中奖纪录</li>
                        <li>我的奖品</li>
                    </ul>
                    <div class="record-list" id="recordList">
                        <ul class="record-model user-record active" id="TdGiftRecord">
                            <script id="TdGiftRecordTpl" type="text/html">
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
                            </script>
                        </ul>
                        <ul class="record-model own-record" id="TdMyGift">
                            <script id="TdMyGiftTpl" type="text/html">
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
                            </script>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="line-single"></div>
        <div class="actor-info-text">
            <div class="actor-title">
                活动规则
            </div>
            <ul class="rule-list">
                <li>1、活动期间，用户投资即可获得相应天豆；</li>
                <li>2、活动期间，平台达到一定投资额，即对天豆排行榜靠前的用户进行奖励;</li>
                <li>3、用户的天豆数量相同时，按最终累计天豆的先后时间排名；</li>
                <li>4、消费天豆可参与“天豆抽奖”活动，7个工作日内客服联系用户发放奖品；</li>
                <li>5、活动截止后7个工作日内公布排行榜颁奖时间。</li>
            </ul>
            <p class="actor-sign">***活动遵循拓天速贷法律声明，最终解释权归拓天速贷平台所有***</p>
        </div>
    </div>
	<div class="tip-list" id="tipList">
		<div class="tip-dom td-tip-big" id="macbookAir">
			<div class="close-btn go-close"></div>
			<div class="text-tip">
				<p>恭喜你抽中了</p>
				<p><img src="${staticServer}/activity/images/sign/actor/ranklist/macbook-air.png" width="50%"></p>
				<p>拓天客服将会在7个工作日内联系您发放奖品</p>
			</div>
			<div class="btn-list">
				<a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
			</div>
		</div>
        <div class="tip-dom td-tip-small" id="iphone6s">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/activity/images/sign/actor/ranklist/iPhone6sp.png" width="50%"></p>
                <p>拓天客服将会在7个工作日内联系您发放奖品</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="jiaxi">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/activity/images/sign/actor/ranklist/jiaxi-two.png" width="50%"></p>
                <p>奖金已发放至“我的宝藏”当中</p>
            </div>
            <div class="btn-list">
                <a href="/my-treasure" class="double-btn first-btn">去查看</a>
                <a href="javascript:void(0)" class="double-btn go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="jdCard">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/activity/images/sign/actor/ranklist/jdcard.png" width="50%"></p>
                <p>拓天客服将会在7个工作日内联系您发放奖品</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="twentyRMB">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/activity/images/sign/actor/ranklist/20rmb.png" width="50%"></p>
                <p>奖金已发放到您的账户！</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-thank" id="TDnoUse">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>您的天豆不足，</p>
                <p>投资赚取更多天豆再来抽奖吧！</p>
            </div>
            <div class="btn-list">
                <a href="/loan-list" class="go-on">去投资</a>
            </div>
        </div>
        <div class="tip-dom td-tip-thank" id="noLogin">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>您还未登录，</p>
                <p>请登录后再来抽奖吧！</p>
            </div>
            <div class="btn-list">
                <a href="/login?redirect=/activity/rank-list" class="go-on">去登录</a>
            </div>
        </div>
        <div class="tip-dom td-tip-thank" id="NoCdbean">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>您的财豆不足，</p>
                <p>投资赚取更多财豆再来抽奖吧！</p>
            </div>
            <div class="btn-list">
                <a href="/point" class="go-on">去赚财豆</a>
            </div>
        </div>
        <div class="tip-dom td-tip-thank" id="oneDay">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>您今天已经抽过奖啦！</p>
                <p>点击按钮分享页面还可再抽一次哦！</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">去分享</a>
            </div>
        </div>
        <div class="tip-dom td-tip-thank" id="onlyTwice">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>您今天的抽奖次数已经用完啦，</p>
                <p>明天再来抽奖吧！</p>
            </div>
            <div class="btn-list">
                <a href="/" class="go-on-big">去看看其他活动</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="cdFive">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/activity/images/sign/actor/ranklist/5rmb_new.png" width="50%"></p>
                <p>拓天客服将会在7个工作日内联系您发放奖品</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="cdBaowen">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/activity/images/sign/actor/ranklist/baowen.png" width="50%"></p>
                <p>拓天客服将会在7个工作日内联系您发放奖品</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="cdTwo">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/activity/images/sign/actor/ranklist/mangguo.png" width="50%"></p>
                <p>拓天客服将会在7个工作日内联系您发放奖品</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="thankYou">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>

                <p><img src="${staticServer}/activity/images/sign/actor/ranklist/2rmb-new.png" width="50%"></p>

                <p>奖品已发放至"我的宝藏"当中 ！</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="percentCoupon">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/activity/images/sign/actor/ranklist/jiaxi-one.png" width="50%"></p>
                <p>奖金已发放至“我的宝藏”当中</p>
            </div>
            <div class="btn-list">
                <a href="/my-treasure" class="double-btn first-btn">去查看</a>
                <a href="javascript:void(0)" class="double-btn go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="freeMoney">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/activity/images/sign/actor/ranklist/percentTwo.png" width="50%"></p>
                <p>奖金已发放至“我的宝藏”当中</p>
            </div>
            <div class="btn-list">
                <a href="/my-treasure" class="double-btn first-btn">去查看</a>
                <a href="javascript:void(0)" class="double-btn go-close">继续抽奖</a>
            </div>
        </div>
	</div>
</div>
</@global.main>