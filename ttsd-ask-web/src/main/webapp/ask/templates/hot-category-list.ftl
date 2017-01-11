<ul>
    <#list hotCategoryList as hotCategory>
        <li>
            <a href="${hotCategory.linkUrl!}" target="_blank">${hotCategory.name!}</a>
        </li>
    </#list>
</ul>
