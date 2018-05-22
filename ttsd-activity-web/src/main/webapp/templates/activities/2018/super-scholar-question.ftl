<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.super_scholar_2018}" pageJavascript="${js.super_scholar_2018}" activeNav="" activeLeftNav="" title='学霸升值季，答题赢加"薪"' keywords="拓天速贷,答题赢加薪,邀请好友,返现奖励" description='拓天速贷答题赢加"薪"活动,用户每日答题,投资可获得年化0.2%-0.6%返现奖励,完成答题赠送最高0.5%加息券,分享答题、邀请好友注册、投资还可增加年化返现,返现+加息高至2%.'>
<div class="question-container">
    <div class="question-head">
    </div>
    <div class="question-wrap" id="questionList">

    </div>
    <div class="question-bot">
        <div class="arrow"></div>
        <div class="book"></div>
        <div class="desk"></div>
    </div>
    <input id="isDoneQuestion" type="hidden" value="${doQuestion?c}">
</div>
<script type="text/html" id="questionTpl">
    {{if questions}}
    {{each questions as value index}}
    <div class="question-inner inner{{index+1}}">
        <h2 class="title">{{index+1}}、 {{value.question}}</h2>
        <ul  class="question-list">
            {{each value.options as item i}}
            <li><span class="hight-light"></span>{{item}}</li>
            {{/each}}
        </ul>
        <a href="javascript:;" class="question-btn"></a>{{index}}
        <div class="page-num">{{index}}/5</div>

    </div>
    {{/each}}
    {{/if}}
</script>
</@global.main>