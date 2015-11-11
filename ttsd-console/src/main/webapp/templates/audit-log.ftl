<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>用户管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
<@global.csrf></@global.csrf>
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/bootstrap-datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap-select.css" rel="stylesheet"/>
    <link href="${requestContext.getContextPath()}/style/libs/jquery-ui/jquery-ui-1.11.4.min.css" rel="stylesheet"/>
    <link href="${requestContext.getContextPath()}/style/index.css" rel="stylesheet"/>
<@global.javascript pageJavascript="audit-log.js"></@global.javascript>
</head>
<body>

<@menu.header label="security"></@menu.header>

<!-- main begin -->
<div class="main">
    <div class="container-fluid">
        <div class="row">

        <@menu.sidebar headLab="security" sideLab="auditLog"></@menu.sidebar>

            <!-- content area begin -->
            <div class="col-md-10">
                <form action="/" class="form-inline query-build">
                    <div class="form-group">
                        <label for="loginName">用户名</label>
                        <input type="text" id="login-name" name="loginName" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${loginName!}"/>
                    </div>

                    <div class="form-group">
                        <label for="loginName">操作人</label>
                        <input type="text" id="operator-login-name" name="operatorLoginName" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${operatorLoginName!}"/>
                    </div>

                    <div class="form-group">
                        <label>日期</label>

                        <div class='input-group date' id='startTime'>
                            <input type='text' class="form-control" name="startTime" value="${(startTime?string('yyyy-MM-dd'))!}"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                        -
                        <div class='input-group date' id='endTime'>
                            <input type='text' class="form-control" name="endTime" value="${(endTime?string('yyyy-MM-dd'))!}"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>

                    <button class="btn btn-sm btn-primary query">查询</button>
                    <a href="/security-log/audit-log" class="btn btn-sm btn-default">重置</a>
                </form>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>操作时间</th>
                            <th>IP</th>
                            <th>用户名</th>
                            <th>操作人</th>
                            <th>操作详情</th>

                        </tr>
                        </thead>
                        <tbody>
                        <#list data.records as record>
                        <tr>
                            <td>${record.operationTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td>${record.ip}</td>
                            <td>${record.loginName}</td>
                            <td>${record.operatorLoginName!}</td>
                            <td>${record.description}</td>
                        </tr>
                        <#else>
                        <tr>
                            <td colspan="5">无记录</td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <nav>
                    <div>
                        <span class="bordern">总共${data.count!('0')}条,每页显示${data.pageSize}条</span>
                    </div>
                    <ul class="pagination">
                        <li <#if !data.hasPreviousPage>class="disabled"</#if>>
                            <a href="javascript:" class="previous <#if !data.hasPreviousPage>disabled</#if>"><span aria-hidden="true">&laquo;</span></a>
                        </li>
                        <li class="disabled"><a class="current-page" data-index="${data.index}">${data.index}</a></li>
                        <li <#if !data.hasNextPage>class="disabled"</#if>>
                            <a href="javascript:" class="next <#if !data.hasNextPage>disabled</#if>"><span aria-hidden="true">&raquo;</span></a>
                        </li>
                    </ul>
                </nav>
            </div>
            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->
</body>
</html>