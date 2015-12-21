<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="" activeNav="关于我们" activeLeftNav="拓天公告" title="拓天公告详情">
    <div class="about-us-container" id="noticeDetail">
        <div class="crumb-lead">
           <a href="/about/company">关于我们</a> > <a href="/about/notice">拓天公告</a>
        </div>
        <h2 class="column-title article">
            <em class="title">${announce.title}</em>
            <time class="tr">发表时间：<i>${(announce.updateTime?string("yyyy-MM-dd"))!}</i></time>
        </h2>
        <div class="detail-content">
            ${announce.content}
        </div>

    <footer class="fr">
        拓天速贷客服中心
        <br/>
        <span class="update-time"> ${(announce.updateTime?string("yyyy年MM月dd日"))!}</span></footer>
    </div>

</@global.main>