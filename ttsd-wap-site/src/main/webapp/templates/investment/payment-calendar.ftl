<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'payment_calendar' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.payment_calendar}" pageJavascript="${js.payment_calendar}" title="回款日历">

<div class="my-account-content payment-calendar"  id="paymentCalendar">

    <div id="calendarTree"></div>

    <div class="payment-section-info"></div>
    <script type="text/template" id="paymentTpl">
        <div class="payment-box">
        <span>
            <em><%=fund%> </em>
            <%=month%>月预计待收款(元)
        </span>
            <span>
            <em><%=finish%></em>
            <%=month%>月已回款(元)
        </span>
        </div>

        <div class="payment-total">
            <span><%=year%>年<%=month%>月<%=day%></span>
            <em class="key">共<%=total%>元</em>
        </div>
        <% if(count==0) { %>

        <div class="no-result">
        本日无回款项目
        </div>

        <% } else { %>

        <div class="payment-info">
            <%
            for(var i =0;i<count;i++) {
            var item = list[i];
            %>
            <span>
            <%=item.name%> <br/>
            回款金额：<em class="key"> <%=item.amount%>元</em>
        </span>
            <span><%=item.status%></span>

            <% } %>
        </div>
        <% } %>
    </script>

</div>

</@global.main>
