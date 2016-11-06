<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "../macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="iphone7-lottery" title="iphone7活动参与情况">

<div class="col-md-10">
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户手机号</th>
                <th>姓名</th>
                <th>获奖时间</th>
                <th>获奖投资码</th>
            </tr>
            </thead>
            <tbody>
                <#if winners?? >
                    <#list winners as userItem>
                    <tr>
                        <td>${userItem.mobile}</td>
                        <td>${userItem.userName}</td>
                        <td>${userItem.effectiveTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                        <td>${userItem.lotteryNumber}</td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </div>
</div>
<!-- content area end -->
</@global.main>