<ul>
    <#if cmsCategoryArticleList ??>
        <#list cmsCategoryArticleList as cmsCategoryArticle>
            <a href="${cmsCategoryArticle.linkUrl!}" target="_blank">${cmsCategoryArticle.name!}</a>
        </#list>
    </#if>
</ul>
