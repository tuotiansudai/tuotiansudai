<?xml version="1.0" encoding="utf-8"?>
<sitemapindex>
<#if siteMap??>
    <#list siteMap?keys as prop>
        <sitemap>
            <loc>https://tuotiansudai.com/${prop!}</loc>

            <lastmod>${siteMap[prop]!}</lastmod>
        </sitemap>
    </#list>
</#if>
</sitemapindex>