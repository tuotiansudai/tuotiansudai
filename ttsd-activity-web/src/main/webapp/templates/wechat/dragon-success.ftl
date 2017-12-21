<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.dragon_success}" pageJavascript="${js.dragon_success}"  title="助力好友抢红包" >

<div class="wechat-invite-container" id="wechatInvite">
    <div class="result-container">
        <h3 class="title-item">
            <#if fetchResult == 1>
                领取成功！
            <#elseif fetchResult == 0>
                您已经领取过这个红包了哦~
            <#elseif fetchResult == -1>
                活动已结束。
            </#if>
        </h3>
        <div class="take-red">
            <p class="red-bag">
                <span class="money-number"><strong>10</strong>元</span>
                <span class="money-type">投资红包</span>
            </p>
            <p class="tip-text">投资红包已发放到您的账户</p>
            <p class="wechat-code"></p>
            <p>关注拓天速贷服务号，<br/>参加端午节活动天天都有奖励哦~</p>
        </div>
    </div>
</div>
</@global.main>