<#macro main pageCss pageJavascript="" activeNav="" activeLeftNav="" title="拓天速贷" keywords="" description="" site='main'>
    <#assign commonStaticServer = "http://localhost:3008/">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>${title}</title>
    <meta name="keywords" content="${keywords}">
    <meta name="description" content="${description}">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="_csrf" content="f60ab1bf-cb28-4ea9-9023-2ac992109c0c"/>
    <meta name="_csrf_header" content="X-CSRF-TOKEN"/>
    <meta name="360-site-verification" content="a3066008a453e5dfcd9f3e288862c9ef" />
    <meta name="sogou_site_verification" content="lXIPItRbXy"/>
    <meta name="baidu-site-verification" content="XVFtcOmhlc" />
    <link rel="stylesheet" type="text/css" href="http://localhost:3008/public/globalFun_page.css" charset="utf-8"/>
    <#if pageCss?? && pageCss != "">
        <link rel="stylesheet" type="text/css" href="${pageCss}" charset="utf-8"/>
    </#if>
</head>
<body>
<div class="header-container">
    <div class="header-download">
        <div id="closeDownloadBox" class="icon-close img-close-tip"></div>
        <div class="img-logo-tip"></div>
        <span>APP客户端重磅来袭<br>更便捷更安全</span>
        <a href="#" class="btn-normal fr" id="btnExperience">立即体验</a>
    </div>
    <div class="header-top page-width">
        <span class="fl service-time">客服电话：400-169-1188<time>（服务时间：9:00－20:00）</time></span>
        <ul class="fr">
            <li class="membership-store">
                <a href="/point-shop" target="_blank">积分商城</a>
            </li>
            <li class="header-membership">
                <a href="/membership" target="_blank">会员中心</a>
            </li>
            <li class="header-activity-center">
                <a href="https://tuotiansudai.com/activity/activity-center">活动中心</a>
            </li>
            <li class="header-help-center">
                <a href="/help/help-center">帮助中心</a>
            </li>

            <li class="header-login">
                <a href="/login">登录</a>
            </li>
            <li class="header-register">
                <a href="/register/user">注册</a>
            </li>
        </ul>

    </div>
</div>

<div class="nav-container">
    <div class="img-top page-width clearfix">
        <div class="logo">
            <a href="/" class="logo-bg"></a>
        </div>
        <div class="login-pop-app" id="iphone-app-pop">
            <em class="img-app-download"></em>
            <a href="javascript:" class="text"><i>手机APP</i>
                出借更便利</a>
            <div id="iphone-app-img" class="img-app-pc-top hide"></div>
        </div>
        <i class="fa fa-navicon show-main-menu fr" id="showMainMenu"></i>
    </div>

    <ul id="TopMainMenuList" class="nav-menu page-width clearfix">
        <li>
            <a href="/">
                首页
            </a>
        </li>
        <li>
            <a href="/loan-list">
                我要投资
                <span class="icon-has-submenu"></span>
            </a>

            <ul class="sub-menu-list">
                <li><a href="/loan-list"> <i>●</i> 直投项目</a>
                </li>
                <li><a href="/transfer-list"> <i>●</i> 转让项目</a>
                </li>
            </ul>

        </li>
        <li>
            <a href="/loan-application">
                我要借款
            </a>
        </li>
        <li>
            <a href="/account">
                我的账户
            </a>
            <ul class="sub-menu-list">
            </ul>
        </li>
        <li>
            <a href="http://localhost:8050/?group=HOT&amp;index=1">
                拓天问答
            </a>
        </li>
        <li>
            <a href="/about/company">
                信息披露
                <span class="icon-has-submenu"></span>
            </a>

            <ul class="sub-menu-list">
                <li><a href="/about/company"> <i>●</i> 公司介绍</a>
                </li>
                <li><a href="/about/team"> <i>●</i> 团队介绍</a>
                </li>
                <li><a href="/about/notice"> <i>●</i> 拓天公告</a>
                </li>
                <li><a href="/about/media"> <i>●</i> 媒体报道</a>
                </li>
                <li><a href="/about/service-fee"> <i>●</i> 服务费用</a>
                </li>
                <li><a href="/about/contact"> <i>●</i> 联系我们</a>
                </li>
                <li><a href="/about/operational"> <i>●</i> 运营数据</a>
                </li>
            </ul>

        </li>
        <li class="top-membership"><a href="/membership">会员中心</a> </li>
        <li class="top-activity">
            <a href="http://localhost:8080/activity/activity-center">活动中心</a>
        </li>
    </ul>
</div>
<div class="main-frame full-screen clearfix">

    <#nested>
</div>
    <#include "../pageLayout/footer.ftl" />
<script>
    window.commonStaticServer='http://localhost:3008';
</script>


<script src="http://localhost:3008/public/dllplugins/jquery.dll.js" defer></script>
<script src="http://localhost:3008/public/globalFun_page.js" defer></script>

<#if pageJavascript??>
<script src="${pageJavascript}" type="text/javascript" id="currentScript"></script>
</#if>

</body>
</html>
</#macro>