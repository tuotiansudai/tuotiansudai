<#import "macro/global.ftl" as global>
<@global.main pageCss="${(css.main)!'main.css'}" pageJavascript="${(js.main)!'main.js'}">
<div class="article-content fl">
    <div class="category-title">分类为 <span>${tag.description}</span> 下的问题</div>
    <div class="borderBox clearfix">
        <div class="answers-box">
            <#list questions.data.records as question>
                <dl class="answers-list">
                    <dt><a href="/question/${question.id?string.computer}" target="_blank">${question.question}</a></dt>
                    <dd class="detail"><a href="/question/${question.id?string.computer}" target="_blank">${question.addition}</a></dd>
                    <dd><span>${question.mobile}</span>
                        <span class="answerNum">回答：${question.answers}</span>
                        <span class="datetime">${question.createdTime?string("yyyy-MM-dd HH:mm")}</span>
                        <span class="fr tag">
                            <#list question.tags as tag>
                                <a href="/question/category?tag=${tag.name()}">${tag.description}</a>
                            </#list>
                        </span>
                    </dd>
                </dl>
            </#list>
        </div>
    </div>

    <div class="pagination">
        <#if questions.data.hasPreviousPage>
            <a href="/question/category?tag=${tag.name()}">首页</a>
        </#if>

        <#if questions.data.index &gt; 3>
            <a href="/question/category?tag=${tag.name()}&index=${questions.data.index-1}"> < </a>
        </#if>

        <#assign lower = 1>
        <#assign upper = lower + 4>
        <#if questions.data.index - 2 &gt; 0>
            <#assign lower = questions.data.index - 2>
        </#if>
        <#if upper &gt; questions.data.maxPage>
            <#assign upper = questions.data.maxPage>
        </#if>

        <#list lower..upper as page>
            <a href="/question/category?tag=${tag.name()}&index=${page}"> ${page} </a>
        </#list>

        <#if questions.data.maxPage - questions.data.index &gt; 2>
            <a href="/question/category?tag=${tag.name()}&index=${questions.data.index+1}"> > </a>
        </#if>

        <#if questions.data.hasNextPage>
            <a href="/question/category?tag=${tag.name()}&index=${questions.data.maxPage}">末页</a>
        </#if>
    </div>
</div>

</@global.main>