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
                {"name":"","class":"sub-title-1","text":"我的任务","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE', 'ASK_ADMIN'"},
                    {"name":"myTasks","text":"我的任务","link":"/","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE', 'ASK_ADMIN'"}
            ]
        },
        {
            "name":"project-manage",
            "header":{"text":"项目管理"},
            "sidebar":
            [
                {"name":"","class":"sub-title-1","text":"直投项目管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"ALL","text":"所有的借款","link":"/project-manage/loan-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"start","text":"发起借款","link":"/project-manage/loan","role":"'ADMIN','OPERATOR'"},
                    {"name":"repaymentInfoList","text":"项目还款明细表","link":"/project-manage/loan-repay","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"转让项目管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"transfer-list","text":"所有的转让项目","link":"/transfer-manage/transfer-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"transfer-rule","text":"转让规则","link":"/transfer-manage/transfer-rule","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"","class":"sub-title-1","text":"项目合同","link":"","role":"'ADMIN'"},
                    {"name":"contract","text":"CFCA创建合同","link":"/project-manage/contract","role":"'ADMIN'"}
            ]
        },
        {
            "name":"user-manage",
            "header":{"text":"用户管理"},
            "sidebar":[
                {"name":"","class":"sub-title-1","text":"用户管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userMan","text":"用户管理","link":"/user-manage/users","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"referMan","text":"推荐人管理","link":"/user-manage/referrer","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userRemain","text":"用户留存管理","link":"/user-manage/remain-users","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userMicroModel","text":"用户微模型","link":"/user-manage/user-micro-model","role":"'ADMIN','OPERATOR_ADMIN','OPERATOR','DATA'"}
            ]
        },
        {
            "name":"finance-manage",
            "header":{"text":"财务管理"},
            "sidebar":[
                {"name":"","class":"sub-title-1","text":"投资管理","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userInvest","text":"用户投资管理","link":"/finance-manage/invests","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"financeReport","text":"债权财务数据","link":"/finance-manage/financeReport","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"debtRepay","text":"债权还款计划","link":"/finance-manage/debt-repayment-plan","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"用户账户管理","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userBalance","text":"用户余额查询","link":"/finance-manage/account-balance?balanceMin=50","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userFund","text":"用户资金查询","link":"/finance-manage/user-funds","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"recharge","text":"充值记录","link":"/finance-manage/recharge","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"withdraw","text":"提现记录","link":"/finance-manage/withdraw","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"adminIntervention","text":"修改账户余额","link":"/finance-manage/admin-intervention","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"系统账户管理","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"systemBill","text":"系统账户查询","link":"/finance-manage/system-bill","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"systemRecharge","text":"平台账户充值","link":"/finance-manage/system-recharge","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"联动优势账户管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"realTimeStatus","text":"联动优势余额查询","link":"/finance-manage/real-time-status","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"transferBill","text":"用户联动优势交易流水","link":"/finance-manage/transfer-bill","role":"'ADMIN'"},
                    {"name":"bankMange","text":"银行卡管理","link":"/finance-manage/bank-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"信用贷标的账户管理","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"creditLoanBill","text":"信用贷标的账户查询","link":"/finance-manage/credit-loan-bill","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"creditLoanRecharge","text":"信用贷标的账户充值","link":"/finance-manage/credit-loan-recharge","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
            ]
        },
        {
            "name":"content-manage",
            "header":{"text":"内容管理"},
            "sidebar":[
                {"name":"","class":"sub-title-1","text":"消息管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"createManualMessage","text":"创建手动站内信","link":"/message-manage/manual-message","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"messageManage","text":"消息管理","link":"/message-manage/manual-message-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"announceMan","text":"公告管理","link":"/announce-manage/announce","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"平台内容管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"bannerMan","text":"banner管理","link":"/banner-manage/list","role":"'ADMIN','OPERATOR_ADMIN'"},
                    {"name":"activityCenter","text":"活动中心管理","link":"/activity-manage/activity-center-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE'"},
                {"name":"","class":"sub-title-1","text":"媒体内容管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"linkExchangeMan","text":"友链管理","link":"/link-exchange-manage/link-exchange","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"articleMan","text":"拓天资讯","link":"/announce-manage/article/list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','EDITOR'"}
            ]
        },
        {
            "name":"security",
            "header":{"text":"安全管理"},
            "sidebar":[
                {"name":"","class":"sub-title-1","text":"日志管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"loginLog","text":"登录日志","link":"/security-log/login-log","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"auditLog","text":"管理日志","link":"/security-log/audit-log","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userOpLog","text":"用户行为日志","link":"/security-log/user-op-log","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"clearCache","text":"清除DB Cache","link":"/cache-manage/clear-db-cache","role":"'ADMIN'"},
                    {"name":"anxinSwitch","text":"安心签开关","link":"/anxin-sign/switch","role":"'ADMIN'"}
            ]
        },
        {
            "name":"activity-manage",
            "header":{"text":"活动管理"},
            "sidebar":[
                {"name":"","class":"sub-title-1","text":"优惠券管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"createCoupon","text":"创建优惠券","link":"/activity-manage/red-envelope","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"statisticsCoupon","text":"优惠券列表","link":"/activity-manage/coupons-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"活动列表","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"lottery","text":"抽奖数据统计","link":"/activity-console/activity-manage/user-time-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"lottery","text":"投资金额统计","link":"/activity-console/activity-manage/invest-annualized-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"heroRanking","text":"周年庆管理","link":"/activity-console/activity-manage/hero-ranking","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"newmanTyrant","text":"新贵富豪争霸赛活动管理","link":"/activity-console/activity-manage/newman-tyrant","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"travelLuxuryActivity","text":"旅游+奢侈品活动管理","link":"/activity-console/activity-manage/travel/user-travel-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"autumn","text":"中秋活动导出","link":"/activity-console/activity-manage/autumn-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"iphone7Lottery","text":"iphone7活动","link":"/activity-console/activity-manage/iphone7-lottery","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"annual","text":"元旦活动", "link":"/activity-console/activity-manage/annual-list", "role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"headlinesToday","text":"今日头条拉新抽奖活动","link":"/activity-console/activity-manage/headlines-today-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"notWork","text":"不上班活动", "link":"/activity-console/activity-manage/not-work-list", "role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"investAchievement","text":"投资称号管理","link":"/activity-manage/invest-achievement","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"promotion","text":"APP弹窗推送管理","link":"/activity-console/activity-manage/promotion/promotion-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"mothersDay","text":"母亲节活动","link":"/activity-console/activity-manage/mothers-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"dragonBoat","text":"端午节活动","link":"/activity-console/activity-manage/dragon-boat","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"exerciseVSWork","text":"运动达人VS职场骄子","link":"/activity-console/activity-manage/exercise-work-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"houseDecorate","text":"家装节活动 ","link":"/activity-console/activity-manage/house-decorate-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"schoolSeason","text":"开学季活动 ","link":"/activity-console/activity-manage/school-season-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"NationalMidAutumn","text":"国庆节活动 ","link":"/activity-console/activity-manage/national-mid-autumn-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"iphoneX","text":"iPhoneX活动 ","link":"/activity-console/activity-manage/iphonex-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"zeroShopping","text":"0元购活动管理 ","link":"/activity-console/activity-manage/zero-shopping/user-prize-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"cashSnowball","text":"现金滚雪球活动管理 ","link":"/activity-console/activity-manage/cash-snowball-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"cashSnowball","text":"惊喜不重样加息不打烊活动管理 ","link":"/activity-console/activity-manage/start-work-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"cashSnowball","text":"返利加油站 ","link":"/activity-console/activity-manage/invest-reward-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"}
            ]
        },
        {
            "name":"membership-manage",
            "header":{"text":"会员管理"},
            "sidebar":[
                {"name":"","class":"sub-title-1","text":"会员等级管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"membershipQuery","text":"会员等级查询","link":"/membership-manage/membership-list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"会员发放管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"membershipGiveCreate", "text":"会员发放创建", "link":"/membership-manage/give/edit-view","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"membershipGiveList", "text":"会员发放管理", "link":"/membership-manage/give/list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"会员购买管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"membershipPurchase","text":"购买增值特权","link":"/membership-manage/membership-privilege-purchase","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"}
            ]
        },
        {
            "name":"statistic",
            "header":{"text":"平台数据","link":"/statistic"},
            "sidebar":[
                {"name":"","class":"sub-title-1","text":"用户基础数据","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userAge","text":"用户年龄分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userDate","text":"用户注册时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"用户充值提现数据","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userRecharge","text":"用户充值时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userWithdraw","text":"用户提现时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"withdrawUserCount","text":"提现人数分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"用户投资数据","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userInvestViscosity","text":"用户续投情况-投资标的数","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userInvestViscosity","text":"用户续投情况-投资次数","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userInvestAmount","text":"用户投资金额时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userInvestCount","text":"用户投资次数时间分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"标的数据","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"loanAmount","text":"标的资金分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"loanRaisingTimeCosting","text":"标的满标周期分布","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"平台业务数据","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"platformRepay","text":"平台待收","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"platformOut","text":"平台支出","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"平台功能数据","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"anxinUserStatus","text":"安心签用户状态统计","link":"/statistic","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"}
            ]
        },
        {
            "name":"point-manage",
            "header":{"text":"积分管理"},
            "sidebar":[
                {"name":"","class":"sub-title-1","text":"用户积分管理","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userPointList","text":"用户积分查询","link":"/point-manage/user-point-list","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"积分商品管理","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"productCreate","text":"添加商品","link":"/point-manage/coupon-exchange","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"productManage","text":"商品管理","link":"/point-manage/coupon-exchange-manage","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"}
            ]
        },
        {
            "name":"ask-manage",
            "header":{"text":"问答管理"},
            "sidebar":[
                {"name":"questionManage","text":"提问管理","link":"/ask-manage/questions","role":"'ADMIN', 'ASK_ADMIN'"},
                {"name":"answerManage","text":"回答管理","link":"/ask-manage/answers","role":"'ADMIN', 'ASK_ADMIN'"},
                {"name":"embodyQuestionManage","text":"收录文章管理","link":"/ask-manage/embody-questions","role":"'ADMIN', 'ASK_ADMIN'"}
            ]
        },
        {
            "name":"customer-center",
            "header":{"text":"客服中心"},
            "sidebar":[
                {"name":"","class":"sub-title-1","text":"信息查询","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"userSearchMan","text":"用户查询","link":"/user-manage/users-search","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"用户反馈","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"feedbackMan","text":"意见反馈","link":"/announce-manage/feedback","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                {"name":"","class":"sub-title-1","text":"用户服务","link":"","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"bookingLoanMan","text":"预约投资管理","link":"/booking-loan-manage/list","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"prepareUsers","text":"IOS邀请好友管理","link":"/activity-manage/prepare-users","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE'"},
                    {"name":"platform-loan-list","text":"借款申请信息","link":"/loan-application/list","role":"'ADMIN','CUSTOMER_SERVICE','OPERATOR','OPERATOR_ADMIN'"},
                    {"name":"bindCard","text":"换卡管理","link":"/bank-card-manager/bind-card","role":"'ADMIN','OPERATOR_ADMIN','CUSTOMER_SERVICE'"}
            ]
        },
        {
            "name":"experience-manage",
            "header":{"text":"体验金管理"},
            "sidebar":[
                {"name":"","class":"sub-title-1","text":"体验金管理","link":"","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE','DATA'"},
                    {"name":"experienceBalance","text":"用户体验金余额","link":"/experience-manage/balance","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE','DATA'"},
                    {"name":"experienceRepay","text":"体验金还款明细","link":"/experience-manage/repay-detail","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE','DATA'"},
                    {"name":"experienceBill","text":"体验金流水明细","link":"/experience-manage/experience-bill","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE','DATA'"},
                    {"name":"experienceRecord","text":"体验金投资记录","link":"/experience-manage/experience-record","role":"'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE','DATA'"}
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
    <link href="/activity-console/images/favicon.ico" rel="shortcut icon" type="image/x-icon"/>
    <!-- link bootstrap css -->
    <link href="/activity-console/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/activity-console/style/libs/bootstrap-datepicker.css" rel="stylesheet">
    <link href="/activity-console/style/libs/bootstrap/bootstrap-datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="/activity-console/style/libs/bootstrap-select.css" rel="stylesheet" type="text/css" charset="utf-8"/>
    <link href="/activity-console/style/libs/jquery-ui/jquery-ui-1.11.4.min.css" rel="stylesheet" charset="utf-8"/>
    <link href="/activity-console/style/index.css" rel="stylesheet">
    <link href="/activity-console/style/libs/jquery.pageflip.css" rel="stylesheet">
    <link href="/activity-console/style/libs/fileinput.css" rel="stylesheet"/><!--上传图片插件-->
    <#if pageCss?? && pageCss != "">
        <link rel="stylesheet" type="text/css" href="${applicationContext}/activity-console/style/dest/${pageCss}" charset="utf-8"/>
    </#if>

    <!-- link bootstrap js -->
    <#if pageJavascript?? && pageJavascript != "">
        <script src="/activity-console/js/libs/config.js"></script>
        <script src="/activity-console/js/libs/require-2.1.20.min.js" defer="defer" async="async"
                data-main="${applicationContext}/activity-console/js/${pageJavascript}"></script>
    </#if>
</head>

<body>
<!-- header begin -->
<header class="navbar" id="top" role="banner">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="/" class="navbar-brand">
                <img src="/activity-console/images/logo.png" alt="" />
            </a>
        </div>
        <div class="collapse navbar-collapse">
            <#--<p class="navbar-text navbar-right"><a id="logout-link" href="/login/sign-out">注销</a>【<@global.security.authentication property="principal.username" />】</p>-->
            <form id="logout-form" action="/login/sign-out" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
    </div>
    <nav id="bs-navbar" class="collapse navbar-collapse top-menu">
        <div class="container-fluid">
            <ul class="nav navbar-nav">
                <#list menus as menu>
                    <#list menu.sidebar as item>
                        <#if item.role?? && item.name?has_content>
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
                                        <li class="${item.class!} <#if item.name == sideLab>active</#if>">
                                            <#if item.link?has_content><a href="${item.link}">${item.text}</a>
                                            <#else>${item.text}</#if>
                                        </li>
                                    </@role>
                                <#else>
                                    <li class="${item.class!} <#if item.name == sideLab>active</#if>">
                                        <#if item.link?has_content><a href="${item.link}">${item.text}</a>
                                        <#else>${item.text}</#if>
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

</body>
</html>
</#macro>
