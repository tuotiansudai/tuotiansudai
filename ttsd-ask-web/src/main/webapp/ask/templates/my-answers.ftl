<#import "macro/global.ftl" as global>
<@global.main pageCss="${(css.main)!'main.css'}" pageJavascript="${(js.main)!'main.js'}">
<div class="article-content fl" id="myQAnswer">
    <ul class="switch-menu clearfix" id="my-questions-tab">
        <li><a href="/question/my-questions">我的提问</a></li>
        <li class="active"><a href="/answer/my-answers">我的回答</a></li>
    </ul>
    <div class="borderBox clearfix">
        <div class="answers-box">
            <#list answers.data.records as answer>
                <dl class="answers-list">
                    <dt>${answer.question.question}</dt>
                    <dd>${answer.question.addition!}</dd>
                    <dd><span>${answer.question.mobile}</span>
                        <span>回答：${answer.question.answers}</span>
                        <span class="datetime">${answer.question.createdTime?string("yyyy-MM-dd HH:mm")}</span>
                            <span class="fr tag">
                                <#list answer.question.tags as tag>
                                    <a class="/question/category?tag=${tag.name()}" href="">${tag.description}</a>
                                </#list>
                            </span>
                    </dd>

                    <dd class="answer-button">
                        <span>我的回答</span>
                        <#if answer.bestAnswer>
                            <span class="accept">已被采纳</span>
                        </#if>
                    </dd>
                    <dd>
                        <p>
                        ${answer.answer}
                        </p>
                    </dd>
                </dl>
            </#list>
        </div>
    </div>

    <div class="pagination">
        <#if answers.data.hasPreviousPage>
            <a href="/answer/my-answers">首页</a>
        </#if>

        <#if answers.data.index &gt; 3>
            <a href="/answer/my-answers?index=${answers.data.index-1}"> < </a>
        </#if>

        <#assign lower = 1>
        <#assign upper = lower + 4>
        <#if answers.data.index - 2 &gt; 0>
            <#assign lower = answers.data.index - 2>
        </#if>
        <#if upper &gt; answers.data.maxPage>
            <#assign upper = answers.data.maxPage>
        </#if>

        <#list lower..upper as page>
            <a href="/answer/my-answers?index=${page}"> ${page} </a>
        </#list>

        <#if answers.data.maxPage - answers.data.index &gt; 2>
            <a href="/answer/my-answers?index=${answers.data.index+1}"> > </a>
        </#if>

        <#if answers.data.hasNextPage>
            <a href="/answer/my-answers?index=${answers.data.maxPage}">末页</a>
        </#if>
    </div>
</div>


</@global.main>