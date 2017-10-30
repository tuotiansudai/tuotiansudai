<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="finance-manage" sideLab="payrollDetail" title="用户资金发放详情">

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
                <#list data as detail>
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
</div>

<!-- content area end -->
</@global.main>