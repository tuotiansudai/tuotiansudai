<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<#macro main pageCss pageJavascript activeNav="" activeLeftNav="" title="拓天速贷">
    <#local menus=[
    {"title":"首页", "url":"/"},
    {"title":"我要投资", "url":"/"},
    {"title":"我的账户", "url":"/account", "leftNavs":[
    {"title":"账户总览", "url":"/account", "role":"'INVESTOR', 'LOANER'"},
    {"title":"我的投资", "url":"/investor/invest-list", "role":"'INVESTOR'"},
    {"title":"我的借款", "url":"/loaner/loan-list", "role":"'LOANER'"},
    {"title":"资金管理", "url":"/user-bill", "role":"'INVESTOR', 'LOANER'"},
    {"title":"个人资料", "url":"/personal-info", "role":"'INVESTOR', 'LOANER'"},
    {"title":"自动投标", "url":"/", "role":"'INVESTOR'"},
    {"title":"推荐管理", "url":"/", "role":"'INVESTOR', 'LOANER'"}]},
    {"title":"推荐奖励", "url":"/"},
    {"title":"关于我们", "url":"/about/company", "leftNavs":[
    {"title":"公司介绍", "url":"/about/company"},
    {"title":"高管团队", "url":"/about/team"},
    {"title":"联系我们", "url":"/about/contact"},
    {"title":"拓天公告", "url":"/about/notice"}]}]/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="${staticServer}/style/dest/${css.global}">
    <#if pageCss?? && pageCss != "">
        <link rel="stylesheet" type="text/css" href="${staticServer}/style/dest/${pageCss}">
    </#if>
</head>
<body>
    <#include "../header.ftl" />
<div class="nav-container">
    <div class="nav">
        <a href="${requestContext.getContextPath()}" class="logo"></a>
        <#if activeNav??>
            <ul>
                <#list menus as menu>
                    <li><a <#if menu.title==activeNav>class="active"</#if> href="${menu.url}">${menu.title}</a></li>
                </#list>
            </ul>
        </#if>

    </div>
</div>
<div class="main-frame">
    <#list menus as menu>
        <#if activeNav?? && activeNav==menu.title && menu.leftNavs??>
            <ul class="left-nav">
                <#list menu.leftNavs as leftNav>
                    <#if leftNav.role??>
                        <@role hasRole=leftNav.role>
                            <li><a <#if leftNav.title==activeLeftNav>class="active"</#if> href="${leftNav.url}">${leftNav.title}</a></li>
                        </@role>
                    <#else>
                        <li><a <#if leftNav.title==activeLeftNav>class="active"</#if> href="${leftNav.url}">${leftNav.title}</a></li>
                    </#if>

                </#list>
            </ul>
        </#if>
    </#list>
    <#nested>
</div>
    <#include "../footer.ftl" />
<script src="${staticServer}/js/dest/${js.config}"></script>
<script src="${staticServer}/js/libs/require-2.1.20.min.js" defer="defer" async="async"
    <#if pageJavascript??>
        data-main="${staticServer}/js/dest/${pageJavascript}"
    </#if>>
</script>
</body>
</html>
</#macro>

<#macro head title pageCss>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="${staticServer}/style/dest/${css.global}">
    <link rel="stylesheet" type="text/css" href="${staticServer}/style/dest/${pageCss}">
</head>
</#macro>

<#macro javascript pageJavascript>
<script src="${staticServer}/js/dest/${js.config}"></script>
<script src="${staticServer}/js/libs/require-2.1.20.min.js"
        defer="defer"
        async="async"
        data-main="${staticServer}/js/dest/${pageJavascript}"></script>
</#macro>

<#macro role hasRole>
    <@security.authorize access="hasAnyAuthority(${hasRole})">
        <#nested>
    </@security.authorize>
</#macro>
