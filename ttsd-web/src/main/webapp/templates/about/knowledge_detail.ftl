<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="网贷课堂" activeLeftNav="" title="网贷课堂">
    <div class="about-us-container" id="noticeDetail">
        <div class="crumb-lead" id="WhetherApp">
            <a href="/about/knowledge">网贷课堂</a> > <span id="knowledgeTitle"></span>
        </div>
        <h2 class="column-title article">
            <em class="title">${knowledge.title}</em>
            <span class="tr">发表时间：<i>${(knowledge.updatedTime?string("yyyy-MM-dd"))!}</i></span>
        </h2>
        <div class="detail-content">
            ${knowledge.content}
        </div>

        <footer class="fr">
            拓天速贷运营中心
            <br/>
            <span class="update-time"> ${(knowledge.updatedTime?string("yyyy年MM月dd日"))!}</span></footer>
    </div>

</@global.main>