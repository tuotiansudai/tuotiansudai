<#import "macro/global.ftl" as global>
<div class="hot-questions margin-top-10 clearfix">
    <div class="qa-title">热门问题分类</div>
    <ul class="qa-list clearfix">
    <#list tags as tagItem>
        <li>
            <a href="${global.applicationContext}/question/category/${tagItem.name()}"
               <#if tag?? && tagItem == tag>class="active"</#if>>${tagItem.description}</a>
        </li>
    </#list>
    </ul>
</div>