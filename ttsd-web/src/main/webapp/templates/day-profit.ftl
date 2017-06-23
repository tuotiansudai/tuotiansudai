<#import "macro/global-dev.ftl" as global>
<#assign jsName = 'day_profit' >

<#assign js = {"${jsName}":"http://localhost:3008/web/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/web/js/account/${jsName}.css"}>

<@global.main pageCss="${css.day_profit}" pageJavascript="${js.day_profit}" activeNav="我的账户" activeLeftNav="资金管理" title="日息宝收益">
<div class="content-container user-bill-list-content">
    <h4 class="column-title"><em>日息宝收益</em></h4>
    <div class="borderBox">
        <p class="text-item">累计收益（元）：<span>45.15</span></p>
    </div>
<div class="clear-blank-m"></div>
    <div class="item-block date-filter ">
        <span class="sub-hd">起止时间:</span>
        <input type="text" id="date-picker" class="input-control" size="35"/>
        <span class="select-item current" data-day="1">今天</span>
        <span class="select-item" data-day="7">最近一周</span>
        <span class="select-item" data-day="30">一个月</span>
        <span class="select-item" data-day="180">六个月</span>
        <span class="select-item" data-day="">全部</span>
    </div>

    <div class="clear-blank"></div>
    <table class="user-bill-list table-striped"></table>
    <div class="pagination" data-url="/user-bill/user-bill-list-data" data-page-size="10"></div>
</div>

<script type="text/template" id="userBillTableTemplate">
    <table class="user-bill-list">
        <thead>
        <tr>
            <th>当日收益（元）</th>
            <th>时间</th>
        </tr>
        </thead>
        <tbody>
        <% for(var i = 0; i < records.length; i++) {
        var item = records[i];
        %>
        <tr>
            <td><%=item.createdTime%></td>
            <td><%=item.businessType%></td>
        </tr>
        <% } %>
        <%=records.length?'':'<tr><td colspan="7" class="no-data">暂时没有投资记录</td></tr>'%>
        </tbody>
    </table>
</script>
</@global.main>