<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.super_scholer_question_result}" pageJavascript="${js.super_scholer_question_result}"  title="恭喜你！领到380元红包">

<div class="result-container">
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
                        <td>${answer}</td>
                    </#list>
                </tr>
            </table>
            <div class="my-reappearance"><span>我的当前返现比例</span><span class="percent">${rate!}</span></div>
            <div class="today-reward">
                <div class="triangle"></div>
            </div>
            <div class="my-coupon coupon05"></div>
            <div class="my-coupon coupon02"></div>
            <div class="fly">
            </div>
        </div>
        <div class="btn-wrap">
            <div class="bonus-question"></div>
            <a href="javascript:;" class="share-activity invite-more">

            </a>
            <a href="javascript:;" class="invite-friend invite-more"></a>
       </div>
        <div class="invest-rules">好友投资1000元以上、60天<br/>以上项目再+0.6%返现</div>

</div>
    <a href="/loan-list" class="to-loan"></a>
    <div class="book"></div>
    <div class="arrow"></div>
</div>


<#--<#include "../module/login-tip.ftl" />-->
<script>
    wx.ready(function () {
        wx.onMenuShareAppMessage({
            title: '春风十里，送你380元投资礼包', // 分享标题
            desc: '春眠不觉晓，红包要收好', // 分享描述
            link: '${webServer}/activity/spring-breeze/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标
            success: function () {
            },
            cancel: function () {
            }
        });

        wx.onMenuShareTimeline({
            title: '恭喜你！领到380元红包', // 分享标题
            desc: '春眠不觉晓，红包要收好', // 分享描述
            link: '${webServer}/activity/spring-breeze/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
    });
</script>
</@global.main>