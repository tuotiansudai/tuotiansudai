<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.super_scholer_question_result}" pageJavascript="${js.super_scholer_question_result}"  title="恭喜你！领到380元红包">

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
    <input id="webServer" type="hidden" value="${webServer}">
</div>
<script>
    var shareUrl;
    var $shareActivity = $('.share-activity'),
            $inviteFriend = $('.invite-friend');

    var webServer = $('#webServer').val();

    $shareActivity.on('click',function () {
        shareUrl = webServer+'/activity/super-scholar?come=wechat';
    })
    $inviteFriend.on('click',function () {
        shareUrl = webServer+'/activity/super-scholar/share/register?come=wechat';
    })
alert(shareUrl)
    wx.ready(function () {
        wx.onMenuShareAppMessage({
            title: '我在拓天速贷答题赢加薪，邀请你来测一测学霸指数', // 分享标题
            desc: '你是学霸还是学渣？答题见分晓！', // 分享描述
            link: shareUrl, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/2018/super-scholar/icon_red_ware.png', // 分享图标
            success: function () {
                // ajax
                commonFun.useAjax({
                    dataType: 'json',
                    url: '/activity/super-scholar/share/success',
                    type: 'post'

                }, function (response) {

                })
            },
            cancel: function () {
            }
        });

        wx.onMenuShareTimeline({
            title: '我在拓天速贷答题赢加薪，邀请你来测一测学霸指数', // 分享标题
            desc: '你是学霸还是学渣？答题见分晓！', // 分享描述
            link: '${webServer}/activity/super-scholar?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images//2018/super-scholar/icon_red_ware.png', // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                commonFun.useAjax({
                    dataType: 'json',
                    url: '/activity/super-scholar/share/success',
                    type: 'post'

                }, function (response) {

                })
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
    });
</script>
</@global.main>