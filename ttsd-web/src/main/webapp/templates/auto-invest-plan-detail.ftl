<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="自动投标" pageCss="${css.global}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="main-frame auto-invest">
    <aside class="menu-box fl">
        <ul class="menu-list">
            <li><a href="javascript:">账户总览</a></li>
            <li><a href="javascript:">投资记录</a></li>
            <li><a href="javascript:">债权转让</a></li>
            <li><a href="javascript:">资金管理</a></li>
            <li><a href="javascript:">个人资产</a></li>
            <li><a href="/investor/auto-invest" class="active">自动投标</a></li>
            <li><a href="javascript:">积分红包</a></li>
            <li><a href="javascript:">推荐管理</a></li>
        </ul>
    </aside>
    <div class="content-container fr auto-height">
        <h4 class="column-title"><em class="tc">自动投标</em></h4>

        <div class="pad-s">
            <div class="borderBox">
                <ul class="planSet">
                    <li><b>投资金额：</b><i>${model.minInvestAmount}</i> --- <i>${model.maxInvestAmount}</i> 元</li>
                    <li><b>保留金额：</b><i>${model.retentionAmount}</i> 元</li>
                    <li><b>项目期限：</b><span><#list periods as period>${period.periodName}<#sep>，  </#list></span></li>
                </ul>
            </div>

            <div class="btnBox tc">
                <#if model.isTodayPlan()>
                    <p class="pad-m text-notice"><i class="icon-notice"></i>自动投标将在次日零点开启</p>
                <#else>
                    <p class="pad-m text-notice"><i class="icon-notice"></i>自动投标已开启</p>
                </#if>
                <button type="button" id="editSetting" class="btn btn-normal">修改设置</button>
            </div>
        </div>

    </div>
</div>

<#include "footer.ftl">
<@global.javascript pageJavascript="${js.autoInvest}">
</@global.javascript>
</body>
</html>