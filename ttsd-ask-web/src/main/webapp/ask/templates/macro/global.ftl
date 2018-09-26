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

<#macro main pageCss pageJavascript="" activeNav="拓天问答" commonStaticServer="${commonStaticServer}" title="拓天速贷" keywords="" activeLeftNav=""  description="">
    <#local mainMenus=[
    {"title":"首页", "url":"${webServer}","category":"16顶部导航","navigation":"true"},
    {"title":"我要出借", "url":"${webServer}/loan-list","category":"17顶部导航","navigation":"true","leftNavs":[
        {"title":"直投项目", "url":"${webServer}/loan-list"},
        {"title":"转让项目", "url":"${webServer}/transfer-list"}
    ]},
    {"title":"我要借款", "url":"${webServer}/loan-application","category":"19顶部导航","navigation":"true"},

    {"title":"我的账户", "url":"${webServer}/account", "category":"18顶部导航","navigation":"true","leftNavs":[
        {"title":"账户总览", "url":"${webServer}/account", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"我的出借", "url":"${webServer}/investor/invest-list", "role":"'USER', 'INVESTOR'"},
        {"title":"债权转让", "url":"${webServer}/transferrer/transfer-application-list/TRANSFERABLE", "role":"'USER', 'INVESTOR'"},
        {"title":"我的借款", "url":"${webServer}/loaner/loan-list", "role":"'LOANER'"},
        {"title":"资金管理", "url":"${webServer}/user-bill", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"我的财豆", "url":"${webServer}/point", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"个人资料", "url":"${webServer}/personal-info", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"自动投标", "url":"${webServer}/auto-invest", "role":"'USER', 'INVESTOR'"},
        {"title":"推荐管理", "url":"${webServer}/referrer/refer-list", "role":"'USER', 'INVESTOR', 'LOANER'"},
        {"title":"我的宝藏", "url":"${webServer}/my-treasure", "role":"'USER', 'INVESTOR', 'LOANER'"}
    ]},
    {"title":"拓天问答", "url":"${askServer}","category":"22顶部导航","navigation":"true"},
    {"title":"信息披露", "url":"${webServer}/about/company","category":"20顶部导航", "navigation":"true","leftNavs":[
        {"title":"公司介绍", "url":"${webServer}/about/company"},
        {"title":"团队介绍", "url":"${webServer}/about/team"},
        {"title":"拓天公告", "url":"${webServer}/about/notice"},
        {"title":"媒体报道", "url":"${webServer}/about/media"},
        {"title":"网贷知识", "url":"/about/knowledge"},
        {"title":"审计报告", "url":"/about/audit-report"},
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
    <title>${title}</title>
    <meta name="keywords" content="${keywords}"/>
    <meta name="description" content="${description}"/>
    <meta name="format-detection" content="telephone=no, email=no"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="_csrf" content="${(_csrf.token)!}"/>
    <meta name="_csrf_header" content="${(_csrf.headerName)!}"/>
    <meta name="sogou_site_verification" content="VU3DEpC4Pk"/>
    <meta name="baidu-site-verification" content="xE3BgFFio5" />
    <meta name="360-site-verification" content="1ac0e9c5a7659c9cce73ecac8f630a0e" />
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-115616275-1"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', 'UA-115616275-1');
    </script>
    <link href="${commonStaticServer}/images/favicon.ico" rel="shortcut icon" type="image/x-icon"/>
    <link rel="stylesheet" type="text/css" href="${css.globalFun_page!}" charset="utf-8"/>
    <#if pageCss?? && pageCss != "">
        <link rel="stylesheet" type="text/css" href="${pageCss}" charset="utf-8"/>
    </#if>

    <!-- growing io -->
    <#include "../growing-io.ftl"/>
    <#include "../baidu-webmaster.ftl"/>
</head>
<body>
    <#include "../pageLayout/header.ftl"/>

<#--top menus-->
    <#include "../pageLayout/top-menu.ftl"/>
<#--top menus-->

    <#if !errorPage??>
<#--ad image-->
<div class="banner-box page-width">
    <a href="https://tuotiansudai.com/activity/landing-page" target="_blank"></a>
</div>
<#--ad image-->
    </#if>
<div class="main-frame full-screen clearfix main-frame-ask">

<#--banner-->
    <#if !errorPage??>
    <#include "../pageLayout/search-bar.ftl"/>
    <div class="download-mobile">
        <a href="https://tuotiansudai.com/app/download" target="_blank"></a>
    </div>
    </#if>
<#--banner-->

    <#--hot question-->
    <div class="hot-question-category" >
        <div class="m-title">热门问题分类  <i></i></div>
        <ul class="qa-list clearfix" style="display: none">
            <#list tags as tagItem>
                <li>
                    <a href="${applicationContext}/question/category/${tagItem.name()?lower_case}"
                       <#if tag?? && tagItem == tag>class="active"</#if>>${tagItem.description}</a>
                </li>
            </#list>
        </ul>
    </div>
    <div class="question-container answer-container">
        <#nested>
        <#if !errorPage??>
    <#--left content-->
        <div class="aside-frame fr" >
            <#include "../pageLayout/user.ftl"/>
            <#include "../pageLayout/tags.ftl"/>

            <a href="https://tuotiansudai.com/activity/landing-page" target="_blank" class="margin-top-10 ad-welfare" ></a>
        </div>
    <#--left content-->
        </#if>
    </div>
</div>

    <#include "../pageLayout/footer.ftl" />

<#--ask百度统计-->
<script>
    var _hmt = _hmt || [];
    (function() {
        var hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?058eb2e1517e1c4d99bc6ffe0b056cc1";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })();
</script>

<script>
    window.commonStaticServer='${commonStaticServer}';
    window.applicationContext='${applicationContext}';
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

</body>
</html>
</#macro>