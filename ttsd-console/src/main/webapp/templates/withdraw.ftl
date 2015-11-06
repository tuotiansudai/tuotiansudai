<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>提现记录</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <link href="style/libs/bootstrap/bootstrap-datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="style/libs/bootstrap-select.css" rel="stylesheet"/>
    <link href="style/libs/jquery-ui/jquery-ui-1.10.3.custom.css" rel="stylesheet"/>
    <link href="style/index.css" rel="stylesheet"/>


    <@global.javascript pageJavascript="withdraw.js"></@global.javascript>

</head>
<body>

<#assign pagination = baseDto.data />
<#assign withdrawList = pagination.records />

<@menu.header label="finaMan"></@menu.header>
<div class="main">
    <div class="container-fluid">
        <div class="row">
        <@menu.sidebar headLab="finaMan" sideLab="withdraw"></@menu.sidebar>
            <!-- content area begin -->

            <div class="col-md-10">
                <form action="" class="form-inline query-build">
                    <div class="row">
                        <div class="form-group">
                            <label for="control-label">编号</label>
                            <input type="text" class="form-control" name="withdrawId" placeholder="" value="${withdrawId!}">
                        </div>
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
                        </br>
                        <div class="form-group">
                            <label for="control-label">用户名</label>
                            <input type="text" id="loginName" name="loginName" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${loginName!}" />
                        </div>
                        <div class="form-group">
                            <label for="control-label">状态</label>
                            <select class="selectpicker" name="status">
                                <option value="">全部</option>
                            <#list withdrawStatusList as statusItem>
                                <option value="${statusItem.name()}"
                                        <#if (status.name())?has_content && status.name() == statusItem.name()>selected</#if>
                                        >${statusItem.description}</option>
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
                            <th>提现编号</th>
                            <th>申请时间</th>
                            <th>初审时间</th>
                            <th>复核时间</th>
                            <th>用户名</th>
                            <th>真实姓名</th>
                            <th>提现金额</th>
                            <th>手续费</th>
                            <th>管理员提现</th>
                            <th>银行卡</th>
                            <th>状态</th>
                        </tr>
                        </thead>

                        <tbody>
                        <#if withdrawList?has_content>
                            <#list withdrawList as withdrawItem>
                            <tr>
                                <td>${withdrawItem.withdrawId}</td>
                                <td>${(withdrawItem.createdTime?string('yyyy-MM-dd HH:mm'))!}</td>
                                <td>${(withdrawItem.verifyTime?string('yyyy-MM-dd HH:mm'))!}</td>
                                <td>${(withdrawItem.recheckTime?string('yyyy-MM-dd HH:mm'))!}</td>
                                <td>${withdrawItem.loginName}</td>
                                <td>${withdrawItem.userName}</td>
                                <td>${withdrawItem.amount}</td>
                                <td>${withdrawItem.fee}</td>
                                <td><#if withdrawItem.adminRole==1>是<#else>否</#if></td>
                                <td>TODO</td>
                                <td>${withdrawItem.status}</td>
                            </tr>
                            </#list>
                        <#else>
                        <tr>
                            <td colspan="11">Empty</td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>

                <!-- pagination  -->
                <nav>
                <#if withdrawList?has_content>
                    <div>
                        <span class="bordern">总共${pagination.count}条,每页显示${pageSize}条</span>
                    </div>
                    <ul class="pagination">
                        <li>
                            <#if pagination.hasPreviousPage >
                            <a href="?withdrawId=${withdrawId!}&loginName=${loginName!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm'))!}&status=${status!}&pageSize=${pageSize}&index=${index-1}"
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
                            <a href="?withdrawId=${withdrawId!}&loginName=${loginName!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm'))!}&status=${status!}&pageSize=${pageSize}&index=${index+1}"
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