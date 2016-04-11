<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.company_activity}" pageJavascript="${js.rank_list}" activeNav="" activeLeftNav="" title="拓天排行榜_拓天活动_拓天速贷" keywords="拓天速贷,拓天排行榜,拓天天豆,投资拓天,霸道总裁" description="拓天速贷二期活动,投资送天豆,参与抽大奖,拓天速贷霸道总裁送您钱,车,房.">
<div class="rank-list-container">
    <div class="rank-phone-model">
        <img src="${staticServer}/images/sign/actor/ranklist/rank-list-top.png" width="100%">
    </div>
    <div class="wp clearfix actor-intro">
        <div class="left-bg"></div>
        <div class="right-bg"></div>
        <div class="line-single"></div>
        <div class="actor-info-text">
            <div class="actor-title">
                活动说明
            </div>
            <p>活动期间，投资即可获得相应天豆，天豆可冲击排行榜拿排名大奖。</p>

            <p>用户还可消费天豆参与<span>“百分百中奖”</span>的抽奖活动。</p>
        </div>
        <div class="line-single"></div>

        <@global.isAnonymous>
            <div class="login-list clearfix">
                <a href="/register/user">立即注册</a>
                <a href="/login">直接登录</a>
            </div>
        </@global.isAnonymous>
        <@global.isNotAnonymous>
            <div class="user-info">
                <ul>
                    <li>
                        <p>
                            <span class="project-name">我的天豆：<strong class="my-td-bean">${myTianDou?c}</strong></span>
                            <a href="javascript:void(0)" class="project-operate" id="myTD">去抽奖</a>
                        </p>
                    </li>
                    <li>
                        <p>
                            <span class="project-name">我的财豆：<strong class="my-cd-bean">${myPoint?string('0')!}</strong></span>
                            <a href="javascript:void(0)" class="project-operate" id="myCD">去抽奖</a>
                        </p>
                    </li>
                    <li>
                        <p>
                            <span class="project-name">我的排名：<#if myRank??>${myRank}<#else>-</#if></span>
                            <a href="/loan-list" class="project-operate">去投资</a>
                        </p>
                    </li>
                </ul>
            </div>
        </@global.isNotAnonymous>
        <div class="line-single"></div>
        <ul class="leader-btn" id="beanBtn">
            <li class="active">排行榜</li>
            <li>奖品单</li>
            <div class="bean-btn">
                <span class="cal-title" id="calBtn">天豆计算器</span>
                <div class="td-calculator">
                    <h3>
                        <span>天豆计算器</span>
                        <i class="close-cal"></i>
                    </h3>
                    <div class="td-content">
                    <form id="countForm" action="">
                        <p>天豆=投资金额x期数/12</p>
                        <div class="form-text">
                            <input type="text" placeholder="投资金额" class="int-text" name="money" id="moneyNum">
                            <span class="unit-text">元</span>
                        </div>
                        <div class="form-text">
                            <input type="text" placeholder="投资时长" class="int-text" name="month" id="monthNum">
                            <span class="unit-text">期</span>
                        </div>
                        <div class="form-text">
                            <input class="submit-btn" type="submit" value="计算">
                            <input class="reset-btn" type="reset" value="重置" id="resetBtn">
                        </div>
                        <div class="form-text">
                            <p class="result-text">天豆数： <span id="resultNum">0</span> 个</p>
                        </div>
                    </form>
                    </div>
                </div>
            </div>
        </ul>
        <div class="leader-container" id="beanCom">
            <div class="leader-list active">
                <div class="bean-rank-list">
                    <h3 class="td-list">
                        <span class="td-name">天豆排行榜</span>
                        <span class="td-intro">（前十五名）</span>
                    </h3>
                    <dl class="bean-data">
                        <dt>
                            <span class="order-num">名次</span>
                            <span class="bean-num">天豆数</span>
                            <span class="bean-user">用户</span>
                        </dt>
                        <dd id="rankList">
                        <script type="text/html" id="rankListTpl">
                        {{each rank}}
                            <span class="order-num"><i class="font-icon">{{$index+1}}</i></span>
                            <span class="bean-num">{{$value.loginName}}</span>
                            <span class="bean-user">{{$value.score}}</span>
                        {{/each}}
                        </script>
                        </dd>
                    </dl>
                </div>
                <div class="bean-rank-money">
                    <div class="progress-line">
                        <h3 class="total-money">累积投资：${totalInvest?float} 元</h3>
                        <dl class="gift-two">
                            <dt>奖池累积中...</br>投资满<span>1.5亿</span>则奖池升级:</dt>
                            <dd>第一名：现金5万</dd>
                            <dd>第二名：现金3万</dd>
                            <dd>第三名：日韩双人游</dd>
                            <dd>第四名：欧洲游</dd>
                            <dd>第五名：海岛游</dd>
                        </dl>
                        <dl class="gift-one">
                            <dt>奖池累积中...</br>投资满<span>8,500万</span>则奖励:</dt>
                            <dd>第一名：现金5万</dd>
                            <dd>第二名：现金3万</dd>
                            <dd>第三名：日韩双人游</dd>
                        </dl>
                        <div class="stage-num first-stage">
                            <span>8,500万</span><i class="line-single"></i>
                        </div>
                        <div class="stage-num two-stage">
                            <span>8,500万</span><i class="line-single"></i>
                        </div>
                        <div class="color-line">
                            <div class="color-pro" data-totalInvest="${totalInvest?c}" id="linePro"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="leader-list">
                <div class="table-gift">
                    <img src="${staticServer}/images/sign/actor/ranklist/gift-table.png" width="100%">
                </div>
            </div>
        </div>
        <div class="line-single"></div>
        <ul class="leader-btn" id="awardBtn">
            <li class="active">天豆抽奖</li>
            <li>财豆抽奖</li>
            <div class="share-list">
                <p class="bdsharebuttonbox">
                    <span class="share-text">分享至：</span>
                    <a href="#" class="share-icon icon-weibo" data-cmd="tsina"></a>
                    <a href="#" class="share-icon icon-weixin" data-cmd="weixin"></a>
                    <a href="#" class="share-icon icon-zone" data-cmd="qzone"></a>
                </p>
            </div>
        </ul>
        <div class="leader-container" id="awardCom">
            <div class="leader-list active">
                <div class="lottery-circle">
                    <h3 class="td-list">
                        <span class="td-total">我的天豆：<strong class="my-td-bean">${myTianDou?c}</strong></span>
                        <span class="td-tip">每次抽奖将消耗 1000 天豆</span>
                    </h3>

                    <div class="circle-shade">
                        <div class="pointer-img" id="pointerTd" data-is-login="<@global.isNotAnonymous>true</@global.isNotAnonymous>">
                            <img src="${staticServer}/images/sign/actor/ranklist/pointer.png" alt="pointer"/>
                        </div>
                        <div class="rotate-btn">
                            <img id="rotateTd" src="${staticServer}/images/sign/actor/ranklist/turntable.png"
                                 alt="turntable"/>
                        </div>
                    </div>
                </div>
                <div class="lottery-detail">
                    <ul class="gift-record" id="tdChangeBtn">
                        <@global.isAnonymous>
                            <li class="active" style="width:336px;">中奖纪录</li>
                        </@global.isAnonymous>
                        <@global.isNotAnonymous>
                            <li class="active">中奖纪录</li>
                            <li>我的奖品</li>
                        </@global.isNotAnonymous>
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
            <div class="leader-list">
                <div class="lottery-circle">
                    <h3 class="td-list share-num">
                        <span class="td-total">我的财豆：<strong class="my-cd-bean">${myPoint?c}</strong></span>
                        <span class="td-tip">每次抽奖将消耗1000财豆，每天限抽一次。(分享可增加一次财豆抽奖机会！)</span>
                    </h3>

                    <div class="circle-shade">
                        <div class="pointer-img" id="pointerCd" data-is-login="<@global.isNotAnonymous>true</@global.isNotAnonymous>">
                            <img src="${staticServer}/images/sign/actor/ranklist/pointer.png" alt="pointer"/>
                        </div>
                        <div class="rotate-btn">
                            <img id="rotateCd" src="${staticServer}/images/sign/actor/ranklist/gift-list-cd.png"
                                 alt="turntable"/>
                        </div>
                    </div>
                </div>
                <div class="lottery-detail">
                    <ul class="gift-record" id="cdChangeBtn">
                        <@global.isAnonymous>
                            <li class="active" style="width:336px;">中奖纪录</li>
                        </@global.isAnonymous>
                        <@global.isNotAnonymous>
                            <li class="active">中奖纪录</li>
                            <li>我的奖品</li>
                        </@global.isNotAnonymous>
                    </ul>
                    <div class="record-list scroll-record" id="beanList">
                        <ul class="record-model user-record  active" id="CdGiftRecord">
                            <script type="text/html" id="CdGiftRecordTpl">
                            {{each cdgiftrecord}}
                            <li>恭喜 {{$value.loginName}} 抽中了 {{$value.pointPrizeName}}</li>
                            {{/each}}
                            </script>
                        </ul>
                        <ul class="record-model own-record" id="CdMyGift">
                            <script type="text/html" id="CdMyGiftTpl">
                            {{if cdmygift}}
                            {{each cdmygift}}
                            <li>
                                <span class="award-name">{{$value.pointPrizeName}}</span>
                                <span class="award-time">{{$value.createTime}}</span>
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
				<p><img src="${staticServer}/images/sign/actor/ranklist/macbook-air.png" width="50%"></p>
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
                <p><img src="${staticServer}/images/sign/actor/ranklist/iPhone6sp.png" width="50%"></p>
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
                <p><img src="${staticServer}/images/sign/actor/ranklist/jiaxi-two.png" width="50%"></p>
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
                <p><img src="${staticServer}/images/sign/actor/ranklist/jdcard.png" width="50%"></p>
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
                <p><img src="${staticServer}/images/sign/actor/ranklist/20rmb.png" width="50%"></p>
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
                <p><img src="${staticServer}/images/sign/actor/ranklist/5rmb.png" width="50%"></p>
                <p>奖金已发放到您的账户！</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="cdTwo">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/images/sign/actor/ranklist/2rmb.png" width="50%"></p>
                <p>奖金已发放到您的账户！</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-thank" id="thankYou">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>谢谢参与</p>
                <p>很遗憾没有中奖,再接再励！</p>
            </div>
            <div class="btn-list">
                <a href="/" class="go-on-big">去看看其他活动</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="percentCoupon">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/images/sign/actor/ranklist/jiaxi-one.png" width="50%"></p>
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
                <p><img src="${staticServer}/images/sign/actor/ranklist/coupon.png" width="50%"></p>
                <p>奖金已发放至“我的宝藏”当中</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
	</div>
