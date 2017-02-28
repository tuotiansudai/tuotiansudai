<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#assign applicationContext=requestContext.getContextPath() />

<#macro role hasRole>
    <@security.authorize access="hasAnyAuthority(${hasRole})">
        <#nested>
    </@security.authorize>
</#macro>

<#macro noRole hasNoRole>
    <@security.authorize access="!hasAuthority(${hasNoRole})">
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

<#macro main pageCss pageJavascript="" activeNav="" activeLeftNav="" title="拓天速贷" keywords="" description="">
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
        {"title":"个人资料", "url":"/personal-info", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"推荐管理", "url":"/referrer/refer-list", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"我的宝藏", "url":"/my-treasure", "role":"'USER', 'INVESTOR', 'LOANER'"}
    ]},
    {"title":"拓天问答", "url":"${askServer}/?group=HOT&index=1","category":"22顶部导航","navigation":"true"},
    {"title":"信息披露", "url":"/about/company","category":"20顶部导航", "navigation":"true","leftNavs":[
        {"title":"公司介绍", "url":"/about/company"},
        {"title":"团队介绍", "url":"/about/team"},
        {"title":"拓天公告", "url":"/about/notice"},
        {"title":"媒体报道", "url":"/about/media"},
        {"title":"推荐奖励", "url":"/about/refer-reward"},
        {"title":"服务费用", "url":"/about/service-fee"},
        {"title":"常见问题", "url":"/about/qa"},
        {"title":"联系我们", "url":"/about/contact"},
        {"title":"运营数据", "url":"/about/operational"}
    ]}]/>

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
    <#if !isAppSource>
        <meta name = "format-detection" content = "telephone=no">
    </#if>
    <link href="${staticServer}/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${css.global}" charset="utf-8" />
    <#if pageCss?? && pageCss != "">
    <link rel="stylesheet" type="text/css" href="${pageCss}" charset="utf-8" />
    </#if>

    <#include "../cnzz.ftl"/>
    <!-- growing io -->
    <#include "../growing-io.ftl"/>
</head>
<body>

<#if !isAppSource>
    <#include "../pageLayout/header.ftl"/>
    <#include "../pageLayout/top-menus.ftl"/>
</#if>
<div class="main-frame full-screen clearfix">
    <#if !isAppSource>
        <#include "../pageLayout/left-menus.ftl"/>
    </#if>
    <#nested>
</div>

<#if !isAppSource>
    <#include "../pageLayout/footer.ftl" />
</#if>

<script type="text/javascript" charset="utf-8">
    var staticServer = '${staticServer}';
    <@security.authorize access="isAuthenticated()">
    document.getElementById("logout-link").onclick=function (event) {
        event.preventDefault();
        document.getElementById("logout-form").submit();
    };
    </@security.authorize>

</script>
<script src="${js.global_page}" type="text/javascript"  charset="utf-8"></script>
<script src="${js.config}" type="text/javascript" charset="utf-8"></script>

<#if pageJavascript?? && pageJavascript?length gt 0>
<script src="${staticServer}/activity/js/libs/require-2.1.20.min.js" type="text/javascript" charset="utf-8" defer="defer" async="async"
        data-main="${pageJavascript}">

</script>
</#if>

<#include "../pageLayout/statistic.ftl" />
</body>
</html>
</#macro>