<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#assign applicationContext=requestContext.getContextPath() />

<#macro role hasRole>
    <@security.authorize access="hasAnyAuthority(${hasRole})">
        <#nested>
    </@security.authorize>
</#macro>

<#macro isAnonymous>
    <@security.authorize access="!isAuthenticated()">
        <#nested>
    </@security.authorize>
</#macro>

<#macro isNotAnonymous>
    <@security.authorize access="isAuthenticated()">
        <#nested>
    </@security.authorize>
</#macro>

<#macro main pageCss pageJavascript="" activeNav="" staticServer="${staticServer}" title="拓天速贷" keywords="" activeLeftNav=""  description="">
    <#local mainMenus=[
    {"title":"首页", "url":"${webServer}","category":"16顶部导航","navigation":"true"},
    {"title":"我要投资", "url":"${webServer}/loan-list","category":"17顶部导航","navigation":"true","leftNavs":[
        {"title":"直投项目", "url":"${webServer}/loan-list"},
        {"title":"转让项目", "url":"${webServer}/transfer-list"}
    ]},
    {"title":"我要借款", "url":"${webServer}/loan-application","category":"19顶部导航","navigation":"true"},

    {"title":"我的账户", "url":"${webServer}/account", "category":"18顶部导航","navigation":"true","leftNavs":[
        {"title":"账户总览", "url":"${webServer}/account", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"我的投资", "url":"${webServer}/investor/invest-list", "role":"'USER', 'INVESTOR'"},
        {"title":"债权转让", "url":"${webServer}/transferrer/transfer-application-list/TRANSFERABLE", "role":"'USER', 'INVESTOR'"},
        {"title":"我的借款", "url":"${webServer}/loaner/loan-list", "role":"'LOANER'"},
        {"title":"资金管理", "url":"${webServer}/user-bill", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"我的财豆", "url":"${webServer}/point", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"个人资料", "url":"${webServer}/personal-info", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"自动投标", "url":"${webServer}/auto-invest", "role":"'USER', 'INVESTOR'"},
        {"title":"推荐管理", "url":"${webServer}/referrer/refer-list", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"我的宝藏", "url":"${webServer}/my-treasure", "role":"'USER', 'INVESTOR', 'LOANER'"}
    ]},
    {"title":"拓天问答", "url":"${askServer}","category":"","navigation":"true"},
    {"title":"信息披露", "url":"${webServer}/about/company","category":"20顶部导航", "navigation":"true","leftNavs":[
        {"title":"公司介绍", "url":"${webServer}/about/company"},
        {"title":"团队介绍", "url":"${webServer}/about/team"},
        {"title":"拓天公告", "url":"${webServer}/about/notice"},
        {"title":"媒体报道", "url":"${webServer}/about/media"},
        {"title":"推荐奖励", "url":"${webServer}/about/refer-reward"},
        {"title":"服务费用", "url":"${webServer}/about/service-fee"},
        {"title":"联系我们", "url":"${webServer}/about/contact"},
        {"title":"运营数据", "url":"${webServer}/about/operational"}
    ]},
    {"title":"帮助中心", "url":"${webServer}/help/help-center","category":"21顶部导航", "navigation":"false","leftNavs":[
        {"title":"注册认证", "url":"${webServer}/help/account"},
        {"title":"账户管理", "url":"${webServer}/help/user"},
        {"title":"资金相关", "url":"${webServer}/help/money"},
        {"title":"产品类型", "url":"${webServer}/help/product"},
        {"title":"其他问题", "url":"/${webServer}help/other"}
    ]}
    ]/>

    <#local membershipMenus=[
        {"title":"我的会员", "url":"/membership","category":""},
        {"title":"成长体系", "url":"/membership/structure","category":""},
        {"title":"会员特权", "url":"/membership/privilege","category":""},
        {"title":"积分商城", "url":"/point-shop","category":""},
        {"title":"积分任务", "url":"/point-shop/task","category":""}
    ]/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="keywords" content="${keywords}"/>
    <meta name="description" content="${description}"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="_csrf" content="${(_csrf.token)!}"/>
    <meta name="_csrf_header" content="${(_csrf.headerName)!}"/>
    <title>${title}</title>
    <link href="${staticServer}/images/favicon.ico" rel="shortcut icon" type="image/x-icon"/>
    <#if pageCss?? && pageCss != "">
        <link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}${pageCss}" charset="utf-8"/>
    </#if>

    <#include "../cnzz.ftl"/>
    <!-- growing io -->
    <#include "../growing-io.ftl"/>
</head>
<body>
    <#include "../pageLayout/header.ftl"/>

<#--top menus-->
    <#include "../pageLayout/top-menu.ftl"/>
<#--top menus-->

<#--ad image-->
<div class="banner-box page-width">
    <a href="https://tuotiansudai.com/activity/landing-page" target="_blank"></a>
</div>
<#--ad image-->

<div class="main-frame full-screen clearfix">
<#--banner-->
    <div class="borderBox tc mobile-menu">
        <a href="/question" class="btn-main want-question">我要提问</a>
        <a href="/question/my-questions" class="btn-main my-question">我的提问</a>
        <a href="/answer/my-answers" class="btn-main my-answer">我的回答</a>
    </div>
    <div class="download-mobile">
        <a href="https://tuotiansudai.com/app/download" target="_blank"></a>
    </div>
<#--banner-->

    <#--hot question-->
    <div class="hot-question-category" >
        <div class="m-title">热门问题分类  <i></i></div>
        <ul class="qa-list clearfix" style="display: none">
            <#list tags as tagItem>
                <li>
                    <a href="/question/category?tag=${tagItem.name()}" <#if tag?? && tagItem == tag>class="active"</#if>>${tagItem.description}</a>
                </li>
            </#list>
        </ul>
    </div>
    <div class="question-container answer-container">
        <#nested>

    <#--left content-->
        <div class="aside-frame fr">
            <#include "../user.ftl"/>
            <#include "../tags.ftl"/>
            <a href="https://tuotiansudai.com/activity/app-download" target="_blank" class="margin-top-10 ad-welfare" ></a>
        </div>
    <#--left content-->
    </div>
</div>

    <#include "../pageLayout/footer.ftl" />

<script src="${staticServer}${jsPath}${pageJavascript}" type="text/javascript"></script>

</body>
</html>
</#macro>