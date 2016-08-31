<div class="nav-container">
    <div class="nav">
        <a href="${webServer}${applicationContext}/" class="logo"></a>
        <i class="fa fa-navicon show-main-menu fr" id="showMainMenu"></i>

    <#if activeNav??>
        <ul id="TopMainMenuList">
            <#list membershipMenus as menu>
                <li><a <#if menu.title==activeNav>class="active"</#if> href="${menu.url}">${menu.title}</a></li>
            </#list>
            <li><a href="${webServer}/">返回首页</a></li>
        </ul>
    </#if>
    </div>
</div>