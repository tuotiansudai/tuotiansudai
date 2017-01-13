<div style="display: none">
    <#--<#assign askDomain="http://ask.tuotiansudai.com"/>-->
    <#assign askDomain="http://localhost:8050"/>
    <#--ask主站-->
        <a href='http://ask.tuotiansudai.com' target="_blank">ASK站</a>
    <#--cms主站-->
        <a href='http://content.tuotiansudai.com' target="_blank">CMS站</a>
    <#--ask栏目列表-->
        <a href='${askDomain}/question/sitemap/hot-category-list' target="_blank">ASK栏目列表</a>
    <#--cms栏目列表-->
        <a href='http://localhost:8080/sitemap/cms-category-list' target="_blank">CMS栏目列表</a>
    <#--ask分类信息-->
        <a href='${askDomain}/question/sitemap/category/INVEST' target="_blank">理财</a>
        <a href='${askDomain}/question/sitemap/category/STOCK' target="_blank">股票</a>
        <a href='${askDomain}/question/sitemap/category/LOAN' target="_blank">贷款</a>
        <a href='${askDomain}/question/sitemap/category/CREDIT_CARD' target="_blank">信用卡</a>
        <a href='${askDomain}/question/sitemap/category/FOREX' target="_blank">外汇</a>
        <a href='${askDomain}/question/sitemap/category/BANK' target="_blank">银行</a>
        <a href='${askDomain}/question/sitemap/category/FUND' target="_blank">基金</a>
        <a href='${askDomain}/question/sitemap/category/P2P' target="_blank">P2P</a>
        <a href='${askDomain}/question/sitemap/category/OTHER' target="_blank">其他</a>
        <a href='${askDomain}/question/sitemap/category/FUTURES' target="_blank">期货</a>
        <a href='${askDomain}/question/sitemap/category/TRUST' target="_blank">信托</a>
        <a href='${askDomain}/question/sitemap/category/SECURITIES' target="_blank">证券</a>
        <a href='${askDomain}/question/sitemap/category/CROWD_FUNDING' target="_blank">众筹</a>
    <#--cms分类信息列表-->
        <#if siteMapList??>
            <#list siteMapList as siteMap>
                <#if siteMap??>
                    <a href='${siteMap.linkUrl!}' target="_blank">${siteMap.name!}</a>
                </#if>
            </#list>
        </#if>
</div>