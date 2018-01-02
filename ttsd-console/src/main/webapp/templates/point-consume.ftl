<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="point-manage" sideLab="pointBillConsume" title="积分消耗记录">

<div class="col-md-10">
    <div class="table-responsive">
        <label for="control-label">速贷积分消费总额: ${sumSudaiPoint}</label>
        <label for="control-label">消费渠道积分总额: ${sumChannelPoint}</label>
        <table class="table table-bordered table-hover " style="width:80%;">
            <thead>
            <tr>
                <th>时间</th>
                <th>流水号</th>
                <th>用户姓名</th>
                <th>手机号</th>
                <th>消费总积分</th>
                <th>渠道来源</th>
                <th>消费渠道积分</th>
                <th>花费速贷积分</th>
                <th>业务类型</th>

            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.createdTime!}</td>
                    <td>${item.orderId!}</td>
                    <td>${item.userName!}</td>
                    <td>${item.mobile!}</td>
                    <td>${item.point?c}</td>

                    <td>${item.channel!}</td>
                    <td>${item.channelPoint!}</td>
                    <td>${item.sudaiPoint!}</td>
                    <td>${item.businessType!}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="8">暂无数据</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

</div>

</@global.main>