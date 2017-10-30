<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="payroll.js" headLab="finance-manage" sideLab="payrollDetail" title="用户资金发放详情">

<!-- content area begin -->
<div class="col-md-10">
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户姓名</th>
                <th>用户手机号</th>
                <th>发放总金额(元)</th>
                <th>发放状态</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as detail>
                <tr>
                    <td>${detail.userName!}</td>
                    <td>${detail.mobile}</td>
                    <td>${(detail.amount/100)?string('0.00')}</td>
                    <td>
                        <#list payrollStatus as status>
                            <#if detail.status == status>
                        ${status.description}
                        </#if>
                        </#list>
                    </td>
                </tr>
                <#else>
                <tr>
                    <td colspan="18">Empty</td>
                </tr>
                </#list>

            </tbody>

        </table>
    </div>

    <div>
        <table class="table">
            <tr>
                <@security.authorize access="hasAuthority('FINANCE_ADMIN')">
                    <td>
                        <button class="btn btn-primary primary-audit"
                                data-url="/finance-manage/payroll-manage/primary-audit/${payrollId?string('0')}">通过
                        </button>
                    </td>
                    <td>
                        <button class="btn btn-primary reject"
                                data-url="/finance-manage/payroll-manage/reject/${payrollId?string('0')}">驳回
                        </button>
                    </td>
                </@security.authorize>
                <@security.authorize access="hasAuthority('OPERATOR_ADMIN')">
                    <td>
                        <button class="btn btn-primary advanced-audit"
                                data-url="/finance-manage/payroll-manage/advanced-audit/${payrollId?string('0')}">通过
                        </button>
                    </td>
                    <td>
                        <button class="btn btn-primary reject"
                                data-url="/finance-manage/payroll-manage/reject/${payrollId?string('0')}">驳回
                        </button>
                    </td>
                </@security.authorize>
            </tr>
        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <div>
            <span class="bordern">总共${data.count}条, 每页显示${data.pageSize}条</span>
        </div>
        <#if data.records?has_content>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
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

<a href="/export/payroll-detail" class="btn btn-default pull-left">导出EXCEl</a>

<!-- content area end -->
</@global.main>