<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>系统账户查询</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <link href="style/libs/bootstrap/bootstrap-datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="style/libs/bootstrap-select.css" rel="stylesheet"/>
    <link href="style/libs/jquery-ui/jquery-ui-1.10.3.custom.css" rel="stylesheet"/>
    <link href="style/index.css" rel="stylesheet"/>

    <@global.javascript pageJavascript="systemBill.js"></@global.javascript>

</head>
<body>

<#assign pagination = baseDto.data />
<#assign systemBillList = pagination.records />

<@menu.header label="finaMan"></@menu.header>
<div class="main">
    <div class="container-fluid">
        <div class="row">
        <@menu.sidebar headLab="finaMan" sideLab="systemBill"></@menu.sidebar>
            <!-- content area begin -->

            <div class="col-md-10">
                <form action="" class="form-inline query-build">
                    <div class="row">
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
                            <#list systemBillOperationTypeList as operationTypeItem>
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
                            <#list systemBillBusinessTypeList as businessTypeItem>
                                <option value="${businessTypeItem.name()}"
                                        <#if (businessType.name())?has_content && businessType.name() == businessTypeItem.name()>selected</#if>
                                        >${businessTypeItem.description}</option>
                            </#list>
                            </select>
                        </div>

                        <button type="submit" class="btn btn-sm btn-primary">查询</button>
                        <button type="reset" class="btn btn-sm btn-default">重置</button>
                    </div>

                </form>

                <div class="row">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>时间</th>
                            <th>费用类型</th>
                            <th>操作类型</th>
                            <th>金额</th>
                            <th>费用详情</th>
                        </tr>
                        </thead>

                        <tbody>
                        <#if systemBillList?has_content>
                            <#list systemBillList as systemBillItem>
                            <tr>
                                <td>${systemBillItem.createdTime?string('yyyy-MM-dd HH:mm')}</td>
                                <td>${systemBillItem.operationType}</td>
                                <td>${systemBillItem.businessType}</td>
                                <td>${systemBillItem.amount}</td>
                                <td>${systemBillItem.detail}</td>
                            </tr>
                            </#list>
                        <#else>
                        <tr>
                            <td colspan="5">Empty</td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>

                <!-- pagination  -->
                <nav>
                <#if systemBillList?has_content>
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
                </#if>
                </nav>
                <!-- pagination -->
            </div>
            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->
</body>
</html>