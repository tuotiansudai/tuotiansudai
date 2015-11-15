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
    <link href="${requestContext.getContextPath()}/style/libs/jquery-ui/jquery-ui-1.11.4.min.css" rel="stylesheet"/>
    <link href="${requestContext.getContextPath()}/style/index.css" rel="stylesheet"/>
<@global.javascript pageJavascript="edit-user.js"></@global.javascript>
</head>
<body>

<@menu.header label="userMain"></@menu.header>

<!-- main begin -->
<div class="main">
    <div class="container-fluid">
        <div class="row">

        <@menu.sidebar headLab="userMain" sideLab="userMain"></@menu.sidebar>

            <!-- content area begin -->
            <div class="col-md-10">
                <form class="form-horizontal" action="/user/edit" method="post">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">登录名：</label>
                        <div class="col-sm-3">
                            <p class="form-control-static">${user.loginName}</p>
                            <input type="hidden" name="loginName" value="${user.loginName}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">姓名：</label>
                        <div class="col-sm-3">
                            <p class="form-control-static">${(user.userName)!}</p>
                            <input type="hidden" name="userName" value="${user.userName}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">身份证：</label>
                        <div class="col-sm-3">
                            <p class="form-control-static">${(user.identityNumber)!}</p>
                            <input type="hidden" name="identityNumber" value="${user.identityNumber}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="mobile" class="col-sm-2 control-label">手机号码：</label>
                        <div class="col-sm-3">
                            <input name="mobile" id="mobile" type="text" class="form-control" maxlength="11" value="${(user.mobile)!}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">电子邮件：</label>
                        <div class="col-sm-3">
                            <input name="email" id="email" type="email" class="form-control" value="${(user.email)!}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="referrer" class="col-sm-2 control-label">推荐人：</label>
                        <div class="col-sm-3">
                            <input name="referrer" id="referrer" type="text" class="form-control" value="${(user.referrer)!}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="status" class="col-sm-2 control-label">状态：</label>
                        <div class="col-sm-3">
                            <label class="radio-inline"><input type="radio" name="status" id="status-active" value="ACTIVE" <#if user.status=="ACTIVE">checked="checked"</#if>>正常</label>
                            <label class="radio-inline"><input type="radio" name="status" id="status-in-active" value="INACTIVE" <#if user.status=="INACTIVE">checked="checked"</#if>>禁用</label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="referrer" class="col-sm-2 control-label">角色：</label>
                        <div class="col-sm-3">
                            <input type="hidden" name="roles" value="USER"/>
                            <div class="checkbox">
                                <label><input type="checkbox" name="roles" <#if user.roles?seq_contains("INVESTOR")>checked="checked"</#if> value="INVESTOR">投资人</label>
                            </div>
                            <div class="checkbox">
                                <label><input type="checkbox" name="roles" <#if user.roles?seq_contains("LOANER")>checked="checked"</#if> value="LOANER">借款人</label>
                            </div>
                            <div class="checkbox">
                                <label><input type="checkbox" name="roles" <#if user.roles?seq_contains("MERCHANDISER")>checked="checked"</#if> value="MERCHANDISER">业务员</label>
                            </div>
                            <div class="checkbox">
                                <label><input type="checkbox" name="roles" <#if user.roles?seq_contains("CUSTOMER_SERVICE")>checked="checked"</#if> value="CUSTOMER_SERVICE">客服</label>
                            </div>
                            <div class="checkbox">
                                <label><input type="checkbox" name="roles" <#if user.roles?seq_contains("ADMIN")>checked="checked"</#if> value="ADMIN">管理员</label>
                            </div>
                        </div>
                    </div>

                    <#if errorMessage??>
                        <div class="form-group console-error-message">
                            <div class="col-sm-offset-2 col-sm-3">
                                <div class="alert alert-danger" role="alert">${errorMessage}</div>
                            </div>
                        </div>
                    </#if>

                    <div class="form-group web-error-message">
                        <div class="col-sm-offset-2 col-sm-3">
                            <div class="alert alert-danger message" role="alert"></div>
                        </div>
                    </div>

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-3">
                            <input class="btn btn-default btn-submit" type="submit" value="提交">
                            <input class="btn btn-default" type="reset" value="重置">
                        </div>
                    </div>
                </form>
            </div>
            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->
</body>
</html>