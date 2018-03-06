<ul>
    <#if hotCategoryList??>
        <#list hotCategoryList as hotCategory>
            <li>
                <a href="${applicationContext}${hotCategory.linkUrl!}" target="_blank">${hotCategory.name!}</a>
            </li>
        </#list>
    </#if>
</ul>
