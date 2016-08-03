<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.message_list}" pageJavascript="${js.message_detail}" activeNav="我的账户" activeLeftNav="消息中心" title="消息中心">

<div class="content-container create-transfer-content">
    <h4 class="column-title"><em class="tc">消息中心</em></h4>

    <div class="list-container">
        <div class="global-message-detail">
            <h1>${title}</h1>

            <div class="meta">${createdTime}</div>
            <div class="content">
            ${content}
            </div>
            <div class="go-back">
                <a href="#">返回列表</a>
            </div>
        </div>
    </div>
</div>
</@global.main>