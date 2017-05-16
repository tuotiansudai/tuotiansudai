<#import "wechat-global.ftl" as global>
<@global.main pageCss="${css.midsummer_wap}" pageJavascript="${js.midsummer_wap}"  title="助力好友抢红包" >
<#assign invitedMap = { "0": 20000, "1":10000, "2":5000, "3":2000} >

<div class="invite-header" id="inviteHeader" data-mobile="${mobile}">
<#--如果是自己打开页面-->
    <span class="invite-text">
        <#if sharedUser>
            <i class="login-mobile">${mobile}</i>
            恭喜您获得了<b>50</b>元红包
        <#else>
            <i>您的好友${mobile[0..2]}****${mobile[7..]}</i>
            获得了<b>50</b>元红包
        </#if>
    </span>
</div>

<div class="invite-friend-container" id="inviteBoxFriend">

    <input type="hidden" value="${invitedCount}" class="invite-number">
    <div class="temperature-box">
        <div class="temp-out-square">
            <div class="temp-inner-square">
                <span class="temp-number">0</span>
                <sub>C</sub>
            </div>
        </div>

        <div class="temp-progress-box">
            <div class="temp-progress"></div>
            <div class="temp-scale-line"></div>
        </div>
        <div class="unit">（单位℃）</div>
    </div>

    <div class="invest-detail-box">
        <p>红包当前起投金额：<i class="from-invest"><#if 4 <= invitedCount>50<#else>${invitedMap[invitedCount?string]}</#if></i>元</p>
        <p>点击按钮邀请好友助力升温，好友注册并累计投资200元即可降低红包起投金额，最低降至50元起投！</p>

        <table class="table-invest">
            <tr>
                <th width="25%">邀请人数</th>
                <th width="15%">0</th>
                <th width="15%">1</th>
                <th width="15%">2</th>
                <th width="15%">3</th>
                <th width="15%">4</th>
            </tr>
            <tr>
                <td>起投金额</td>
                <td>20000</td>
                <td>10000</td>
                <td>5000</td>
                <td>2000</td>
                <td>50</td>
            </tr>
        </table>
    </div>

    <div class="button-layer-bottom clearfix" id="buttonLayer">
        <#if sharedUser>
            <a href="javascript:void(0)" class="btn-normal btn-invite fl">邀请微信好友</a>
            <a href="javascript:void(0)" class="btn-normal btn-invite fr">分享至朋友圈</a>
        <#else>
            <a href="javascript:void(0)" class="btn-normal btn-help">帮助TA</a>
        </#if>
    </div>

</div>

<div class="note-box clearfix">
    <div class="note-inner">
        <b>温馨提示：</b>
        <span>
            1.活动时间：5月18日-5月31日 <br/>
            2.好友在活动期间内完成注册，并完成除体验项目及债权转让项目外的200元以上投资，即为助力成功； <br/>
            3.50元红包将于活动结束后三个工作日统一发放，用户可在PC端“我的账户”或App端“我的”中进行查看； <br/>
            4.本活动仅限直投项目，债权转让及体验项目不参与累计； <br/>
            5.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格； <br/>
            6.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。 <br/>
        </span>
    </div>

</div>

<div class="floating-box" id="FloatingBox" style="display: none">
    <i class="icon-arrow"></i>
    <span class="text">
        点击右上角， <br/>
将页面分享给好友即可完成操作
    </span>
    <button class="btn-close" type="button">我知道了</button>
</div>

<script>
    wx.ready(function () {
//        分享给朋友
        wx.onMenuShareAppMessage({
            title: '是朋友就帮我一起把夏季燃爆', // 分享标题
            desc: '领取投资红包50元，最低50元起投', // 分享描述
            link: '${webServer}/activity/mid-summer/invited-user?mobile=${mobile}', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/logo-tip.png', // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                $('#FloatingBox').hide();
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                $('#FloatingBox').hide();
            }
        });

//        分享到朋友圈
        wx.onMenuShareTimeline({
            title: '是朋友就帮我一起把夏季燃爆', // 分享标题
            link: '${webServer}/activity/mid-summer/invited-user?mobile=${mobile}', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/logo-tip.png', // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                $('#FloatingBox').hide();
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                $('#FloatingBox').hide();
            }
        });
    });
</script>
</@global.main>