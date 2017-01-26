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
                    {"name":"myTasks","text":"我的任务"},
                    {"name":"myTasks","text":"我的任务","link":"/","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE', 'ASK_ADMIN'"}
                ]
            },
            {
                "name":"project-manage",
                "header":{"text":"项目管理"},
                "sidebar":
                [
                    {"name":"LOAN","text":"直投项目管理"},
                    {"name":"ALL","text":"所有的借款","link":"/project-manage/loan-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"start","text":"发起借款","link":"/project-manage/loan","role":"'ADMIN','OPERATOR'"},
                    {"name":"TRANSFER","text":"转让项目管理"},
                    {"name":"WAITING_VERIFY","text":"所有的转让项目","link":"/project-manage/loan-list?status=WAITING_VERIFY","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"transfer-rule","text":"转让规则","link":"/transfer-manage/transfer-rule","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"user-manage",
                "header":{"text":"用户管理"},
                "sidebar":[
                    {"name":"userMan","text":"用户管理"},
                    {"name":"userMan","text":"用户管理","link":"/user-manage/users","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"referMan","text":"推荐人管理","link":"/user-manage/referrer","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"agentMan","text":"代理商管理","link":"/user-manage/agents","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"finance-manage",
                "header":{"text":"财务管理"},
                "sidebar":[
                    {"name":"InvestManager","text":"投资管理"},
                    {"name":"userInvest","text":"用户投资管理","link":"/finance-manage/invests","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"financeReport","text":"债权财务数据","link":"/finance-manage/financeReport","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"debtRepay","text":"债权还款计划","link":"/finance-manage/debt-repayment-plan","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"InvestManager","text":"用户账户管理"},
                    {"name":"userBalance","text":"用户余额查询","link":"/finance-manage/account-balance?balanceMin=50","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userFund","text":"用户资金查询","link":"/finance-manage/user-funds","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"recharge","text":"充值记录","link":"/finance-manage/recharge","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"withdraw","text":"提现记录","link":"/finance-manage/withdraw","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"adminIntervention","text":"修改账户余额","link":"/finance-manage/admin-intervention","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"InvestManager","text":"系统账户管理"},
                    {"name":"systemBill","text":"系统账户查询","link":"/finance-manage/system-bill","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"systemRecharge","text":"平台账户充值","link":"/finance-manage/system-recharge","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"InvestManager","text":"联动优势账户管理"},
                    {"name":"realTimeStatus","text":"联动优势余额查询","link":"/finance-manage/real-time-status","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"bankMange","text":"银行卡管理","link":"/finance-manage/bank-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"message-manage",
                "header":{"text":"内容管理"},
                "sidebar":[
                    {"name":"messageManager","text":"消息管理"},
                    {"name":"autoMessageManage","text":"自动发送站内信管理","link":"/message-manage/auto-message-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"manualMessageManage","text":"手动发送站内信管理","link":"/message-manage/manual-message-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"createManualMessage","text":"创建手动站内信","link":"/message-manage/manual-message","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"announceMan","text":"公告管理","link":"/announce-manage/announce","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"contentManager","text":"平台内容管理"},
                    {"name":"bannerMan","text":"banner管理","link":"/banner-manage/list","role":"'ADMIN','OPERATOR_ADMIN'"},
                    {"name":"activityCenter","text":"活动中心","link":"/activity-manage/activity-center-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"promotion","text":"APP弹窗推送管理","link":"/activity-console/activity-manage/promotion/promotion-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"mediumManager","text":"媒体内容管理"},
                    {"name":"linkExchangeMan","text":"友链管理","link":"/link-exchange-manage/link-exchange","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"articleMan","text":"拓天资讯","link":"/announce-manage/article/list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','EDITOR'"}
                ]
            },
            {
                "name":"activity-manage",
                "header":{"text":"活动管理"},
                "sidebar":[
                    {"name":"couponManager","text":"优惠券管理"},
                    {"name":"createInterestCoupon","text":"创建优惠券","link":"/activity-manage/coupon","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"statisticsInterestCoupon","text":"优惠券列表","link":"/aaaaaaaaaa","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"couponManager","text":"活动管理"},
                    {"name":"lottery","text":"抽奖数据统计","link":"/activity-console/activity-manage/user-time-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"autumn","text":"活动列表","link":"/aaaaaaa","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"membership-manage",
                "header":{"text":"会员管理"},
                "sidebar":[
                    {"name":"membershipManager","text":"会员等级管理"},
                    {"name":"membershipQuery","text":"会员等级查询","link":"/membership-manage/membership-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"membershipGiveManager","text":"会员发放管理"},
                    {"name":"membershipGiveCreate", "text":"会员发放创建", "link":"/membership-manage/give/edit-view","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"membershipGiveList", "text":"会员发放管理", "link":"/membership-manage/give/list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"membershipPurchaseManager","text":"会员购买管理"},
                    {"name":"membershipPurchase","text":"会员购买","link":"/finance-manage/membership-purchase","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"point-manage",
                "header":{"text":"积分管理"},
                "sidebar":[
                    {"name":"pointManager","text":"用户积分管理"},
                    {"name":"userPointList","text":"用户积分查询","link":"/point-manage/user-point-list","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"pointManager","text":"积分商品管理"},
                    {"name":"createCouponExchange","text":"添加优惠券商品","link":"/point-manage/coupon-exchange","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"couponExchangeManage","text":"优惠券商品管理","link":"/point-manage/coupon-exchange-manage","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"createVIRTUALProduct","text":"添加虚拟商品","link":"/point-manage/create?type=VIRTUAL","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"productVIRTUALManage","text":"虚拟商品管理","link":"/point-manage/product-list?type=VIRTUAL","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"createPHYSICALProduct","text":"添加实物商品","link":"/point-manage/create?type=PHYSICAL","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"productPHYSICALManage","text":"实物商品管理","link":"/point-manage/product-list?type=PHYSICAL","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"service-manage",
                "header":{"text":"客服中心"},
                "sidebar":[
                    {"name":"infoSerarch","text":"信息查询"},
                    {"name":"userSearchMan","text":"用户查询","link":"/user-manage/users-search","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userFeedback","text":"用户反馈"},
                    {"name":"feedbackMan","text":"意见反馈","link":"/announce-manage/feedback","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userFeedback","text":"用户服务"},
                    {"name":"bookingLoanMan","text":"预约投资管理","link":"/booking-loan-manage/list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"bookingLoanMan","text":"IOS邀请好友管理","link":"/aaaaaaa","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"platform-loan-list","text":"借款申请信息","link":"/loan-application/list","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"statistic",
                "header":{"text":"平台数据","link":"/statistic"},
                "sidebar":[
                    {"name":"userBaseDate","text":"用户基础数据"},
                    {"name":"userAge","text":"用户年龄分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userDate","text":"用户注册时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userRechargeWithdrawDate","text":"用户充值提现数据"},
                    {"name":"userRecharge","text":"用户充值时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userWithdraw","text":"用户提现时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"withdrawUserCount","text":"提现人数分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userInvestDate","text":"用户投资数据"},
                    {"name":"userInvestAmount","text":"用户投资金额时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userInvestCount","text":"用户投资次数时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userInvestViscosity","text":"用户续投情况","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"loanDate","text":"标的数据"},
                    {"name":"loanAmount","text":"标的资金情况","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"loanRaisingTimeCosting","text":"标的满标周期情况","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"platformDate","text":"平台业务数据"},
                    {"name":"platformRepay","text":"平台待收","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"platformOut","text":"平台支出","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"platformDate","text":"平台功能数据"},
                    {"name":"anxinUserStatus","text":"安心签用户状态统计","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"}
                ]
            },
            {
                "name":"security",
                "header":{"text":"安全管理"},
                "sidebar":[
                    {"name":"logManager","text":"日志管理"},
                    {"name":"loginLog","text":"登录日志","link":"/security-log/login-log","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"auditLog","text":"管理日志","link":"/security-log/audit-log","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userOpLog","text":"用户行为日志","link":"/security-log/user-op-log","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
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
                                            <a href="${item.link}">&nbsp;&nbsp;&nbsp;&nbsp;${item.text}</a>
                                        </li>
                                    </@role>
                                <#else>
                                    <li>
                                        &nbsp;&nbsp;<b>${item.text}</b>
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
