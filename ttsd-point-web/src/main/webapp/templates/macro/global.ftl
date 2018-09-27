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
    {"title":"首页", "url":"/","category":"16顶部导航"},
    {"title":"我要投资", "url":"/loan-list","category":"17顶部导航","leftNavs":[
        {"title":"直投项目", "url":"/loan-list"},
        {"title":"转让项目", "url":"/transfer-list"}
    ]},
    {"title":"我的账户", "url":"/account", "category":"18顶部导航","leftNavs":[
        {"title":"账户总览", "url":"/account", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"我的投资", "url":"/investor/invest-list", "role":"'USER', 'INVESTOR'"},
        {"title":"债权转让", "url":"/transferrer/transfer-application-list/TRANSFERABLE", "role":"'USER', 'INVESTOR'"},
        {"title":"我的借款", "url":"/loaner/loan-list", "role":"'LOANER'"},
        {"title":"资金管理", "url":"/user-bill", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"消息中心", "url":"/message/user-messages", "role":"'USER'"},
        {"title":"我的积分", "url":"/point", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"个人资料", "url":"/personal-info", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"自动投标", "url":"/auto-invest", "role":"'USER', 'INVESTOR'"},
        {"title":"推荐管理", "url":"/referrer/refer-list", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"我的宝藏", "url":"/my-treasure", "role":"'USER', 'INVESTOR', 'LOANER'"}
    ]},
    {"title":"新手指引", "url":"/about/guide","category":"19顶部导航"},
    {"title":"信息纰漏", "url":"/about/company","category":"20顶部导航", "leftNavs":[
        {"title":"组织架构", "url":"/about/team"},
        {"title":"拓天公告", "url":"/about/notice"},
        {"title":"媒体报道", "url":"/about/media"},
        {"title":"网贷知识", "url":"/about/knowledge"},
        {"title":"合规报告", "url":"/about/audit-report"},
        {"title":"服务费用", "url":"/about/service-fee"},
        {"title":"常见问题", "url":"/about/qa"},
        {"title":"联系我们", "url":"/about/contact"},
        {"title":"运营数据", "url":"/about/operational"}
    ]
    }
    ]/>

    <#local membershipMenus=[
    {"title":"我的会员", "url":"${webServer}/membership","category":""},
    {"title":"成长体系", "url":"${webServer}/membership/structure","category":""},
    {"title":"会员特权", "url":"${webServer}/membership/privilege","category":""},
    {"title":"积分商城", "url":"/point-shop","category":""},
    {"title":"积分任务", "url":"/point-shop/task","category":""}
    ]/>
<#--${webServer}-->
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
    <link href="${commonStaticServer}/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${css.globalFun_page!}" charset="utf-8" />
    <#if pageCss?? && pageCss != "">
        <link rel="stylesheet" type="text/css" href="${pageCss}" charset="utf-8" />
    </#if>
    <!-- growing io -->
    <#include "../growing-io.ftl"/>
    <#include "../baidu-webmaster.ftl"/>
</head>
<body>

    <#if !isAppSource>
        <#include "../header.ftl"/>
        <#include "../top-point-menus.ftl"/>
    </#if>

<div class="main-frame full-screen clearfix">
    <#nested>
</div>

    <#if !isAppSource>
        <#include "../point-footer.ftl"/>
    </#if>

<script type="text/javascript" charset="utf-8">
    window.commonStaticServer='${commonStaticServer}';
</script>

<#if (js.jquerydll)??>
<script src="${js.jquerydll}" type="text/javascript" ></script>
</#if>
<#if (js.globalFun_page)??>
<script src="${js.globalFun_page!}" type="text/javascript" ></script>
</#if>
<#if pageJavascript??>
<script src="${pageJavascript}" type="text/javascript" id="currentScript"></script>
</#if>

<#include "../statistic.ftl" />
</body>
</html>
</#macro>