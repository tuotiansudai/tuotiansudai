<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="cashSnowball" title="现金滚雪球活动">

<!-- content area begin -->
<div class="col-md-10">

    <form action="/activity-console/activity-manage/cash-snowball-list" class="form-inline query-build"
          method="get">
        <div class="form-group">
            <label for="mobile">手机号</label>
            <input type="text" id="mobile" name="mobile" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${mobile!}">
        </div>

        <div class="form-group">
            <label for="number">活动期间内投资额</label>
            <input type="text" class="form-control jq-balance-min" name="startInvestAmount" value="${startInvestAmount!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control jq-balance-max" name="endInvestAmount" value="${endInvestAmount!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>
        <button type="submit" class="btn btn-sm btn-primary">查询</button>
    </form>

    <div class="col-md-3">总投资额:${(sumInvestAmount/100)?string('0.00')}元</div>
    <div class="col-md-3">总累计年化投资额:${(sumAnnualizedAmount/100)?string('0.00')}元</div>
    <div class="col-md-3">总现金奖励:${(sumCashAmount/100)?string('0.00')}元</div>
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
                    活动期内投资金额
                </th>
                <th>
                    累计年化投资额
                </th>
                <th>
                    现金奖励
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
                    ${(item.investAmount/100)?string('0.00')!}
                    </td>
                    <td>
                    ${(item.annualizedAmount/100)?string('0.00')!}
                    </td>
                    <td>
                    ${(item.cashAmount/100)?string('0.00')!}
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <a href="/activity-console/activity-manage/cash-snowball" class="form-control" style="width: 200px">请点击此处导出EXCEl</a>
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