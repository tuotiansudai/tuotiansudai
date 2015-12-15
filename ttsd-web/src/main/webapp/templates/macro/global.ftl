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

<#macro main pageCss pageJavascript activeNav="" activeLeftNav="" title="拓天速贷">
    <#local menus=[
    {"title":"首页", "url":"/"},
    {"title":"我要投资", "url":"/loan-list"},
    {"title":"我的账户", "url":"/account", "leftNavs":[
    {"title":"账户总览", "url":"/account", "role":"'INVESTOR', 'LOANER'"},
    {"title":"我的投资", "url":"/investor/invest-list", "role":"'INVESTOR'"},
    {"title":"我的借款", "url":"/loaner/loan-list", "role":"'LOANER'"},
    {"title":"资金管理", "url":"/user-bill", "role":"'INVESTOR', 'LOANER'"},
    {"title":"个人资料", "url":"/personal-info", "role":"'INVESTOR', 'LOANER'"},
    {"title":"自动投标", "url":"/investor/auto-invest", "role":"'INVESTOR'"},
    {"title":"推荐管理", "url":"/referrer/refer-list", "role":"'INVESTOR', 'LOANER'"}]},
    {"title":"推荐奖励", "url":"/activity/refer-reward"},
    {"title":"关于我们", "url":"/about/company", "leftNavs":[
    {"title":"公司介绍", "url":"/about/company"},
    {"title":"团队介绍", "url":"/about/team"},
    {"title":"拓天公告", "url":"/about/notice"},
    {"title":"服务费用", "url":"/about/service-fee"},
    {"title":"联系我们", "url":"/about/contact"}
    ]}]/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <#if responsive??>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    </#if>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>${title}</title>
    <link href="${staticServer}/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${staticServer}/style/dest/${css.global}" charset="utf-8" />
    <#if pageCss?? && pageCss != "">
    <link rel="stylesheet" type="text/css" href="${staticServer}/style/dest/${pageCss}" charset="utf-8" />
    </#if>
</head>
<body>
<#include "../header.ftl"/>
<div class="nav-container">
    <div class="nav">
        <a href="${applicationContext}/" class="logo"></a> <i class="fa fa-navicon show-main-menu fr" id="showMainMenu"></i>
        <#if activeNav??>
            <ul id="TopMainMenuList">
                <#list menus as menu>
                    <li><a <#if menu.title==activeNav>class="active"</#if> href="${menu.url}">${menu.title}</a></li>
                </#list>
            </ul>
        </#if>
    </div>
</div>
<div class="main-frame full-screen">
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
<script type="text/javascript" charset="utf-8">
    var staticServer = '${staticServer}';
    <@security.authorize access="isAuthenticated()">
    document.getElementById("logout-link").addEventListener('click', function (event) {
        event.preventDefault();
        document.getElementById("logout-form").submit();
    });
    </@security.authorize>

    adjustMobileHideHack();
    function adjustMobileHideHack() {

        //this function will be remove when all pages are responsive
        var bodyDom=document.getElementsByTagName("body")[0],
            userAgent = navigator.userAgent.toLowerCase(),
            metaTags=document.getElementsByTagName('meta'),
            metaLen=metaTags.length,isResponse=false,isPC=false,i=0;
        if(userAgent.indexOf('android') > -1 || userAgent.indexOf('iphone') > -1 || userAgent.indexOf('ipad') > -1) {
            isPC=false;
        }
        else {
            isPC=true;
        }
        for(;i<metaLen;i++) {
            if(metaTags[i].getAttribute('name')=='viewport') {
                isResponse=true;
            }
        }
        bodyDom.className=(!isResponse&&!isPC)?'page-width':'';
    }

    window.$ = function(id) {
        return document.getElementById(id);
    };

    function phoneLoadFun() {

        window.$('closeDownloadBox').onclick=function(event) {
            event.stopPropagation();
            event.preventDefault();
            this.parentElement.style.display='none';
        };
        window.$('btnExperience').onclick=function(event) {
            event.stopPropagation();
            event.preventDefault();
            var userAgent = navigator.userAgent.toLowerCase();
            if (userAgent.indexOf('android') > -1) {
                location.href = "/app/tuotiansudai.apk";
            } else if (userAgent.indexOf('iphone') > -1 || userAgent.indexOf('ipad') > -1) {
                location.href = "http://itunes.apple.com/us/app/id1039233966";
            }
        };

        window.$('showMainMenu').onclick=function(event) {
            event.stopPropagation();
            event.preventDefault();
            this.nextElementSibling.style.display='block';

        };

    }
    var imgDom=window.$('iphone-app-img'),
        TopMainMenuList=window.$('TopMainMenuList');

    window.$('iphone-app-pop').onclick=function(event) {
        event.stopPropagation();
        event.preventDefault();

        if(imgDom.style.display == "block") {
            imgDom.style.display='none';
        }
        else {
            imgDom.style.display='block';
        }
    };

    document.getElementsByTagName("body")[0].onclick=function(event) {
        var userAgent = navigator.userAgent.toLowerCase(),
            target=event.srcElement || event.target;
        if(target.tagName=='LI' ) {
            return;
        }
        imgDom.style.display='none';
        if(userAgent.indexOf('android') > -1 || userAgent.indexOf('iphone') > -1 || userAgent.indexOf('ipad') > -1) {

            //判断是否为viewport
            var metaTags=document.getElementsByTagName('meta'),
                    metaLen=metaTags.length,i=0;
            for(;i<metaLen;i++) {
                if(metaTags[i].getAttribute('name')=='viewport') {
                    TopMainMenuList.style.display='none';
                }
            }
        }

    };

    phoneLoadFun();

</script>
<script src="${staticServer}/js/dest/${js.config}" type="text/javascript" charset="utf-8"></script>
<#if pageJavascript??>
<script src="${staticServer}/js/libs/require-2.1.20.min.js" type="text/javascript" charset="utf-8" defer="defer" async="async"
        <#--data-main="${staticServer}/js/dest/${pageJavascript}">-->
    data-main="${staticServer}/js/register_user.js">
</script>
</#if>

<#include "../statistic.ftl" />
</body>
</html>
</#macro>