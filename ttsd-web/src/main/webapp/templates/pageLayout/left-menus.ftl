<#list mainMenus as menu>
    <#if activeNav?? && activeNav==menu.title && menu.leftNavs??>
    <div class="swiper-container clearfix" id="leftMenuBox">
        <ul class="left-nav swiper-wrapper">
            <#list menu.leftNavs as leftNav>
                <#if leftNav.role??>
                    <#if ['我的投资', '债权转让']?seq_contains(leftNav.title)>
                        <#assign showMenu = true >
                        <@role hasRole="'LOANER'">
                            <#assign showMenu = false >
                        </@role>
                        <#if showMenu><li class="swiper-slide"><a <#if leftNav.title==activeLeftNav>class="active"</#if>
                                                    href="${leftNav.url}">${leftNav.title}</a></li></#if>
                    <#else>
                        <@role hasRole=leftNav.role>
                            <li class="swiper-slide"><a <#if leftNav.title==activeLeftNav>class="active"</#if>
                                                        href="${leftNav.url}">${leftNav.title}</a></li>
                        </@role>
                    </#if>
                <#else>
                    <li class="swiper-slide"><a <#if leftNav.title==activeLeftNav>class="active"</#if>
                                                href="${leftNav.url}">${leftNav.title}</a></li>
                </#if>
            </#list>
        </ul>
    </div>
    </#if>

</#list>