<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.point}" activeNav="我的账户" activeLeftNav="我的积分" title="我的积分">
<div class="content-container my-choi-beans">
    <h4 class="column-title">
        <em class="tc title-navli active">我的积分</em>
        <em class="tc title-navli">任务中心</em>
        <em class="tc title-navli">积分兑换</em>
        <em class="tc title-navli">积分明细</em>
    </h4>
    <div class="content-list">
        <div class="choi-beans-list active">
            <div class="beans-intro">
                <div class="beans-list">
                    <i class="icon-result icon-beans"></i>
                    <span class="title-text">积分总览</span>
                    <span class="title-href" id="beansDetail">积分明细></span>
                </div>
                <div class="beans-list mt-20">
                    <span class="beans-num">可用积分：${myPoint?string.computer}</span>
                    <i class="icon-result icon-dou"></i>
                </div>
                <div class="beans-list mt-20">
                    <ul class="beans-recent">
                        <#list latestThreePointBills.records as pointBill>
                            <li class="<#if pointBill_index == 0>one-day</#if><#if pointBill_index == 1>two-day</#if><#if pointBill_index == 2>three-day</#if>">
                                <p>
                                    <i class="icon-circle"></i>
                                    <span class="text-date">${pointBill.createdTime?string('MM月dd日')}</span>
                                    <span class="text-money">
                                        <strong>${pointBill.point?string.computer}</strong>
                                        <i class="icon-result icon-sm-dou"></i>
                                    </span>
                                </p>
                            </li>
                        </#list>
                    </ul>
                </div>
            </div>
            <div class="beans-operat">
                <h3>赚取积分</h3>
                <ul class="object-list">
                    <li>
                        <p class="icon-com invest-bg"><i class="icon-result icon-invest"></i></p>
                        <p class="btn-list">
                            <a href="/loan-list"><span class="btn-invest">去投资</span></a>
                        </p>
                    </li>
                    <li>
                        <p class="icon-com sign-bg"><i class="icon-result icon-sign"></i></p>
                        <p class="btn-list">
                            <#if signedIn>
                                <span class="btn-sign no-click">今日已签到</span>
                            <#else >
                                <span class="btn-sign" data-url="/point/sign-in" id="signBtn">签到</span>
                            </#if>
                        </p>
                    </li>
                    <li>
                        <p class="icon-com task-bg"><i class="icon-result icon-task"></i></p>
                        <p class="btn-list">
                            <span class="btn-task" id="taskBtn">做任务</span>
                        </p>
                    </li>
                </ul>
            </div>
            <div class="beans-infotext">
                <dl>
                    <dt>积分说明：</dt>
                    <dd>1.积分可用于兑换体验券、加息券等优惠券；</dd>
                    <dd>2.在平台投资，投资金额与所获积分的比率为1：1，即每投资100元现金奖励100个积分；</dd>
                    <dd>3.连续签到，积分翻倍送！第一天签到领5积分，最多每天可领80积分；</dd>
                    <dd>4.完成任务，赠送积分，完成任务越多，赠送积分越多；</dd>
                    <dd>5.积分不可以提现，不可以转让，不可以用于其他平台。</dd>
                </dl>
            </div>
        </div>
        <div class="choi-beans-list ">
            <div class="title-task clearfix">
                <span class="active">新手任务</span>
            </div>
            <div class="notice-tip">
                <#assign allNewbieTaskCompleted = true />
                <#list newbiePointTasks as newbiePointTask>
                    <#if newbiePointTask.completed == false>
                        <#assign allNewbieTaskCompleted = false />
                        任务提醒：快去完成${newbiePointTask.name.title}任务，领取${newbiePointTask.point}积分奖励吧！
                        <#break/>
                    </#if>
                </#list>
                <#if allNewbieTaskCompleted>
                    您已完成所有新手任务，快去完成进阶任务向高手进发吧！
                </#if>

                <i class="fr fa fa-chevron-up"></i>
            </div>

            <div class="task-frame clearfix" id="taskFrame">
                <#list newbiePointTasks as newbiePointTask>
                    <div class="task-box">
                        <span class="serial-number">${newbiePointTask_index + 1}</span>
                        <dl class="step-content">
                            <dt>${newbiePointTask.name.title}</dt>
                            <dd>说明：${newbiePointTask.name.getDescription()}</dd>
                            <dd class="reward">奖励：<span>${newbiePointTask.point?string.computer}积分</span></dd>
                            <dd>
                                <a class="btn-normal"  <#if newbiePointTask.completed> href="javascript:void(0)"<#else> href="${newbiePointTask.url}"</#if> <#if newbiePointTask.completed>disabled="disabled"</#if>>
                                ${newbiePointTask.completed?string('已完成', '立即去完成')}
                                </a>
                            </dd>
                        </dl>
                    </div>
                </#list>
            </div>

            <@global.role hasRole="'INVESTOR','LOANER'">
                <div class="title-task clearfix" id="taskStatusMenu">
                    <span class="active">进阶任务</span>
                    <span>已完成任务</span>
                </div>

                <div class="task-status active">
                    <#list advancedPointTasks as advancePointTask>
                        <div class="border-box <#if advancePointTask.description??>two-col<#else>one-col</#if>">
                            <dl class="fl">
                                <dt>${advancePointTask.title} <span class="color-key">奖励${advancePointTask.point?string.computer}积分</span></dt>
                                <#if advancePointTask.description??>
                                    <dd>${advancePointTask.description}</dd>
                                </#if>
                            </dl>
                            <a href="${advancePointTask.url}" class="fr btn-normal">去完成</a>
                        </div>
                    </#list>
                    <#if advancedPointTasks?size &gt; 4>
                        <div class="tc button-more">
                            <a href="javascript:void(0);"><span>点击查看更多任务</span> <i class="fa fa-chevron-circle-down"></i></a>
                        </div>
                    </#if>

                </div>
                <div class="task-status" style="display: none;">
                    <#list completedAdvancedPointTasks as completedAdvancePointTask>
                        <div class="border-box one-col">
                            <dl class="fl">
                                <dt>${completedAdvancePointTask.title} <span class="color-key">奖励${completedAdvancePointTask.point?string.computer}积分</span></dt>
                            </dl>
                            <button class="fr btn-normal" disabled>已完成</button>
                        </div>
                    </#list>
                    <#if completedAdvancedPointTasks?size &gt; 4>
                        <div class="tc button-more">
                            <a href="javascript:void(0);"><span>点击查看更多任务 </span><i class="fa fa-chevron-circle-down"></i></a>
                        </div>
                    </#if>
                </div>
            </@global.role>


        </div>
        <div class="choi-beans-list">
            <div class="beans-coupon">
                <div class="beans-list">
                    <i class="icon-result icon-beans"></i>
                    <span class="beans-num">可用积分：<strong class="bean-use">${myPoint?string.computer}</strong></span>
                    <i class="icon-result icon-dou"></i>
                </div>
            </div>
            <ul class="coupon-list">
                <#list exchangeCoupons as exchangeCoupon>
                    <li class="<#if myPoint lt exchangeCoupon.exchangePoint>no-click<#else><#if exchangeCoupon.couponType == 'INVEST_COUPON'>new-type<#else>bite-type</#if></#if>">
                        <div class="top-com">
                            <div class="left-name">
                                <span>${exchangeCoupon.couponType.getName()!}</span>
                                <em></em>
                                <i class="circle-top"></i>
                                <i class="circle-bottom"></i>
                            </div>
                            <div class="right-coupon">

                                <#if exchangeCoupon.couponType == 'INVEST_COUPON'>
                                    <p class="mt-10">
                                        <span class="num-text"><@amount>${(exchangeCoupon.amount?number*100)?string('0')}</@amount></span>
                                        <span class="unit-text">元</span>
                                    </p>
                                <#else>
                                    <p class="mt-10">
                                        <span class="num-text">${exchangeCoupon.rate*100}%</span>
                                        <span class="unit-text">年化收益</span>
                                    </p>
                                </#if>
                                <p>
                                    <#if exchangeCoupon.investLowerLimit == '0.00'>
                                        [投资即可使用]
                                    <#else>
                                        [投资满<@amount>${(exchangeCoupon.investLowerLimit?number*100)?string('0')}</@amount>元即可使用]
                                    </#if>
                                </p>

                                <p>
                                    <#if (exchangeCoupon.productTypes?size)  == 4>
                                        全部产品均可使用
                                    <#else>
                                        <#list exchangeCoupon.productTypes as productType>
                                            <#if productType_index == (exchangeCoupon.productTypes?size - 1)>
                                            ${productType.getName()}
                                            <#else>
                                            ${productType.getName()},
                                            </#if>
                                        </#list>
                                        产品可用
                                    </#if>
                                </p>
                            </div>
                        </div>
                        <div class="bottom-time">
                            <span>所需积分：${exchangeCoupon.exchangePoint?string('0')}</span>
                            <i class="icon-dou"></i>
                            <a href="javascript:void(0)" class="reedom-now" data-id="${exchangeCoupon.id?string('0')}" data-bite="${exchangeCoupon.couponType.getName()!}"
                               data-beans="${exchangeCoupon.exchangePoint?string('0')}">立即兑换</a>
                        </div>
                    </li>
                </#list>
                <#if !(exchangeCoupons?has_content)>
                    <span class="pad-m fl tc text-18" style="width:705px;color:#808080">礼品即将上线，敬请期待！</span>
                </#if>
            </ul>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    	<div class="choi-beans-list invest-list-content">
    		<div class="item-block date-filter">
		        <span class="sub-hd">起止时间:</span>
		        <input type="text" id="date-picker" class="input-control" size="35"/>
		        <span class="select-item" data-day="1">今天</span>
		        <span class="select-item" data-day="7">最近一周</span>
		        <span class="select-item current" data-day="30">一个月</span>
		        <span class="select-item" data-day="180">六个月</span>
		        <span class="select-item" data-day="">全部</span>
		    </div>
		    <div class="item-block status-filter">
		        <span class="sub-hd">往来类型:</span>
		        <span class="select-item current" data-status="">全部</span>
                <span class="select-item" data-status="SIGN_IN,TASK,INVEST">已获取</span>
                <span class="select-item" data-status="EXCHANGE,LOTTERY">已使用</span>
		    </div>
		    <div class="clear-blank"></div>
		    <div class="point-bill-list">
		    </div>
            <div class="pagination" data-url="/point/point-bill-list-data" data-page-size="10">
            </div>
        </div>
    </div>
    <div class="sign-layer" id="signLayer">
        <div class="sign-layer-list">
            <div class="sign-top">
                <div class="close-btn" id="closeSign"></div>
                <p class="sign-text">签到成功，领取5积分！</p>
                <p class="tomorrow-text">明日可领10积分</p>
                <p class="img-beans">
                    <img src="${staticServer}/images/sign/sign-beans.png"/>
					<span class="add-dou">
						+5
					</span>
                </p>
                <p class="intro-text">连续签到，积分翻倍送，最多每天可领<span>80</span>积分！</p>
            </div>
            <div class="sign-bottom">
                <ul>
                    <li>
                        <p class="day-name">第1天</p>
                        <p class="day-beans">
                            <span>5</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第2天</p>
                        <p class="day-beans">
                            <span>10</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第3天</p>
                        <p class="day-beans">
                            <span>20</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第4天</p>
                        <p class="day-beans">
                            <span>40</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第5天</p>
                        <p class="day-beans">
                            <span>80</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第6天</p>
                        <p class="day-beans">
                            <span>80</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第7天</p>
                        <p class="day-beans">
                            <span>80</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li class="last-day">
                        <p class="day-name">第N天</p>
                        <p class="day-beans">
                            <span>...</span>
                        </p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
</@global.main>