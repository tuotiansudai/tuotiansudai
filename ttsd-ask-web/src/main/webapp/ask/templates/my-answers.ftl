<#import "macro/global.ftl" as global>
<@global.main pageCss="${(css.mainSite)!}" pageJavascript="${(js.mainSite)!}" title="拓天问答_投资问答_拓天速贷" keywords="投资问答,网贷问答,投资知识,金融问答" description="拓天速贷投资问答系统,为您解答金融行业最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.">
<div class="article-content fl" id="myQAnswer">
    <ul class="switch-menu clearfix" id="my-questions-tab">
        <li><a href="${applicationContext}/question/my-questions">我的提问</a></li>
        <li class="active"><a href="${applicationContext}/answer/my-answers">我的回答</a></li>
    </ul>
    <div class="border-ask-box clearfix">
        <div class="answers-box">
            <#list answers.data.records as answer>
                <dl class="answers-list">
                    <dt><a href="${applicationContext}/question/${answer.question.id?string.computer}" target="_blank">${answer.question.question}</a></dt>
                    <dd><a href="${applicationContext}/question/${answer.question.id?string.computer}" target="_blank">${answer.question.addition!}</a></dd>
                    <dd><span>${answer.question.mobile}</span>
                        <span>回答：${answer.question.answers}</span>
                        <span class="datetime">${answer.question.createdTime?string("yyyy年MM月dd日 HH:mm")}</span>

                                <#list answer.question.tags as tag>
                                <span class="fr tag">
                                    <a class="${applicationContext}/question/category/${tag.name()}" href="">${tag.description}</a>
                               </span>
                                </#list>

                    </dd>

                    <dd class="answer-button">
                        <span>我的回答</span>
                        <#if answer.bestAnswer>
                            <span class="accept">已被采纳</span>
                        </#if>
                    </dd>
                    <dd>
                        <p>
                        ${answer.answer?replace('\\n','<br/>','i')?replace('\\r','<br/>','i')}
                        </p>
                    </dd>
                </dl>
            </#list>
        </div>
    </div>

    <div class="pagination">
        <#if answers.data.hasPreviousPage>
            <a href="${applicationContext}/answer/my-answers">首页</a>
        </#if>
        <#if answers.data.index &gt; 3>
            <a href="${applicationContext}/answer/my-answers?index=${answers.data.index-1}"> < </a>
        </#if>

        <#assign lower = 1>
        <#assign upper = answers.data.maxPage>
        <#if answers.data.maxPage &gt; 5>
            <#assign lower = answers.data.index>
            <#assign upper = answers.data.index>
            <#list 1..2 as index>
                <#if answers.data.index - index &gt; 0>
                    <#assign lower = lower - 1>
                <#else>
                    <#assign upper = upper + 1>
                </#if>
            </#list>
            <#list 1..2 as index>
                <#if answers.data.index + index <= answers.data.maxPage>
                    <#assign upper = upper + 1>
                <#else>
                    <#assign lower = lower - 1>
                </#if>
            </#list>
        </#if>

        <#list lower..upper as page>
            <a href="${applicationContext}/answer/my-answers?index=${page}" <#if page == answers.data.index>class="active"</#if>> ${page} </a>
        </#list>

        <#if answers.data.maxPage - answers.data.index &gt; 2>
            <a href="${applicationContext}/answer/my-answers?index=${answers.data.index+1}"> > </a>
        </#if>
        <#if answers.data.hasNextPage>
            <a href="${applicationContext}/answer/my-answers?index=${answers.data.maxPage}">末页</a>
        </#if>
    </div>
</div>


</@global.main>