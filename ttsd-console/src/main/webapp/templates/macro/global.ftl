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
                "header":{"text":"系统首页","link":"/"},
                "sidebar":
                [
                    {"name":"myTasks","text":"我的任务","link":"/","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE'"}
                ]
            },
            {
                "name":"project-manage",
                "header":{"text":"项目管理"},
                "sidebar":
                [
                    {"name":"ALL","text":"所有的借款","link":"/project-manage/loan-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"start","text":"发起借款","link":"/project-manage/loan","role":"'ADMIN','OPERATOR'"},
                    {"name":"WAITING_VERIFY","text":"初审的借款","link":"/project-manage/loan-list?status=WAITING_VERIFY","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"RAISING","text":"筹款中的借款","link":"/project-manage/loan-list?status=RAISING","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"RECHECK","text":"复审的借款","link":"/project-manage/loan-list?status=RECHECK","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"REPAYING","text":"还款中的借款","link":"/project-manage/loan-list?status=REPAYING","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"COMPLETE","text":"完成还款的借款","link":"/project-manage/loan-list?status=COMPLETE","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"CANCEL","text":"已经流标的借款","link":"/project-manage/loan-list?status=CANCEL","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"OVERDUE","text":"逾期的借款","link":"/project-manage/loan-list?status=OVERDUE","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"repaymentInfoList","text":"项目还款明细表","link":"/project-manage/loan-repay","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"user-manage",
                "header":{"text":"用户管理"},
                "sidebar":[
                    {"name":"userMan","text":"用户管理","link":"/user-manage/users","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userSearchMan","text":"用户查询","link":"/user-manage/users-search","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"referMan","text":"推荐人管理","link":"/user-manage/referrer","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"agentMan","text":"代理商管理","link":"/user-manage/agents","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"finance-manage",
                "header":{"text":"财务管理"},
                "sidebar":[
                    {"name":"userInvest","text":"用户投资管理","link":"/finance-manage/invests","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"debtRepay","text":"债权还款计划","link":"/finance-manage/debt-repayment-plan","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"recharge","text":"充值记录","link":"/finance-manage/recharge","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"withdraw","text":"提现记录","link":"/finance-manage/withdraw","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userFund","text":"用户资金查询","link":"/finance-manage/user-funds","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"systemBill","text":"系统账户查询","link":"/finance-manage/system-bill","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"adminIntervention","text":"修改账户余额","link":"/finance-manage/admin-intervention","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"realTimeStatus","text":"联动优势余额查询","link":"/finance-manage/real-time-status","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userBalance","text":"用户余额","link":"/finance-manage/account-balance?balanceMin=50","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"systemRecharge","text":"平台账户充值","link":"/finance-manage/system-recharge","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"announce-manage",
                "header":{"text":"公告管理"},
                "sidebar":[
                    {"name":"announceMan","text":"公告管理","link":"/announce-manage/announce","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"feedbackMan","text":"意见反馈","link":"/announce-manage/feedback","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"security",
                "header":{"text":"安全管理"},
                "sidebar":[
                    {"name":"loginLog","text":"登录日志","link":"/security-log/login-log","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"auditLog","text":"管理日志","link":"/security-log/audit-log","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"activity-manage",
                "header":{"text":"活动管理"},
                "sidebar":[
                    {"name":"createCoupon","text":"创建体验券","link":"/activity-manage/coupon","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"statisticsCoupon","text":"体验券管理","link":"/activity-manage/coupons","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"createInterestCoupon","text":"创建加息劵","link":"/activity-manage/interest-coupon","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"statisticsInterestCoupon","text":"加息券管理","link":"/activity-manage/interest-coupons","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"createRedEnvelope","text":"现金红包创建","link":"/activity-manage/red-envelope","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"statisticsRedEnvelope","text":"现金红包管理","link":"/activity-manage/red-envelopes","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"createBirthdayCoupon","text":"生日月活动创建","link":"/activity-manage/birthday-coupon","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"statisticsBirthdayCoupon","text":"生日月活动管理","link":"/activity-manage/birthday-coupons","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userTiandou","text":"用户天豆查询","link":"/activity-manage/user-tiandou","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"tiandouPrize","text":"天豆奖品管理","link":"/activity-manage/tiandou-prize","role":"'ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"pointPrize","text":"财豆奖品管理","link":"/activity-manage/point-prize","role":"'ADMIN','CUSTOMER_SERVICE'"}
                ]
            },
            {
                "name":"app-push-manage",
                "header":{"text":"APP推送管理"},
                "sidebar":[
                    {"name":"manualAppPushManage","text":"手动推送管理","link":"/app-push-manage/manual-app-push-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"createManualAppPush","text":"创建手动推送","link":"/app-push-manage/manual-app-push","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"autoAppPushManage","text":"自动推送管理","link":"/app-push-manage/auto-app-push-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"statistic",
                "header":{"text":"平台数据","link":"/statistic"},
                "sidebar":[
                    {"name":"userDate","text":"用户注册时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userRecharge","text":"用户充值时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userWithdraw","text":"用户提现时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"withdrawUserCount","text":"提现人数分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userInvestViscosity","text":"用户续投情况","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userInvestAmount","text":"用户投资金额时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userInvestCount","text":"用户投资次数时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userAge","text":"用户年龄分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"loanRaisingTimeCosting","text":"标的满标周期分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"loanAmount","text":"标的资金分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"point-manage",
                "header":{"text":"财豆管理"},
                "sidebar":[
                    {"name":"userPointList","text":"用户财豆查询","link":"/point-manage/user-point-list","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"createCouponExchange","text":"优惠券兑换创建","link":"/activity-manage/coupon-exchange","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"couponExchangeManage","text":"优惠券兑换管理","link":"/activity-manage/coupon-exchange-manage","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"}
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
