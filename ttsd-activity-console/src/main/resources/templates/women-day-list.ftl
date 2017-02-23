<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="notWork" title="不上班活动管理">

<!-- content area begin -->
<div class="col-md-10">
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    手机号
                </th>
                <th>
                    姓名
                </th>
                <th>
                    获取花瓣数量
                </th>
                <th>
                    投资花瓣数量
                </th>
                <th>
                    签到花瓣数量
                </th>
                <th>
                    邀请花瓣数量
                </th>
                <th>
                    获得礼盒
                </th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>
                    ${item.mobile!}
                    </td>
                    <td>
                    ${item.name!}
                    </td>
                    <td>
                    ${item.totalLeaves!}
                    </td>
                    <td>
                    ${item.investLeaves!}
                    </td>
                    <td>
                    ${item.signLeaves!}
                    </td>
                    <td>
                    ${item.referrerLeaves!}
                    </td>
                    <td>
                    ${item.prize!}
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="panel panel-default">
        <a href="/activity-console/activity-manage/woman-day-work">请点击此处导出EXCEl</a>
    </div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span>
        </div>
        <#if data?has_content>
            <ul class="pagination">
                <li>
                    <#if data.hasPreviousPage>
                    <a href="?index=${data.index - 1}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage>
                    <a href="?index=${data.index + 1}"
                       aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>
                </li>
            </ul>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>