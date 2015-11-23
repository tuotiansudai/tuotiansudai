<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

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
    {"title":"推荐奖励", "url":"/events/refer-reward-instruction"},
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
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
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
        <a href="${requestContext.getContextPath()}/" class="logo"></a>
        <#if activeNav??>
            <ul>
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

    function stopBubble(e) {
        if ( e && e.stopPropagation )
            e.stopPropagation();
        else
            window.event.cancelBubble = true;
    }
    var imgDom=document.getElementById('iphone-app-img');
    function stopBubble(e)
    {
        if (e && e.stopPropagation)
            e.stopPropagation()
        else
            window.event.cancelBubble=true
    }
    document.getElementById('iphone-app-pop').addEventListener('click',function(e) {
        stopBubble(e);

        if(imgDom.style.display == "block") {
            imgDom.style.display='none';
        }
        else {
            imgDom.style.display='block';
        }
    });
    document.getElementsByTagName("body")[0].addEventListener('click',function() {
        imgDom.style.display='none';
    });

</script>
<script src="${staticServer}/js/dest/${js.config}" type="text/javascript" charset="utf-8"></script>
<#if pageJavascript??>
<script src="${staticServer}/js/libs/require-2.1.20.min.js" type="text/javascript" charset="utf-8" defer="defer" async="async"
        data-main="${staticServer}/js/dest/${pageJavascript}">
</script>
</#if>
<div class="hide">

    <script type="text/javascript">
        var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1254796373'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1254796373%26show%3Dpic1' type='text/javascript'%3E%3C/script%3E"));
    </script>
    <script type="text/javascript">
        var _hmt = _hmt || [];
        (function() {
            var hm = document.createElement("script");
            hm.src = "//hm.baidu.com/hm.js?b4205647cc83b405927bd22f70eaf362";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>

</div>
</body>
</html>
</#macro>