<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.message_list}" pageJavascript="${js.message_list}" activeNav="我的账户" activeLeftNav="消息中心" title="消息中心">

<div class="content-container create-transfer-content" id="userMessageList">
    <h4 class="column-title"><em class="tc">消息中心</em></h4>

    <div class="list-container">
        <div class="global-message-list active">

        </div>
        <div class="pagination" data-url="/message/user-message-list-data" data-page-index="1"
             data-page-size="10"></div>
    </div>
</div>


<script type="text/template" id="messageListTemplate">
    <table class="table-striped transfer-list">
        <thead>
        <tr>
            <th>来源</th>
            <th>标题</th>
            <th>时间</th>
        </tr>
        </thead>
        <tbody>
        <% for(var i = 0; i < records.length; i++) {
        var item = records[i];
        var category=item.messageCategory,
            messageCategoryTitle;
        if(category=='ACTIVITY') {
            messageCategoryTitle='活动通知';
        }
        else if(category=='NOTIFY') {
            messageCategoryTitle='拓天公告';
        }
        else {
            messageCategoryTitle='系统消息';
        }
        %>
        <tr <%=item.read?'':'class="unread"'%>>
        <td><span class="icon">【<%=messageCategoryTitle%>】</span></td>

        <td>
            <em class="jump-detail" data-url="/message/user-message/<%=item.userMessageId%>#<%=item.index%>">
                <%=item.title%>
            </em>

        </td>
        <td><%=item.createdTime%></td>
        </tr>
        <% } %>

        <%=records?'':'<tr><td colspan="3" class="no-data">暂时没有记录</td></tr>'%>
        </tbody>
    </table>
    <p class="page-count-bottom"><button class="read-all-messages">全部标记为已读</button></p>
</script>
</@global.main>
