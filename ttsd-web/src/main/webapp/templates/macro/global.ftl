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

<#macro main pageCss pageJavascript="" activeNav="" activeLeftNav="" title="拓天速贷" keywords="" description="">
    <#local menus=[
    {"title":"首页", "url":"/","category":"16顶部导航"},
    {"title":"我要投资", "url":"/loan-list","category":"17顶部导航"},
    {"title":"我的账户", "url":"/account", "category":"18顶部导航","leftNavs":[
    {"title":"账户总览", "url":"/account", "role":"'INVESTOR', 'LOANER'"},
    {"title":"我的投资", "url":"/investor/invest-list", "role":"'INVESTOR'"},
    {"title":"债权转让", "url":"/transferrer/transfer-application-list/transferable", "role":"'INVESTOR'"},
    {"title":"我的借款", "url":"/loaner/loan-list", "role":"'LOANER'"},
    {"title":"资金管理", "url":"/user-bill", "role":"'INVESTOR', 'LOANER'"},
    {"title":"我的财豆", "url":"/point", "role":"'INVESTOR', 'LOANER'"},
    {"title":"个人资料", "url":"/personal-info", "role":"'INVESTOR', 'LOANER'"},
    {"title":"自动投标", "url":"/investor/auto-invest", "role":"'INVESTOR'"},
    {"title":"推荐管理", "url":"/referrer/refer-list", "role":"'INVESTOR', 'LOANER'"},
    {"title":"我的宝藏", "url":"/my-treasure", "role":"'INVESTOR', 'LOANER'"}
    ]},
    {"title":"新手指引", "url":"/about/guide","category":"19顶部导航"},
    {"title":"关于我们", "url":"/about/company","category":"20顶部导航", "leftNavs":[
    {"title":"公司介绍", "url":"/about/company"},
    {"title":"团队介绍", "url":"/about/team"},
    {"title":"拓天公告", "url":"/about/notice"},
    {"title":"媒体报道", "url":"/about/media"},
    {"title":"推荐奖励", "url":"/about/refer-reward"},
    {"title":"服务费用", "url":"/about/service-fee"},
    {"title":"常见问题", "url":"/about/qa"},
    {"title":"联系我们", "url":"/about/contact"}
    ]}]/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <meta name="keywords" content="${keywords}">

    <meta name="description" content="${description}">
    <#if responsive??>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    </#if>
    <meta name="_csrf" content="${(_csrf.token)!}"/>
    <meta name="_csrf_header" content="${(_csrf.headerName)!}"/>
    <title>${title}</title>
    <link href="${staticServer}/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}${css.global}" charset="utf-8" />
    <#if pageCss?? && pageCss != "">
    <link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}${pageCss}" charset="utf-8" />
    </#if>
    <script>
        var _czc = _czc || [];
        <#if isProduction>
            _czc.push(["_trackEvent()", "1254796373"]);
        <#else >
            _czc.push(["_trackEvent()", "1257936541"]);
        </#if>

    </script>
</head>
<body>

<#if !isAppSource>
    <#include "../header.ftl"/>
    <#include "../top-menus.ftl"/>
</#if>

<div class="main-frame full-screen clearfix">
    <#if !isAppSource>
        <#include "../left-menus.ftl"/>
    </#if>
    <#nested>
</div>

<#if !isAppSource>
    <#include "../footer.ftl" />
</#if>

<script type="text/javascript" charset="utf-8">
    var staticServer = '${staticServer}';
    <@security.authorize access="isAuthenticated()">
    document.getElementById("logout-link").onclick=function (event) {
        event.preventDefault();
        document.getElementById("logout-form").submit();
    };
    </@security.authorize>

    adjustMobileHideHack();
    function adjustMobileHideHack() {

        //this function will be remove when all pages are responsive
        var bodyDom=document.getElementsByTagName("body")[0],
            userAgent = navigator.userAgent.toLowerCase(),
            metaTags=document.getElementsByTagName('meta'),
            metaLen=metaTags.length,isResponse=false,isPC=false,i=0;
        isPC = !(userAgent.indexOf('android') > -1 || userAgent.indexOf('iphone') > -1 || userAgent.indexOf('ipad') > -1);
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
            location.href = "/app/download";
        };

        window.$('showMainMenu').onclick=function(event) {
            event.stopPropagation();
            event.preventDefault();
            this.nextElementSibling.style.display='block';

        };

    }
    var imgDom=window.$('iphone-app-img'),
        TopMainMenuList=window.$('TopMainMenuList');

    if (window.$('iphone-app-pop')) {
        window.$('iphone-app-pop').onclick=function(e) {
            if(imgDom.style.display == "block") {
                imgDom.style.display='none';
            }
            else {
                imgDom.style.display='block';
            }
            if (event.stopPropagation) {
                event.stopPropagation();
            }
            else if (window.event) {
                window.event.cancelBubble = true;
            }
        };
    }

    document.getElementsByTagName("body")[0].onclick=function(e) {
        var userAgent = navigator.userAgent.toLowerCase(),
                event = e || window.event,
                target = event.srcElement || event.target;
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

<script src="${staticServer}${jsPath}${js.config}" type="text/javascript" charset="utf-8"></script>

<#if pageJavascript?? && pageJavascript?length gt 0>
<script src="${staticServer}/js/libs/require-2.1.20.min.js" type="text/javascript" charset="utf-8" defer="defer" async="async"
        data-main="${staticServer}${jsPath}${pageJavascript}">

</script>
<script src="${staticServer}${jsPath}${js.cnzz_statistics}" type="text/javascript" charset="utf-8"></script>

</#if>

<#include "../statistic.ftl" />
</body>
</html>
</#macro>