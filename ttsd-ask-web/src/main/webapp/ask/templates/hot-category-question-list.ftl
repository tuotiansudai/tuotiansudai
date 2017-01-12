<#if siteMapDataDtoList??>
    <#list siteMapDataDtoList as siteMapDataDto>
            <a href="${siteMapDataDto.linkUrl!}" target="_blank">${siteMapDataDto.name!}</a>
    </#list>
</#if>
