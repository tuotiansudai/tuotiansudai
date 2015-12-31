<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="user-funds.js" headLab="finance-manage" sideLab="userFund" title="用户资金管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">用户名</label>
                <input type="text" class="form-control jq-loginName" value="${loginName!}">
            </div>
            <div class="form-group">
                <label for="control-label">时间</label>

                <div class='input-group date' id="investDateBegin">
                    <input type='text' class="form-control jq-startTime" value="${(startTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
                -
                <div class='input-group date' id="investDateEnd">
                    <input type='text' class="form-control jq-endTime" value="${(endTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
            </div>
            <div class="form-group">
                <label for="control-label">费用类型</label>
                <select class="selectpicker operationType" data-style="btn-default">
                    <option value="">请选择费用类型</option>
                    <#list operationTypeList as operationType>
                        <option value="${operationType}"
                                <#if userBillOperationType?has_content && operationType == userBillOperationType>selected</#if>>${operationType.description}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label for="control-label">操作类型</label>
                <select class="selectpicker businessType" data-style="btn-default">
                    <option value="">全部</option>
                    <#list businessTypeList as businessType>
                        <option value="${businessType}"
                                <#if userBillBusinessType?has_content && businessType == userBillBusinessType>selected</#if>>${businessType.description}</option>
                    </#list>
                </select>
            </div>
            <button class="btn btn-primary search" type="button">查询</button>
            <button class="btn btn-default reset" type="reset">重置</button>
        </div>

    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>时间</th>
                <th>序号</th>
                <th>用户名</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>费用类型</th>
                <th>操作类型</th>
                <th>金额(元)</th>
                <th>余额(元)</th>
                <th>冻结金额(元)</th>
            </tr>
            </thead>
            <tbody>
                <#list userBillModels as userBillModel>
                <tr>
                    <td>${(userBillModel.createdTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${userBillModel.id?string('0')}</td>
                    <td>${userBillModel.loginName!''}
                        <#if userBillModel.isStaff()>
                            <span class="glyphicon glyphicon glyphicon-user" aria-hidden="true"></span>
                        </#if>
                    </td>
                    <td>${userBillModel.userName}</td>
                    <td>${userBillModel.mobile}</td>
                    <td>${userBillModel.operationType.getDescription()}</td>
                    <td>${userBillModel.businessType.getDescription()}</td>
                    <td>${userBillModel.amount/100}</td>
                    <td>${userBillModel.balance/100}</td>
                    <td>${userBillModel.freeze/100}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${userFundsCount}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="/finance-manage/user-funds?loginName=${loginName!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm:ss'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm:ss'))!}&userBillOperationType=${userBillOperationType!}&userBillBusinessType=${userBillBusinessType!}&currentPageNo=${currentPageNo-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${currentPageNo}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="/finance-manage/user-funds?loginName=${loginName!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm:ss'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm:ss'))!}&userBillOperationType=${userBillOperationType!}&userBillBusinessType=${userBillBusinessType!}&currentPageNo=${currentPageNo+1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span></a>
                </li>
            </ul>
            <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
        </nav>
    </div>
</div>
<!-- content area end -->
</@global.main>
