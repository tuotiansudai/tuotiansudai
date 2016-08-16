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

<#macro main pageCss pageJavascript staticServer="${staticServer}" title="拓天速贷" keywords="" description="">
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
    <script>
        var _czc = _czc || [];
        _czc.push(["_trackEvent()", "1257936541"]);
    </script>
</head>
<body>
    <#include "../header.ftl"/>

<#--top menus-->
    <#include "../top-menu.ftl"/>
<#--top menus-->

<#--ad image-->
<div class="banner-box page-width"></div>
<#--ad image-->

<div class="main-frame full-screen clearfix">
<#--banner-->
    <div class="borderBox tc mobile-menu">
        <a href="/question" class="btn-main want-question">我要提问</a>
        <a href="/question/my-questions" class="btn-main my-question">我的提问</a>
        <a href="/answer/my-answers" class="btn-main my-answer">我的回答</a>
    </div>
    <div class="download-mobile">
        <a href="#"> <img src="${staticServer}/images/sign/downloadApp.jpg"></a>
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
            <a href="#" class="margin-top-10 ad-welfare" ></a>
        </div>
    <#--left content-->
    </div>
</div>

    <#include "../footer.ftl" />
<script src="${staticServer}${jsPath}${pageJavascript}" type="text/javascript"></script>
</body>
</html>
</#macro>