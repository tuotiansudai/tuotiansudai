<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.anniversary_celebration}" pageJavascript="${js.anniversary_celebration}" activeNav="" activeLeftNav="" title="周年庆活动_拓天活动_拓天速贷" keywords="拓天速贷,拓天活动.生日活动,生日月特权" description="拓天速贷专属生日月特权,生日月投资收益翻倍,拓天速贷专属活动超高收益等你拿.">
<div class="anniversary-container">
    <div class="container clearfix">
        <img src="${staticServer}/images/sign/actor/anniversary/anniversary-top.png" width="100%" class="responsive-pc">
        <img src="${staticServer}/images/app-banner/app-banner-anniversary.jpg" width="100%" class="responsive-phone">
        <div class="wp actor-bg clearfix">
            <div class="model-list">
                <div class="img-vip">
                    <img src="${staticServer}/images/sign/actor/anniversary/vip-img.png" width="100%">
                </div>
                <div class="img-one">
                    <p class="intro-text"><strong>2015年7月1日</strong>，我们上线了...</p>
                    <p class="intro-text">平台安全运营至今</p>
                </div>
            </div>
            <div class="model-list">
                <div class="img-two">
                    <p class="intro-text">历史借款项目全部全额兑付！</p>
                    <p class="intro-text"><strong>198488</strong>个投资人加入了我们</p>
                </div>
                <div class="img-rank">
                    <img src="${staticServer}/images/sign/actor/anniversary/list-img.png" width="100%">
                </div>
            </div>
            <div class="model-list">
                <div class="img-bag">
                    <img src="${staticServer}/images/sign/actor/anniversary/red-bag.png" width="100%">
                </div>
                <div class="img-three">
                    <p class="intro-text">出于对我们的信任</p>
                    <p class="intro-text"><strong>76294735.58</strong>元投资发生在这里</p>
                </div>
            </div>
            <div class="model-list">
                <div class="text-info">
                    <img src="${staticServer}/images/sign/actor/anniversary/text-info.png" width="100%">
                </div>
            </div>
            <div class="model-list">
                <h3>1 周年超级VIP大派送</h3>
                <p class="content-text">&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;不知道怎么理财？专属理财顾问送给你！服务费太高？减免！还有会员大礼包，生日福利等等等等...</p>
                <p class="content-text">&emsp;&emsp;&emsp;&emsp;部分<i class="vip-icon"></i>超级特权一览：</p>
                <ul class="feature-list">
                    <li>
                        <i class="icon-type">
                            <img src="${staticServer}/images/sign/actor/anniversary/bian-icon.png" width="100%">
                        </i>
                        <p>方便快捷 随时提现</p>
                    </li>
                    <li>
                        <i class="icon-type">
                            <img src="${staticServer}/images/sign/actor/anniversary/li-icon.png" width="100%">
                        </i>
                        <p>生日月 收益翻倍</p>
                    </li>
                    <li>
                        <i class="icon-type">
                            <img src="${staticServer}/images/sign/actor/anniversary/zhuan-icon.png" width="100%">
                        </i>
                        <p>专属顾问 安全理财</p>
                    </li>
                    <li>
                        <i class="icon-type">
                            <img src="${staticServer}/images/sign/actor/anniversary/sheng-icon.png" width="100%">
                        </i>
                        <p>会员专享 服务费折扣</p>
                    </li>
                </ul>
            </div>
            <div class="model-list text-c">
                <a href="javascript:void(0)" class="get-vip">价值25元会员限量领取</a>
            </div>
            <div class="model-list">
                <h3>2 投资英雄榜中榜</h3>
                <p class="content-text font-20">&emsp;&emsp;&emsp;英雄榜招募中！</p>
                <p class="content-text">&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;7月1日至31日活动期间，每天进行英雄榜招募，上榜者可获丰富奖励！根据每人每日累计投资金额</p>
                <p class="content-text">&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;进行排名，24：00锁定当日榜单，前十名有丰厚奖励，第一名将获得价值不菲的每日神秘大奖.</p>
            </div>
            <div class="model-list">
                <div class="title-line">
                    <span>
                        <i class="left-line"></i>
                        投资英雄榜
                        <i class="right-line"></i>
                    </span>
                </div>
            </div>
            <div class="model-list">
                <div class="title-model">
                    <i class="date-icon"></i>
                    <span class="date-text">2016-05-27</span>
                    <span class="rank-number">我的排名：登录后查看</span>
                    <a href="/login" target="_blank" class="get-rank">查看我的排名</a>
                </div>
                <ul class="rank-name">
                    <li>英雄榜</li>
                </ul>
                <div class="rank-content">
                    <div class="rank-model">
                        <h3>今日投资英雄榜<span>(实时)</span></h3>
                        <dl class="data-list" id="heroList">
                        </dl>
                            <script type="text/html" id="heroListTpl">
                                <dt>
                                    <span class="title-text">名次</span>
                                    <span class="data-text">今日投资额(元)</span>
                                    <span class="user-text">用户</span>
                                    <span class="reward-text">奖励</span>
                                </dt>
                                {{if records.length>0}}
                                {{each records}}
                                <dd>
                                    <span class="title-text">
                                        <i>{{$index+1}}</i>
                                    </span>
                                    <span class="data-text">{{$value.sumAmount}}</span>
                                    <span class="user-text">{{$value.loginName}}</span>
                                    {{if $index==0}}
                                        <span class="reward-text">神秘大奖</span>
                                    {{else if $index>0 && $index<5}}
                                        <span class="reward-text">200元红包</span>
                                    {{else}}
                                    <span class="reward-text">100元红包</span>
                                    {{/if}}
                                </dd>
                                {{/each}}
                                {{else}}
                                <dd class="empty-text">暂无数据</dd>
                                {{/if}}
                            </script>
                        <p class="tip-text">注：每日英雄榜排名前十名的上榜者可获得1%加息劵一张！</p>
                    </div>
                    <div class="rank-model">
                        <h3 class="title-name">当日投资英雄榜<span>(实时)</span></h3>
                        <h3 class="list-title">
                            <span class="pre-btn" id="heroPre">上一天</span>
                            <span class="date-info">2016-07-01</span>
                            <span>英雄榜</span>
                            <span class="next-btn" id="heroNext">下一天</span>
                        </h3>
                        <dl class="data-list" id="heroRecord"></dl>
                            <script type="text/html" id="heroRecordTpl">
                                <dt>
                                    <span class="title-text">名次</span>
                                    <span class="data-text">当日投资额(元)</span>
                                    <span class="user-text">用户</span>
                                    <span class="reward-text">奖励</span>
                                </dt>
                                {{if records.length>0}}
                                {{each records}}
                                <dd>
                                    <span class="title-text">
                                        <i>{{$index+1}}</i>
                                    </span>
                                    <span class="data-text">{{$value.sumAmount}}</span>
                                    <span class="user-text">{{$value.loginName}}</span>
                                    {{if $index==0}}
                                        <span class="reward-text">神秘大奖</span>
                                    {{else if $index>0 && $index<5}}
                                        <span class="reward-text">200元红包</span>
                                    {{else}}
                                    <span class="reward-text">100元红包</span>
                                    {{/if}}
                                </dd>
                                {{/each}}
                                {{else}}
                                <dd class="empty-text">暂无数据</dd>
                                {{/if}}
                            </script>
                        <p class="tip-text"><span class="show-btn">查看历史榜单</span><span class="back-btn">返回</span></p>
                    </div>
                </div>
            </div>
            <div class="model-list">
                <div class="today-gift">
                    <div class="today-title">
                    </div>
                    <div class="gift-img">
                        <img src="${staticServer}/images/sign/actor/anniversary/gift-img.png" width="100%">
                    </div>
                    <a href="/loan-list" class="get-rank" target="_blank">我要上榜</a>
                </div>
            </div>
            <div class="model-list">
                <h3>3 好友投资拿红包</h3>
                <p class="content-text">&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;当您的直接推荐人进行投资时，您将收到其年化投资本金1%的现金奖励，活动期间，每日前三名还有额外红包奖励。</p>
            </div>
            <div class="model-list mt-40">
                <div class="title-model">
                    <i class="date-icon"></i>
                    <span class="date-text">2016-05-27</span>
                    <span class="rank-number">我的排名：登录后查看</span>
                    <a href="/login" target="_blank" class="get-rank">查看我的排名</a>
                </div>
                <ul class="rank-name">
                    <li>推荐榜</li>
                </ul>
                <div class="rank-content">
                    <div class="rank-model">
                        <h3>今日推荐英雄榜<span>(实时)</span></h3>
                        <dl class="data-list" id="refeInvest"></dl>
                            <script type="text/html" id="refeInvestTpl">
                                <dt>
                                    <span class="title-text">名次</span>
                                    <span class="data-text">今日投资额(元)</span>
                                    <span class="user-text">用户</span>
                                    <span class="reward-text">奖励</span>
                                </dt>
                                {{if records}}
                                {{each records}}
                                <dd>
                                    <span class="title-text">
                                        <i>{{$index+1}}</i>
                                    </span>
                                    <span class="data-text">{{$value.sumAmount}}</span>
                                    <span class="user-text">{{$value.loginName}}</span>
                                    {{if $index==0}}
                                        <span class="reward-text">神秘大奖</span>
                                    {{else if $index>0 && $index<5}}
                                        <span class="reward-text">200元红包</span>
                                    {{else}}
                                    <span class="reward-text">100元红包</span>
                                    {{/if}}
                                </dd>
                                {{/each}}
                                {{else}}
                                <dd class="empty-text">暂无数据</dd>
                                {{/if}}
                            </script>
                    </div>
                    <div class="rank-model">
                        <h3 class="title-name">当日推荐英雄榜<span>(实时)</span></h3>
                        <h3 class="list-title">
                            <span class="pre-btn" id="refePre">上一天</span>
                            <span class="date-info">2016-07-01</span>
                            <span>英雄榜</span>
                            <span class="next-btn" id="refeNext">下一天</span>
                        </h3>
                        <dl class="data-list" id="refeRecord"></dl>
                            <script type="text/html" id="refeRecordTpl">
                                <dt>
                                    <span class="title-text">名次</span>
                                    <span class="data-text">当日投资额(元)</span>
                                    <span class="user-text">用户</span>
                                    <span class="reward-text">奖励</span>
                                </dt>
                                {{if records}}
                                {{each records}}
                                <dd>
                                    <span class="title-text">
                                        <i>{{$index+1}}</i>
                                    </span>
                                    <span class="data-text">{{$value.sumAmount}}</span>
                                    <span class="user-text">{{$value.loginName}}</span>
                                    {{if $index==0}}
                                        <span class="reward-text">神秘大奖</span>
                                    {{else if $index>0 && $index<5}}
                                        <span class="reward-text">200元红包</span>
                                    {{else}}
                                    <span class="reward-text">100元红包</span>
                                    {{/if}}
                                </dd>
                                {{/each}}
                                {{else}}
                                <dd class="empty-text">暂无数据</dd>
                                {{/if}}
                            </script>
                        <p class="tip-text"><span class="show-btn">查看历史榜单</span><span class="back-btn">返回</span></p>
                    </div>
                </div>
            </div>
            <div class="model-list">
                <div class="title-line">
                    <span>
                        <i class="left-line"></i>
                        活动规则
                        <i class="right-line"></i>
                    </span>
                </div>
            </div>
            <div class="model-list">
                <ul class="rule-list">
                    <li>1. 每天24点将计算当日新增投资及新增直接推荐投资的排名，投资者在当日24点之前进行的多次投资，金额可累计计算；投资英雄榜仅限于直投项目，其余投资不计入今日投资金额中；</li>
                    <li>2. 投资英雄榜中奖人数最多十名,推荐英雄榜最多三名，如遇金额一致则并列获奖，下一奖项名额顺延缩减；</li>
                    <li>3. 每日英雄榜排名将在活动页面实时更新。中奖结果将于次日由客服联系确认，红包和加息券在中奖次日发放，实物奖品将于月底活动结束后七个工作日内统一安排发放；</li>
                    <li>4. <span>特别提示</span>：活动期间一旦提交债权转让申请，则不可在活动期间继续参与投资英雄榜。即使发起债权转让申请的当天，累计投资额已入围当日前10名，也不可参与当日投资英雄榜排名及获取奖励；</li>
                    <li>5. 拓天速贷会根据活动的情况，以等值，增值为基础调整奖品类型；</li>
                    <li class="tip-info">***活动遵循拓天速贷法律声明，最终解释权归拓天速贷平台所有***</li>
                </ul>
            </div>
        </div>
    </div>
</div>
</@global.main>