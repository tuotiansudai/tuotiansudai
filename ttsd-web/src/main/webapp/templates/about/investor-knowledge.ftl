<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="网贷课堂" activeLeftNav="出借人教育" title="出借人教育_拓天速贷" keywords="拓天速贷,拓天活动,拓天排行榜,拓天理财,网贷课堂" description="拓天速贷是中国金融行业较为安全的P2P金融平台,全方位报道拓天速贷最新新闻动态,实时推出拓天速贷最新活动以及拓天公告.">
<div class="about-us-container" id="WhetherApp">
    <h2 class="column-title"><em>出借人教育</em></h2>
    <div id="investorList"></div>

    <script type="text/template" id="noticeListTemplate">
        <ul class="notice-list">
            <%
            if(records.length>0){
            for(var i=0,len=records.length; i < len; i++) {
            var item = records[i];
            %>
            <li><i>●</i><a href="/knowledge/<%=item.id%>?subSection=investor"><%=item.title%></a> <span><%=item.updatedTime%></span></li>
            <% }

            }else {
            %>

            <div class="no-data">暂无内容</div>
            <%
            } %>

    </script>

    <div class="pagination" data-url="/knowledge/list"></div>
</div>
</@global.main>