<ul>
    <#if cmsCategoryList ??>
        <#list cmsCategoryList as cmsCategory>
            <li>
                <a href="${cmsCategory.linkUrl!}" target="_blank">${cmsCategory.name!}</a><br/>
            </li>
        </#list>
    </#if>
</ul>
