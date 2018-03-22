<#import "macro/global.ftl" as global>
<ul>
    <#if hotCategoryList??>
        <#list hotCategoryList as hotCategory>
            <li>
                <a href="${global.applicationContext}${hotCategory.linkUrl!}" target="_blank">${hotCategory.name!}</a>
            </li>
        </#list>
    </#if>
</ul>
