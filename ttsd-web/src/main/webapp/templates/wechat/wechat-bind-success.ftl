<#import "wechat-global.ftl" as global>
<@global.main pageCss="${css.wechat_entry_point}" pageJavascript="${js.wechat_bind_success}">
<div class="weChat-container success-box">
    <span class="result-ok">
        <i class="icon-sucess"></i>
        <em>登录成功</em>
    </span>

    <span class="down-time"><i id="downTime" data-redirect="${redirect!('/')}"></i>后自动跳转</span>
    <button type="button" class="btn-normal" id="okButton">确认</button>
</div>

<script src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script>
    wx.config({
        debug: true,
        appId: '${(wxConfig.appId)!}',
        timestamp: '${(wxConfig.timestamp)!}',
        nonceStr: '${(wxConfig.nonceStr)!}',
        signature: '${(wxConfig.signature)!}',
        jsApiList: [
            'onMenuShareAppMessage',
            'onMenuShareTimeline'
        ]
    });
    wx.ready(function () {
        wx.onMenuShareTimeline({
            title: '互联网之子',
            link: 'http://192.168.60.159',
            imgUrl: 'http://demo.open.weixin.qq.com/jssdk/images/p2166127561.jpg',
            trigger: function (res) {
                // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
                alert('用户点击分享到朋友圈');
            },
            success: function (res) {
                console.log('已分享');
            },
            cancel: function (res) {
                console.log('已取消');
            },
            fail: function (res) {
                alert(JSON.stringify(res));
            }
        });
    });

</script>
</@global.main>