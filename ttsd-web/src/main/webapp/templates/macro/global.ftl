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
    {"title":"推荐管理", "url":"/referrer/refer-list", "role":"'INVESTOR', 'LOANER'"},
    {"title":"我的宝藏", "url":"/my-treasure", "role":"'INVESTOR', 'LOANER'"}
    ]},
    {"title":"新手指引", "url":"/about/guide"},
    {"title":"关于我们", "url":"/about/company", "leftNavs":[
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
    <meta name="keywords" content="拓天,拓天速贷,投资,投资安全,理财,理财安全,P2P,速贷,金融,互联网金融,互联网金融是什么,银行理财,私募,信托,贷款,股票,P2P理财,P2P财是什么,理财产品公司,理财产品排行,P2P理财公司,P2P理财产品公司,P2P公司,理财排名,互联网借贷平台,网络借贷平台,中介,中介金融,中介金融公司,金融中介,互联网金融公司,P2P收益,高收益,拓天伟业,拓天官网,拓天资产,拓天担保,担保,伟业,资产,资产管理,房屋抵押,汽车抵押,房屋抵押贷款,抵押贷款,买车贷款,公证书,房产证,他项证,银行托管,第三方托管,第三方支付,支付,提现,理财服务费,新手,投资新手,理财新手,新手体验,新手体验券,投资体验券,体验,投资体验,加息,加息券,债权,债务,债权转让,转让,短期借贷,借贷,P2P搜索,P2P搜索神器,神器,P2P终结者,P2P排名,P2P网贷,网贷,网贷App,网贷应用,网贷工具,网贷软件,网贷系统,网贷安全,网贷之家,网贷专家,理财专家,网贷天眼,P2Peye,p2p天眼,P2P软件,P2P第一品牌,P2P系统,p2p投资,p2p贷款,P2P怎么用,P2P贷款平台,国资系P2P,国资P2P,P2P圈,小额贷款,p2p小额贷款,致富,小额担保,年化收益,收益,利息,高利息,利率,高利率,普惠,普惠金融">
    <meta name="description" content="拓天速贷是基于互联网的金融信息服务平台，由拓天伟业（北京）资产管理有限公司旗下的拓天伟业（北京）金融信息服务有限公司运营">
    <#if responsive??>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    </#if>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>${title}</title>
    <link href="${staticServer}/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}${css.global}" charset="utf-8" />
    <#if pageCss?? && pageCss != "">
    <link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}${pageCss}" charset="utf-8" />
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
<div class="main-frame full-screen ">
    <#list menus as menu>
        <#if activeNav?? && activeNav==menu.title && menu.leftNavs??>
        <div class="swiper-container">
            <ul class="left-nav swiper-wrapper">
                <#list menu.leftNavs as leftNav>
                    <#if leftNav.role??>
                        <@role hasRole=leftNav.role>
                            <li class="swiper-slide"><a <#if leftNav.title==activeLeftNav>class="active"</#if> href="${leftNav.url}">${leftNav.title}</a></li>
                        </@role>
                    <#else>
                        <li class="swiper-slide"><a <#if leftNav.title==activeLeftNav>class="active"</#if> href="${leftNav.url}">${leftNav.title}</a></li>
                    </#if>
                </#list>
            </ul>
        </div>
        </#if>
    </#list>
    <#nested>
</div>
<#include "../footer.ftl" />
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
<#if pageJavascript??>
<script src="${staticServer}/js/libs/require-2.1.20.min.js" type="text/javascript" charset="utf-8" defer="defer" async="async"
        data-main="${staticServer}${jsPath}${pageJavascript}">
</script>
</#if>

<#include "../statistic.ftl" />
</body>
</html>
</#macro>