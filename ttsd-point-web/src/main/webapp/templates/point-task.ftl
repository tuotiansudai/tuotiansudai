<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.point_task}" pageJavascript="${js.point_task}" activeNav="积分任务" activeLeftNav="" title="积分任务" site="membership">

<div class="global-member-task">
    <div class="wp clearfix">
        <div class="choi-beans-list ">
            <div class="title-task clearfix">
                <span class="active">新手任务</span>
            </div>
           <div class="notice-tip">
                <em>任务提醒：快去完成绑定银行卡任务，领取100积分奖励吧！</em>
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
                                <a class="btn-normal"  <#if newbiePointTask.completed> href="javascript:void(0)"<#else> href="${webServer}${newbiePointTask.url}"</#if> <#if newbiePointTask.completed>disabled="disabled"</#if>>
                                ${newbiePointTask.completed?string('已完成', '立即去完成')}
                                </a>
                            </dd>
                        </dl>
                    </div>
                </#list>
            </div>

            <div class="title-task clearfix" id="taskStatusMenu">
                <span class="active">进阶任务</span>
                <span>已完成任务</span>
            </div>
            <div class="task-status active">

                <#list advancedPointTasks as advancePointTask>
                    <div class="border-box <#if advancePointTask.description??>two-col<#else>one-col</#if>">
                        <dl class="fl">
                            <dt>${advancePointTask.title?replace('.00','')} <span class="color-key">奖励${advancePointTask.point?string.computer}积分</span></dt>
                            <#if advancePointTask.description??>
                                <dd>${advancePointTask.description}</dd>
                            </#if>
                        </dl>
                        <a href="${webServer}${advancePointTask.url}" class="fr btn-normal">去完成</a>
                    </div>
                </#list>
                <#if advancedPointTasks?size &gt; 4>
                    <div class="tc button-more">
                        <a href="javascript:void(0);"><span>点击查看更多任务</span> <i class="fa fa-chevron-circle-down"></i></a>
                    </div>
                </#if>

            </div>
            <div class="task-status">
                <#list completedAdvancedPointTasks as completedAdvancePointTask>
                    <div class="border-box one-col">
                        <dl class="fl">
                            <dt>${completedAdvancePointTask.title?replace('.00','')} <span class="color-key">奖励${completedAdvancePointTask.point?string.computer}积分</span></dt>
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
        </div>
    </div>
</div>




</@global.main>