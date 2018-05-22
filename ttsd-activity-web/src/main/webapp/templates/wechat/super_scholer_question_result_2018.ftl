<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.super_scholer_question_result}" pageJavascript="${js.super_scholer_question_result}"  title='学霸升值季，答题赢加"薪"' >

<div class="result-container" data-url="">
    <div class="result-bg">
        <div class="answer-bg">
            <table class="result-table">
                <tr>
                    <td class="first-td">题目</td>
                    <td>1</td>
                    <td>2</td>
                    <td>3</td>
                    <td>4</td>
                    <td>5</td>
                </tr>
                <tr>
                    <td class="first-td">选项</td>
                    <#list userAnswer as answer>
                        <td <#if answer == questionAnswer[answer_index]>class="answer-right"<#else>class="answer-wrong"</#if>>${answer}</td>
                    </#list>
                </tr>
            </table>
            <div class="my-reappearance"><span>我的当前返现比例</span><span class="percent">${rate!}</span></div>
            <div class="today-reward">
                <div class="triangle"></div>
            </div>
            <#if coupon == 495>
                <div class="my-coupon coupon05"></div>
            <#else>
                <div class="my-coupon coupon02"></div>
            </#if>
            <div class="fly">
            </div>
        </div>
        <div class="btn-wrap">
            <div class="bonus-question"></div>
            <a href="javascript:;" id="" class="share-activity invite-more">
                分享活动+0.1%返现
            </a>
            <a href="javascript:;" class="invite-friend invite-more">邀请好友认证+0.2%</a>
       </div>
        <div class="invest-rules">好友投资1000元以上、60天<br/>以上项目再+0.6%返现</div>

</div>
    <a href="/loan-list" class="to-loan"></a>
    <div class="book"></div>
    <div class="arrow"></div>
    <div class="tip-box share-tip" id="shareBox" style="display: none"><a href="javascript:;" class="known-btn share-tip-btn"></a></div>
    <div class="tip-box invite-tip" id="inviteBox" style="display: none"><a href="javascript:;" class="known-btn invite-tip-btn"></a></div>
</div>
<script>
    webServer = '${webServer}';
    commonStaticServer = '${commonStaticServer}';
    shareType = '${shareType!}';
    mobile = '${mobile}';
</script>
</@global.main>