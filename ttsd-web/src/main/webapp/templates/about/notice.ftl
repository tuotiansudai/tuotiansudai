<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="信息披露" activeLeftNav="拓天公告" title="拓天公告_拓天活动_拓天速贷" keywords="拓天速贷,拓天活动,拓天排行榜,拓天理财" description="拓天速贷是中国金融行业较为安全的P2P金融平台,全方位报道拓天速贷最新新闻动态,实时推出拓天速贷最新活动以及拓天公告.">
    <div class="about-us-container" id="WhetherApp">
        <h2 class="column-title"><em>拓天公告</em></h2>
        <div id="noticeList"></div>

        <script type="text/template" id="noticeListTemplate">
        	<ul class="notice-list">
                <% for(var i=0,len=records.length; i < len; i++) {
                    var item = records[i];
                %>
			    <li><i>●</i><a href="/announce/<%=item.id%>"><%=item.title%></a> <span><%=item.updateTime%></span></li>
                <% } %>
			</ul>
        </script>

        <div class="pagination" data-url="/announce/list"></div>
    </div>
</@global.main>