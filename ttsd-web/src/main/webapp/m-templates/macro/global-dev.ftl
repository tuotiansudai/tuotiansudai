<#macro main pageCss pageJavascript="" activeNav="" activeLeftNav="" title="拓天速贷" keywords="" description="" site='main'>
    <#assign commonStaticServer = "http://localhost:3008">
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
    <link rel="stylesheet" type="text/css" href="${commonStaticServer}/public/globalFun_page.css" charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="${commonStaticServer}/m-web/js/wap_global.css" charset="utf-8"/>

    <#if pageCss?? && pageCss != "">
        <link rel="stylesheet" type="text/css" href="${pageCss}" charset="utf-8"/>
    </#if>
</head>
<body>

<div class="nav-wap-container">
    <div class="img-top">
        <div class="logo">
            <a href="/" class="logo-bg"></a>
        </div>
        <i class="fa fa-navicon show-main-menu fr" id="showMainMenu"></i>
    </div>

    <ul id="TopMainMenuList" class="nav-menu">
        <li>
            <a href="/">
                首页
            </a>
        </li>
        <li>
            <a href="/loan-list">
                投资
            </a>
        </li>
        <li>
            <a href="/account">
                我的账户
            </a>
        </li>
        <li><a href="/register">登录/注册</a> </li>
        <li><a href="/register">下载APP</a> </li>
    </ul>
</div>
<div class="main-frame-wap" id="mainFrameWap">
    <#nested>
</div>

<div class="footer-wap-container">
    <a  class="menu-home current">
        <i></i>
        <span>首页</span>
    </a>
    <a class="menu-invest">
        <i ></i>
        <span>出借</span>
    </a>
    <a class="menu-my">
        <i ></i>
        <span>我的</span>
    </a>
</div>

<script>
    window.commonStaticServer='http://localhost:3008';
</script>


<script src="${commonStaticServer}/public/dllplugins/jquery.dll.js" ></script>
<script src="${commonStaticServer}/public/globalFun_page.js" ></script>
<script src="${commonStaticServer}/wapSite/js/wap_global.js" ></script>
<script src="${pageJavascript}" type="text/javascript" id="currentScript" ></script>
m-web
</body>
</html>
</#macro>