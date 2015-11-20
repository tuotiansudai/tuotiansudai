<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.auto_invest}" activeNav="我的账户" activeLeftNav="自动投标" title="自动投标">
<div class="content-container auto-invest">
    <h4 class="column-title"><em class="tc">自动投标</em></h4>
            <ul class="planSet">
                <li><b>投资金额：</b><i>${model.minInvestAmount}</i> --- <i>${model.maxInvestAmount}</i> 元</li>
                <li><b>保留金额：</b><i>${model.retentionAmount}</i> 元</li>
                <li><b>项目期限：</b><span><#list periods as period>${period.periodName}<#sep>，  </#list></span></li>
            </ul>


        <div class="btnBox tc">
            <#if model.isTodayPlan()>
                <p class="pad-m text-notice"><i class="icon-notice"></i>自动投标将在次日零点开启</p>
            <#else>
                <p class="pad-m text-notice"><i class="icon-notice"></i>自动投标已开启</p>
            </#if>
            <button type="button" id="editSetting" class="btn btn-normal">修改设置</button>
        </div>

</div>
</@global.main>