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
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" charset="utf-8"/>
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap-select.css" rel="stylesheet" type="text/css" charset="utf-8"/>
    <link href="${requestContext.getContextPath()}/style/libs/jquery-ui/jquery-ui-1.11.4.min.css" rel="stylesheet" charset="utf-8"/>
    <link href="${requestContext.getContextPath()}/style/index.css" rel="stylesheet" charset="utf-8"/>
<@global.javascript pageJavascript="admin-intervention.js"></@global.javascript>
</head>
<body>

<@menu.header label="finaMan"></@menu.header>

<!-- main begin -->
<div class="main">
    <div class="container-fluid">
        <div class="row">

        <@menu.sidebar headLab="finaMan" sideLab="adminIntervention"></@menu.sidebar>

            <!-- content area begin -->
            <div class="col-md-10">
                <form class="form-horizontal" action="/admin-intervention" method="post">
                    <div class="form-group">
                        <label for="login-name" class="col-sm-1 control-label">用户名</label>

                        <div class="col-sm-2">
                            <input id="login-name" name="loginName" class="form-control" value="${data.loginName!}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="operation-type" class="col-sm-1 control-label">操作</label>

                        <div class="col-sm-2">
                            <select id="operation-type" name="operationType" class="selectpicker form-control">
                            <#list operationTypes as operationType>
                                <option value="${operationType.name()}"
                                        <#if (!(data.operationType)?? && operationType_index==0) || ((data.operationType)?? && data.operationType==operationType)>selected</#if>>
                                ${operationType.description}
                                </option>
                            </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="amount" class="col-sm-1 control-label">金额</label>

                        <div class="col-sm-2">
                            <input id="amount" name="amount" type="text" class="form-control" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00" value="${data.amount!}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="description" class="col-sm-1 control-label">详情</label>

                        <div class="col-sm-2">
                            <textarea name="description" id="description" class="form-control">${data.description!}</textarea>
                        </div>
                    </div>

                    <div class="form-group console-error-message" <#if !(message??)>style="display: none"</#if>>
                        <div class="col-sm-offset-1 col-sm-2">
                            <div class="alert alert-danger" role="alert"><#if message??>${message}</#if></div>
                        </div>
                    </div>

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="form-group">
                        <div class="col-sm-offset-1 col-sm-2">
                            <input class="btn btn-default btn-submit" type="submit" value="提交">
                            <a class="btn btn-default" href="/admin-intervention" role="button">重置</a>
                        </div>
                    </div>
                </form>
            </div>

            <!-- Modal -->
            <div class="modal fade" id="confirm-modal" tabindex="-1" role="dialog">
                <div class="modal-dialog modal-sm" role="document">
                    <div class="modal-content">
                        <div class="modal-body">
                            <h5>确认修改？</h5>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-default btn-submit">确认</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->
</body>
</html>