</div>
<div class="rank-list-phone">
    <div class="rank-phone-model">
        <img src="${staticServer}/images/sign/actor/ranklist/top-title.png" width="100%">
    </div>
    <div class="rank-phone-intro">
        <img src="${staticServer}/images/sign/actor/ranklist/actor-intro-app.png" width="100%" class="actor-intro">
    </div>
    <div class="login-btn-phone">
    <@global.isAnonymous>
        <p>
            <a href="/register/user">立即注册</a>
        </p>
        <p>
            <a href="/login">直接登录</a>
        </p>
    </@global.isAnonymous>
    <@global.isNotAnonymous>
        <ul class="user-info-phone">
            <li>
                <span class="project-name">我的天豆：<strong class="myphone-td-bean">${myTianDou?c}</strong></span>
                <span class="btn-gift-phone" id="myTDPhone">去抽奖</span>
            </li>
            <li>
                <span class="project-name">我的财豆：<strong class="myphone-cd-bean">${myPoint?string('0')!}</strong></span>
                <span class="btn-gift-phone" id="myCDPhone">去抽奖</span>
            </li>
            <li>
                <span class="project-name">我的排名：<#if myRank??>${myRank}<#else>-</#if></span>
            </li>
        </ul>
    </@global.isNotAnonymous>
    </div>
    <div class="bean-rank-list">
        <h3><i class="left-circle"></i><span>天豆排行榜</span><i class="right-circle"></i></h3>
        <dl class="max-bean-rank">
            <dt>
                <span><strong class="rank-num">名次</strong></span>
                <span><strong class="rank-beans">天豆数</strong></span>
                <span><strong class="rank-user">用户</strong></span>
            </dt>
            <dd id="rankListPhone">
            <script type="text/html" id="rankListPhoneTpl">
                {{each rank}}
                <span>{{$index+1}}</span>
                <span>{{$value.loginName}}</span>
                <span>{{$value.score}}</span>
                {{/each}}
            </script>
            </dd>
        </dl>
    </div>
    <div class="change-rank-list">
        <div class="change-btn-list" id="awardBtnPhone">
            <i class="left-circle"></i><span><strong class="active">天豆抽奖</strong><strong>财豆抽奖</strong></span><i class="right-circle"></i>
        </div>
        <div class="change-gift-com" id="changeGift">
            <div class="circle-list active">
                <div class="user-info">
                    <p>我的天豆：<span class="myphone-td-bean">${myTianDou?c}</span></p>
                    <p>每次抽奖将消耗1000天豆</p>
                </div>
                <div class="gift-circle">
                    <div class="max-gift">
                        <div class="pointer-img" id="pointerTdPhone" data-is-login="<@global.isNotAnonymous>true</@global.isNotAnonymous>">
                            <img src="${staticServer}/images/sign/actor/ranklist/pointer.png" width="100%" alt="pointer"/>
                        </div>
                        <div class="rotate-btn">
                            <img id="rotateTdPhone" src="${staticServer}/images/sign/actor/ranklist/turntable.png"
                                 width="100%" alt="turntable"/>
                        </div>
                    </div>
                </div>
                <div class="gift-record">
                    <ul class="td-record" id="TdGiftRecordPhone">
                        <script id="TdGiftRecordPhoneTpl" type="text/html">
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
                </div>
                <@global.isNotAnonymous>
                <div class="my-record">
                    <dl id="TdMyGiftPhone"></dl>
                        <script id="TdMyGiftPhoneTpl" type="text/html">
                        <dt><span>我的奖品</span><i class="fa fa-angle-up"></i><i class="fa fa-angle-down"></i></dt>
                        {{if tdmygift.length>0}}
                        {{each tdmygift}}
                        <dd>
                            <span class="gift-name">
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
                            <span class="gift-time">{{$value.time}}</span>
                        </dd>
                        {{/each}}
                        {{else}}
                        <dd><span class="gift-name">您暂时还没有奖品，快去抽奖吧！</span></dd>
                        {{/if}}
                        </script>
                    
                </div>
                </@global.isNotAnonymous>
            </div>
            <div class="circle-list">
                <div class="user-info">
                    <p>我的财豆：<span class="myphone-td-bean">${myPoint?string('0')!}</span></p>
                    <p>每次抽奖将消耗1000财豆</p>
                </div>
                <div class="gift-circle">
                    <div class="max-gift">
                        <div class="pointer-img" id="pointerCdPhone" data-is-login="<@global.isNotAnonymous>true</@global.isNotAnonymous>">
                            <img src="${staticServer}/images/sign/actor/ranklist/pointer.png" width="100%" alt="pointer"/>
                        </div>
                        <div class="rotate-btn">
                            <img id="rotateCdPhone" src="${staticServer}/images/sign/actor/ranklist/gift-list-cd.png"
                                 width="100%" alt="turntable"/>
                        </div>
                    </div>
                </div>
                <div class="gift-record">
                    <ul class="td-record" id="CdGiftRecordPhone">
                        <script type="text/html" id="CdGiftRecordPhoneTpl">
                            {{each cdgiftrecord}}
                            <li>恭喜 {{$value.loginName}} 抽中了 {{$value.pointPrizeName}}</li>
                            {{/each}}
                        </script>
                    </ul>
                </div>
                <@global.isNotAnonymous>
                <div class="my-record">
                    <dl id="CdMyGiftPhone">
                        <script type="text/html" id="CdMyGiftPhoneTpl">
                        <dt><span>我的奖品</span><i class="fa fa-angle-up"></i><i class="fa fa-angle-down"></i></dt>
                        {{if cdmygift.length>0}}
                        {{each cdmygift}}
                        <dd><span class="gift-name">{{$value.pointPrizeName}}</span><span class="gift-time">{{$value.createTime}}</span></dd>
                        {{/each}}
                        {{else}}
                        <dd><span class="gift-name">您暂时还没有奖品，快去抽奖吧！</span></dd>
                        {{/if}}
                        </script>
                    </dl>
                </div>
                </@global.isNotAnonymous>
            </div>
        </div>
    </div>
    <div class="rank-phone-intro">
        <span class="fl" style="padding:0 30px;"><img src="${staticServer}/images/sign/actor/ranklist/share-button.png" width="100%" class="fl share-btn"></span>
        <img src="${staticServer}/images/sign/actor/ranklist/share-intro-phone.png" width="100%">
    </div>
    <#if !isAppSource>
    <div class="share-list">
        <p class="bdsharebuttonbox">
            <span class="share-text">分享至：</span>
            <a href="#" class="share-icon icon-weibo" data-cmd="tsina"></a>
            <a href="#" class="share-icon icon-zone" data-cmd="qzone"></a>
        </p>
    </div>
    </#if>
    <div class="bean-rank-list">
        <h3><i class="left-circle"></i><span>奖池直播</span><i class="right-circle"></i></h3>
        <div class="money-online">
            <div class="progress-line">
                <h3 class="total-money">累积投资：${totalInvest/100?float} 元</h3>
                <dl class="gift-two">
                    <dt>奖池累积中...</br>投资满<span>1.5亿</span>则奖池升级:</dt>
                    <dd>第一名：现金5万</dd>
                    <dd>第二名：现金3万</dd>
                    <dd>第三名：日韩双人游</dd>
                    <dd>第四名：欧洲游</dd>
                    <dd>第五名：海岛游</dd>
                </dl>
                <dl class="gift-one">
                    <dt>奖池累积中...</br>投资满<span>8,500万</span>则奖励:</dt>
                    <dd>第一名：现金5万</dd>
                    <dd>第二名：现金3万</dd>
                    <dd>第三名：日韩双人游</dd>
                </dl>
                <div class="stage-num first-stage">
                    <span>8,500万</span><i class="line-single"></i>
                </div>
                <div class="stage-num two-stage">
                    <span>8,500万</span><i class="line-single"></i>
                </div>
                <div class="color-line">
                    <div class="color-pro" data-totalInvest="${totalInvest?c}" id="lineProPhone"></div>
                </div>
            </div>
        </div>
    </div>
    <#if !isAppSource>
    <div class="rank-phone-intro">
        <a href="/loan-list"><img src="${staticServer}/images/sign/actor/ranklist/to-loan.png" width="100%" class="share-intro"></a>
    </div>
    </#if>
    <dl class="actor-rule">
        <dt>活动规则：</dt>
        <dd>1. 活动期间，用户投资即可获得相应天豆；</dd>
        <dd>2. 活动期间，平台达到一定投资额，即对天豆排行榜靠前的用户进行奖励;</dd>
        <dd>3. 用户的天豆数量相同时，按最终累计天豆的先后时间排名；</dd>
        <dd>4. 消费天豆可参与“天豆抽奖”活动，7个工作日内客服联系用户发放奖品；</dd>
        <dd>5. 活动截止后7个工作日内公布排行榜颁奖时间。</dd>
    </dl>
    <div class="actor-write">
        <p>活动遵循拓天速贷法律声明，</p>
        <p>最终解释权归拓天速贷平台所有。</p>
    </div>
    <div class="tip-list" id="tipListPhone">
        <div class="tip-dom td-tip-big" id="macbookAirPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/images/sign/actor/ranklist/macbook-air.png" width="50%"></p>
                <p>拓天客服将会在7个工作日内联系您发放奖品</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="iphone6sPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/images/sign/actor/ranklist/iPhone6sp.png" width="50%"></p>
                <p>拓天客服将会在7个工作日内联系您发放奖品</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="jiaxiPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/images/sign/actor/ranklist/jiaxi-two.png" width="50%"></p>
                <p>奖金已发放至“我的宝藏”当中</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="jdCardPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/images/sign/actor/ranklist/jdcard.png" width="50%"></p>
                <p>拓天客服将会在7个工作日内联系您发放奖品</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="twentyRMBPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/images/sign/actor/ranklist/20rmb.png" width="50%"></p>
                <p>奖金已发放到您的账户！</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-thank" id="TDnoUsePhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>您的天豆不足，</p>
                <p>投资赚取更多天豆再来抽奖吧！</p>
            </div>
        </div>
        <div class="tip-dom td-tip-thank" id="noLoginPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>您还未登录，</p>
                <p>请登录后再来抽奖吧！</p>
            </div>
            <div class="btn-list">
                <a href="/login?redirect=/activity/rank-list" class="go-on">去登录</a>
            </div>
        </div>
        <div class="tip-dom td-tip-thank" id="NoCdbeanPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>您的财豆不足，</p>
                <p>投资赚取更多财豆再来抽奖吧！</p>
            </div>
        </div>
        <div class="tip-dom td-tip-thank" id="oneDayPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>您今天已经抽过奖啦！</p>
                <p>点击按钮分享页面还可再抽一次哦！</p>
            </div>
            <#if !isAppSource>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">去分享</a>
            </div>
            </#if>
        </div>
        <div class="tip-dom td-tip-thank" id="onlyTwicePhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>您今天的抽奖次数已经用完啦，</p>
                <p>明天再来抽奖吧！</p>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="cdFivePhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/images/sign/actor/ranklist/5rmb.png" width="50%"></p>
                <p>奖金已发放到您的账户！</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="cdTwoPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/images/sign/actor/ranklist/2rmb.png" width="50%"></p>
                <p>奖金已发放到您的账户！</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-thank" id="thankYouPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>谢谢参与</p>
                <p>很遗憾没有中奖,再接再励！</p>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="percentCouponPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/images/sign/actor/ranklist/jiaxi-one.png" width="50%"></p>
                <p>奖金已发放至“我的宝藏”当中</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
        <div class="tip-dom td-tip-small" id="freeMoneyPhone">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p>恭喜你抽中了</p>
                <p><img src="${staticServer}/images/sign/actor/ranklist/coupon.png" width="50%"></p>
                <p>奖金已发放至“我的宝藏”当中</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
            </div>
        </div>
    </div>
</div>
</@global.main>