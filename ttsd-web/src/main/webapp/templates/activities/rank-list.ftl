<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.company_activity}" pageJavascript="${js.rank_list}" activeNav="" activeLeftNav="" title="排行榜">
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
                        <#list tianDouTop15 as tianDouRank>
                            <dd>
                                <span class="order-num"><i class="font-icon">${tianDouRank?index+1}</i></span>
                                <span class="bean-num">${tianDouRank.score}</span>
                                <span class="bean-user">${tianDouRank.loginName[0..2]}******</span>
                            </dd>
                        </#list>
                    </dl>
                </div>
                <div class="bean-rank-money">
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
                    <!-- <span class="share-text">分享可增加一次财豆抽奖机会！</span> -->
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
                        <li class="active">中奖纪录</li>
                        <li>我的奖品</li>
                    </ul>
                    <div class="record-list" id="recordList">
                        <ul class="record-model user-record active">
                            <#list winnerList.MacBook as mackBookWinner>
                                <li>恭喜 ${mackBookWinner.loginName[0..2]}***** 抽中了 ${mackBookWinner.prize.getName()}</li>
                            </#list>
                            <#list winnerList.iPhone as iPhoneWinner>
                                <li>恭喜 ${iPhoneWinner.loginName[0..2]}***** 抽中了 ${iPhoneWinner.prize.getName()}</li>
                            </#list>
                            <#list winnerList.other as otherWinner>
                                <li>恭喜 ${otherWinner.loginName[0..2]}***** 抽中了 ${otherWinner.prize.getName()}</li>
                            </#list>
                        </ul>
                        <ul class="record-model own-record">
                            <#list myPrizeList as myPrize>
                                <li>
                                    <span class="award-name">${myPrize.prize.getName()}</span>
                                    <span class="award-time">${myPrize.time}</span>
                                </li>
                            </#list>
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
                        <li class="active">中奖纪录</li>
                        <li>我的奖品</li>
                    </ul>
                    <div class="record-list scroll-record" id="beanList">
                        <ul class="record-model user-record  active">
                            <#list allPointLotteries as allPointLottery>
                                <li>恭喜 ${allPointLottery.loginName[0..2]}***** 抽中了 ${allPointLottery.pointPrizeName!}</li>
                            </#list>
                        </ul>
                        <ul class="record-model own-record">
                            <#list myPointLotteries as myPointLottery>
                                <li>
                                    <span class="award-name">${myPointLottery.pointPrizeName!}</span>
                                    <span class="award-time">${myPointLottery.createTime?string('yyyy-MM-dd HH:mm')!}</span>
                                </li>
                            </#list>
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
                <p>拓天客服将会在7个工作日内联系您发放奖品</p>
            </div>
            <div class="btn-list">
                <a href="javascript:void(0)" class="go-on go-close">继续抽奖</a>
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
                <p>拓天客服将会在7个工作日内联系您发放奖品</p>
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
                <p>您还未登陆，</p>
                <p>请登录过再来抽奖吧！</p>
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
        <img src="${staticServer}/images/sign/actor/ranklist/actor-intro.png" width="100%" class="actor-intro">
        <img src="${staticServer}/images/sign/actor/ranklist/bean-circle.png" width="100%" class="bean-circle">
        <img src="${staticServer}/images/sign/actor/ranklist/share-btn.png" width="100%" class="share-btn">
        <img src="${staticServer}/images/sign/actor/ranklist/share-intro.png" width="100%" class="share-intro">
    </div>
    <dl class="actor-rule">
        <dt>活动规则：</dt>
        <dd>1. 活动期间，用户投资即可获得相应天豆；</dd>
        <dd>2. 活动期间，平台达到一定投资额，即对天豆排行榜靠前的用户进行奖励;</dd>
        <dd>3. 用户的天豆数量相同时，按最终累计天豆的先后时间排名；</dd>
        <dd>4. 消费天豆可参与“天豆抽奖”活动，7个工作日内客服联系用户发放奖品；</dd>
        <dd>5. 活动截止后7个工作日内公布排行榜颁奖时间。</dd>
    </dl>
</div>
</@global.main>