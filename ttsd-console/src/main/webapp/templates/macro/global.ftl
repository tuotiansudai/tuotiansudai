<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#assign applicationContext=requestContext.getContextPath() />

<#macro role hasRole>
    <@security.authorize access="hasAnyAuthority(${hasRole})">
        <#nested>
    </@security.authorize>
</#macro>

<#assign menus=
        [
            {
                "name":"sys-manage",
                "role":"'ADMIN'",
                "header":{"text":"系统主页","link":"/"},
                "sidebar":[]
            },
            {
                "name":"project-manage",
                "role":"'ADMIN'",
                "header":{"text":"项目管理","link":"/project-manage/loan-list"},
                "sidebar":
                [
                    {"name":"ALL","text":"所有的借款","link":"/project-manage/loan-list","role":"'ADMIN'"},
                    {"name":"start","text":"发起借款","link":"/project-manage/loan","role":"'ADMIN'"},
                    {"name":"WAITING_VERIFY","text":"初审的借款","link":"/project-manage/loan-list?status=WAITING_VERIFY","role":"'ADMIN'"},
                    {"name":"RAISING","text":"筹款中的借款","link":"/project-manage/loan-list?status=RAISING","role":"'ADMIN'"},
                    {"name":"RECHECK","text":"复审的借款","link":"/project-manage/loan-list?status=RECHECK","role":"'ADMIN'"},
                    {"name":"REPAYING","text":"还款中的借款","link":"/project-manage/loan-list?status=REPAYING","role":"'ADMIN'"},
                    {"name":"COMPLETE","text":"完成还款的借款","link":"/project-manage/loan-list?status=COMPLETE","role":"'ADMIN'"},
                    {"name":"CANCEL","text":"已经流标的借款","link":"/project-manage/loan-list?status=CANCEL","role":"'ADMIN'"},
                    {"name":"OVERDUE","text":"逾期的借款","link":"/project-manage/loan-list?status=OVERDUE","role":"'ADMIN'"},
                    {"name":"repaymentInfoList","text":"项目还款明细表","link":"/project-manage/loan-repay","role":"'ADMIN'"}
                ]
            },
            {
                "name":"user-manage",
                "role":"'ADMIN','CUSTOMER_SERVICE'",
                "header":{"text":"用户管理","link":"/user-manage/referrer"},
                "sidebar":[
                    {"name":"addUser","text":"添加用户","link":"/user-manage/user/create","role":"'ADMIN'"},
                    {"name":"userMan","text":"用户管理","link":"/user-manage/users","role":"'ADMIN'"},
                    {"name":"referMan","text":"推荐人管理","link":"/user-manage/referrer","role":"'ADMIN','CUSTOMER_SERVICE'"}
                ]
            },
            {
                "name":"finance-manage",
                "role":"'ADMIN','CUSTOMER_SERVICE'",
                "header":{"text":"财务管理","link":"/finance-manage/invests"},
                "sidebar":[
                    {"name":"userInvest","text":"用户投资管理","link":"/finance-manage/invests","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"debtRepay","text":"债权还款计划","link":"/finance-manage/debt-repayment-plan","role":"'ADMIN'"},
                    {"name":"recharge","text":"充值记录","link":"/finance-manage/recharge","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"withdraw","text":"提现记录","link":"/finance-manage/withdraw","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"userFund","text":"用户资金查询","link":"/finance-manage/user-funds","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"systemBill","text":"系统账户查询","link":"/finance-manage/system-bill","role":"'ADMIN'"},
                    {"name":"adminIntervention","text":"管理员修改账户余额","link":"/finance-manage/admin-intervention","role":"'ADMIN'"},
                    {"name":"realTimeStatus","text":"联动优势余额查询","link":"/finance-manage/real-time-status","role":"'ADMIN'"}
                ]
            },
            {
                "name":"announce-manage",
                "role":"'ADMIN'",
                "header":{"text":"公告管理","link":"/announce-manage/announce"},
                "sidebar":[
                    {"name":"announceMan","text":"公告管理","link":"/announce-manage/announce","role":"'ADMIN'"}
                ]
            },
            {
                "name":"security",
                "role":"'ADMIN'",
                "header":{"text":"安全管理","link":"/security-log/login-log"},
                "sidebar":[
                    {"name":"loginLog","text":"登录日志","link":"/security-log/login-log","role":"'ADMIN'"},
                    {"name":"auditLog","text":"管理日志","link":"/security-log/audit-log","role":"'ADMIN'"}
                ]
            }
        ]
>

<#macro main pageCss pageJavascript headLab="" sideLab="" title="拓天速贷">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>${title}</title>
    <link href="${applicationContext}/images/favicon.ico" rel="shortcut icon" type="image/x-icon"/>
    <!-- link bootstrap css -->
    <link href="${applicationContext}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${applicationContext}/style/libs/bootstrap-datepicker.css" rel="stylesheet">
    <link href="${applicationContext}/style/libs/bootstrap/bootstrap-datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="${applicationContext}/style/libs/bootstrap-select.css" rel="stylesheet" type="text/css" charset="utf-8"/>
    <link href="${applicationContext}/style/libs/jquery-ui/jquery-ui-1.11.4.min.css" rel="stylesheet" charset="utf-8"/>
    <link href="${applicationContext}/style/index.css" rel="stylesheet">
    <link href="${applicationContext}/style/libs/fileinput.css" rel="stylesheet"/><!--上传图片插件-->
    <#if pageCss?? && pageCss != "">
        <link rel="stylesheet" type="text/css" href="${applicationContext}/style/dest/${pageCss}" charset="utf-8"/>
    </#if>

    <!-- link bootstrap js -->
    <#if pageJavascript?? && pageJavascript != "">
        <script src="${applicationContext}/js/libs/config.js"></script>
        <script src="${applicationContext}/js/libs/require-2.1.20.min.js" defer="defer" async="async"
                data-main="${applicationContext}/js/${pageJavascript}"></script>
    </#if>
</head>

<body>
<!-- header begin -->
<header class="navbar navbar-static-top bs-docs-nav" id="top" role="banner">
    <div class="container-fluid">
        <div class="navbar-header">
            <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target="#bs-navbar"
                    aria-controls="bs-navbar" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="${applicationContext}" class="navbar-brand"><img
                    src="${applicationContext}/images/logo.png" alt=""></a>
        </div>
    </div>
    <nav id="bs-navbar" class="collapse navbar-collapse">
        <div class="container-fluid">
            <ul class="nav navbar-nav">
                <#list menus as menu>
                    <#if menu.role??>
                        <@role hasRole=menu.role>
                            <li <#if menu.name == headLab>class="active"</#if>>
                                <a href="${menu.header.link}">${menu.header.text}</a>
                            </li>
                        </@role>
                    <#else>
                        <li <#if menu.name == headLab>class="active"</#if>>
                            <a href="${menu.header.link}">${menu.header.text}</a>
                        </li>
                    </#if>
                </#list>
            </ul>
            <ul class="nav navbar-nav logout">
                <li>
                    <a id="logout-link" href="/logout">退出</a>
                    <form id="logout-form" action="/logout" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                </li>
            </ul>
        </div>
    </nav>
</header>
<!-- header end -->

<!-- main begin -->
<div class="main">
    <div class="container-fluid">
        <div class="row">

            <!-- menu sidebar -->
            <div class="col-md-2">
                <ul class="nav bs-docs-sidenav">
                    <#list menus as menu>
                        <#if menu.name=headLab>
                            <#list menu.sidebar as item>
                                <#if item.role??>
                                    <@role hasRole=item.role>
                                        <li <#if item.name == sideLab>class="active"</#if>>
                                            <a href="${item.link}">${item.text}</a>
                                        </li>
                                    </@role>
                                <#else>
                                    <li <#if item.name == sideLab>class="active"</#if>>
                                        <a href="${item.link}">${item.text}</a>
                                    </li>
                                </#if>
                            </#list>
                        </#if>
                    </#list>
                </ul>
            </div>
            <!-- menu sidebar end -->

            <!-- content area begin -->
            <#nested>
            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->
<script type="text/javascript" charset="utf-8">
    document.getElementById("logout-link").addEventListener('click', function (event) {
        event.preventDefault();
        document.getElementById("logout-form").submit();
    });
</script>
</body>
</html>
</#macro>
