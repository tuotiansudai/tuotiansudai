<#import "../macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.loan_knowledge}" pageJavascript="${m_js.loan_knowledge}" activeNav="信息披露" activeLeftNav="网贷知识" title="网贷知识_拓天速贷" keywords="拓天速贷,拓天活动,拓天排行榜,拓天理财,网贷知识" description="拓天速贷是中国金融行业较为安全的P2P金融平台,全方位报道拓天速贷最新新闻动态,实时推出拓天速贷最新活动以及拓天公告.">
<div class="knowledge-container">
    <div class="knowledge-banner">

    </div>
    <div class="title-section" id="knowledgeTitle">
        <div class="wrap"><span class="active first" data-category="ALL">全部</span><span
                data-category="LAW_RULE">法律法规</span><span data-category="INVESTOR_EDUCATION">出款人教育</span><span
                class="last" data-category="BASIC_KNOWLEDGE">基础知识</span></div>
    </div>
    <div id="wrapperOut" class="knowledge-list-frame">
        <div class="knowledge-content">
            <div id="knowledgeList" class="knowledges-list">

            </div>
            <div id="pullUp" style="display: none">
                <span class="pullUpLabel">上拉加载更多</span>
            </div>
            <div id="noData" style="display: none">
                <span class="pullUpLabel">没有更多数据了</span>
            </div>
        </div>
    </div>

    <script type="text/template" id="knowledgeListTemplate">
        <%if(records.length>0){
        %>
        <% for(var i=0,len=records.length; i < len; i++) {
        var item = records[i];
        %>
        <a href="/m/knowledge/<%=item.id%>">
            <dl class="knowledge-item clearfix">
                <dt><img src="<%=item.thumb%>" alt=""></dt>
                <dd><%=item.title%></dd>
                <dd class="date-time"><%=item.updatedTime%></dd>
            </dl>
        </a>

        <% } %>
        <%
        }%>


    </script>

    <#if !isAppSource>
        <div class="knowledge-list">
            <div class="footer-wap-container">
                <a class="menu-home" href="/m">
                    <i></i>
                    <span>首页</span>
                </a>
                <a class="menu-invest" href="/m/loan-list">
                    <i></i>
                    <span>投资</span>
                </a>
                <a class="menu-knowledge current" href="/m/about/knowledge">
                    <i></i>
                    <span>发现</span>
                </a>
                <a class="menu-my" href="/m/account">
                    <i></i>
                    <span>我的</span>
                </a>
            </div>
        </div>
    </#if>

</div>
</@global.main>