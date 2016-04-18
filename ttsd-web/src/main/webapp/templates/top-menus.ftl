<div class="nav-container">
    <div class="nav">
        <a href="${applicationContext}/" class="logo"></a> <i class="fa fa-navicon show-main-menu fr" id="showMainMenu"></i>
        <#if activeNav??>
            <ul id="TopMainMenuList">
                <#list menus as menu>
                    <li><a <#if menu.title==activeNav>class="active"</#if> href="${menu.url}" onclick="cnzzPush.trackClick('${menu.category}','${menu.title}')">${menu.title}</a></li>
                </#list>
            </ul>
        </#if>
    </div>
</div>