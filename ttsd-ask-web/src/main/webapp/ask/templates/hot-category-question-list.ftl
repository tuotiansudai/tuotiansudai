<#import "macro/global.ftl" as global>
<#if siteMapDataDtoList??>
    <#list siteMapDataDtoList as siteMapDataDto>
            <a href="${global.applicationContext}${siteMapDataDto.linkUrl!}" target="_blank">${siteMapDataDto.name!}</a>
    </#list>
</#if>
