<div class="nav-container">
    <div class="nav">
        <a href="${applicationContext}/" class="logo"></a> <i class="fa fa-navicon show-main-menu fr" id="showMainMenu"></i>
        <#if activeNav??>
            <ul id="TopMainMenuList">
                <#list mainMenus as menu>
                    <#if menu.navigation?? && menu.navigation="true">
                        <li><a <#if menu.title==activeNav>class="active"</#if> href="${menu.url}" onclick="cnzzPush.trackClick('${menu.category}','${menu.title}')">${menu.title}</a></li>
                    </#if>
                </#list>
                <li class="top-membership"><a href="/membership">会员中心</a> </li>
                <li class="top-activity">
                    <a href="/activity-center">活动中心</a>
                </li>
            </ul>
        </#if>
    </div>
</div>