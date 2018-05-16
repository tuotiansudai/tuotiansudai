<?xml version="1.0"encoding="UTF-8"?>
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