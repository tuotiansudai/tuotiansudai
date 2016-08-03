<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.message_list}" pageJavascript="${js.message_list}" activeNav="我的账户" activeLeftNav="消息中心" title="消息中心">

<div class="content-container create-transfer-content">
    <h4 class="column-title"><em class="tc">消息中心</em></h4>

    <div class="list-container">
        <div class="global-message-list active">

        </div>
        <div class="pagination" data-url="/message/user-message-list-data" data-page-index="1"
             data-page-size="10"></div>
    </div>
</div>

</@global.main>
