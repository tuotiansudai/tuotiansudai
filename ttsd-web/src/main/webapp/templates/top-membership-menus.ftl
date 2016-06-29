<div class="nav-container">
    <div class="nav">
        <a href="${applicationContext}/" class="logo"></a> <i class="fa fa-navicon show-main-menu fr"
                                                              id="showMainMenu"></i>

        <div class="fr go-home">
            <a href="/">返回首页</a>
        </div>
    <#if activeNav??>
        <ul id="TopMainMenuList">
            <#list membershipMenus as menu>
                <li><a <#if menu.title==activeNav>class="active"</#if> href="${menu.url}">${menu.title}</a></li>
            </#list>
        </ul>
    </#if>
    </div>
</div>