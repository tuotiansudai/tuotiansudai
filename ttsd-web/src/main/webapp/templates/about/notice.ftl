<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="${js.notice}" activeNav="关于我们" activeLeftNav="拓天公告" title="拓天公告列表">
    <div class="about-us-container">
        <h2 class="a-title"><span>拓天公告</span></h2>
        <div id="noticeList"></div>
        <div class="pagination" data-url="/announce/list"></div>
    </div>
</@global.main>