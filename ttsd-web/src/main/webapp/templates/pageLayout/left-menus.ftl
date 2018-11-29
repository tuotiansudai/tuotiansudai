<#list mainMenus as menu>
    <#if activeNav?? && activeNav==menu.title && menu.leftNavs??>
    <div class="swiper-container clearfix" id="leftMenuBox">
        <ul class="left-nav swiper-wrapper">
                <#list menu.leftNavs as leftNav>
                <#if leftNav.role??>
                    <@role hasRole=leftNav.role>
                        <li class="swiper-slide"><a <#if leftNav.title==activeLeftNav>class="active"</#if>
                                                    href="${leftNav.url}">${leftNav.title}</a></li>
                    </@role>
                <#else>
                    <li class="swiper-slide">
                        <#if leftNav.subLeftMenuItem??>
                            <a class="totalMenu <#if leftNav.title==activeLeftNav>menuItem active</#if>" data-current="${leftNav.current}">
                                <span <#if leftNav.title==activeLeftNav >class="text_icon text_icon_open" <#else> class="text_icon text_icon_close" </#if>></span>
                                ${leftNav.title}
                            </a>
                            <div <#if leftNav.title==activeLeftNav>class="subMenu_show subMenu" <#else> class="subMenu_hide subMenu" </#if>>
                                <#list leftNav.subLeftMenuItem as subLeftMenu>
                                    <span class="object_label subMenuItem <#if subLeftMenu_index == 0>default_light_item</#if>" data-position="${subLeftMenu.offsetName}">
                                        ${subLeftMenu.title}
                                    </span>
                                </#list>
                            </div>
                        <#else>
                            <a <#if leftNav.title==activeLeftNav>class="active noSubMenuItem"</#if> href="${leftNav.url}">${leftNav.title}</a>
                        </#if>
                    </li>
                </#if>
            </#list>
        </ul>
    </div>
    </#if>

</#list>