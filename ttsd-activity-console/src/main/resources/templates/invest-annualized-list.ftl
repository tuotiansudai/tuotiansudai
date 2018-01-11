<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="invest-annualized-list" headLab="activity-manage" sideLab="investAnnualized" title="投资金额统计">

<!-- content area begin -->
<div class="col-md-10" xmlns="http://www.w3.org/1999/html">

    <form action="/activity-console/activity-manage/invest-annualized-list" class="form-inline query-build" id="investAnnualizedForm"
          method="get">

        <div class="form-group">
            <label>活动类型</label>
            <select class="selectpicker" name="activityInvestAnnualized">
                <#list activityInvestAnnualizeds as type>
                    <option value="${type}" <#if activityInvestAnnualizeds?? && type==selectType>selected</#if>>
                    ${type.activityName}
                    </option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="mobile">手机号</label>
            <input type="text" id="mobile" name="mobile" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${mobile!}">
        </div>

        <button type="submit" class="btn btn-sm btn-primary">查询</button>
    </form>

    <a href="/activity-console/activity-manage/invest-annualized?activityInvestAnnualized=${selectType!}&mobile=${mobile!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTine=${(endTine?string('yyyy-MM-dd'))!}" class="form-control" style="width: 100px">导出EXCEL</a></br>

    <div class="col-md-3">总投资额:${(sumInvestAmount/100)?string('0.00')}元</div>
    <div class="col-md-3">总累计年化投资额:${(sumAnnualizedAmount/100)?string('0.00')}元</div>
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    姓名
                </th>
                <th>
                    用户名
                </th>
                <th>
                    手机号
                </th>
                <th>
                    活动期内投资金额
                </th>
                <th>
                    累计年化投资额
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
                    ${item.loginName!}
                    </td>
                    <td>
                    ${item.mobile!}
                    </td>
                    <td>
                    ${(item.sumInvestAmount/100)?string('0.00')!}
                    </td>
                    <td>
                    ${(item.sumAnnualizedAmount/100)?string('0.00')!}
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <br/>
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