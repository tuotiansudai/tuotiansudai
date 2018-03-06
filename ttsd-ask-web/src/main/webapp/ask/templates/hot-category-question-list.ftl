<#if siteMapDataDtoList??>
    <#list siteMapDataDtoList as siteMapDataDto>
            <a href="${applicationContext}${siteMapDataDto.linkUrl!}" target="_blank">${siteMapDataDto.name!}</a>
    </#list>
</#if>
