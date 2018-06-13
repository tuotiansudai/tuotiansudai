<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="ThirdAnniversaryActivity" title="3周年活动">

<div class="col-md-10">
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>姓名</th>
                <th>手机号</th>
                <th>投资金额</th>
                <th>年化投资额</th>
                <th>助力人数</th>
                <th>奖励比例</th>
                <th>获得现金奖励</th>
                <th>助力好友</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.userName!}</td>
                    <td>${item.mobile!}</td>
                    <td>${item.investAmount}</td>
                    <td>${item.annualizedAmount}</td>
                    <td>${item.helpCount}</td>
                    <td>${item.rate}</td>
                    <td>${item.reward}</td>
                    <td>${item.friends}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="9">暂无数据</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if data.count &gt; 0>
            <div>
                <span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                        <a href="?index=${data.index - 1}"
                           aria-label="Previous">
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                    </#if>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage >
                        <a href="?index=${data.index - 1}"
                           aria-label="Previous">
                            <span aria-hidden="true">Next &raquo;</span>
                        </a>
                    </#if>
                </li>
            </ul>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>