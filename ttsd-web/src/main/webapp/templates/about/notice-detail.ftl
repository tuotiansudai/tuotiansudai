<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="" activeNav="关于我们" activeLeftNav="拓天公告" title="拓天公告详情">
    <div class="about-us-container" id="noticeDetail">
        <div class="crumb-lead">
            <a href="/"> 首页</a> > <a href="/about/company">关于我们</a> > 拓天公告
        </div>
        <h2 class="page-col-title article">
            <span class="title">${announcement.title}</span>
            <time class="tr">发表时间：<i>${(announcement.updateTime?string("yyyy-MM-dd"))!}</i></time>
        </h2>
        <div class="detail-content">
            ${announcement.content}
        </div>

    <footer class="fr">
        拓天速贷客服中心
        <br/>
        <span class="update-time"> ${(announcement.updateTime?string("yyyy年MM月dd日"))!}</span></footer>
    </div>

</@global.main>