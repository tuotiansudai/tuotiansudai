<urlset>
    <#if subSiteMap??>
        <#list subSiteMap as sub>
            <url>
                <loc>${sub!}</loc>
                <lastmod>${lastModifyTime}</lastmod>
            </url>
        </#list>

    </#if>
</urlset>