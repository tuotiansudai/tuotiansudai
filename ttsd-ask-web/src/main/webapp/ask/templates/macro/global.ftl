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

    <#include "../cnzz.ftl"/>
    <!-- growing io -->
    <#include "../growing-io.ftl"/>
</head>
<body>
    <#include "../header.ftl"/>

<#--top menus-->
    <#include "../top-menu.ftl"/>
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

    <#include "../footer.ftl" />

<script type="text/javascript">
    window.$ = function(id) {
        return document.getElementById(id);
    };
    var imgDom=window.$('iphone-app-img'),
            TopMainMenuList=window.$('TopMainMenuList');
    if (window.$('iphone-app-pop')) {
        window.$('iphone-app-pop').onclick=function(event) {
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


    document.getElementById('showMainMenu').onclick=function(event) {
        event.stopPropagation();
        event.preventDefault();
        this.nextElementSibling.style.display='block';
    }

    document.getElementsByTagName("body")[0].onclick=function(e) {
        var userAgent = navigator.userAgent.toLowerCase(),
                event = e || window.event,
                target = event.srcElement || event.target;

        imgDom.style.display='none';
        if(userAgent.indexOf('android') > -1 || userAgent.indexOf('iphone') > -1 || userAgent.indexOf('ipad') > -1) {

            //判断是否为viewport
            var metaTags=document.getElementsByTagName('meta'),
                    metaLen=metaTags.length,i=0;
            for(;i<metaLen;i++) {
                if(metaTags[i].getAttribute('name')=='viewport') {
                    document.getElementById('TopMainMenuList').style.display='none';
                }
            }
        }

    };
</script>
<script src="${staticServer}${jsPath}${pageJavascript}" type="text/javascript"></script>

</body>
</html>
</#macro>