<div class="hot-questions margin-top-10 clearfix">
    <div class="qa-title">热门问题分类</div>
    <ul class="qa-list clearfix">
        <#list tags as tag>
            <li>
                <a href="javascript:void(0);" class="active">${tag.description}</a>
            </li>
        </#list>
    </ul>
</div>