<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.activity_center}" title="活动中心_投资活动_拓天速贷" keywords="拓天活动中心,拓天活动,拓天投资列表,拓天速贷" description="拓天速贷活动中心为投资用户提供投资大奖,投资奖励,收益翻倍等福利,让您在赚钱的同时体验更多的投资乐趣.">

<div class="activity-frame">
    <div class="ac-title">
        <span>活动中心</span> <em>让赚钱的同时体验更多的乐趣</em>
    </div>
    <div class="activity-container">
    <#list data as activityItem>
        <div class="activity-box">
            <div class="activity-img">
                <div class="img-inner">
                     <img src="${activityItem.webPictureUrl}" alt="${activityItem.description}">
                </div>
            </div>
            <i class="icon-going"><span class="hide">进行中</span></i>
            <span class="activity-title">${activityItem.description}</span>
            <span class="button-pos">
                <a class="btn" href="${activityItem.webActivityUrl}">查看详情</a>
                <i class="fa fa-angle-right hide"></i>
            </span>
        </div>
        </#list>
    </div>

</div>
</@global.main>