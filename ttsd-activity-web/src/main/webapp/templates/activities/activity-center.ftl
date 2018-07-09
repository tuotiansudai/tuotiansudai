<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.activity_center}" pageJavascript="${js.activity_center}" activeNav="" activeLeftNav="" title="活动中心_投资活动_拓天速贷" keywords="拓天活动中心,拓天活动,拓天投资列表,拓天速贷" description="拓天速贷活动中心为投资用户提供投资大奖,投资奖励,收益翻倍等福利,让您在赚钱的同时体验更多的投资乐趣.">

<div class="activity-frame">
    <div class="ac-title">
        <span>活动中心</span> <em>让赚钱的同时体验更多的乐趣</em>
    </div>
    <div class="ac-item clearfix">
        <span class="active">当前活动</span>
        <span>往期精彩</span>
    </div>
    <div class="activity-container clearfix">
        <div class="actor-list active">
            <#if data!?if_exists?size != 0>
                <#list data as activityItem>
                    <#if activityItem.longTerm=='longTerm' || (activityItem.expiredTime?? && activityItem.expiredTime?date gte .now?date)>
                        <div class="activity-box" data-href="${activityItem.webActivityUrl}">
                            <div class="activity-img">
                                <div class="img-inner compliance-center">
                                    <img src="${commonStaticServer}${activityItem.webPictureUrl}"
                                         alt="${activityItem.description}">
                                    <div class="invest-tips">市场有风险，投资需谨慎！</div>
                                </div>
                            </div>
                            <i class="icon-going"><span class="hide">进行中</span></i>

                            <span class="activity-title">${activityItem.description}</span>
                            <span class="time">
                                <#if activityItem.longTerm == 'longTerm'>
                                    长期活动
                                <#elseif activityItem.activatedTime??&&activityItem.expiredTime??>
                                ${(activityItem.activatedTime?string('yyyy-MM-dd'))!}
                                    - ${(activityItem.expiredTime?string('yyyy-MM-dd'))!}
                                </#if>
                        </span>
                            <span class="button-pos">
                        <a class="btn" href="${activityItem.webActivityUrl}">查看详情</a>
                        <i class="fa fa-angle-right hide"></i>
                    </span>
                        </div>
                    </#if>
                </#list>
            <#else>
                <div class="no-activity">
                    <div class="no-data-wap">
                        <div class="img"></div>
                        <p>当前暂无活动</p>
                    </div>
                </div>
            </#if>
        </div>
        <div class="actor-list">
            <#list data as activityItem>
                <#if activityItem.longTerm == 'notLongTerm' && activityItem.expiredTime??&&activityItem.expiredTime?date lt .now?date>
                    <div class="activity-box" data-href="${activityItem.webActivityUrl}">
                        <div class="activity-img">
                            <div class="img-inner compliance-center">
                                <img src="${commonStaticServer}${activityItem.webPictureUrl}"
                                     alt="${activityItem.description}">
                                <div class="invest-tips">市场有风险，投资需谨慎！</div>
                            </div>
                        </div>
                        <i class="icon-finished"><span class="hide">已结束</span></i>
                        <span class="activity-title">${activityItem.description}</span>
                        <span class="time">
                            <#if activityItem.activatedTime??&&activityItem.expiredTime??>
                            ${(activityItem.activatedTime?string('yyyy-MM-dd'))!}
                                - ${(activityItem.expiredTime?string('yyyy-MM-dd'))!}

                            </#if>
                        </span>
                        <span class="button-pos">
                        <a class="btn" href="${activityItem.webActivityUrl}">查看详情</a>
                        <i class="fa fa-angle-right hide"></i>
                    </span>
                    </div>
                </#if>
            </#list>
        </div>
    </div>

</div>
</@global.main>