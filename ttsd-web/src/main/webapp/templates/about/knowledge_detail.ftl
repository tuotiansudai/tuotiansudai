<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="信息披露" activeLeftNav="网贷知识" title="网贷知识详情">
    <div class="about-us-container" id="noticeDetail">
        <div class="crumb-lead" id="WhetherApp">
            <a href="/about/company">关于我们</a> > <a href="/about/knowledge">网贷知识</a>
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