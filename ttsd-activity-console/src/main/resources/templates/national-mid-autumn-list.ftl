<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="SchoolSeason" title="国庆节活动">

<!-- content area begin -->
<div class="col-md-10">
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    姓名
                </th>
                <th>
                    手机号
                </th>
                <th>
                    逢万返百标的投资金额
                </th>
                <th>
                    加息6.8%标的投资金额
                </th>
                <th>
                    累计获得现金奖励
                </th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>
                    ${item.userName!}
                    </td>
                    <td>
                    ${item.mobile!}
                    </td>
                    <td>
                    ${(item.sumCashInvestAmount/100)?string('0.00')}
                    </td>
                    <td>
                    ${(item.sumCouponInvestAmount/100)?string('0.00')}
                    </td>
                    <td>
                    ${(item.sumMoneyAmount/100)?string('0.00')}
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
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