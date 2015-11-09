<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>投资记录</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" charset="utf-8"/>
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" charset="utf-8">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap-select.css" rel="stylesheet" type="text/css" charset="utf-8"/>
    <link href="${requestContext.getContextPath()}/style/libs/jquery-ui/jquery-ui-1.11.4.min.css" rel="stylesheet" type="text/css" charset="utf-8"/>
    <link href="${requestContext.getContextPath()}/style/index.css" rel="stylesheet" type="text/css" charset="utf-8"/>
<@global.javascript pageJavascript="real-time-status.js"></@global.javascript>

</head>
<body>
<@menu.header label="finaMan"></@menu.header>
<div class="main">
    <div class="container-fluid">
        <div class="row">
        <@menu.sidebar headLab="finaMan" sideLab="realTimeStatus"></@menu.sidebar>

            <div class="col-md-10">
                <form action="" method="get" class="form-inline query-build">
                    <div class="form-group">
                        <label for="project">账户类型</label>
                        <select class="selectpicker" name="type">
                            <option value="user" <#if !(type??) || type=='user'>selected</#if>>用户账户</option>
                            <option value="platform" <#if type?? && type=='platform'>selected</#if>>平台账户</option>
                            <option value="loan" <#if type?? && type=='loan'>selected</#if>>标的账户</option>
                        </select>
                    </div>

                    <div class="login-name form-group" <#if type?? && type != 'user'>style="display: none"</#if>>
                        <label for="loginName">用户名</label>
                        <input type="text" id="loginName" name="loginName" class="form-control ui-autocomplete-input"/>
                    </div>

                    <div class="loan form-group" <#if !(type??) || type != 'loan'>style="display: none"</#if>>
                        <label for="loanId">标的号</label>
                        <input type="text" id="loanId" name="loanId" class="form-control ui-autocomplete-input"/>
                    </div>

                    <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
                </form>

                <#if data??>
                    <div class="panel panel-default">
                        <div class="panel-body form-horizontal">
                            <#list data?keys as key>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">${key}</label>
                                    <div class="col-sm-3">
                                        <p class="form-control-static">${data[key]}</p>
                                    </div>
                                </div>
                            <#else>
                                无结果
                            </#list>
                        </div>
                    </div>
                </#if>

            </div>
        </div>
    </div>
</div>

</body>
</html>