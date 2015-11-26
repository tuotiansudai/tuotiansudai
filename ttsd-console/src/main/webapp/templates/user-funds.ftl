<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>用户资金管理</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap-datepicker.css" rel="stylesheet">
    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/libs/bootstrap-select.css"/>
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">

    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/index.css">
    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/libs/jquery-ui/jquery-ui-1.11.4.min.css"/>
<@global.javascript pageJavascript="user-funds.js"></@global.javascript>

</head>
<body>
<@menu.header label="finaMan"></@menu.header>
<div class="main">
    <div class="container-fluid">
        <div class="row">
        <@menu.sidebar headLab="finaMan" sideLab="userFund"></@menu.sidebar>
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
                                <input type='text' class="form-control jq-startTime" value="${(startTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                            </div>
                            -
                            <div class='input-group date' id="investDateEnd">
                                <input type='text' class="form-control jq-endTime" value="${(endTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="control-label">费用类型</label>
                            <select class="selectpicker operationType"  data-style="btn-default" >
                                <option value="">请选择费用类型</option>
                                <#list operationTypeList as operationType>
                                    <option value="${operationType}" <#if userBillOperationType?has_content && operationType == userBillOperationType>selected</#if>>${operationType.description}</option>
                                </#list>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="control-label">操作类型</label>
                            <select class="selectpicker businessType"  data-style="btn-default" >
                                <option value="">全部</option>
                                <#list businessTypeList as businessType>
                                    <option value="${businessType}" <#if userBillBusinessType?has_content && businessType == userBillBusinessType>selected</#if>>${businessType.description}</option>
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
                            <th>费用类型</th>
                            <th>操作类型</th>
                            <th>金额</th>
                            <th>余额</th>
                            <th>冻结金额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list userBillModels as userBillModel>
                            <tr>
                                <td>${(userBillModel.createdTime?string('yyyy-MM-dd'))!}</td>
                                <td>${userBillModel.id?string('0')}</td>
                                <td>${userBillModel.loginName!''}</td>
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
                                    <a href="/userFunds?loginName=${loginName!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&userBillOperationType=${userBillOperationType!}&userBillBusinessType=${userBillBusinessType!}&currentPageNo=${currentPageNo-1}&pageSize=${pageSize}">
                                <#else>
                                    <a href="#">
                                </#if>
                                    <span>« Prev</span></a>
                            </li>
                            <li><a>${currentPageNo}</a></li>
                            <li>
                                <#if hasNextPage >
                                    <a href="/userFunds?loginName=${loginName!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&userBillOperationType=${userBillOperationType!}&userBillBusinessType=${userBillBusinessType!}&currentPageNo=${currentPageNo+1}&pageSize=${pageSize}">
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
        </div>
    </div>
</div>
<!-- main end -->
</body>
</html>
