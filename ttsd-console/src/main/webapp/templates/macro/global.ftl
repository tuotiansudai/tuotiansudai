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
                "sidebar":[
                    {"name":"userDate","text":"用户注册时间分布","link":"/","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"userRecharge","text":"用户充值时间分布","link":"/","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"userWithdraw","text":"用户提现时间分布","link":"/","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"withdrawUserCount","text":"提现人数分布","link":"/","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"userInvestViscosity","text":"用户续投情况","link":"/","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"userInvestAmount","text":"用户投资金额时间分布","link":"/","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"userInvestCount","text":"用户投资次数时间分布","link":"/","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"userAge","text":"用户年龄分布","link":"/","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"loanRaisingTimeCosting","text":"标的满标周期分布","link":"/","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"loanAmount","text":"标的资金分布","link":"/","role":"'ADMIN','CUSTOMER_SERVICE'"}
                ]

            },
            {
                "name":"project-manage",
                "header":{"text":"项目管理"},
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
                "header":{"text":"用户管理"},
                "sidebar":[
                    {"name":"userMan","text":"用户管理","link":"/user-manage/users","role":"'ADMIN'"},
                    {"name":"userSearchMan","text":"用户查询","link":"/user-manage/users-search","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"referMan","text":"推荐人管理","link":"/user-manage/referrer","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"agentMan","text":"代理商管理","link":"/user-manage/agents","role":"'ADMIN'"}
                ]
            },
            {
                "name":"finance-manage",
                "header":{"text":"财务管理"},
                "sidebar":[
                    {"name":"userInvest","text":"用户投资管理","link":"/finance-manage/invests","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"debtRepay","text":"债权还款计划","link":"/finance-manage/debt-repayment-plan","role":"'ADMIN'"},
                    {"name":"recharge","text":"充值记录","link":"/finance-manage/recharge","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"withdraw","text":"提现记录","link":"/finance-manage/withdraw","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"userFund","text":"用户资金查询","link":"/finance-manage/user-funds","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"systemBill","text":"系统账户查询","link":"/finance-manage/system-bill","role":"'ADMIN'"},
                    {"name":"adminIntervention","text":"修改账户余额","link":"/finance-manage/admin-intervention","role":"'ADMIN'"},
                    {"name":"realTimeStatus","text":"联动优势余额查询","link":"/finance-manage/real-time-status","role":"'ADMIN'"},
                    {"name":"userBalance","text":"用户余额","link":"/finance-manage/account-balance?balanceMin=50","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"systemRecharge","text":"平台账户充值","link":"/finance-manage/system-recharge","role":"'ADMIN'"}
                ]
            },
            {
                "name":"announce-manage",
                "header":{"text":"公告管理"},
                "sidebar":[
                    {"name":"announceMan","text":"公告管理","link":"/announce-manage/announce","role":"'ADMIN'"},
                    {"name":"feedbackMan","text":"意见反馈","link":"/announce-manage/feedback","role":"'ADMIN'"}
                ]
            },
            {
                "name":"security",
                "header":{"text":"安全管理"},
                "sidebar":[
                    {"name":"loginLog","text":"登录日志","link":"/security-log/login-log","role":"'ADMIN'"},
                    {"name":"auditLog","text":"管理日志","link":"/security-log/audit-log","role":"'ADMIN'"}
                ]
            },
            {
                "name":"activity-manage",
                "header":{"text":"活动管理"},
                "sidebar":[
                    {"name":"createCoupon","text":"创建体验券","link":"/activity-manage/coupon","role":"'ADMIN'"},
                    {"name":"statisticsCoupon","text":"体验券管理","link":"/activity-manage/coupons","role":"'ADMIN'"},
                    {"name":"createInterestCoupon","text":"创建加息劵","link":"/activity-manage/interest-coupon","role":"'ADMIN'"},
                    {"name":"statisticsInterestCoupon","text":"加息券管理","link":"/activity-manage/interest-coupons","role":"'ADMIN'"},
                    {"name":"createRedEnvelope","text":"现金红包创建","link":"/activity-manage/red-envelope","role":"'ADMIN'"},
                    {"name":"statisticsRedEnvelope","text":"现金红包管理","link":"/activity-manage/red-envelopes","role":"'ADMIN'"},
                    {"name":"createBirthdayCoupon","text":"生日月活动创建","link":"/activity-manage/birthday-coupon","role":"'ADMIN'"},
                    {"name":"statisticsBirthdayCoupon","text":"生日月活动管理","link":"/activity-manage/birthday-coupons","role":"'ADMIN'"}
                ]
            },
            {
                "name":"app-push-manage",
                "header":{"text":"APP推送管理"},
                "sidebar":[
                    {"name":"manualAppPushManage","text":"手动推送管理","link":"/app-push-manage/manual-app-push-list","role":"'ADMIN'"},
                    {"name":"createManualAppPush","text":"创建手动推送","link":"/app-push-manage/manual-app-push","role":"'ADMIN'"},
                    {"name":"autoAppPushManage","text":"自动推送管理","link":"/app-push-manage/auto-app-push-list","role":"'ADMIN'"}
                ]
            },
            {
                "name":"point-manage",
                "header":{"text":"财豆管理"},
                "sidebar":[
                    {"name":"userPointList","text":"用户财豆查询","link":"/point-manage/user-point-list","role":"'ADMIN'"},
                    {"name":"createCouponExchange","text":"优惠券兑换创建","link":"/activity-manage/coupon-exchange","role":"'ADMIN'"},
                    {"name":"","text":"优惠券兑换管理","link":"","role":""}
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
    <link href="${applicationContext}/style/libs/jquery.pageflip.css" rel="stylesheet">
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
<header class="navbar" id="top" role="banner">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="${applicationContext}/" class="navbar-brand">
                <img src="${applicationContext}/images/logo.png" alt="" />
            </a>
        </div>
        <div class="collapse navbar-collapse">
            <p class="navbar-text navbar-right"><a id="logout-link" href="/logout">注销</a>【<@global.security.authentication property="principal.username" />】</p>
            <form id="logout-form" action="/logout" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
    </div>
    <nav id="bs-navbar" class="collapse navbar-collapse top-menu">
        <div class="container-fluid">
            <ul class="nav navbar-nav">
                <#list menus as menu>
                    <#list menu.sidebar as item>
                        <#if item.role??>
                            <@role hasRole=item.role>
                                <li <#if menu.name == headLab>class="active"</#if>>
                                    <a href="${item.link}">${menu.header.text}</a>
                                </li>
                                <#break>
                            </@role>
                        </#if>
                    </#list>
                </#list>
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
                <ul class="nav sidenav">
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
