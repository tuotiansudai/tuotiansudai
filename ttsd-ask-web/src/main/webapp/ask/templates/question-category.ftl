<#import "macro/global.ftl" as global>
<#assign tkdMap = {"SECURITIES":"{'title':'证券问答_拓天速贷','keywords':'证券问答,网贷问答,证券知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答证券最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "BANK":"{'title':'银行问答_拓天速贷','keywords':'银行问答,网贷问答,银行知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答银行最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "FUTURES":"{'title':'期货问答_拓天速贷','keywords':'期货问答,网贷问答,期货知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答期货最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "P2P":"{'title':'P2P问答_拓天速贷','keywords':'P2P问答,网贷问答,P2P知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答P2P行业最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "TRUST":"{'title':'信托问答_拓天速贷','keywords':'信托问答,网贷问答,信托知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答信托行业最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "LOAN":"{'title':'贷款问答_拓天速贷','keywords':'贷款问答,网贷问答,贷款知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答贷款最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "FUND":"{'title':'基金问答_拓天速贷','keywords':'基金问答,网贷问答,基金知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答基金行业最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "CROWD_FUNDING":"{'title':'众筹问答_拓天速贷','keywords':'众筹问答,网贷问答,众筹知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答众筹最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "INVEST":"{'title':'投资问答_拓天速贷','keywords':'投资问答,网贷问答,投资知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答金融行业最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "CREDIT_CARD":"{'title':'信用卡问答_拓天速贷','keywords':'信用卡问答,网贷问答,信用卡知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答信用卡最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "FOREX":"{'title':'外汇问答_拓天速贷','keywords':'外汇问答,网贷问答,外汇知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答外汇最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "STOCK":"{'title':'股票问答_拓天速贷','keywords':'股票问答,网贷问答,股票知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答股票最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}",
                   "OTHER":"{'title':'金融问答_拓天速贷','keywords':'投资问答,网贷问答,投资知识,金融问答','descritpion':'拓天速贷投资问答系统,为您解答金融行业最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.'}"
}>

<#list tkdMap?keys as key>
    <#assign tkd = tkdMap[key]?eval>
    <#if tag.name() == key>
        <#assign title='${tkd.title}'>
        <#assign keywords='${tkd.keywords}'>
        <#assign description='${tkd.descritpion}'>
        <#break>
    </#if>
</#list>
<@global.main pageCss="${(css.mainSite)!}" pageJavascript="${(js.mainSite)!}" title="${title}" keywords="${keywords}" description="${description}">

<div class="article-content fl">
    <div class="category-title">分类为<span>${tag.description}</span> 下的问题</div>
    <div class="border-ask-box clearfix">
        <div class="answers-box">
            <#list questions.data.records as question>
                <dl class="answers-list">
                    <dt><a href="${global.applicationContext}/question/${question.id?string.computer}" target="_blank">${question.question}</a></dt>
                    <dd class="detail"><a href="${global.applicationContext}/question/${question.id?string.computer}" target="_blank">${question.addition?replace('\\n','<br/>','i')?replace('\\r','<br/>','i')}</a></dd>
                    <dd><span>${question.mobile}</span>
                        <span class="answerNum">回答：${question.answers}</span>
                        <span class="datetime">${question.createdTime?string("yyyy年MM月dd日 HH:mm")}</span><span style="width:100%;height:1px"></span>

                        <#list question.tags as tag>
                                <em class="fr tag">
                                    <a href="${global.applicationContext}/question/category/${tag.name()}">${tag.description}</a>
                                </em>
                            </#list>
                    </dd>
                </dl>
            </#list>
        </div>
    </div>

    <div class="pagination">
        <#if questions.data.hasPreviousPage>
            <a href="${global.applicationContext}/question/category/${tag.name()}">首页</a>
        </#if>
        <#if questions.data.index &gt; 3>
            <a href="${global.applicationContext}/question/category/${tag.name()}?index=${questions.data.index-1}"> < </a>
        </#if>

        <#assign lower = 1>
        <#assign upper = questions.data.maxPage>
        <#if questions.data.maxPage &gt; 5>
            <#assign lower = questions.data.index>
            <#assign upper = questions.data.index>
            <#list 1..2 as index>
                <#if questions.data.index - index &gt; 0>
                    <#assign lower = lower - 1>
                <#else>
                    <#assign upper = upper + 1>
                </#if>
            </#list>
            <#list 1..2 as index>
                <#if questions.data.index + index <= questions.data.maxPage>
                    <#assign upper = upper + 1>
                <#else>
                    <#assign lower = lower - 1>
                </#if>
            </#list>
        </#if>

        <#list lower..upper as page>
            <a href="${global.applicationContext}/question/category/${tag.name()}?index=${page}" <#if page == questions.data.index>class="active"</#if>> ${page} </a>
        </#list>

        <#if questions.data.maxPage - questions.data.index &gt; 2>
            <a href="${global.applicationContext}/question/category/${tag.name()}?index=${questions.data.index+1}"> > </a>
        </#if>
        <#if questions.data.hasNextPage>
            <a href="${global.applicationContext}/question/category/${tag.name()}?index=${questions.data.maxPage}">末页</a>
        </#if>
    </div>
</div>

</@global.main>