<div class="nav-container">
    <div class="img-top page-width clearfix">
        <div class="logo">
            <a href="${applicationContext}/" class="logo-bg"></a>
        </div>
        <div class="login-pop-app" id="iphone-app-pop">
            <em class="img-app-download"></em>
            <a href="javascript:" class="text" onclick="cnzzPush.trackClick('13顶部导航','手机APP')"><i>手机APP</i>
                出借更便利</a>
            <div id="iphone-app-img" class="img-app-pc-top hide"></div>
        </div>
        <i class="fa fa-navicon show-main-menu fr" id="showMainMenu"></i>
    </div>

<#if activeNav??>
    <ul id="TopMainMenuList" class="nav-menu page-width clearfix" >
        <#list mainMenus as menu>
            <#if menu.navigation?? && menu.navigation="true">
                <li <#if menu.title==activeNav>class="active"</#if>>
                    <a  href="${menu.url}" onclick="cnzzPush.trackClick('${menu.category}','${menu.title}')" >${menu.title}
                    <#if menu.leftNavs??>
                        <#list menu.leftNavs as leftNav>
                            <#if leftNav.role??>
                                <#assign showLeftNavs=false>
                                <@role hasRole=leftNav.role>
                                    <#assign showLeftNavs=true>
                                </@role>
                            <#else>
                                <#assign showLeftNavs=true>
                            </#if>
                        </#list>
                        <#if showLeftNavs>
                            <span class="icon-has-submenu"></span>
                        </#if>
                    </a>
                        <ul class="sub-menu-list">
                            <#list menu.leftNavs as leftNav>
                                <#if leftNav.role??>
                                    <@role hasRole=leftNav.role>
                                        <li>
                                            <a <#if leftNav.title==activeLeftNav>class="active"</#if>
                                               href="${leftNav.url}"><i>●</i>${leftNav.title}</a>
                                        </li>
                                    </@role>
                                <#else>
                                    <li><a <#if leftNav.title==activeLeftNav>class="active"</#if>
                                           href="${leftNav.url}"> <i>●</i> ${leftNav.title}</a>
                                    </li>
                                </#if>
                            </#list>
                        </ul>
                    </#if>
                </li>

            </#if>
        </#list>
        <li class="top-membership"><a href="/membership">会员中心</a> </li>
        <li class="top-activity">
            <a href="${webServer}/activity/activity-center">活动中心</a>
        </li>
    </ul>
</#if>
</div>