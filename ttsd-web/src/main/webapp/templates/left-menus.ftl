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