<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.super_scholar_2018}" pageJavascript="${js.super_scholar_2018}" activeNav="" activeLeftNav="" title="加息不打烊_活动中心_拓天速贷" keywords="拓天速贷,开工红包,实物奖励,全场加息,体验金奖励" description='拓天速贷加息不打烊活动,活动期间微信关注"拓天速贷服务号"即可领取520元开工红包,周五投资额外享受年化投资额的0.5%返现奖励,用户累计年化投资可兑换实物奖励与体验金奖励.'>
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
        <a href="javascript:;" class="question-btn"></a>

    </div>
    {{/each}}
    {{/if}}
</script>
</@global.main>