<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.company_activity}" pageJavascript="${js.rank_list}" activeNav="" activeLeftNav="" title="排行榜">
<div class="rank-list-container">
    <div class="rank-phone-model">
        <img src="${staticServer}/images/sign/actor/ranklist/rank-list-top.png" width="100%">
    </div>
    <div class="wp clearfix actor-intro">
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
                            <span class="project-name">我的天豆：${myTianDou!0}</span>
                            <a href="javascript:void(0)" class="project-operate">去抽奖</a>
                        </p>
                    </li>
                    <li>
                        <p>
                            <span class="project-name">我的财豆：1000</span>
                            <a href="javascript:void(0)" class="project-operate">去抽奖</a>
                        </p>
                    </li>
                    <li>
                        <p>
                            <span class="project-name">我的排名：<#if myRank??>${myRank}<#else>-</#if></span>
                            <a href="#" class="project-operate">去投资</a>
                        </p>
                    </li>
                </ul>
            </div>
        </@global.isNotAnonymous>
        <div class="line-single"></div>
        <ul class="leader-btn">
            <li class="active">排行榜</li>
            <li>奖品单</li>
            <div class="bean-btn">天豆计算器</div>
        </ul>
        <div class="leader-container">
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
                                <span class="order-num"><i class="font-icon">${tianDouRank?index}</i></span>
                                <span class="bean-num">${tianDouRank.score}</span>
                                <span class="bean-user">${tianDouRank.loginName[0..2]}******</span>
                            </dd>
                        </#list>
                    </dl>
                </div>
                <div class="bean-rank-money">
                    <div class="progress-line">
                        <h3 class="total-money">累积投资：48,000,000.00元</h3>
                        <dl class="gift-one">
                            <dt>奖池累积中...</br>投资满<span>1.5亿</span>则奖池升级:</dt>
                            <dd>第一名：现金5万</dd>
                            <dd>第二名：现金3万</dd>
                            <dd>第三名：日韩双人游</dd>
                            <dd>第四名：欧洲游</dd>
                            <dd>第五名：海岛游</dd>
                        </dl>
                        <dl class="gift-two">
                            <dt>当前奖池：</dt>
                            <dd>第一名：现金5万</dd>
                            <dd>第二名：现金3万</dd>
                            <dd>第三名：日韩双人游</dd>
                        </dl>
                        <div class="stage-num first-stage">
                            <span>8,500万</span><i class="line-single"></i>
                        </div>
                        <div class="stage-num two-stage">
                            <span>1.5亿</span><i class="line-single"></i>
                        </div>
                        <div class="color-line">
                            <div class="color-pro"></div>
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
        <ul class="leader-btn">
            <li class="active">天豆抽奖</li>
            <li>财豆抽奖</li>
            <div class="share-list">
                <p class="bdsharebuttonbox">
                    <span class="share-text">分享至：</span>
                    <a href="#" class="share-icon icon-weibo" data-cmd="tsina"></a>
                    <a href="#" class="share-icon icon-weixin" data-cmd="weixin"></a>
                    <a href="#" class="share-icon icon-zone" data-cmd="qzone"></a>
                    <span class="share-text">分享可增加一次财豆抽奖机会！</span>
                </p>
            </div>
        </ul>
        <div class="leader-container">
            <div class="leader-list active">
                <div class="lottery-circle">
                    <h3 class="td-list">
                        <span class="td-total">我的天豆：${myTianDou!0}</span>
                        <span class="td-tip">每次抽奖将消耗1000天豆</span>
                    </h3>

                    <div class="circle-shade">
                        <div class="pointer-img">
                            <img src="${staticServer}/images/sign/actor/ranklist/pointer.png" alt="pointer"/>
                        </div>
                        <div class="rotate-btn">
                            <img id="rotateTd" src="${staticServer}/images/sign/actor/ranklist/turntable.png"
                                 alt="turntable"/>
                        </div>
                    </div>
                </div>
                <div class="lottery-detail">
                    <ul class="gift-record">
                        <li class="active">中奖纪录</li>
                        <li>我的奖品</li>
                    </ul>
                    <div class="record-list" id="recordList">
                        <ul class="record-model user-record active">
                            <#list winnerList.MacBook as mackBookWinner>
                                <li>恭喜${mackBookWinner.loginName[0..2]}*****抽中了${mackBookWinner.prize.name()}</li>
                            </#list>
                            <#list winnerList.iPhone as iPhoneWinner>
                                <li>恭喜${iPhoneWinner.loginName[0..2]}*****抽中了${iPhoneWinner.prize.name()}</li>
                            </#list>
                            <#list winnerList.other as otherWinner>
                                <li>恭喜${otherWinner.loginName[0..2]}*****抽中了${otherWinner.prize.name()}</li>
                            </#list>
                        </ul>
                        <ul class="record-model own-record">
                            <#list myPrizeList as myPrize>
                                <li>
                                    <span class="award-name">${myPrize.prize.name()}</span>
                                    <span class="award-time">${myPrize.time}</span>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="leader-list">
                <div class="lottery-circle">
                    <h3 class="td-list">
                        <span class="td-tip">每次抽奖将消耗1000财豆，每天限抽一次。</span>
                    </h3>

                    <div class="circle-shade">
                        <div class="pointer-img">
                            <img src="${staticServer}/images/sign/actor/ranklist/pointer.png" alt="pointer"/>
                        </div>
                        <div class="rotate-btn">
                            <img id="rotateCd" src="${staticServer}/images/sign/actor/ranklist/gift-list-cd.png"
                                 alt="turntable"/>
                        </div>
                    </div>
                </div>
                <div class="lottery-detail">
                    <ul class="gift-record">
                        <li class="active">中奖纪录</li>
                        <li>我的奖品</li>
                    </ul>
                    <div class="record-list">
                        <ul class="record-model user-record">
                            <#list allPointLotteries as allPointLottery>
                                <li>恭喜${allPointLottery.loginName[0..2]}*****抽中了${allPointLottery.pointPrizeName!}</li>
                            </#list>
                        </ul>
                        <ul class="record-model own-record active">
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
</div>
</@global.main>