<#import "macro/global.ftl" as global>
<@global.main pageCss="${(css.main)!'main.css'}" pageJavascript="${(js.main)!'main.js'}">
<div class="article-content fl answer-container">
    <div class="borderBox clearfix">
        <div class="answers-box">
            <dl class="answers-list">
                <dt>${question.question}</dt>
                <dd><span>${question.mobile}</span>
                    <span>回答：${question.answers}</span>
                    <span class="datetime">${question.createdTime?string("yyyy-MM-dd HH:mm")}</span> <br/>

                </dd>
                <dd class="tag-answer">
                    <#list question.tags as tag>
                        <span class="tag">${tag}</span>
                    </#list>
                <dd>${question.addition!}</dd>
            </dl>
        </div>
    </div>

    <#--ad-->
    <div class="ad-answer"><img src="${staticServer}/images/sign/ad-answer.jpg"></div>

    <div class="borderBox clearfix margin-top-10">
        <div class="answers-box ">
            <div class="other-title">共${question.answers}个回答</div>
            <dl class="answers-list">
                <dd>这个平台活动好多啊！！！对拓天速贷近一年的经营数据进行了详细分析，平台房产抵押借贷表现如下：以房产作抵押标的项目达93个，占平台项目总数的67%。;涉及金额占平台总金额的87%。</dd>
                <dd class="date-time-answer"><span>miaojiahang</span>
                    <span class="answerNum">回答：2</span>
                    <span class="datetime">2016-05-30 11:43</span>
                    <span class="btn fr">采纳此条信息</span>
                </dd>
            </dl>
            <dl class="answers-list">
                <dd>这个平台活动好多啊！！！对拓天速贷近一年的经营数据进行了详细分析，平台房产抵押借贷表现如下：以房产作抵押标的项目达93个，占平台项目总数的67%。;涉及金额占平台总金额的87%。</dd>
                <dd class="date-time-answer"><span>miaojiahang</span>
                    <span class="answerNum">回答：2</span>
                    <span class="datetime">2016-05-30 11:43</span>
                    <span class="btn fr">采纳此条信息</span>
                </dd>
            </dl>
        </div>
    </div>
</div>
</@global.main>