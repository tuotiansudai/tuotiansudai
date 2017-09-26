<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="credit-loan-bill.js" headLab="finance-manage" sideLab="CreditLoanBill" title="信用贷账户查询">

<#assign pagination = baseDto.data />
<#assign creditLoanBillList = pagination.records />

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group" id="recordDate">
            <label for="control-label">时间</label>

            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" name="startTime"
                       value="${(startTime?string('yyyy-MM-dd HH:mm'))!}"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" name="endTime"
                       value="${(endTime?string('yyyy-MM-dd HH:mm'))!}"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
            </div>
        </div>
        <div class="form-group">
            <label for="control-label">收益类型</label>
            <select class="selectpicker" name="operationType">
                <option value="">全部</option>
                <#list creditLoanBillOperationTypeList as operationTypeItem>
                    <option value="${operationTypeItem.name()}"
                            <#if (operationType.name())?has_content && operationType.name() == operationTypeItem.name()>selected</#if>
                            >${operationTypeItem.description}</option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="control-label">操作类型</label>
            <select class="selectpicker" name="businessType">
                <option value="">全部</option>
                <#list creditLoanBillBusinessTypeList as businessTypeItem>
                    <option value="${businessTypeItem.name()}"
                            <#if (businessType.name())?has_content && businessType.name() == businessTypeItem.name()>selected</#if>
                            >${businessTypeItem.description}</option>
                </#list>
            </select>
        </div>
        <button type="submit" class="btn btn-sm btn-primary">查询</button>
        <button type="reset" class="btn btn-sm btn-default">重置</button>
    </form>

    <table class="table table-bordered table-hover">
        <thead>
        <tr>
            <th colspan="5">收入总额:&nbsp;${sumIncome/100} 元 &nbsp;&nbsp;&nbsp;支出总额:&nbsp;${sumExpend/100} 元 &nbsp;&nbsp;&nbsp;收益(收入-支出):&nbsp;${sumWin/100} 元</th>
        </tr>
        <tr>
            <th>时间</th>
            <th>费用类型</th>
            <th>操作类型</th>
            <th>金额(元)</th>
        </tr>
        </thead>

        <tbody>
            <#if creditLoanBillList?has_content>
                <#list creditLoanBillList as creditLoanBillItem>
                <tr>
                    <td>${(creditLoanBillItem.createdTime?string('yyyy-MM-dd HH:mm'))!}</td>
                    <td>${creditLoanBillItem.operationType}</td>
                    <td>${creditLoanBillItem.businessType}</td>
                    <td>${creditLoanBillItem.amount}</td>
                </tr>
                </#list>
            <#else>
            <tr>
                <td colspan="5">Empty</td>
            </tr>
            </#if>
        </tbody>
    </table>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if creditLoanBillList?has_content>
            <div>
                <span class="bordern">总共${pagination.count}条,每页显示${pageSize}条</span>
            </div>
            <ul class="pagination">
                <li>
                    <#if pagination.hasPreviousPage >
                    <a href="?startTime=${(startTime?string('yyyy-MM-dd HH:mm'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm'))!}&operationType=${operationType!}&businessType=${businessType!}&pageSize=${pageSize}&index=${index-1}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${pagination.index}</a></li>
                <li>
                    <#if pagination.hasNextPage >
                    <a href="?startTime=${(startTime?string('yyyy-MM-dd HH:mm'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm'))!}&operationType=${operationType!}&businessType=${businessType!}&pageSize=${pageSize}&index=${index+1}"
                       aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>
                </li>
            </ul>
            <@security.authorize access="hasAnyAuthority('DATA')">
                <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
            </@security.authorize>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>