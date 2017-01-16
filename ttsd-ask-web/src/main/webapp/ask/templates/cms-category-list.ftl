<ul>
    <#list cmsCategoryList as cmsCategory>
        <#if cmsCategory.seq = 1>
            <li>
                <a href="${cmsCategory.linkUrl!}" target="_blank">${cmsCategory.name!}</a><br/>
            </li>
        <#else>
            <a href="${cmsCategory.linkUrl!}" target="_blank">${cmsCategory.name!}</a>
        </#if>
    </#list>
</ul>
