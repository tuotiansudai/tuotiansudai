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

<#macro main pageCss pageJavascript="" activeNav="" activeLeftNav="" title="拓天速贷" keywords="" description="" site='main'>
    <#local mainMenus=[
    {"title":"首页", "url":"/","category":"16顶部导航","navigation":"true"},
    {"title":"我要投资", "url":"/loan-list","category":"17顶部导航","navigation":"true","leftNavs":[
        {"title":"直投项目", "url":"/loan-list"},
        {"title":"转让项目", "url":"/transfer-list"}
    ]},
    {"title":"我要借款", "url":"/loan-application","category":"19顶部导航","navigation":"true"},

    {"title":"我的账户", "url":"/account", "category":"18顶部导航","navigation":"true","leftNavs":[
        {"title":"账户总览", "url":"/account", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"我的投资", "url":"/investor/invest-list", "role":"'USER', 'INVESTOR'"},
        {"title":"债权转让", "url":"/transferrer/transfer-application-list/TRANSFERABLE", "role":"'USER', 'INVESTOR'"},
        {"title":"我的借款", "url":"/loaner/loan-list", "role":"'LOANER'"},
        {"title":"资金管理", "url":"/user-bill", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"消息中心", "url":"/message/user-messages", "role":"'USER'"},
        {"title":"个人资料", "url":"/personal-info", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"安心签", "url":"/anxinSign", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"推荐送现金", "url":"/referrer/refer-list", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"我的宝藏", "url":"/my-treasure", "role":"'USER', 'INVESTOR', 'LOANER'"}
    ]},
    {"title":"拓天问答", "url":"${askServer}/?group=HOT&index=1","category":"22顶部导航","navigation":"true"},
    {"title":"信息披露", "url":"/about/company","category":"20顶部导航", "navigation":"true","leftNavs":[
        {"title":"公司介绍", "url":"/about/company"},
        {"title":"团队介绍", "url":"/about/team"},
        {"title":"拓天公告", "url":"/about/notice"},
        {"title":"媒体报道", "url":"/about/media"},
        {"title":"服务费用", "url":"/about/service-fee"},
        {"title":"联系我们", "url":"/about/contact"},
        {"title":"运营数据", "url":"/about/operational"}
    ]},
    {"title":"帮助中心", "url":"/help/help-center","category":"21顶部导航", "navigation":"false","leftNavs":[
        {"title":"注册认证", "url":"/help/account"},
        {"title":"账户管理", "url":"/help/user"},
        {"title":"资金相关", "url":"/help/money"},
        {"title":"产品类型", "url":"/help/product"},
        {"title":"其他问题", "url":"/help/other"}
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
    <meta charset="UTF-8" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>${title}</title>
    <meta name="keywords" content="${keywords}">
    <meta name="description" content="${description}">
    <#if responsive??>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    </#if>
    <meta name="_csrf" content="${(_csrf.token)!}"/>
    <meta name="_csrf_header" content="${(_csrf.headerName)!}"/>
    <meta name="360-site-verification" content="8f78c77592779bad6fb5acc422271b6f" />
    <link href="${staticServer}/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}${css.global}" charset="utf-8" />
    <#if pageCss?? && pageCss != "">
    <link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}${pageCss}" charset="utf-8" />
    </#if>
    <!--[if lte IE 8]>
        <link rel="stylesheet" href="${staticServer}${cssPath}ie_hack_grid.css">
    <![endif]-->
    <!-- -->
    <#include "../pageLayout/cnzz.ftl"/>
    <!-- growing io -->
    <#include "../pageLayout/growing-io.ftl"/>

</head>
<body>

<#if !isAppSource>
    <#include "../pageLayout/header.ftl"/>

    <#switch site>
        <#case "membership">
            <#include "../pageLayout/top-membership-menus.ftl"/>
            <#break>
        <#default>
            <#include "../pageLayout/top-menus.ftl"/>
    </#switch>

</#if>

<div class="main-frame full-screen clearfix">
    <#if !isAppSource>
        <#include "../pageLayout/left-menus.ftl"/>
    </#if>
    <#nested>
</div>

<#if !isAppSource>
    <#switch site>
        <#case "membership">
            <#include "../pageLayout/membership-footer.ftl"/>
            <#break>
        <#default>
            <#include "../pageLayout/footer.ftl" />
    </#switch>
</#if>

<script type="text/javascript" charset="utf-8">
    var staticServer = '${staticServer}';
    <@security.authorize access="isAuthenticated()">
    document.getElementById("logout-link").onclick = function () {
        document.getElementById("logout-form").submit();
    };
    </@security.authorize>

</script>
<script type="text/javascript" src="${staticServer}${jsPath}${js.global_page}"></script>
<script src="${staticServer}${jsPath}${js.config}" type="text/javascript" charset="utf-8"></script>

<#if pageJavascript?? && pageJavascript?length gt 0>
<script src="${staticServer}/js/libs/require-2.1.20.min.js" type="text/javascript" charset="utf-8" defer="defer" async="async"
        data-main="${staticServer}${jsPath}${pageJavascript}">

</script>
</#if>

<#include "../pageLayout/statistic.ftl" />
</body>
</html>
</#macro>