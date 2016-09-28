<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="loan-application-list.js" headLab="platform-loan" sideLab="platform-loan-list" title="借款申请信息">

<!-- content area begin -->
<div class="col-md-10" id="loanApplicationList">
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    申请时间
                </th>
                <th>
                    申请人姓名
                </th>
                <th>
                    申请人手机号
                </th>
                <th>
                    地址
                </th>
                <th>
                    借款金额（万元）
                </th>
                <th>
                    借款周期（月）
                </th>
                <th>
                    借款类型
                </th>
                <th>
                    抵押物信息
                </th>
                <th>
                    备注
                </th>
                <th>
                    操作
                </th>
            </tr>
            </thead>
            <tbody>
                <#list dataDto.records as loanApplicationView>
                <tr>
                    <td>
                    ${loanApplicationView.createdTime?datetime!}
                    </td>
                    <td>
                    ${loanApplicationView.userName!}
                    </td>
                    <td>
                    ${loanApplicationView.mobile!}
                    </td>
                    <td>
                    ${loanApplicationView.region!}
                    </td>
                    <td>
                    ${loanApplicationView.amount!}
                    </td>
                    <td>
                    ${loanApplicationView.period!}
                    </td>
                    <td>
                    ${loanApplicationView.pledgeType!}
                    </td>
                    <td>
                    ${loanApplicationView.pledgeInfo!}
                    </td>
                    <td>
                    ${loanApplicationView.comment!}
                    </td>
                    <td>
                        <@security.authorize access="hasAnyAuthority('OPERATOR','OPERATOR_ADMIN','ADMIN')">
                            备注
                        </@security.authorize>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${dataDto.count!0}条,每页显示${dataDto.pageSize!10}条</span>
        </div>
        <ul class="pagination">
            <li>
                <#if dataDto.hasPreviousPage>
                <a href="?index=${dataDto.index - 1}&pageSize=${dataDto.pageSize!10}" aria-label="Previous">
                <#else>
                <a href="#" aria-label="Previous">
                </#if>
                <span aria-hidden="true">&laquo; Prev</span>
            </a>
            </li>
            <li><a>${dataDto.index!1}</a></li>
            <li>
                <#if dataDto.hasNextPage>
                <a href="?index=${dataDto.index + 1}&pageSize=${dataDto.pageSize!10}" aria-label="Next">
                <#else>
                <a href="#" aria-label="Next">
                </#if>
                <span aria-hidden="true">Next &raquo;</span>
            </a>
            </li>
        </ul>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->

</@global.main>